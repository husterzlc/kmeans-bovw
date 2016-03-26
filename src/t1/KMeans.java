package t1;

import java.util.*;

public class KMeans {
    protected int countAllFeatures = 0, numClusters = 256;
    public Cluster[] clusters = null;

    public KMeans() {

    }

    public KMeans(int numClusters) {
        this.numClusters = numClusters;
    }

    public void begin(ArrayList<int[]> final_features) {
        
        countAllFeatures += final_features.size();
        init(final_features);
    }

    public int getFeatureCount() {
        return countAllFeatures;
    }

    // find first clusters:
    public void init(ArrayList<int[]> final_features)
    {
        clusters = new Cluster[numClusters];
        for (int i = 0; i < clusters.length; i++) {
        	int tmpIndex = (int) Math.floor(Math.random() * countAllFeatures);
            clusters[i] = new Cluster(new int[final_features.get(tmpIndex).length]);  
            System.arraycopy(final_features.get(tmpIndex), 0, clusters[i].mean, 0, final_features.get(tmpIndex).length);
        }
    }

    public double clusteringStep(ArrayList<int[]> final_features) {
        for (int i = 0; i < clusters.length; i++) {
            clusters[i].members.clear();
        }
        reOrganizeFeatures(final_features);
        recomputeMeans( final_features);
        return overallStress(final_features);
    }

    protected void reOrganizeFeatures(ArrayList<int[]> final_features) {
        for (int k = 0; k < final_features.size(); k++) {
            int[] f = final_features.get(k);
            Cluster best = clusters[0];
            double minDistance = clusters[0].getDistance(f);
            for (int i = 1; i < clusters.length; i++) {
                double v = clusters[i].getDistance(f);
                if (minDistance > v) {
                    best = clusters[i];
                    minDistance = v;
                }
            }
            best.members.add(k);
        }
    }

    /**
     * Computes the mean per cluster (averaged vector)
     */
    protected void recomputeMeans(ArrayList<int[]> final_features) {
        int length = final_features.get(0).length;
        for (int i = 0; i < clusters.length; i++) {
            Cluster cluster = clusters[i];
            int[] mean = cluster.mean;
            for (int j = 0; j < length; j++) {
                mean[j] = 0;
                for (Integer member : cluster.members) {
                    mean[j] += final_features.get(member)[j];
                }
                if (cluster.members.size() > 1)
                    mean[j] = mean[j] / (int) cluster.members.size();
            }
            if (cluster.members.size() == 1) {
                System.err.println("** There is just one member in cluster " + i);
            } else if (cluster.members.size() < 1) {
                System.err.println("** There is NO member in cluster " + i);
                // fill it with a random member?!?
                int index = (int) Math.floor(Math.random()*final_features.size());
                System.arraycopy(final_features.get(index), 0, clusters[i].mean, 0, clusters[i].mean.length);
            }

        }
    }

    /**
     * Squared error in classification.
     *
     * @return
     */
    protected double overallStress(ArrayList<int[]> final_features) {
        double v = 0;
        int length = final_features.get(0).length;
        for (int i = 0; i < clusters.length; i++) {
            for (Integer member : clusters[i].members) {
                float tmpStress = 0;
                for (int j = 0; j < length; j++) {
//                    if (Float.isNaN(features.get(member).descriptor[j])) System.err.println("Error: there is a NaN in cluster " + i + " at member " + member);
                    tmpStress += Math.abs(clusters[i].mean[j] -final_features.get(member)[j]);
                }
                v += tmpStress;
            }
        }
        return v;
    }

    public Cluster[] getClusters() {
        return clusters;
    }


    /**
     * Set the number of desired clusters.
     *
     * @return
     */
    public int getNumClusters() {
        return numClusters;
    }

    public void setNumClusters(int numClusters) {
        this.numClusters = numClusters;
    }

 
}
