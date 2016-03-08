package jonghoonlim.two_oh.dataStructures;

import java.util.Comparator;

/**
 * Created by jhl2298 on 3/8/2016.
 */
public class CustomComparator implements Comparator<Item> {

    @Override
    public int compare (Item firstItem, Item secondItem) {
        int firstItemNo = 0;
        int secondItemNo = 0;
        try {
            firstItemNo = Integer.parseInt(firstItem.getUtTag());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            secondItemNo = Integer.parseInt(secondItem.getUtTag());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return firstItemNo - secondItemNo;
    }

}
