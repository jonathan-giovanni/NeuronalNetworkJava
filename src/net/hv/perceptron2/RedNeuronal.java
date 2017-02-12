/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hv.perceptron2;

import java.util.Random;
import java.text.DecimalFormat;

/**
 *
 * @author Jonathan
 */
public class RedNeuronal {
    private int INPUT_NEURONS;
    private int HIDDEN_NEURONS;
    private int OUTPUT_NEURONS;

    private double LEARN_RATE = 0.15;    //Factor de aprendizaje Rho.
    private int TRAINING_REPS = 60000;

    // Input to Hidden Weights (with Biases).
    // Entradas para los pesos de las capas ocultas
    private double wih[][];

    // Hidden to Output Weights (with Biases).
    //salidas de los pesos en las capas ocultas
    private double who[][];
    
    private char valores[];

    // Activations.
    private double inputs[];
    private double hidden[];
    private double target[];
    private double actual[];

    // Unit errors.
    private double erro[];
    private double errh[];

    private final int MAX_SAMPLES;

    private double trainInputs[][];

    private double trainOutput[][];

    public RedNeuronal(int neuronasOcultas,double [][]entradas,double aprender[][],char _valores[])
    {
        
        HIDDEN_NEURONS = neuronasOcultas;
        trainInputs = entradas;
        INPUT_NEURONS = trainInputs[0].length;
        trainOutput = aprender;
        OUTPUT_NEURONS = trainOutput.length;
        MAX_SAMPLES = OUTPUT_NEURONS;
        valores = _valores;
        wih = new double[INPUT_NEURONS + 1][HIDDEN_NEURONS];
        who = new double[HIDDEN_NEURONS + 1][OUTPUT_NEURONS];
        inputs = new double[INPUT_NEURONS];
        hidden = new double[HIDDEN_NEURONS];
        target = new double[OUTPUT_NEURONS];
        actual = new double[OUTPUT_NEURONS];
        erro = new double[OUTPUT_NEURONS];
        errh = new double[HIDDEN_NEURONS];
        int sample = 0;

        assignRandomWeights();

        // Train the network.
        for(int epoch = 0; epoch < TRAINING_REPS; epoch++)
        {
            sample += 1;
            if(sample == MAX_SAMPLES){
                sample = 0;
            }

            for(int i = 0; i < INPUT_NEURONS; i++)
            {
                inputs[i] = trainInputs[sample][i];
            } // i

            for(int i = 0; i < OUTPUT_NEURONS; i++)
            {
                target[i] = trainOutput[sample][i];
            } // i

            feedForward();

            backPropagate();

        } // epoch

        getTrainingStats();
		
        System.out.println("\nRed neuronal testeada - entrada original :");
        testNetworkTraining();
		
        return;
    }

    private void getTrainingStats()
    {
        double sum = 0.0;
        for(int i = 0; i < MAX_SAMPLES; i++)
        {
            for(int j = 0; j < INPUT_NEURONS; j++)
            {
                inputs[j] = trainInputs[i][j];
            } // j

            for(int j = 0; j < OUTPUT_NEURONS; j++)
            {
                target[j] = trainOutput[i][j];
            } // j

            feedForward();

            if(maximum(actual) == maximum(target)){
                sum += 1;
            }else{
                System.out.println((int)inputs[0] + "\t" + (int)inputs[1] + "\t" + (int)inputs[2] + "\t" + (int)inputs[3]);
              System.out.println(maximum(actual) + "\t" + maximum(target));
            }
        } // i

        System.out.println("Network is " + ((double)sum / (double)MAX_SAMPLES * 100.0) + "% correct.");

        return;
    }

    private void testNetworkTraining()
    {
        // This function simply tests the training vectors against network.
        // Esta es una funcion simple testea el entrenamiento de los vectores en la red
        for(int i = 0; i < MAX_SAMPLES; i++)
        {
            for(int j = 0; j < INPUT_NEURONS; j++)
            {
                inputs[j] = trainInputs[i][j];
            } // j
            
            feedForward();
            
            for(int j = 0; j < INPUT_NEURONS; j++)
            {
                System.out.print(inputs[j] + "\t");
            } // j
            int pos = maximum(actual);
            System.out.print("Salida: " + pos + " Letra " + valores[pos] +  " \n");
        } // i
        
        return;
    }
    
    
    

