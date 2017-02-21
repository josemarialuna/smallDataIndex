package es.us.indices;

import java.util.ArrayList;
import java.util.List;

public class KMeansResultado {
    private List<Cluster> clusters = new ArrayList<Cluster>();
    private Double ofv;
    private Double dunn;
    private Double silhouette;
    private Double davidBouldin;
    private Double entropy;
    private Double maximumDiameter;
    private Double averageDistance;
    private Double minimumDistance;
    private Double squaredSum;
    private Double calinskiHarabasz;
    private Double averageBetweenClusterDistance;


    public KMeansResultado(List<Cluster> clusters, Double ofv, Double dunn, Double silhouette, Double davidBouldin, Double entropy, Double maximumDiameter, Double averageDistance, Double minimumDistance, Double squaredSum, Double calinskiHarabasz, Double averageBetweenClusterDistance) {
        super();
        this.ofv = ofv;
        this.dunn = dunn;
        this.silhouette = silhouette;
        this.clusters = clusters;
        this.davidBouldin = davidBouldin;
        this.entropy = entropy;
        this.maximumDiameter = maximumDiameter;
        this.averageDistance = averageDistance;
        this.minimumDistance = minimumDistance;
        this.squaredSum = squaredSum;
        this.calinskiHarabasz = calinskiHarabasz;
        this.averageBetweenClusterDistance = averageBetweenClusterDistance;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public Double getOfv() {
        return ofv;
    }

    public Double getDunn() {
        return dunn;
    }

    public Double getSilhouette() {
        return silhouette;
    }

    public Double getDavidBouldin() {
        return davidBouldin;
    }

    public Double getEntropy() {
        return entropy;
    }

    public Double getMaximumDiameter() {
        return maximumDiameter;
    }

    public Double getAverageDistance() {
        return averageDistance;
    }

    public Double getMinimumDistance() {
        return minimumDistance;
    }

    public Double getSquaredSum() {
        return squaredSum;
    }

    public Double getCalinskiHarabasz() {
        return calinskiHarabasz;
    }

    public Double getAverageBetweenClusterDistance() {
        return averageBetweenClusterDistance;
    }

}

