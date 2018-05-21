package hashtable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

/**
 * FINAL TSB 2018
 * @author Patricio Ezequiel Carranza, UTN FRC, legajo 60900
 */
public class Hashtable<K,V> extends Dictionary<K,V> implements java.util.Map, Serializable {
    
    private KeyValueNode<K,V>[] nodes;
     
    
    public Hashtable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Hashtable(int initialCapacity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Hashtable(int initialCapacity, float loadFactor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V get(Object key) {
        int index = key.hashCode() % nodes.length;
        
        //Podria haber colision (el index es igual) cuando dos hashCode() son similares.
        //Usamos DIRECCIONAMIENTO ABIERTO.
              
        ojo que tenemos que preguntar por el status del node
        while( ! nodes[index].key.equals(key) ) {
            index++;
            
            //Para evitar ArrayIndexOutOfBoundsException
            if(index >= nodes.length)
                index -= nodes.length;
            
            //Le dimos la vuelta entera al array y no encontramos coincidencia de key.
            //Segun java.util.Map debemos devolver null.
            if(index == key.hashCode() % nodes.length)
                return null;
        }
          
        TIRARA NPE SI NO HAY OBJETO INICIALIZADO EN ESE INDEX???
        return nodes[index].value;
    }

    @Override
    public Object put(Object key, Object value) {
        Tenga cuidado al hacer una inserción: en general en una tabla hash no se admiten valores repetidos, 
        por lo que antes de insertar un objeto debe buscar el mismo en la tabla. Si no se encuentra, se inserta. 
        Debe resistir la tentación de insertar el objeto en la primera tumba que encuentre, antes de terminar
        la búsqueda, pues el objeto podría estar más abajo... Lo que sí puede hacer es guardar el índice de la
        primera casilla tipo tumba que encuentre, y si luego descubre que el objeto no está en la tabla, guardarlo en ella
                
                
                
         Esto sugiere que la tabla debería empezar con un tamaño mayor al número de objetos esperados, y controlar el porcentaje de ocupación.
         Si el mismo llega a cierto valor crítico (entre 50 y 70 por ciento en muchas implementaciones), se debería redimensionar la tabla.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void putAll(Map m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set keySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection values() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Enumeration<K> keys() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Enumeration<V> elements() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
   
    
}
