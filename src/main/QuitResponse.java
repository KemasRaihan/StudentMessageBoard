package main;

import org.json.simple.JSONObject;

public class QuitResponse extends Response{
    // class name to be used as tag in JSON representation
    private static final String _class =
            QuitResponse.class.getSimpleName();

    public String getMessage(){
        return "Goodbye";
    }

    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        return obj;
    }

    public static QuitResponse fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            // check for _class field matching class name
            if (!_class.equals(obj.get("_class")))
                return null;

            // construct the object to return (checking for nulls)
            return new QuitResponse();
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
