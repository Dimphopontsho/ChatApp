package chatapp;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author dtsotetsi
 */
public class Message {

    // ===================== Constants =====================
    // Shared with ChatApp so the text always matches
    public static final String RECIPIENT_VALID = "Cellphone number captured successfully";
    public static final String RECIPIENT_INVALID =
            "Cellphone number is incorrectly formatted or does not contain the international code. "
            + "Please enter the correct details";
    public static final String LENGTH_VALID = "Message is ready to send";

    private static final int MAX_LENGTH = 250;
    private static final String FILE_PATH = "message.json";

    // ===================== Static state =====================
    private static final ArrayList<Message> sentMessages = new ArrayList<Message>();
    private static int totalMessagesSent = 0;

    // ===================== Instance variables =====================
    private String messageID;
    private int messageNum;
    private String recipientCell;
    private String message;
    private String messageHash;

    // ===================== Constructor =====================
    public Message(int messageNum, String recipientCell, String message) {
        this.messageNum = messageNum;
        this.recipientCell = recipientCell;
        this.message = message;
        this.messageID = generateMessageID();    // ID first...
        this.messageHash = createMessageHash();  // ...because the hash uses the ID
    }

    // ===================== Validation =====================

    // Valid if the ID has 10 characters or fewer
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    // Recipient must be +27 followed by 9 digits
    public static boolean isValidRecipient(String cell) {
        return cell != null && cell.matches("^\\+27[0-9]{9}$");
    }

    // Message must be 250 characters or fewer
    public static boolean isValidLength(String text) {
        return text != null && text.length() <= MAX_LENGTH;
    }

    public static String lengthMessage(String text) {
        if (isValidLength(text)) {
            return LENGTH_VALID;
        }
        int extra = text.length() - MAX_LENGTH;
        return "Message exceeds 250 characters by " + extra + "; please reduce the size";
    }

    public String checkRecipientCell() {
        return isValidRecipient(recipientCell) ? RECIPIENT_VALID : RECIPIENT_INVALID;
    }

    public String checkMessageLength() {
        return lengthMessage(message);
    }

    // ===================== Hash =====================

    // Format: first two digits of ID : message number : FIRSTWORD+LASTWORD
    // e.g. 00:0:HITONIGHT
    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);

        String trimmed = message.trim();
        String firstWord = "";
        String lastWord = "";

        if (!trimmed.isEmpty()) {
            String[] words = trimmed.split("\\s+");
            // Remove punctuation such as "?" or ","
            firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
            lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        }

        return (firstTwo + ":" + messageNum + ":" + firstWord + lastWord).toUpperCase();
    }

    // ===================== Actions =====================

    // 1 = send, 2 = disregard, 3 = store
    public String sentMessage(int menuChoice) {
        String result;

        switch (menuChoice) {
            case 1:
                sentMessages.add(this);
                totalMessagesSent++;
                result = "Message sent successfully";
                break;
            case 2:
                result = "Press 0 to delete the message";
                break;
            case 3:
                result = storeMessage()
                        ? "Message successfully stored"
                        : "Message could not be stored";
                break;
            default:
                result = "Invalid choice";
                break;
        }
        return result;
    }

    // Formatted string of all sent messages
    public static String printMessage() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }

        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message ID: ").append(m.messageID).append("\n");
            sb.append("Message Hash: ").append(m.messageHash).append("\n");
            sb.append("Recipient: ").append(m.recipientCell).append("\n");
            sb.append("Message: ").append(m.message).append("\n");
            sb.append("--------------------------\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Appends this message to message.json
    @SuppressWarnings("unchecked")
    public boolean storeMessage() {
        JSONArray messageArray = new JSONArray();

        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Object obj = new JSONParser().parse(reader);
                if (obj instanceof JSONArray) {
                    messageArray = (JSONArray) obj;
                }
            } catch (IOException | ParseException e) {
                System.out.println("Could not read existing file, starting fresh");
            }
        }

        JSONObject messageObject = new JSONObject();
        messageObject.put("messageID", messageID);
        messageObject.put("messageHash", messageHash);
        messageObject.put("recipient", recipientCell);
        messageObject.put("message", message);
        messageArray.add(messageObject);

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(messageArray.toJSONString());
            writer.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error writing JSON file " + e.getMessage());
            return false;
        }
    }

    // Generates a random 10-digit ID
    private String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int count = 0; count < 10; count++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    // ===================== Getters / setters =====================
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public String getRecipient() {
        return recipientCell;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageHash() {
        return messageHash;
    }

    // Update the ID first, then rebuild the hash
    public void setMessageID(String messageID) {
        this.messageID = messageID;
        this.messageHash = createMessageHash();
    }

    public String getFullDetails() {
        return "MessageID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipientCell
                + "\nMessage: " + message + "\n";
    }
}