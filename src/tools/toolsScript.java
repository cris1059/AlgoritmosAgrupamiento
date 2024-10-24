package tools;
import java.io.*;
import java.util.*;

public class toolsScript {
    

    public boolean Existe_enArreglo(int[] array, int value) {
        for (int i : array) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }
    
    public int contador_filas_datos(BufferedReader br) throws IOException{
        int cont = 0;
        while (br.ready()) {
            br.readLine();
            cont++;
        }
        br.close();
    return cont;}


    public double[] normalizar_double(String [] arreglo){
        double [] arregloD = new double[arreglo.length];
        for (int i = 0; i < arreglo.length; i++) arregloD[i] = Double.parseDouble(arreglo[i]);
        
    return arregloD;}


    public int[] eliminarDuplicados(int[] arreglo) {
        if (arreglo.length == 0) {
            return new int[0];
        }
        int[] temporal = new int[arreglo.length];
        int contador = 0; 

        for (int i = 0; i < arreglo.length; i++) {
            boolean esDuplicado = false;
            for (int j = 0; j < contador; j++) {
                if (arreglo[i] == temporal[j]) {
                    esDuplicado = true; 
                    break;
                }
            }
            if (!esDuplicado) {
                temporal[contador] = arreglo[i];
                contador++;
            }
        }
        int[] resultado = new int[contador];
        for (int i = 0; i < contador; i++) {
            resultado[i] = temporal[i];
        }

        return resultado;
    }

    public double[][] transponerMatriz(double[][] matriz) {
        int filas = matriz.length;
        int columnas = matriz[0].length;
        double[][] transpuesta = new double[columnas][filas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                transpuesta[j][i] = matriz[i][j];
            }
        }

        return transpuesta;
    }


    public double[][] multiplicarMatrices(double[][] matrizA, double[][] matrizB) {
        int filasA = matrizA.length;
        int columnasA = matrizA[0].length;
        int filasB = matrizB.length;
        int columnasB = matrizB[0].length;
    
        if (columnasA != filasB) {
            throw new IllegalArgumentException("El número de columnas de la primera matriz debe ser igual al número de filas de la segunda matriz.");
        }
    
        double[][] resultado = new double[filasA][columnasB];
    
        for (int i = 0; i < filasA; i++) {
            for (int j = 0; j < columnasB; j++) {
                for (int k = 0; k < columnasA; k++) {
                    resultado[i][j] += matrizA[i][k] * matrizB[k][j];
                }
            }
        }
        return resultado;
    }

    public double[][] inverseMatriz(double[][] Matriz) {
        int n = Matriz.length;
        double[][] augmentedMatriz = new double[n][2 * n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmentedMatriz[i][j] = Matriz[i][j];
            }
            augmentedMatriz[i][i + n] = 1; 
        }

        for (int i = 0; i < n; i++) {
            double diagElement = augmentedMatriz[i][i];
            for (int j = 0; j < 2 * n; j++) {
                augmentedMatriz[i][j] /= diagElement;
            }

            for (int j = 0; j < n; j++) {
                if (j != i) {
                    double factor = augmentedMatriz[j][i];
                    for (int k = 0; k < 2 * n; k++) {
                        augmentedMatriz[j][k] -= factor * augmentedMatriz[i][k];
                    }
                }
            }
        }
        
        double[][] inverseMatriz = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseMatriz[i][j] = augmentedMatriz[i][j + n];
            }
        }
        return inverseMatriz;
    }

    public boolean esBianriaLaMatriz(double[][] Matriz) {
        int totalElements = 0;
        int binaryElements = 0;

        for (double[] row : Matriz) {
            for (double value : row) {
                totalElements++;
                if (value == 0 || value == 1) {
                    binaryElements++; 
                }
            }
        }

        return (binaryElements >= totalElements * 0.5);
    }

    public boolean analizadorData(double[][] matrix){
        int n = matrix[0].length;
        for (int i = 1; i < matrix.length; i++) if(n != matrix[i].length) return true;
            
    return false;}

}
