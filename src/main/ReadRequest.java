package main;// Solution to Week 8 Homework Exercise 4

// compile: javac -cp json-simple-1.1.1.jar;. ReadRequest.java

import org.json.simple.*;

public class ReadRequest extends Request {
    // class name to be used as tag in JSON representation
    private static final String _class =
            ReadRequest.class.getSimpleName();

    private User user;
    // Constructor; no arguments as there are no instance fields
    public ReadRequest(User user) {
        if(user==null)
            throw new NullPointerException();
        this.user=user;
    }

    public User getUser(){return user;}

    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("user", user);
        return obj;
    }

    // Tries to deserialize a ReadRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static ReadRequest fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            User user = User.fromJSON(obj.get("user"));
            // construct the new object to return
            return new ReadRequest(user);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
