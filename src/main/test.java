package main;

public class test {
    public static void main(String[] args) {
        String path = "C:\\Users\\guero.LAP1059\\Desktop\\UAEMex\\AlgoritmosDeAgrupamiento\\matriz\\Algoritmo_De_Agrupamiento\\";

        //path += "matriz_prueba2.csv";
        //path += "matriz_prueba2.xls";
        //path += "matrix_Big.csv";
        path += "hala.csv";
        
        program pr = new program();
        pr.start(path);
    }
}
