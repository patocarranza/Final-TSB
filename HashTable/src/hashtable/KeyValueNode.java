package hashtable;

import java.io.Serializable;

/**
 * FINAL TSB 2018
 * @author Patricio Ezequiel Carranza, UTN FRC, legajo 60900
 */
class KeyValueNode<K,V> implements Serializable {
    
    private static final long serialVersionUID = 42L;
    
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
