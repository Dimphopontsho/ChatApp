/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 *
 * @author dtsotetsi
 */


       
public class Message {
    //Variables
    private int messageID;
    private int messageNum;
    private String recipientcell;
    private String message;
    private String messageHash;
    private String messageStatus;
    
    //Array Variables
    private static ArrayList<String> sentMessage = new ArrayList<String>();
    private static int totalMessagesSent =0;
    
    //Constructor
    public Message(int messageNum, String recipientcell , String message ){
        messageNum= this.messageNum;
        recipientcell=this.recipientcell;
        message=this.message;
        generateMessageID()=this.messageID;
        createMessageHash()=this.messageHash;
        
        
      
    }
    //Method 1
    public boolean checkMessageID() {
        //Valid if the ID has less than 10 characters
        boolean valid = false;
        if (messageID.length() <= 10){
            valid=true;
        }
      return valid  ;
    }
    
    public String checkRecipientCell(){
        String valid = "Cellphone number is incorrectly formatted or doesnt have contain the International Code.Please enter the correct details";
        if (recipientcell.matches("^\\+27[0-9]{9}$'")){
            valid = "Cellphone number captured successfully";
        }
     return valid;   
    }
    
    //Formart - first two digits of ID: message Number :FIRSTLASTWORD
        //eg. 00:0:HITONIGHT
    public String createMessageHash(){
        String firstTwo= messageID.substring(0,2);
        
        String[] words = message.trim().split(" ");
        String firstWord = words[0];
        String lastWord = words [words.length -1];
        
        // Remove punctuantion such as "?" or ","
        firstWord=firstWord.replaceAll("[^A-Za-z0-9]", "");
          lastWord=lastWord.replaceAll("[^A-Za-z0-9]", "");
          
          String hash = firstTwo + ":" + messageNum + ":" + firstWord + lastWord;
          return hash.toUpperCase();
    }
    public String sentMessage(int menuChoice){
        String validMenuOpt;
        
        switch (menuChoice) {
            case 1: sentMessages.add(getFullDetails());
                    totalMessagesSent++;
                    validMenuOpt = "Message sent successfully";
                    break;
            case 2: validMenuOpt = "Press 0 to delete the message";
                    break;
            case 3: validMenuOpt = "Message successfully stored";
                    break;
                    
            default : validMenuOpt = "Invalid choice";
                break;
        }
        return validMenuOpt;
    }
    public String printMessage(){
        
    }
    public int returnTotalMessages(){
        return totalMessagesSent;
    }
    //message legnth method
    public String checkMessageLength(){
        String lengthValid;
        
        if (message.length() <=250) {
            lengthValid = "Message is ready to send";
        }else {
            int extra = message.length() - 250;
            lengthValid = "Message exceeds 250 characters by" + extra + "; please reduce the size";
        }
        return lengthValid;
    }
    //research method
    public boolean storeMessage(){
        boolean stored;
        String filePath = "message.json";
        JSONArray messageArray = new JSONArray();
        
        File file = new File(filePath);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                JSONParser parser = new JSONParser();
                Object object = parser.parse(reader);
                if (obj instanceof JSONArray) {
                    messageArray = (JSONArray) obj ;
                    
                }
            }catch (IOException | ParseException e) {
                System.out.println("Could not read existing file, starting fresh");
            }
        }
        
        JSONObject messageObject = new JSONObject();
        messageObject.put("messageID", messageID);
        messageObject.put("messageHash", messageHash);
        messageObject.put("recipient", recipientcell);
        messageObject.put("message", message);
        messageArray.add(messageObject);
        
        try (FileWriter writer = new FileWriter(filePath)){
            writer.write(messageArray.toJSONString());
            writer.flush();
            stored = true;
        } catch (IOException e) {
            System.out.println("Error writing JSON file " + e.getMessage());
            stored = false;
        }
        return stored;
    }
    
    private String generateMessageID(){
        Random randomObj = new Random();
        
        String id = "";
        int count = 0;
        
        while (count < 10) {
            id = id + randomObj.nextInt(10);
            count++;
        }
        
        return id;
    }
    
    
    
    
}
