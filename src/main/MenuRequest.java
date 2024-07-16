package main;

import org.json.simple.JSONObject;

public class MenuRequest extends Request{
    // class name to be used as tag in JSON representation
    private static final String _class =
           MenuRequest.class.getSimpleName();

    private User user;

    // Constructor; throws NullPointerException if name is null.
    public MenuRequest(User user) {
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
        obj.put("user", user.toJSON());
        return obj;
    }

    // Tries to deserialize a LoginRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static MenuRequest fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            User user = User.fromJSON(obj.get("user"));
            // construct the object to return (checking for nulls)
            return new MenuRequest(user);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
