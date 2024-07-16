package main;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CreateChannelResponse extends Response{
    Scanner input = new Scanner(System.in);
    // class name to be used as tag in JSON representation
    private static final String _class =
            CreateChannelResponse.class.getSimpleName();


    // Constructor; throws NullPointerException if name is null.
    public CreateChannelResponse() {}


    public Channel createNewChannel(){
        System.out.print("Enter name of the channel: ");
        String name = input.nextLine();
        Map<User, Integer> subscribers = new HashMap<>();
        ArrayList<Message> board = new ArrayList<>();
        return new Channel(name, subscribers, board);
    }


    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);

        return obj;
    }

    // Tries to deserialize a LoginRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static CreateChannelResponse fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;

            // construct the object to return (checking for nulls)
            return new CreateChannelResponse();
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }

}
