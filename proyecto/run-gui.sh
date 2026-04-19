#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="/workspaces/Sistemas_Interactivos"
DISPLAY_NUM=":99"
NOVNC_PORT="6080"
VNC_PORT="5901"
RUNTIME_DIR="/tmp/sisint-gui"

mkdir -p "$RUNTIME_DIR"

# Stop previous instances to avoid port/display conflicts.
pkill -f "Xvfb $DISPLAY_NUM" || true
pkill -f "x11vnc .*${VNC_PORT}" || true
pkill -f "websockify .*${NOVNC_PORT}" || true
pkill -f "main.MainFrame" || true

nohup Xvfb "$DISPLAY_NUM" -screen 0 1280x800x24 -ac -nolisten tcp > "$RUNTIME_DIR/xvfb.log" 2>&1 &
sleep 1
nohup x11vnc -display "$DISPLAY_NUM" -forever -shared -nopw -rfbport "$VNC_PORT" -localhost > "$RUNTIME_DIR/x11vnc.log" 2>&1 &
nohup websockify --web=/usr/share/novnc "$NOVNC_PORT" "localhost:${VNC_PORT}" > "$RUNTIME_DIR/novnc.log" 2>&1 &
nohup env DISPLAY="$DISPLAY_NUM" java -cp "$ROOT_DIR/bin" main.MainFrame > "$RUNTIME_DIR/mainframe.log" 2>&1 &

echo "GUI levantada en noVNC: http://127.0.0.1:${NOVNC_PORT}/vnc.html?autoconnect=true&resize=scale"
echo "Logs en: $RUNTIME_DIR"
