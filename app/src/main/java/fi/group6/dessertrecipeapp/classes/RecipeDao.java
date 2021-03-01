package fi.group6.dessertrecipeapp.classes;

import android.annotation.SuppressLint;
import android.os.Build;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public abstract class RecipeDao {

    @Query("SELECT * FROM recipe")
    abstract public List<Recipe> getAllRecipes();

    /**
     * This method returns all recipes with its ingredients
     * @return List of recipes with its ingredients
     */
    @Transaction
    @Query("SELECT * FROM recipe")
    abstract public List<RecipeWithIngredients> getRecipeWithIngredients();

    @Query("SELECT * FROM ingredient")
    abstract public List<Ingredient> getAllIngredients();

    @Insert
    abstract public long insertRecipe(Recipe recipe);

    @Insert
    abstract public void insertIngredient(Ingredient ingredient);

    @Insert
    abstract public void insertAllIngredients(List<Ingredient> ingredients);

    @SuppressLint("NewApi")
    @Insert // // Priority function
    public void insertRecipeWithIngredients(Recipe recipe, List<Ingredient> ingredients) {
        long recipeId =insertRecipe(recipe);
        ingredients.forEach(ingredient -> ingredient.recipeId = recipeId);
        insertAllIngredients(ingredients);
    }

    @Delete
    abstract public void deleteRecipe(Recipe recipe);

    @Delete
    abstract public void deleteIngredient(Ingredient ingredient);

    @Delete
    abstract public void deleteRecipeWithIngredients(Recipe recipe, List<Ingredient> ingredients);

    @Query("SELECT * FROM recipe WHERE recipeId=:uid")
    abstract public Recipe getRecipeById(int uid);

    @Query("SELECT * FROM recipe WHERE name LIKE '%' || :search || '%'") // Priority function
    abstract public List<Recipe> searchRecipeByName(String search);

    @Query("SELECT * FROM recipe WHERE name LIKE '%' || :search || '%'") // Priority function
    abstract public List<RecipeWithIngredients> searchRecipeWithIngredientsByName(String search);

    @Query("SELECT * FROM recipe WHERE recipeId =:recipeId")
    abstract public List<RecipeWithIngredients> getRecipeWithIngredientsByRecipeId(String recipeId);

    @Update
    abstract public void updateRecipe(Recipe recipe);

    @Update
    abstract public void updateIngredient(Ingredient Ingredient);

    @Update
    abstract public void updateIngredientWithRecipe(Recipe recipe, List<Ingredient> ingredients);
}

