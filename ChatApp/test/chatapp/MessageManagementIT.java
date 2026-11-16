package chatapp;

import java.io.File;
import java.io.IOException;
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
 * Tests for MessageManagement using the POE sample data.
 *
 * @author dtsotetsi
 */
public class MessageManagementIT {

    private static final String SENDER = "+27718693002";

    // ----- Test data: {ID, HASH, RECIPIENT, TEXT, STATUS} -----
    private static final String[][] DATA = {
        {"1000000001", "10:0:DIDCAKE", "+27834557896", "Did you get the cake?", MessageManagement.STATUS_SENT},
        {"1000000002", "10:1:WHEREAREYOU", "+27838884567",
            "Where are you? You are late! I have asked you to be on time.", MessageManagement.STATUS_STORED},
        {"1000000003", "10:2:YOHOGATE", "+27834484567", "Yohoooo, I am at your gate.", MessageManagement.STATUS_DISREGARD},
        {"1000000004", "10:3:ITISTIME", "+27838884567", "It is dinner time!", MessageManagement.STATUS_SENT},
        {"1000000005", "10:4:OKLEAVING", "+27838884567", "Ok, I am leaving without you.", MessageManagement.STATUS_STORED}
    };

    private static final String LONGEST = "Where are you? You are late! I have asked you to be on time.";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private MessageManagement instance;
    private Path defaultFile;
    private byte[] defaultFileBackup;

    @Before
    public void setUp() throws IOException {
        // Protect any real message.json in the working directory
        defaultFile = Paths.get(JSONStorage.DEFAULT_FILE);
        defaultFileBackup = Files.exists(defaultFile) ? Files.readAllBytes(defaultFile) : null;

        instance = new MessageManagement(SENDER);
        for (String[] d : DATA) {
            assertTrue(instance.addMessage(msg(d[0], d[1], d[2], d[3]), d[4]));
        }
    }

    @After
    public void tearDown() throws IOException {
        if (defaultFileBackup != null) {
            Files.write(defaultFile, defaultFileBackup);
        } else {
            Files.deleteIfExists(defaultFile);
        }
    }

    // ===================== addMessage =====================

    @Test
    public void testAddMessage() {
        MessageManagement m = new MessageManagement(SENDER);

        assertTrue(m.addMessage(msg("2000000001", "20:0:HELLO", "+27711112222", "Hello"),
                MessageManagement.STATUS_SENT));
        assertTrue(m.addMessage(msg("2000000002", "20:1:BYE", "+27711112222", "Bye"),
                MessageManagement.STATUS_STORED));
        assertTrue(m.addMessage(msg("2000000003", "20:2:OOPS", "+27711112222", "Oops"),
                MessageManagement.STATUS_DISREGARD));

        assertEquals(1, m.getSentMessages().length);
        assertEquals(1, m.getStoredMessages().length);
        assertEquals(1, m.getDisregardedMessages().length);
        assertEquals(3, m.getMessageIDs().length);
    }

    @Test
    public void testAddMessage_invalidStatusRejected() {
        MessageManagement m = new MessageManagement(SENDER);

        assertFalse(m.addMessage(msg("2000000009", "20:9:BAD", "+27711112222", "Bad"), "Unknown"));
        assertEquals(0, m.getMessageIDs().length);
    }

    @Test
    public void testAddMessage_capacityLimit() {
        MessageManagement m = new MessageManagement(SENDER);
        for (int i = 0; i < 200; i++) {
            assertTrue(m.addMessage(msg(String.format("%010d", i), "30:" + i + ":X", "+27711112222", "m" + i),
                    MessageManagement.STATUS_SENT));
        }
        assertFalse(m.addMessage(msg("9999999999", "30:200:X", "+27711112222", "overflow"),
                MessageManagement.STATUS_SENT));
        assertEquals(200, m.getSentMessages().length);
    }

    // ===================== load / save =====================

