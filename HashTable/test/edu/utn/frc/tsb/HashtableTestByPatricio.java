/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utn.frc.tsb;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.Iterator;
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
    public void testClone() throws CloneNotSupportedException {
        Hashtable<Integer, String> table = new Hashtable<>();
        table.put(10, "hola");
        table.put(20, "hola");
        table.put(30, "hola");
        Hashtable<Integer, String> tableClone = table.clone();
        assertEquals(tableClone.size(), table.size());
        assertEquals(tableClone.get(10), table.get(10));
        assertEquals(tableClone.hashCode(), table.hashCode());
        assertEquals(true, tableClone.equals(table));
        
        table.remove(10);
        assertNotEquals(tableClone.size(), table.size());
        assertEquals(null, table.get(10));
        assertEquals("hola", tableClone.get(10));
        assertNotEquals(tableClone.hashCode(), table.hashCode());
        assertEquals(false, tableClone.equals(table));
    }
    
    private Hashtable<Integer, String> setUpForCollectionsViewsTests() {
        Hashtable<Integer, String> table = new Hashtable<>();
        table.put(10, "hola");
        table.put(20, "hola");
        table.put(30, "hola");
        return table;
    }
    
    @Test
    public void testKeySet() {
        Hashtable<Integer, String> table = setUpForCollectionsViewsTests();
        Set<Integer> keys = table.keySet();
        assertEquals(table.size(), keys.size());
        assertEquals(true, keys.contains(10));
        assertEquals(false, keys.contains(40));
        
        //Devuelve el IteratorFailFast
        for(Integer singleInt : keys) {
            assertEquals(true, table.containsKey(singleInt));
        }
        
        //Remove() en el keySet debe tambien remover de la tabla
        keys.remove(10);
        assertEquals(2, keys.size());
        assertEquals(false, keys.contains(10));
        assertEquals(false, table.containsKey(10));
        assertEquals(2, table.size());
        
        //El set esta backed por la hashtable. Un cambio en la hash se ve
        //reflejado tambien en el set
        table.put(40, "blanco");
        table.put(50, "negro");
        table.put(60, "azul");
        assertEquals(table.size(), keys.size());
        assertEquals(true, keys.contains(40));
        
        //Aplicar remove() desde el iterador no genera excepciones, y remueve
        //la key desde el set y la tabla que la backea.
        Iterator<Integer> iter = keys.iterator();
        int count = 0;
        while(iter.hasNext()) {
            Integer keyInt = iter.next();
            assertEquals(true, table.containsKey(keyInt));
            if(count == 1) {
                iter.remove();
                assertEquals(keys.contains(keyInt), table.containsKey(keyInt));
                assertEquals(keys.size(), table.size());
            }
            count++;
        }
    }
    
    @Test (expected = ConcurrentModificationException.class)
    public void testKeySetCME1() {
        Hashtable<Integer, String> table = setUpForCollectionsViewsTests();
        Set<Integer> keys = table.keySet();
        Iterator<Integer> iter = keys.iterator();
        int count = 0;
        while(iter.hasNext()) {
            Integer keyInt = iter.next();
            assertEquals(true, table.containsKey(keyInt));
            if(count == 1) {
                //No se debe poder modificar la tabla mientras hay un iterador en curso
                table.put(50, "marron");
            }
            count++;
        }
    }
    
    @Test (expected = ConcurrentModificationException.class)
    public void testKeySetCME2() {
        Hashtable<Integer, String> table = setUpForCollectionsViewsTests();
        Set<Integer> keys = table.keySet();
        Iterator<Integer> iter = keys.iterator();
        int count = 0;
        while(iter.hasNext()) {
            Integer keyInt = iter.next();
            assertEquals(true, table.containsKey(keyInt));
            if(count == 1) {
                //No se debe poder modificar la tabla mientras hay un iterador en curso
                table.remove(30);
            }
            count++;
        }
    }
    
    @Test (expected = ConcurrentModificationException.class)
    public void testKeySetCME3() {
        Hashtable<Integer, String> table = setUpForCollectionsViewsTests();
        Set<Integer> keys = table.keySet();
        Iterator<Integer> iter = keys.iterator();
        int count = 0;
        while(iter.hasNext()) {
            Integer keyInt = iter.next();
            assertEquals(true, table.containsKey(keyInt));
            if(count == 1) {
                //No se debe poder modificar la tabla mientras hay un iterador en curso
                table.clear();
            }
            count++;
        }
    }
    
    @Test
    public void testKeySetCME4() {
        Hashtable<Integer, String> table = setUpForCollectionsViewsTests();
        Set<Integer> keys = table.keySet();
        Iterator<Integer> iter = keys.iterator();
        int count = 0;
        while(iter.hasNext()) {
            Integer keyInt = iter.next();
            assertEquals(true, table.containsKey(keyInt));
            if(count == 1) {
                //No cambia la estructura de la tabla, esta reemplazando
                //un value por otro de una key ya existente.
                table.put(10, "gracias");
            }
            count++;
        }
    }
    
    @Test
    public void testValues() {
        Hashtable<Integer, String> table = setUpForCollectionsViewsTests();
        Collection<String> values = table.values();
        assertEquals(table.size(), values.size());
        assertEquals(true, values.contains("hola"));
        assertEquals(false, values.contains("negativo"));
        
        //Devuelve el IteratorFailFast
        for(String singleStr : values) {
            assertEquals(true, table.containsValue(singleStr));
        }
        
        //Remove() en el values debe tambien remover de la tabla
        values.remove("hola");
        assertEquals(2, values.size());
        assertEquals(2, table.size());
        assertEquals(true, values.contains("hola"));
        assertEquals(true, table.containsValue("hola"));
        
        //La collection esta backed por la hashtable. Un cambio en la hash se ve
        //reflejado tambien en la collection
        table.put(40, "blanco");
        table.put(50, "negro");
        table.put(60, "azul");
        assertEquals(table.size(), values.size());
        assertEquals(true, values.contains("blanco"));
        
        //Aplicar remove() desde el iterador no genera excepciones, y remueve
        //la key desde la collection y la tabla que la backea.
        Iterator<String> iter = values.iterator();
        while(iter.hasNext()) {
            String str = iter.next();
            assertEquals(true, table.containsValue(str));
            if(str.equalsIgnoreCase("blanco")) {
                iter.remove();
                assertEquals(values.contains(str), table.containsValue(str));
                assertEquals(values.size(), table.size());
            }
        }
    }
    
    @Test
    public void testEntrySet() {
        Hashtable<Integer, String> table = setUpForCollectionsViewsTests();
        Set<Map.Entry<Integer,String>> entries = table.entrySet();
        assertEquals(table.size(), entries.size());
        assertEquals(true, entries.contains(new KeyValueNode<>(10, "hola")));
        assertEquals(false, entries.contains(new KeyValueNode<>(50, "charco")));
        
        //Devuelve el IteratorFailFast
        for(Map.Entry<Integer,String> entry : entries) {
            assertEquals(true, table.containsKey(entry.getKey()));
            assertEquals(true, table.containsValue(entry.getValue()));
        }
        
        //Remove() en el values debe tambien remover de la tabla
        entries.remove(new KeyValueNode<>(10, "hola"));
        assertEquals(2, entries.size());
        assertEquals(2, table.size());
        assertEquals(false, entries.contains(new KeyValueNode<>(10, "hola")));
        assertEquals(false, table.containsKey(new KeyValueNode<>(10, "hola").getKey()));
        
        //La collection esta backed por la hashtable. Un cambio en la hash se ve
        //reflejado tambien en la collection
        table.put(40, "blanco");
        table.put(50, "negro");
        table.put(60, "azul");
        assertEquals(table.size(), entries.size());
        assertEquals(true, entries.contains(new KeyValueNode<>(40, "blanco")));
        
        //Aplicar remove() desde el iterador no genera excepciones, y remueve
        //la key desde la collection y la tabla que la backea.
        Iterator<Map.Entry<Integer,String>> iter = entries.iterator();
        while(iter.hasNext()) {
            Map.Entry<Integer,String> str = iter.next();
            assertEquals(true, table.containsKey(str.getKey()));
            if(str.getValue().equalsIgnoreCase("blanco")) {
                iter.remove();
                assertEquals(entries.contains(str), table.containsKey(str.getKey()));
                assertEquals(entries.size(), table.size());
            }
        }
    }
    
    @Test
    public void testKeys() {
        Hashtable<Integer, String> table = setUpForCollectionsViewsTests();
        Enumeration<Integer> keys = table.keys();
        int count = 0;
        while(keys.hasMoreElements()) {
            Integer key = keys.nextElement();
            assertEquals(true, table.containsKey(key));
            count++;
        }
        assertEquals(count, table.size());
        
        //Segun contrato de Dictionary y de java.util.Hashtable esto no es failsafe
        //y la enumeration no esta backed (no es una "view") de la tabla.
        count = 0;
        while(keys.hasMoreElements()) {
            Integer key = keys.nextElement();
            assertEquals(true, table.containsKey(key));
            if(count == 1)
                table.put(100, "nuevo");
            if(count == 2)
                table.put(200, "mas nuevo");
            if(count == 3)
                table.remove(10);
            
            count++;
        }
        
        assertNotEquals(count, table.size());
    }
    
    @Test
    public void testElements() {
        Hashtable<Integer, String> table = setUpForCollectionsViewsTests();
        Enumeration<String> values = table.elements();
        int count = 0;
        while(values.hasMoreElements()) {
            String val = values.nextElement();
            assertEquals(true, table.containsValue(val));
            count++;
        }
        assertEquals(count, table.size());
        
        //Segun contrato de Dictionary y de java.util.Hashtable esto no es failsafe
        //y la enumeration no esta backed (no es una "view") de la tabla.
        count = 0;
        while(values.hasMoreElements()) {
            String val = values.nextElement();
            assertEquals(true, table.containsValue(val));
            if(count == 1)
                table.put(100, "nuevo");
            if(count == 2)
                table.put(200, "mas nuevo");
            if(count == 3)
                table.remove(10);
            
            count++;
        }
        
        assertNotEquals(count, table.size());
    }
    
    @Test
    public void testPerformance() {
        Hashtable<Integer, String> table = new Hashtable<>(12000);
        long time = 0;
        for(int i = 0; i < 6000; i++) { 
            time = System.nanoTime();
            table.put(i, "string " + i);
            time = System.nanoTime() - time;
            System.out.println("Delay put " + i + ": " + time + " nanoSecs");
        }
        
        for(int i = 0; i < 6000; i++) { 
            time = System.nanoTime();
            String str = table.get(i);
            time = System.nanoTime() - time;
            System.out.println("Delay get " + str + ": " + time + " nanoSecs");
        }
    }
}
