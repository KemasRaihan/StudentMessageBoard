package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Channel {

    private static final String _class =
            Channel.class.getSimpleName();

    // name of the channel
    private String name;

    // Map of users that have subscribed of this channel where the key is the account and
    // the corresponding integer value is the number of messages the user have already read from the message board
    private Map<User, Integer> subscribers = new HashMap<>();

    // List of messages that have been posted in this channel
    private List<Message> board = new ArrayList<Message>();



    // shared logical clock
    private static Clock clock = new Clock();

    static class Clock {
        private long t;

        public Clock() { t = 0; }

        // tick the clock and return the current time
        public synchronized long tick() { return ++t; }
    }

    public Channel(String name, Map<User, Integer> subscribers, List<Message> board){
        if(name==null | subscribers==null | board==null){
            throw new NullPointerException();
        }
        this.name = name;
        this.subscribers=subscribers;
        this.board=board;

    }

    void addToSubscribers(User subscriber){
        subscribers.put(subscriber, 0);
    }
    public void addToBoard(Message message){
        board.add(message);
    }

    // check if user has subscribed already
    boolean check(String name)
    {
        for(User account : subscribers.keySet()){
            if(account.getName()==name)
                return true;
        }
        return false;
    }

    public String getName(){return name;}
    public Map<User, Integer> getSubscribers(){return subscribers;}
    public List<Message> getBoard(){return board;}

    public int getNumberOfReads(String name){
        for(User user : subscribers.keySet()){
            if(user.getName()==name)
                return subscribers.get(name);
        }
        return -1;
    }



    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class",    _class);

        obj.put("name",      name);

        // serialize list of accounts in 'subscribers' to a JSON array
        JSONArray jsonAccs = new JSONArray();
        for(User account : subscribers.keySet()){
            jsonAccs.add(account.toJSON());
        }
        obj.put("accounts" , jsonAccs);
        // serialize list of number of read messages in 'subscribers' to a JSON array
        JSONArray jsonRead = new JSONArray();
        for(int read : subscribers.values()){
            jsonRead.add(read);
        }
        obj.put("reads" , jsonRead);

        // serialize list of messages in 'board' to a JSON Array
        JSONArray jsonBoard = new JSONArray();
        for(Message message : board){
            jsonBoard.add(message.toJSON());
        }
        obj.put("board",    jsonBoard);
        return obj;
    }

    // Tries to deserialize a Message instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static Channel fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;

            // deserialize channel name
            String name = (String)obj.get("name");

            // deserialize accounts from JSONArray

            JSONArray accsArray = (JSONArray)obj.get("susbcribers");
            List<User> accounts = new ArrayList<>();

            for(int i = 0; i < accounts.size(); i++){
               accounts.add(User.fromJSON(accsArray.get(i)));
            }

            // deserialize number of messages, read from each account, from JSONArray
            int[] reads = new int[accounts.size()];
            JSONArray readArray = (JSONArray)obj.get("reads");

            for(int i = 0; i < reads.length; i++){
                reads[i] = (int) readArray.get(i);
            }

            Map<User, Integer> subscribers = new HashMap<>();
            for(int i = 0; i < accounts.size(); i++)
                subscribers.put(accounts.get(i), reads[i]);

            // deserialize messages from JSONArray
            ArrayList<Message> board = new ArrayList<>();
            JSONArray boardArray = (JSONArray)obj.get("board");
            for (Object msg_obj : boardArray)
                board.add(Message.fromJSON(msg_obj));

            return new Channel(name, subscribers, board);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }

}
