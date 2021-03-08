package fi.group6.dessertrecipeapp.classes;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;
/**
 * This class models the one-to-many relationship between Recipe entity(one) and (to many) Ingredients entity
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
     * photo, instructions, tags, isCustom, isFavourite - are the same
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

        //Photo comparison must take null into account, so handled separately
        boolean photoCheck;
        if (this.recipe.photo != null && recipeWithIngredients.recipe.photo != null) {
            photoCheck = this.recipe.photo.equals(recipeWithIngredients.recipe.photo);
        } else if( this.recipe.photo == null && recipeWithIngredients.recipe.photo == null ) {
            photoCheck = true;
        } else {
            photoCheck = false;
        }

        return this.ingredients.equals(recipeWithIngredients.ingredients) &&
                this.recipe.name.equals(recipeWithIngredients.recipe.name) &&
                this.recipe.author.equals(recipeWithIngredients.recipe.author) &&
                this.recipe.levelOfDifficulty.equals(recipeWithIngredients.recipe.levelOfDifficulty) &&
                this.recipe.prepareTime == recipeWithIngredients.recipe.prepareTime &&
                this.recipe.numberOfServings == recipeWithIngredients.recipe.numberOfServings &&
                photoCheck &&
                this.recipe.instructions.equals(recipeWithIngredients.recipe.instructions) &&
                this.recipe.tags.equals(recipeWithIngredients.recipe.tags) &&
                this.recipe.isCustom == recipeWithIngredients.recipe.isCustom &&
                this.recipe.isFavourite == recipeWithIngredients.recipe.isFavourite;
    }
}
