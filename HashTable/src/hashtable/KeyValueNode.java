
package hashtable;

/**
 * FINAL TSB 2018
 * @author Patricio Ezequiel Carranza, UTN FRC, legajo 60900
 */
class KeyValueNode<K,V> {
    
    K key;
    V value;
    KeyValueFlags status;
    
    enum KeyValueFlags {
        ABIERTO, CERRADO, TUMBA;
    }
    
    KeyValueNode() {
        status = KeyValueFlags.ABIERTO;
    }
    
}
