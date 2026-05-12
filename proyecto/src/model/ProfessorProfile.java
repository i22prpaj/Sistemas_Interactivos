package model;

import java.util.List;
import java.util.ResourceBundle;

public final class ProfessorProfile {

    private final String id;
    private final String displayName;
    private final String department;
    private final String email;
    private final String office;
    private final String phone;
    private final String officeHours;
    private final List<String> subjectKeys;
    private final List<String> considerations;

    public ProfessorProfile(String id, String displayName, String department, String email, String office,
            String phone, String officeHours, List<String> subjectKeys, List<String> considerations) {
        this.id = id;
        this.displayName = displayName;
        this.department = department;
        this.email = email;
        this.office = office;
        this.phone = phone;
        this.officeHours = officeHours;
        this.subjectKeys = List.copyOf(subjectKeys);
        this.considerations = List.copyOf(considerations);
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public String getOffice() {
        return office;
    }

    public String getPhone() {
        return phone;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public List<String> getSubjectKeys() {
        return subjectKeys;
    }

    public List<String> getConsiderations() {
        return considerations;
    }

    public String[] getLocalizedSubjectNames(ResourceBundle bundle) {
        return subjectKeys.stream()
            .map(key -> bundle.containsKey(key) ? bundle.getString(key) : key)
            .toArray(String[]::new);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
