#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DEFAULT_ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
if [[ -n "${ROOT_DIR:-}" ]]; then
  ROOT_DIR="$(cd "$ROOT_DIR" && pwd)"
elif [[ -d "/workspaces/Sistemas_Interactivos/proyecto" ]]; then
  ROOT_DIR="/workspaces/Sistemas_Interactivos"
else
  ROOT_DIR="$DEFAULT_ROOT_DIR"
fi
DISPLAY_NUM=":99"
NOVNC_PORT="6080"
VNC_PORT="5901"
DEFAULT_RUNTIME_DIR="${TMPDIR:-/tmp}/sisint-gui-$(id -u)"
RUNTIME_DIR="${RUNTIME_DIR:-$DEFAULT_RUNTIME_DIR}"
if [[ -e "$RUNTIME_DIR" && ! -w "$RUNTIME_DIR" ]]; then
  RUNTIME_DIR="$(mktemp -d "${TMPDIR:-/tmp}/sisint-gui.XXXXXX")"
fi
IS_CODESPACES=false
if [[ "${CODESPACES:-}" == "true" ]]; then
  IS_CODESPACES=true
fi
MAX_RETRIES=3
RETRY_DELAY=2

mkdir -p "$RUNTIME_DIR"

# Compilar el código Java
compile_project() {
  echo "[Compile] Compilando código Java..."
  find "$ROOT_DIR/proyecto/src" -name "*.java" -type f | xargs javac -d "$ROOT_DIR/bin" -cp "$ROOT_DIR/bin:$ROOT_DIR/proyecto/src" 2>&1 | grep -v "^Note:" || true
  echo "[Compile] ✓ Compilación completada"
}

# Copiar recursos al classpath antes de arrancar Java.
sync_resources() {
  mkdir -p "$ROOT_DIR/bin/resources" "$ROOT_DIR/bin/bundle"
  cp -R "$ROOT_DIR/proyecto/src/resources/." "$ROOT_DIR/bin/resources/" 2>/dev/null || true
  cp -R "$ROOT_DIR/proyecto/src/bundle/." "$ROOT_DIR/bin/bundle/" 2>/dev/null || true
}

# Función: limpiar todos los procesos pendientes
cleanup_processes() {
  echo "[Cleanup] Deteniendo procesos previos..."
  if $IS_CODESPACES; then
    pkill -9 -f "Xvfb $DISPLAY_NUM" || true
    pkill -9 -f "x11vnc .*${VNC_PORT}" || true
    pkill -9 -f "websockify .*${NOVNC_PORT}" || true
  fi
  pkill -9 -f "main.MainFrame" || true
  sleep 2
}

# Función: esperar a que un puerto esté libre
wait_port_free() {
  local port=$1
  local timeout=10
  local elapsed=0
  while lsof -i ":$port" >/dev/null 2>&1; do
    if [ $elapsed -ge $timeout ]; then
      echo "[ERROR] Puerto $port sigue ocupado tras ${timeout}s. Fuerza kill más agresivo."
      return 1
    fi
    echo "[Espera] Puerto $port aún ocupado, esperando..."
    sleep 1
    elapsed=$((elapsed + 1))
  done
  echo "[OK] Puerto $port libre"
  return 0
}

