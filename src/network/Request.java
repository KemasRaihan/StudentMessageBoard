package network;// Week 18 Exercises

// compile: javac -cp json-simple-1.1.1.jar;. Request.java

import org.json.simple.*;

// Abstract super class for all requests
public abstract class Request implements JSONAware {
    // Serializes this object into a JSONObject
    public abstract Object toJSON();

    // Serializes this object and returns the JSON as a string
    public String toJSONString() { return toJSON().toString(); }

    // Prints this object in JSON representation
    public String toString() { return toJSONString(); }
}
