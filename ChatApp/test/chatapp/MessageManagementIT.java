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
public class MessageManagementIT {
    
    public MessageManagementIT() {
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
     * Test of addMessage method, of class MessageManagement.
     */
    @Test
    public void testAddMessage() {
        System.out.println("addMessage");
        Message m = null;
        String status = "";
        MessageManagement instance = null;
        boolean expResult = false;
        boolean result = instance.addMessage(m, status);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadStoredMessages method, of class MessageManagement.
     */
    @Test
    public void testLoadStoredMessages_String() {
        System.out.println("loadStoredMessages");
        String path = "";
        MessageManagement instance = null;
        int expResult = 0;
        int result = instance.loadStoredMessages(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadStoredMessages method, of class MessageManagement.
     */
    @Test
    public void testLoadStoredMessages_0args() {
        System.out.println("loadStoredMessages");
        MessageManagement instance = null;
        int expResult = 0;
        int result = instance.loadStoredMessages();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveStoredMessages method, of class MessageManagement.
     */
    @Test
    public void testSaveStoredMessages_String() {
        System.out.println("saveStoredMessages");
        String path = "";
        MessageManagement instance = null;
        boolean expResult = false;
        boolean result = instance.saveStoredMessages(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveStoredMessages method, of class MessageManagement.
     */
    @Test
    public void testSaveStoredMessages_0args() {
        System.out.println("saveStoredMessages");
        MessageManagement instance = null;
        boolean expResult = false;
        boolean result = instance.saveStoredMessages();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSentMessages method, of class MessageManagement.
     */
    @Test
    public void testGetSentMessages() {
        System.out.println("getSentMessages");
        MessageManagement instance = null;
        String[] expResult = null;
        String[] result = instance.getSentMessages();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDisregardedMessages method, of class MessageManagement.
     */
    @Test
    public void testGetDisregardedMessages() {
        System.out.println("getDisregardedMessages");
        MessageManagement instance = null;
        String[] expResult = null;
        String[] result = instance.getDisregardedMessages();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStoredMessages method, of class MessageManagement.
     */
    @Test
    public void testGetStoredMessages() {
        System.out.println("getStoredMessages");
        MessageManagement instance = null;
        String[] expResult = null;
        String[] result = instance.getStoredMessages();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMessageHashes method, of class MessageManagement.
     */
    @Test
    public void testGetMessageHashes() {
        System.out.println("getMessageHashes");
        MessageManagement instance = null;
        String[] expResult = null;
        String[] result = instance.getMessageHashes();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMessageIDs method, of class MessageManagement.
     */
    @Test
    public void testGetMessageIDs() {
        System.out.println("getMessageIDs");
        MessageManagement instance = null;
        String[] expResult = null;
        String[] result = instance.getMessageIDs();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of displaySenderAndRecipient method, of class MessageManagement.
     */
    @Test
    public void testDisplaySenderAndRecipient() {
        System.out.println("displaySenderAndRecipient");
        MessageManagement instance = null;
        String expResult = "";
        String result = instance.displaySenderAndRecipient();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLongestStoredMessage method, of class MessageManagement.
     */
    @Test
    public void testGetLongestStoredMessage() {
        System.out.println("getLongestStoredMessage");
        MessageManagement instance = null;
        String expResult = "";
        String result = instance.getLongestStoredMessage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMessageByID method, of class MessageManagement.
     */
    @Test
    public void testGetMessageByID() {
        System.out.println("getMessageByID");
        String id = "";
        MessageManagement instance = null;
        String expResult = "";
        String result = instance.getMessageByID(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of displayByMessageID method, of class MessageManagement.
     */
    @Test
    public void testDisplayByMessageID() {
        System.out.println("displayByMessageID");
        String id = "";
        MessageManagement instance = null;
        String expResult = "";
        String result = instance.displayByMessageID(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMessagesForRecipient method, of class MessageManagement.
     */
    @Test
    public void testGetMessagesForRecipient() {
        System.out.println("getMessagesForRecipient");
        String recipient = "";
        MessageManagement instance = null;
        String[] expResult = null;
        String[] result = instance.getMessagesForRecipient(recipient);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStoredMessagesForRecipient method, of class MessageManagement.
     */
    @Test
    public void testGetStoredMessagesForRecipient() {
        System.out.println("getStoredMessagesForRecipient");
        String recipient = "";
        MessageManagement instance = null;
        String[] expResult = null;
        String[] result = instance.getStoredMessagesForRecipient(recipient);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteByHash method, of class MessageManagement.
     */
    @Test
    public void testDeleteByHash() {
        System.out.println("deleteByHash");
        String hash = "";
        MessageManagement instance = null;
        String expResult = "";
        String result = instance.deleteByHash(hash);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of displaySentReport method, of class MessageManagement.
     */
    @Test
    public void testDisplaySentReport() {
        System.out.println("displaySentReport");
        MessageManagement instance = null;
        String expResult = "";
        String result = instance.displaySentReport();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of displayStoredReport method, of class MessageManagement.
     */
    @Test
    public void testDisplayStoredReport() {
        System.out.println("displayStoredReport");
        MessageManagement instance = null;
        String expResult = "";
        String result = instance.displayStoredReport();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
