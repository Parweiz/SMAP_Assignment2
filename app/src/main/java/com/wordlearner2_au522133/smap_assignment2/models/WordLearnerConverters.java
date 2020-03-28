package com.wordlearner2_au522133.smap_assignment2.models;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class WordLearnerConverters {

    @TypeConverter
    public static List<Definition> stringToDefinitionList(String data) {
        Gson gson = new Gson();
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Definition>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String definitionListToString(List<Definition> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
