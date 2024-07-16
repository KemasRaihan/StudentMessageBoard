package main;

import org.json.simple.JSONObject;

import java.util.Scanner;

public class WriteResponse extends Response{
    Scanner input = new Scanner(System.in);
    // class name to be used as tag in JSON representation
    private static final String _class =
            WriteResponse.class.getSimpleName();

    private Channel channel;
    private String author;

    // Constructor; throws NullPointerException if name is null.
    public WriteResponse(Channel channel, String author) {
        if(channel==null|author==null)
            throw new NullPointerException();
        this.channel=channel;
        this.author=author;
    }




    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("channel", channel.toJSON());
        obj.put("author", author);
        return obj;
    }

    // Tries to deserialize a LoginRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static WriteResponse fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            Channel channel = Channel.fromJSON(obj.get("channel"));
            String author = (String) obj.get("author");
            // construct the object to return (checking for nulls)
            return new WriteResponse(channel, author);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
