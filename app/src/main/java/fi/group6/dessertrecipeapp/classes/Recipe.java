package fi.group6.dessertrecipeapp.classes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
/**
 * Models a dessert recipe
 * @author Trang
 * @version 1.3
 */
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

    /**
     * Special Recipe comparison
     * Not takes into account recipeId
     * @param o
     * Object to compare with
     * @return
     * name, author, levelOfDifficulty, prepareTime, numberOfServings,
     * photo, instructions, tags, isCustom, isFavourite - are the same.
     * true - they are, false - they are not
     */
    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Recipe recipe = (Recipe) o;

        //Photo comparison must take null into account, so handled separately
        boolean photoCheck;
        if (this.photo != null && recipe.photo != null) {
            photoCheck = this.photo.equals(recipe.photo);
        } else if( this.photo == null && recipe.photo == null ) {
            photoCheck = true;
        } else {
            photoCheck = false;
        }

        return this.name.equals(recipe.name) &&
                this.author.equals(recipe.author) &&
                this.levelOfDifficulty.equals(recipe.levelOfDifficulty) &&
                this.prepareTime == recipe.prepareTime &&
                this.numberOfServings == recipe.numberOfServings &&
                photoCheck &&
                this.instructions.equals(recipe.instructions) &&
                this.tags.equals(recipe.tags) &&
                this.isCustom == recipe.isCustom &&
                this.isFavourite == recipe.isFavourite;
    }
}
