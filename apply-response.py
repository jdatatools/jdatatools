import os
import re
import logging
import argparse
import subprocess
from datetime import datetime

logging.basicConfig(level=logging.INFO, format='%(levelname)s: %(message)s')

# Regex patterns for parsing Gemini response
JAVA_START_MARKER_RE = re.compile(r'//--- START FILE: (.+) \(JAVA\) - (CREATE|UPDATE|REMOVE) ---')
JAVA_END_MARKER_RE = re.compile(r'//--- END FILE: (.+) - (CREATE|UPDATE|REMOVE) ---')
XML_START_MARKER_RE = re.compile(r'<!----- START FILE: (.+) \(XML\) - (CREATE|UPDATE|REMOVE) --- -->')
XML_END_MARKER_RE = re.compile(r'<!----- END FILE: (.+) - (CREATE|UPDATE|REMOVE) --- -->')
EXPLANATIONS_SEPARATOR_RE = re.compile(r'### EXPLANATIONS ###')
NO_CHANGES_RE = re.compile(r'No changes to: (.+)')

DEFAULT_MAVEN_HOME = r"F:\\app\\apache-maven-3.9.9"

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

        # CORRECTED LINE WITH PROPER PARENTHESIS
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
                file_content = "\n".join(file_content_lines)
                if current_action == "UPDATE":
                    modifications[current_file_path] = file_content
                elif current_action == "CREATE":
                    creations[current_file_path] = file_content
                elif current_action == "REMOVE":
                    removals.append(current_file_path)
            current_file_path = None
            current_action = None
            file_content_lines = []
        elif EXPLANATIONS_SEPARATOR_RE.match(line):
            in_explanations_section = True
            explanations = ""
        elif NO_CHANGES_RE.match(line):
            no_changes_files.append(os.path.normpath(NO_CHANGES_RE.match(line).group(1)))
        elif current_file_path and current_action != "REMOVE":
            file_content_lines.append(line)

    return modifications, removals, creations, no_changes_files, explanations

def apply_modifications(modifications, removals, creations, project_dir, verbose=False):
    """Applies file changes and runs Maven build with enhanced reporting."""
    maven_home = os.environ.get("MAVEN_HOME") or DEFAULT_MAVEN_HOME
    maven_bin = os.path.join(maven_home, "bin")
    mvn_cmd = os.path.join(maven_bin, "mvn.cmd" if os.name == "nt" else "mvn")
    build_report_path = os.path.join(project_dir, "build_report.txt")

    # File operations
    try:
        for action, items in [("UPDATE", modifications), ("CREATE", creations), ("REMOVE", removals)]:
            for path in (items if isinstance(items, list) else items.keys()):
                full_path = os.path.join(project_dir, path)
                if verbose:
                    logging.debug(f"Processing {action} for {full_path}")

                if action == "REMOVE":
                    if os.path.exists(full_path):
                        os.remove(full_path)
                        logging.info(f"Removed: {path}")
                    else:
                        logging.warning(f"File not found: {path}")
                else:
                    content = items[path]
                    os.makedirs(os.path.dirname(full_path), exist_ok=True)

                    if action == "UPDATE" and os.path.exists(full_path):
                        with open(full_path, "r", encoding="utf-8") as f:
                            if f.read() == content:
                                if verbose:
                                    logging.debug(f"No changes needed: {path}")
                                continue

                    with open(full_path, "w", encoding="utf-8") as f:
                        f.write(content)
                    logging.info(f"{action}d: {path}")

    except Exception as e:
        logging.error(f"File operation failed: {str(e)}")
        return

    # Maven build execution
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

        # Successful build report
        with open(build_report_path, "w") as f:
            f.write(f"build successful {datetime.now().isoformat()}\n")
            f.write(result.stdout)

        logging.info(f"Maven build succeeded - report saved to {build_report_path}")

    except subprocess.CalledProcessError as e:
        # Failed build report
        with open(build_report_path, "w") as f:
            f.write(f"BUILD FAILED ({datetime.now().isoformat()})\n")
            f.write(e.stdout)

        logging.error(f"Maven build failed (code {e.returncode})")
        if verbose:
            logging.error(f"Error details:\n{e.stdout}")
        logging.error(f"Build report saved to {build_report_path}")

def main():
    parser = argparse.ArgumentParser(description="Code Modification and Build System")
    parser.add_argument("response_file", help="Path to Gemini response file")
    parser.add_argument("project_dir", help="Project root directory")
    parser.add_argument("--explanations", help="Output file for explanations")
    parser.add_argument("-v", "--verbose", action="store_true", help="Enable verbose output")

    args = parser.parse_args()
    logging.getLogger().setLevel(logging.DEBUG if args.verbose else logging.INFO)

    try:
        with open(args.response_file, "r", encoding="utf-8") as f:
            changes = parse_gemini_response(f.read())
    except Exception as e:
        logging.error(f"Failed to process response file: {str(e)}")
        return

    apply_modifications(*changes[:3], os.path.normpath(args.project_dir), args.verbose)

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