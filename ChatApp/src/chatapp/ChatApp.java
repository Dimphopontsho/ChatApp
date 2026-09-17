/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chatapp;

import java.util.Scanner;

/**
 *
 * @author Student
 */
public class ChatApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        String username;
        String password;
        String cellNumber;
        String firstName;
        String lastName;
        
        //user registration
        System.out.println("Register");
        //prompt the user to enter first name and last name
        System.out.print("Enter Your first name: ");
        firstName = input.nextLine();

        System.out.print("Enter last name: ");
        lastName = input.nextLine();

        // Username validation loop
        while (true) {
            System.out.print("Enter username: ");
            username = input.nextLine();

            if (Login.checkUserName(username)) {
                System.out.println("Username successfully captured.");
                break;
            } else {
                System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
            }
        }

        // Password validation loop
        while (true) {
            System.out.print("Enter password: ");
            password = input.nextLine();

            if (Login.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured.");
                break;
            } else {
                System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            }
        }

        // Cell validation loop
        while (true) {
            System.out.print("Enter cell number: ");
            cellNumber = input.nextLine();

            if (Login.checkCellPhoneNumber(cellNumber)) {
                System.out.println("Cell phone number successfully added.");
                break;
            } else {
                System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
            }
        }

        //validates all input fields
        Login login = new Login(username, password, cellNumber, firstName, lastName);
        System.out.println(login.registerUser(username, password, cellNumber, firstName, lastName));

        //user login
        System.out.println("\n=== Login ===");

        System.out.print("Enter username: ");
        String loginUser = input.nextLine();

        System.out.print("Enter password: ");
        String loginPass = input.nextLine();

        boolean isLoggedIn = login.loginUser(loginUser, loginPass);

        System.out.println(login.returnLoginStatus(isLoggedIn));

        if (isLoggedIn) {
            System.out.println("\n--- Welcome to the Chat App ---");
        }
    }
}