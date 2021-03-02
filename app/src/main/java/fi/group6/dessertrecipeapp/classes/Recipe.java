package fi.group6.dessertrecipeapp.classes;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
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

    @ColumnInfo(name = "level_of_difficulty")
    public String levelOfDifficulty ;

    public Recipe() {
    }

    public Recipe(String name, List<String> instructions, List<String> tags, String photo, boolean isCustom, int numberOfServings, int prepareTime, String author, String levelOfDifficulty) {
        this.recipeId = recipeId;
        this.name = name;
        this.instructions = instructions;
        this.tags = tags;
        this.photo = photo;
        this.isCustom = isCustom;
        this.numberOfServings = numberOfServings;
        this.prepareTime = prepareTime;
        this.author = author;
        this.levelOfDifficulty = levelOfDifficulty;
    }

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
                ", levelOfDifficulty=" + levelOfDifficulty +
                '}';
    }
}