    @Test
    public void testLoadStoredMessages_String() {
        File file = new File(tempFolder.getRoot(), "load.json");
        String[][] rows = {
            {"3000000001", "30:0:HIMIKE", "+27718693002", "Hi Mike, can you join us for dinner tonight"},
            {"3000000002", "30:1:HIKEEGAN", "+27838884567", "Hi Keegan, did you receive the payment? Thanks"}
        };
        assertTrue(JSONStorage.writeMessages(file.getAbsolutePath(), rows));

        MessageManagement m = new MessageManagement(SENDER);
        assertEquals(2, m.loadStoredMessages(file.getAbsolutePath()));

        assertEquals(2, m.getStoredMessages().length);
        assertEquals("Hi Mike, can you join us for dinner tonight", m.getStoredMessages()[0]);
        assertEquals("30:1:HIKEEGAN", m.getMessageHashes()[1]);
    }

    @Test
    public void testLoadStoredMessages_String_skipsDuplicateIDs() {
        File file = new File(tempFolder.getRoot(), "dupes.json");
        String[][] rows = {
            {"1000000002", "10:1:WHEREAREYOU", "+27838884567", LONGEST},      // already in instance
            {"3000000003", "30:2:NEWONE", "+27711112222", "A brand new message"}
        };
        assertTrue(JSONStorage.writeMessages(file.getAbsolutePath(), rows));

        assertEquals(1, instance.loadStoredMessages(file.getAbsolutePath()));
        assertEquals(6, instance.getMessageIDs().length);

        // Loading the same file again adds nothing
        assertEquals(0, instance.loadStoredMessages(file.getAbsolutePath()));
    }

    @Test
    public void testLoadStoredMessages_String_missingFile() {
        assertEquals(0, instance.loadStoredMessages("does_not_exist_12345.json"));
        assertEquals(5, instance.getMessageIDs().length);
    }

    @Test
    public void testLoadStoredMessages_0args() {
        String[][] rows = {{"3000000004", "30:3:DEFAULT", "+27711112222", "From default file"}};
        assertTrue(JSONStorage.writeMessages(rows));

        MessageManagement m = new MessageManagement(SENDER);
        assertEquals(1, m.loadStoredMessages());
        assertEquals("From default file", m.getStoredMessages()[0]);
    }

    @Test
    public void testSaveStoredMessages_String() {
        File file = new File(tempFolder.getRoot(), "save.json");

        assertTrue(instance.saveStoredMessages(file.getAbsolutePath()));

        // Only the two stored messages are written
        String[][] saved = JSONStorage.readMessages(file.getAbsolutePath());
        assertEquals(2, saved.length);
        assertEquals("1000000002", saved[0][JSONStorage.ID]);
        assertEquals("10:1:WHEREAREYOU", saved[0][JSONStorage.HASH]);
        assertEquals("+27838884567", saved[0][JSONStorage.RECIPIENT]);
        assertEquals(LONGEST, saved[0][JSONStorage.MESSAGE]);
        assertEquals("1000000005", saved[1][JSONStorage.ID]);
    }

    @Test
    public void testSaveStoredMessages_String_invalidPath() {
        String badPath = tempFolder.getRoot().getAbsolutePath()
                + File.separator + "no_such_folder" + File.separator + "out.json";
        assertFalse(instance.saveStoredMessages(badPath));
    }

    @Test
    public void testSaveStoredMessages_0args() {
        assertTrue(instance.saveStoredMessages());

        String[][] saved = JSONStorage.readMessages();
        assertEquals(2, saved.length);
        assertEquals("Ok, I am leaving without you.", saved[1][JSONStorage.MESSAGE]);
    }

    // ===================== getters =====================

    @Test
    public void testGetSentMessages() {
        assertArrayEquals(new String[]{"Did you get the cake?", "It is dinner time!"},
                instance.getSentMessages());
    }

