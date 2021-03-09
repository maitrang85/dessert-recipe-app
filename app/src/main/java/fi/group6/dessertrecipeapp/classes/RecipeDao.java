package fi.group6.dessertrecipeapp.classes;

import android.annotation.SuppressLint;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

/**
 * Data access object class RecipeDao provides methods for app to use to query, insert, update, and
 * delete data in the database
 * @author Trang
 * @version 1.2
 */
@Dao
public abstract class RecipeDao {

    /**
     * This function to count recipes
     * @return number of recipes
     */
    @Query("SELECT count(*) FROM recipe")
    abstract public int countRecipes();

    /**
     * This function to count local recipes where is_custom = 1
     * @return number of local recipes
     */
    @Query("SELECT count(*) FROM recipe WHERE is_custom = 1")
    abstract public int countLocalRecipes();

    /**
     * This function to count favourite recipes where is_favourite = 1
     * @return number of favourite recipes
     */
    @Query("SELECT count(*) FROM recipe WHERE is_favourite = 1")
    abstract public int countFavoriteRecipes();

    /**
     * This function to count ingredients in a recipe;
     * @return number of ingredients
     */
    @Query("SELECT count(*) FROM ingredient WHERE recipeId=:uid")
    abstract public int countIngredients(int uid);

    /**
     * This function is to get a all recipes as a list
     * @return List<Recipe> list of recipe
     */
    @Query("SELECT * FROM recipe")
    abstract public List<Recipe> getAllRecipes();

    /**
     * This method returns all recipes with its ingredients
     * @return List of recipes with its ingredients
     */
    @Transaction
    @Query("SELECT * FROM recipe")
    abstract public List<RecipeWithIngredients> getRecipeWithIngredients();

    /**
     * This function to get list of pre-made recipes with ingredients
     * @return List<RecipeWithIngredients>  list of recipe (is_custom = 0) with ingredients
     */
    @Transaction
    @Query("SELECT * FROM recipe WHERE is_custom = 0")
    abstract public List<RecipeWithIngredients> getPremadeRecipeWithIngredients();

    /**
     * This function to get list of local recipes with ingredients
     * @return List<RecipeWithIngredients>  list of recipe (is_custom = 1) with ingredients
     */
    @Transaction
    @Query("SELECT * FROM recipe WHERE is_custom = 1")
    abstract public List<RecipeWithIngredients> getLocalRecipeWithIngredients();

    /**
     * This function to get list of  Favourite recipes with ingredients
     * @return List<RecipeWithIngredients>  list of recipe (is_favourite = 1) with ingredients
     */
    @Transaction
    @Query("SELECT * FROM recipe WHERE is_favourite = 1")
    abstract public List<RecipeWithIngredients> getFavoriteRecipeWithIngredients();

    /**
     * This function to get all ingredients
     * @return List<Ingredients>  all ingredients
     */
    @Query("SELECT * FROM ingredient")
    abstract public List<Ingredient> getAllIngredients();

    /**
     * This function to get list of ingredients by using recipeId
     * @param uid
     * recipeId of the attached recipe
     * @return List<Ingredients>
     */
    @Query("SELECT * FROM ingredient WHERE recipeId=:uid")
    abstract public List<Ingredient> getIngredientsById(int uid);

    /**
     * This function to insert recipe
     * @param recipe
     * Recipe to insert
     * @return recipeId
     */
    @Insert
    abstract public long insertRecipe(Recipe recipe);

    /**
     * This function to insert ingredient
     * @param ingredient
     * Ingredient to insert
     */
    @Insert
    abstract public void insertIngredient(Ingredient ingredient);

    /**
     * This function to insert all ingredients for the List of ingredients
     * @param ingredients
     * List of ingredients to insert
     */
    @Insert
    abstract public void insertAllIngredients(List<Ingredient> ingredients);

    /**
     * This is a priority function. It inserts recipe with all ingredients.
     * For each ingredient, recipeId will be identified to match with the recipe it belongs to.
     * @param recipe
     * Recipe to insert
     * @param ingredients
     * List of ingredients to attach to that recipe and insert
     */
    @SuppressLint("NewApi")
    @Insert
    public void insertRecipeWithIngredients(Recipe recipe, List<Ingredient> ingredients) {
        long recipeId =insertRecipe(recipe);
        ingredients.forEach(ingredient -> ingredient.recipeId = recipeId);
        insertAllIngredients(ingredients);
    }

    /**
     * This function to delete a recipe
     * @param recipe
     * Recipe to delete
     */
    @Delete
    abstract public void deleteRecipe(Recipe recipe);

    /**
     * This function to delete an ingredient
     * @param ingredient
     * Ingredients to delete
     */
    @Delete
    abstract public void deleteIngredient(Ingredient ingredient);

    /**
     * This function to delete recipe with all ingredients.
     * @param recipe
     * Recipe to delete
     * @param ingredients
     * List of ingredients to delete
     */
    @Delete
    abstract public void deleteRecipeWithIngredients(Recipe recipe, List<Ingredient> ingredients);

    /**
     * This function to get a recipe by its Id.
     * @param uid id of the recipe
     * @return the recipe with Id correspondingly
     */
    @Query("SELECT * FROM recipe WHERE recipeId=:uid")
    abstract public Recipe getRecipeById(int uid);

    /**
     * This function to search for a recipe by its name.
     * @param search
     * Name to search for
     * @return a list of recipes with names where search keyword is present(anywhere in the name)
     */
    @Query("SELECT * FROM recipe WHERE name LIKE '%' || :search || '%'") // Priority function
    abstract public List<Recipe> searchRecipeByName(String search);

    /**
     * This function to search for a recipe by its name.
     * @param search
     * Name to search for
     * @return a list of recipes with names and all of its ingredients where search keyword is
     * present(anywhere in the name)
     */
    @Query("SELECT * FROM recipe WHERE name LIKE '%' || :search || '%'") // Priority function
    abstract public List<RecipeWithIngredients> searchRecipeWithIngredientsByName(String search);

    /**
     * This function to search for recipe by its Id
     * @param recipeId
     * recipeId to search for
     * @return recipe with its ingredients
     */
    @Query("SELECT * FROM recipe WHERE recipeId =:recipeId")
    abstract public RecipeWithIngredients getRecipeWithIngredientsByRecipeId(int recipeId);

    /**
     * This function to add a recipe which has is_favourite = 1 to Favourites
     * @param recipeId
     * recipeId of a recipe to update
     */
    @Query("UPDATE recipe SET is_favourite = 1 WHERE recipeId=:recipeId")
    public abstract void addRecipeToFavorites(int recipeId);

    /**
     * This function is used to delete a recipe which has is_favourite = 0 from Favourites
     * @param recipeId
     * recipeId of a recipe to update
     */
    @Query("UPDATE recipe SET is_favourite = 0 WHERE recipeId=:recipeId")
    public abstract void deleteRecipeFromFavorites(int recipeId);

    /**
     * This function is for update a recipe
     * @param recipe
     * Recipe to update
     */
    @Update
    abstract public void updateRecipe(Recipe recipe);

    /**
     * This function is for update ingredient
     * @param Ingredient
     * Ingredient to update
     */
    @Update
    abstract public void updateIngredient(Ingredient Ingredient);

    /**
     * This function is for update all ingredients with corresponding recipe
     * @param recipe
     * Recipe to update
     * @param ingredients
     * Ingredients to update
     */
    @Update
    abstract public void updateIngredientWithRecipe(Recipe recipe, List<Ingredient> ingredients);

}

