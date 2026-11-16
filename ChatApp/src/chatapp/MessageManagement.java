

package chatapp;
 
import java.util.Arrays;
 
/**
 * Part 3: holds the five arrays required by the POE (sent, disregarded, stored,
 * message hash, message ID) and implements the "Stored Messages" features.
 *
 * @author dtsotetsi
 */
public class MessageManagement {
 
    public static final String STATUS_SENT = "Sent";
    public static final String STATUS_STORED = "Stored";
    public static final String STATUS_DISREGARD = "Disregard";
 
    private static final int MAXIMUM_MESSAGES = 200;
 
    // ===== The five required arrays =====
    private String[] sentMessages = new String[MAXIMUM_MESSAGES];         // all messages sent
    private String[] disregardedMessages = new String[MAXIMUM_MESSAGES];  // all messages disregarded
    private String[] storedMessages = new String[MAXIMUM_MESSAGES];       // all stored messages
    private String[] messageHashes = new String[MAXIMUM_MESSAGES];        // all message hashes
    private String[] messageIDs = new String[MAXIMUM_MESSAGES];           // all message IDs
 
    private int sentCount = 0;
    private int disregardedCount = 0;
    private int storedCount = 0;
 
    // ===== Supporting arrays: same index as messageIDs / messageHashes =====
    private String[] recipients = new String[MAXIMUM_MESSAGES];
    private String[] texts = new String[MAXIMUM_MESSAGES];
    private String[] statuses = new String[MAXIMUM_MESSAGES];
    private int count = 0;   // number of messages overall
 
    private final String sender;
 
    public MessageManagement(String sender) {
        this.sender = sender;
    }
 
    // ===================== Populate the arrays =====================
 
    public boolean addMessage(Message m, String status) {
        return addEntry(m.getMessageID(), m.getMessageHash(),
                m.getRecipient(), m.getMessage(), status);
    }
 
    private boolean addEntry(String id, String hash, String recipient,
                             String text, String status) {
        if (count >= MAXIMUM_MESSAGES) {
            return false;
        }
 
        // Category array
        if (STATUS_SENT.equals(status)) {
            sentMessages[sentCount++] = text;
        } else if (STATUS_DISREGARD.equals(status)) {
            disregardedMessages[disregardedCount++] = text;
        } else if (STATUS_STORED.equals(status)) {
            storedMessages[storedCount++] = text;
        } else {
            return false;
        }
 
        // Hash and ID arrays (all messages) plus supporting arrays
        messageIDs[count] = id;
        messageHashes[count] = hash;
        recipients[count] = recipient;
        texts[count] = text;
        statuses[count] = status;
        count++;
        return true;
    }
 
    // Reads the JSON file into the stored messages array. Returns how many were added.
    public int loadStoredMessages(String path) {
        String[][] rows = JSONStorage.readMessages(path);
        int added = 0;
        for (String[] row : rows) {
            if (indexOfID(row[JSONStorage.ID]) == -1
                    && addEntry(row[JSONStorage.ID], row[JSONStorage.HASH],
                            row[JSONStorage.RECIPIENT], row[JSONStorage.MESSAGE],
                            STATUS_STORED)) {
                added++;
            }
        }
        return added;
    }
 
    public int loadStoredMessages() {
        return loadStoredMessages(JSONStorage.DEFAULT_FILE);
    }
 
    // Writes the stored messages back to the JSON file (used after a delete)
    public boolean saveStoredMessages(String path) {
        String[][] rows = new String[storedCount][4];
        int r = 0;
        for (int i = 0; i < count; i++) {
            if (STATUS_STORED.equals(statuses[i])) {
                rows[r][JSONStorage.ID] = messageIDs[i];
                rows[r][JSONStorage.HASH] = messageHashes[i];
                rows[r][JSONStorage.RECIPIENT] = recipients[i];
                rows[r][JSONStorage.MESSAGE] = texts[i];
                r++;
            }
        }
        return JSONStorage.writeMessages(path, rows);
    }
 
    public boolean saveStoredMessages() {
        return saveStoredMessages(JSONStorage.DEFAULT_FILE);
    }
 
    // ===================== Access the arrays =====================
 
    public String[] getSentMessages() {
        return Arrays.copyOf(sentMessages, sentCount);
    }
 
    public String[] getDisregardedMessages() {
        return Arrays.copyOf(disregardedMessages, disregardedCount);
    }
 
    public String[] getStoredMessages() {
        return Arrays.copyOf(storedMessages, storedCount);
    }
 
    public String[] getMessageHashes() {
        return Arrays.copyOf(messageHashes, count);
    }
 
    public String[] getMessageIDs() {
        return Arrays.copyOf(messageIDs, count);
    }
 
    // ===================== 2a. Sender and recipient of stored messages =====================
 
