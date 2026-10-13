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

    // Instance variables
    private String messageID;
    private int messageNum;
    private String recipientcell;
    private String message;
    private String messageHash;

    // Static variables shared by all messages
    private static ArrayList<Message> sentMessages = new ArrayList<Message>();
    private static int totalMessagesSent = 0;

    // Constructor
    public Message(int messageNum, String recipientcell, String message) {
        this.messageNum = messageNum;
        this.recipientcell = recipientcell;
        this.message = message;
        this.messageID = generateMessageID();      // ID first...
        this.messageHash = createMessageHash();    // ...because the hash uses the ID
    }

    // Valid if the ID has 10 characters or fewer
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    // Recipient must be in the format +27 followed by 9 digits
    public String checkRecipientCell() {
        String valid = "Cellphone number is incorrectly formatted or does not contain the international code. "
                + "Please enter the correct details";
        if (recipientcell.matches("^\\+27[0-9]{9}$")) {
            valid = "Cellphone number captured successfully";
        }
        return valid;
    }

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

        String hash = firstTwo + ":" + messageNum + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    // 1 = send, 2 = disregard, 3 = store
    public String sentMessage(int menuChoice) {
        String validMenuOpt;

        switch (menuChoice) {
            case 1:
                sentMessages.add(this);
                totalMessagesSent++;
                validMenuOpt = "Message sent successfully";
                break;
            case 2:
                validMenuOpt = "Press 0 to delete the message";
                break;
            case 3:
                validMenuOpt = "Message successfully stored";
                break;
            default:
                validMenuOpt = "Invalid choice";
                break;
        }
        return validMenuOpt;
    }

    // Returns a formatted string of all sent messages
    public String printMessage() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }

        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message ID: ").append(m.messageID).append("\n");
            sb.append("Message Hash: ").append(m.messageHash).append("\n");
            sb.append("Recipient: ").append(m.recipientcell).append("\n");
            sb.append("Message: ").append(m.message).append("\n");
            sb.append("--------------------------\n");
        }
        return sb.toString();
    }

    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Message length must be 250 characters or fewer
    public String checkMessageLength() {
        String lengthValid;

        if (message.length() <= 250) {
            lengthValid = "Message is ready to send";
        } else {
            int extra = message.length() - 250;
            lengthValid = "Message exceeds 250 characters by " + extra + "; please reduce the size";
        }
        return lengthValid;
    }

    // Appends this message to message.json
    @SuppressWarnings("unchecked")
    public boolean storeMessage() {
        boolean stored;
        String filePath = "message.json";
        JSONArray messageArray = new JSONArray();

        File file = new File(filePath);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(reader);
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
        messageObject.put("recipient", recipientcell);
        messageObject.put("message", message);
        messageArray.add(messageObject);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(messageArray.toJSONString());
            writer.flush();
            stored = true;
        } catch (IOException e) {
            System.out.println("Error writing JSON file " + e.getMessage());
            stored = false;
        }
        return stored;
    }

    // Generates a random 10-digit ID
    private String generateMessageID() {
        Random randomObj = new Random();
        StringBuilder id = new StringBuilder();

        for (int count = 0; count < 10; count++) {
            id.append(randomObj.nextInt(10));
        }
        return id.toString();
    }

    // Getters
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public String getRecipient() {
        return recipientcell;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageHash() {
        return messageHash;
    }

    // Setter: update the ID first, then rebuild the hash
    public void setMessageID(String messageID) {
        this.messageID = messageID;
        this.messageHash = createMessageHash();
    }

    public String getFullDetails() {
        return "MessageID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipientcell
                + "\nMessage: " + message + "\n";
    }
}