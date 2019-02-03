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
public class HashtableTestByPatricio {
    
    public HashtableTestByPatricio() {
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
        
    @Test
    public void testGet() {
        Hashtable<Integer, String> table = new Hashtable<>();
        table.put(20, "hola");
        assertEquals("hola", table.get(20));
        assertNotEquals("hola2", table.get(20));
        assertEquals(null, table.get(30));
    }
    
    @Test (expected = NullPointerException.class)
    public void testGetNPE() {
        Hashtable<Integer, String> table = new Hashtable<>();
        table.put(20, "hola");
        table.get(20);
        table.get(null);
    }
    
    @Test
    public void testPut() {
        Hashtable<Integer, String> table = new Hashtable<>();
        table.put(20, "hola");
        table.put(30, "por favor");
        assertEquals("hola", table.get(20));
        assertNotEquals("hola2", table.get(20));
        assertEquals("por favor", table.get(30));        
        
        //tiene que reemplazar el valor 
        String valorAnterior = table.put(20, "gracias");
        assertEquals("hola", valorAnterior);
        assertEquals("gracias", table.get(20));
        assertEquals(2, table.size());
    }
    
    @Test (expected = NullPointerException.class)
    public void testPutNPE1() {
        Hashtable<Integer, String> table = new Hashtable<>();
        table.put(null, "hola");
    }
    
    @Test (expected = NullPointerException.class)
    public void testPutNPE2() {
        Hashtable<Integer, String> table = new Hashtable<>();
        table.put(10, null);
    }
    
    @Test
    public void testRehash() {
        Hashtable<Integer, String> table = new Hashtable<>();
        //rehash hace que:
        //- la tabla tenga el doble de capacidad
        //- todos los objetos que antes estaban tambien deben estar
        assertEquals(31, table.getCapacity());
        table.put(10, "rojo");
        table.put(20, "azul");
        table.put(30, "verde");
        assertEquals(3, table.size());
        table.rehash();
        assertEquals(Hashtable.getSiguientePrimo(31*2), table.getCapacity());
        assertNotEquals(31, table.getCapacity());
        assertEquals(3, table.size());
        
        //Aca probamos autorehash
        table = new Hashtable<>(3);
        int capacity = table.getCapacity();
        assertEquals(3, capacity);
        table.put(10, "rojo");
        table.put(20, "azul");
        assertEquals(Hashtable.getSiguientePrimo(capacity*2), table.getCapacity());
    }
    
    @Test
    public void testGetSiguientePrimo() {
        //lista de primos: 2 3...17 19...283 293 
        assertEquals(3, Hashtable.getSiguientePrimo(2));
        assertEquals(19, Hashtable.getSiguientePrimo(17));
        assertEquals(293, Hashtable.getSiguientePrimo(283));
        assertEquals(281, Hashtable.getSiguientePrimo(278));
    }
    
    @Test
    public void testRemove() {
        Hashtable<Integer, String> table = new Hashtable<>();
        table.put(10, "hola");
        table.put(20, "gracias");
        assertEquals(2, table.size());
        String valueRemoved = table.remove(10);
        assertEquals(1, table.size());
        assertEquals("hola", valueRemoved);
        
        table.put(10, "por favor");
        assertEquals(2, table.size());
        assertEquals("por favor", table.get(10));
        
        valueRemoved = table.remove(50);
        assertEquals(2, table.size());
        assertEquals(null, valueRemoved);
    }
    
    @Test
    public void testSize() {
        //Ya testeado en otros tests
    }
    
    @Test
    public void testHashtableSinGenerics() {
        Hashtable table = new Hashtable();
        table.put("casa", 10);
        table.put(20, "coca");
        assertEquals(10, table.get("casa"));
        assertEquals("coca", table.get(20));
    }
    
    @Test
    public void testContainsKey() {
        Hashtable<Integer, String> table = new Hashtable<>();
        table.put(10, "hola");
        table.put(20, "gracias");
        assertEquals(true, table.containsKey(10));
        assertEquals(false, table.containsKey(30));
        table.remove(10);
        assertEquals(false, table.containsKey(10));
    }
    
    @Test
    public void testContainsValue() {
        Hashtable<Integer, String> table = new Hashtable<>();
        table.put(10, "hola");
        table.put(20, "gracias");
        assertEquals(true, table.containsValue("hola"));
        assertEquals(false, table.containsValue("nunca"));
        table.remove(10);
        assertEquals(false, table.containsValue("hola"));
    }
    
    @Test
    public void testPutAll() {
        java.util.Hashtable<Integer, String> tableJava = new java.util.Hashtable<>();
        tableJava.put(10, "hola");
        tableJava.put(20, "gracias");
        Hashtable<Integer, String> table = new Hashtable<>();
        table.putAll(tableJava);
        assertEquals(tableJava.size(), table.size());
        assertEquals(tableJava.get(10), table.get(10));
        
        tableJava.remove(10);
        assertNotEquals(tableJava.size(), table.size());
        assertEquals(null, tableJava.get(10));
        assertEquals("hola", table.get(10));
    }
    
    @Test
    public void testClear() {
        Hashtable<Integer, String> table = new Hashtable<>();
        table.put(10, "hola");
        table.put(20, "gracias");
        assertEquals(2, table.size());
        table.clear();
        assertEquals(0, table.size());
        assertEquals(null, table.get(10));
    }
    
    @Test
    public void testEquals() {
        Hashtable<Integer, String> table = new Hashtable<>();
        java.util.Hashtable<Integer, String> tableJava = new java.util.Hashtable<>();
        table.put(10, "hola");
        table.put(20, "gracias");
        tableJava.put(10, "hola");
        tableJava.put(20, "gracias");
        assertEquals(true, table.equals(tableJava));
        tableJava.remove(10);
        assertEquals(false, table.equals(tableJava));
    }
    
    @Test
    public void testHashcode() {
        Hashtable<Integer, String> table = new Hashtable<>();
        java.util.Hashtable<Integer, String> tableJava = new java.util.Hashtable<>();
        assertEquals(tableJava.hashCode(), table.hashCode());
        table.put(10, "hola");
        table.put(20, "hola");
        table.put(30, "hola");
        assertNotEquals(tableJava.hashCode(), table.hashCode());
        tableJava.put(10, "hola");
        tableJava.put(20, "hola");
        tableJava.put(30, "hola");
        assertEquals(tableJava.hashCode(), table.hashCode());
        table.remove(10);
        table.remove(20);
        table.remove(30);
        tableJava.remove(10);
        tableJava.remove(20);
        tableJava.remove(30);
        assertEquals(tableJava.hashCode(), table.hashCode());
    }
    
    @Test
    public void testClone() {
        
    }
}
