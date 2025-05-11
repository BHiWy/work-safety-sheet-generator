package services;

import jakarta.persistence.EntityManager;
import org.generator.entities.Student;
import org.generator.services.StudentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private StudentService studentService;

    /**
     * Tests the successful saving of a student.
     * Verifies that the {@link EntityManager#persist(Object)} method is called once
     * with the provided {@link Student} object.
     */
    @Test
    void testSaveStudentSuccessfully() {
        Student student = new Student();
        student.setFirstName("Gigel");
        student.setLastName("Frone");

        studentService.save(student);

        verify(entityManager, times(1)).persist(student);
    }

    /**
     * Tests the extraction of name details from a full name with a single first name.
     * Verifies that the {@link StudentService#extractNameDetails(String)} method correctly
     * identifies the last name and the single first name.
     */
    @Test
    void testExtractNameDetails_SingleFirstName() {
        String fullName = "Popescu Ion";
        Student extractedStudent = StudentService.extractNameDetails(fullName);
        Assertions.assertEquals("Popescu", extractedStudent.getLastName());
        Assertions.assertEquals("Ion", extractedStudent.getFirstName());
        Assertions.assertEquals("", extractedStudent.getPaternalInitial()); // Modifică aserțiunea aici
    }

    /**
     * Tests the extraction of name details from a full name with multiple first names.
     * Verifies that the {@link StudentService#extractNameDetails(String)} method correctly
     * combines multiple words into the first name.
     */
    @Test
    void testExtractNameDetails_MultipleFirstNames() {
        String fullName = "Ionescu Maria Elena";
        Student extractedStudent = StudentService.extractNameDetails(fullName);
        Assertions.assertEquals("Ionescu", extractedStudent.getLastName());
        Assertions.assertEquals("Maria Elena", extractedStudent.getFirstName());
        Assertions.assertEquals("", extractedStudent.getPaternalInitial()); // Modifică aserțiunea aici
    }


    /**
     * Tests the extraction of name details from a full name with a paternal initial.
     * Verifies that the {@link StudentService#extractNameDetails(String)} method correctly
     * identifies and extracts the paternal initial.
     */
    @Test
    void testExtractNameDetails_WithPaternalInitial() {
        String fullName = "Georgescu V. Andrei";
        Student extractedStudent = StudentService.extractNameDetails(fullName);
        Assertions.assertEquals("Georgescu", extractedStudent.getLastName());
        Assertions.assertEquals("Andrei", extractedStudent.getFirstName());
        Assertions.assertEquals("V.", extractedStudent.getPaternalInitial());
    }


    /**
     * Tests the extraction of name details from a full name with mixed first names and initials.
     * Verifies that the {@link StudentService#extractNameDetails(String)} method correctly
     * separates first names and paternal initials.
     */
    @Test
    void testExtractNameDetails_MixedFirstNamesAndInitials() {
        String fullName = "Popa I. Vasile";
        Student extractedStudent = StudentService.extractNameDetails(fullName);
        Assertions.assertEquals("Popa", extractedStudent.getLastName());
        Assertions.assertEquals("Vasile", extractedStudent.getFirstName());
        Assertions.assertEquals("I.", extractedStudent.getPaternalInitial());
    }



    /**
     * Tests the extraction of name details from an empty full name.
     * Verifies that the {@link StudentService#extractNameDetails(String)} method returns
     * a {@link Student} object with null or empty fields.
     */
    @Test
    void testExtractNameDetails_EmptyFullName() {
        String fullName = "";
        Student extractedStudent = StudentService.extractNameDetails(fullName);
        Assertions.assertNull(extractedStudent.getLastName());
        Assertions.assertNull(extractedStudent.getFirstName());
        Assertions.assertNull(extractedStudent.getPaternalInitial());
    }

    /**
     * Tests the extraction of name details from a null full name.
     * Verifies that the {@link StudentService#extractNameDetails(String)} method returns
     * a {@link Student} object with null or empty fields.
     */
    @Test
    void testExtractNameDetails_NullFullName() {
        String fullName = null;
        Student extractedStudent = StudentService.extractNameDetails(fullName);
        Assertions.assertNull(extractedStudent.getLastName());
        Assertions.assertNull(extractedStudent.getFirstName());
        Assertions.assertNull(extractedStudent.getPaternalInitial());
    }

    /**
     * Tests the creation of an email address with a simple first and last name.
     * Verifies that the {@link StudentService#createEmail(String)} method generates
     * the correct email format.
     */
    @Test
    void testCreateEmail_SimpleName() {
        String fullName = "Popescu Ion";
        String email = studentService.createEmail(fullName);
        Assertions.assertEquals("ion.popescu@student.tuiasi.ro", email);
    }

    /**
     * Tests the creation of an email address with multiple first names.
     * Verifies that the {@link StudentService#createEmail(String)} method joins
     * multiple first names with a hyphen.
     */
    @Test
    void testCreateEmail_MultipleFirstNames() {
        String fullName = "Ionescu Maria Elena";
        String email = studentService.createEmail(fullName);
        Assertions.assertEquals("maria-elena.ionescu@student.tuiasi.ro", email);
    }

    /**
     * Tests the creation of an email address with diacritics in the name.
     * Verifies that the {@link StudentService#createEmail(String)} method removes
     * diacritics and converts the name to lowercase.
     */
    @Test
    void testCreateEmail_WithDiacritics() {
        String fullName = "Ștefănescu Ălin";
        String email = studentService.createEmail(fullName);
        Assertions.assertEquals("alin.stefanescu@student.tuiasi.ro", email);
    }

    /**
     * Tests the creation of an email address with trailing punctuation in the name.
     * Verifies that the {@link StudentService#createEmail(String)} method removes
     * trailing dots and commas.
     */
    @Test
    void testCreateEmail_WithTrailingPunctuation() {
        String fullName = "Marinescu, Ana.";
        String email = studentService.createEmail(fullName);
        Assertions.assertEquals("ana.marinescu@student.tuiasi.ro", email);
    }

    /**
     * Tests the creation of an email address with a paternal initial.
     * Verifies that the {@link StudentService#createEmail(String)} method ignores
     * single-letter paternal initials with a dot.
     */
    @Test
    void testCreateEmail_WithSinglePaternalInitial() {
        String fullName = "Georgescu V. Andrei";
        String email = studentService.createEmail(fullName);
        Assertions.assertEquals("andrei.georgescu@student.tuiasi.ro", email);
    }

    /**
     * Tests the creation of an email address with multiple paternal initials.
     * Verifies that the {@link StudentService#createEmail(String)} method ignores
     * multiple single-letter paternal initials with dots.
     */
    @Test
    void testCreateEmail_WithMultiplePaternalInitials() {
        String fullName = "Dumitrescu C.C. Mihai";
        String email = studentService.createEmail(fullName);
        Assertions.assertEquals("mihai.dumitrescu@student.tuiasi.ro", email);
    }

    /**
     * Tests the creation of an email address with mixed first names and initials.
     * Verifies that the {@link StudentService#createEmail(String)} method correctly
     * handles the combination.
     */
    @Test
    void testCreateEmail_MixedFirstNamesAndInitials() {
        String fullName = "Popa I. Vasile";
        String email = studentService.createEmail(fullName);
        Assertions.assertEquals("vasile.popa@student.tuiasi.ro", email);
    }

    /**
     * Tests the creation of an email address with a single name part.
     * Verifies that the {@link StudentService#createEmail(String)} method returns
     * an empty string if the full name has only one part.
     */
    @Test
    void testCreateEmail_SingleNamePart() {
        String fullName = "Gigel";
        String email = studentService.createEmail(fullName);
        Assertions.assertEquals("", email);
    }

    /**
     * Tests the creation of an email address with an empty full name.
     * Verifies that the {@link StudentService#createEmail(String)} method returns
     * an empty string if the full name is empty.
     */
    @Test
    void testCreateEmail_EmptyFullName() {
        String fullName = "";
        String email = studentService.createEmail(fullName);
        Assertions.assertEquals("", email);
    }

    /**
     * Tests the creation of an email address with a null full name.
     * Verifies that the {@link StudentService#createEmail(String)} method returns
     * an empty string if the full name is null.
     */
    @Test
    void testCreateEmail_NullFullName() {
        String fullName = null;
        String email = studentService.createEmail(fullName);
        Assertions.assertEquals("", email);
    }
}
