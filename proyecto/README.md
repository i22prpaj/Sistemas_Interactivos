## Proyecto UCO Reviews

Aplicación Java Swing desarrollada como la segunda parte de la práctica. Incluye un modo de ejecución en entornos sin servidor gráfico mediante Xvfb + x11vnc + noVNC.

## Requisitos

- Java JDK 17+ (OpenJDK funcional):

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk
```

- Herramientas opcionales para entorno gráfico headless (si quieres levantar la UI en un contenedor o servidor):

```bash
sudo apt install -y xvfb x11vnc novnc websockify
```

Si aparece un error con repositorios (Yarn u otros), revisa `/etc/apt/sources.list.d/` y desactiva temporalmente la entrada problemática.

## Compilar (desde la carpeta `proyecto`)

Forma rápida (sin Maven/Gradle):

```bash
cd /ruta/a/proyecto
bash build.sh
```

Si prefieres hacerlo manualmente:

```bash
mkdir -p bin
javac -d bin $(find src -name "*.java")
cp -R src/bundle bin/
cp -R src/resources bin/
jar cfe sisint.jar main.MainFrame -C bin .
```

El script `build.sh` deja el JAR listo para ejecutar y copia también los bundles de idioma y los recursos gráficos.

## Ejecutar

- Opción recomendada (arranque preparado que levanta entorno gráfico headless y la app):

```bash
cd /ruta/a/proyecto
./run-gui.sh
```

Abrir en el navegador usando noVNC:

 Local:

http://127.0.0.1:6080/vnc.html?autoconnect=true&resize=scale

 Remoto:

https://ubiquitous-succotash-jjwjppvjjgg2pp74-6080.app.github.dev/vnc.html?autoconnect=true&resize=scale


- Ejecutar el JAR (si lo has creado y contiene `Main-Class`):

```bash
cd /ruta/a/proyecto
java -jar sisint.jar
```

## Detener servicios gráficos

```bash
cd /ruta/a/proyecto
./stop-gui.sh
```

## Notas opcionales

- Si quieres generar localmente el PDF de la memoria (`test.pdf`), pero esto es opcional.

```bash
# Generar memoria (opcional)
pdflatex -interaction=nonstopmode test.tex
```

## Estructura del repositorio

- `src/` : código fuente Java.
- `bin/` : clases compiladas (si usas compilación manual).
- `sisint.jar` : JAR ejecutable.
- `run-gui.sh`, `stop-gui.sh` : scripts para levantar/detener entorno gráfico y la app.
- `test.tex` / `test.pdf` : memoria en LaTeX y PDF
- `slides.html` : presentación y PDF de la presentación.
- `screenshots/` : capturas usadas en la memoria.

## Solución de problemas comunes

- "No se encuentra java": instala `openjdk-17-jdk` y verifica `java -version`.
- Errores al compilar: asegúrate de lanzar `javac` desde la carpeta `proyecto` y que la ruta `src` exista.
- Si `run-gui.sh` no arranca: revisa permisos (`chmod +x run-gui.sh`) y dependencias del entorno gráfico.
- Si el JAR falla con textos en otro idioma, recompila con `bash build.sh` para regenerar `sisint.jar` con los bundles actualizados.
