package aero.sita.iborders.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class DataProviderUtil {

    @DataProvider(name="LoginData")
    public Object[][] getData() {

        return new Object[][] {{"naveenpathaktest@gmail.com", "Sonu123@#"},
                               {"naveenpathaktest@gmail.com", "Sonu123@#"}};
    }

    @DataProvider(name="HashMapData")
    public Object[][] hashMapData() {

        HashMap<String, Object> user1 = new HashMap<>();
        user1.put("email", "naveenpathak715@gmail.com");
        user1.put("password", "Sonu123@#");

        HashMap<String, Object> user2 = new HashMap<>();
        user2.put("username", "naveenpathaktest@gmail.com");
        user2.put("password", "Sonu123@#");

        return new Object[][] {{user1}, {user2}};
    }

//    @DataProvider(name="eventBookingData")
//    public Object[][] eventBookingData(String filePath) throws IOException {
//
//        return getJsonDataToMap(filePath);
//    }

    @DataProvider(name="eventBookingData")
    public Object[][] eventBookingData() throws IOException {
        return getJsonDataToMap(System.getProperty("user.dir") + "/src/test/resources/eventBookingData.json");
    }

    public static Object[][] getJsonDataToMap(String filePath) throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        Type type = new TypeToken<List<HashMap<String, String>>>() {}.getType();
        List<HashMap<String, String>> list = new Gson().fromJson(jsonContent, type);

        Object[][] table = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            table[i][0] = list.get(i);
        }
        return table;
    }
}
