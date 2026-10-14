package chatapp;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageIT {

    private Message message;

    @Before
    public void setUp() {
        message = new Message(
                1,
                "+27831234567",
                "Hi Mike, can you join us for dinner tonight"
        );
    }

    @After
    public void tearDown() {
        File storedFile = new File("message.json");
        if (storedFile.exists()) {
            storedFile.delete();
        }
    }

    @Test
    public void testCheckMessageID() {
        assertTrue(message.checkMessageID());
    }

    @Test
    public void testIsValidRecipient_Valid() {
        assertTrue(Message.isValidRecipient("+27831234567"));
    }

    @Test
    public void testIsValidRecipient_Empty() {
        assertFalse(Message.isValidRecipient(""));
    }

    @Test
    public void testIsValidRecipient_Null() {
        assertFalse(Message.isValidRecipient(null));
    }

    @Test
    public void testIsValidRecipient_NoInternationalCode() {
        assertFalse(Message.isValidRecipient("0831234567"));
    }

    @Test
    public void testIsValidRecipient_WrongLength() {
        assertFalse(Message.isValidRecipient("+2783123456"));
    }

    @Test
    public void testIsValidLength_Valid() {
        assertTrue(Message.isValidLength("Hi Mike, can you join us for dinner tonight"));
    }

    @Test
    public void testIsValidLength_Empty() {
        assertTrue(Message.isValidLength(""));
    }

    @Test
    public void testIsValidLength_Null() {
        assertFalse(Message.isValidLength(null));
    }

    @Test
    public void testIsValidLength_TooLong() {
        String longText = "a".repeat(251);
        assertFalse(Message.isValidLength(longText));
    }

    @Test
    public void testLengthMessage_Valid() {
        assertEquals(
                Message.LENGTH_VALID,
                Message.lengthMessage("Hi Mike, can you join us for dinner tonight")
        );
    }

    @Test
    public void testLengthMessage_TooLong() {
        String longText = "a".repeat(251);

        String result = Message.lengthMessage(longText);

        assertEquals(
                "Message exceeds 250 characters by 1; please reduce the size",
                result
        );
    }

    @Test
    public void testCheckRecipientCell_Valid() {
        assertEquals(
                Message.RECIPIENT_VALID,
                message.checkRecipientCell()
        );
    }

    @Test
    public void testCheckRecipientCell_Invalid() {
        Message invalid = new Message(
                2,
                "0831234567",
                "Invalid recipient test"
        );

        assertEquals(
                Message.RECIPIENT_INVALID,
                invalid.checkRecipientCell()
        );
    }

    @Test
    public void testCheckMessageLength_Valid() {
        assertEquals(
                Message.LENGTH_VALID,
                message.checkMessageLength()
        );
    }

    @Test
    public void testCheckMessageLength_TooLong() {
        Message longMessage = new Message(
                3,
                "+27831234567",
                "a".repeat(251)
        );

        assertEquals(
                "Message exceeds 250 characters by 1; please reduce the size",
                longMessage.checkMessageLength()
        );
    }

    @Test
    public void testCreateMessageHash() {
        message.setMessageID("0012345678");

        assertEquals(
                "00:1:HIKENIGHT",
                message.createMessageHash()
        );
    }

    @Test
    public void testCreateMessageHash_SingleWord() {
        Message singleWord = new Message(
                4,
                "+27831234567",
                "Hello"
        );
        singleWord.setMessageID("0098765432");

        assertEquals(
                "00:4:HELLOHELLO",
                singleWord.createMessageHash()
        );
    }

    @Test
    public void testSentMessage_Send() {
        int before = Message.returnTotalMessages();

        String result = message.sentMessage(1);

        assertEquals("Message sent successfully", result);
        assertEquals(before + 1, Message.returnTotalMessages());
    }

    @Test
    public void testSentMessage_Disregard() {
        assertEquals(
                "Press 0 to delete the message",
                message.sentMessage(2)
        );
    }

    @Test
    public void testSentMessage_Store() {
        String result = message.sentMessage(3);

        assertEquals("Message successfully stored", result);
    }

    @Test
    public void testSentMessage_InvalidChoice() {
        assertEquals(
                "Invalid choice",
                message.sentMessage(99)
        );
    }

    @Test
    public void testPrintMessage_NoMessages() {
        assertEquals(
                "No messages have been sent.",
                Message.printMessage()
        );
    }

    @Test
    public void testPrintMessage_AfterSend() {
        message.sentMessage(1);

        String result = Message.printMessage();

        assertTrue(result.contains("Message ID: " + message.getMessageID()));
        assertTrue(result.contains("+27831234567"));
        assertTrue(result.contains("Hi Mike, can you join us for dinner tonight"));
    }

    @Test
    public void testReturnTotalMessages() {
        int before = Message.returnTotalMessages();

        message.sentMessage(1);

        assertEquals(before + 1, Message.returnTotalMessages());
    }

    @Test
    public void testStoreMessage() {
        assertTrue(message.storeMessage());

        File file = new File("message.json");
        assertTrue(file.exists());
    }

    @Test
    public void testGetMessageID() {
        assertNotNull(message.getMessageID());
        assertEquals(10, message.getMessageID().length());
    }

    @Test
    public void testGetMessageNum() {
        assertEquals(1, message.getMessageNum());
    }

    @Test
    public void testGetRecipient() {
        assertEquals("+27831234567", message.getRecipient());
    }

    @Test
    public void testGetMessage() {
        assertEquals(
                "Hi Mike, can you join us for dinner tonight",
                message.getMessage()
        );
    }

    @Test
    public void testGetMessageHash() {
        assertNotNull(message.getMessageHash());
        assertFalse(message.getMessageHash().isEmpty());
    }

    @Test
    public void testSetMessageID() {
        message.setMessageID("0012345678");

        assertEquals("0012345678", message.getMessageID());
        assertEquals(
                "00:1:HIKENIGHT",
                message.getMessageHash()
        );
    }

    @Test
    public void testGetFullDetails() {
        String details = message.getFullDetails();

        assertTrue(details.contains("MessageID: " + message.getMessageID()));
        assertTrue(details.contains("Message Hash: " + message.getMessageHash()));
        assertTrue(details.contains("Recipient: +27831234567"));
        assertTrue(details.contains(
                "Message: Hi Mike, can you join us for dinner tonight"
        ));
    }
}