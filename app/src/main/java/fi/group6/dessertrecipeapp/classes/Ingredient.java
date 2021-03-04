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

    public Ingredient() {
    }

    public Ingredient(String name, double amount, String measure) {
        this.name = name;
        this.amount = amount;
        this.measure = measure;
    }

    public Ingredient(String name, double amount, String measure, long recipeId) {
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
     * Compares Ingredients without taking in account uid and recipeId
     * @param o
     * Object to compare with
     * @return
     * name, amount, measure - are the same
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
                this.measure.equals(ingredient.measure);

    }
}
