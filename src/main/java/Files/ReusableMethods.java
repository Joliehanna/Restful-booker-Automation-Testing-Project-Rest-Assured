package Files;

import io.restassured.path.json.JsonPath;

public class ReusableMethods {
    public static JsonPath RawToJson(String response){
        JsonPath js1 =new JsonPath(response);
        return js1;
    }
}