    public String verificar(double []variables)
    {
        // This function adds a random fractional value to all the training
        // inputs greater than zero.
        // Esta funcio agrega decimales aleatorios a todo el entrenamiento
        // las entradas son mayor a cero
        inputs=variables;
        for(int i = 0; i < MAX_SAMPLES*3; i++)
        {
            feedForward();
        } // i
        int pos = maximum(actual);
        if(pos<0){
            System.out.print("No se tiene una certeza sobre lo identificado \n");
            return "indeterminado";
        }else{
            System.out.print("Output: " + pos + " Letra " + valores[pos] +  " \n");
            return ""+valores[pos];
        }
        
    }

    private int maximum(final double[] vector)
    {
        // This function returns the index of the maximum of vector().
        // Esta funcion retorna el indice del valor maximo del vector().
        int sel = 0;
        double max = vector[sel];
        for(int index = 0; index < OUTPUT_NEURONS; index++)
        {
            if(vector[index] > max){
                max = vector[index];
                sel = index;
            }
        }
        
        System.out.print("  "+ String.format( "%.2f", max*100) + "% Que sea correcto");
        if(max<0.25){
            return -1;
        }
        return sel;
    }

    private void feedForward()
    {
        double sum = 0.0;

        
        // Calculate input to hidden layer.
        // Calcula las entradas para las capas ocultas.
        for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
        {
            sum = 0.0;
            for(int inp = 0; inp < INPUT_NEURONS; inp++)
            {
                sum += inputs[inp] * wih[inp][hid];
            } // inp

            sum += wih[INPUT_NEURONS][hid]; // Add in bias.
            hidden[hid] = sigmoid(sum);
        } // hid

        // Calculate the hidden to output layer.
        // Calcular la salida de las capas ocultas.
        for(int out = 0; out < OUTPUT_NEURONS; out++)
        {
            sum = 0.0;
            for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
            {
                sum += hidden[hid] * who[hid][out];
            } // hid

            sum += who[HIDDEN_NEURONS][out]; //Sumar en bias - Add in bias.
            actual[out] = sigmoid(sum);
        } // out - salida
        return;
    }

    private void backPropagate()
    {
        // Calculate the output layer error (step 3 for output cell).
        // Calcula el error en la capa de salida
        for(int out = 0; out < OUTPUT_NEURONS; out++)
        {
            erro[out] = (target[out] - actual[out]) * sigmoidDerivative(actual[out]);
        }

        // Calculate the hidden layer error (step 3 for hidden cell).
        // Calcula el error en la capa de salida
        for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
        {
            errh[hid] = 0.0;
            for(int out = 0; out < OUTPUT_NEURONS; out++)
            {
                errh[hid] += erro[out] * who[hid][out];
            }
            errh[hid] *= sigmoidDerivative(hidden[hid]);
        }

        // Update the weights for the output layer (step 4).
        // Actualiza los pesos para la capa de salida
        for(int out = 0; out < OUTPUT_NEURONS; out++)
        {
            for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
            {
                who[hid][out] += (LEARN_RATE * erro[out] * hidden[hid]);
            } // hid
            who[HIDDEN_NEURONS][out] += (LEARN_RATE * erro[out]); // Update the bias.
        } // out

        // Update the weights for the hidden layer (step 4).
        // Actualiza los pesos para la capa oculta
        for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
        {
            for(int inp = 0; inp < INPUT_NEURONS; inp++)
            {
                wih[inp][hid] += (LEARN_RATE * errh[hid] * inputs[inp]);
            } // inp
            wih[INPUT_NEURONS][hid] += (LEARN_RATE * errh[hid]); // Update the bias.
        } // hid
        return;
    }

    private void assignRandomWeights()
    {
        for(int inp = 0; inp <= INPUT_NEURONS; inp++) // Do not subtract 1 here.
        {
            for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
            {
                // Assign a random weight value between -0.5 and 0.5
                wih[inp][hid] = new Random().nextDouble() - 0.5;
            } // hid
        } // inp

        for(int hid = 0; hid <= HIDDEN_NEURONS; hid++) // Do not subtract 1 here.
        {
            for(int out = 0; out < OUTPUT_NEURONS; out++)
            {
                // Assign a random weight value between -0.5 and 0.5
                who[hid][out] = new Random().nextDouble() - 0.5;
            } // out
        } // hid
        return;
    }

    private static double sigmoid(final double val)
    {
        return (1.0 / (1.0 + Math.exp(-val)));
    }
    private static double sigmoidDerivative(final double val)
    {
        return (val * (1.0 - val));
    }
}
