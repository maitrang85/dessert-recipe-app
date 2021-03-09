package fi.group6.dessertrecipeapp.classes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;
/**
 * Convert from raw data in json to list of object string and vice versa
 * @author: Trang
 * @version: 1.2
 * REFERENCES:
 * https://stackoverflow.com/questions/56077525/java-android-room-list-in-class-cannot-figure-out-how-to-save-this-field-into-da
 * https://www.tutorialspoint.com/gson/gson_serialization_examples.htm
 */
public class DataConverter {
    /**
     * This function to convert JSON string to list of objects (De-serialization)
     * @param value json string
     * @return List<String> list of object strings
     */
    @TypeConverter
    public static List<String> fromString(String value) {
        //Get the type of the list
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    /**
     * This function to convert list of objects to JSON string (Serialization)
     * @param list List<String>
     * @return json: String json type
     */
    @TypeConverter
    public static String fromList(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
