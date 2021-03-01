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
}
