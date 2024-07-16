package main;// Solution to Week 8 Homework Exercise 4

// compile: javac -cp json-simple-1.1.1.jar;. LoginRequest.java

import org.json.simple.*;

import java.util.ArrayList;
import java.util.Scanner;

public class LoginResponse extends Response{
    Scanner input = new Scanner(System.in);
    // class name to be used as tag in JSON representation
    private static final String _class =
            LoginResponse.class.getSimpleName();


    String name;
    // Constructor; throws NullPointerException if name is null.
    public LoginResponse(String name) {
        if(name==null)
            throw new NullPointerException();
        this.name=name;
    }


    // Serializes this object into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("name", name);
        return obj;
    }

    public String enterDetails(){
        System.out.print("Enter your name to be signed in: ");
        String name = input.next();
        return name;
    }

    // Tries to deserialize a LoginRequest instance from a JSONObject.
    // Returns null if deserialization was not successful (e.g. because a
    // different object was serialized).
    public static LoginResponse fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;
            String name = (String) obj.get("name");
            // construct the object to return (checking for nulls)
            return new LoginResponse(name);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
