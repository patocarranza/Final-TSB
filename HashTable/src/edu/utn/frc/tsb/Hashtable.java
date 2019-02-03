package edu.utn.frc.tsb;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * FINAL TSB 2018
 * @author Patricio Ezequiel Carranza, UTN FRC, legajo 60900
 */
public class Hashtable<K,V> extends Dictionary<K,V>
                            implements java.util.Map<K,V>, 
                                       Serializable, 
                                       Cloneable {
    
    private static final long serialVersionUID = 42L;
    
    private KeyValueNode<K,V>[] nodos;
    private final float porcentajeOcupacionMaximo;
    private int nodosInsertados;
    private int ultimoHashcodeCalculado;
    
//    private volatile transient int hashEstructura;
     
    
    public Hashtable() {
        this(31);
    }
    
    public Hashtable(int initialCapacity) {
        this(initialCapacity, 0.5f);
    }
    
    
    public Hashtable(int initialCapacity, float loadFactor) {  
//        el tamaño de la tabla siempre debería ser un número primo, y esto 
//        debe garantizarse incluso si la clase implementada brinda un constructor que 
//        acepte como parámetro el tamaño deseado para la tabla a crear. Sin importar
//        el valor de ese parámetro, el constructor deberá crear una tabla de tamaño 
//        m primo, tal que m sea mayor que el  parámetro enviado
                
//      Algoritmo:
//      1- Calcular si initialCapacity es primo. En caso de serlo, ir al paso 3.
//      2- Calcular el proximo numero primo MAYOR a initialCapacity. Asignar este numero primo a initialCapacity
//      3- Crear el arreglo nodes con el tamaño de initialCapacity.
//      4- Inicializar todos los objetos en nodes con KeyValueNode en ABIERTO
                
//      Explicacion
//      El tamaño de la tabla siempre debería ser un número primo. Se puede demostrar
//      que si el tamaño de la tabla es un número primo y el porcentaje de ocupación no es mayor
//      al 50% de la tabla, entonces la exploración cuadrática garantiza que la clave será insertada.

        this.nodosInsertados = 0;
        
        //Segun contrato java.util.Hashtable y https://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/test/java/util/Hashtable/IllegalLoadFactor.java
        if(loadFactor <= 0.0f || Float.isNaN(loadFactor) || initialCapacity < 0)
            throw new IllegalArgumentException("loadFactor o initialCapacity son negativos o no son un numero");
        
        //no importa si loadFactor es un valor mayor a %50, apenas se ejecute put()
        //esta Hashtable duplicara su tamaño.
        while(loadFactor > 1.0f)
            loadFactor -= 0.5f;
        while(loadFactor <= 0.0f)
            loadFactor += 0.5f;
        this.porcentajeOcupacionMaximo = loadFactor;
        if( ! esPrimo(initialCapacity))
            initialCapacity = getSiguientePrimo(initialCapacity);
        nodos = new KeyValueNode[initialCapacity];
        for(int i = 0; i < initialCapacity; i++)
            nodos[i] = new KeyValueNode<>();     
        
        this.ultimoHashcodeCalculado = hashCode();
    }
    
    public Hashtable(Map<? extends K,? extends V> t) {
        //porcentajeOcupacionMaximo por default es 0.5, asi que duplicamos el tamaño
        //para evitar un rehash durante este constructor.
        this(t.size() * 2);
        this.putAll(t);
        this.ultimoHashcodeCalculado = hashCode();
    }
    
    public int getCapacity() {
        return nodos.length;
    }
    
    public float getLoadFactor() {
        return porcentajeOcupacionMaximo;
    }
    
    @Override
    public synchronized V get(Object key) {
//        Para buscar un objeto, se obtiene su dirección madre con la función de dispersion, y se entra en la tabla en esa dirección.
//        Si el objeto en esa casilla no es el buscado, se explora hacia abajo hasta encontrarlo (búsqueda exitosa), 
//        o hasta encontrar una casilla abierta (búsqueda infructuosa). Y de esta forma, la búsqueda puede plantearse así: 
//        obtenga la dirección madre del objeto a buscar. Entre en esa casilla. Si no contiene al objeto, 
//        realice una exploración cuadratica hacia abajo y en forma circular si fuera necesario, 
//        hasta dar con el objeto o enctontrar una casilla abierta (no una casilla cerrada o una tumba).
//        
//        En general la técnica de resolución de colisiones por direccionamiento abierto permite encontrar
//        un objeto en tiempo casi constante: puede verse que se requiere un acceso directo a la dirección madre, 
//        y una corta exploración en el peor caso para hallar el objeto
//        1- Preguntar si key es null. Puesto que no se permite almacenar null key
//           (por el contrato de java.util.Hashtable), devolvemos NPE
//        2- Preguntar si key es de la misma clase que K. Si no lo son
//           devolvemos ClassCastException (por el contrato de java.util.Map).
//        3- Buscamos si la key esta mapeada mediante busqueda cuadratica
//        4- Si encontramos mapeada la key hacemos return del value.
//        5- Si no esta mapeada la key, devolvemos null (contrato de java.util.HashTable).

//      1, 2)
//        checkValidKey(key);
        if(key == null)
            throw new NullPointerException("La key no puede ser null.");

//      3)
        int index = getKeyIndexConCuadratica((K)key, nodos);
//      4)
        if(index != -1)
            return nodos[index].getValue();
//      5)
        else
            return null;
    }

    @Override
    public synchronized V put(K key, V value) {
//        CAMBIA ESTRUCTURA DE HASHTABLE
//      Apunte
//        Al principio, cada casilla está vacía o abierta (de allí el nombre de esta técnica). Si un objeto 
//        O1 pide entrar en una casilla i y la misma está ABIERTA, O1 se almacena en ella.
//        A partir de este momento, la casilla i está CERRADA.
//                 
//        Si otro objeto O2 pide entrar en la tabla y la casilla i esta CERRADA, se prueba en la casilla i+1 (direccionamiento abierto).
//        Si i+1 está ABIERTA, O2 se almacena en ella. Pero si está CERRADA, se sigue probando con i+2, i+3, i+4...
//        (haciendo una exploración lineal) hasta llegar a una casilla ABIERTA.
//                
//        Puede preverse que a medida que la tabla se llene, las exploraciones lineales serán cada vez más largas.
//        Esto sugiere que la tabla debería empezar con un tamaño mayor al número de objetos esperados, y controlar 
//        el porcentaje de ocupación. Si el mismo llega a cierto valor crítico (entre 50 y 70 por ciento en muchas 
//        implementaciones), se debería redimensionar la tabla.

//        Por otra parte, aún cuando el porcentaje de ocupación de la tabla sea adecuado, el direccionamiento abierto 
//        tiende a presentar comportamientos no deseados conocidos como agrupamiento primario y agrupamiento secundario.
//        El agrupamiento primario es la tendencia de los objetos de la tabla a formar islas dentro de esa tabla. 
//        La tabla presenta muchos espacios libres, pero los objetos tienden a caer cerca de las mismas direcciones 
//        madre. Esto provoca que para ciertas claves, la inserción resulte muy costosa pues debe acomodarse dentro de 
//        un grupo ya grande, y además la isla crece en tamaño con la inserción de la nueva clave. Una explicación para
//        este comportamiento es que muchas veces los objetos vienen en secuencias de entrada no aleatorias, haciendo que
//        la función de dispersión acomode juntos a varios de esos objetos.
//                
//        Una solución para el agrupamiento primario consiste en realizar lo que se conoce como exploración cuadrática
//        (en lugar de exploración lineal): Si la casilla i está ocupada, se sigue con i + 1 y luego con i + 4, i + 9, ... i + j^2  
//        (con j = 1, 2, 3, ...). Está claro que el agupamiento primario se rompe con este tipo de exploración, pero ahora se produce 
//        el agrupamiento secundario: dada una dirección madre i, siempre se exploran las mismas casillas de allí en adelante. 
//        Esto lleva a otro posible problema: la exploración cuadrática podría no garantizar que una clave se inserte finalmente en
//        la tabla, aún habiendo lugar libre.
//        No obstante, se puede demostrar que si el tamaño de la tabla es un número primo y el porcentaje de 
//        ocupación no es mayor al 50% de la tabla, entonces la exploración cuadrática garantiza que la clave será insertada.
//        
//        Se debería monitorear la carga de la tabla  y en caso de llegar al punto de intervención, aumentar
//        el tamaño de la tabla (por ejemplo, en un 50% o hasta el número primo siguiente a ese 50%). 
//        El proceso es: se crea la nueva tabla, se toman todos los registros que estaban en la anterior, y
//        se redispersan en la nueva tabla: no se puede sólo copiar los registros de una a otra, pues los 
//        tamaños de ambas no coinciden y los valores calculados por h() no serán consistentes. Al terminar, la vieja tabla se elimina.

//      Algoritmo
//      1- Preguntar si la key o el value son null. Si alguno de los dos es null, tirar NPE.
//         Contrato de java.util.Hashtable.put().
//      2- Preguntar si dicha key ya se encuentra mapeada a un valor en nodes. 
//         Si la key ya estaba mapeado a otro valor, el valor antiguo se debe almacenar en una variable
//         ya que esto es lo que pondremos en el return. La key se mapeara ahora con el nuevo valor.
//         Ir directo a ultimo paso devolviendo el valor antiguo
//      3- Si la key no se encontraba ya insertada. significa que estamos insertando una nueva key.
//         Checkeamos si ((size() + 1) / nodos.length) >= porcentajeOcupacionMaximo. Si esto da true entonces se hace
//         rehash().
//      4- Se calcula index = key.hashCode() % nodes.length. Si nodes[index] esta ABIERTA o TUMBA, se inserta ahi.
//         Ir directo al ultimo paso devolviendo null.
//      5- Si nodes[index] esta CERRADA, entonces se debe hacer exploración cuadratica para
//         encontrar un lugar donde guardar el nuevo par key/value. CUIDADO CON ArrayIndexOutOfBoundsException!!
//      6- Return value antiguo si se cumplio todo en paso 3. Return null si se continuo con el paso 4, 5 y 6

//      1)
//      checkValidKey(key);
        if(key == null || value == null)
            throw new NullPointerException("La key o el value es null.");
        
//      2)
        int index = getKeyIndexConCuadratica((K)key, nodos);
        if(index != -1) {
            V valRet = nodos[index].getValue();
//            nodos[index].key = key;
            nodos[index].setValue(value);
            return valRet;
        }
        
//      3)
        if(checkOcupacionMasUno())
            rehash();
            
//      4, 5)
        index = getIndexParaPutConCuadratica(key, nodos);
        KeyValueNode<K,V> nodoNuevo = new KeyValueNode<>(key, value);
        nodos[index] = nodoNuevo;
        nodosInsertados++;
        this.ultimoHashcodeCalculado = this.hashCode();
        
//      6)
        return null;
    }
    
    /**
     * Checkea si el arreglo de nodos pasa a tener un porcentaje de ocupacion mayor
     * a porcentajeOcupacionMaximo (definido en el constructor) en el caso de que
     * se le agregue un nodo mas.
     * @return true si el arreglo supera el porcentaje de ocupacion si se le agrega
     * un nodo mas, sino falso.
     */
    private boolean checkOcupacionMasUno() {
        float size = size() + 1;
        float length = nodos.length;
        if((size / length) >= porcentajeOcupacionMaximo)
            return true;
        return false;
    }
    
    protected synchronized void rehash() {
//        CAMBIA ESTRUCTURA DE HASHTABLE
//        Un control importante que debe realizarse es el del porcentaje de carga de la tabla (si se usa direccionamiento abierto) 
//        Se debería monitorear la carga de la tabla, y en caso de llegar al punto de intervención, aumentar el tamaño de la 
//        tabla (por ejemplo, en un 50% o hasta el número primo siguiente a ese 50%).
//        
//        El proceso es simple pero debe hacerse con cuidado: se crea la nueva tabla, se toman todos los registros que estaban 
//        en la anterior, y se redispersan en la nueva tabla: no se puede sólo copiar los registros de una a otra, pues los
//        tamaños de ambas no coinciden y los valores calculados por h() no serán consistentes. Al terminar, la vieja tabla 
//        se elimina, y la nueva debe tomar el nombre de la eliminada (o ser apuntada por la referencia que antes apuntaba a la vieja tabla).
//      1- Crear un nuevo array, asignandole de tamaño getSiguientePrimo(nodos.length*2).
//      2- Por cada nodo con status.CERRADA en el array original se debe calcular el index
//         a ocupar en el nuevo array, y copiar la key y value al nuevo array.
//      3- Reemplazar la referencia del array viejo por el nuevo array.

//      1)
        KeyValueNode<K,V>[] nuevoArray = new KeyValueNode[getSiguientePrimo(nodos.length*2)];
        for(int i = 0; i < nuevoArray.length; i++)
            nuevoArray[i] = new KeyValueNode<>();
        
//      2)
        for(KeyValueNode<K,V> node : nodos) {
            if(node.status == KeyValueNode.KeyValueFlags.CERRADO) {
                int index = getIndexParaPutConCuadratica(node.getKey(), nuevoArray);
                nuevoArray[index] = node;
            }
        }
//      3)
        nodos = nuevoArray;
        this.ultimoHashcodeCalculado = this.hashCode();
    }

    protected static boolean esPrimo(int num) {        
        if(num <= 2 || num % 2 == 0 ) 
            return false;
        
        //Solo necesitamos fijarnos hasta la raiz cuadrada
        //https://stackoverflow.com/questions/5811151/why-do-we-check-up-to-the-square-root-of-a-prime-number-to-determine-if-it-is-pr
        int top = (int)Math.sqrt(num);
        for(int i = 3; i <= top; i+=2){
            if(num % i == 0)
                return false;
        }
        return true;
    }
    
    protected static int getSiguientePrimo(int num) {
        //El numero que viene por parametro podria ser primo. Nos interesa
        //que nos de el siguiente primo si o si, si el que pasan por parametro
        //es primo no nos interesa (que el usuario lo pruebe usando esPrimo())
        num++;
        if(num % 2 == 0)
            num++;
        for(; ; num+=2){
            if(esPrimo(num))
                return num;
        }
    }
    
    /**
     * 
     * @param key
     * @return  
    */
    @Override
    public synchronized V remove(Object key) {
//        CAMBIA ESTRUCTURA DE HASHTABLE
////         En algún momento se querrá eliminar un objeto de la tabla.
////         En principio, usamos marcado lógico para eliminar el objeto. Para ello, se debe buscar el objeto con el procedimiento anterior 
////         (exploracion cuadratica) y una vez hallado marcar la casilla como ABIERTA. Pero eso invalidaría el procedimiento de búsqueda: 
////         si este debe terminar al encontrar una casilla ABIERTA, la búsqueda podría darse por infructuosa cuando quizás el 
////         objeto esté más abajo en la tabla. De modo que al borrar un objeto lo que se hace es dejar su casilla como TUMBA.
//        1- Preguntar si key es null. Puesto que no se permite almacenar null key
//           (por el contrato de java.util.Hashtable), devolvemos NPE
//        2- Preguntar si key es de la misma clase que K. Si no lo son
//           devolvemos ClassCastException (por el contrato de java.util.Map).
//        3- Buscamos si la key esta mapeada mediante busqueda cuadratica
//        4- Si encontramos mapeada la key, seteamos el nodo en TUMBA, nulizamos la key y value del nodo
//           y hacemos return del value.
//        5- Si no esta mapeada la key, devolvemos null (contrato de java.util.HashTable).

//      1, 2)
        if(key == null)
            throw new NullPointerException("La key no puede ser null.");

//      3)
        int index = getKeyIndexConCuadratica((K)key, nodos);
//      4)
        if(index != -1) {
            nodosInsertados--;
            nodos[index].status = KeyValueNode.KeyValueFlags.TUMBA;
//            nodos[index].key = null;
            V value = nodos[index].getValue();
//            nodos[index].value = null;

            this.ultimoHashcodeCalculado = this.hashCode();
            return value;
        }
//      5)
        else
            return null;
    }
    
    @Override
    public synchronized int size() {
        return nodosInsertados;
    }

    @Override
    public synchronized boolean isEmpty() {
        return nodosInsertados == 0;
    }
    
    /**
     * Metodo para checkear que la key pasada como Object sea valida.
     * @param key
     * @throws NullPointerException si la key es null (no se permite segun contrato de java.util.HashTable)
     */
//    protected synchronized void checkValidKey(Object key)
//            throws NullPointerException {
//        if(key == null)
//            throw new NullPointerException("La key pasada por parametro no puede ser null.");          
////        
//        put((K)keyParam, (V)null);
//        System.out.println("stop");
//        
////        TODO ESTO NO FUE EXITOSO, las librerias de Java tampoco lo logran hacer.
////        Z casted = clazz.cast(keyParam);
//
////        Class<K> clazz = keyParam
////        try {
////            K castedKeyParam = (K) keyParam;
////            System.out.println(castedKeyParam);
////            return castedKeyParam;
////        } catch(ClassCastException ex) {
////            throw ex;
////        }
//    }
    
    /**
     * Metodo para checkear que el Value pasado como Object sea valido.
     *
     * @param valueParam
     * @throws NullPointerException si el value es null (no se permite segun
     * contrato de java.util.HashTable)
     * @throws ClassCastException si el value no es casteable a generic V
     */
//    private void checkValidValue(Object valueParam)
//            throws NullPointerException, ClassCastException {
//        if(valueParam == null)
//            throw new NullPointerException("El value pasado por parametro no puede ser null.");
//        
//        try {
//            V castedKeyParam = (V) valueParam;
//        } catch(ClassCastException ex) {
//            throw ex;
//        }
//    }

    /**
     * @param key
     * @return true si hay una key en la hashTable que cumpla con equals(). Falso en caso
     * contrario.
     * @throws NullPointerException si keyParam es null (por contrato de java.util.HashTable.containsKey()).
     */
    @Override
    public synchronized boolean containsKey(Object key) 
            throws NullPointerException {
//        1- Preguntar si keyParam es null. Puesto que no se permite almacenar null key
//           (por el contrato de java.util.Hashtable), devolvemos NPE
//        2- Preguntar si keyParam es de la misma clase que K. Si no lo son
//           devolvemos ClassCastException (por el contrato de java.util.Map).
//        3- Si keyParam no es null, calcular index = keyParam.hashCode() % nodes.length. Si nodes[index] esta ABIERTA entonces
//           la key no esta presente y devolvemos false. 
//        4- Si nodes[index] es CERRADA entonces hacemos preguntamos si nodes[index].key.equals(keyParam). 
//           Si esto da false, entonces hacemos exploración cuadrática, repitiendo en cada caso el check de
//           ABIERTA, CERRADA o TUMBRA de los pasos 2, 3 y 4.
//        5- si nodes[index] es TUMBA, entonces hacemos exploracion cuadratica. La busqueda
//           continua mientras el nodo obtenido sea TUMBA o (CERRADA && ! nodes[index].key.equals(keyParam)),
//           y termina cuando encontramos (CERRADA && nodes[index].key.equals(keyParam)) o ABIERTA.

//      1, 2)
//        checkValidKey(key);
        if(key == null)
            throw new NullPointerException("La key no puede ser null.");
        
//      3, 4 y 5)
        if(getKeyIndexConCuadratica((K)key, nodos) == -1)
            return false;
        return true;
    }
    
    private int getHashIndex(K keyParam, KeyValueNode[] array) {
        int index = keyParam.hashCode() % array.length;
        //hashCode() podria dar un valor negativo, dejando el resto en negativo
        if(index < 0)
            index += array.length;
        return index;
    }
    
    /**
     * Devuelve el index en donde se encuentra ubicada dicha keyParam.
     * @param keyParam
     * @param array
     * @return 
     */
    private int getKeyIndexConCuadratica(K keyParam, KeyValueNode[] array) {
        int keyHashIndex = getHashIndex(keyParam, array);
        for(int i = 0; ;i++) { 
            int indexCuad = keyHashIndex + (i^2);
            //indexCuad puede crecer mayor al arreglo de nodos. Debemos restarle
            //para evitar ArrayIndexOutOfBoundsException.
            while(indexCuad >= array.length)
                indexCuad -= array.length;
            KeyValueNode<K,V> nodo = array[indexCuad];
            
            //ABIERTO: termina la busqueda, no se encontro la key en el arreglo.
            if(nodo.status == KeyValueNode.KeyValueFlags.ABIERTO) 
                return -1;
            else if(nodo.status == KeyValueNode.KeyValueFlags.TUMBA)
                continue;
            else if(nodo.status == KeyValueNode.KeyValueFlags.CERRADO) {
                if(nodo.getKey().equals(keyParam))
                    return indexCuad;
                continue;
            }
        }
    }    
    
    /**
     * Devuelve un index en el cual es posible insertar un nodo.
     * @param keyParam
     * @param array
     * @return 
     */
    private int getIndexParaPutConCuadratica(K keyParam, KeyValueNode[] array) {
        int keyHashIndex = getHashIndex(keyParam, array);
        for(int i = 0; ;i++) { 
            int indexCuad = keyHashIndex + (i^2);
            //indexCuad puede crecer mayor al arreglo de nodos. Debemos restarle
            //para evitar ArrayIndexOutOfBoundsException.
            while(indexCuad >= array.length)
                indexCuad -= array.length;
            KeyValueNode<K,V> nodo = array[indexCuad];
            

            if(nodo.status == KeyValueNode.KeyValueFlags.ABIERTO) 
                return indexCuad;
            else if(nodo.status == KeyValueNode.KeyValueFlags.TUMBA)
                return indexCuad;
            else if(nodo.status == KeyValueNode.KeyValueFlags.CERRADO) {
                if(nodo.getKey().equals(keyParam))
                    return indexCuad;
                continue;
            }
        }
    }

    @Override
    public synchronized boolean containsValue(Object value) {
//        checkValidValue(value);
        if(value == null)
            throw new NullPointerException("El value pasado por parametro no puede ser null.");
        
        try {
            return this.values().contains((V) value);
        } catch(NoSuchElementException ex) {
            return false;
        }
    }    

    @Override
    public synchronized void putAll(Map<? extends K,? extends V> m) {
        if(m == this)
            return;
//        CAMBIA ESTRUCTURA DE HASHTABLE
        m.entrySet().forEach((entry) -> put(entry.getKey(), entry.getValue()));
        this.ultimoHashcodeCalculado = this.hashCode();
    }

    @Override
    public synchronized void clear() {
//        CAMBIA ESTRUCTURA DE HASHTABLE
        nodosInsertados = 0;
        for(int i = 0; i < nodos.length; i++)
            nodos[i] = new KeyValueNode<>();
        this.ultimoHashcodeCalculado = this.hashCode();
    }
    
    @Override
    public synchronized String toString() {
        StringBuilder str = new StringBuilder();
        //Segun contrato java.util.Hashtable.toString()
        for(Map.Entry<K,V> entry : this.entrySet()) 
            str.append("{key = ")
               .append(entry.getKey().toString())
               .append(", value = ")
               .append(entry.getValue().toString())
               .append("}\n");
       return str.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        //De acuerdo a contrato de java.util.Map
        else if (!(o instanceof java.util.Map)) return false;
        Map map2 = (Map) o;
        if(this.size() != map2.size())
            return false;
        
        for(Map.Entry entry : this.entrySet()) {
            if(map2.containsKey(entry.getKey()) && map2.get(entry.getKey()).equals(entry.getValue()))
                continue;
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        //De acuerdo a contrato de java.util.Map
        int hash = 0;
        int count = 1;
        for(KeyValueNode nodo : this.nodos) {
            //No hace falta seguir recorriendo, ya hemos visitado todas las posiciones
            //del arreglo que tenian un objeto.
            if(count > size())
                break;
            //Hay momentos, como el remove(), que no quitan un nodo de la tabla, sino
            //que simplemente le cambian la bandera de status. Los unicos nodos que
            //estan activos en la tabla son aquellos que tienen status CERRADO.
            if(nodo.status != KeyValueNode.KeyValueFlags.CERRADO)
                continue;
            
            //De acuerdo a estipulado por java.util.Map.hashCode() junto con la 
            //documentacion de Map.Entry y AbstractMap.
            hash += nodo.hashCode();
        }
        return hash;
    }
    
    @Override
    public Hashtable clone() 
            throws CloneNotSupportedException {
        //de acuerdo a https://stackoverflow.com/a/5431006/7416707
        Hashtable<K,V> cloneTable = (Hashtable<K,V>) super.clone();
        for(Map.Entry<K,V> entry : this.entrySet())
            cloneTable.put(entry.getKey(), entry.getValue());
        
        return cloneTable;
    }

    
    //Hashtable indica en su documentacion: The iterators returned by the iterator 
    //method of the collections returned by all of this class's "collection view methods"
    //are fail-fast: if the Hashtable is structurally modified at any time after the 
    //iterator is created, in any way except through the iterator's own remove method, 
    //the iterator will throw a ConcurrentModificationException
    @Override
    public synchronized Set<K> keySet()
            throws ConcurrentModificationException {
        return Collections.synchronizedSet(new KeySet<>());
    }

    @Override
    public synchronized Collection<V> values()
            throws ConcurrentModificationException {
        return Collections.synchronizedCollection(new ValuesCollection<>());
    }

    @Override
    public synchronized Set<Map.Entry<K,V>> entrySet() 
            throws ConcurrentModificationException  {
        return Collections.synchronizedSet(new EntrySet<>());
    }

    //Hashtable indica en su documentacion: The Enumerations returned by Hashtable's 
    //keys and elements methods are not fail-fast.
    @Override
    public synchronized java.util.Enumeration<K> keys() {
        return new Enumeration<>(this.keySet());
    }

    //Hashtable indica en su documentacion: The Enumerations returned by Hashtable's 
    //keys and elements methods are not fail-fast.
    @Override
    public synchronized java.util.Enumeration<V> elements() {
        return new Enumeration<>(this.values());
    }

    
    
    
    
    
    private class ValuesCollection<V> extends AbstractCollection<V> {
        //The collection supports element removal, which removes the corresponding mapping from
        //the map, via the Iterator.remove, Collection.remove, removeAll, retainAll and clear
        //operations. It does not support the add or addAll operations.
        
        @Override
        public Iterator<V> iterator() {
//            The iterators returned by the iterator method of the collections returned by all of this class's "collection view methods" 
//            are fail-fast: if the Hashtable is structurally modified at any time after the iterator is created, in any way except through 
//            the iterator's own remove method, the iterator will throw a ConcurrentModificationException
            return new IteratorFailFast<>(Hashtable.this.hashCode(), IteratorType.VALUES);
        }

        @Override
        public int size() {
            return nodosInsertados;
        }
        
        @Override
        public boolean addAll(Collection<? extends V> c) {
            throw new UnsupportedOperationException();
        }
    }
    
    
    
    private class KeySet<K> extends AbstractSet<K> {
        //The set supports element removal, which removes the corresponding mapping 
        //from the map, via the Iterator.remove, Set.remove, removeAll, retainAll, 
        //and clear operations. It does not support the add or addAll operations.
        
        @Override
        public Iterator<K> iterator() {
            return new IteratorFailFast<>(Hashtable.this.hashCode(), IteratorType.KEY);
        }

        @Override
        public int size() {
            return nodosInsertados;
        }
        
        
        @Override
        public boolean addAll(Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
    }
    
    
    
    private class EntrySet<K,V> extends AbstractSet<Map.Entry<K,V>>  {
        //If the map is modified while an iteration over the set is in progress 
        //(except through the iterator's own remove operation, or through the setValue 
        //operation on a map entry returned by the iterator) the results of the iteration are undefined
        
//        OJO ACA QUE SE PERMIETE USAR EL setValue() DE UNA ENTRY SIN QUE TIRE CONCURRENTEXCEPTION!
        
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
//            The iterators returned by the iterator method of the collections returned by all of this class's "collection view methods" 
//            are fail-fast: if the Hashtable is structurally modified at any time after the iterator is created, in any way except through 
//            the iterator's own remove method, the iterator will throw a ConcurrentModificationException
            return new IteratorFailFast<>(Hashtable.this.hashCode(), IteratorType.ENTRY);
        }

        @Override
        public int size() { 
            return nodosInsertados;
        }
        
        @Override
        public boolean addAll(Collection<? extends Map.Entry<K,V>>  c) {
            throw new UnsupportedOperationException();
        }
    }
    
    
    
    
    private enum IteratorType {
        VALUES, KEY, ENTRY;
    }
    
    private class IteratorFailFast<P> implements Iterator<P> {
        
//        private ArrayList<P> items;
        private int currentIndex;
        private KeyValueNode<K,V> ultimoObjetoReturned;
        private int hashAlCrearIterator;
        private boolean nextFueLlamado;
        private final IteratorType type;
        private int objetosDevueltos;
        
        IteratorFailFast(int hashEstructura, IteratorType type) {
            this.currentIndex = -1;
            this.hashAlCrearIterator = hashEstructura;
            this.type = type;
            this.objetosDevueltos = 0;
        }

        @Override
        public boolean hasNext() {
            //DEBE SER FAIL-FAST!!
            this.checkConcurrentModEx();

            if(size() <= objetosDevueltos)
                return false;
            return true;
        }
        
        private void checkConcurrentModEx() {
            if(hashAlCrearIterator != ultimoHashcodeCalculado)
                throw new ConcurrentModificationException("Se ha modificado la estructura de la Hashtable mientras este iterador estaba en uso.");
        }

        @Override
        public P next() {
            //DEBE SER FAIL-FAST!!
            this.checkConcurrentModEx();
            
            try {
                currentIndex++;
                for(;currentIndex < nodos.length; currentIndex++) {
                    ultimoObjetoReturned = nodos[currentIndex];
                    if(ultimoObjetoReturned.getKey() != null && 
                       ultimoObjetoReturned.status == KeyValueNode.KeyValueFlags.CERRADO)
                        break;
                }
                    
                if(ultimoObjetoReturned.getKey() == null)
                    throw new NoSuchElementException("No hay mas elementos en este Enumeration.");
                
                P genericAReturnear = null;
                if(this.type == IteratorType.KEY)
                    genericAReturnear = (P) ultimoObjetoReturned.getKey();
                else if(this.type == IteratorType.VALUES)
                    genericAReturnear = (P) ultimoObjetoReturned.getValue();
                else if(this.type == IteratorType.ENTRY)
                    genericAReturnear = (P) ultimoObjetoReturned;
                nextFueLlamado = true;
                objetosDevueltos++;
                return genericAReturnear;
            } catch(IndexOutOfBoundsException ex) {
                //Segun contrato de java.util.Iterator
                throw new NoSuchElementException("No hay mas elementos en este Enumeration.");
            }  
        }

        @Override
        public void remove() {
            //NO DEBE FALLAR, A MENOS QUE HAYA UN CAMBIO ESTRUCTURAL ANTES DE APLICAR ESTE REMOVE!
            //DEBE SER FAIL-FAST!!
            this.checkConcurrentModEx();
            
            //Segun contrato java.util.Iterator
            if(! nextFueLlamado)
                throw new IllegalStateException("Antes de llamar remove() se debe llamar a next().");
            
//            if(type == IteratorType.KEY)
                Hashtable.this.remove(ultimoObjetoReturned.getKey());
//            else if(type == IteratorType.ENTRY)
//                Hashtable.this.remove(((Entry<K,V>)ultimoObjetoReturned).getKey());
//            else if(type == IteratorType.VALUES) {
//                Hashtable.this.re
//            }
            
            nextFueLlamado = false;
            ultimoObjetoReturned = null;
            this.hashAlCrearIterator = ultimoHashcodeCalculado;
        }
    }
    
    
    
    
    private class Enumeration<P> implements java.util.Enumeration<P> {
        
        private ArrayList<P> items;
        private int currentIndex;
        
        Enumeration(Collection<P> items) {
            this.items = new ArrayList<>(items);
            this.currentIndex = 0;
        }

        @Override
        public boolean hasMoreElements() {
            if(items.size() <= currentIndex)
                return false;
            return true;
        }

        @Override
        public P nextElement() {
            try {
                return items.get(currentIndex++);
            } catch(IndexOutOfBoundsException ex) {
                //Segun contrato de java.util.Enumeration.nextElement()
                throw new NoSuchElementException("No hay mas elementos en esta Enumeration.");
            }            
        }
    }
}
