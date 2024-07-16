package main;// Solution to Week 8 Homework Exercise 4

// compile: javac -cp json-simple-1.1.1.jar;. LoginRequest.java

import org.json.simple.*;

public class ViewChannelRequest extends Request {
    // class name to be used as tag in JSON representation
    private static final String _class =
            ViewChannelRequest.class.getSimpleName();

    private Channel channel;


    // Constructor; throws NullPointerException if name is null.
    public ViewChannelRequest(Channel channel){
        if(channel == null)
            throw new NullPointerException();
        this.channel=channel;
    }

    public Channel getChannel(){return channel;}


    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("channel", channel.toJSON());
        return obj;
    }

    // Tries to deserialize a LoginRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static ViewChannelRequest fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            Channel channel = Channel.fromJSON(obj.get("channel"));
            return new ViewChannelRequest(channel);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}