package main;// Solution to Week 8 Homework Exercise 4

// compile: javac -cp json-simple-1.1.1.jar;. LoginRequest.java

import org.json.simple.*;

import java.util.Scanner;

public class ViewChannelResponse extends Response{
    Scanner input = new Scanner(System.in);
    // class name to be used as tag in JSON representation
    private static final String _class =
            ViewChannelResponse.class.getSimpleName();

    private Channel channel;

    public void displayOptions(){
        System.out.println("------------------");
        System.out.println(channel.getName());
        System.out.println("-----------------");
        System.out.println("Write the following options: ");
        System.out.println("'write' - to write to message board");
        System.out.println("'subscribe' - subscribe to channel");
        System.out.println("'unsubscribe' - unsubscribe from channel");
        System.out.println("'return' - return to main menu");
        System.out.print("\nEnter your option: ");
    }

    // Constructor; throws NullPointerException if name is null.
    public ViewChannelResponse(Channel channel) {
        if(channel==null)
            throw new NullPointerException();
        this.channel = channel;
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
    public static ViewChannelResponse fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;

            Channel channel = Channel.fromJSON(obj.get("channel"));
            // construct the object to return (checking for nulls)
            return new ViewChannelResponse(channel);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}