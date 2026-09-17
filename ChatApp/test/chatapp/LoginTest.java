/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package chatapp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Student
 */
public class LoginTest {

    public LoginTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of checkUserName method, of class Login.
     * Rule: username must contain an underscore and be no more than 5 characters.
     */
    @Test
    public void testCheckUserName_Valid() {
        System.out.println("checkUserName - valid username");
        String username1 = "kyl_1";
        boolean expResult = true;
        boolean result = Login.checkUserName(username1);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckUserName_NoUnderscore() {
        System.out.println("checkUserName - missing underscore");
        String username1 = "kyle1";
        boolean expResult = false;
        boolean result = Login.checkUserName(username1);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckUserName_TooLong() {
        System.out.println("checkUserName - too long");
        String username1 = "kyle_12345";
        boolean expResult = false;
        boolean result = Login.checkUserName(username1);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckUserName_Empty() {
        System.out.println("checkUserName - empty string");
        String username1 = "";
        boolean expResult = false;
        boolean result = Login.checkUserName(username1);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkPasswordComplexity method, of class Login.
     * Rule: at least 8 characters, one capital letter, one number, one special character.
     */
    @Test
    public void testCheckPasswordComplexity_Valid() {
        System.out.println("checkPasswordComplexity - valid password");
        String password1 = "Ch@t8App1";
        boolean expResult = true;
        boolean result = Login.checkPasswordComplexity(password1);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckPasswordComplexity_TooShort() {
        System.out.println("checkPasswordComplexity - too short");
        String password1 = "C@1a";
        boolean expResult = false;
        boolean result = Login.checkPasswordComplexity(password1);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckPasswordComplexity_NoCapitalLetter() {
        System.out.println("checkPasswordComplexity - no capital letter");
        String password1 = "ch@t8app1";
        boolean expResult = false;
        boolean result = Login.checkPasswordComplexity(password1);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckPasswordComplexity_NoNumber() {
        System.out.println("checkPasswordComplexity - no number");
        String password1 = "Ch@tApppp";
        boolean expResult = false;
        boolean result = Login.checkPasswordComplexity(password1);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckPasswordComplexity_NoSpecialCharacter() {
        System.out.println("checkPasswordComplexity - no special character");
        String password1 = "Chat8App1";
        boolean expResult = false;
        boolean result = Login.checkPasswordComplexity(password1);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckPasswordComplexity_Empty() {
        System.out.println("checkPasswordComplexity - empty string");
        String password1 = "";
        boolean expResult = false;
        boolean result = Login.checkPasswordComplexity(password1);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkCellPhoneNumber method, of class Login.
     * Rule: must start with +27 followed by exactly 9 digits (12 characters total).
     */
    @Test
    public void testCheckCellPhoneNumber_Valid() {
        System.out.println("checkCellPhoneNumber - valid number");
        String cellNumber = "+27831234567";
        boolean expResult = true;
        boolean result = Login.checkCellPhoneNumber(cellNumber);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckCellPhoneNumber_MissingInternationalCode() {
        System.out.println("checkCellPhoneNumber - missing international code");
        String cellNumber = "0831234567";
        boolean expResult = false;
        boolean result = Login.checkCellPhoneNumber(cellNumber);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckCellPhoneNumber_TooLong() {
        System.out.println("checkCellPhoneNumber - too many digits");
        String cellNumber = "+2783123456789";
        boolean expResult = false;
        boolean result = Login.checkCellPhoneNumber(cellNumber);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckCellPhoneNumber_Empty() {
        System.out.println("checkCellPhoneNumber - empty string");
        String cellNumber = "";
        boolean expResult = false;
        boolean result = Login.checkCellPhoneNumber(cellNumber);
        assertEquals(expResult, result);
    }

    /**
     * Test of registerUser method, of class Login.
     */
    @Test
    public void testRegisterUser_Success() {
        System.out.println("registerUser - all fields valid");
        String username = "kyl_1";
        String password = "Ch@t8App1";
        String cellPhone = "+27831234567";
        String firstName = "Kyle";
        String lastName = "Smith";
        Login instance = new Login("kyle", "Ch@t8App1", "+27831234567", "Kyle","Smith" );
        String expResult = "User registered successfully.";
        String result = instance.registerUser(username, password, cellPhone, firstName, lastName);
        assertEquals(expResult, result);
    }

    @Test
    public void testRegisterUser_InvalidUsername() {
        System.out.println("registerUser - invalid username");
        String username = "kyle";
        String password = "Ch@t8App1";
        String cellPhone = "+27831234567";
        String firstName = "Kyle";
        String lastName = "Smith";
        Login instance = new Login("kyle", "Ch@t8App1", "+27831234567", "Kyle","Smith" );
        String expResult = "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        String result = instance.registerUser(username, password, cellPhone, firstName, lastName);
        assertEquals(expResult, result);
    }

    @Test
    public void testRegisterUser_InvalidPassword() {
        System.out.println("registerUser - invalid password");
        String username = "kyl_1";
        String password = "password";
        String cellPhone = "+27831234567";
        String firstName = "Kyle";
        String lastName = "Smith";
        Login instance = new Login("kyle", "Ch@t8App1", "+27831234567", "Kyle","Smith" );
        String expResult = "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        String result = instance.registerUser(username, password, cellPhone, firstName, lastName);
        assertEquals(expResult, result);
    }

    @Test
    public void testRegisterUser_InvalidCellPhone() {
        System.out.println("registerUser - invalid cell phone number");
        String username = "kyl_1";
        String password = "Ch@t8App1";
        String cellPhone = "0831234567";
        String firstName = "Kyle";
        String lastName = "Smith";
        Login instance = new Login("kyle", "Ch@t8App1", "+27831234567", "Kyle","Smith" );
        String expResult = "Cell phone number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
        String result = instance.registerUser(username, password, cellPhone, firstName, lastName);
        assertEquals(expResult, result);
    }

    /**
     * Test of loginUser method, of class Login.
     */
    @Test
    public void testLoginUser_Success() {
        System.out.println("loginUser - correct credentials");
        String username = "kyl_1";
        String password = "Ch@t8App1";
        String cellPhone = "+27831234567";
        String firstName = "Kyle";
        String lastName = "Smith";
        Login instance = new Login("kyle", "Ch@t8App1", "+27831234567", "Kyle","Smith" );
        instance.registerUser(username, password, cellPhone, firstName, lastName);

        String enteredUsername = "kyl_1";
        String enteredPassword = "Ch@t8App1";
        boolean expResult = true;
        boolean result = instance.loginUser(enteredUsername, enteredPassword);
        assertEquals(expResult, result);
    }

    @Test
    public void testLoginUser_WrongPassword() {
        System.out.println("loginUser - incorrect password");
        String username = "kyl_1";
        String password = "Ch@t8App1";
        String cellPhone = "+27831234567";
        String firstName = "Kyle";
        String lastName = "Smith";
        Login instance = new Login("kyle", "Ch@t8App1", "+27831234567", "Kyle","Smith" );
        instance.registerUser(username, password, cellPhone, firstName, lastName);

        String enteredUsername = "kyl_1";
        String enteredPassword = "WrongPass1!";
        boolean expResult = false;
        boolean result = instance.loginUser(enteredUsername, enteredPassword);
        assertEquals(expResult, result);
    }

    @Test
    public void testLoginUser_WrongUsername() {
        System.out.println("loginUser - incorrect username");
        String username = "kyl_1";
        String password = "Ch@t8App1";
        String cellPhone = "+27831234567";
        String firstName = "Kyle";
        String lastName = "Smith";
        Login instance = new Login("kyle", "Ch@t8App1", "+27831234567", "Kyle","Smith" );
        instance.registerUser(username, password, cellPhone, firstName, lastName);

        String enteredUsername = "wrong_1";
        String enteredPassword = "Ch@t8App1";
        boolean expResult = false;
        boolean result = instance.loginUser(enteredUsername, enteredPassword);
        assertEquals(expResult, result);
    }

    /**
     * Test of returnLoginStatus method, of class Login.
     */
    @Test
    public void testReturnLoginStatus_Success() {
        System.out.println("returnLoginStatus - login successful");
        boolean loginStatus = true;
        Login instance = new Login("kyle", "Ch@t8App1", "+27831234567", "Kyle","Smith" );
        instance.registerUser("kyl_1", "Ch@t8App1", "+27831234567", "Kyle", "Smith");
        instance.loginUser("kyl_1", "Ch@t8App1");
        String expResult = "Welcome Kyle, it is great to see you again.";
        String result = instance.returnLoginStatus(loginStatus);
        assertEquals(expResult, result);
    }

    @Test
    public void testReturnLoginStatus_Failure() {
        System.out.println("returnLoginStatus - login failed");
        boolean loginStatus = false;
        Login instance = new Login("kyle", "Ch@t8App1", "+27831234567", "Kyle","Smith" );
        String expResult = "Username or password incorrect, please try again.";
        String result = instance.returnLoginStatus(loginStatus);
        assertEquals(expResult, result);
    }

}