package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public final class ProfessorDirectory {

    // Directorio en memoria de perfiles registrados.
    // - `PROFILES`: mapa de id -> ProfessorProfile (ordenado por inserción)
    // - `BY_SUBJECT`: índice por asignatura para obtener profesores por materia
    // - `ALL_RATINGS`: ratings acumulados por profesor (persistidos en JSON)
    // - `SAVED_ASPECTS`: aspectos (tags) guardados por profesor
    private static final Map<String, ProfessorProfile> PROFILES = new LinkedHashMap<>();
    private static final Map<String, List<ProfessorProfile>> BY_SUBJECT = new LinkedHashMap<>();
    private static final Map<String, List<Double>> ALL_RATINGS = new LinkedHashMap<>();  // Todos los ratings
    private static final Map<String, List<String>> SAVED_ASPECTS = new LinkedHashMap<>();  // Aspectos guardados

    // Ruta del fichero donde se persisten ratings y aspectos del usuario.
    // Se almacena en el home del usuario para no requerir permisos adicionales.
    private static final String RATINGS_FILE = System.getProperty("user.home") + "/.sisint_ratings.json";

    static {
        register(new ProfessorProfile(
            "profesor_antonio_lopez",
            "Antonio José López Jiménez",
            "Departamento de Matemáticas",
            "aloji@uco.es",
            "C2 Albert Einstein 2ª planta",
            "957 846 513",
            "martes y jueves de 10:00 a 12:30",
            subjects("common.linear_algebra", "common.calculus"),
            notes("Prepara ejercicios extra para quien quiera ampliar.", "Explica con mucho detalle los pasos de resolución.")));

        register(new ProfessorProfile(
            "profesora_rosa_munoz",
            "Rosa María Muñoz Millán",
            "Departamento de Ingeniería de Sistemas",
            "rmmunoz@uco.es",
            "Despacho B1 204",
            "957 846 789",
            "lunes y miércoles de 12:00 a 14:00",
            subjects("common.linear_algebra", "subjects.matematica_discreta"),
            notes("Comparte resúmenes antes de los parciales.", "Resuelve dudas con bastante rapidez.")));

        register(new ProfessorProfile(
            "profesora_laura_martinez",
            "Laura Martínez Vega",
            "Departamento de Informática",
            "lmartinez@uco.es",
            "Despacho A1 108",
            "957 846 520",
            "lunes de 16:00 a 18:00 y miércoles de 9:00 a 11:00",
            subjects("subjects.intro_programacion", "subjects.programacion_web"),
            notes("Prioriza el trabajo práctico.", "Revisa el código con ejemplos muy visuales.")));

        register(new ProfessorProfile(
            "profesor_javier_nunez",
            "Javier Núñez Castro",
            "Departamento de Matemática Aplicada",
            "jnunez@uco.es",
            "Despacho C1 214",
            "957 846 521",
            "martes de 9:30 a 11:30 y jueves de 12:00 a 14:00",
            subjects("common.calculus", "subjects.estadistica"),
            notes("Suele centrar la clase en problemas resueltos.", "Aporta ejercicios guiados para practicar.")));

        register(new ProfessorProfile(
            "profesora_marta_gil",
            "Marta Gil Romero",
            "Departamento de Estadística",
            "mgil@uco.es",
            "Despacho C3 102",
            "957 846 522",
            "martes y jueves de 11:00 a 13:00",
            subjects("subjects.estadistica", "subjects.sistemas_inteligentes"),
            notes("Insiste en interpretar bien los datos.", "Trabaja con ejemplos de casos reales.")));

        register(new ProfessorProfile(
            "profesor_diego_ramos",
            "Diego Ramos Molina",
            "Departamento de Física Aplicada",
            "dramos@uco.es",
            "Despacho F2 018",
            "957 846 523",
            "lunes y miércoles de 10:00 a 12:00",
            subjects("subjects.fisica", "subjects.circuitos"),
            notes("Combina teoría y demostraciones cortas.", "Da prioridad a la comprensión conceptual.")));

        register(new ProfessorProfile(
            "profesora_carmen_prieto",
            "Carmen Prieto Salas",
            "Departamento de Economía",
            "cprieto@uco.es",
            "Despacho E1 301",
            "957 846 524",
            "martes de 16:00 a 18:00 y viernes de 10:00 a 12:00",
            subjects("subjects.economia", "subjects.legislacion"),
            notes("Relaciona la materia con ejemplos de empresa.", "Pide que se lleven las lecturas al día.")));

        register(new ProfessorProfile(
            "profesor_sergio_hidalgo",
            "Sergio Hidalgo Pardo",
            "Departamento de Ingeniería del Software",
            "shidalgo@uco.es",
            "Despacho D1 112",
            "957 846 525",
            "lunes de 11:00 a 13:00 y jueves de 16:00 a 18:00",
            subjects("subjects.metodologia_programacion", "subjects.ingenieria_software"),
            notes("Cuida especialmente la calidad del código.", "Trabaja con entregas parciales y feedback continuo.")));

        register(new ProfessorProfile(
            "profesora_elena_cruz",
            "Elena Cruz Navarro",
            "Departamento de Arquitectura de Computadores",
            "ecruz@uco.es",
            "Despacho D2 208",
            "957 846 526",
            "miércoles de 12:00 a 14:00 y viernes de 9:00 a 11:00",
            subjects("subjects.fundamentos_computadores", "subjects.arquitectura_computadores"),
            notes("Explica la materia con esquemas de hardware.", "Valora mucho la participación en clase.")));

        register(new ProfessorProfile(
            "profesor_raul_ortega",
            "Raúl Ortega León",
            "Departamento de Electrónica",
            "roltega@uco.es",
            "Despacho F1 205",
            "957 846 527",
            "lunes y jueves de 8:30 a 10:30",
            subjects("subjects.circuitos", "subjects.arquitectura_redes"),
            notes("Suele usar problemas cortos al final de cada tema.", "Insiste en entender la base física de cada bloque.")));

        register(new ProfessorProfile(
            "profesora_noelia_sanchez",
            "Noelia Sánchez Ríos",
            "Departamento de Matemática Discreta",
            "nsanchez@uco.es",
            "Despacho C2 117",
            "957 846 528",
            "martes de 9:00 a 11:00 y jueves de 12:00 a 14:00",
            subjects("subjects.matematica_discreta", "subjects.estructuras_datos"),
            notes("Recomienda practicar con muchos ejercicios.", "Deja bastante margen para resolver dudas.")));

        register(new ProfessorProfile(
            "profesor_alberto_medina",
            "Alberto Medina Flores",
            "Departamento de Programación",
            "amedina@uco.es",
            "Despacho A2 014",
            "957 846 529",
            "lunes y miércoles de 17:00 a 19:00",
            subjects("subjects.poo", "subjects.programacion_administracion"),
            notes("Se centra en buenas prácticas y diseño.", "Propone pequeñas implementaciones de clase.")));

        register(new ProfessorProfile(
            "profesora_patricia_leon",
            "Patricia León Marín",
            "Departamento de Bases de Datos",
            "pleon@uco.es",
            "Despacho B2 210",
            "957 846 530",
            "martes y viernes de 10:00 a 12:00",
            subjects("subjects.bases_datos", "subjects.sistemas_informacion"),
            notes("Subraya la importancia del modelo relacional.", "Revisa entregas con bastante detalle.")));

        register(new ProfessorProfile(
            "profesor_hugo_varela",
            "Hugo Varela Ponce",
            "Departamento de Sistemas Operativos",
            "hvarela@uco.es",
            "Despacho B3 009",
            "957 846 531",
            "miércoles de 9:30 a 11:30 y viernes de 12:00 a 14:00",
            subjects("subjects.sistemas_operativos", "subjects.sistemas_informacion"),
            notes("Suele conectar la teoría con ejemplos del sistema real.", "Pide que se prueben los ejercicios en casa.")));

        register(new ProfessorProfile(
            "profesora_silvia_ferrer",
            "Silvia Ferrer Campos",
            "Departamento de Ingeniería del Software",
            "sferrer@uco.es",
            "Despacho D1 201",
            "957 846 532",
            "lunes de 12:00 a 14:00 y jueves de 9:00 a 11:00",
            subjects("subjects.ingenieria_software"),
            notes("Da mucha importancia a la documentación.", "Entrega rúbricas claras antes de cada práctica.")));

        register(new ProfessorProfile(
            "profesor_ivan_serrano",
            "Iván Serrano Gil",
            "Departamento de Arquitectura de Computadores",
            "iserrano@uco.es",
            "Despacho D2 104",
            "957 846 533",
            "martes y jueves de 8:30 a 10:30",
            subjects("subjects.arquitectura_computadores", "subjects.fundamentos_computadores"),
            notes("Trabaja desde bloques muy concretos.", "Suele dedicar tiempo a ejercicios guiados.")));

        register(new ProfessorProfile(
            "profesora_aitana_romero",
            "Aitana Romero Cruz",
            "Departamento de Administración de Sistemas",
            "aromero@uco.es",
            "Despacho A1 212",
            "957 846 534",
            "lunes de 10:00 a 12:00 y miércoles de 16:00 a 18:00",
            subjects("subjects.programacion_administracion", "subjects.bases_datos"),
            notes("Relaciona la materia con casos de administración real.", "Valora la entrega limpia y bien organizada.")));

        register(new ProfessorProfile(
            "profesor_marcos_pineda",
            "Marcos Pineda Torres",
            "Departamento de Estructuras de Datos",
            "mpineda@uco.es",
            "Despacho C1 118",
            "957 846 535",
            "martes de 15:30 a 17:30 y jueves de 9:00 a 11:00",
            subjects("subjects.estructuras_datos", "subjects.sistemas_inteligentes"),
            notes("Insiste en la eficiencia de cada algoritmo.", "Revisa la complejidad en cada práctica.")));

        register(new ProfessorProfile(
            "profesora_nuria_casas",
            "Nuria Casas Beltrán",
            "Departamento de Sistemas de Información",
            "ncasas@uco.es",
            "Despacho B1 109",
            "957 846 536",
            "lunes y jueves de 12:00 a 14:00",
            subjects("subjects.sistemas_informacion", "subjects.bases_datos"),
            notes("Busca que el alumnado vea el proceso completo.", "Repite varios ejemplos antes de cada entrega.")));

        register(new ProfessorProfile(
            "profesor_oscar_medina",
            "Óscar Medina Ruiz",
            "Departamento de Inteligencia Artificial",
            "omedina@uco.es",
            "Despacho F3 007",
            "957 846 537",
            "miércoles de 11:00 a 13:00 y viernes de 9:00 a 11:00",
            subjects("subjects.sistemas_inteligentes", "subjects.redes"),
            notes("Explica los conceptos con ejemplos de datos reales.", "Suele dejar tiempo para preguntas al final.")));

        register(new ProfessorProfile(
            "profesora_lucia_navarro",
            "Lucía Navarro Pastor",
            "Departamento de Redes y Comunicaciones",
            "lnavarro@uco.es",
            "Despacho E2 015",
            "957 846 538",
            "martes y viernes de 12:00 a 14:00",
            subjects("subjects.arquitectura_redes", "subjects.redes"),
            notes("Combina teoría con simulaciones.", "Pide revisar la configuración paso a paso.")));

        register(new ProfessorProfile(
            "profesora_sandra_molina",
            "Sandra Molina Herrero",
            "Departamento de Desarrollo Web",
            "smolina@uco.es",
            "Despacho A3 301",
            "957 846 539",
            "lunes y miércoles de 9:00 a 11:00",
            subjects("subjects.programacion_web"),
            notes("Focaliza mucho en la parte práctica.", "Revisa con detalle la estructura de los proyectos.")));

        register(new ProfessorProfile(
            "profesor_pablo_campos",
            "Pablo Campos Vidal",
            "Departamento de Redes y Comunicaciones",
            "pcampos@uco.es",
            "Despacho E2 112",
            "957 846 540",
            "martes de 16:00 a 18:00 y jueves de 10:00 a 12:00",
            subjects("subjects.redes", "subjects.arquitectura_redes"),
            notes("Se centra en topologías y protocolos.", "Pide razonamiento, no solo memorización.")));

        register(new ProfessorProfile(
            "profesora_elena_vargas",
            "Elena Vargas Blanco",
            "Departamento de Derecho y Normativa",
            "evargas@uco.es",
            "Despacho G1 102",
            "957 846 541",
            "lunes y jueves de 11:00 a 13:00",
            subjects("subjects.legislacion"),
            notes("Aporta casos reales para contextualizar la materia.", "Recomienda leer la normativa con calma.")));

        register(new ProfessorProfile(
            "profesor_tomas_gil",
            "Tomás Gil Navarro",
            "Departamento de Derecho y Normativa",
            "tgil@uco.es",
            "Despacho G1 104",
            "957 846 542",
            "martes y viernes de 9:00 a 11:00",
            subjects("subjects.legislacion", "subjects.programacion_administracion"),
            notes("Suele trabajar con ejemplos de procedimientos.", "Da importancia a la precisión en las respuestas.")));
        
        // Cargar ratings persistentes desde archivo
        loadRatingsFromFile();
    }

    private ProfessorDirectory() {
    }

    public static ProfessorProfile get(String id) {
        return PROFILES.get(id);
    }

    public static ProfessorProfile getDefaultProfile() {
        // Devuelve el primer perfil registrado en `PROFILES`, o `null` si no
        // hay ninguno. Usamos `values().stream().findFirst()` porque
        // `PROFILES` es un LinkedHashMap y preserva el orden de inserción,
        // por lo que este método devuelve de forma estable el "primer"
        // perfil añadido al directorio. Retornar `null` permite al caller
        // comprobar la ausencia de datos y aplicar un comportamiento por defecto.
        return PROFILES.values().stream().findFirst().orElse(null);
    }

    public static List<ProfessorProfile> getBySubject(String subjectKey) {
        // Recupera la lista de profesores para `subjectKey`.
        // - Si no hay entrada en `BY_SUBJECT` o la lista está vacía, se
        //   devuelve una lista de fallback que contiene el perfil por
        //   defecto (si existe) o una lista vacía.
        // - Si existe una lista de perfiles, devolvemos `List.copyOf(profiles)`,
        //   que crea una copia inmodificable del contenido actual. Esto es
        //   una práctica defensiva: evita que los llamadores modifiquen la
        //   lista interna almacenada en `BY_SUBJECT`.
        //
        // Notas:
        // - `List.copyOf(...)` devuelve una lista inmutable (UnsupportedOperationException
        //   si se intenta modificar) y está disponible desde Java 10.
        // - El fallback `List.of(fallback)` también produce una lista inmodificable
        //   con un único elemento.
        List<ProfessorProfile> profiles = BY_SUBJECT.get(subjectKey);
        if (profiles == null || profiles.isEmpty()) {
            ProfessorProfile fallback = getDefaultProfile();
            return fallback == null ? List.of() : List.of(fallback);
        }
        return List.copyOf(profiles);
    }

    // Devuelve la valoración promedio de un profesor.
    // - Si hay ratings personalizados en `ALL_RATINGS`, devuelve su promedio.
    // - Si no hay datos personalizados, devuelve un valor por defecto codificado.
    public static double getRating(String professorId) {
        // Si existen ratings personalizados, calcular promedio; si no, usar el rating por defecto
        
        // Si existen ratings almacenados para `professorId` y la lista no está vacía,
        // los tomamos para calcular el promedio. Observación: el código usa
        // `containsKey()` seguido de `get()` — eso provoca dos búsquedas en el mapa;
        // alternativamente se podría usar `List<Double> ratings = ALL_RATINGS.get(professorId);
        // if (ratings != null && !ratings.isEmpty())` para evitar la doble consulta.
        if (ALL_RATINGS.containsKey(professorId) && !ALL_RATINGS.get(professorId).isEmpty()) {
            List<Double> ratings = ALL_RATINGS.get(professorId); // Recuperamos la lista de ratings (ya sabemos que existe y no está vacía).
            double sum = 0;
            for (Double r : ratings) {
                sum += r;
            }
            return sum / ratings.size();
        }
        
        return switch (professorId == null ? "" : professorId) {
            case "profesor_antonio_lopez" -> 4.9d;
            case "profesora_rosa_munoz" -> 4.8d;
            case "profesora_laura_martinez" -> 4.4d;
            case "profesor_javier_nunez" -> 4.5d;
            case "profesora_marta_gil" -> 4.3d;
            case "profesor_diego_ramos" -> 4.2d;
            case "profesora_carmen_prieto" -> 4.1d;
            case "profesor_sergio_hidalgo" -> 4.6d;
            case "profesora_elena_cruz" -> 4.7d;
            case "profesor_raul_ortega" -> 4.0d;
            case "profesora_noelia_sanchez" -> 4.8d;
            case "profesor_alberto_medina" -> 4.3d;
            case "profesora_patricia_leon" -> 4.5d;
            case "profesor_hugo_varela" -> 4.2d;
            case "profesora_silvia_ferrer" -> 4.6d;
            case "profesor_ivan_serrano" -> 4.1d;
            case "profesora_aitana_romero" -> 4.4d;
            case "profesor_marcos_pineda" -> 4.7d;
            case "profesora_nuria_casas" -> 4.5d;
            case "profesor_oscar_medina" -> 4.6d;
            case "profesora_lucia_navarro" -> 4.2d;
            case "profesora_sandra_molina" -> 4.8d;
            case "profesor_pablo_campos" -> 4.1d;
            case "profesora_elena_vargas" -> 4.4d;
            case "profesor_tomas_gil" -> 4.2d;
            default -> 4.4d;
        };
    }
    
    // Añade un rating nuevo para `professorId`, normalizando a [0..5] y persistiendo.
    public static void addRating(String professorId, double rating) {
        if (rating < 0.0) rating = 0.0;
        if (rating > 5.0) rating = 5.0;
        // `computeIfAbsent` busca la entrada `professorId` en el mapa `ALL_RATINGS`.
        // - Si existe, devuelve la lista asociada.
        // - Si no existe, crea una nueva `ArrayList<>`, la inserta en el mapa y la devuelve.
        // El resultado es la lista (existente o recién creada) sobre la que
        // llamamos `.add(rating)` para añadir la puntuación.
        //
        // Nota: esta operación NO es atómica respecto a hilos concurrentes; en
        // entornos multihilo podría requerirse sincronización externa si varias
        // hebras pueden llamar a `addRating` simultáneamente para el mismo id.
        ALL_RATINGS.computeIfAbsent(professorId, k -> new ArrayList<>()).add(rating);
        saveRatingsToFile();
    }
    
    // Guarda una lista de aspectos (tags) seleccionados por el usuario para un profesor.
    // Evita duplicados y persiste el resultado.
    public static void setSavedAspects(String professorId, List<String> aspects) {
        if (aspects != null) {
            // Obtener (o crear si no existe) la lista de aspectos guardados
            // para `professorId`. `computeIfAbsent` devuelve la lista existente
            // o inserta y devuelve una nueva `ArrayList<>` si la clave no existe.
            // De este modo evitamos la comprobación manual de existencia.
            List<String> currentAspects = SAVED_ASPECTS.computeIfAbsent(professorId, key -> new ArrayList<>());

            // Para cada aspecto propuesto comprobamos varias condiciones antes
            // de añadirlo:
            // - `aspect != null`: evitar NPEs y entradas nulas.
            // - `!aspect.isBlank()`: evitar entradas vacías o solo espacios.
            // - `!currentAspects.contains(aspect)`: evitar duplicados en la lista.
            // Nota: `contains` hace una búsqueda lineal O(n); si se prevén muchas
            // entradas o comprobaciones frecuentes, podría usarse un Set auxiliar
            // para acelerar la detección de duplicados.
            for (String aspect : aspects) {
                if (aspect != null && !aspect.isBlank() && !currentAspects.contains(aspect)) {
                    currentAspects.add(aspect);
                }
            }
            saveRatingsToFile();
        }
    }
    
    // Devuelve una copia mutable de los aspectos guardados para `professorId`.
    public static List<String> getSavedAspects(String professorId) {
        // Obtener la lista interna de aspectos para `professorId`, o una lista
        // vacía si no existe entrada en el mapa. `getOrDefault` garantiza que
        // `aspects` nunca sea null aquí.
        List<String> aspects = SAVED_ASPECTS.getOrDefault(professorId, new ArrayList<>());

        // Devolvemos una copia mutable (`new ArrayList<>(aspects)`) en lugar de
        // devolver la referencia directa a la lista almacenada en `SAVED_ASPECTS`.
        // Esto protege el estado interno del repositorio: el caller puede
        // modificar la lista retornada sin afectar los datos guardados en memoria
        // (para persistir cambios debe usarse `setSavedAspects`).
        return new ArrayList<>(aspects);
    }

    // Traduce una consideración/aspecto usando: tabla interna -> clave canónica -> ResourceBundle.
    // Además, para la presentación, si la consideración resulta ser muy larga
    // insertamos un salto de línea tras la primera " y " (conjunción en español)
    // para que las tarjetas no se estiren excesivamente en horizontal.
    // Devuelve texto plain o HTML (comienza con "<html>") listo para usar en JLabel.
    public static String localizeConsideration(String value, ResourceBundle bundle) {
        String translatedNote = localizedNoteForValue(value, bundle);
        String result;
        if (translatedNote != null) {
            result = translatedNote;
        } else {
            String key = considerationKeyForValue(value);
            if (key != null && bundle != null && bundle.containsKey(key)) {
                result = bundle.getString(key);
            } else {
                result = value == null ? "" : value;
            }
        }

        // Formateo adicional para presentación: si es español y la cadena
        // sobrepasa un umbral razonable, insertar un salto de línea tras
        // la primera " y " para mejorar lectura. Envolvemos en HTML y
        // escapamos caracteres especiales.
        return formatConsiderationForDisplay(result, bundle);
    }

    // Formatea una consideración para mostrar en UI.
    private static String formatConsiderationForDisplay(String text, ResourceBundle bundle) {
        if (text == null) return "";

        // Umbral a partir del cual intentamos partir la frase (caracteres).
        final int WRAP_THRESHOLD = 48;

        String safe = text.trim();

        // Solo aplicamos la regla específica de " y " para bundles en español
        // (o cuando no hay bundle explícito, asumimos español por defecto).
        boolean isEnglish = isEnglishBundle(bundle);

        if (!isEnglish && safe.length() > WRAP_THRESHOLD && safe.contains(" y ")) {
            // Para evitar que el tag <br> sea escapado y mostrado como texto,
            // dividimos en dos partes alrededor de la primera ' y ', escapamos
            // cada parte por separado y luego ensamblamos con un <br> real.
            int idx = safe.indexOf(" y ");
            String left = safe.substring(0, idx);
            String right = safe.substring(idx + 3); // saltamos " y "
            String leftEsc = escapeHtml(left.trim());
            String rightEsc = escapeHtml(right.trim());
            return "<html><div style='width: 205px; padding: 0 4px;'>" + leftEsc + " y<br>" + rightEsc + "</div></html>";
        }

        // Escapar HTML básico y envolver en un ancho fijo para que JLabel
        // haga el wrapping correcto cuando se presente como HTML.
        String escaped = escapeHtml(safe);
        return "<html><div style='width: 205px; padding: 0 4px;'>" + escaped + "</div></html>";
    }

    // Escapa los caracteres HTML básicos para evitar HTML injection en los labels.
    private static String escapeHtml(String s) {
        if (s == null) return null;
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // Devuelve la clave canónica para una consideración, o el valor original si no hay clave.
    public static String considerationIdentity(String value) {
        String key = considerationKeyForValue(value);
        return key != null ? key : value;
    }

    // Traduce una lista de consideraciones aplicando `localizeConsideration` a cada elemento.
    public static List<String> localizeConsiderations(List<String> values, ResourceBundle bundle) {
        List<String> localized = new ArrayList<>();
        for (String value : values) {
            localized.add(localizeConsideration(value, bundle));
        }
        return localized;
    }

    // Normaliza variantes textuales a claves de ResourceBundle conocidas.
    // Permite aceptar entradas en español, inglés o claves ya formateadas.
    private static String considerationKeyForValue(String value) {
        return switch (value == null ? "" : value.trim()) {
            case "profesor.considera_pasa_lista", "Pasa Lista", "Takes attendance" -> "profesor.considera_pasa_lista";
            case "profesor.considera_explica_bien", "Explica bien", "Explains well" -> "profesor.considera_explica_bien";
            case "profesor.considera_revisa_practicas", "Revisa las prácticas", "Reviews assignments" -> "profesor.considera_revisa_practicas";
            case "profesor.considera_hace_parciales", "Hace parciales", "Uses midterms" -> "profesor.considera_hace_parciales";
            case "valoracion.aspecto_lee_pdf", "Lee PDF", "Reads PDFs" -> "valoracion.aspecto_lee_pdf";
            case "valoracion.aspecto_examen_dificil", "Examen Difícil", "Hard exam" -> "valoracion.aspecto_examen_dificil";
            case "valoracion.aspecto_asistencia_obligatoria", "Asistencia Obligatoria", "Mandatory attendance" -> "valoracion.aspecto_asistencia_obligatoria";
            case "valoracion.aspecto_examen_test", "Examen Tipo Test", "Multiple-choice exam" -> "valoracion.aspecto_examen_test";
            case "valoracion.aspecto_muy_practico", "Muy Práctico", "Very practical" -> "valoracion.aspecto_muy_practico";
            case "valoracion.aspecto_buen_material", "Buen material", "Good materials" -> "valoracion.aspecto_buen_material";
            default -> null;
        };
    }

    // Traducciones directas de notas en español al inglés si el bundle es inglés.
    // Esto cubre casos donde el texto de la nota se almacena literalmente.
    private static String localizedNoteForValue(String value, ResourceBundle bundle) {
        if (!isEnglishBundle(bundle) || value == null) {
            return null;
        }

        return switch (value.trim()) {
            case "Prepara ejercicios extra para quien quiera ampliar." -> "Provides extra exercises for anyone who wants to go further.";
            case "Explica con mucho detalle los pasos de resolución." -> "Explains the solution steps in great detail.";
            case "Comparte resúmenes antes de los parciales." -> "Shares summaries before midterms.";
            case "Resuelve dudas con bastante rapidez." -> "Resolves doubts quite quickly.";
            case "Prioriza el trabajo práctico." -> "Prioritizes practical work.";
            case "Revisa el código con ejemplos muy visuales." -> "Reviews the code with very visual examples.";
            case "Suele centrar la clase en problemas resueltos." -> "Usually focuses class on solved problems.";
            case "Aporta ejercicios guiados para practicar." -> "Provides guided exercises to practice.";
            case "Insiste en interpretar bien los datos." -> "Insists on interpreting the data correctly.";
            case "Trabaja con ejemplos de casos reales." -> "Works with real-case examples.";
            case "Combina teoría y demostraciones cortas." -> "Combines theory and short demonstrations.";
            case "Da prioridad a la comprensión conceptual." -> "Gives priority to conceptual understanding.";
            case "Relaciona la materia con ejemplos de empresa." -> "Relates the subject to company examples.";
            case "Pide que se lleven las lecturas al día." -> "Expects students to keep up with the readings.";
            case "Cuida especialmente la calidad del código." -> "Pays special attention to code quality.";
            case "Trabaja con entregas parciales y feedback continuo." -> "Works with partial submissions and continuous feedback.";
            case "Explica la materia con esquemas de hardware." -> "Explains the subject with hardware diagrams.";
            case "Valora mucho la participación en clase." -> "Values class participation highly.";
            case "Suele usar problemas cortos al final de cada tema." -> "Usually uses short problems at the end of each topic.";
            case "Insiste en entender la base física de cada bloque." -> "Insists on understanding the physical basis of each block.";
            case "Recomienda practicar con muchos ejercicios." -> "Recommends practicing with many exercises.";
            case "Deja bastante margen para resolver dudas." -> "Leaves plenty of room to ask questions.";
            case "Se centra en buenas prácticas y diseño." -> "Focuses on good practices and design.";
            case "Propone pequeñas implementaciones de clase." -> "Proposes small in-class implementations.";
            case "Subraya la importancia del modelo relacional." -> "Emphasizes the importance of the relational model.";
            case "Revisa entregas con bastante detalle." -> "Reviews submissions in quite a lot of detail.";
            case "Suele conectar la teoría con ejemplos del sistema real." -> "Usually connects theory with examples from the real system.";
            case "Pide que se prueben los ejercicios en casa." -> "Asks that exercises be tested at home.";
            case "Da mucha importancia a la documentación." -> "Places a lot of importance on documentation.";
            case "Entrega rúbricas claras antes de cada práctica." -> "Provides clear rubrics before each lab.";
            case "Trabaja desde bloques muy concretos." -> "Works from very concrete blocks.";
            case "Suele dedicar tiempo a ejercicios guiados." -> "Usually devotes time to guided exercises.";
            case "Relaciona la materia con casos de administración real." -> "Relates the subject to real administration cases.";
            case "Valora la entrega limpia y bien organizada." -> "Values clean, well-organized submissions.";
            case "Insiste en la eficiencia de cada algoritmo." -> "Insists on the efficiency of each algorithm.";
            case "Revisa la complejidad en cada práctica." -> "Reviews complexity in each lab.";
            case "Busca que el alumnado vea el proceso completo." -> "Seeks for students to see the complete process.";
            case "Repite varios ejemplos antes de cada entrega." -> "Repeats several examples before each submission.";
            case "Explica los conceptos con ejemplos de datos reales." -> "Explains concepts with real data examples.";
            case "Suele dejar tiempo para preguntas al final." -> "Usually leaves time for questions at the end.";
            case "Combina teoría con simulaciones." -> "Combines theory with simulations.";
            case "Pide revisar la configuración paso a paso." -> "Asks to review the configuration step by step.";
            case "Focaliza mucho en la parte práctica." -> "Focuses heavily on the practical side.";
            case "Revisa con detalle la estructura de los proyectos." -> "Reviews project structure in detail.";
            case "Se centra en topologías y protocolos." -> "Focuses on topologies and protocols.";
            case "Pide razonamiento, no solo memorización." -> "Asks for reasoning, not just memorization.";
            case "Aporta casos reales para contextualizar la materia." -> "Provides real cases to contextualize the subject.";
            case "Recomienda leer la normativa con calma." -> "Recommends reading the regulations calmly.";
            case "Suele trabajar con ejemplos de procedimientos." -> "Usually works with procedure examples.";
            case "Da importancia a la precisión en las respuestas." -> "Gives importance to precision in answers.";
            default -> null;
        };
    }

    // Traduce el texto de tutorías/horario de consulta al idioma activo.
    // El dato base se conserva en el modelo, y la localización se aplica solo
    // en la presentación. En español, además, insertamos un salto de línea tras
    // la primera "y" para evitar que la fila se estire demasiado horizontalmente.
    public static String localizeOfficeHours(String value, ResourceBundle bundle) {
        if (value == null) {
            return null;
        }

        if (isEnglishBundle(bundle)) {
            return switch (value.trim()) {
                case "martes y jueves de 10:00 a 12:30" -> "Tuesday and Thursday\n10:00-12:30";
                case "lunes y miércoles de 12:00 a 14:00" -> "Monday and Wednesday\n12:00-14:00";
                case "lunes de 16:00 a 18:00 y miércoles de 9:00 a 11:00" -> "Monday 16:00-18:00\nand Wednesday 9:00-11:00";
                case "martes de 9:30 a 11:30 y jueves de 12:00 a 14:00" -> "Tuesday 9:30-11:30\nand Thursday 12:00-14:00";
                case "martes y jueves de 11:00 a 13:00" -> "Tuesday and Thursday\n11:00-13:00";
                case "lunes y miércoles de 10:00 a 12:00" -> "Monday and Wednesday\n10:00-12:00";
                case "martes de 16:00 a 18:00 y viernes de 10:00 a 12:00" -> "Tuesday 16:00-18:00\nand Friday 10:00-12:00";
                case "lunes de 11:00 a 13:00 y jueves de 16:00 a 18:00" -> "Monday 11:00-13:00\nand Thursday 16:00-18:00";
                case "miércoles de 12:00 a 14:00 y viernes de 9:00 a 11:00" -> "Wednesday 12:00-14:00\nand Friday 9:00-11:00";
                case "lunes y jueves de 8:30 a 10:30" -> "Monday and Thursday\n8:30-10:30";
                case "martes de 9:00 a 11:00 y jueves de 12:00 a 14:00" -> "Tuesday 9:00-11:00\nand Thursday 12:00-14:00";
                case "lunes y miércoles de 17:00 a 19:00" -> "Monday and Wednesday\n17:00-19:00";
                case "martes y viernes de 10:00 a 12:00" -> "Tuesday and Friday\n10:00-12:00";
                case "miércoles de 9:30 a 11:30 y viernes de 12:00 a 14:00" -> "Wednesday 9:30-11:30\nand Friday 12:00-14:00";
                case "lunes de 12:00 a 14:00 y jueves de 9:00 a 11:00" -> "Monday 12:00-14:00\nand Thursday 9:00-11:00";
                case "martes y jueves de 8:30 a 10:30" -> "Tuesday and Thursday\n8:30-10:30";
                case "lunes de 10:00 a 12:00 y miércoles de 16:00 a 18:00" -> "Monday 10:00-12:00\nand Wednesday 16:00-18:00";
                case "martes de 15:30 a 17:30 y jueves de 9:00 a 11:00" -> "Tuesday 15:30-17:30\nand Thursday 9:00-11:00";
                case "lunes y jueves de 12:00 a 14:00" -> "Monday and Thursday\n12:00-14:00";
                case "miércoles de 11:00 a 13:00 y viernes de 9:00 a 11:00" -> "Wednesday 11:00-13:00\nand Friday 9:00-11:00";
                case "martes y viernes de 12:00 a 14:00" -> "Tuesday and Friday\n12:00-14:00";
                case "lunes y miércoles de 9:00 a 11:00" -> "Monday and Wednesday\n9:00-11:00";
                case "martes de 16:00 a 18:00 y jueves de 10:00 a 12:00" -> "Tuesday 16:00-18:00\nand Thursday 10:00-12:00";
                case "lunes y jueves de 11:00 a 13:00" -> "Monday and Thursday\n11:00-13:00";
                case "martes y viernes de 9:00 a 11:00" -> "Tuesday and Friday\n9:00-11:00";
                default -> value;
            };
        }

        if (value.contains(" y ")) {
            return value.replaceFirst(" y ", " y\n");
        }

        return value;
    }

    private static boolean isEnglishBundle(ResourceBundle bundle) {
        // Comprueba si el ResourceBundle representa el idioma inglés.
        // - Primero verificamos que `bundle` no sea null para evitar NPE.
        // - `bundle.getLocale().getLanguage()` devuelve el código de idioma
        //   en minúsculas (por ejemplo "en", "es"). Usamos
        //   `equalsIgnoreCase` por si acaso el código viene con distinto case.
        // - Devolver `true` solo cuando el código de idioma sea "en".
        return bundle != null && "en".equalsIgnoreCase(bundle.getLocale().getLanguage());
    }
    
    // Carga desde disco el fichero JSON de ratings y aspectos y actualiza las estructuras en memoria.
    private static void loadRatingsFromFile() {
        try {
            if (Files.exists(Paths.get(RATINGS_FILE))) {
                String content = Files.readString(Paths.get(RATINGS_FILE));
                parseRatingsJson(content); //fx def en este archivo
            }
        } catch (Exception e) {
            // Silenciar errores de carga, usar ratings por defecto
        }
    }
    
    // Serializa `ALL_RATINGS` y `SAVED_ASPECTS` a un JSON simple y lo escribe en disco.
    // Implementación manual (sin librería JSON) para mantener el demo ligero.
    private static void saveRatingsToFile() {
        try {
            StringBuilder json = new StringBuilder("{");
            boolean first = true;
            
            for (String professorId : ALL_RATINGS.keySet()) {
                if (!first) json.append(",");
                json.append("\"").append(professorId).append("\":{");
                
                // Guardar ratings
                List<Double> ratings = ALL_RATINGS.get(professorId);
                json.append("\"ratings\":[");
                for (int i = 0; i < ratings.size(); i++) {
                    if (i > 0) json.append(",");
                    json.append(ratings.get(i));
                }
                json.append("]");
                
                // Guardar aspects
                if (SAVED_ASPECTS.containsKey(professorId)) {
                    List<String> aspects = SAVED_ASPECTS.get(professorId);
                    json.append(",\"aspects\":[");
                    for (int i = 0; i < aspects.size(); i++) {
                        if (i > 0) json.append(",");
                        json.append("\"").append(escapeJson(aspects.get(i))).append("\"");
                    }
                    json.append("]");
                }
                
                json.append("}");
                first = false;
            }
            json.append("}");
            
            Files.writeString(Paths.get(RATINGS_FILE), json.toString());
        } catch (Exception e) {
            // Silenciar errores de guardado
        }
    }
    
    // Escapa comillas y saltos de línea para incrustar en JSON simple.
    // Cada llamada a `replace(a, b)` reemplaza las ocurrencias de `a` por `b`.
    // Por ejemplo: la comilla doble `"` se convierte en `\"` y el carácter
    // de nueva línea '\n' se convierte en la secuencia de escape `\n`.
    // Atención: en los literales Java vemos `\\n` (dos barras) porque en
    // el código fuente la barra invertida está escapada; el resultado final
    // en el JSON contendrá la secuencia `\n`.
    // Nota: este método no comprueba `null`; si `str` pudiera ser null,
    // conviene verificarlo antes de llamar para evitar NullPointerException.
    private static String escapeJson(String str) {
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
    
    // Parse a mano un JSON sencillo con la estructura esperada y rellena ALL_RATINGS y SAVED_ASPECTS.
    private static void parseRatingsJson(String json) {
        // Parse JSON structure: {"prof1":{"ratings":[4.5,4.2],"aspects":["a","b"]},...}
        try {
            String content = json.trim();
            if (content.startsWith("{") && content.endsWith("}")) {
                content = content.substring(1, content.length() - 1);
                if (!content.isEmpty()) {
                    int braceDepth = 0;
                    int lastComma = -1;
                    
                    for (int i = 0; i < content.length(); i++) {
                        char c = content.charAt(i);
                        if (c == '{') braceDepth++;
                        else if (c == '}') braceDepth--;
                        else if (c == ',' && braceDepth == 0) {
                            String entry = content.substring(lastComma + 1, i).trim();
                            if (!entry.isEmpty()) parseProfessorEntry(entry);
                            lastComma = i;
                        }
                    }
                    // Última entrada
                    String entry = content.substring(lastComma + 1).trim();
                    if (!entry.isEmpty()) parseProfessorEntry(entry);
                }
            }
        } catch (Exception e) {
            // Ignorar errores de parsing
        }
    }
    
    // Parsea una entrada del JSON con la forma "profId":{...} y extrae ratings/aspects.
    private static void parseProfessorEntry(String entry) {
        int colonIdx = entry.indexOf(":");
        if (colonIdx <= 0) return;
        
        String professorId = entry.substring(0, colonIdx).trim().replaceAll("\"", "");
        String data = entry.substring(colonIdx + 1).trim();
        
        if (data.startsWith("{") && data.endsWith("}")) {
            data = data.substring(1, data.length() - 1);
            
            // Parse ratings array
            int ratingsStart = data.indexOf("\"ratings\":[");
            if (ratingsStart >= 0) {
                int arrayStart = data.indexOf("[", ratingsStart);
                int arrayEnd = data.indexOf("]", arrayStart);
                if (arrayEnd > arrayStart) {
                    String ratingsStr = data.substring(arrayStart + 1, arrayEnd);
                    List<Double> ratings = new ArrayList<>();
                    for (String r : ratingsStr.split(",")) {
                        try {
                            ratings.add(Double.parseDouble(r.trim()));
                        } catch (Exception ignored) {}
                    }
                    if (!ratings.isEmpty()) {
                        ALL_RATINGS.put(professorId, ratings);
                    }
                }
            }
            
            // Parse aspects array
            int aspectsStart = data.indexOf("\"aspects\":[");
            if (aspectsStart >= 0) {
                int arrayStart = data.indexOf("[", aspectsStart);
                int arrayEnd = data.indexOf("]", arrayStart);
                if (arrayEnd > arrayStart) {
                    String aspectsStr = data.substring(arrayStart + 1, arrayEnd);
                    List<String> aspects = new ArrayList<>();
                    for (String a : aspectsStr.split(",")) {
                        a = a.trim().replaceAll("\"", "").replace("\\\"", "\"");
                        if (!a.isEmpty()) {
                            aspects.add(a);
                        }
                    }
                    if (!aspects.isEmpty()) {
                        SAVED_ASPECTS.put(professorId, aspects);
                    }
                }
            }
        }
    }

    // Registra un nuevo `ProfessorProfile` en los mapas en memoria y actualiza el índice por asignatura.
    private static void register(ProfessorProfile profile) {
        // Insertar el perfil en el mapa principal `PROFILES` usando su id como clave.
        // - `profile.getId()` devuelve el identificador único del profesor (p.ej. "profesor_antonio_lopez").
        // - `PROFILES` es un LinkedHashMap, por lo que preserva el orden de inserción
        //   (útil para presentar una lista estable de profesores).
        PROFILES.put(profile.getId(), profile);

        // Para cada asignatura asociada al perfil, añadimos el perfil al índice
        // `BY_SUBJECT`, que es un mapa subjectKey -> List<ProfessorProfile>.
        // `computeIfAbsent(subjectKey, key -> new ArrayList<>())` hace dos cosas:
        // 1) Si ya existe una lista para `subjectKey` la devuelve.
        // 2) Si no existe, crea una nueva `ArrayList<>`, la inserta en el mapa y la devuelve.
        // Esto evita tener que comprobar manualmente si la clave existe y simplifica
        // la inicialización perezosa del listado por asignatura.
        // Finalmente llamamos `.add(profile)` sobre la lista para registrar el perfil
        // en esa asignatura.
        for (String subjectKey : profile.getSubjectKeys()) {
            BY_SUBJECT.computeIfAbsent(subjectKey, key -> new ArrayList<>()).add(profile);
        }
    }

    // Helpers de construcción: devuelven listas inmutables a partir de varargs.
    private static List<String> subjects(String... keys) {
        // `String... keys` -> sintaxis de varargs en Java: el llamador puede
        // pasar cero o más `String` separados por comas, p.ej.
        //   subjects("common.linear_algebra", "common.calculus")
        // y dentro del método `keys` se ve como un `String[]` (un array).
        //
        // `List.of(keys)` crea una lista inmutable (no se puede modificar) con
        // los elementos proporcionados. Es equivalente a `List.of()` con los
        // elementos de `keys` como argumentos y fue introducido en Java 9.
        // Advertencias importantes:
        // - La lista devuelta es inmodificable: operaciones como add/remove lanzarán
        //   UnsupportedOperationException.
        // - No permite elementos nulos: si alguna entrada es null, se lanzará
        //   NullPointerException al construir la lista.
        // - Si se llama explícitamente con `subjects((String[]) null)` `keys` será
        //   null y List.of(keys) lanzará NullPointerException; llamar sin argumentos
        //   produce un array vacío y devuelve una lista vacía.
        return List.of(keys);
    }

    private static List<String> notes(String... items) {
        return List.of(items);
    }
}
