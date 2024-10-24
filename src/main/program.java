package main;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import obj.grupo;
import obj.matriz;
import tools.*;


public class program {

    int m = 0;

    public void start(String path){
        matriz matrizO = null;
        m = 0; // 0 Matriz normal, 1 binaria 
        if(path.contains(".csv")) {
            System.out.println("Tu archivo es formato .csv");
            matrizO = new matriz(leerArchivo_csv_txt(path));
            if(new toolsScript().analizadorData(matrizO.matriz))System.exit(0);
            if(new toolsScript().esBianriaLaMatriz(matrizO.matriz)){
                System.out.println("Tu matriz es Binaria");
                m = 1;
            }
            
        } else if(path.contains(".txt")){
            System.out.println("Tu archivo es formato .txt");
            matrizO = new matriz(leerArchivo_csv_txt(path));
            if(new toolsScript().analizadorData(matrizO.matriz))System.exit(0);
            if(new toolsScript().esBianriaLaMatriz(matrizO.matriz)){
                System.out.println("Tu matriz es Binaria");
                m = 1;
            }
            
        } else if(path.contains(".xls")){
            System.out.println("Tu archivo es formato Excel (xls | xlsx)");
            try {
                matrizO = new matriz(leerArchivo_Excel(path));
                if(new toolsScript().analizadorData(matrizO.matriz))System.exit(0);
                if(new toolsScript().esBianriaLaMatriz(matrizO.matriz)){
                    System.out.println("Tu matriz es Binaria");
                    m = 1;
                }
            } catch (IOException e) {
                System.out.println("Error al leer el archivo xlsx");
                e.printStackTrace();
            }
        }else {
            System.out.println("La extension del archivo no se admite");
            System.exit(0);
        }

        if(matrizO.ObtenIntFilas() < 10000 && matrizO.obtenIntColumnas() < 50000){
            System.out.println("Datos obtenida con en la lectura del archivo");
            matrizO.pirntMatriz(); 
            algortimo(matrizO.ObtenIntFilas(), matrizO);
        }else{
            System.out.println("Archivo no leido, supera el limite de filas x columnas permitido");
            System.exit(0);
        }
        

    }

    @SuppressWarnings("resource")
    public double[][] leerArchivo_Excel(String path) throws IOException {
        
        try (InputStream fis = new FileInputStream(path)) {
            Workbook workbook = null;

            if (path.contains(".xlsx"))  workbook = new XSSFWorkbook(fis);  
            else if (path.contains(".xls")) workbook = new HSSFWorkbook(fis); 
            else {
                System.out.println("El archivo debe ser .xls o .xlsx");
                System.exit(0);
            }
            

            Sheet sheet = workbook.getSheetAt(0);
            int n = sheet.getPhysicalNumberOfRows(); 
            if (n == 0) {
                System.out.println("El archivo está vacío");
                System.exit(0);
            }
            

            double[][] matrix = new double[n][n];
            
            for (int i = 0; i < n; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                for (int j = 0; j < n; j++) {
                    Cell cell = row.getCell(j);

                    if (cell == null) matrix[i][j] = 0; 
                    else if (cell.getCellType() == CellType.NUMERIC) matrix[i][j] = cell.getNumericCellValue(); 
                    else throw new IllegalArgumentException("Celdas no numéricas encontradas en la hoja de cálculo");
                    
                }
            }
            workbook.close();
            return matrix;
        }
    }
    
    public double [][] leerArchivo_csv_txt(String path){
        BufferedReader br;
        double [][] matriz = null;
        int cont = 0;
        try {
            matriz = new double[new toolsScript().contador_filas_datos(new BufferedReader(new FileReader(path)))][];
            br = new BufferedReader(new FileReader(path));
            while (br.ready()) {    
                String [] datos = br.readLine().replace("\\s+", "").split(",");
                matriz[cont++] = new toolsScript().normalizar_double(datos);
            }
        } catch (Exception e) {
            System.out.println("Huboo un error al leer el archivo\n Error: "+e);

        }
    return matriz;}



