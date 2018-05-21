/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.Scanner;

/**
 *
 * @author examen
 */
public class Test {
    
    private static BigInteger bigMio1, bigMio2, bigMio3;        
    private static java.math.BigInteger bigJava1, bigJava2, bigJava3;
    private static String bigString1 = "5278";
    private static String bigString2 = "-8729";
    private static String bigString3 = "6000000"; //6 millones
            
    
    public static void main (String[] args)
    {
        System.out.println("numero big 1: " + bigString1);
        System.out.println("numero big 2: " + bigString2);
        System.out.println("numero big 3: " + bigString3 + "\n");
        bigMio1 = new BigInteger(bigString1);
        bigMio2 = new BigInteger(bigString2);
        bigMio3 = new BigInteger(bigString3);
        bigJava1 = new java.math.BigInteger(bigString1);
        bigJava2 = new java.math.BigInteger(bigString2);
        bigJava3 = new java.math.BigInteger(bigString3);
        
        System.out.println("Suma " + bigString1 + " y " + bigString2 + " mia: " + bigMio1.add(bigMio2));
        Scanner scan = new Scanner(System.in);
        String numeroCadena = scan.next();
        numero1 = new BigInteger(numeroCadena);
        
        System.out.print("Ingrese el segundo numero: ");
        numeroCadena = scan.next();
        numero2 = new BigInteger(numeroCadena);
        int op;
        
        do
    	{
            BigInteger numero3;
    	    System.out.println ("\nLos numeros ingresados son " + numero1 + " y " + numero2);
    	    System.out.println ("****ELIJA UNA OPCION****");
    	    System.out.println ("1. Sumarlos (resta si son de distinto signo)");
    	    System.out.println ("2. Probar si son iguales");
            System.out.println ("3. Salir");
    	    System.out.print ("Ingrese opcion: ");
    	    op = scan.nextInt();
    	    switch (op)
    	    {
                case 1:
                    numero3 = numero1.add(numero2);
                    System.out.println("***SUMA: " + numero1 + " + " + numero2 + " = " + numero3);
                    break;
                case 2:
                    if(numero1.equals(numero2))
                        System.out.println("***LOS NUMEROS " + numero1 + " y " + numero2 + " son iguales.");
                    else
                        System.out.println("***LOS NUMEROS " + numero1 + " y " + numero2 + " son distintos.");
                    break;
                case 3: ;
                default: ;          
            }
        }while(op != 3);
    }
}
