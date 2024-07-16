package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static final String _class =
           User.class.getSimpleName();
    private String name;

    // list of channels that the user has subscribed to
    private List<Channel> channels;

    // Constructor; throws NullPointerException if arguments are null
    public User(String name, List<Channel> channels) {
        if (name == null || channels==null || channels.contains(null))
            throw new NullPointerException();
        this.name      = name;
        this.channels=channels;
    }

    public String getName()      { return name; }
    public List<Channel> getChannels() {return channels;}

    public void addToChannels(Channel channel){channels.add(channel);}


//    public String toString() {
//        return  + ": " + body + " (" + timestamp + ")";
//    }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class",    _class);
        obj.put("name",      name);
        JSONArray arr = new JSONArray();
        for (Channel channel : channels)
            arr.add(channel.toJSON());
        // serialize this as a JSONObject
        obj.put("channels", arr);
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
            // deserialize message fields (checking timestamp for null)
            String name      = (String)obj.get("name");
            // construct the object to return (checking for nulls)
            JSONArray arr = (JSONArray)obj.get("channels");
            List<Channel> channels = new ArrayList<>();
            for (Object channel : arr)
                channels.add(Channel.fromJSON(channel));
            // construct the object to return (checking for nulls)
            return new User(name, channels);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
