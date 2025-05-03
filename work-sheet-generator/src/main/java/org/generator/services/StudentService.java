package org.generator.services;

import org.generator.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Service class responsible for managing {@link Student} entities.
 * Provides functionalities for saving student information, extracting name details,
 * and creating student email addresses.
 */
@Transactional
@Service
@Slf4j
public class StudentService {
    private final EntityManager entityManager;

    @Autowired
    public StudentService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Persists a {@link Student} entity into the database.
     * This operation is transactional, ensuring data consistency.
     *
     * @param student the {@link Student} object to be saved.
     */
    @Transactional
    public void save(Student student) {
        entityManager.persist(student);
    }


    /**
     * Extracts the last name, first name(s), and paternal initial(s) from a full name string.
     * Assumes the first word is the last name and identifies paternal initials based on the format (e.g., "L.", "C.C.").
     *
     * @param fullName the full name of the student.
     * @return a {@link Student} object containing the extracted last name, first name, and paternal initial.
     */
    public static Student extractNameDetails(String fullName) {
        Student student = new Student();
        if (fullName == null || fullName.trim().isEmpty()) {
            return student;
        }

        String[] parts = fullName.trim().split("\\s+");
        if (parts.length < 2) {
            student.setFirstName(fullName.trim());
            return student;
        }

        // First part is the last name
        student.setLastName(parts[0].replaceAll("[.,]", ""));

        StringBuilder firstNameBuilder = new StringBuilder();
        StringBuilder parentalInitialBuilder = new StringBuilder();
        boolean firstNameStarted = false;

        for (int i = 1; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.matches("[A-Z]\\.")) {                 // Matches a single initial
                if (parentalInitialBuilder.length() > 0) {
                    parentalInitialBuilder.append(" ");
                }
                parentalInitialBuilder.append(part.toUpperCase());
            } else if (part.matches("[A-Z]\\.[A-Z]\\.")) {  // Matches two initials
                if (parentalInitialBuilder.length() > 0) {
                    parentalInitialBuilder.append(" ");
                }
                parentalInitialBuilder.append(part.toUpperCase());
            } else {
                if (firstNameStarted) {
                    firstNameBuilder.append(" ");
                }
                firstNameBuilder.append(part.replaceAll("[.,]", ""));
                firstNameStarted = true;
            }
        }

        student.setFirstName(firstNameBuilder.toString().trim());
        student.setPaternalInitial(parentalInitialBuilder.toString().trim());

        return student;
    }

    /**
     * Creates an email address for a student based on their name.
     * @param student the student's full name
     * @return the generated email address, or an empty string if the name is invalid.
     */
    public String createEmail(String student) {
        if (student == null || student.trim().isEmpty()) {
            System.out.println("Student is null or empty");
            return "";
        }

        String normalizedStudent = Normalizer.normalize(student, Normalizer.Form.NFD); //diacritic remover
        Pattern pattern = Pattern.compile("[\\p{InCombiningDiacriticalMarks}]");
        String studentWithoutDiacritics = pattern.matcher(normalizedStudent).replaceAll("");
        String lowerCaseStudent = studentWithoutDiacritics.toLowerCase();

        String[] parts = lowerCaseStudent.trim().split("\\s+");
        if (parts.length < 2) {
            return "";
        }

        String lastName = parts[0].replaceAll("[.,]", "");
        StringBuilder firstNameParts = new StringBuilder();
        boolean startedFirstName = false;

        for (int i = 1; i < parts.length; i++) {
            String currentPart = parts[i].trim();
            if (currentPart.length() == 2 && currentPart.matches("[a-z]\\.")) {
                continue; // Skip single parental initials (e.g., f.)
            }
            if (currentPart.length() > 2 && currentPart.matches("([a-z]\\.)+")) {
                continue; // Skip multiple parental initials (e.g., n.a.)
            }

            String cleanedPart = currentPart.replaceAll("[.,]", "");
            if (!cleanedPart.isEmpty()) {
                if (startedFirstName) {
                    firstNameParts.append("-");
                }
                firstNameParts.append(cleanedPart);
                startedFirstName = true;
            }
        }

        if (firstNameParts.isEmpty()) {
            return "";
        }

        return firstNameParts + "." + lastName + "@student.tuiasi.ro";
    }
}
