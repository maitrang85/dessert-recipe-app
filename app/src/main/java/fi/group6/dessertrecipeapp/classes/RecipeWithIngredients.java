package fi.group6.dessertrecipeapp.classes;
/**
 * This class models the one-to-many relationship between Recipe entity(one) and (to many) Ingredients entity
 */
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class RecipeWithIngredients {

    @Embedded
    public Recipe recipe;
    @Relation(
            parentColumn = "recipeId",
            entityColumn = "recipeId"
    )

    public List<Ingredient> ingredients;

    public RecipeWithIngredients(Recipe recipe, List<Ingredient> ingredients) {
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    /**
     * Special RecipeWithIngredients comparison
     * Do not takes in account:
     * isCustom
     * isFavourite
     * recipeId
     * @param o
     * Object to compare with
     * @return
     * Ingredients, name, author, levelOfDifficulty, prepareTime, numberOfServings,
     * photo, instructions, tags - are the same
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

        //Must take null into account, so handled separately
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
                this.recipe.tags.equals(recipeWithIngredients.recipe.tags);

    }
}
