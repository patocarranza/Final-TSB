/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utn.frc.tsb;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Patricio
 */
public class HashtableTest {
    
    public HashtableTest() {
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
     * Test of getCapacity method, of class Hashtable.
     */
    @Test
    public void testGetCapacity() {
        System.out.println("getCapacity");
        Hashtable instance = new Hashtable();
        int expResult = 0;
        int result = instance.getCapacity();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLoadFactor method, of class Hashtable.
     */
    @Test
    public void testGetLoadFactor() {
        System.out.println("getLoadFactor");
        Hashtable instance = new Hashtable();
        float expResult = 0.0F;
        float result = instance.getLoadFactor();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class Hashtable.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        Object key = null;
        Hashtable instance = new Hashtable();
        Object expResult = null;
        Object result = instance.get(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of put method, of class Hashtable.
     */
    @Test
    public void testPut() {
        System.out.println("put");
        Object key = null;
        Object value = null;
        Hashtable instance = new Hashtable();
        Object expResult = null;
        Object result = instance.put(key, value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rehash method, of class Hashtable.
     */
    @Test
    public void testRehash() {
        System.out.println("rehash");
        Hashtable instance = new Hashtable();
        instance.rehash();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of esPrimo method, of class Hashtable.
     */
    @Test
    public void testEsPrimo() {
        System.out.println("esPrimo");
        int num = 0;
        boolean expResult = false;
        boolean result = Hashtable.esPrimo(num);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSiguientePrimo method, of class Hashtable.
     */
    @Test
    public void testGetSiguientePrimo() {
        System.out.println("getSiguientePrimo");
        int num = 0;
        int expResult = 0;
        int result = Hashtable.getSiguientePrimo(num);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of remove method, of class Hashtable.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        Object key = null;
        Hashtable instance = new Hashtable();
        Object expResult = null;
        Object result = instance.remove(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of size method, of class Hashtable.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        Hashtable instance = new Hashtable();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isEmpty method, of class Hashtable.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        Hashtable instance = new Hashtable();
        boolean expResult = false;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of containsKey method, of class Hashtable.
     */
    @Test
    public void testContainsKey() {
        System.out.println("containsKey");
        Object keyParam = null;
        Hashtable instance = new Hashtable();
        boolean expResult = false;
        boolean result = instance.containsKey(keyParam);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of containsValue method, of class Hashtable.
     */
    @Test
    public void testContainsValue() {
        System.out.println("containsValue");
        Object value = null;
        Hashtable instance = new Hashtable();
        boolean expResult = false;
        boolean result = instance.containsValue(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of contains method, of class Hashtable.
     */
    @Test
    public void testContains() {
        System.out.println("contains");
        Object value = null;
        Hashtable instance = new Hashtable();
        boolean expResult = false;
        boolean result = instance.contains(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of putAll method, of class Hashtable.
     */
    @Test
    public void testPutAll() {
        System.out.println("putAll");
        Map m = null;
        Hashtable instance = new Hashtable();
        instance.putAll(m);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clear method, of class Hashtable.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        Hashtable instance = new Hashtable();
        instance.clear();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Hashtable.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Hashtable instance = new Hashtable();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class Hashtable.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        Hashtable instance = new Hashtable();
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class Hashtable.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Hashtable instance = new Hashtable();
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clone method, of class Hashtable.
     */
    @Test
    public void testClone() throws Exception {
        System.out.println("clone");
        Hashtable instance = new Hashtable();
        Hashtable expResult = null;
        Hashtable result = instance.clone();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of keySet method, of class Hashtable.
     */
    @Test
    public void testKeySet() {
        System.out.println("keySet");
        Hashtable instance = new Hashtable();
        Set expResult = null;
        Set result = instance.keySet();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of values method, of class Hashtable.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        Hashtable instance = new Hashtable();
        Collection expResult = null;
        Collection result = instance.values();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of entrySet method, of class Hashtable.
     */
    @Test
    public void testEntrySet() {
        System.out.println("entrySet");
        Hashtable instance = new Hashtable();
//        Set<Map.Entry<K, V>> expResult = null;
//        Set<Map.Entry<K, V>> result = instance.entrySet();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of keys method, of class Hashtable.
     */
    @Test
    public void testKeys() {
        System.out.println("keys");
        Hashtable instance = new Hashtable();
        Enumeration expResult = null;
        Enumeration result = instance.keys();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of elements method, of class Hashtable.
     */
    @Test
    public void testElements() {
        System.out.println("elements");
        Hashtable instance = new Hashtable();
        Enumeration expResult = null;
        Enumeration result = instance.elements();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
