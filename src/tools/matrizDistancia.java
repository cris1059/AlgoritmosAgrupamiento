package tools;

import obj.matriz;

public class matrizDistancia {
    
    
    public matriz[] calcularMatrizDeDistancias(double[][] a, double[][] b, double[][] c, double[][] d){
        matriz sokal = new matriz(new double[a.length][]), jaccard = new matriz(new double[a.length][]);
        for (int i = 0; i < a.length; i++) {
            sokal.matriz[i] = new double[i+1];
            jaccard.matriz[i] = new double[i+1];
            for (int j = 0; j < a[i].length; j++) {
                double aa = a[i][j], bb = b[i][j], cc = c[i][j], dd = d[i][j];
                sokal.matriz[i][j] = sokal(aa, bb, cc, dd);
                jaccard.matriz[i][j] = jaccard(aa, bb, cc, dd);
            }
        }
        matriz[] m = new matriz[2];
        m[0] = sokal;
        m[1] = jaccard;
    return m;}



    public double sokal(double a, double b, double c, double d){
        return (a+d)/(a+b+c+d);}


    public double jaccard(double a, double b, double c, double d){
        return (a)/(a+b+c);}




public matriz calcularMatrizMahalanobis(double[][] matriz){

double[] promedios = promedios(matriz);
double [][] distanciaCentroide = new double[matriz.length][matriz[0].length];

for (int i = 0; i < distanciaCentroide.length; i++) for (int j = 0; j < distanciaCentroide[i].length; j++) distanciaCentroide[i][j] = matriz[i][j] - promedios[j];
    

double[][] distanciaCentroideT = new toolsScript().transponerMatriz(distanciaCentroide);
int n = distanciaCentroide.length - 1;
double[][] MultdistancaCentroide = new toolsScript().multiplicarMatrices(distanciaCentroide, distanciaCentroideT);
double[][] matrizCovarianza = calculateCovarianceMatriz(distanciaCentroide);
double[][]  matrizCovarianzaInv = new toolsScript().inverseMatriz(matrizCovarianza);
double[][] matrizAntecedente = new toolsScript().multiplicarMatrices(distanciaCentroide, matrizCovarianzaInv);

double[][] matrizDistancia = new toolsScript().multiplicarMatrices(matrizAntecedente, distanciaCentroideT);

matriz matrizR = new matriz(matrizDistancia);
return matrizR;}

public static double[][] calculateCovarianceMatriz(double[][] centeredData) {
    int n = centeredData.length; 
    int k = centeredData[0].length; 
    
    double[][] covarianceMatriz = new double[k][k];

    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            double covariance = 0;
            for (int row = 0; row < n; row++) {
                covariance += centeredData[row][i] * centeredData[row][j];
            }
            covarianceMatriz[i][j] = covariance / (n - 1);
        }
    }
    return covarianceMatriz;
}


public double[] promedios(double[][] matriz){
    double [] promedios = new double[matriz[0].length];
    for (int i = 0; i < matriz[i].length; i++) {
        double total = 0;
        for (int j = 0; j < matriz.length; j++) {
            total += matriz[j][i];
        } 
        promedios[i] = total / matriz.length;
    }

return promedios;}

}