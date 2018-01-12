package es.us.indices;

import weka.core.Instance;
import weka.core.Instances;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Josem on 17/02/2017.
 */
public class KMeansIndices extends weka.clusterers.SimpleKMeans {

    private Indice silhouette;
    private Indice dunn;
    private Indice bdSilhouette;
    private Indice bdDunn;
    private Indice davidBouldin;
    private Indice calinskiHarabasz;
    private Indice maximumDiameter;
    private Indice squaredDistance;
    private Indice averageDistance;
    private Indice averageBetweenClusterDistance;
    private Indice minimumDistance;
    private List<Cluster> clusters;

    public KMeansIndices(int k) {
        super();
        this.clusters = new ArrayList<Cluster>(k);
        for (int i = 0; i < k; i++) {
            this.clusters.add(new Cluster());
        }
    }

    public void calculaIndices() {

        this.silhouette = calcularSilhouette();
        this.dunn = calcularDunn();
        this.bdSilhouette = calcularBDSilhouette();
        this.bdDunn = calcularBDDunn();
        this.davidBouldin = calcularDavidBouldin();
        this.calinskiHarabasz = calcularCalinskiHarabasz();
        this.maximumDiameter = calcularMaximumDiameter();
        this.squaredDistance = calcularSquaredDistance();
        this.averageDistance = calcularAverageDistance();
        this.averageBetweenClusterDistance = calcularAverageBetweenClusterDistance();
        this.minimumDistance = calcularMinimumDistance();

    }

    public void calculaIndicesBigData() {

        this.bdSilhouette = calcularBDSilhouette();
        this.bdDunn = calcularBDDunn();

    }

    public void generateStructure(Instances data) {

        try {
            int[] assignments = super.getAssignments();
            for (int i = 0; i < assignments.length; i++) {
                clusters.get(assignments[i]).getInstances().add(data.instance(i));
                //if the centroid is not set
                if (clusters.get(assignments[i]).getCentroide() == null) {
                    clusters.get(assignments[i]).setCentroide(super.getClusterCentroids().get(assignments[i]));
                }
            }

        } catch (Exception e) {
            System.out.println("Excepcion en GENERATE STRUCTURE");
            e.printStackTrace();
        }


    }