    @SuppressWarnings("unused")
    public void algortimo(int fila, matriz matriz){

        double[][] matrizO = matriz.matriz;
        matriz  sokal = null, jaccard = null, mahalanobis = null;
        

        System.out.println("Ingresa la opcion en numero y dale enter para calcular la matriz de distancia");
        System.out.println("1 sokal\n2 jaccard\n3 Mahalanobis");
        Scanner sc = new Scanner(System.in);
        int opcion = sc.nextInt();

        

        boolean validado = true;
        while(validado) {
            if((opcion == 1 || opcion == 2) && m == 1) validado = false;
            else if(opcion == 3 && m == 0) validado = false;
            else {
                System.out.println("Ingresa una opcion valida\nRecuerda que la matriz en binario solo se puede procesar con\n Jaccard y Sokal.");
                System.out.print("Y una matriz cuantitativa con Mahalanobis");
                opcion = sc.nextInt();
            }
            
        }
        if(opcion == 3) mahalanobis =  new matrizDistancia().calcularMatrizMahalanobis(matrizO);
        else{
            matriz a = new matriz(new matrizConfusion().calculate(matrizO, fila, 'a')), b = new matriz(new matrizConfusion().calculate(matrizO, fila, 'b'));
            matriz c = new matriz(new matrizConfusion().calculate(matrizO, fila, 'c')), d = new matriz(new matrizConfusion().calculate(matrizO, fila, 'd'));
            matriz[] sokal_jaccard = new matrizDistancia().calcularMatrizDeDistancias(a.matriz, b.matriz, c.matriz, d.matriz);
            sokal = sokal_jaccard[0];
            jaccard = sokal_jaccard[1];
        }

        if(opcion == 1){
            System.out.println("\nMatriz de distancia Sokal");
            sokal.matrizNxN();
            sokal.transforString();
        } else if(opcion == 2){
            System.out.println("\nMatriz de distancia jaccard");
            jaccard.matrizNxN();
            jaccard.transforString();
        }else{
            System.out.println("\nMatriz de distancia mahalanobis");
            mahalanobis.transforString();
        }

        System.out.println("Ingresa el Algoritmo de Agrupamiento (Eslabonamiento) deseado\n1 vmc\n2 vml\n3 centroide");
        int opcionA = sc.nextInt();
        validado = true;
        while (validado) {
            if (opcionA == 1 || opcionA == 2 || opcionA == 3) validado = false;
            else {
                System.out.println("Ingresa una opcion valida");
                opcionA = sc.nextInt();
            }
        }
        grupo[] gruposGenerados = null;
        if(opcion == 1) gruposGenerados = agrupar(sokal, opcionA);
        else if(opcion == 2) gruposGenerados = agrupar(jaccard, opcionA);
        else gruposGenerados = agrupar(mahalanobis, opcionA);
        
        if(gruposGenerados.length == 0){
            System.out.println("No se logro realizar ninguna agrupacion");
            System.exit(0);
        }
        
        int[] lineaGrupos;
        if(opcion == 1) lineaGrupos = grupoHistorial(gruposGenerados, sokal.matriz);
        else if(opcion == 2) lineaGrupos = grupoHistorial(gruposGenerados, jaccard.matriz);
        else lineaGrupos = grupoHistorial(gruposGenerados, mahalanobis.matriz);
        
        dendograma(lineaGrupos, gruposGenerados);

        
        
    }


    public grupo[] agrupar(matriz matriz, int opcion){

        double[][] originalMatriz = matriz.matriz;
        int max_grupos = originalMatriz.length-1;
        grupo[] gr = new grupo[max_grupos-1];

        double[][][] grupos_de_matrices = new double[max_grupos][][];

        int matrizCount = 0;

        double[][] matriz_recursiva = originalMatriz;

        while (matriz_recursiva.length > 2) {

            double[] datos;

            //OPCIONES VMC, VML, PROM
            if(opcion == 1) datos = encontrar_pequenio(matriz_recursiva);
            else if(opcion == 2) datos = encontrar_grande(matriz_recursiva);
            else datos = promedio(matriz_recursiva);

            int I =(int) datos[0], J = (int) datos[1];
            gr[matrizCount] = new grupo();
            gr[matrizCount].elemento = datos[2];
            double[][] newMatriz = null;

            if(opcion == 1) newMatriz = vmc(matriz_recursiva, I, J);
            else if(opcion == 2) newMatriz = vml(matriz_recursiva, I, J);
            else newMatriz = centroide(matriz_recursiva, I, J);
            
            grupos_de_matrices[matrizCount++] = newMatriz;
            matriz_recursiva = newMatriz;
        }

        matriz[] grupos = new matriz[max_grupos-1];
        for (int i = 0; i < grupos.length; i++) {
            grupos[i] = new matriz(grupos_de_matrices[i]);
            System.out.println("\n"+"Grupo "+(i+1));
            grupos[i].transforString();
        }
        //grupoHistorial(gr, matriz.matriz);

    return gr;}