    @Test
    public void testGetDisregardedMessages() {
        assertArrayEquals(new String[]{"Yohoooo, I am at your gate."},
                instance.getDisregardedMessages());
    }

    @Test
    public void testGetStoredMessages() {
        assertArrayEquals(new String[]{LONGEST, "Ok, I am leaving without you."},
                instance.getStoredMessages());
    }

    @Test
    public void testGetMessageHashes() {
        assertArrayEquals(new String[]{"10:0:DIDCAKE", "10:1:WHEREAREYOU", "10:2:YOHOGATE",
            "10:3:ITISTIME", "10:4:OKLEAVING"}, instance.getMessageHashes());
    }

    @Test
    public void testGetMessageIDs() {
        assertArrayEquals(new String[]{"1000000001", "1000000002", "1000000003",
            "1000000004", "1000000005"}, instance.getMessageIDs());
    }

    @Test
    public void testGetters_emptyInstance() {
        MessageManagement m = new MessageManagement(SENDER);
        assertEquals(0, m.getSentMessages().length);
        assertEquals(0, m.getDisregardedMessages().length);
        assertEquals(0, m.getStoredMessages().length);
        assertEquals(0, m.getMessageHashes().length);
        assertEquals(0, m.getMessageIDs().length);
    }

    // ===================== 2a. sender and recipient =====================

    @Test
    public void testDisplaySenderAndRecipient() {
        String expected = "Sender: " + SENDER + " | Recipient: +27838884567\n"
                + "Sender: " + SENDER + " | Recipient: +27838884567\n";
        assertEquals(expected, instance.displaySenderAndRecipient());
    }

    @Test
    public void testDisplaySenderAndRecipient_noStoredMessages() {
        assertEquals("No stored messages.",
                new MessageManagement(SENDER).displaySenderAndRecipient());
    }

    // ===================== 2b. longest stored message =====================

    @Test
    public void testGetLongestStoredMessage() {
        assertEquals(LONGEST, instance.getLongestStoredMessage());
    }

    @Test
    public void testGetLongestStoredMessage_noStoredMessages() {
        assertEquals("", new MessageManagement(SENDER).getLongestStoredMessage());
    }

    // ===================== 2c. search by ID =====================

    @Test
    public void testGetMessageByID() {
        assertEquals("It is dinner time!", instance.getMessageByID("1000000004"));
    }

    @Test
    public void testGetMessageByID_notFound() {
        assertNull(instance.getMessageByID("0000000000"));
    }

    @Test
    public void testDisplayByMessageID() {
        assertEquals("Recipient: +27834557896 | Message: Did you get the cake?",
                instance.displayByMessageID("1000000001"));
    }

    @Test
    public void testDisplayByMessageID_notFound() {
        assertEquals("No message found with ID 0000000000",
                instance.displayByMessageID("0000000000"));
    }

    // ===================== 2d. messages for a recipient =====================

    @Test
    public void testGetMessagesForRecipient() {
        // Sent + stored only, in the order they were added
        assertArrayEquals(new String[]{LONGEST, "It is dinner time!", "Ok, I am leaving without you."},
                instance.getMessagesForRecipient("+27838884567"));
    }

    @Test
    public void testGetMessagesForRecipient_disregardedExcluded() {
        // +27834484567 only has a disregarded message
        assertEquals(0, instance.getMessagesForRecipient("+27834484567").length);
    }

    @Test
    public void testGetMessagesForRecipient_unknownRecipient() {
        assertEquals(0, instance.getMessagesForRecipient("+27000000000").length);
    }

    @Test
    public void testGetStoredMessagesForRecipient() {
        assertArrayEquals(new String[]{LONGEST, "Ok, I am leaving without you."},
                instance.getStoredMessagesForRecipient("+27838884567"));
    }

    @Test
    public void testGetStoredMessagesForRecipient_sentOnlyRecipient() {
        // +27834557896 only has a sent message
        assertEquals(0, instance.getStoredMessagesForRecipient("+27834557896").length);
    }

