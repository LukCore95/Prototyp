package zpi.data.model;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by Ania on 2017-05-23.
 */

public class PointAlphabeticComparator implements Comparator<Point> {
    @Override
    public int compare(@NonNull Point o1,@NonNull Point o2){
        return o1.getName().compareTo(o2.getName());
    }
}
