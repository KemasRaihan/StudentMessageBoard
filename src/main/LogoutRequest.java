package main;

import org.json.simple.JSONObject;

public class LogoutRequest extends Request{
    private static final String _class =
            LogoutRequest.class.getSimpleName();



    // Constructor; throws NullPointerException if name is null.
    public LogoutRequest(){

    }

    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        return obj;
    }

    // Tries to deserialize a LoginRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static LogoutRequest fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            return new LogoutRequest();
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
