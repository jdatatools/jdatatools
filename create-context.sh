#!/bin/bash

# Script to combine all files of specific types in a directory (including subdirectories) into a single text file,
# optimized for training an AI model (e.g., Gemini).
#
# This version:
#   - Includes a default output file in the source directory if no output file is specified.
#   - **Excludes "target" directories** from the search to avoid processing build artifacts.

# --- Configuration ---
SOURCE_DIR="$1"          # The directory containing the files to combine. Passed as the first argument.
OUTPUT_FILE="$2"        # The name of the output text file. Passed as the second argument (optional).
FILE_EXTENSIONS=("xml" "java") # Array of file extensions to include. Modify this as needed. Must be lowercase.
MODEL_TYPE="gemini"    # Target model type (optional - for future customizations)
DEFAULT_OUTPUT_FILENAME="combined_output.txt" # Default output file name

# --- Error Handling ---
if [ -z "$SOURCE_DIR" ]; then
  echo "Error: Missing source directory. Usage: $0 <source_directory> [<output_file>]"
  echo "       If <output_file> is not provided, '$DEFAULT_OUTPUT_FILENAME' will be used in the source directory."
  exit 1
fi

if [ ! -d "$SOURCE_DIR" ]; then
  echo "Error: Source directory '$SOURCE_DIR' does not exist or is not a directory."
  exit 1
fi

# --- Determine Output File ---
if [ -z "$OUTPUT_FILE" ]; then
  OUTPUT_FILE="$SOURCE_DIR/$DEFAULT_OUTPUT_FILENAME"
  echo "Output file not specified. Using default output file: '$OUTPUT_FILE'"
else
  echo "Using specified output file: '$OUTPUT_FILE'"
fi


# --- Main Script ---

echo "Combining files from '$SOURCE_DIR' (excluding 'target' directories) into '$OUTPUT_FILE' for '$MODEL_TYPE' model..."

# Redirect all output to the output file, overwriting if it exists.
> "$OUTPUT_FILE"

# Iterate through the specified file extensions.
for ext in "${FILE_EXTENSIONS[@]}"; do
  echo "Processing files with extension: .$ext"

  # Find all files with the current extension in the source directory and its subdirectories.
  # EXCLUDING directories named "target" to avoid build artifacts.
  find "$SOURCE_DIR" -type f -name "*.${ext}" -not -path "*/target/*" -not -path "*/target" -print0 | while IFS= read -r -d $'\0' file; do
    # Read each file and append its contents to the output file.
    cat "$file" >> "$OUTPUT_FILE"
    echo "" >> "$OUTPUT_FILE" # Blank line separator between files.
  done
done

echo "Files combined successfully for '$MODEL_TYPE' training!"
echo "Output written to: '$OUTPUT_FILE'"

exit 0