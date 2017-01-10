package es.us.indices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeans {
    public KMeansResultado calcular(List<Punto> puntos, Integer k) {
        List<Cluster> clusters = elegirCentroides(puntos, k);

        while (!finalizo(clusters)) {
            prepararClusters(clusters);
            asignarPuntos(puntos, clusters);
            recalcularCentroides(clusters);
        }

        Double ofv = calcularFuncionObjetivo(clusters);
        Double dunn = calcularDunn(clusters);
        Double silhouette = calcularSilhouette(clusters);
        Double davidBouldin = calcularDavidBouldin(clusters);
        Double entropy = calcularEntropy(clusters);

        return new KMeansResultado(clusters, ofv, dunn, silhouette, davidBouldin, entropy);
    }

    private void recalcularCentroides(List<Cluster> clusters) {
        for (Cluster c : clusters) {
            if (c.getPuntos().isEmpty()) {
                c.setTermino(true);
                continue;
            }

            Float[] d = new Float[c.getPuntos().get(0).getGrado()];
            Arrays.fill(d, 0f);
            for (Punto p : c.getPuntos()) {
                for (int i = 0; i < p.getGrado(); i++) {
                    d[i] += (p.get(i) / c.getPuntos().size());
                }
            }

            Punto nuevoCentroide = new Punto(d);

            if (nuevoCentroide.equals(c.getCentroide())) {
                c.setTermino(true);
            } else {
                c.setCentroide(nuevoCentroide);
            }
        }
    }

    private void asignarPuntos(List<Punto> puntos, List<Cluster> clusters) {
        for (Punto punto : puntos) {
            Cluster masCercano = clusters.get(0);
            Double distanciaMinima = Double.MAX_VALUE;
            for (Cluster cluster : clusters) {
                Double distancia = punto.distanciaEuclideana(cluster
                        .getCentroide());
                if (distanciaMinima > distancia) {
                    distanciaMinima = distancia;
                    masCercano = cluster;
                }
            }
            masCercano.getPuntos().add(punto);
        }
    }

    private void prepararClusters(List<Cluster> clusters) {
        for (Cluster c : clusters) {
            c.limpiarPuntos();
        }
    }

    private Double calcularFuncionObjetivo(List<Cluster> clusters) {
        Double ofv = 0d;

        for (Cluster cluster : clusters) {
            for (Punto punto : cluster.getPuntos()) {
                ofv += punto.distanciaEuclideana(cluster.getCentroide());
            }
        }

        return ofv;
    }

    private Double calcularDunn(List<Cluster> clusters) {
        double dunn;
        double max = 0;
        double min = 0;

        for (Cluster cluster : clusters) {
            for (Punto punto : cluster.getPuntos()) {
                for (Cluster cluster2 : clusters) {
                    if (!cluster.equals(cluster2)) {
                        for (Punto punto2 : cluster.getPuntos()) {
                            if (!punto.equals(punto2)) {
                                double dist = punto.distanciaEuclideana(punto2);
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
            for (Punto punto : cluster.getPuntos()) {
                for (Punto punto2 : cluster.getPuntos()) {
                    if (!punto.equals(punto2)) {
                        double dist = punto.distanciaEuclideana(punto2);
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

    private Double calcularSilhouette(List<Cluster> clusters) {
        double silhouette;
        double a;
        double distA = 0;
        double b;
        double distB = 0;
        double cont = 0;

        for (Cluster cluster : clusters) {
            for (Punto punto : cluster.getPuntos()) {
                for (Cluster cluster2 : clusters) {
                    if (!cluster.equals(cluster2)) {
                        for (Punto punto2 : cluster.getPuntos()) {
                            if (!punto.equals(punto2)) {
                                distB += punto.distanciaEuclideana(punto2);

                            }
                        }
                    }
                }
            }
        }
        b = distB / clusters.size();

        cont = 0;
        for (Cluster cluster : clusters) {
            for (Punto punto : cluster.getPuntos()) {
                for (Punto punto2 : cluster.getPuntos()) {
                    if (!punto.equals(punto2)) {
                        distA += punto.distanciaEuclideana(punto2);
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

    private Double calcularDavidBouldin(List<Cluster> clusters) {
        int numberOfClusters = clusters.size();

        if (numberOfClusters == 1) {
            throw new RuntimeException(
                    "Impossible to evaluate Davies-Bouldin index over a single cluster");
        } else {
            // counting distances within
            double[] withinClusterDistance = new double[numberOfClusters];

            int i = 0;
            for (Cluster cluster : clusters) {
                for (Punto punto : cluster.getPuntos()) {
                    withinClusterDistance[i] += punto.distanciaEuclideana(cluster.getCentroide());
                }
                withinClusterDistance[i] /= cluster.getPuntos().size();
                i++;
            }


            double result = 0.0;

            for (i = 0; i < numberOfClusters; i++) {
                double max = Double.NEGATIVE_INFINITY;
                for (int j = 0; j < numberOfClusters; j++)
                    if (i != j) {
                        double val = (withinClusterDistance[i] + withinClusterDistance[j]);
                                // / distanceMeasure.measure(ClusterOperations.getCentroidCoordinates(clusters[i], distanceMeasure),
                                //ClusterOperations.getCentroidCoordinates(clusters[j], distanceMeasure));
                        if (val > max)
                            max = val;
                    }
                result = result + max;
            }
            return result / numberOfClusters;
        }

    }

    private Double calcularEntropy(List<Cluster> clusters) {
        double silhouette;
        double a;
        double distA = 0;
        double b;
        double distB = 0;
        double cont = 0;

        for (Cluster cluster : clusters) {
            for (Punto punto : cluster.getPuntos()) {
                for (Cluster cluster2 : clusters) {
                    if (!cluster.equals(cluster2)) {
                        for (Punto punto2 : cluster.getPuntos()) {
                            if (!punto.equals(punto2)) {
                                distB += punto.distanciaEuclideana(punto2);
                                cont++;
                            }
                        }
                    }
                }
            }
        }
        b = distB / cont;

        cont = 0;
        for (Cluster cluster : clusters) {
            for (Punto punto : cluster.getPuntos()) {
                for (Punto punto2 : cluster.getPuntos()) {
                    if (!punto.equals(punto2)) {
                        distA += punto.distanciaEuclideana(punto2);
                        cont++;
                    }
                }
            }
        }
        a = distA / cont;
        //System.out.println("A: " + a);
        //System.out.println("B: " + b);

        silhouette = b - a / Math.max(a, b);
        return silhouette;
    }

    private boolean finalizo(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            if (!cluster.isTermino()) {
                return false;
            }
        }
        return true;
    }

    private List<Cluster> elegirCentroides(List<Punto> puntos, Integer k) {
        List<Cluster> centroides = new ArrayList<Cluster>();

        List<Float> maximos = new ArrayList<Float>();
        List<Float> minimos = new ArrayList<Float>();
        // me fijo máximo y mínimo de cada dimensión

        for (int i = 0; i < puntos.get(0).getGrado(); i++) {
            Float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;

            for (Punto punto : puntos) {
                min = min > punto.get(i) ? punto.get(0) : min;
                max = max < punto.get(i) ? punto.get(i) : max;
            }

            maximos.add(max);
            minimos.add(min);
        }

        Random random = new Random();

        for (int i = 0; i < k; i++) {
            Float[] data = new Float[puntos.get(0).getGrado()];
            Arrays.fill(data, 0f);
            for (int d = 0; d < puntos.get(0).getGrado(); d++) {
                data[d] = random.nextFloat()
                        * (maximos.get(d) - minimos.get(d)) + minimos.get(d);
            }

            Cluster c = new Cluster();
            Punto centroide = new Punto(data);
            c.setCentroide(centroide);
            centroides.add(c);
        }

        return centroides;
    }
}
