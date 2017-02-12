/*
 * Implementacion del perceptron multicapa con retropropagación
 * CapaI : valores que aprendera la red
 * Aprender : valores binarios que seran su evaluacion de aprendizaje
 * Letras : Salida de lo valores binarios
 * Verificar : prueba la red
 * nodosJ : los nodos de la primera capa
 * entradasK : las entradas de la segunda capa
 */
package perceptron;

import net.hv.perceptron2.RedNeuronal;
/**
 *
 * @author Jonathan Geovany Hernandez Vasquez
 */
public class Perceptron {
    static double capaI[][]={
        {
        // A
        1,1,1,1,
        1,0,0,1,
        1,1,1,1,
        1,0,0,1,
        1,0,0,1
        },{
        // B
        1,1,1,1,
        1,0,0,1,
        1,1,1,0,
        1,0,0,1,
        1,1,1,1
        },{
        // C
        1,1,1,1,
        1,0,0,0,
        1,0,0,0,
        1,0,0,0,
        1,1,1,1
        },{
        // D
        1,1,1,0,
        1,0,0,1,
        1,0,0,1,
        1,0,0,1,
        1,1,1,0
        },{
        // E
        1,1,1,1,
        1,0,0,0,
        1,1,1,0,
        1,0,0,0,
        1,1,1,1
        },{
        // F
        1,1,1,1,
        1,0,0,0,
        1,1,1,0,
        1,0,0,0,
        1,0,0,0
        },{
        // G
        1,1,1,1,
        1,0,0,0,
        1,1,1,1,
        1,0,0,1,
        1,1,1,1
        },{
        // H
        1,0,0,1,
        1,0,0,1,
        1,1,1,1,
        1,0,0,1,
        1,0,0,1
        }
    };
    static double aprender[][]=
    {
        {1,0,0,0,0,0,0,0},
        {0,1,0,0,0,0,0,0},
        {0,0,1,0,0,0,0,0},
        {0,0,0,1,0,0,0,0},
        {0,0,0,0,1,0,0,0},
        {0,0,0,0,0,1,0,0},
        {0,0,0,0,0,0,1,0},
        {0,0,0,0,0,0,0,1}
            
    };
    static char letras[] = {'A','B','C','D','E','F','G','H'};
    static double verificar[] = {
        // Demostracion de una letra con ruido
        1,0,0,0,
        1,0,0,0,
        1,0,0,0,
        1,0,0,0,
        1,1,0,1
        };
    static int nodosJ = 5;
    static int entradasK = 10;
    static RedNeuronal red;
    public static void main(String[] args) {
        // Aplicacion de la red
        //app = new Aplicacion(nodosJ,entradasK,capaI, aprender,letras);
        //ejemplo();
        
        red = new RedNeuronal(6,capaI,aprender,letras);
        //ejemplo();
        //dialogoPrincipal dialogo = new dialogoPrincipal(new JFrame(), true);
        //dialogo.red = red;
        //dialogo.app = app;
        //dialogo.setVisible(true);
        System.exit(0);
        
    }
    public static  void ejemplo(){
        System.out.println("\nVerificando");
        red.verificar(verificar);
    }
    
}
