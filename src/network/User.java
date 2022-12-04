package network;

import org.json.simple.JSONObject;

public class User {
    private String name;
    private String password;

    private static final String _class =
            User.class.getSimpleName();

    public User(String name, String password) {
        if(name==null || password==null)
            throw new NullPointerException();
        this.name=name;
        this.password=password;
    }
    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class",    _class);
        obj.put("name",      name);
        obj.put("password",    password);
        return obj;
    }

    // Tries to deserialize a Message instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static User fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            // deserialize user fields
            String name     = (String)obj.get("name");
            String password    = (String)obj.get("password");
            // construct the object to return (checking for nulls)
            return new User(name, password);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
