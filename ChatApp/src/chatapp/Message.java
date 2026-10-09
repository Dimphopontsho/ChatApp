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
        messageStaus=this.messageStatus;
        
        
      
    }
    //Method 1
    public boolean checkMessageID() {
        
    }
    
    public boolean checkRecipientCell(){
        
    }
    public String createMessageHash(){
        
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
