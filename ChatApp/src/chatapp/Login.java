/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatapp;

/**
 *
 * @author Student
 */
public class Login {

    private String username;
    private String password;
    private String cellPhone;
    private String firstName;
    private String lastName;

    // CONSTRUCTOR
    public Login(String username, String password, String cellPhone,
                 String firstName, String lastName) {

        this.username = username;
        this.password = password;
        this.cellPhone = cellPhone;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // USERNAME CHECK
    public static boolean checkUserName(String username1) {

        return username1 != null
                && username1.contains("_")
                && username1.length() <= 5;
    }

    // PASSWORD CHECK
    public static boolean checkPasswordComplexity(String password1) {

        return password1 != null
                && password1.matches(
                        "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).{8,}$");
    }

    // CELL NUMBER CHECK
    public static boolean checkCellPhoneNumber(String cellNumber) {

        return cellNumber != null
                && cellNumber.matches("^\\+27\\d{9}$");
    }

    // REGISTER USER
    public String registerUser(String username,
                               String password,
                               String cellPhone,
                               String firstName,
                               String lastName) {

        if (!checkUserName(username)) {

            return "Username is not correctly formatted, "
                    + "please ensure that your username contains an "
                    + "underscore and is no more than five characters "
                    + "in length.";
        }

        if (!checkPasswordComplexity(password)) {

            return "Password is not correctly formatted; "
                    + "please ensure that the password contains at least "
                    + "eight characters, a capital letter, a number, "
                    + "and a special character.";
        }

        if (!checkCellPhoneNumber(cellPhone)) {

            return "Cell phone number is incorrectly formatted or does "
                    + "not contain an international code, please correct "
                    + "the number and try again.";
        }

        // Save the registered details.
        this.username = username;
        this.password = password;
        this.cellPhone = cellPhone;
        this.firstName = firstName;
        this.lastName = lastName;

        return "User registered successfully.";
    }

    // LOGIN USER
    public boolean loginUser(String enteredUsername,
                             String enteredPassword) {

        return enteredUsername != null
                && enteredPassword != null
                && enteredUsername.equals(username)
                && enteredPassword.equals(password);
    }

    // LOGIN STATUS
    public String returnLoginStatus(boolean loginStatus) {

        if (loginStatus) {

            return "Welcome "
                    + firstName
                    + ", it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }
}