package es.us.indices;

/**
 * Created by Josem on 16/02/2017.
 */

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class WekaCluster {

    public static BufferedReader readDataFile(String filename) {
        BufferedReader inputReader = null;

        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }

        return inputReader;
    }

    public static void main(String[] args) throws Exception {

        int maxNumCluster = 10;


        String fileName = "C11-D20-I1000.csv";

        DataSource source = new DataSource("C:\\datasets\\art2\\" + fileName);
        Instances data = source.getDataSet();

        long startTime = System.currentTimeMillis();

        FileWriter writerSil = new FileWriter("Resultados-" + fileName + ".csv");
        writerSil.write("k;Silhouette;Dunn;DavidBouldin;AverageDistance;MaxiumDiameter;MinimumDistance;CalinskiHarabasz;AverageBetweenCluster\n");

        for (int k = 2; k <= maxNumCluster; k++) {
            System.out.println("*** K = " + k + " ***");
            System.out.println("Calculando KMeans");

            KMeansIndices kmeans = new KMeansIndices(k);

            kmeans.setSeed(10);

            //important parameter to set: preserver order, number of cluster.
            kmeans.setPreserveInstancesOrder(true);
            kmeans.setNumClusters(k);
            kmeans.buildClusterer(data);
            kmeans.generateStructure(data, k);
            System.out.println("Calculando índices");
            kmeans.calculaIndices();

            writerSil.write(k + ";");
            writerSil.write(kmeans.getSilhouette() + ";");
            writerSil.write(kmeans.getDunn() + ";");
            writerSil.write(kmeans.getDavidBouldin() + ";");
            writerSil.write(kmeans.getAverageDistance() + ";");
            writerSil.write(kmeans.getMaximumDiameter() + ";");
            writerSil.write(kmeans.getMinimumDistance() + ";");
            //writerSil.write(kmeans.getSquaredSum() + ";");
            writerSil.write(kmeans.getCalinskiHarabasz() + ";");
            writerSil.write(kmeans.getAverageBetweenClusterDistance() + ";");

            writerSil.write("\n");
            System.out.println("\n");

        }
        long stopTime = System.currentTimeMillis();

        writerSil.write("TIME;" + (stopTime - startTime));

        writerSil.close();


/*
        // This array returns the cluster number (starting with 0) for each instance
        // The array has as many elements as the number of instances
        int[] assignments = kmeans.getAssignments();

        int i = 0;

        for (int clusterNum : assignments) {
            System.out.printf("Instance %d -> Cluster %d \n", i, clusterNum);
            i++;
        }
        */
    }
}