# Limpiar e inicializar
cleanup_processes
compile_project
sync_resources
if $IS_CODESPACES; then
  wait_port_free "$VNC_PORT" || true
  wait_port_free "$NOVNC_PORT" || true
  sleep 1

  for command_name in Xvfb x11vnc websockify; do
    if ! command -v "$command_name" >/dev/null 2>&1; then
      echo "[ERROR] Falta '$command_name'. Instálalo para usar el modo Codespaces/noVNC."
      exit 1
    fi
  done

  echo "[Init] Levantando Xvfb en $DISPLAY_NUM..."
  nohup Xvfb "$DISPLAY_NUM" -screen 0 1280x800x24 -ac -nolisten tcp > "$RUNTIME_DIR/xvfb.log" 2>&1 &
  XVFB_PID=$!
  sleep 2

  echo "[Init] Levantando x11vnc en puerto $VNC_PORT..."
  nohup x11vnc -display "$DISPLAY_NUM" -forever -shared -nopw -rfbport "$VNC_PORT" -localhost > "$RUNTIME_DIR/x11vnc.log" 2>&1 &
  VNC_PID=$!
  sleep 1

  echo "[Init] Levantando websockify/noVNC en puerto $NOVNC_PORT..."
  nohup websockify --web=/usr/share/novnc "$NOVNC_PORT" "127.0.0.1:${VNC_PORT}" > "$RUNTIME_DIR/novnc.log" 2>&1 &
  NOVNC_PID=$!
  sleep 1

  echo "[Init] Levantando MainFrame en display $DISPLAY_NUM..."
  nohup env DISPLAY="$DISPLAY_NUM" java -cp "$ROOT_DIR/bin" main.MainFrame > "$RUNTIME_DIR/mainframe.log" 2>&1 &
  JAVA_PID=$!
  sleep 2

  echo "[Validación] Comprobando procesos..."
  for pid in $XVFB_PID $VNC_PID $NOVNC_PID $JAVA_PID; do
    if kill -0 "$pid" 2>/dev/null; then
      echo "  ✓ PID $pid está activo"
    else
      echo "  ✗ PID $pid NO está activo (posible fallo de inicio)"
    fi
  done

  echo ""
  echo "═════════════════════════════════════════════════════════════"
  LOCAL_NOVNC_URL="http://127.0.0.1:${NOVNC_PORT}/vnc.html?autoconnect=true&resize=scale"
  if [[ -n "${GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN:-}" ]]; then
    PUBLIC_NOVNC_URL="https://${CODESPACE_NAME}-${NOVNC_PORT}.${GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN}/vnc.html?autoconnect=true&resize=scale"
    echo "✓ GUI levantada en noVNC:"
    echo "  → ${PUBLIC_NOVNC_URL}"
    echo "  → Local: ${LOCAL_NOVNC_URL}"
  else
    echo "✓ GUI levantada en noVNC:"
    echo "  → ${LOCAL_NOVNC_URL}"
  fi
  echo ""
  echo "Logs en: $RUNTIME_DIR"
  echo "  - Xvfb:      $RUNTIME_DIR/xvfb.log"
  echo "  - x11vnc:    $RUNTIME_DIR/x11vnc.log"
  echo "  - noVNC:     $RUNTIME_DIR/novnc.log"
  echo "  - MainFrame: $RUNTIME_DIR/mainframe.log"
  echo ""
  echo "Para parar:  cd proyecto && ./stop-gui.sh"
  echo "═════════════════════════════════════════════════════════════"
else
  if [[ -z "${DISPLAY:-}" ]]; then
    echo "[ERROR] DISPLAY no está definido. Ejecuta el script en una sesión gráfica local o usa Codespaces."
    exit 1
  fi

  echo "[Init] Levantando MainFrame en local sobre DISPLAY=${DISPLAY}..."
  nohup java -cp "$ROOT_DIR/bin" main.MainFrame > "$RUNTIME_DIR/mainframe.log" 2>&1 &
  JAVA_PID=$!
  sleep 2

  echo "[Validación] Comprobando proceso..."
  if kill -0 "$JAVA_PID" 2>/dev/null; then
    echo "  ✓ PID $JAVA_PID está activo"
    echo ""
    echo "═════════════════════════════════════════════════════════════"
    echo "✓ GUI levantada en local"
    echo ""
    echo "Logs en: $RUNTIME_DIR"
    echo "  - MainFrame: $RUNTIME_DIR/mainframe.log"
    echo ""
    echo "Para parar:  cd proyecto && ./stop-gui.sh"
    echo "═════════════════════════════════════════════════════════════"
  else
    echo "  ✗ PID $JAVA_PID NO está activo (posible fallo de inicio)"
    echo "[ERROR] Revisa el log: $RUNTIME_DIR/mainframe.log"
    exit 1
  fi
fi
