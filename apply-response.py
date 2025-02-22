import os
import re
import logging
import argparse

logging.basicConfig(level=logging.INFO, format='%(levelname)s: %(message)s')

JAVA_START_MARKER_RE = re.compile(r'//--- START FILE: (.+) \(JAVA\) - (CREATE|UPDATE|REMOVE) ---') # Capture action
JAVA_END_MARKER_RE = re.compile(r'//--- END FILE: (.+) - (CREATE|UPDATE|REMOVE) ---')       # Capture action
XML_START_MARKER_RE = re.compile(r'<!----- START FILE: (.+) \(XML\) - (CREATE|UPDATE|REMOVE) --- -->') # Capture action
XML_END_MARKER_RE = re.compile(r'<!----- END FILE: (.+) - (CREATE|UPDATE|REMOVE) --- -->')         # Capture action
EXPLANATIONS_SEPARATOR_RE = re.compile(r'### EXPLANATIONS ###')
NO_CHANGES_RE = re.compile(r'No changes to: (.+)')

def parse_gemini_response(response_text):
    """
    Parses Gemini's response to extract file modifications, removals, creations, no-changes, and explanations.

    Returns:
        tuple: (modifications, removals, creations, no_changes_files, explanations)
            - modifications (dict): {filepath: content} for updates.
            - removals (list): List of file paths to remove.
            - creations (dict): {filepath: content} for new files.
            - no_changes_files (list): List of file paths with "No changes" indication.
            - explanations (str, optional): Explanations from Gemini.
    """
    modifications = {}
    removals = []
    creations = {}
    explanations = None
    current_file_path = None
    current_action = None # To track CREATE, UPDATE, REMOVE
    file_content_lines = []
    in_explanations_section = False
    no_changes_files = []

    for line in response_text.splitlines():
        if in_explanations_section:
            if explanations is None:
                explanations = line + "\n"
            else:
                explanations += line + "\n"
            continue

        start_match_java = JAVA_START_MARKER_RE.match(line)
        start_match_xml = XML_START_MARKER_RE.match(line)
        end_match_java = JAVA_END_MARKER_RE.match(line)
        end_match_xml = XML_END_MARKER_RE.match(line)
        explanations_match = EXPLANATIONS_SEPARATOR_RE.match(line)
        no_changes_match = NO_CHANGES_RE.match(line)

        start_match = start_match_java or start_match_xml
        end_match = end_match_java or end_match_xml


        if start_match:
            if current_file_path:
                logging.warning(f"Unexpected start marker found before end marker for file: {current_file_path}")
            current_file_path = start_match.group(1)
            current_action = start_match.group(2) # Extract action (CREATE, UPDATE, REMOVE)
            file_content_lines = []

        elif end_match:
            if current_file_path:
                ended_file_path = end_match.group(1)
                ended_action = end_match.group(2)

                if current_file_path != ended_file_path or current_action != ended_action:
                    logging.warning(f"Start/End marker mismatch: start='{current_file_path} ({current_action})', end='{ended_file_path} ({ended_action})'")

                file_content = "".join(file_content_lines).rstrip("\n")

                if current_action == "UPDATE":
                    modifications[current_file_path] = file_content
                elif current_action == "CREATE":
                    creations[current_file_path] = file_content
                elif current_action == "REMOVE":
                    removals.append(current_file_path)
                    if file_content: # Warn if content is provided for REMOVE (should be empty)
                        logging.warning(f"Content found for REMOVE action in file: {current_file_path}, content will be ignored.")

                current_file_path = None
                current_action = None
                file_content_lines = []
            else:
                logging.warning("Unexpected end marker without a start marker.")


        elif explanations_match:
            in_explanations_section = True
            explanations = ""

        elif no_changes_match:
            no_changes_file_path = no_changes_match.group(1)
            no_changes_files.append(no_changes_file_path)
            logging.info(f"Gemini indicated no changes for file: {no_changes_file_path}")

        elif current_file_path and current_action != "REMOVE": # Only accumulate content for CREATE/UPDATE
            file_content_lines.append(line + "\n")

    if current_file_path:
        logging.warning(f"File started but not properly ended in response: {current_file_path}. Content discarded.")

    return modifications, removals, creations, no_changes_files, explanations


