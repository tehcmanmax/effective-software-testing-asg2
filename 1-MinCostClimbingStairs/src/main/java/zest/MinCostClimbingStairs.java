package zest;

public class MinCostClimbingStairs {

      public int minCostClimbingStairs(int[] cost) {

        if (cost.length < 2 || cost.length > 1000){
            throw new IllegalArgumentException("Array length must be between 2 and 1000");
        }

        for (int i : cost){
            if(i < 0 || i > 999){
                throw new IllegalArgumentException("Cost must be between 0 and 999");
            }
        }


        int n = cost.length;
        int[] dp = new int[n];
        dp[0] = cost[0];
        dp[1] = cost[1];

        for (int i = 2; i < n; i++) {
            dp[i] = cost[i] + Math.min(dp[i - 1], dp[i - 2]);
        }

        int minCost = Math.min(dp[n - 1], dp[n - 2]);

        if(minCost < 0 || minCost > cost.length * 999){
            throw new IllegalArgumentException("Minimum cost must be between 0 and " + (cost.length * 999));
        }

        return minCost;}
}
