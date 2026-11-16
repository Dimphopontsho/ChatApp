
package chatapp;
 
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
/**
 * Reads and writes the stored messages JSON file (the same file Message.storeMessage() uses).
 *
 * Every message is a row of four Strings: message ID, message hash, recipient and message text.
 * The column positions are the constants ID, HASH, RECIPIENT and MESSAGE.
 */
public class JSONStorage {
 
    /**
     * Name of the JSON file used when no path is given.
     */
    public static final String DEFAULT_FILE = "message.json";
 
    /**
     * Column positions inside each row (see readMessages and writeMessages).
     */
    public static final int ID = 0;
    public static final int HASH = 1;
    public static final int RECIPIENT = 2;
    public static final int MESSAGE = 3;
 
    /**
     * Reads the JSON file into an array of rows.
     *
     * @param path the file to read
     * @return one row per message: {ID, HASH, RECIPIENT, MESSAGE}; an empty array if the file
     *         does not exist or cannot be read
     */
    public static String[][] readMessages(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return new String[0][];
        }
 
        try (FileReader reader = new FileReader(file)) {
            Object obj = new JSONParser().parse(reader);
            if (!(obj instanceof JSONArray)) {
                return new String[0][];
            }
 
            JSONArray jsonArray = (JSONArray) obj;
            String[][] rows = new String[jsonArray.size()][4];
 
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject o = (JSONObject) jsonArray.get(i);
                rows[i][ID] = String.valueOf(o.get("messageID"));
                rows[i][HASH] = String.valueOf(o.get("messageHash"));
                rows[i][RECIPIENT] = String.valueOf(o.get("recipient"));
                rows[i][MESSAGE] = String.valueOf(o.get("message"));
            }
            return rows;
 
        } catch (IOException | ParseException e) {
            System.out.println("Could not read JSON file: " + e.getMessage());
            return new String[0][];
        }
    }
 
    /**
     * Reads the default JSON file (message.json).
     *
     * @return the rows read, or an empty array if there is no file
     */
    public static String[][] readMessages() {
        return readMessages(DEFAULT_FILE);
    }
 
    /**
     * Overwrites the JSON file with the given rows.
     *
     * @param path the file to write
     * @param rows one row per message: {ID, HASH, RECIPIENT, MESSAGE}
     * @return true if the file was written, false if an I/O error occurred
     */
    @SuppressWarnings("unchecked")
    public static boolean writeMessages(String path, String[][] rows) {
        JSONArray jsonArray = new JSONArray();
 
        for (String[] row : rows) {
            JSONObject o = new JSONObject();
            o.put("messageID", row[ID]);
            o.put("messageHash", row[HASH]);
            o.put("recipient", row[RECIPIENT]);
            o.put("message", row[MESSAGE]);
            jsonArray.add(o);
        }
 
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(jsonArray.toJSONString());
            writer.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error writing JSON file: " + e.getMessage());
            return false;
        }
    }
 
    /**
     * Overwrites the default JSON file (message.json) with the given rows.
     *
     * @param rows one row per message: {ID, HASH, RECIPIENT, MESSAGE}
     * @return true if the file was written, false if an I/O error occurred
     */
    public static boolean writeMessages(String[][] rows) {
        return writeMessages(DEFAULT_FILE, rows);
    }
}
 

