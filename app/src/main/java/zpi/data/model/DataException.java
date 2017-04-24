package zpi.data.model;

/**
 * Created by Ania on 2017-04-24.
 */

public class DataException extends Exception {
    public String attribute;
    public String violatedConstraint;

    public DataException(String attribute, String violatedConstraint) {
        super();
        this.attribute = attribute;
        this.violatedConstraint = violatedConstraint;
    }

    @Override
    public String toString(){
        return "Invalid data! /n" +
                "attribute: " + attribute +
                "has violated constraint: " + violatedConstraint + "!";
    }
}
