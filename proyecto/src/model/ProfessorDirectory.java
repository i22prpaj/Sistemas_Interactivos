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

public final class ProfessorDirectory {

    private static final Map<String, ProfessorProfile> PROFILES = new LinkedHashMap<>();
    private static final Map<String, List<ProfessorProfile>> BY_SUBJECT = new LinkedHashMap<>();
    private static final Map<String, List<Double>> ALL_RATINGS = new LinkedHashMap<>();  // Todos los ratings
    private static final Map<String, List<String>> SAVED_ASPECTS = new LinkedHashMap<>();  // Aspectos guardados
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
        return PROFILES.values().stream().findFirst().orElse(null);
    }

    public static List<ProfessorProfile> getBySubject(String subjectKey) {
        List<ProfessorProfile> profiles = BY_SUBJECT.get(subjectKey);
        if (profiles == null || profiles.isEmpty()) {
            ProfessorProfile fallback = getDefaultProfile();
            return fallback == null ? List.of() : List.of(fallback);
        }
        return List.copyOf(profiles);
    }

    public static double getRating(String professorId) {
        // Si existen ratings personalizados, calcular promedio; si no, usar el rating por defecto
        if (ALL_RATINGS.containsKey(professorId) && !ALL_RATINGS.get(professorId).isEmpty()) {
            List<Double> ratings = ALL_RATINGS.get(professorId);
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
    
    public static void addRating(String professorId, double rating) {
        if (rating < 0.0) rating = 0.0;
        if (rating > 5.0) rating = 5.0;
        ALL_RATINGS.computeIfAbsent(professorId, k -> new ArrayList<>()).add(rating);
        saveRatingsToFile();
    }
    
    public static void setSavedAspects(String professorId, List<String> aspects) {
        if (aspects != null) {
            List<String> currentAspects = SAVED_ASPECTS.computeIfAbsent(professorId, key -> new ArrayList<>());
            for (String aspect : aspects) {
                if (aspect != null && !aspect.isBlank() && !currentAspects.contains(aspect)) {
                    currentAspects.add(aspect);
                }
            }
            saveRatingsToFile();
        }
    }
    
    public static List<String> getSavedAspects(String professorId) {
        List<String> aspects = SAVED_ASPECTS.getOrDefault(professorId, new ArrayList<>());
        return new ArrayList<>(aspects);
    }
    
    private static void loadRatingsFromFile() {
        try {
            if (Files.exists(Paths.get(RATINGS_FILE))) {
                String content = Files.readString(Paths.get(RATINGS_FILE));
                parseRatingsJson(content);
            }
        } catch (Exception e) {
            // Silenciar errores de carga, usar ratings por defecto
        }
    }
    
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
    
    private static String escapeJson(String str) {
        return str.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
    
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

    private static void register(ProfessorProfile profile) {
        PROFILES.put(profile.getId(), profile);
        for (String subjectKey : profile.getSubjectKeys()) {
            BY_SUBJECT.computeIfAbsent(subjectKey, key -> new ArrayList<>()).add(profile);
        }
    }

    private static List<String> subjects(String... keys) {
        return List.of(keys);
    }

    private static List<String> notes(String... items) {
        return List.of(items);
    }
}
