package chatapp;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

/**
 * Tests for JSONStorage using sample message data.
 *
 * @author dtsotetsi
 */
public class JSONStorageIT {

    /** Sample rows: {ID, HASH, RECIPIENT, MESSAGE}. */
    private static final String[][] SAMPLE_ROWS = {
        {"0123456789", "01:0:HITONIGHT", "+27718693002",
            "Hi Mike, can you join us for dinner tonight"},
        {"0234567890", "02:1:HITHANKS", "+27838884567",
            "Hi Keegan, did you receive the payment? Thanks"},
        {"0345678901", "03:2:LETSMEET", "+27721234567",
            "Let's meet at the \"main\" gate before noon"},
        {"0456789012", "04:3:SEEYOUTHERE", "+27609876543",
            "See you there"}
    };

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private Path defaultFile;
    private byte[] defaultFileBackup;

    @Before
    public void setUp() throws IOException {
        // Protect any real message.json in the working directory
        defaultFile = Paths.get(JSONStorage.DEFAULT_FILE);
        defaultFileBackup = Files.exists(defaultFile) ? Files.readAllBytes(defaultFile) : null;
    }

    @After
    public void tearDown() throws IOException {
        // Restore (or remove) message.json so tests leave no trace
        if (defaultFileBackup != null) {
            Files.write(defaultFile, defaultFileBackup);
        } else {
            Files.deleteIfExists(defaultFile);
        }
    }

    // ---------- readMessages(String) ----------

    @Test
    public void testReadMessages_String() throws IOException {
        File file = tempFolder.newFile("messages.json");
        String json = "[{\"messageID\":\"0123456789\",\"messageHash\":\"01:0:HITONIGHT\","
                + "\"recipient\":\"+27718693002\","
                + "\"message\":\"Hi Mike, can you join us for dinner tonight\"},"
                + "{\"messageID\":\"0234567890\",\"messageHash\":\"02:1:HITHANKS\","
                + "\"recipient\":\"+27838884567\","
                + "\"message\":\"Hi Keegan, did you receive the payment? Thanks\"}]";
        Files.write(file.toPath(), json.getBytes(StandardCharsets.UTF_8));

        String[][] result = JSONStorage.readMessages(file.getAbsolutePath());

        assertEquals(2, result.length);
        assertEquals("0123456789", result[0][JSONStorage.ID]);
        assertEquals("01:0:HITONIGHT", result[0][JSONStorage.HASH]);
        assertEquals("+27718693002", result[0][JSONStorage.RECIPIENT]);
        assertEquals("Hi Mike, can you join us for dinner tonight", result[0][JSONStorage.MESSAGE]);
        assertEquals("0234567890", result[1][JSONStorage.ID]);
        assertEquals("+27838884567", result[1][JSONStorage.RECIPIENT]);
    }

    @Test
    public void testReadMessages_String_fileDoesNotExist() {
        String[][] result = JSONStorage.readMessages("does_not_exist_12345.json");
        assertEquals(0, result.length);
    }

    @Test
    public void testReadMessages_String_emptyPath() {
        String[][] result = JSONStorage.readMessages("");
        assertEquals(0, result.length);
    }

    @Test
    public void testReadMessages_String_emptyArray() throws IOException {
        File file = tempFolder.newFile("empty.json");
        Files.write(file.toPath(), "[]".getBytes(StandardCharsets.UTF_8));

        assertEquals(0, JSONStorage.readMessages(file.getAbsolutePath()).length);
    }

    @Test
    public void testReadMessages_String_notAnArray() throws IOException {
        File file = tempFolder.newFile("object.json");
        Files.write(file.toPath(), "{\"messageID\":\"1\"}".getBytes(StandardCharsets.UTF_8));

        assertEquals(0, JSONStorage.readMessages(file.getAbsolutePath()).length);
    }

    @Test
    public void testReadMessages_String_malformedJson() throws IOException {
        File file = tempFolder.newFile("bad.json");
        Files.write(file.toPath(), "[{\"messageID\": ".getBytes(StandardCharsets.UTF_8));

        assertEquals(0, JSONStorage.readMessages(file.getAbsolutePath()).length);
    }

    // ---------- readMessages() ----------

    @Test
    public void testReadMessages() throws IOException {
        Files.deleteIfExists(defaultFile);
        assertEquals(0, JSONStorage.readMessages().length);

        assertTrue(JSONStorage.writeMessages(SAMPLE_ROWS));
        String[][] result = JSONStorage.readMessages();

        assertArrayEquals(SAMPLE_ROWS, result);
    }

    // ---------- writeMessages(String, String[][]) ----------

    @Test
    public void testWriteMessages_String_StringArrArr() throws IOException {
        File file = tempFolder.newFile("out.json");

        boolean written = JSONStorage.writeMessages(file.getAbsolutePath(), SAMPLE_ROWS);

        assertTrue(written);
        assertTrue(file.length() > 0);
        // Round trip: what was written must be read back identically
        assertArrayEquals(SAMPLE_ROWS, JSONStorage.readMessages(file.getAbsolutePath()));
    }

    @Test
    public void testWriteMessages_String_StringArrArr_overwritesExisting() throws IOException {
        File file = tempFolder.newFile("overwrite.json");
        JSONStorage.writeMessages(file.getAbsolutePath(), SAMPLE_ROWS);

        String[][] replacement = {SAMPLE_ROWS[3]};
        assertTrue(JSONStorage.writeMessages(file.getAbsolutePath(), replacement));

        String[][] result = JSONStorage.readMessages(file.getAbsolutePath());
        assertEquals(1, result.length);
        assertArrayEquals(replacement, result);
    }

    @Test
    public void testWriteMessages_String_StringArrArr_emptyRows() throws IOException {
        File file = tempFolder.newFile("none.json");

        assertTrue(JSONStorage.writeMessages(file.getAbsolutePath(), new String[0][]));
        assertEquals(0, JSONStorage.readMessages(file.getAbsolutePath()).length);
    }

    @Test
    public void testWriteMessages_String_StringArrArr_invalidPath() {
        // A directory that does not exist cannot be written to
        String badPath = tempFolder.getRoot().getAbsolutePath()
                + File.separator + "no_such_folder" + File.separator + "out.json";

        assertFalse(JSONStorage.writeMessages(badPath, SAMPLE_ROWS));
    }

    @Test
    public void testWriteMessages_specialCharactersSurviveRoundTrip() throws IOException {
        File file = tempFolder.newFile("special.json");
        String[][] rows = {
            {"0987654321", "09:0:HIBYE", "+27711112222",
                "Quote \"here\", backslash \\ and slash / and unicode é ü"}
        };

        assertTrue(JSONStorage.writeMessages(file.getAbsolutePath(), rows));
        assertArrayEquals(rows, JSONStorage.readMessages(file.getAbsolutePath()));
    }

    // ---------- writeMessages(String[][]) ----------

    @Test
    public void testWriteMessages_StringArrArr() throws IOException {
        boolean written = JSONStorage.writeMessages(SAMPLE_ROWS);

        assertTrue(written);
        assertTrue(Files.exists(defaultFile));
        assertArrayEquals(SAMPLE_ROWS,
                JSONStorage.readMessages(JSONStorage.DEFAULT_FILE));
    }

    // ---------- constants ----------

    @Test
    public void testColumnConstants() {
        assertEquals(0, JSONStorage.ID);
        assertEquals(1, JSONStorage.HASH);
        assertEquals(2, JSONStorage.RECIPIENT);
        assertEquals(3, JSONStorage.MESSAGE);
        assertEquals("message.json", JSONStorage.DEFAULT_FILE);
    }
}