package main;

import org.json.simple.JSONObject;

public class CreateChannelRequest extends Request{
    // class name to be used as tag in JSON representation
    private static final String _class =
            CreateChannelRequest.class.getSimpleName();

    // user to be added
    private Channel channel;

    // Constructor; throws NullPointerException if name is null.
    public CreateChannelRequest(Channel channel) {
        if(channel==null)
            throw new NullPointerException();
        this.channel=channel;
    }

    public Channel getChannel(){return channel;}



    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // convert channel object to JSON object
        Object channelObj = channel.toJSON();
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("channel", channelObj);
        return obj;
    }

    // Tries to deserialize a CreateChannelRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static CreateChannelRequest fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            Channel channel = Channel.fromJSON(obj.get("channel"));
            // construct the object to return (checking for nulls)
            return new CreateChannelRequest(channel);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
