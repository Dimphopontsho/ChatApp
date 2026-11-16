
package chatapp;
 
import java.util.Scanner;
 
/**
 * QuickChat console application: registration and login (Part 1), sending, disregarding
 * and storing messages (Part 2), and the Stored Messages menu (Part 3).
 *
 * @author Student
 */
public class ChatApp {
 
    /**
     * Starts the program: register, log in, then open QuickChat if the login succeeds.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
 
        Login login = registerUser(input);
 
        if (loginUser(input, login)) {
            runQuickChat(input);
        } else {
            System.out.println("Login failed. You cannot use QuickChat.");
        }
 
        input.close();
    }
 
    // ===================== Part 1: Registration & Login =====================
 
    /**
     * Asks for the registration details, repeating each question until the answer is valid.
     *
     * @param input the keyboard scanner
     * @return the registered Login
     */
    private static Login registerUser(Scanner input) {
        String username;
        String password;
        String cellNumber;
 
        System.out.println("Register");
 
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();
 
        System.out.print("Enter last name: ");
        String lastName = input.nextLine();
 
        // Username validation loop
        while (true) {
            System.out.print("Enter username: ");
            username = input.nextLine();
 
            if (Login.checkUserName(username)) {
                System.out.println("Username successfully captured.");
                break;
            }
            System.out.println("Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.");
        }
 
        // Password validation loop
        while (true) {
            System.out.print("Enter password: ");
            password = input.nextLine();
 
            if (Login.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured.");
                break;
            }
            System.out.println("Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, and a special character.");
        }
 
        // Cell number validation loop
        while (true) {
            System.out.print("Enter cell number: ");
            cellNumber = input.nextLine();
 
            if (Login.checkCellPhoneNumber(cellNumber)) {
                System.out.println("Cell phone number successfully added.");
                break;
            }
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }
 
        Login login = new Login(username, password, cellNumber, firstName, lastName);
        System.out.println(login.registerUser(username, password, cellNumber, firstName, lastName));
        return login;
    }
 
    /**
     * Asks for the username and password and checks them.
     *
     * @param input the keyboard scanner
     * @param login the registered user
     * @return true if the login details are correct
     */
    private static boolean loginUser(Scanner input, Login login) {
        System.out.println("\n=== Login ===");
 
        System.out.print("Enter username: ");
        String loginUser = input.nextLine();
 
        System.out.print("Enter password: ");
        String loginPass = input.nextLine();
 
        boolean isLoggedIn = login.loginUser(loginUser, loginPass);
        System.out.println(login.returnLoginStatus(isLoggedIn));
        return isLoggedIn;
    }
 
    // ===================== Part 2 & 3: QuickChat =====================
 
