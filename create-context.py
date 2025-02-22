import os
import argparse
import logging
from xml.dom import minidom
import re

logging.basicConfig(level=logging.INFO, format='%(levelname)s: %(message)s')

DEFAULT_OUTPUT_FILENAME = "training_data.txt"
FILE_EXTENSIONS = [".xml", ".java"]  # List of extensions to process
TARGET_DIR_NAME = "target"  # Directory name to ignore


def combine_files_for_gemini(source_dir, output_file=None, format_xml=True, include_file_markers=True):
    """
    Combines files of specified extensions from a directory (and subdirectories)
    into a single text file, optimized for Gemini training.

    Args:
        source_dir (str): Path to the source directory.
        output_file (str, optional): Path to the output file. If None, a default file
                                      'combined_output.txt' will be created in the source directory.
        format_xml (bool): If True, format XML content for better readability (and consistency).
        include_file_markers (bool): If True, add markers to indicate the start and end of each file.
    """

    if not os.path.isdir(source_dir):
        logging.error(f"Source directory '{source_dir}' does not exist or is not a directory.")
        return

    if not output_file:
        output_file = os.path.join(source_dir, DEFAULT_OUTPUT_FILENAME)
        logging.info(f"Output file not specified. Using default output file: '{output_file}'")
    else:
        logging.info(f"Using specified output file: '{output_file}'")

    logging.info(f"Combining files from '{source_dir}' (excluding '{TARGET_DIR_NAME}' directories) into '{output_file}' for Gemini training...")

    with open(output_file, "w", encoding="utf-8") as outfile:
        for root, _, files in os.walk(source_dir):
            if TARGET_DIR_NAME in root.split(os.sep):  # Ignore 'target' directories
                continue

            for file in files:
                if any(file.lower().endswith(ext) for ext in FILE_EXTENSIONS):
                    filepath = os.path.join(root, file)
                    logging.info(f"Processing file: {filepath}")

                    try:
                        with open(filepath, "r", encoding="utf-8") as infile:
                            file_content = infile.read()

                            if include_file_markers:
                                file_type = "XML" if file.lower().endswith(".xml") else "JAVA"
                                outfile.write(f"--- START FILE: {filepath} ({file_type}) ---\n")

                            if file.lower().endswith(".xml") and format_xml:
                                try:
                                    parsed_xml = minidom.parseString(file_content)
                                    formatted_xml = parsed_xml.toprettyxml(indent="  ", encoding="utf-8").decode()
                                    # Remove XML declaration if present to avoid repetition, and extra newlines
                                    formatted_xml_no_decl = re.sub(r'<\?xml[^>]*\?>\n?', '', formatted_xml).strip()
                                    outfile.write(formatted_xml_no_decl)

                                except Exception as e:
                                    logging.warning(f"XML formatting error in '{filepath}': {e}. Writing raw content.")
                                    outfile.write(file_content) # Fallback to raw content on error
                            else:
                                outfile.write(file_content)

                            outfile.write("\n")  # Add a newline within the file content for better readability in some cases
                            if include_file_markers:
                                outfile.write(f"\n--- END FILE: {filepath} ---\n") # Add extra newline before end marker
                            outfile.write("\n\n") # Blank lines between files

                    except Exception as e:
                        logging.error(f"Error processing file '{filepath}': {e}")

    logging.info(f"Files combined successfully for Gemini training!")
    logging.info(f"Output written to: '{output_file}'")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Combine XML and Java files for Gemini training, excluding 'target' directories.")
    parser.add_argument("source_dir", help="Path to the source directory")
    parser.add_argument("output_file", nargs='?', default=None, help="Optional path to the output file. Default is combined_output.txt in source directory.")
    parser.add_argument("--no-xml-format", dest='format_xml', action='store_false', help="Disable XML formatting.")
    parser.add_argument("--no-file-markers", dest='include_file_markers', action='store_false', help="Disable file start/end markers in output.")

    args = parser.parse_args()

    combine_files_for_gemini(args.source_dir, args.output_file, args.format_xml, args.include_file_markers)