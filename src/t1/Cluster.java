 
package t1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;


public class Cluster implements Comparable<Object> {
    public int[] mean;
    HashSet<Integer> members = new HashSet<Integer>();

    private double stress = 0;

    public Cluster() {
        this.mean = new int[4 * 4 * 8];
        Arrays.fill(mean, 0);
    }

    public Cluster(int[] mean) {
        this.mean = mean;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(512);
        for (Integer integer : members) {
            sb.append(integer);
            sb.append(", ");
        }
        for (int i = 0; i < mean.length; i++) {
            sb.append(mean[i]);
            sb.append(';');
        }
        return sb.toString();
    }

    public int compareTo(Object o) {
        return ((Cluster) o).members.size() - members.size();
    }



    public double getDistance(int[] f) {
//        L1
//        return MetricsUtils.distL1(mean, f);

//        L2
        return distL2(mean, f);
    }
    
    public static int distL2(int[] h1, int[] h2) {
        assert (h1.length == h2.length);
        int sum = 0;
        for (int i = 0; i < h1.length; i++) {
            sum += (h1[i] - h2[i]) * (h1[i] - h2[i]);
        }
        return (int)(Math.sqrt(sum));
    }

 

    public double getStress() {
        return stress;
    }

    public void setStress(double stress) {
        this.stress = stress;
    }

    public HashSet<Integer> getMembers() {
        return members;
    }

    public void setMembers(HashSet<Integer> members) {
        this.members = members;
    }

    /**
     * Returns the cluster mean
     *
     * @return the cluster mean vector
     */
    public int[] getMean() {
        return mean;
    }
}