    public String displaySenderAndRecipient() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (STATUS_STORED.equals(statuses[i])) {
                sb.append("Sender: ").append(sender)
                  .append(" | Recipient: ").append(recipients[i]).append("\n");
            }
        }
        return sb.length() == 0 ? "No stored messages." : sb.toString();
    }
 
    // ===================== 2b. Longest stored message =====================
 
    public String getLongestStoredMessage() {
        String longest = "";
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].length() > longest.length()) {
                longest = storedMessages[i];
            }
        }
        return longest;
    }
 
    // ===================== 2c. Search by message ID =====================
 
    // Returns the message text, or null if the ID is not found
    public String getMessageByID(String id) {
        int i = indexOfID(id);
        return i == -1 ? null : texts[i];
    }
 
    public String displayByMessageID(String id) {
        int i = indexOfID(id);
        if (i == -1) {
            return "No message found with ID " + id;
        }
        return "Recipient: " + recipients[i] + " | Message: " + texts[i];
    }
 
    // ===================== 2d. Messages for a particular recipient =====================
 
    // Sent and stored messages for the recipient
    public String[] getMessagesForRecipient(String recipient) {
        String[] result = new String[count];
        int n = 0;
        for (int i = 0; i < count; i++) {
            if (recipient.equals(recipients[i])
                    && (STATUS_SENT.equals(statuses[i]) || STATUS_STORED.equals(statuses[i]))) {
                result[n++] = texts[i];
            }
        }
        return Arrays.copyOf(result, n);
    }
 
    // Stored messages only (for the menu option in the POE brief)
    public String[] getStoredMessagesForRecipient(String recipient) {
        String[] result = new String[storedCount];
        int n = 0;
        for (int i = 0; i < count; i++) {
            if (recipient.equals(recipients[i]) && STATUS_STORED.equals(statuses[i])) {
                result[n++] = texts[i];
            }
        }
        return Arrays.copyOf(result, n);
    }
 
    // ===================== 2e. Delete using the message hash =====================
 
    public String deleteByHash(String hash) {
        int index = -1;
        for (int i = 0; i < count; i++) {
            if (messageHashes[i].equals(hash)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return "No message found with hash " + hash;
        }
 
        String text = texts[index];
        String status = statuses[index];
 
        // Position inside its category array = how many earlier messages share the status
        int pos = 0;
        for (int i = 0; i < index; i++) {
            if (status.equals(statuses[i])) {
                pos++;
            }
        }
 
        if (STATUS_SENT.equals(status)) {
            removeAt(sentMessages, pos, sentCount);
            sentCount--;
        } else if (STATUS_DISREGARD.equals(status)) {
            removeAt(disregardedMessages, pos, disregardedCount);
            disregardedCount--;
        } else {
            removeAt(storedMessages, pos, storedCount);
            storedCount--;
        }
 
        // Remove from the main arrays
        removeAt(messageIDs, index, count);
        removeAt(messageHashes, index, count);
        removeAt(recipients, index, count);
        removeAt(texts, index, count);
        removeAt(statuses, index, count);
        count--;
 
        return "Message: \u201C" + text + "\u201D successfully deleted.";
    }
 
    // Shifts everything after position pos one place left
    private void removeAt(String[] array, int pos, int size) {
        for (int i = pos; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        array[size - 1] = null;
    }
 
    // ===================== 2f. Reports =====================
 
    // All sent messages: Message Hash, Recipient, Message
    public String displaySentReport() {
        StringBuilder sb = new StringBuilder("--- SENT MESSAGES REPORT ---\n");
        for (int i = 0; i < count; i++) {
            if (STATUS_SENT.equals(statuses[i])) {
                sb.append("Message Hash: ").append(messageHashes[i])
                  .append(" | Recipient: ").append(recipients[i])
                  .append(" | Message: ").append(texts[i]).append("\n");
            }
        }
        return sb.toString();
    }
 
    // Full details of all stored messages
    public String displayStoredReport() {
        StringBuilder sb = new StringBuilder("--- STORED MESSAGES REPORT ---\n");
        for (int i = 0; i < count; i++) {
            if (STATUS_STORED.equals(statuses[i])) {
                sb.append("Message ID: ").append(messageIDs[i])
                  .append(" | Message Hash: ").append(messageHashes[i])
                  .append(" | Sender: ").append(sender)
                  .append(" | Recipient: ").append(recipients[i])
                  .append(" | Message: ").append(texts[i]).append("\n");
            }
        }
        return sb.toString();
    }
 
    // ===================== Helper =====================
 
    private int indexOfID(String id) {
        for (int i = 0; i < count; i++) {
            if (messageIDs[i].equals(id)) {
                return i;
            }
        }
        return -1;
    }
}
 
