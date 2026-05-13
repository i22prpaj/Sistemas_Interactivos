package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CredentialStore {

    private static final Map<String, String> PASSWORDS = new LinkedHashMap<>();
    private static final Path STORE_PATH = Paths.get(System.getProperty("user.home"), ".sisint_credentials.properties");

    static {
        loadDefaults();
        loadFromDisk();
    }

    private CredentialStore() {
    }

    public static boolean authenticate(String email, String password) {
        if (email == null || password == null) {
            return false;
        }
        String storedPassword = PASSWORDS.get(normalizeEmail(email));
        return storedPassword != null && storedPassword.equals(password);
    }

    public static boolean changePassword(String email, String currentPassword, String newPassword) {
        if (email == null || currentPassword == null || newPassword == null) {
            return false;
        }

        String normalizedEmail = normalizeEmail(email);
        String storedPassword = PASSWORDS.get(normalizedEmail);
        if (storedPassword == null || !storedPassword.equals(currentPassword)) {
            return false;
        }

        PASSWORDS.put(normalizedEmail, newPassword);
        saveToDisk();
        return true;
    }

    public static void ensureAccountExists(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        String normalizedEmail = normalizeEmail(email);
        PASSWORDS.putIfAbsent(normalizedEmail, "1234");
        saveToDisk();
    }

    private static void loadDefaults() {
        PASSWORDS.put("moderador@uco.es", "1234");
        PASSWORDS.put("elena.ruiz@uco.es", "1234");
    }

    private static void loadFromDisk() {
        if (!Files.exists(STORE_PATH)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(STORE_PATH, StandardCharsets.UTF_8);
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                int idx = trimmed.indexOf('=');
                if (idx > 0) {
                    String email = normalizeEmail(trimmed.substring(0, idx).trim());
                    String password = trimmed.substring(idx + 1).trim();
                    if (!email.isEmpty()) {
                        PASSWORDS.put(email, password);
                    }
                }
            }
        } catch (IOException ignored) {
        }
    }

    private static void saveToDisk() {
        try {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : PASSWORDS.entrySet()) {
                builder.append(entry.getKey()).append('=').append(entry.getValue()).append('\n');
            }
            Files.writeString(STORE_PATH, builder.toString(), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}