package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ProfessorDirectory {

    private static final Map<String, ProfessorProfile> PROFILES = new LinkedHashMap<>();
    private static final Map<String, List<ProfessorProfile>> BY_SUBJECT = new LinkedHashMap<>();

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
