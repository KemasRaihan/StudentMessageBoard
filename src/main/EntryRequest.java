package main;// Solution to Week 8 Homework Exercise 4

// compile: javac -cp json-simple-1.1.1.jar;. LoginRequest.java

import org.json.simple.*;

public class EntryRequest extends Request {
    // class name to be used as tag in JSON representation
    private static final String _class =
            EntryRequest.class.getSimpleName();


    // Constructor; throws NullPointerException if name is null.
    public EntryRequest(){
    }

    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        return obj;
    }

    // Tries to deserialize a EntryRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static EntryRequest fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            return new EntryRequest();
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}