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
        String fileName = "C4-D4-I5";
        //CSVReader reader = new CSVReader(new FileReader("C:\\datasets\\" + fileName), ';');
        CSVReader reader = new CSVReader(new FileReader("C:\\datasets\\art2\\" + fileName), ',');

        FileWriter writerSil = new FileWriter("Resultados-" + fileName + ".csv");
        //FileWriter writerDunn = new FileWriter("Dunn-" + fileName + ".csv");
        List<String[]> myEntries = reader.readAll();
        List<Punto> puntos = new ArrayList<Punto>();

        for (String[] strings : myEntries) {
            Punto p = new Punto(strings);
            puntos.add(p);
        }

        int maxNumCluster = 4;
/*
        KMeans kmeans = new KMeans();
        long startTime = System.currentTimeMillis();
        System.out.println("Silhouette: " + fileName);
        writerSil.write("k;Silhouette;Dunn;DavidBouldin;AverageDistance;MaxiumDiameter;MinimumDistance;SquaredSum;CalinskiHarabasz;AverageBetweenCluster\n");
        for (int k = 4; k <= maxNumCluster; k++) {
            System.out.println("KMeans: " + k);
            KMeansResultado resultado = kmeans.calcular(puntos, k);
            System.out.println("HAY "+ resultado.getClusters().size()+" CLUSTERSSSS");

            //PRINT POINTS WITH ITS CLUSTER NUMBER
            FileWriter writer = new FileWriter(fileName + "-" + k + "-Points.txt");
            int clusterNumber = 1;
            for (Cluster c : resultado.getClusters()) {
                for (Punto p : c.getPuntos()) {
                    System.out.println(p.toString() + "," + clusterNumber);
                    writer.write(p.toString() + "," + clusterNumber + "\n");
                }
                clusterNumber++;


            }

            writerSil.write(k + ";");
            writerSil.write(resultado.getSilhouette() + ";");
            writerSil.write(resultado.getDunn() + ";");
            writerSil.write(resultado.getDavidBouldin() + ";");
            writerSil.write(resultado.getAverageDistance() + ";");
            writerSil.write(resultado.getMaximumDiameter() + ";");
            writerSil.write(resultado.getMinimumDistance() + ";");
            writerSil.write(resultado.getSquaredSum() + ";");
            writerSil.write(resultado.getCalinskiHarabasz() + ";");
            writerSil.write(resultado.getAverageBetweenClusterDistance() + ";");

            writerSil.write("\n");
        }
        long stopTime = System.currentTimeMillis();
        writerSil.write("TIME;" + (stopTime - startTime));

        writerSil.close();
        */
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