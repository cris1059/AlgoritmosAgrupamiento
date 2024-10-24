package obj;

import java.util.Arrays;
import java.text.DecimalFormat;

public class matriz {

    public double [][] matriz;

    public matriz(double[][] matriz){
        this.matriz = matriz;
    }

    public int ObtenIntFilas(){
    return matriz.length;}

    public int obtenIntColumnas(){
    return matriz[0].length;}


    public void pirntMatriz(){
        for (int i = 0; i < matriz.length; i++) System.out.println(Arrays.toString(matriz[i]));
    }


    public void transforString(){
        String [][] t = new String[matriz.length][];
        for (int i = 0; i < t.length; i++) {
            t[i] = new String[i+1];
            for (int j = 0; j < t[i].length; j++) {
                t[i][j] = new DecimalFormat("#.00").format(matriz[i][j]);
            }
        }
        for (int i = 0; i < t.length; i++) System.out.println(Arrays.toString(t[i]));
    }

    public void setMatriz(double[][] matriz){
        matriz = this.matriz;
    }

    public void matrizNxN(){
        double[][] nueva = new double[matriz.length][matriz.length];

        for (int i = 0; i < nueva.length; i++) for (int j = 0; j < nueva.length; j++) nueva[i][j] = 999.999;
        
        for (int i = 0; i < matriz.length; i++) for (int j = 0; j < matriz[i].length; j++) nueva[i][j] = matriz[i][j];

        matriz = nueva;
    } 
    
}
