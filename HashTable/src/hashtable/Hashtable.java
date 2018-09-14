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
public class Hashtable<K,V> extends Dictionary<K,V>
                            implements java.util.Map<K,V>, 
                                       Serializable {
    
    private KeyValueNode<K,V>[] nodos;
    private float porcentajeOcupacionMaximo;
     
    
    public Hashtable() {
        this(31);
    }
    
    public Hashtable(int initialCapacity) {
            this(initialCapacity, 0.5f);
    }
    
    public Hashtable(int initialCapacity, float loadFactor) {
        this.porcentajeOcupacionMaximo = loadFactor;  
        
//        APUNTE
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
    }
    
    @Override
    public V get(Object key) {
        //funcion de dispersion/transformacion
        int index = key.hashCode() % nodos.length;
        
        //Podria haber colision (el index es igual) cuando dos hashCode() son similares.
        //Usamos DIRECCIONAMIENTO ABIERTO.
        
        Para buscar un objeto, se obtiene su dirección madre con la función h(), y se entra en la tabla en esa dirección.
        Si el objeto en esa casilla no es el buscado, se explora hacia abajo hasta encontrarlo (búsqueda exitosa), 
        o hasta encontrar una casilla abierta (búsqueda infructuosa). Y de esta forma, la búsqueda puede plantearse así: 
        obtenga la dirección madre del objeto a buscar. Entre en esa casilla. Si no contiene al objeto, 
        realice una exploración cuadratica hacia abajo y en forma circular si fuera necesario, 
        hasta dar con el objeto o enctontrar una casilla abierta (no una casilla cerrada o una tumba).
        
        En general la técnica de resolución de colisiones por direccionamiento abierto permite encontrar
        un objeto en tiempo casi constante: puede verse que se requiere un acceso directo a la dirección madre, 
        y una corta exploración en el peor caso para hallar el objeto
        
              
        ojo que tenemos que preguntar por el status del node
        while( nodos[index] != null &&
               ! nodos[index].key.equals(key) ) {
            index++;
            
            //Para evitar ArrayIndexOutOfBoundsException
            if(index >= nodos.length)
                index -= nodos.length; no deberia ser index = 0??
            
            //Le dimos la vuelta entera al array y no encontramos coincidencia de key.
            //Segun java.util.Map debemos devolver null.
            if(index == key.hashCode() % nodos.length)
                return null;
        }
          
        TIRARA NPE SI NO HAY OBJETO INICIALIZADO EN ESE INDEX???
        return nodos[index].value;
    }

    @Override
    public V put(K key, V value) {
//      Apunte
//        Al principio, cada casilla está vacía o abierta (de allí el nombre de esta técnica). Si un objeto 
//        O1 pide entrar en una casilla i y la misma está abierta, O1 se almacena en ella.
//        A partir de este momento, la casilla i está cerrada.
//                 
//        Si otro objeto O2 pide entrar en la tabla y colisiona en la casilla i, se prueba en la casilla i+1.
//        Si está abierta, O2 se almacena en ella. Pero si está cerrada, se sigue probando con i+2, i+3, i+4...
//        (haciendo una exploración lineal) hasta llegar a una casilla abierta y en ella se almacena el nuevo objeto.
//                
//        Puede preverse que a medida que la tabla se llene, las exploraciones lineales serán cada vez más largas.
//        Esto sugiere que la tabla debería empezar con un tamaño mayor al número de objetos esperados, y controlar 
//        el porcentaje de ocupación. Si el mismo llega a cierto valor crítico (entre 50 y 70 por ciento en muchas 
//        implementaciones), se debería redimensionar la tabla

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
//        (en lugar de exploración lineal): Si la casilla i está ocupada, se sigue con i + 1 y luego con i + 4, i + 9, ... i + j2  
//        (con j = 1, 2, 3, ...) Está claro que el agupamiento primario se rompe con este tipo de exploración, pero ahora se produce 
//        el agrupamiento secundario: dada una dirección madre i, siempre se exploran las mismas casillas de allí en adelante. 
//        Esto lleva a otro posible problema: la exploración cuadrática podría no garantizar que una clave se inserte finalmente en
//        la tabla, aún habiendo lugar libre.
//        No obstante, se puede demostrar que si el tamaño de la tabla es un número primo y el porcentaje de 
//        ocupación no es mayor al 50% de la tabla, entonces la exploración cuadrática garantiza que la clave será insertada.
//        Como ayuda, va el siguiente método que facilita encontrar el siguiente primo mayor que un número n dado: 
// 
//        private static final int siguientePrimo ( int  n ) { 
//            if ( n % 2  == 0)   
//                n++; 
//            for ( ; !esPrimo(n); n+=2 ) ;  
//            return n; 
//        }
//        el método  boolean esPrimo(int n)  debe verificar los divisores impares 
//        posibles de n (el algoritmo mostrado garantiza que luego del if, n no es par) entre 3 y raiz 
//        de n. Si ningún número en ese intervalo divide a n, entonces n es primo.
//        
//        Se debería monitorear la carga de la tabla  y en caso de llegar al punto de intervención, aumentar
//        el tamaño de la tabla (por ejemplo, en un 50% o hasta el número primo siguiente a ese 50%). 
//        El proceso es: se crea la nueva tabla, se toman todos los registros que estaban en la anterior, y
//        se redispersan en la nueva tabla: no se puede sólo copiar los registros de una a otra, pues los 
//        tamaños de ambas no coinciden y los valores calculados por h() no serán consistentes. Al terminar, la vieja tabla se elimina.

//      Algoritmo
//      1- Preguntar si la key o el value son null. Si alguno de los dos es null, tirar NPE.
//         Asi lo especifica java.util.Map.put() y el contrato propio de java.util.Hashtable.put().
//      2- Preguntar si dicha key ya se encuentra mapeada a un valor en nodes. Esto debe llevarse a cabo
//         usando containsKey(key)
//      3- Si la key ya estaba mapeado a otro valor, el valor antiguo se debe almacenar en una variable
//         ya que esto es lo que pondremos en el return. La key se mapeara ahora con el nuevo valor.
//         Si la key no se encontraba en el arreglo, el return sera null. Saltar al paso 8.
//      4- 
//      5- Se calcula index = key.hashCode() % nodes.length. Si nodes[index] esta ABIERTA o TUMBA, se inserta ahi y
//         continue en paso 6.
//      6- Si nodes[index] esta CERRADA, 
//      7- Checkear porcentaje de llenado de nodos. Si es igual o supera porcentajeOcupacionMaximo, entonces se debe hacer
//         rehash().
//      7- valorAntiguo != null, entonces return valorAntiguo. 
        

        
        
    }

    private boolean esPrimo(int n) {
        
    }

    @Override
    public V remove(K key) {
         En algún momento se querrá eliminar un objeto de la tabla.
         En principio, usamos marcado lógico para eliminar el objeto. Para ello, se debe buscar el objeto con el procedimiento anterior 
         (exploracion cuadratica) y una vez hallado marcar la casilla como vacía o abierta. Pero eso invalidaría el procedimiento de búsqueda: 
         si este debe terminar al encontrar una casilla abierta, la búsqueda podría darse por infructuosa cuando quizás el 
         objeto esté más abajo en la tabla. De modo que al borrar un objeto lo que se hace es dejar su casilla como TUMBA.
         
        
        
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
