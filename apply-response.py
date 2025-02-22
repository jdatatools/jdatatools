import os
import re
import logging
import argparse

logging.basicConfig(level=logging.INFO, format='%(levelname)s: %(message)s')

JAVA_START_MARKER_RE = re.compile(r'//--- START FILE: (.+) \(JAVA\) ---')
JAVA_END_MARKER_RE = re.compile(r'//--- END FILE: (.+) ---')
XML_START_MARKER_RE = re.compile(r'<!----- START FILE: (.+) \(XML\) --- -->')
XML_END_MARKER_RE = re.compile(r'<!----- END FILE: (.+) --- -->')
EXPLANATIONS_SEPARATOR_RE = re.compile(r'### EXPLANATIONS ###')

def parse_gemini_response(response_text):
    """
    Parses Gemini's response text to extract modified file content and explanations.
    (No changes in this function)
    """
    modifications = {}
    explanations = None
    current_file_path = None
    file_content_lines = []
    in_explanations_section = False

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


        if start_match_java or start_match_xml:
            if current_file_path:
                logging.warning(f"Unexpected start marker found before end marker for file: {current_file_path}")
                # You might want to handle this more strictly in a production setting
            current_file_path = start_match_java.group(1) if start_match_java else start_match_xml.group(1)
            file_content_lines = [] # Start a new content list for the new file

        elif end_match_java or end_match_xml:
            if current_file_path:
                file_content = "".join(file_content_lines).rstrip("\n") # Join and remove trailing newline
                modifications[current_file_path] = file_content
                current_file_path = None # Reset for the next file
                file_content_lines = []
            else:
                logging.warning("Unexpected end marker found without a start marker.")

        elif explanations_match:
            in_explanations_section = True
            explanations = "" # Initialize explanations string

        elif current_file_path:
            file_content_lines.append(line + "\n") # Accumulate lines for the current file

    if current_file_path: # Handle case where a file starts but doesn't end in the response
        logging.warning(f"File started but not properly ended in response: {current_file_path}. File content will be discarded.")

    return modifications, explanations

def normalize_lines(content):
    """
    Normalizes file content by splitting into lines, stripping whitespace from each line,
    and removing empty lines.  This function now ignores whitespace differences.

    Args:
        content (str): File content string.

    Returns:
        list: List of normalized lines (whitespace stripped, empty lines removed).
    """
    lines = content.splitlines()
    normalized_lines = [line.strip() for line in lines if line.strip()] # Strip and filter empty lines
    return normalized_lines


def apply_modifications(modifications):
    """
    Applies the extracted modifications by overwriting the original files,
    but only if content has changed, ignoring whitespace and empty lines.

    Args:
        modifications (dict): Dictionary of file paths and modified content from parse_gemini_response.
    """
    if not modifications:
        logging.info("No file modifications found in Gemini response.")
        return

    for filepath, new_content in modifications.items():
        if os.path.exists(filepath):
            try:
                with open(filepath, "r", encoding="utf-8") as infile:
                    original_content = infile.read()

                original_lines_normalized = normalize_lines(original_content)
                new_lines_normalized = normalize_lines(new_content)

                if original_lines_normalized != new_lines_normalized:
                    logging.info(f"Applying modification to file: {filepath} (content changed, ignoring whitespace)")
                    with open(filepath, "w", encoding="utf-8", errors='replace') as outfile: # Added errors='replace' for robustness
                        outfile.write(new_content)
                else:
                    logging.info(f"Skipping file: {filepath} (no significant content changes, ignoring whitespace)")

            except Exception as e:
                logging.error(f"Error processing file '{filepath}': {e}")
        else:
            logging.warning(f"Original file not found, cannot apply modification: {filepath}")


def main():
    parser = argparse.ArgumentParser(description="Parses Gemini's response and applies file modifications, comparing content and ignoring whitespace and empty lines.")
    parser.add_argument("response_file", help="Path to the file containing Gemini's response text.")
    parser.add_argument("--explanations-output", help="Optional file to save explanations to.")

    args = parser.parse_args()

    try:
        with open(args.response_file, "r", encoding="utf-8") as infile:
            gemini_response_text = infile.read()
    except Exception as e:
        logging.error(f"Error reading Gemini response file '{args.response_file}': {e}")
        return

    modifications, explanations = parse_gemini_response(gemini_response_text)

    if modifications:
        apply_modifications(modifications)
        logging.info("File modifications applied based on content comparison (ignoring whitespace).")
    else:
        logging.info("No file modifications detected in Gemini response.")

    if explanations is not None:
        if args.explanations_output:
            try:
                with open(args.explanations_output, "w", encoding="utf-8") as exp_outfile:
                    exp_outfile.write(explanations)
                logging.info(f"Explanations saved to: '{args.explanations_output}'")
            except Exception as e:
                logging.error(f"Error saving explanations to file '{args.explanations_output}': {e}")
        else:
            print("\n--- EXPLANATIONS FROM GEMINI ---")
            print(explanations)


if __name__ == "__main__":
    main()