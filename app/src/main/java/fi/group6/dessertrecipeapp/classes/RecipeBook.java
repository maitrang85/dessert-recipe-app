package fi.group6.dessertrecipeapp.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a recipe singleton for the recipe book
 * @author Tamas
 * @version 1.0
 */

public class RecipeBook {
    private static final RecipeBook recipeInstance = new RecipeBook();
    private List<Recipe> recipes;

    //**************//
    //  Constructor //
    //**************//

    /**
     * Constructor
     * Creates recipes according to the parameters in the constructor of Recipe.java
     */
    private RecipeBook(){
        recipes = new ArrayList<Recipe>();
        recipes.add(new Recipe("Dummy Recipe", "IngredientName,13.2,kg;Ingredient2Name,22.16,l;",
                "Instruction`Instruction2`", "Tag,Tag2,", "", false, 1,
                0, "Generic", 0));
    }

    //*********//
    // Methods //
    //*********//

    /**
     * Gets all the recipes from the recipe book
     */
    public List<Recipe> getAllRecipes(){
        return this.recipes;
    }

    /**
     * Gives the correct recipe details
     * @param indexOfRecipe
     * Index of the recipe in the recipe book
     */
    public Recipe getRecipe(int indexOfRecipe){
        return recipes.get(indexOfRecipe);
    }

    /**
     * Adds a recipe to the list
     * @param recipe
     * Recipe details
     */
    public void addRecipe(Recipe recipe){
        this.recipes.add(recipe);
    }

    /**
     * Gives exactly one recipe instance from the recipe book
     */
    public static RecipeBook getInstance(){
        return recipeInstance;
    }

}
