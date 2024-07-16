package main;

import org.json.simple.JSONObject;

import java.util.Scanner;

public class WriteRequest extends Request{

    Scanner input = new Scanner(System.in);

    // class name to be used as tag in JSON representation
    private static final String _class =
            WriteRequest.class.getSimpleName();

    // channel for the account to be added on

    private Channel channel;

    // author
    private String author;

    // message to be added to board
    private Message message;


    // Constructor; throws NullPointerException if name is null.
    public WriteRequest(Channel channel, String author, Message message) {
        if(channel == null | author==null)
            throw new NullPointerException();
        this.channel=channel;
        this.author=author;
        this.message=message;
    }
    public Channel getChannel(){return channel;}
    public String getAuthor(){return author;}
    public Message getMessage(){return message;}

    public void writeMessage(){
        System.out.print("Write message to " + channel.getName() + " board: ");
        String body = input.nextLine();
        String time = java.time.LocalTime.now().toString();
        this.message= new Message(body, author, time);
    }





    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("channel", channel.toJSON());
        obj.put("author", author);
        obj.put("message", message.toJSON());
        return obj;
    }

    // Tries to deserialize a SubscribeRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static WriteRequest fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            String author = (String) obj.get("author");
            Channel channel = Channel.fromJSON(obj.get("channel"));
            Message message = Message.fromJSON(obj.get("message"));
            //Message message = Message.fromJSON(obj.get("message"));
            // construct the object to return (checking for nulls)
            return new WriteRequest(channel, author, message);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