    /**
     * Shows the QuickChat main menu (send, recent messages, quit, stored messages).
     * The stored messages are read from the JSON file when QuickChat opens.
     *
     * @param input the keyboard scanner
     */
    private static void runQuickChat(Scanner input) {
        System.out.println("Welcome to QuickChat.");
 
        // Part 3: one manager holds all the arrays; stored messages are read from the JSON file
        MessageManagement manager = new MessageManagement("Developer");
        manager.loadStoredMessages();
 
        // The user sets how many messages they will enter, once, at the start
        int numMessages = readNumber(input, "How many messages do you wish to enter?");
        boolean messagesEntered = false;
        boolean running = true;
 
        while (running) {
            System.out.println("\n--- QuickChat Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            int option = readNumber(input, "Choose an option:");
 
            switch (option) {
                case 1:
                    if (messagesEntered) {
                        System.out.println("You have already entered your " + numMessages + " messages.");
                    } else {
                        sendMessages(input, numMessages, manager);
                        messagesEntered = true;
                    }
                    break;
 
                case 2:
                    System.out.println("\n--- Recently Sent Messages ---");
                    System.out.println(Message.printMessage());
                    break;
 
                case 3:
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    running = false;
                    break;
 
                case 4:
                    storedMessagesMenu(input, manager);
                    break;
 
                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3 or 4.");
                    break;
            }
        }
    }
 
    // ===================== Part 3: Stored Messages menu =====================
 
    /**
     * Part 3 menu: sender and recipient, longest message, search by ID or recipient,
     * delete by hash, and the stored messages report.
     *
     * @param input the keyboard scanner
     * @param manager holds the message arrays
     */
    private static void storedMessagesMenu(Scanner input, MessageManagement manager) {
        int choice;
        do {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search messages for a recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display report of all stored messages");
            System.out.println("0) Back to main menu");
            choice = readNumber(input, "Choose an option:");
 
            switch (choice) {
                case 1:
                    System.out.println(manager.displaySenderAndRecipient());
                    break;
 
                case 2:
                    System.out.println("Longest stored message: " + manager.getLongestStoredMessage());
                    break;
 
                case 3:
                    System.out.println("Enter the message ID:");
                    System.out.println(manager.displayByMessageID(input.nextLine().trim()));
                    break;
 
                case 4:
                    System.out.println("Enter the recipient cell number:");
                    String[] found = manager.getStoredMessagesForRecipient(input.nextLine().trim());
                    if (found.length == 0) {
                        System.out.println("No messages found for that recipient.");
                    }
                    for (String text : found) {
                        System.out.println("- " + text);
                    }
                    break;
 
                case 5:
                    System.out.println("Enter the message hash:");
                    System.out.println(manager.deleteByHash(input.nextLine().trim()));
                    manager.saveStoredMessages();   // keep the JSON file in step
                    break;
 
                case 6:
                    System.out.println(manager.displayStoredReport());
                    break;
 
                case 0:
                    break;
 
                default:
                    System.out.println("Invalid option. Please choose 0 to 6.");
                    break;
            }
        } while (choice != 0);
    }
 
    /**
     * Collects every message from the user (message numbers start at 0).
     *
     * @param input the keyboard scanner
     * @param numMessages how many messages the user chose to enter
     * @param manager records each message in the arrays
     */
    private static void sendMessages(Scanner input, int numMessages, MessageManagement manager) {
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");
 
            String recipient = readValidRecipient(input);
            String text = readValidMessage(input);
 
            Message message = new Message(i, recipient, text);
            handleMessageChoice(input, message, manager);
        }
 
        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
    }
 
    /**
     * Repeats until the recipient number is correctly formatted.
     *
     * @param input the keyboard scanner
     * @return a valid recipient cell number
     */
    private static String readValidRecipient(Scanner input) {
        String recipient;
        while (true) {
            System.out.println("Enter recipient cell number:");
            recipient = input.nextLine().trim();
 
            if (Message.isValidRecipient(recipient)) {
                System.out.println(Message.RECIPIENT_VALID);
                return recipient;
            }
            System.out.println(Message.RECIPIENT_INVALID);
        }
    }
 
    /**
     * Repeats until the message is 250 characters or fewer.
     *
     * @param input the keyboard scanner
     * @return a valid message
     */
    private static String readValidMessage(Scanner input) {
        String text;
        while (true) {
            System.out.println("Enter your message (max 250 characters):");
            text = input.nextLine();
            System.out.println(Message.lengthMessage(text));
 
            if (Message.isValidLength(text)) {
                return text;
            }
        }
    }
 
    /**
     * Lets the user send, disregard or store a message, and records it in the Part 3 arrays.
     *
     * @param input the keyboard scanner
     * @param message the message to handle
     * @param manager records the message
     */
    private static void handleMessageChoice(Scanner input, Message message, MessageManagement manager) {
        System.out.println("\nWhat would you like to do with this message?");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
 
        int menuChoice;
        do {
            menuChoice = readNumber(input, "Choose an option:");
            if (menuChoice < 1 || menuChoice > 3) {
                System.out.println("Invalid choice. Please choose 1, 2 or 3.");
            }
        } while (menuChoice < 1 || menuChoice > 3);
 
        System.out.println(message.sentMessage(menuChoice));
 
        if (menuChoice == 1) {
            manager.addMessage(message, MessageManagement.STATUS_SENT);
            System.out.println("\n" + message.getFullDetails());
        } else if (menuChoice == 2) {
            manager.addMessage(message, MessageManagement.STATUS_DISREGARD);
            String deleteInput = input.nextLine();
            if (deleteInput.trim().equals("0")) {
                System.out.println("Message deleted.");
            }
        } else {
            manager.addMessage(message, MessageManagement.STATUS_STORED);
        }
    }
 
    /**
     * Keeps asking until the user types a whole number.
     *
     * @param input the keyboard scanner
     * @param prompt the question to show
     * @return the number entered
     */
    private static int readNumber(Scanner input, String prompt) {
        while (true) {
            System.out.println(prompt);
            String text = input.nextLine();
            try {
                return Integer.parseInt(text.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
 

