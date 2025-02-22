import os
import re
import logging
import argparse
import subprocess

logging.basicConfig(level=logging.INFO, format='%(levelname)s: %(message)s')

JAVA_START_MARKER_RE = re.compile(r'//--- START FILE: (.+) \(JAVA\) - (CREATE|UPDATE|REMOVE) ---')
JAVA_END_MARKER_RE = re.compile(r'//--- END FILE: (.+) - (CREATE|UPDATE|REMOVE) ---')
XML_START_MARKER_RE = re.compile(r'<!----- START FILE: (.+) \(XML\) - (CREATE|UPDATE|REMOVE) --- -->')
XML_END_MARKER_RE = re.compile(r'<!----- END FILE: (.+) - (CREATE|UPDATE|REMOVE) --- -->')
EXPLANATIONS_SEPARATOR_RE = re.compile(r'### EXPLANATIONS ###')
NO_CHANGES_RE = re.compile(r'No changes to: (.+)')

DEFAULT_MAVEN_HOME = r"F:\\app\\apache-maven-3.9.9"  # Update this to your Maven path

def parse_gemini_response(response_text):
    """Parses Gemini's response text to extract file modifications and explanations."""
    modifications = {}
    removals = []
    creations = {}
    explanations = None
    current_file_path = None
    current_action = None
    file_content_lines = []
    in_explanations_section = False
    no_changes_files = []

    for line in response_text.splitlines():
        if in_explanations_section:
            explanations = (explanations or "") + line + "\n"
            continue

        start_match = (JAVA_START_MARKER_RE.match(line) or
                       XML_START_MARKER_RE.match(line))
        end_match = (JAVA_END_MARKER_RE.match(line) or
                     XML_END_MARKER_RE.match(line))

        if start_match:
            current_file_path = os.path.normpath(start_match.group(1))
            current_action = start_match.group(2)
            file_content_lines = []
        elif end_match:
            ended_file_path = os.path.normpath(end_match.group(1))
            ended_action = end_match.group(2)

            if current_file_path == ended_file_path and current_action == ended_action:
                file_content = "".join(file_content_lines).rstrip("\n")
                if current_action == "UPDATE":
                    modifications[current_file_path] = file_content
                elif current_action == "CREATE":
                    creations[current_file_path] = file_content
                elif current_action == "REMOVE":
                    removals.append(current_file_path)
                    if file_content:
                        logging.warning(f"Content ignored for REMOVE action in: {current_file_path}")
            else:
                logging.warning(f"Marker mismatch: Start({current_file_path}/{current_action}) vs End({ended_file_path}/{ended_action})")

            current_file_path = None
            current_action = None
            file_content_lines = []
        elif EXPLANATIONS_SEPARATOR_RE.match(line):
            in_explanations_section = True
            explanations = ""
        elif NO_CHANGES_RE.match(line):
            no_changes_files.append(os.path.normpath(NO_CHANGES_RE.match(line).group(1)))
        elif current_file_path and current_action != "REMOVE":
            file_content_lines.append(line + "\n")

    return modifications, removals, creations, no_changes_files, explanations

def normalize_lines(content):
    """Normalizes file content for comparison."""
    return [line.strip() for line in content.splitlines() if line.strip()]

def apply_modifications(modifications, removals, creations, project_dir):
    """Applies file changes and runs Maven build with proper environment setup."""
    # Set up Maven paths
    maven_home = os.environ.get("MAVEN_HOME") or DEFAULT_MAVEN_HOME
    maven_bin = os.path.join(maven_home, "bin")
    mvn_cmd = os.path.join(maven_bin, "mvn.cmd" if os.name == "nt" else "mvn")

    # Verify Maven installation
    try:
        subprocess.run([mvn_cmd, "-v"], check=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        logging.info("Maven verification successful")
    except (subprocess.CalledProcessError, FileNotFoundError) as e:
        logging.error(f"Maven verification failed: {str(e)}")
        return

    # Apply file operations
    try:
        for action_name, items in [
            ("Updating", modifications.items()),
            ("Creating", creations.items()),
            ("Removing", [(f, None) for f in removals])
        ]:
            for file_path, content in items:
                full_path = os.path.join(project_dir, file_path)
                if action_name == "Removing":
                    if os.path.exists(full_path):
                        os.remove(full_path)
                        logging.info(f"Removed: {file_path}")
                    else:
                        logging.warning(f"File not found for removal: {file_path}")
                else:
                    os.makedirs(os.path.dirname(full_path), exist_ok=True)
                    if action_name == "Updating":
                        with open(full_path, "r", encoding="utf-8") as f:
                            existing = normalize_lines(f.read())
                        if existing == normalize_lines(content):
                            logging.info(f"No changes needed: {file_path}")
                            continue
                    with open(full_path, "w", encoding="utf-8") as f:
                        f.write(content)
                    logging.info(f"{action_name[:-3]}ed: {file_path}")
    except Exception as e:
        logging.error(f"File operation failed: {str(e)}")
        return

    # Run Maven build with proper environment
    try:
        env = os.environ.copy()
        env["PATH"] = f"{maven_bin}{os.pathsep}{env['PATH']}"

        result = subprocess.run(
            [mvn_cmd, "clean", "install"],
            cwd=project_dir,
            env=env,
            check=True,
            text=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT
        )
        logging.info("Maven build succeeded\n" + result.stdout)
    except subprocess.CalledProcessError as e:
        error_msg = f"Maven build failed (code {e.returncode}):\n{e.stdout}"
        logging.error(error_msg)
        with open(os.path.join(project_dir, "build_error.txt"), "w") as f:
            f.write(error_msg)

def main():
    parser = argparse.ArgumentParser(description="Apply code changes and run Maven build")
    parser.add_argument("response_file", help="Path to Gemini response file")
    parser.add_argument("project_dir", help="Project root directory")
    parser.add_argument("--explanations", help="Output file for explanations")

    args = parser.parse_args()

    try:
        with open(args.response_file, "r", encoding="utf-8") as f:
            response = f.read()
    except Exception as e:
        logging.error(f"Failed to read response file: {str(e)}")
        return

    changes = parse_gemini_response(response)
    apply_modifications(*changes[:3], os.path.normpath(args.project_dir))

    if changes[3]:
        logging.info(f"No changes indicated for: {', '.join(changes[3])}")

    if changes[4] and args.explanations:
        try:
            with open(args.explanations, "w", encoding="utf-8") as f:
                f.write(changes[4])
            logging.info(f"Explanations saved to {args.explanations}")
        except Exception as e:
            logging.error(f"Failed to save explanations: {str(e)}")

if __name__ == "__main__":
    main()