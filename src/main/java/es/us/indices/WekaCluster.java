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
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

        int minNumCluster = 2;
        int maxNumCluster = 15;

        String fileName = "C5-D20-I10000.txt";
        String folderFileOLD = "C:\\datasets\\art2\\";
        String folderFile = "C:\\Users\\Josem\\Documents\\randomDataset\\";
        String pathFile = folderFile + fileName;
        String outFile = "Resultados-" + fileName + ".csv";

        if (args.length > 2) {
            minNumCluster = Integer.parseInt(args[0]);
            maxNumCluster = Integer.parseInt(args[1]);
            pathFile = args[2];
            outFile = args[3] + new SimpleDateFormat("yyyyMMddhhmm").format(Calendar.getInstance().getTime()) + ".txt";
        }

        System.out.println("*******************************");
        System.out.println("*********CLUSTER WEKA**********");
        System.out.println("*******************************");
        System.out.println("Configuration:");
        System.out.println("\tCLUSTERS: " + minNumCluster + "-" + maxNumCluster);
        System.out.println("\tInput file: "+ pathFile);
        System.out.println("\tOutput File: "+ outFile);

        System.out.println("Running...\n");

        System.out.println("Loading file..");
        DataSource source = new DataSource(pathFile);
        Instances data = source.getDataSet();
        System.out.println("File loaded\n");
        long startTime = System.currentTimeMillis();

        FileWriter writerSil = new FileWriter(outFile);
        writerSil.write("k;Silhouette;Dunn;DavidBouldin;AverageDistance;MaxiumDiameter;MinimumDistance;CalinskiHarabasz;AverageBetweenCluster;elapsedtime\n");

        for (int k = minNumCluster; k <= maxNumCluster; k++) {
            long startTimeK = System.currentTimeMillis();
            System.out.println("*** K = " + k + " ***");
            System.out.println("Executing KMeans");

            KMeansIndices kmeans = new KMeansIndices(k);

            kmeans.setSeed(10);

            //important parameter to set: preserver order, number of cluster.
            kmeans.setPreserveInstancesOrder(true);
            kmeans.setNumClusters(k);
            kmeans.buildClusterer(data);
            kmeans.generateStructure(data, k);
            System.out.println("Calculating índices");
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

            long stopTimeK = System.currentTimeMillis();
            long elapsedTimeK = stopTimeK - startTimeK;
            writerSil.write(String.valueOf(elapsedTimeK));
            System.out.println("Time: " + elapsedTimeK);

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