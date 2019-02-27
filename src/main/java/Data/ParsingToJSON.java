package Data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ParsingToJSON {


    JSONParser jsParser;
    JSONObject jsonObject;
    HashMap<String, String> data;
    public ParsingToJSON(String fileName) {
        try {
            jsParser = new JSONParser();
            jsonObject = (JSONObject) jsParser
                    .parse(new FileReader(System.getProperty("user.dir") + File.separator + "resources" + File.separator + fileName + ".json"));
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getData(String object) {
        data = new HashMap<String, String>();
        data = ((HashMap<String, String>) jsonObject.get(object));
        return data;
    }
}
