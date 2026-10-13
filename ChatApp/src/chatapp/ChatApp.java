package chatapp;

import java.util.Scanner;

/**
 * @author Student
 */
public class ChatApp {

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

    // ===================== Part 2: QuickChat =====================

    private static void runQuickChat(Scanner input) {
        System.out.println("Welcome to QuickChat.");

        // The user sets how many messages they will enter, once, at the start
        int numMessages = readNumber(input, "How many messages do you wish to enter?");
        boolean messagesEntered = false;
        boolean running = true;

        while (running) {
            System.out.println("\n--- QuickChat Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            int option = readNumber(input, "Choose an option:");

            switch (option) {
                case 1:
                    if (messagesEntered) {
                        System.out.println("You have already entered your " + numMessages + " messages.");
                    } else {
                        sendMessages(input, numMessages);
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

                default:
                    System.out.println("Invalid option. Please choose 1, 2 or 3.");
                    break;
            }
        }
    }

    // Collects every message from the user (message number starts at 0)
    private static void sendMessages(Scanner input, int numMessages) {
        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");

            String recipient = readValidRecipient(input);
            String text = readValidMessage(input);

            Message message = new Message(i, recipient, text);
            handleMessageChoice(input, message);
        }

        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
    }

    // Repeats until the recipient number is correctly formatted
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

    // Repeats until the message is 250 characters or fewer
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

    // Send / Disregard / Store
    private static void handleMessageChoice(Scanner input, Message message) {
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
            System.out.println("\n" + message.getFullDetails());
        } else if (menuChoice == 2) {
            String deleteInput = input.nextLine();
            if (deleteInput.trim().equals("0")) {
                System.out.println("Message deleted.");
            }
        }
    }

    // Keeps asking until the user types a whole number
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