package edu.utn.frc.tsb;

import java.io.Serializable;
import java.util.Map;

/**
 * FINAL TSB 2018
 * @author Patricio Ezequiel Carranza, UTN FRC, legajo 60900
 */
class KeyValueNode<K,V> implements Serializable, Map.Entry<K,V> {
    
    private static final long serialVersionUID = 42L;
    
    private K key;
    private V value;
    KeyValueFlags status;

    
    enum KeyValueFlags {
        //Numeros primos para hash, en una de esas coinciden menos...
        ABIERTO(151), CERRADO(2939), TUMBA(7919);
        private final int hash;
        
        KeyValueFlags(int hash) {
            this.hash = hash;
        }
        
        int getHash() {
           return hash;
        }
    }
    
    KeyValueNode() {
        status = KeyValueFlags.ABIERTO;
    }
    
    KeyValueNode(K key, V value) {
        status = KeyValueFlags.CERRADO;
        this.key = key;
        this.value = value;
    }
    
    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        else if (!(o instanceof Map.Entry)) return false;
        Map.Entry ent2 = (Map.Entry) o;
        //De acuerdo a java.util.Map.Entry
        return (this.getKey() == null ? ent2.getKey() == null : this.getKey().equals(ent2.getKey()))  &&
               (this.getValue() == null ? ent2.getValue() == null : this.getValue().equals(ent2.getValue()));
    }
    
    @Override
    public int hashCode() {
        //De acuerdo a estipulado en documentacion de java.util.Map.Entry.hashCode()
        return (getKey()==null   ? 0 : getKey().hashCode()) ^
              (getValue()==null ? 0 : getValue().hashCode());

    }
}
