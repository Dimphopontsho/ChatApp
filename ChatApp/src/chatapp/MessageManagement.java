/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatapp;

/**
 *
 * @author dtsotetsi
 */
public class MessageManagement {
    public static final String STATUS_SENT = "Sent";
    public static final String STATUS_STORED = "Stored";
    public static final String STATUS_DISREGARD = "Disregard";
    
    private static final int MAXIMUM_MESSAGES = 200;
    
    // Parallel arrays: index i in every array describes the same message
    private String[] messageIDs = new String[MAXIMUM_MESSAGES];
    private String[] messagesHashes = new String[MAXIMUM_MESSAGES];
    private String[] recipientCellPhoneNumber = new String[MAXIMUM_MESSAGES];
    private String[] messageTexts = new String[MAXIMUM_MESSAGES];
    private String[] messageStatuses = new String[MAXIMUM_MESSAGES];
    
    private int count = 0;
    private final String sender;
 
    public MessageManagement(String sender) {
        this.sender = sender;
    }
    
    //Populate the arrays 
    public boolean addMessage(Message m, String status) {
        return addEntry(m.getMessageID(), m.getMessageHash(),
                m.getRecipient(), m.getMessage(), status);
    }
    
    private boolean addEntry(String id, String hash, String recipient,
                             String text, String status) {
        if (count >= MAXIMUM_MESSAGES) {
            return false;
        }
        messageIDs[count] = id;
        messagesHashes[count] = hash;
        recipientCellPhoneNumber[count] = recipient;
        messageTexts[count] = text;
        messageStatuses[count] = status;
        count++;
        return true;
    }
    
    // Reads the JSON file into the stored messages. Returns how many were added.
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
        String[] texts = getStoredMessages();
        String[][] rows = new String[texts.length][4];
        int r = 0;
        for (int i = 0; i < count; i++) {
            if (STATUS_STORED.equals(messageStatuses[i])) {
                rows[r][JSONStorage.ID] = messageIDs[i];
                rows[r][JSONStorage.HASH] = messagesHashes[i];
                rows[r][JSONStorage.RECIPIENT] = recipientCellPhoneNumber[i];
                rows[r][JSONStorage.MESSAGE] = messageTexts[i];
                r++;
            }
        }
        return JSONStorage.writeMessages(path, rows);
    }
    
    
    
    
    
}
