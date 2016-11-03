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

    public KMeansResultado(List<Cluster> clusters, Double ofv, Double dunn, Double silhouette,Double davidBouldin, double entropy) {
        super();
        this.ofv = ofv;
        this.dunn = dunn;
        this.silhouette = silhouette;
        this.clusters = clusters;
        this.davidBouldin = davidBouldin;
        this.entropy = entropy;
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

}

