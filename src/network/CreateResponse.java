package network;

import org.json.simple.JSONObject;

public class CreateResponse extends Response{
    // class name to be used as tag in JSON representation
    private static final String _class =
            LoginRequest.class.getSimpleName();


    // Constructor; throws NullPointerException if name is null.
    public CreateResponse() {}


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
    public static CreateResponse fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;

            // construct the object to return (checking for nulls)
            return new CreateResponse();
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
