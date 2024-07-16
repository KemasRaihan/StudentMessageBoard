package main;

import org.json.simple.JSONObject;

import java.util.Scanner;

public class MenuResponse extends Response{
    Scanner input = new Scanner(System.in);
    // class name to be used as tag in JSON representation
    private static final String _class =
            MenuResponse.class.getSimpleName();


    private User user;
    // Constructor; throws NullPointerException if name is null.
    public MenuResponse(User user)
    {
        if(user==null)
            throw new NullPointerException();
        this.user=user;
    }


    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("user", user.toJSON());
        return obj;
    }

    public void displayOptions()
    {
        System.out.println("-----");
        System.out.println("Menu");
        System.out.println("-----");
        System.out.println("Write the following options:");
        System.out.println("'read' - read from subscribed channels.");
        System.out.println("'create' - create a new channel.");
        System.out.println("'view' - view existing channels.");
        System.out.println("'logout' - logout from account.");
        System.out.print("\nEnter your option: ");
    }

    // Tries to deserialize a LoginRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static MenuResponse fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;

            User user = User.fromJSON(obj.get("user"));
            // construct the object to return (checking for nulls)
            return new MenuResponse(user);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
