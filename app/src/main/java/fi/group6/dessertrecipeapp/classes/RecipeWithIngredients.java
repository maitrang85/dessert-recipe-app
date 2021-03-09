package fi.group6.dessertrecipeapp.classes;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;
/**
 * This class models the one-to-many relationship between Recipe entity(one) and (to many) Ingredients entity
 * @author Trang
 * @version 1.1
 */
public class RecipeWithIngredients {

    /**
     * To model the relationship between recipe and its ingredients
     * Recipe is connected to each ingredient by recipeId
     */
    @Embedded
    public Recipe recipe;
    @Relation(
            parentColumn = "recipeId",
            entityColumn = "recipeId"
    )

    /**
     * List of all ingredients
     */
    public List<Ingredient> ingredients;

    /**
     * Constructor RecipeWithIngredients with parameters
     * @param recipe the recipe
     * @param ingredients list of ingredients belong to it
     */
    public RecipeWithIngredients(Recipe recipe, List<Ingredient> ingredients) {
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    /**
     * Special RecipeWithIngredients comparison
     * Not takes into account recipeId
     * @param o
     * Object to compare with
     * @return
     * Ingredients, name, author, levelOfDifficulty, prepareTime, numberOfServings,
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

        RecipeWithIngredients recipeWithIngredients = (RecipeWithIngredients) o;

        return this.ingredients.equals(recipeWithIngredients.ingredients) &&
                this.recipe.equals(recipeWithIngredients.recipe);
    }
}
