import os
import argparse
import logging

logging.basicConfig(level=logging.INFO, format='%(levelname)s: %(message)s')

DEFAULT_OUTPUT_FILENAME = "training_data.txt"
FILE_EXTENSIONS = [".xml", ".java"]
TARGET_DIR_NAME = "target"


def combine_files_for_gemini(source_dir, output_file=None):
    """
    Combines all files of specific types in a directory (including subdirectories)
    into a single text file.
    """
    if not os.path.isdir(source_dir):
        logging.error(f"Source directory '{source_dir}' does not exist or is not a directory.")
        return

    if not output_file:
        output_file = os.path.join(source_dir, DEFAULT_OUTPUT_FILENAME)
        logging.info(f"Output file not specified. Using default output file: '{output_file}'")
    else:
        logging.info(f"Using specified output file: '{output_file}'")

    logging.info(f"Combining files from '{source_dir}' (excluding '{TARGET_DIR_NAME}') into '{output_file}'...")

    with open(output_file, "w", encoding="utf-8") as outfile:
        for root, _, files in os.walk(source_dir):
            if TARGET_DIR_NAME in root.split(os.sep):
                continue

            for file in files:
                if any(file.lower().endswith(ext) for ext in FILE_EXTENSIONS):
                    filepath = os.path.join(root, file)
                    logging.info(f"Processing file: {filepath}")

                    try:
                        with open(filepath, "r", encoding="utf-8") as infile:
                            file_content = infile.read()

                            file_type = "XML" if file.lower().endswith(".xml") else "JAVA"
                            outfile.write(f"//--- START FILE: {filepath} ({file_type}) ---\n")
                            outfile.write(file_content)
                            outfile.write(f"//--- END FILE: {filepath} ---\n")
                            outfile.write("\n\n")

                    except Exception as e:
                        logging.error(f"Error processing file '{filepath}': {e}")

    logging.info(f"Files combined successfully!")
    logging.info(f"Output written to: '{output_file}'")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Combines XML and Java files in a directory (and subdirectories) into a single text file, excluding 'target' directories.")
    parser.add_argument("source_dir", help="Path to the source directory")
    parser.add_argument("output_file", nargs='?', default=None, help="Optional path to the output file. Default is training_data.txt in source directory.")

    args = parser.parse_args()

    combine_files_for_gemini(args.source_dir, args.output_file)