    // ===================== 2e. delete by hash =====================

    @Test
    public void testDeleteByHash_stored() {
        String result = instance.deleteByHash("10:1:WHEREAREYOU");

        assertEquals("Message: \u201C" + LONGEST + "\u201D successfully deleted.", result);
        assertArrayEquals(new String[]{"Ok, I am leaving without you."}, instance.getStoredMessages());
        assertEquals(4, instance.getMessageIDs().length);
        assertNull(instance.getMessageByID("1000000002"));
        // Other categories untouched
        assertEquals(2, instance.getSentMessages().length);
        assertEquals(1, instance.getDisregardedMessages().length);
    }

    @Test
    public void testDeleteByHash_sent() {
        // Second sent message: checks the position inside the sent array is worked out correctly
        String result = instance.deleteByHash("10:3:ITISTIME");

        assertEquals("Message: \u201CIt is dinner time!\u201D successfully deleted.", result);
        assertArrayEquals(new String[]{"Did you get the cake?"}, instance.getSentMessages());
        assertEquals(2, instance.getStoredMessages().length);
    }

    @Test
    public void testDeleteByHash_disregarded() {
        String result = instance.deleteByHash("10:2:YOHOGATE");

        assertEquals("Message: \u201CYohoooo, I am at your gate.\u201D successfully deleted.", result);
        assertEquals(0, instance.getDisregardedMessages().length);
        assertEquals(4, instance.getMessageHashes().length);
    }

    @Test
    public void testDeleteByHash_notFound() {
        assertEquals("No message found with hash 99:9:NOPE", instance.deleteByHash("99:9:NOPE"));
        assertEquals(5, instance.getMessageIDs().length);
    }

    @Test
    public void testDeleteByHash_thenSaveRemovesFromFile() {
        File file = new File(tempFolder.getRoot(), "afterdelete.json");
        instance.deleteByHash("10:1:WHEREAREYOU");

        assertTrue(instance.saveStoredMessages(file.getAbsolutePath()));

        String[][] saved = JSONStorage.readMessages(file.getAbsolutePath());
        assertEquals(1, saved.length);
        assertEquals("10:4:OKLEAVING", saved[0][JSONStorage.HASH]);
    }

    // ===================== 2f. reports =====================

    @Test
    public void testDisplaySentReport() {
        String expected = "--- SENT MESSAGES REPORT ---\n"
                + "Message Hash: 10:0:DIDCAKE | Recipient: +27834557896 | Message: Did you get the cake?\n"
                + "Message Hash: 10:3:ITISTIME | Recipient: +27838884567 | Message: It is dinner time!\n";
        assertEquals(expected, instance.displaySentReport());
    }

    @Test
    public void testDisplaySentReport_empty() {
        assertEquals("--- SENT MESSAGES REPORT ---\n",
                new MessageManagement(SENDER).displaySentReport());
    }

    @Test
    public void testDisplayStoredReport() {
        String expected = "--- STORED MESSAGES REPORT ---\n"
                + "Message ID: 1000000002 | Message Hash: 10:1:WHEREAREYOU | Sender: " + SENDER
                + " | Recipient: +27838884567 | Message: " + LONGEST + "\n"
                + "Message ID: 1000000005 | Message Hash: 10:4:OKLEAVING | Sender: " + SENDER
                + " | Recipient: +27838884567 | Message: Ok, I am leaving without you.\n";
        assertEquals(expected, instance.displayStoredReport());
    }

    // ===================== helper =====================

    /**
     * Builds a Message for the test data. Message generates a random ID and a
     * hash from it, so those two getters are overridden to return the fixed
     * values from DATA and keep the assertions predictable.
     */
    private static Message msg(final String id, final String hash,
                               final String recipient, final String text) {
        return new Message(0, recipient, text) {
            @Override
            public String getMessageID() {
                return id;
            }

            @Override
            public String getMessageHash() {
                return hash;
            }
        };
    }
}