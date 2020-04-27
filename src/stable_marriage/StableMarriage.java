package stable_marriage;

import java.util.Arrays;

public class StableMarriage {

    int[] solveStableMarriage(int[][] boys, int[][] girls) {
        int n = boys.length;
        int[] prefIndex = new int[n];
        int[] matched = new int[n];
        boolean[] boyEngaged = new boolean[n];

        Arrays.fill(matched, -1);

        int unmarried = n;
        boolean rejected = true;

        while (unmarried > 0 || rejected)  {
            rejected = false;
            for (int i =0;  i < n; ++i) {
                if(boyEngaged[i]) continue;

                int prefGirl = boys[i][prefIndex[i]];
                if (matched[prefGirl] == -1 || guyBetter4Gal(girls[prefGirl], i, matched[prefGirl])) { // then this current guy is the better suitror for the prefGal
                    int prevGuy = matched[prefGirl];
                    if (prevGuy > -1) {
                        prefIndex[prevGuy]++;
                        boyEngaged[prevGuy] = false;
                    } else
                        unmarried--;
                    matched[prefGirl] = i;
                    boyEngaged[i] = true;
                } else {
                    prefIndex[i]++;
                }
                rejected = true;
            }
        }
        System.out.println(Arrays.toString(matched));
        int[] ans = new int[n];
        for (int i = 0; i < n; ++i) {
            ans[i] = boys[i][prefIndex[i]];
        }
        return ans;
    }

    private boolean guyBetter4Gal(int[] girl, int newGuy, int prevGuy) {
        if (newGuy == prevGuy) return true;
        for (int guy : girl) {
            if (guy == newGuy)
                return true;
            if (guy == prevGuy)
                return false;
        }
        return false;
    }


    private void printMarriageOptions(int[] boyChoices) {
        for (int i = 0; i < boyChoices.length; ++i) {
            System.out.printf("Boy %d married to girl %d\n", i, boyChoices[i]);
        }
    }

    public static void main(String[] args) {
        StableMarriage sm = new StableMarriage();
        int[][] boys = {{3, 1, 2, 0},
                {1, 0, 2, 3},
                {0, 1, 2, 3},
                {0, 1, 2, 3}};
        int[][] girls = {{0, 1, 2, 3},
                {0, 1, 2, 3},
                {0, 1, 2, 3},
                {0, 1, 2, 3}};
        sm.printMarriageOptions(sm.solveStableMarriage(boys, girls));
    }
}
