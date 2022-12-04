package network;// Solution to Week 8 Homework Exercise 4

// compile: javac -cp json-simple-1.1.1.jar;. LoginRequest.java

import org.json.simple.*;

public class LoginRequest extends Request {
    // class name to be used as tag in JSON representation
    private static final String _class =
            LoginRequest.class.getSimpleName();

    private String login;
    private String password;

    // Constructor; throws NullPointerException if name is null.
    public LoginRequest(String login, String password) {
        // check for null
        if (login == null)
            throw new NullPointerException();
        this.login = login;
        this.password = password;
    }

    String getLogin() { return login; }
    String getPassword() { return password; }

    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("login", login);
        obj.put("password", password);
        return obj;
    }

    // Tries to deserialize a LoginRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static LoginRequest fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            // deserialize login name
            String login = (String)obj.get("login");
            // deserialize password
            String password = (String)obj.get("password");
            // construct the object to return (checking for nulls)
            return new LoginRequest(login, password);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
