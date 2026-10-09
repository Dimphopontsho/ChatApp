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
import org.json.simple.parser.PaerseException;

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
    private static int totalMessageSent =0;
    
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
        if (messageID.length()<=10){
            valid=true;
        }
      return valid  ;
    }
    
    public boolean checkRecipientCell(){
        
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
    public String sentMessage(){
        
    }
    public String printMessage(){
        
    }
    public int returnTotalMessages(){
        
    }
    public boolean storeMessage(){
        
    }
}
