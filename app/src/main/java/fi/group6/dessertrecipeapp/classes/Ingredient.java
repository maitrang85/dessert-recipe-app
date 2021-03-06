package fi.group6.dessertrecipeapp.classes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Ingredient represents one ingredient used in a recipe.
 * @author Daniil
 * Refactored by Trang
 * @version 1.2
 */
@Entity
public class Ingredient {

    /**
     * Id of ingredient
     */
    @PrimaryKey(autoGenerate = true)
    public int uid;

    /**
     * Name of ingredient
     */
    @ColumnInfo(name = "name")
    public String name;

    /**
     * Amount of ingredient
     */
    @ColumnInfo(name = "amount")
    public double amount;

    /**
     * How to measure ingredient
     */
    @ColumnInfo(name = "measure")
    public String measure;

    /**
     * Recipe Id
     */
    @ColumnInfo(name = "recipeId")
    public long recipeId;

    /**
     * Constructor Ingredient without any parameters
     */
    public Ingredient() {
    }

    /**
     * Constructor Ingredient with parameters
     * @param name name of ingredient
     * @param amount amount of ingredient
     * @param measure by which measurement method
     */
    public Ingredient(String name, double amount, String measure) {
        this.name = name;
        this.amount = amount;
        this.measure = measure;
    }

    /**
     * Constructor Ingredient will parameters
     * @param name name of ingredient
     * @param amount amount of ingredient
     * @param measure by which measurement method
     * @param recipeId id of recipe ingredient belongs to
     */
    public Ingredient(String name, double amount, String measure, int recipeId) {
        this.name = name;
        this.amount = amount;
        this.measure = measure;
        this.recipeId = recipeId;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", measure='" + measure + '\'' +
                ", recipeId=" + recipeId +
                '}';
    }

    /**
     * Compares Ingredients without taking in account uid
     * @param o
     * Object to compare with
     * @return
     * name, amount, measure, recipeId - are the same.
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

        Ingredient ingredient = (Ingredient) o;
        return this.name.equals(ingredient.name) &&
                this.amount == ingredient.amount &&
                this.measure.equals(ingredient.measure) &&
                this.recipeId == ingredient.recipeId;

    }
}
