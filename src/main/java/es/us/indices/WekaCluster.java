package es.us.indices;

/**
 * Created by Josem on 16/02/2017.
 */

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WekaCluster {

    public static final int ALL = 0;
    public static final int SMALLDATA = 1;
    public static final int BIGDATA = 2;

    public static BufferedReader readDataFile(String filename) {
        BufferedReader inputReader = null;

        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }

        return inputReader;
    }

    public static String getFileNameOutput(int selector, String datasetName) {
        String aux = "";
        switch (selector) {
            case SMALLDATA:
                aux = "SMALLDATA";
                break;
            case BIGDATA:
                aux = "BIGDATA";
                break;
            case ALL:
                aux = "ALL";
                break;
        }
        return datasetName + "Resultados-" + aux + "-" + new SimpleDateFormat("yyyyMMddhhmm").format(Calendar.getInstance().getTime()) + ".txt";
    }

    public static void main(String[] args) throws Exception {
        int times = 1;
        if (args.length > 4)
            times = 1;
        for (int ttt = 0; ttt < times; ttt++) {

            int minNumCluster = 2;
            int maxNumCluster = 10;
            int selector = SMALLDATA;

            String fileName = "C5-D20-I1000.csv";
            String folderFile = "C:\\datasets\\art2\\005\\";
            String folderFileOLD = "C:\\Users\\Josem\\Documents\\randomDataset\\";
            String pathFile = folderFile + fileName;
            String outFile = getFileNameOutput(selector, fileName);


            if (args.length > 2) {
                minNumCluster = Integer.parseInt(args[0]);
                maxNumCluster = Integer.parseInt(args[1]);
                pathFile = args[2];
                selector = Integer.parseInt(args[4]);
                outFile = getFileNameOutput(selector, args[3]);

            }

            System.out.println("*******************************");
            System.out.println("*********CLUSTER WEKA**********");
            System.out.println("*******************************");
            System.out.println("Configuration:");
            System.out.println("\tCLUSTERS: " + minNumCluster + "-" + maxNumCluster);
            System.out.println("\tInput file: " + pathFile);
            System.out.println("\tOutput File: " + outFile);

            System.out.println("Running...\n");

            System.out.println("Loading file..");
            DataSource source = new DataSource(pathFile);
            Instances data = source.getDataSet();
            System.out.println("File loaded\n");
            long startTime = System.currentTimeMillis();

            FileWriter writerSil = new FileWriter(outFile);
            writerSil.write("k;Silhouette;Dunn;DavidBouldin;AverageDistance;MaxiumDiameter;MinimumDistance;CalinskiHarabasz;AverageBetweenCluster;BDSilhouette;BDDunn;TSilhouette;TDunn;TDavidBouldin;TAverageDistance;TMaxiumDiameter;TMinimumDistance;TCalinskiHarabasz;TAverageBetweenCluster;TBDSilhouette;TBDDunn;elapsedtime\n");

            for (int k = minNumCluster; k <= maxNumCluster; k++) {
                long startTimeK = System.currentTimeMillis();
                System.out.println("*** K = " + k + " ***");
                System.out.println("Executing KMeans");

                KMeansIndices kmeans = new KMeansIndices(k);

                kmeans.setSeed(ttt);

                //important parameter to set: preserver order, number of cluster.
                kmeans.setPreserveInstancesOrder(true);
                kmeans.setNumClusters(k);
                kmeans.setInitializationMethod(new SelectedTag(SimpleKMeans.KMEANS_PLUS_PLUS, SimpleKMeans.TAGS_SELECTION));
                kmeans.buildClusterer(data);
                kmeans.generateStructure(data);
                System.out.println("Calculating índices");

                writerSil.write(k + ";");

                switch (selector) {
                    case SMALLDATA:
                        kmeans.calculaIndices();
                        writerSil.write(kmeans.getSilhouette().getResultado() + ";");
                        writerSil.write(kmeans.getDunn().getResultado() + ";");
                        writerSil.write(kmeans.getDavidBouldin().getResultado() + ";");
                        writerSil.write(kmeans.getAverageDistance().getResultado() + ";");
                        writerSil.write(kmeans.getMaximumDiameter().getResultado() + ";");
                        writerSil.write(kmeans.getMinimumDistance().getResultado() + ";");
                        //writerSil.write(kmeans.getSquaredSum().getResultado() + ";");
                        writerSil.write(kmeans.getCalinskiHarabasz().getResultado() + ";");
                        writerSil.write(kmeans.getAverageBetweenClusterDistance().getResultado() + ";");
                        //Write the times
                        writerSil.write(kmeans.getSilhouette().getTime() + ";");
                        writerSil.write(kmeans.getDunn().getTime() + ";");
                        writerSil.write(kmeans.getDavidBouldin().getTime() + ";");
                        writerSil.write(kmeans.getAverageDistance().getTime() + ";");
                        writerSil.write(kmeans.getMaximumDiameter().getTime() + ";");
                        writerSil.write(kmeans.getMinimumDistance().getTime() + ";");
                        //writerSil.write(kmeans.getSquaredSum().getTime() + ";");
                        writerSil.write(kmeans.getCalinskiHarabasz().getTime() + ";");
                        writerSil.write(kmeans.getAverageBetweenClusterDistance().getTime() + ";");
                        break;
                    case BIGDATA:
                        kmeans.calculaIndicesBigData();
                        writerSil.write(kmeans.getBdSilhouette().getResultado() + ";");
                        writerSil.write(kmeans.getBdDunn().getResultado() + ";");
                        //Write the times
                        writerSil.write(kmeans.getBdSilhouette().getTime() + ";");
                        writerSil.write(kmeans.getBdDunn().getTime() + ";");
                        break;
                    case ALL:
                        kmeans.calculaIndices();
                        writerSil.write(kmeans.getSilhouette().getResultado() + ";");
                        writerSil.write(kmeans.getDunn().getResultado() + ";");
                        writerSil.write(kmeans.getDavidBouldin().getResultado() + ";");
                        writerSil.write(kmeans.getAverageDistance().getResultado() + ";");
                        writerSil.write(kmeans.getMaximumDiameter().getResultado() + ";");
                        writerSil.write(kmeans.getMinimumDistance().getResultado() + ";");
                        //writerSil.write(kmeans.getSquaredSum().getResultado() + ";");
                        writerSil.write(kmeans.getCalinskiHarabasz().getResultado() + ";");
                        writerSil.write(kmeans.getAverageBetweenClusterDistance().getResultado() + ";");

                        kmeans.calculaIndicesBigData();
                        writerSil.write(kmeans.getBdSilhouette().getResultado() + ";");
                        writerSil.write(kmeans.getBdDunn().getResultado() + ";");
                        //Write the times
                        writerSil.write(kmeans.getSilhouette().getTime() + ";");
                        writerSil.write(kmeans.getDunn().getTime() + ";");
                        writerSil.write(kmeans.getDavidBouldin().getTime() + ";");
                        writerSil.write(kmeans.getAverageDistance().getTime() + ";");
                        writerSil.write(kmeans.getMaximumDiameter().getTime() + ";");
                        writerSil.write(kmeans.getMinimumDistance().getTime() + ";");
                        //writerSil.write(kmeans.getSquaredSum().getTime() + ";");
                        writerSil.write(kmeans.getCalinskiHarabasz().getTime() + ";");
                        writerSil.write(kmeans.getAverageBetweenClusterDistance().getTime() + ";");

                        writerSil.write(kmeans.getBdSilhouette().getTime() + ";");
                        writerSil.write(kmeans.getBdDunn().getTime() + ";");
                        break;
                }

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


}