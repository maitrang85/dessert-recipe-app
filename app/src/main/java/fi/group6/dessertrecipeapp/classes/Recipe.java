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

    /**
     * Recipe Id
     */
    @PrimaryKey(autoGenerate = true)
    public int recipeId;

    /**
     * Name of recipe
     */
    @ColumnInfo(name = "name")
    public String name;

    /**
     * List of instructions
     */
    @ColumnInfo(name = "instructions")
    public List<String> instructions;

    /**
     * List of tags
     */
    @ColumnInfo(name = "tags")
    public List<String> tags;

    /**
     * Photo of recipe
     */
    @ColumnInfo(name = "photo")
    public String photo;

    /**
     * is recipe custom (pre-made)
     */
    @ColumnInfo(name = "is_custom")
    public boolean isCustom;

    /**
     * is recipe favourite
     */
    @ColumnInfo(name = "is_favourite")
    public boolean isFavourite;

    /**
     * number of people to be served
     */
    @ColumnInfo(name = "number_of_servings")
    public int numberOfServings;

    /**
     * Time for preparation
     */
    @ColumnInfo(name = "prepare_time")
    public int prepareTime;

    /**
     * author
     */
    @ColumnInfo(name = "author")
    public String author;

    /**
     * level of difficulty (easy, medium, hard)
     */
    @ColumnInfo(name = "level_of_difficulty")
    public String levelOfDifficulty ;

    /**
     * Constructor Recipe without parameters
     */
    public Recipe() {
    }

    /**
     * Constructor Recipe with parameters
     */
    public Recipe(String name, List<String> instructions, List<String> tags, String photo, boolean isCustom, boolean isFavourite, int numberOfServings, int prepareTime, String author, String levelOfDifficulty) {
        this.recipeId = recipeId;
        this.name = name;
        this.instructions = instructions;
        this.tags = tags;
        this.photo = photo;
        this.isCustom = isCustom;
        this.isFavourite = isFavourite;
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
                ", isFavourite=" + isFavourite +
                ", numberOfServings=" + numberOfServings +
                ", prepareTime=" + prepareTime +
                ", author='" + author + '\'' +
                ", levelOfDifficulty=" + levelOfDifficulty +
                '}';
    }
}
