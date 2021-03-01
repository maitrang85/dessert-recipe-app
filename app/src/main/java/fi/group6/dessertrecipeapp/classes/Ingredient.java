package fi.group6.dessertrecipeapp.classes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Ingredient represents one ingredient used in a recipe.
 * @author TRANG
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
}
