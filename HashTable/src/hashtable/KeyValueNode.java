
package hashtable;

/**
 *
 * @author Patricio
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
