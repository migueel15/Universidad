import java.util.ArrayList;
import java.util.List;

public class Solution {
    public Solution(){

    }
    public int maxArea(int[] height) {
        int area = 0;

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < height.length; i++) {
            list.add(height[i]);
        }

        int ha;
        int hb;
        int distance = 0;

        for (int i = 0; i < height.length; i++) {
            ha = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                hb = list.get(j);
                distance = j - i;
                if (ha < hb) {
                    if (area < ha * distance) {
                        area = ha * distance;
                    }
                } else {
                    if (area < hb * distance) {
                        area = hb * distance;
                    }
                }
            }
        }
        return area;
    }
}
