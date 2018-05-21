
package modelo;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Patricio Carranza, UTN FRC, 2014
 */
public class BigInteger extends java.lang.Number
{
    private ArrayList<Integer> numeros;
    private int signum;
    
    public BigInteger() {
        numeros = new ArrayList<>();
    }   
    
    public BigInteger(String val) throws ArithmeticException, NumberFormatException {
        int tamaño = val.length();
        numeros = new ArrayList<>(tamaño);
        boolean flagNegativo = false;
        
        for(int i = 0; i < tamaño; i++) {
            Character ch = val.charAt(i);
            if(i == 0 && ch.toString().equalsIgnoreCase("-")) {
               flagNegativo = true;
               continue;
            }
            int num = Integer.parseInt(ch.toString());

            //Vino un signo menos en un digito que no corresponde
            if(i != 0 && checkSignum(num) == -1)
                throw new ArithmeticException();
            
            numeros.add(num);
        }
        
        if(flagNegativo)
            changeSignum();
    }
    
    //Si bien no se demuestra el funcionamiento de este
    //constructor en la clase Test, se utiliza como finalizacion
    //de los metodos de suma y resta, de esta forma comprobando
    //que funciona
    public BigInteger(int[] val) throws ArithmeticException {
        numeros = new ArrayList<>(val.length);
        for(int i = 0; i < val.length; i++) {
            int num = val[i];
            //Vino un signo menos en un digito que no corresponde
            if(i != 0 && checkSignum(num) == -1)
                throw new ArithmeticException();
            
            numeros.add(num);
        }
    }  
    
    public int signum() {
        return this.signum;
    }   
       
    public BigInteger add(BigInteger val) {
        if(this.signum() == 0)
            return val;
        if(val.signum() == 0)
            return this;
        if(val.signum() == 1 && this.signum() == 1)
            return this.sumaPositiva(val);
        if(val.signum() == -1 && this.signum() == -1)
            return this.sumaNegativa(val);        
        else
            return this.resta(val);       
    }
    
    public int cantidadDigitos() {
        return numeros.size();
    }
    
    private int getDigitoAt(int index) {
        return numeros.get(index);
    }
    
    private void changeSignum() {
        numeros.set(0, numeros.get(0) * (-1));
        mmmm
    }
    
    private BigInteger sumaNegativa(BigInteger val) {
        val.changeSignum();
        this.changeSignum();
        BigInteger suma = this.sumaPositiva(val);
        suma.changeSignum();
        val.changeSignum();
        this.changeSignum();
        return suma;
    }
    
    private BigInteger sumaPositiva(BigInteger val) {
        int arregloMasGrande = this.cantidadDigitos();
        if(val.cantidadDigitos() > arregloMasGrande)
            arregloMasGrande = val.cantidadDigitos();
        
        int[] sumaTotal = new int[arregloMasGrande];
        int carry = 0;          
        
        for(int i = 0; i < arregloMasGrande; i++)
        {
            int sumaDeDosDigitos = 0 + carry;
            try
            {   sumaDeDosDigitos += val.getDigitoAt(val.cantidadDigitos() - i - 1); }
            catch(ArrayIndexOutOfBoundsException ex) {}
            
            try
            {   sumaDeDosDigitos += this.getDigitoAt(this.cantidadDigitos() - i - 1);   }
            catch(ArrayIndexOutOfBoundsException ex) {}
            
            if(sumaDeDosDigitos >= 10) {
                sumaDeDosDigitos -= 10;
                carry = 1;
            }
            else
                carry = 0;
            sumaTotal[arregloMasGrande - i - 1] = sumaDeDosDigitos;
        }
        
        if(carry == 1) {
            int[] masUno = new int[arregloMasGrande + 1];
            masUno[0] = carry;
            System.arraycopy(sumaTotal, 0, masUno, 1, arregloMasGrande);
            return new BigInteger(masUno);
        }
        return new BigInteger(sumaTotal);
    }
    
    private BigInteger resta(BigInteger val) {        
        val.changeSignum();
        int arregloMasGrande = this.cantidadDigitos();
        if(val.cantidadDigitos() > arregloMasGrande)
            arregloMasGrande = val.cantidadDigitos();
        
        int[] restaTotal = new int[arregloMasGrande];  
        int carry = 0;        
        
        for(int i = 0; i < arregloMasGrande; i++) {
            int restaDeDosDigitos = 0 + carry;  
            //this representa al numero que es restado
            try
            {   restaDeDosDigitos += this.getDigitoAt(this.cantidadDigitos() - i - 1);   }
            catch(ArrayIndexOutOfBoundsException ex) { restaDeDosDigitos = 0; }
            
            //val representa al numero que va a restar
            try
            {   restaDeDosDigitos -= val.getDigitoAt(val.cantidadDigitos() - i - 1); }
            catch(ArrayIndexOutOfBoundsException ex) {}
            
            if(restaDeDosDigitos < 0 ) {
                restaDeDosDigitos = restaDeDosDigitos * (-1);
                carry = -1;
            }
            else
                carry = 0;
            restaTotal[arregloMasGrande - i - 1] = restaDeDosDigitos;
        }
        
        val.changeSignum();
        if(carry == -1) {
            BigInteger resultado = new BigInteger(restaTotal);
            resultado.changeSignum();
            return resultado;
        }
        
        return new BigInteger(restaTotal);
    }
    
    //Devuelve el valor correcto del numero, o sea positivo.
    private int checkSignum(int val) {
        if(Integer.signum(val) == -1)
            val = val * (-1);
        return val;
    }    
    
    @Override
    public boolean equals(Object x) {
        BigInteger extranjero;
        try{    extranjero = (BigInteger) x; }
        catch(ClassCastException ex) { return false;    }
        
        try {
            for(int i = 0; i < numeros.size(); i++) {
                if(extranjero.getDigitoAt(i) != this.getDigitoAt(i))
                    return false;
            }
        }
        //podria ser ArrayIndexOutOfBounds, IndexOutOfBounds, etc... nos ahorramos todo en uno solo
        catch(Exception ex) { return false; }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.numeros);
        hash = 97 * hash + this.signum;
        return hash;
        revisa esto que fue auto generado
    }
    
    public String toString() {
        StringBuffer cadena = new StringBuffer();
        for(Integer num : numeros)
        {
            cadena.append(num);
        }
        return cadena.toString();
    }

    @Override
    public int intValue() {
        falta aca
    }

    @Override
    public long longValue() {
        falta aca
    }

    @Override
    public float floatValue() {
        falta aca
    }

    @Override
    public double doubleValue() {
        falta aca
    }
}