    private Indice calcularDunn() {
        double dunn = 0.0;
        double max = 0;
        double min = 0;
        long startTime = System.currentTimeMillis();
        try {
            for (Cluster cluster : clusters) {
                for (Instance punto : cluster.getInstances()) {
                    for (Cluster cluster2 : clusters) {
                        if (!cluster.equals(cluster2)) {
                            for (Instance punto2 : cluster.getInstances()) {
                                if (!punto.equals(punto2)) {
                                    double dist = m_DistanceFunction.distance(punto, punto2);
                                    if (min != 0) {
                                        if (dist < min) {
                                            min = dist;
                                        }
                                    } else {
                                        min = dist;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (Cluster cluster : clusters) {
                for (Instance punto : cluster.getInstances()) {
                    for (Instance punto2 : cluster.getInstances()) {
                        if (!punto.equals(punto2)) {
                            double dist = m_DistanceFunction.distance(punto, punto2);
                            if (dist > max) {
                                max = dist;
                            }
                        }

                    }
                }
            }
            //System.out.println("MINIMO: " + min);
            //System.out.println("MAXIMO: " + max);

            dunn = min / max;
        } catch (Exception e) {
            e.printStackTrace();
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        return new Indice(dunn, elapsedTime);

    }

    private Indice calcularBDDunn() {
        double bdDunn = 0.0;
        double max = 0;
        double min = 0;

        long startTime = System.currentTimeMillis();
        try {

            for (Cluster cluster : clusters) {
                for (Cluster cluster2 : clusters) {
                    if (!cluster.equals(cluster2) && cluster.getCentroide() != null && cluster2.getCentroide() != null) {
                        double dist = m_DistanceFunction.distance(cluster.getCentroide(), cluster2.getCentroide());
                        if (min != 0) {
                            if (dist < min) {
                                min = dist;
                            }
                        } else {
                            min = dist;
                        }
                    }
                }
            }


            //get the maximum distance of the points to the centroid of the cluster they belong to
            for (Cluster cluster : clusters) {
                if (cluster.getCentroide() != null) {
                    for (Instance punto : cluster.getInstances()) {
                        double dist = m_DistanceFunction.distance(punto, cluster.getCentroide());
                        if (dist > max) {
                            max = dist;
                        }
                    }
                }
            }
            //System.out.println("MINIMO: " + min);
            //System.out.println("MAXIMO: " + max);

            bdDunn = min / max;
        } catch (Exception e) {
            e.printStackTrace();
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        return new Indice(bdDunn, elapsedTime);

    }

    private Indice calcularSilhouette() {
        double silhouette;
        double a;
        double distA = 0;
        double b;
        double distB = 0;
        double cont;

        long startTime = System.currentTimeMillis();

        for (Cluster cluster : clusters) {
            for (Instance punto : cluster.getInstances()) {
                for (Cluster cluster2 : clusters) {
                    if (!cluster.equals(cluster2)) {
                        for (Instance punto2 : cluster.getInstances()) {
                            if (!punto.equals(punto2)) {
                                distB += m_DistanceFunction.distance(punto, punto2);

                            }
                        }
                    }
                }
            }
        }
        b = distB / clusters.size();

        cont = 0;
        for (Cluster cluster : clusters) {
            for (Instance punto : cluster.getInstances()) {
                for (Instance punto2 : cluster.getInstances()) {
                    if (!punto.equals(punto2)) {
                        distA += m_DistanceFunction.distance(punto, punto2);
                        cont++;
                    }
                }
            }
        }
        a = distA / clusters.size();
        //System.out.println("A: " + a);
        //System.out.println("B: " + b);

        silhouette = b - a / Math.max(a, b);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        return new Indice(silhouette, elapsedTime);
    }

    private Indice calcularBDSilhouette() {
        double silhouette;
        double a;
        double distA = 0;
        double b;
        double distB = 0;
        double cont = 0;

        long startTime = System.currentTimeMillis();

        for (Cluster cluster : clusters) {
            if (cluster.getCentroide() != null) {
                for (Cluster cluster2 : clusters) {
                    if (cluster2.getCentroide() != null) {
                        if (!cluster.equals(cluster2)) {
                            distB += m_DistanceFunction.distance(cluster.getCentroide(), cluster2.getCentroide());
                            cont++;
                        }
                    }
                }
            }
        }

        b = distB / cont;

        cont = 0;
        for (Cluster cluster : clusters) {
            if (cluster.getCentroide() != null) {
                for (Instance punto : cluster.getInstances()) {
                    distA += m_DistanceFunction.distance(punto, cluster.getCentroide());
                    cont++;
                }
            }
        }
        a = distA / cont;
        //System.out.println("A: " + a);
        //System.out.println("B: " + b);

        silhouette = b - a / Math.max(a, b);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        return new Indice(silhouette, elapsedTime);
    }

    private Indice calcularDavidBouldin() {
        int numberOfClusters = clusters.size();
        double david = 0.0;

        long startTime = System.currentTimeMillis();

        if (numberOfClusters == 1) {
            throw new RuntimeException(
                    "Impossible to evaluate Davies-Bouldin index over a single cluster");
        } else {
            // counting distances within
            double[] withinClusterDistance = new double[numberOfClusters];

            int i = 0;
            for (Cluster cluster : clusters) {
                for (Instance punto : cluster.getInstances()) {
                    withinClusterDistance[i] += m_DistanceFunction.distance(punto, cluster.getCentroide());
                }
                withinClusterDistance[i] /= cluster.getInstances().size();
                i++;
            }


            double result = 0.0;
            double max = Double.NEGATIVE_INFINITY;

            try {
                for (i = 0; i < numberOfClusters; i++) {
                    //if the cluster is null
                    if (clusters.get(i).getCentroide() != null) {

                        for (int j = 0; j < numberOfClusters; j++)
                            //if the cluster is null
                            if (i != j && clusters.get(j).getCentroide() != null) {
                                double val = (withinClusterDistance[i] + withinClusterDistance[j])
                                        / m_DistanceFunction.distance(clusters.get(i).getCentroide(), clusters.get(j).getCentroide());
                                if (val > max)
                                    max = val;
                            }
                    }
                    result = result + max;
                }
            } catch (Exception e) {
                System.out.println("Excepcion al calcular DAVID BOULDIN");
                e.printStackTrace();
            }
            david = result / numberOfClusters;
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        return new Indice(david, elapsedTime);
    }

    private Indice calcularCalinskiHarabasz() {
        double calinski = 0.0;
        double squaredInterCluter = 0;
        double aux;
        double cont = 0;

        long startTime = System.currentTimeMillis();

        try {
            for (Cluster cluster : clusters) {
                if (cluster.getCentroide() != null) {
                    for (Cluster cluster2 : clusters) {
                        if (cluster2.getCentroide() != null) {
                            if (!cluster.equals(cluster2)) {
                                aux = m_DistanceFunction.distance(cluster.getCentroide(), cluster2.getCentroide());
                                squaredInterCluter += aux * aux;
                                cont++;
                            }
                        }
                    }
                }
            }

            calinski = (this.calcularSquaredDistance().getResultado()) / (squaredInterCluter / cont);
        } catch (Exception e) {
            System.out.println("Excepcion al calcular CALINSKI");
            e.printStackTrace();
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        return new Indice(calinski, elapsedTime);
    }

    //Diámetro máximo entre dos puntos que pertenecen al mismo cluster.
    private Indice calcularMaximumDiameter() {
        double maximumDiameter = 0;
        double aux;

        long startTime = System.currentTimeMillis();

        for (Cluster cluster : clusters) {
            for (Instance punto : cluster.getInstances()) {
                for (Instance punto2 : cluster.getInstances()) {
                    if (!punto.equals(punto2)) {
                        aux = m_DistanceFunction.distance(punto, punto2);
                        if (aux > maximumDiameter) {
                            maximumDiameter = aux;
                        }
                    }
                }
            }
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        return new Indice(maximumDiameter, elapsedTime);
    }

    //Media de distancia cuadrática entre los puntos del mismo cluster.
    private Indice calcularSquaredDistance() {
        double squaredDistance = 0;
        double aux;
        double cont = 0;

        long startTime = System.currentTimeMillis();

        for (Cluster cluster : clusters) {
            for (Instance punto : cluster.getInstances()) {
                for (Instance punto2 : cluster.getInstances()) {
                    if (!punto.equals(punto2)) {
                        aux = m_DistanceFunction.distance(punto, punto2);
                        squaredDistance += aux * aux;
                        cont++;
                    }
                }
            }
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        return new Indice(squaredDistance / cont, elapsedTime);
    }

    private Indice calcularAverageDistance() {
        double averageDistance;
        double distA = 0;
        double cont = 0;

        long startTime = System.currentTimeMillis();

        for (Cluster cluster : clusters) {
            for (Instance punto : cluster.getInstances()) {
                for (Instance punto2 : cluster.getInstances()) {
                    if (!punto.equals(punto2)) {
                        distA += m_DistanceFunction.distance(punto, punto2);
                        cont++;
                    }
                }
            }
        }
        averageDistance = distA / cont;


        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        return new Indice(averageDistance, elapsedTime);
    }

    private Indice calcularAverageBetweenClusterDistance() {
        double averageDistanceBetween;
        double distA = 0;
        double cont = 0;

        long startTime = System.currentTimeMillis();

        for (Cluster cluster : clusters) {
            for (Instance punto : cluster.getInstances()) {
                for (Cluster cluster2 : clusters) {
                    if (!cluster.equals(cluster2)) {
                        for (Instance punto2 : cluster.getInstances()) {
                            if (!punto.equals(punto2)) {
                                distA += m_DistanceFunction.distance(punto, punto2);
                                cont++;
                            }
                        }
                    }
                }
            }
        }
        averageDistanceBetween = distA / cont;

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        return new Indice(averageDistanceBetween, elapsedTime);

    }

    //Distancia minima entre puntos de diferentes clusters
    private Indice calcularMinimumDistance() {
        double minimumDistance = -1;
        double aux;

        long startTime = System.currentTimeMillis();

        for (Cluster cluster : clusters) {
            for (Instance punto : cluster.getInstances()) {
                for (Cluster cluster2 : clusters) {
                    if (!cluster.equals(cluster2)) {
                        for (Instance punto2 : cluster.getInstances()) {
                            if (!punto.equals(punto2)) {
                                if (minimumDistance == -1) {
                                    minimumDistance = m_DistanceFunction.distance(punto, punto2);
                                } else {
                                    aux = m_DistanceFunction.distance(punto, punto2);
                                    if (aux < minimumDistance)
                                        minimumDistance = aux;
                                }
                            }
                        }
                    }
                }
            }
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        return new Indice(minimumDistance, elapsedTime);
    }


    public Indice getMaximumDiameter() {
        return maximumDiameter;
    }

    public void setMaximumDiameter(Indice maximumDiameter) {
        this.maximumDiameter = maximumDiameter;
    }

    public Indice getSilhouette() {
        return silhouette;
    }

    public void setSilhouette(Indice silhouette) {
        this.silhouette = silhouette;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    public Indice getDunn() {
        return dunn;
    }

    public void setDunn(Indice dunn) {
        this.dunn = dunn;
    }

    public Indice getDavidBouldin() {
        return davidBouldin;
    }

    public void setDavidBouldin(Indice davidBouldin) {
        this.davidBouldin = davidBouldin;
    }

    public Indice getCalinskiHarabasz() {
        return calinskiHarabasz;
    }

    public void setCalinskiHarabasz(Indice calinskiHarabasz) {
        this.calinskiHarabasz = calinskiHarabasz;
    }

    public Indice getSquaredDistance() {
        return squaredDistance;
    }

    public void setSquaredDistance(Indice squaredDistance) {
        this.squaredDistance = squaredDistance;
    }

    public Indice getAverageDistance() {
        return averageDistance;
    }

    public void setAverageDistance(Indice averageDistance) {
        this.averageDistance = averageDistance;
    }

    public Indice getAverageBetweenClusterDistance() {
        return averageBetweenClusterDistance;
    }

    public void setAverageBetweenClusterDistance(Indice averageBetweenClusterDistance) {
        this.averageBetweenClusterDistance = averageBetweenClusterDistance;
    }

    public Indice getMinimumDistance() {
        return minimumDistance;
    }

    public void setMinimumDistance(Indice minimumDistance) {
        this.minimumDistance = minimumDistance;
    }

    public Indice getBdSilhouette() {
        return bdSilhouette;
    }

    public void setBdSilhouette(Indice bdSilhouette) {
        this.bdSilhouette = bdSilhouette;
    }

    public Indice getBdDunn() {
        return bdDunn;
    }

    public void setBdDunn(Indice bdDunn) {
        this.bdDunn = bdDunn;
    }
}



