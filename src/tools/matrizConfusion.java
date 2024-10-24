package tools;

public class matrizConfusion {
    
    public double[][] calculate(double[][] matrizO, int fila, char n){
        double[][] a = new double[fila][];

        for (int i = 0; i < a.length; i++) {
            a[i] = new double[i+1];
            if(n == 'a') for (int j = 0; j < a[i].length; j++) a[i][j] = search_a(i, j, matrizO);
            else if(n == 'b') for (int j = 0; j < a[i].length; j++) a[i][j] = search_b(i, j, matrizO);
            else if(n == 'c') for (int j = 0; j < a[i].length; j++) a[i][j] = search_c(i, j, matrizO);
            else if(n == 'd') for (int j = 0; j < a[i].length; j++) a[i][j] = search_d(i, j, matrizO);
            
        }

    return a;}

    public int search_a(int i, int j, double[][] matriz){
        int cont = 0;
        if(i == j) for (int k = 0; k < matriz[i].length; k++) if(matriz[i][k] == 1) cont++;
        if(i > j) for (int k = 0; k < matriz[i].length; k++)  if(matriz[j][k] == 1 && matriz[i][k] == 1) cont++;
    return cont;}

    public int search_b(int i, int j, double[][] matriz){
        int cont = 0;
        if(i > j) for (int k = 0; k < matriz[i].length; k++)  if(matriz[j][k] == 1 && matriz[i][k] == 0) cont++;
    return cont;}

    public int search_c(int i, int j, double[][] matriz){
        int cont = 0;
        if(i > j) for (int k = 0; k < matriz[i].length; k++)  if(matriz[j][k] == 0 && matriz[i][k] == 1) cont++;
    return cont;}

    public int search_d(int i, int j, double[][] matriz){
        int cont = 0;
        if(i == j) for (int k = 0; k < matriz[i].length; k++) if(matriz[i][k] == 0) cont++;
        if(i > j) for (int k = 0; k < matriz[i].length; k++)  if(matriz[j][k] == 0 && matriz[i][k] == 0) cont++;
    return cont;}

}
