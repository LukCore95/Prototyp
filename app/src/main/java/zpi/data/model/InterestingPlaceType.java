package zpi.data.model;


public enum InterestingPlaceType {
    sakralny, kultury;

    public static InterestingPlaceType fromIntToType(int i){
        InterestingPlaceType type = null;
        switch(i){
            case 0:
                type = InterestingPlaceType.sakralny;
                break;
            case 1:
                type = InterestingPlaceType.kultury;
                break;
        }

        return type;
    }

    public static int fromTypeToInt(InterestingPlaceType type){
        int i = -1;
        switch(type){
            case sakralny:
                i = 0;
                break;
            case kultury:
                i = 1;
                break;
        }

        return i;
    }

    public static String fromTypeToString(InterestingPlaceType type){ //TODO przenieść opisy do strings.xml?
        String str = "";
        switch(type){
            case sakralny:
                str = "Obiekt sakralny";
                break;
            case kultury:
                str = "Obiekt kultury";
                break;
        }

        return str;
    }
}
