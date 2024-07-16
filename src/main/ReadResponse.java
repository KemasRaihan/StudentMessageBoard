package main;// Solution to Week 8 Home Exercise 4

// compile: javac -cp json-simple-1.1.1.jar;. ReadResponse.java

import org.json.simple.*;

import java.util.ArrayList;
import java.util.List;

public class ReadResponse extends Response {
    // class name to be used as tag in JSON representation
    private static final String _class =
            ReadResponse.class.getSimpleName();

    private User user;

    // Constructor; throws NullPointerException if messages contains nulls.
    public ReadResponse(User user) {
        // check for nulls
        if (user == null)
            throw new NullPointerException();
        this.user = user;
    }

    public int findRead(String subscriber, Channel channel){
        return channel.getNumberOfReads(subscriber);
    }
    public void readMessages(){
        List<Channel> channels = user.getChannels();
        for(Channel channel : channels) {
            int numberOfReads = channel.getNumberOfReads(user.getName());
            List<Message> board = channel.getBoard();
            for (int i = numberOfReads; i < board.size(); i++) {
                System.out.println(board.get(i).getAuthor() + ": " + board.get(i).getBody() + "(sent at " + board.get(i).getTimestamp() + ")");
            }
            channel.getSubscribers();
        }

    }
    User getUser() {return user;}

    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // serialize messages into a JSONArray
        JSONArray arr = new JSONArray();
        // serialize this as a JSONObject
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("user", user);
        return obj;
    }

    // Tries to deserialize a ReadResponse instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static ReadResponse fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            // deserialize messages from JSONArray
            JSONArray arr = (JSONArray)obj.get("messages");
            User user = User.fromJSON(obj.get("user"));

            // construct the object to return (checking for nulls)
            return new ReadResponse(user);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
