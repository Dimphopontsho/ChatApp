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
 * @author dtsotetsi
 */
public class JSONStorageIT {
    
    public JSONStorageIT() {
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
     * Test of readMessages method, of class JSONStorage.
     */
    @Test
    public void testReadMessages_String() {
        System.out.println("readMessages");
        String path = "";
        String[][] expResult = null;
        String[][] result = JSONStorage.readMessages(path);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readMessages method, of class JSONStorage.
     */
    @Test
    public void testReadMessages() {
        System.out.println("readMessages");
        String[][] expResult = null;
        String[][] result = JSONStorage.readMessages();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeMessages method, of class JSONStorage.
     */
    @Test
    public void testWriteMessages_String_StringArrArr() {
        System.out.println("writeMessages");
        String path = "";
        String[][] rows = null;
        boolean expResult = false;
        boolean result = JSONStorage.writeMessages(path, rows);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of writeMessages method, of class JSONStorage.
     */
    @Test
    public void testWriteMessages_StringArrArr() {
        System.out.println("writeMessages");
        String[][] rows = null;
        boolean expResult = false;
        boolean result = JSONStorage.writeMessages(rows);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
