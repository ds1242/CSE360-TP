#!/usr/bin/env bash

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SRC_DIR="${PROJECT_ROOT}/src"
BIN_DIR="${PROJECT_ROOT}/bin"
LIB_DIR="${PROJECT_ROOT}/lib"
javac -d "$BIN_DIR" --module-path "$LIB_DIR/javafx-sdk-25.0.4/lib" $(find "$SRC_DIR" -name "*.java")