    public int[] grupoHistorial(grupo[] gr, double[][] matriz){
        System.out.println("_________________________________\nHISTORIAL\n");
        int conta = 1;
        double[][] anter = matriz;
        for (int i = 0; i < gr.length; i++) {
            int n = 1;
            for (int j = 0; j < matriz.length; j++) {
                for (int k = 0; k< matriz[j].length; k++) {
                    if(gr[i].elemento == anter[j][k]){
                        gr[i].grupo = new int[]{j, k};
                        System.out.println("grupo Etapa "+(conta++)+": "+Arrays.toString(gr[i].grupo));
                        anter[j][k] = -1;
                        n = -1;
                        break;
                }

                }
                if(n == -1) break;
            }
        }
    return historial(gr);}

    public int[] historial(grupo[] gr){
        grupo[] historial = new grupo[gr.length];
        
        //System.out.println(Arrays.toString(gr[0].grupo));
        historial[0] = new grupo();
        historial[0].grupo = gr[0].grupo;
        int cont = 2;
        for (int i = 1; i < gr.length; i++) {
            historial[i] = new grupo();
            for (int m = i-1; m >= 0; m--) {
                int [] nuevo_grupo;
                if (gr[i].grupo != null && gr[m].grupo != null ) {
                    
                    if (((new toolsScript().Existe_enArreglo(gr[m].grupo, gr[i].grupo[0])) || (new toolsScript().Existe_enArreglo(gr[m].grupo, gr[i].grupo[1])))) {
                        if((new toolsScript().Existe_enArreglo(gr[m].grupo, gr[i].grupo[0])) && (new toolsScript().Existe_enArreglo(gr[m].grupo, gr[i].grupo[1]))){

                        }else if(new toolsScript().Existe_enArreglo(gr[m].grupo, gr[i].grupo[0])){
                            nuevo_grupo = new int[(gr[m].grupo.length+1)];
                            for (int j = 0; j < nuevo_grupo.length-1; j++) nuevo_grupo[j] = gr[m].grupo[j];    
                            nuevo_grupo[nuevo_grupo.length-1] = gr[i].grupo[1];
                            gr[i].grupo = nuevo_grupo;
                            historial[i].grupo = nuevo_grupo;
                        } else if(new toolsScript().Existe_enArreglo(gr[m].grupo, gr[i].grupo[1])) {
                            nuevo_grupo = new int[gr[m].grupo.length+1];
                            for (int j = 0; j < nuevo_grupo.length-1; j++) nuevo_grupo[j] = gr[m].grupo[j];    
                            nuevo_grupo[nuevo_grupo.length-1] = gr[i].grupo[0];
                            gr[i].grupo = nuevo_grupo;
                            historial[i].grupo = nuevo_grupo;
                        }
                    }
                }else if(gr[i].grupo == null && gr[m].grupo == null ){
                    break;
                }else{
                    nuevo_grupo = gr[i].grupo;
                    historial[i].grupo = nuevo_grupo;
                }
            }
        }
        for (int i = 0, etapa = 1; i < historial.length; i++) {
            if(historial[i].grupo != null && !(historial[i].grupo[0] == 0 && historial[i].grupo[1] == 0)){
            System.out.println("Etapa "+(etapa++)+" "+Arrays.toString(historial[i].grupo)+" | Elemento " +new DecimalFormat("#.00").format(gr[i].elemento));}
        }
        int contador = 0;
        for (int i = 0; i < historial.length; i++) if(historial[i].grupo != null) for (int j = 0; j < historial[i].grupo.length; j++) contador++;

        int[] linea = new int[contador];
        contador = 0;
        for (int i = 0; i < historial.length; i++) if(historial[i].grupo != null) for (int j = 0; j < historial[i].grupo.length; j++) linea[contador++] = historial[i].grupo[j];

        linea = new toolsScript().eliminarDuplicados(linea);
        System.out.println("Debugueando: "+Arrays.toString(linea));

        //dendograma(linea, gr);

    return linea;}

    public void dendograma(int[] linea, grupo[] gr){
        System.out.println("_________________________________\nDENDOGRAMA\n");
        String[][] dendograma = new String[linea.length][linea.length];
        int salto = 0;
        for (int i = 0; i < dendograma.length; i++) {
            for (int j = 0; j < dendograma.length; j++) {
                if(i == 0) dendograma[i][j] = "|  ";
                else if(j >= salto && j < salto+2 ) dendograma[i][j] = "___";
                else if(j >= salto+2 )dendograma[i][j] = "|  ";
                else dendograma[i][j] = "   ";
            }
            if(i != 0) salto += 2;
            }
        for (int i = 0; i < dendograma.length; i++) {
            System.out.print(linea[i]+"  ");
        }
        System.out.println();
        for (int i = 0; i < dendograma.length; i++) {
            for (int j = 0; j < dendograma.length; j++) {
                System.out.print(dendograma[i][j]);
            }
            System.out.println();
            
        }
    }

