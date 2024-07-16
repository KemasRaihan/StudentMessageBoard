package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewChannelsResponse extends Response{
    // class name to be used as tag in JSON representation
    private static final String _class =
            ReadResponse.class.getSimpleName();

    // list of channels to be view
    private List<Channel> channels;

    ModifiedScanner input = new ModifiedScanner();

    // Constructor; throws NullPointerException if messages contains nulls.
    public ViewChannelsResponse(List<Channel> channels) {
        // check for nulls
        if (channels == null || channels.contains(null))
            throw new NullPointerException();
        this.channels = channels;
    }

    List<Channel> getChannels() { return channels; }

    public void view(){
        // print every channel available
        for (int i = 0; i < channels.size(); i++)
            System.out.println((i + 1) + ". " + channels.get(i).getName()); // display channel name
    }

    public Channel chooseChannel(){
        System.out.print("Please select a channel (or enter 0 to return to main menu): ");
        int option = input.choose();
        if(option==0)
            return null;
        return channels.get(option-1);
    }



    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // serialize messages into a JSONArray
        JSONArray arr = new JSONArray();
        for (Channel channel : channels)
            arr.add(channel.toJSON());
        // serialize this as a JSONObject
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("channels", arr);
        return obj;
    }

    // Tries to deserialize a ReadResponse instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static ViewChannelsResponse fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            // deserialize messages from JSONArray
            JSONArray arr = (JSONArray)obj.get("channels");
            List<Channel> channels = new ArrayList<>();
            for (Object channel_obj : arr)
                channels.add(Channel.fromJSON(channel_obj));
            // construct the object to return (checking for nulls)
            return new ViewChannelsResponse(channels);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
