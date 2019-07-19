import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] thresholdResults; // hold each experiment's result
    private int T;

    public PercolationStats(int N, int T) {

        if (N < 1 || T < 1)
            throw new IllegalArgumentException("both arguments N and T must be greater than 1");

        //this.T = T;
        thresholdResults = new double[T];

        for (int t = 0; t < T; t++) {
            Percolation percolation = new Percolation(N);
            int openSites = 0;

            while (!percolation.percolates()) {
                int i = StdRandom.uniform(1, N + 1);
                int j = StdRandom.uniform(1, N + 1);

                if (!percolation.isOpen(i, j)) {
                    percolation.open(i, j);
                    openSites += 1;
                }

            }
            double threshold = (double) openSites / (double) (N * N);
            thresholdResults[t] = threshold;
        }
    }

    public double mean() {
        return StdStats.mean(thresholdResults);
    }

    public double stddev() {
        return StdStats.stddev(thresholdResults);
    }

    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(T));
    }

    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(T));
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.println("mean = " + stats.mean());
        StdOut.println("standard deviation = " + stats.stddev());
        StdOut.println(
                "95% confidence interval = " + stats.confidenceLo() + " , " + stats.confidenceHi());
    }

}
