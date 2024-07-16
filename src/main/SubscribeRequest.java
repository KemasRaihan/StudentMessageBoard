package main;

import org.json.simple.JSONObject;

public class SubscribeRequest extends Request{


    // class name to be used as tag in JSON representation
    private static final String _class =
            SubscribeRequest.class.getSimpleName();

    // channel for the account to be added on

    private Channel channel;

    // user to be added
    private User user;


    // Constructor; throws NullPointerException if name is null.
    public SubscribeRequest(Channel channel, User user) {
        if(channel == null | user==null)
            throw new NullPointerException();
        this.channel=channel;
        this.user=user;
    }
    public Channel getChannel(){return channel;}
    public User getUser(){return user;}




    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("user", user.toJSON());
        obj.put("channel", channel.toJSON());
        return obj;
    }

    // Tries to deserialize a SubscribeRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static SubscribeRequest fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            User user = User.fromJSON(obj.get("user"));
            Channel channel = Channel.fromJSON(obj.get("channel"));
            // construct the object to return (checking for nulls)
            return new SubscribeRequest(channel, user);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
