package zpi.data.model;

/**
 * Created by Ania on 2017-04-24.
 */

public enum RestPointType {
    pub,
    restaurant,
    cafe;

    public static RestPointType fromIntToType(int i){
        RestPointType type = null;
        switch(i){
            case 0:
                type = RestPointType.pub;
                break;
            case 1:
                type = RestPointType.restaurant;
                break;
            case 2:
                type = RestPointType.cafe;
                break;
        }

        return type;
    }

    public static int fromTypeToInt(RestPointType type){
        int i = -1;
        switch(type){
            case pub:
                i = 0;
                break;
            case restaurant:
                i = 1;
                break;
            case cafe:
                i = 2;
                break;
        }

        return i;
    }
}
