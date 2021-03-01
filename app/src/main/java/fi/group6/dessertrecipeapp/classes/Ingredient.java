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
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "amount")
    public double amount;

    @ColumnInfo(name = "measure")
    public String measure;

    @ColumnInfo(name = "recipeId")
    public long recipeId;
    //**************//
    // Constructors //
    //**************//
    /**
     * Creates new Ingredient
     * @param name
     * Name of the ingredient
     * @param amount
     * Amount of the ingredient given by decimal number
     * @param measure
     * Measure for the amount ( kg, l, etc... )
     */
    public Ingredient ( String name, double amount, String measure ) {
        this.name = name;
        this.amount = amount;
        this.measure = measure;
    }

    //*********//
    // Methods //
    //*********//

    /**
     * Changes amount of the ingredient
     * @param newNum
     * New amount
     */
    public void changeAmount(double newNum) {
        this.amount = newNum;
    }

    /**
     * Gives name of the ingredient
     * @return
     * Name of the Ingredient
     */
    public String getIngredientName() {
        return this.name;
    }

    /**
     * Gives amount of the ingredient
     * @return
     * Amount
     */
    public double getIngredientAmount() {
        return this.amount;
    }

    /**
     * Gives measure for the ingredient amount
     * @return
     * Measure
     */
    public String getIngredientMeasure() {
        return this.measure;
    }

    @Override
    public String toString() {
        return this.name + ": " + Double.toString(this.amount) + " " + measure;
    }

}