def normalize_lines(content):
    """Normalizes file content (no changes)."""
    lines = content.splitlines()
    normalized_lines = [line.strip() for line in lines if line.strip()]
    return normalized_lines


def apply_modifications(modifications, removals, creations):
    """
    Applies modifications, removals, and creations based on Gemini's response.
    Now creates new files if they don't exist, even for "UPDATE" actions (if original not found).
    """
    if modifications:
        logging.info("Applying file updates and creations...") # Updated log message
        for filepath, new_content in modifications.items():
            filepath = filepath.replace("/", "\\") # Normalize path for Windows
            if os.path.exists(filepath):
                try:
                    with open(filepath, "r", encoding="utf-8") as infile:
                        original_content = infile.read()

                    original_lines_normalized = normalize_lines(original_content)
                    new_lines_normalized = normalize_lines(new_content)

                    if original_lines_normalized != new_lines_normalized:
                        logging.info(f"Updating existing file: {filepath} (content changed)")
                        with open(filepath, "w", encoding="utf-8", errors='replace') as outfile:
                            outfile.write(new_content)
                    else:
                        logging.info(f"Skipping update for existing file: {filepath} (no significant content changes)")

                except Exception as e:
                    logging.error(f"Error processing existing file '{filepath}': {e}")
            else:
                # File not found for UPDATE, creating it instead
                try:
                    os.makedirs(os.path.dirname(filepath), exist_ok=True)
                    with open(filepath, "w", encoding="utf-8", errors='replace') as outfile:
                        outfile.write(new_content)
                    logging.info(f"File not found for update, CREATING new file: {filepath}") # Modified log message
                except Exception as e:
                    logging.error(f"Error creating file (even though marked for UPDATE) '{filepath}': {e}")


    if creations:
        logging.info("Creating new files...")
        for filepath, new_content in creations.items():
            filepath = filepath.replace("/", "\\") # Normalize path for Windows
            if not os.path.exists(filepath):
                try:
                    os.makedirs(os.path.dirname(filepath), exist_ok=True)
                    with open(filepath, "w", encoding="utf-8", errors='replace') as outfile:
                        outfile.write(new_content)
                    logging.info(f"Created new file: {filepath}")
                except Exception as e:
                    logging.error(f"Error creating new file '{filepath}': {e}")
            else:
                logging.warning(f"File already exists, skipping creation: {filepath}") # Should not happen in CREATE flow

    if removals:
        logging.info("Removing files...")
        for filepath in removals:
            filepath = filepath.replace("/", "\\") # Normalize path for Windows
            if os.path.exists(filepath):
                try:
                    os.remove(filepath)
                    logging.info(f"Removed file: {filepath}")
                except Exception as e:
                    logging.error(f"Error removing file '{filepath}': {e}")
            else:
                logging.warning(f"File to remove not found: {filepath}")


def main():
    parser = argparse.ArgumentParser(description="Parses Gemini response, applies file modifications, removals, creations, and handles 'No changes'.")
    parser.add_argument("response_file", help="Path to Gemini response file.")
    parser.add_argument("--explanations-output", help="Optional file for explanations.")

    args = parser.parse_args()

    try:
        with open(args.response_file, "r", encoding="utf-8") as infile:
            gemini_response_text = infile.read()
    except Exception as e:
        logging.error(f"Error reading response file: '{args.response_file}': {e}")
        return

    modifications, removals, creations, no_changes_files, explanations = parse_gemini_response(gemini_response_text)

    apply_modifications(modifications, removals, creations)

    if modifications or creations or removals:
        logging.info("File operations (updates, creations, removals) applied based on Gemini response.")
    else:
        logging.info("No file modifications, creations, or removals detected in Gemini response.")
        if no_changes_files:
            logging.info(f"No changes indicated for the following files: {', '.join(no_changes_files)}")

    if explanations is not None:
        if args.explanations_output:
            try:
                with open(args.explanations_output, "w", encoding="utf-8") as exp_outfile:
                    exp_outfile.write(explanations)
                logging.info(f"Explanations saved to: '{args.explanations_output}'")
            except Exception as e:
                logging.error(f"Error saving explanations: '{args.explanations_output}': {e}")
        else:
            print("\n--- EXPLANATIONS FROM GEMINI ---")
            print(explanations)


if __name__ == "__main__":
    main()