package es.us.indices;


import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Start {
    public static void main(String[] args) throws IOException {
        //CSVReader reader = new CSVReader(new FileReader("data.csv"));
        String fileName = "Prueba";
        //CSVReader reader = new CSVReader(new FileReader("C:\\datasets\\" + fileName), ';');
        CSVReader reader = new CSVReader(new FileReader("C:\\Users\\Josem\\Desktop\\datasets\\a1.txt"), ';');

        FileWriter writerSil = new FileWriter("Silhoutte-" + fileName + ".csv");
        FileWriter writerDunn = new FileWriter("Dunn-" + fileName + ".csv");
        List<String[]> myEntries = reader.readAll();
        List<Punto> puntos = new ArrayList<Punto>();

        for (String[] strings : myEntries) {
            Punto p = new Punto(strings);
            puntos.add(p);
        }

        int maxNumCluster = 30;

        KMeans kmeans = new KMeans();
        long startTime = System.currentTimeMillis();
        System.out.println("Silhouette: " + fileName);
        for (int k = 2; k <= maxNumCluster; k++) {
            KMeansResultado resultado = kmeans.calcular(puntos, k);
            writerSil.write(k + ";");
            writerSil.write(resultado.getSilhouette() + ";");
            writerSil.write(resultado.getDunn() + ";");
            writerSil.write(resultado.getDavidBouldin() + "\n");
        }
        long stopTime = System.currentTimeMillis();
        writerSil.write("TIME;" + (stopTime - startTime));

        writerSil.close();
/*
        startTime = System.currentTimeMillis();
        System.out.println("Dunn: "+fileName);
        for (int k = 1; k <= maxNumCluster; k++) {
            KMeansResultado resultado = kmeans.calcular(puntos, k);
            writerDunn.write(k + ";" + resultado.getDunn() + "\n");
        }
        stopTime = System.currentTimeMillis();
        writerDunn.write("TIME;" + (stopTime - startTime));

        writerDunn.close();
        */
        System.out.println("\n\n");
    }
}