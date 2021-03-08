package fi.group6.dessertrecipeapp.classes;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;
public class DataConverter {
    /**
     * This function to convert data from json to list of object string
     * @param value json string
     * @return List<String> list of object strings
     */
    @TypeConverter
    public static List<String> fromString(String value) {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    /**
     * This function to convert list of string objects back to json type
     * @param list
     * @return String json type
     */
    @TypeConverter
    public static String fromList(List<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
