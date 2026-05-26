#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC_DIR="$ROOT_DIR/src"
BIN_DIR="$ROOT_DIR/bin"
JAR_FILE="$ROOT_DIR/sisint.jar"

rm -rf "$BIN_DIR"
mkdir -p "$BIN_DIR"

javac -d "$BIN_DIR" $(find "$SRC_DIR" -name "*.java")

if [[ -d "$SRC_DIR/bundle" ]]; then
  cp -R "$SRC_DIR/bundle" "$BIN_DIR/"
fi

if [[ -d "$SRC_DIR/resources" ]]; then
  cp -R "$SRC_DIR/resources" "$BIN_DIR/"
fi

jar cfe "$JAR_FILE" main.MainFrame -C "$BIN_DIR" .

echo "JAR generado en: $JAR_FILE"