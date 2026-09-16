#!/usr/bin/env bash

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SRC_DIR="${PROJECT_ROOT}/src"
BIN_DIR="${PROJECT_ROOT}/bin"
LIB_DIR="${PROJECT_ROOT}/lib"
java --module-path "$LIB_DIR/javafx-sdk-25.0.4/lib:$LIB_DIR/h2/bin/h2-2.4.240.jar:$BIN_DIR" \
     --add-modules com.h2database \
     --enable-native-access=javafx.graphics \
     -m FoundationsF26/applicationMain.FoundationsMain