    public double[][] vmc(double[][] matriz, int minI, int minJ) {
        int newSize = matriz.length - 1;
        double[][] matriz_agrupada = new double[newSize][newSize];

        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                if (i < minI && j < minJ) {
                    matriz_agrupada[i][j] = matriz[i][j];
                } else if (i < minI) {
                    matriz_agrupada[i][j] = min(matriz[i][j], matriz[i][minJ]);
                } else if (j < minJ) {
                    matriz_agrupada[i][j] = min(matriz[i + 1][j], matriz[minI][j]);
                } else {
                    matriz_agrupada[i][j] = matriz[i + 1][j];
                }
            }
        }
        for (int k = 0; k < newSize; k++) matriz_agrupada[k][k] = 1.00;
        

        return matriz_agrupada;
    }

    public double[][] vml(double[][] matriz, int minI, int minJ) {
        int newSize = matriz.length - 1;
        double[][] matriz_agrupada = new double[newSize][newSize];
    
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < i; j++) {
                if (i < minI && j < minJ) {
                    matriz_agrupada[i][j] = matriz[i][j];
                } else if (i < minI) {
                    matriz_agrupada[i][j] = max(matriz[i][j], matriz[i][minJ]);
                } else if (j < minJ) {
                    matriz_agrupada[i][j] = max(matriz[i + 1][j], matriz[minI][j]);
                } else {
                    matriz_agrupada[i][j] = matriz[i + 1][j + 1];
                }
            }
        }
    
        for (int k = 0; k < newSize; k++) matriz_agrupada[k][k] = 1.00;
        
    
        return matriz_agrupada;
    }


    public double[][] centroide(double[][] matriz, int minI, int minJ) {
        int newSize = matriz.length - 1;
        double[][] matriz_agrupada = new double[newSize][newSize];
    
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                if (i < minI && j < minJ) {
                    matriz_agrupada[i][j] = matriz[i][j];
                } else if (i < minI) {
                    matriz_agrupada[i][j] = aproximarAlPromedio(matriz[i][j], matriz[i][minJ]);
                } else if (j < minJ) {
                    matriz_agrupada[i][j] = aproximarAlPromedio(matriz[i + 1][j], matriz[minI][j]);
                } else {
                    matriz_agrupada[i][j] = matriz[i + 1][j + 1];
                }
            }
        }
    
        for (int k = 0; k < newSize; k++) matriz_agrupada[k][k] = 1.00;
        
    
        return matriz_agrupada;
    }    
    
        public double[] encontrar_pequenio(double[][] matriz) {
            double menor = Double.MAX_VALUE; 
            double[] datos = new double[3];
    
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < (i); j++) {
                    if (matriz[i][j] < menor) {
                        menor = matriz[i][j];
                        datos[0] = (double) i;
                        datos[1] = (double) j;
                        datos[2] = menor;
                    }
                }
            }
            return datos;
        }


        public double[] encontrar_grande(double[][] matriz) {
            double mayor = Double.MIN_VALUE;
            double[] datos = new double[3];
        
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < (i); j++) {
                    if (matriz[i][j] > mayor) { 
                        mayor = matriz[i][j];
                        datos[0] = (double) i; 
                        datos[1] = (double) j; 
                        datos[2] = mayor; 
                    }
                }
            }
            
            return datos;
        }
        


        public double min(double a, double b) {
            return (a < b) ? a : b;
        }

        private double max(double a, double b) {
            return (a > b) ? a : b;
        }


        public double[] promedio(double[][] matriz) {
            double suma = 0;
            int totalElementos = 0;
        
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < (i); j++) {
                    suma += matriz[i][j];
                    totalElementos++;
                }
            }
        
            double promedio = suma / totalElementos;
        
            double cercano = matriz[1][0]; 
            double menorDiferencia = Math.abs(cercano - promedio); 
            double[] datos = new double[3];
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < (i); j++) {
                    double diferencia = Math.abs(matriz[i][j] - promedio);
                    if (diferencia < menorDiferencia && matriz[i][j] != 1) {
                        menorDiferencia = diferencia;
                        cercano = matriz[i][j];
                        datos[0] = (double) i; 
                        datos[1] = (double) j; 
                        datos[2] = cercano; 
                    }
                }
            }
            
        
            return datos; 
        }


        private double aproximarAlPromedio(double a, double b) {
            double promedio = (a + b) / 2;
            return (Math.abs(a - promedio) < Math.abs(b - promedio)) ? a : b;
        }

}
