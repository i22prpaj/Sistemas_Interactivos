#!/usr/bin/env bash
set -euo pipefail

pkill -f "main.MainFrame" || true
pkill -f "websockify .*6080" || true
pkill -f "x11vnc .*5901" || true
pkill -f "Xvfb :99" || true

echo "GUI detenida (MainFrame, noVNC, x11vnc y Xvfb)."
