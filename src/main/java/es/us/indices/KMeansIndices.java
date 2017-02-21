package es.us.indices;

import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josem on 17/02/2017.
 */
public class KMeansIndices extends weka.clusterers.SimpleKMeans {

    private double silhouette;
    private double dunn;
    private double davidBouldin;
    private double calinskiHarabasz;
    private double maximumDiameter;
    private double squaredDistance;
    private double averageDistance;
    private double averageBetweenClusterDistance;
    private double minimumDistance;
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
        this.davidBouldin = calcularDavidBouldin();
        this.calinskiHarabasz = calcularCalinskiHarabasz();
        this.maximumDiameter = calcularMaximumDiameter();
        this.squaredDistance = calcularSquaredDistance();
        this.averageDistance = calcularAverageDistance();
        this.averageBetweenClusterDistance = calcularAverageBetweenClusterDistance();
        this.minimumDistance = calcularMinimumDistance();
    }

    public void generateStructure(Instances data, int k) {

        try {
            int[] assignments = super.getAssignments();
            for (int i = 0; i < assignments.length; i++) {
                clusters.get(assignments[i]).getInstances().add(data.instance(i));
                clusters.get(assignments[i]).setCentroide(super.getClusterCentroids().get(assignments[i]));
            }

        } catch (Exception e) {
        }


    }

    private Double calcularDunn() {
        double dunn;
        double max = 0;
        double min = 0;

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
        return dunn;
    }

    private Double calcularSilhouette() {
        double silhouette;
        double a;
        double distA = 0;
        double b;
        double distB = 0;
        double cont;

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
        return silhouette;
    }

    private Double calcularDavidBouldin() {
        int numberOfClusters = clusters.size();

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
            for (i = 0; i < numberOfClusters; i++) {
                double max = Double.NEGATIVE_INFINITY;
                for (int j = 0; j < numberOfClusters; j++)
                    if (i != j) {
                        double val = (withinClusterDistance[i] + withinClusterDistance[j])
                                / m_DistanceFunction.distance(clusters.get(i).getCentroide(), clusters.get(j).getCentroide());
                        if (val > max)
                            max = val;
                    }
                result = result + max;
            }
            return result / numberOfClusters;
        }

    }

    private Double calcularCalinskiHarabasz() {
        double squaredInterCluter = 0;
        double aux = 0;
        double cont = 0;

        for (Cluster cluster : clusters) {
            for (Cluster cluster2 : clusters) {
                if (!cluster.equals(cluster2)) {
                    aux = m_DistanceFunction.distance(cluster.getCentroide(), cluster2.getCentroide());
                    squaredInterCluter += aux * aux;
                    cont++;
                }
            }
        }

        return (this.calcularSquaredDistance()) / (squaredInterCluter / cont);
    }

    //Diámetro máximo entre dos puntos que pertenecen al mismo cluster.
    private Double calcularMaximumDiameter() {
        double maximumDiameter = 0;
        double aux = 0;

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
        return maximumDiameter;
    }

    //Media de distancia cuadrática entre los puntos del mismo cluster.
    private Double calcularSquaredDistance() {
        double squaredDistance = 0;
        double aux;
        double cont = 0;

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

        return squaredDistance / cont;
    }

    private Double calcularAverageDistance() {
        double averageDistance;
        double distA = 0;
        double cont = 0;

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

        return averageDistance;
    }

    private Double calcularAverageBetweenClusterDistance() {
        double averageDistanceBetween;
        double distA = 0;
        double cont = 0;

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

        return averageDistanceBetween;
    }

    //Distancia minima entre puntos de diferentes clusters
    private Double calcularMinimumDistance() {
        double minimumDistance = -1;
        double aux;

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

        return minimumDistance;
    }

    public double getMaximumDiameter() {
        return maximumDiameter;
    }

    public void setMaximumDiameter(double maximumDiameter) {
        this.maximumDiameter = maximumDiameter;
    }

    public double getSilhouette() {
        return silhouette;
    }

    public void setSilhouette(double silhouette) {
        this.silhouette = silhouette;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    public double getDunn() {
        return dunn;
    }

    public void setDunn(double dunn) {
        this.dunn = dunn;
    }

    public double getDavidBouldin() {
        return davidBouldin;
    }

    public void setDavidBouldin(double davidBouldin) {
        this.davidBouldin = davidBouldin;
    }

    public double getCalinskiHarabasz() {
        return calinskiHarabasz;
    }

    public void setCalinskiHarabasz(double calinskiHarabasz) {
        this.calinskiHarabasz = calinskiHarabasz;
    }

    public double getSquaredDistance() {
        return squaredDistance;
    }

    public void setSquaredDistance(double squaredDistance) {
        this.squaredDistance = squaredDistance;
    }

    public double getAverageDistance() {
        return averageDistance;
    }

    public void setAverageDistance(double averageDistance) {
        this.averageDistance = averageDistance;
    }

    public double getAverageBetweenClusterDistance() {
        return averageBetweenClusterDistance;
    }

    public void setAverageBetweenClusterDistance(double averageBetweenClusterDistance) {
        this.averageBetweenClusterDistance = averageBetweenClusterDistance;
    }

    public double getMinimumDistance() {
        return minimumDistance;
    }

    public void setMinimumDistance(double minimumDistance) {
        this.minimumDistance = minimumDistance;
    }
}



