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


    @Test
    public void testConstructors() {
        Hashtable ht = new Hashtable();
        assertEquals(31, ht.getCapacity());
        assertEquals(0.5f, ht.getLoadFactor(), 0.0f);
        
        ht = new Hashtable(25);
        //capacity debe ser siempre un numero primo
        assertEquals(Hashtable.getSiguientePrimo(25), ht.getCapacity());
        
        ht = new Hashtable(78, 0.2f);
        assertEquals(Hashtable.getSiguientePrimo(78), ht.getCapacity());
        assertEquals(0.2f, ht.getLoadFactor(), 0.0f);
        
        
        java.util.Hashtable<Integer, Double> htJava = new java.util.Hashtable<>();
        htJava.put(10, 20.5);
        htJava.put(35, 55.555);
        Hashtable<Integer, Double> htPatricio = new Hashtable<>(htJava);
        assertEquals(20.5, htPatricio.get(10), 0.0);
        assertEquals(55.555, htPatricio.get(35), 0.0);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorsExceptions1() {
        Hashtable ht = new Hashtable(-1, 0.2f);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testConstructorsExceptions2() {
        Hashtable ht = new Hashtable(3, 0.0f);
    }
        
}
