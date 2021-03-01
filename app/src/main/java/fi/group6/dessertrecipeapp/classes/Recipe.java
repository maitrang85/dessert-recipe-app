package fi.group6.dessertrecipeapp.classes;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Models a dessert recipe
 * @author Trang
 * Refactored by Daniil
 * @version 1.2
 */
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public long recipeId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "instructions")
    public List<String> instructions;

    @ColumnInfo(name = "tags")
    public List<String> tags;

    @ColumnInfo(name = "photo")
    public String photo;

    @ColumnInfo(name = "is_custom")
    public boolean isCustom;

    @ColumnInfo(name = "number_of_servings")
    public int numberOfServings;

    @ColumnInfo(name = "prepare_time")
    public int prepareTime;

    @ColumnInfo(name = "author")
    public String author;

    @ColumnInfo(name = "rating")
    public float rating;

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId=" + recipeId +
                ", name='" + name + '\'' +
                ", tags='" + tags + '\'' +
                ", instruction='" + instructions + '\'' +
                ", photo='" + photo + '\'' +
                ", isCustom=" + isCustom +
                ", numberOfServings=" + numberOfServings +
                ", prepareTime=" + prepareTime +
                ", author='" + author + '\'' +
                ", rating=" + rating +
                '}';
    }
}
