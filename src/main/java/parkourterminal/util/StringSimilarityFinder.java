package parkourterminal.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class StringSimilarityFinder {
    public static int computeLevenshteinDistance(String a, String b) {
        a=a.toLowerCase();
        b=b.toLowerCase();
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[a.length()][b.length()];
    }

    public static int SimilarSubstringDistance(String query, String target,int maxDistance) {
        int qLen = query.length();
        int minDist=Integer.MAX_VALUE;
        for (int i = 0; i <= target.length() - qLen; i++) {
            String sub = target.substring(i, i + qLen);
            int dist = computeLevenshteinDistance(query, sub);
            if (dist <= maxDistance) {
                minDist=Math.min(minDist,dist);
            }
        }
        return minDist;
    }
    public static List<String> findSimilar(final String query, List<String> words, final int maxDistance) {
        List<String> result = new ArrayList<String>();
        for (String word : words) {
            if (SimilarSubstringDistance(query, word, maxDistance) <= maxDistance) {
                result.add(word);
            }
        }

        Collections.sort(result, new Comparator<String>() {
            @Override
            public int compare(String word1, String word2) {
                int dist1 = SimilarSubstringDistance(query, word1,maxDistance);
                int dist2 = SimilarSubstringDistance(query, word2,maxDistance);
                return dist1>dist2?1:dist1==dist2?0:-1;
            }
        });

        return result;
    }
}
