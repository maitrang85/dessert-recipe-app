package fi.group6.dessertrecipeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

import fi.group6.dessertrecipeapp.classes.AppDatabase;
import fi.group6.dessertrecipeapp.classes.Ingredient;
import fi.group6.dessertrecipeapp.classes.Recipe;

/**
 * Recipe activity displays the recipe page which the user has clicked on
 * @author Tamas
 * @version 1.4
 */
public class ActivityRecipe extends AppCompatActivity {

    public static final String CLICKED_ITEM = "indexOfRecipe";
    private static final String EDIT_RECIPE_ID_KEY = "editRecipeId";

    TextView name;
    TextView time;
    TextView people;
    TextView difficulty;
    TextView ingredients;
    TextView instructions;
    TextView tags;
    TextView author;

    List<Ingredient> ingredientList;
    List<String> ingredientAmountList = new ArrayList<>();
    List<String> newAmountList = new ArrayList<>();
    List<String> instructionList;
    List<String> tagList;

    Button editRecipeButton;
    Button deleteRecipeButton;

    ImageView photoView;
    ImageView favorites;

    int ingredientCount;
    int instructionCount;
    int tagCount;

    boolean isFavorite;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        getSupportActionBar().hide();

        //Recall the intent and recall the index of the recipe
        Bundle b = getIntent().getExtras();
        int indexOfRecipe = b.getInt(CLICKED_ITEM);
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        name = findViewById(R.id.name);
        name.setText(db.recipeDao().getRecipeById(indexOfRecipe).name);

        addImage(indexOfRecipe);
        initFavoritesButton(indexOfRecipe);

        time = findViewById(R.id.timeText);
        time.setText(db.recipeDao().getRecipeById(indexOfRecipe).prepareTime + " minutes");

        people = findViewById(R.id.peopleText);
        if(db.recipeDao().getRecipeById(indexOfRecipe).numberOfServings == 1){
            people.setText(db.recipeDao().getRecipeById(indexOfRecipe).numberOfServings + " person");
        }else{
            people.setText(db.recipeDao().getRecipeById(indexOfRecipe).numberOfServings + " people");
        }

        difficulty = findViewById(R.id.difficultyText2);
        difficulty.setText(db.recipeDao().getRecipeById(indexOfRecipe).levelOfDifficulty);

        showIngredients(indexOfRecipe);
        showInstructions(indexOfRecipe);

        author = findViewById(R.id.authorText);
        author.setText("Author - " + db.recipeDao().getRecipeById(indexOfRecipe).author);

        showTags(indexOfRecipe);

        if(db.recipeDao().getRecipeById(indexOfRecipe).isCustom){
            buttonsForMyRecipes(indexOfRecipe);
        }
    }

    /**
     * This function adds photo to the recipe
     * @param indexOfRecipe
     * Database id of the currently selected recipe
     */
    private void addImage(int indexOfRecipe){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        String imageUrl = db.recipeDao().getRecipeById(indexOfRecipe).photo;
        photoView = findViewById(R.id.image);

        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(photoView);
    }

    /**
     * This function adds functionality to the favorites image
     * @param indexOfRecipe
     * Database id of the currently selected recipe
     */
    private void initFavoritesButton(int indexOfRecipe){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        isFavorite = false;
        favorites = findViewById(R.id.favorites);
        if(db.recipeDao().getRecipeById(indexOfRecipe).isFavourite){
            favorites.setImageResource(R.drawable.ic_baseline_favorite_full_color24);
            isFavorite = true;
        }

        favorites.setOnClickListener(v -> {
            if(isFavorite){
                db.recipeDao().deleteRecipeFromFavorites(indexOfRecipe);
                Toast.makeText(ActivityRecipe.this,
                        "Recipe removed from favorites", Toast.LENGTH_SHORT).show();
                favorites.setImageResource(R.drawable.ic_baseline_favorite_border_color_24);
                isFavorite = false;
            }else{
                db.recipeDao().addRecipeToFavorites(indexOfRecipe);
                Toast.makeText(ActivityRecipe.this,
                        "Recipe added to favorites", Toast.LENGTH_SHORT).show();
                favorites.setImageResource(R.drawable.ic_baseline_favorite_full_color24);
                isFavorite = true;
            }
        });
    }

    /**
     * This function adds the ingredients to the ingredients TextView
     * @param indexOfRecipe
     * Database id of the currently selected recipe
     */
    private void showIngredients(int indexOfRecipe) {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        ingredients = findViewById(R.id.ingredientList);
        ingredientCount = db.recipeDao().countIngredients(indexOfRecipe);
        ingredientList = db.recipeDao().getIngredientsById(indexOfRecipe);

        for(int i = 0; i < ingredientCount; i++) {
            ingredientAmountList.add(String.valueOf(ingredientList.get(i).amount));
            if(ingredientAmountList.get(i).endsWith(".0")){
                String newAmount = ingredientAmountList.get(i);
                newAmount = newAmount.replace(".0", "");
                Log.d("NEW AMOUNT", newAmount);
                newAmountList.add(newAmount);
            }
        }

        int k = 0;
        for(int i = 0; i < ingredientCount; i++) {
            ingredients.append(" • ");
            if(ingredientAmountList.get(i).endsWith(".0")){
                ingredients.append(newAmountList.get(k));
                k++;
            }else{
                ingredients.append(ingredientAmountList.get(i));
            }
            ingredients.append(" ");
            ingredients.append(ingredientList.get(i).measure);
            ingredients.append(" ");
            ingredients.append(ingredientList.get(i).name);
            ingredients.append("\n");
        }
    }

    /**
     * This function adds the ingredients to the ingredients TextView
     * @param indexOfRecipe
     * Database id of the currently selected recipe
     */
    private void showInstructions(int indexOfRecipe){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        instructions = findViewById(R.id.instructionList);
        instructionList = db.recipeDao().getRecipeById(indexOfRecipe).instructions;
        instructionCount = db.recipeDao().getRecipeById(indexOfRecipe).instructions.size();
        for(int i = 0; i < instructionCount; i++) {
            instructions.append(instructionList.get(i));
            if(i != instructionCount - 1){
                instructions.append("\n\n");
            }
        }
    }

    /**
     * This function adds the tags to the tags TextView
     */
    private void showTags(int indexOfRecipe){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        tags = findViewById(R.id.tagList);
        tags.append("Tags: ");
        tagList = db.recipeDao().getRecipeById(indexOfRecipe).tags;

        //Add the tags to the recipe page
        if (tagList != null) {
            tagCount = db.recipeDao().getRecipeById(indexOfRecipe).tags.size();
            for (int i = 0; i < tagCount; i++) {
                tags.append(tagList.get(i));
                if (i != tagCount - 1) {
                    tags.append(", ");
                }
            }
        }
    }

    /**
     * This function is only called when the user has clicked on one of their own recipes
     * Edit button is added so that the user can edit their recipes
     * Delete button is added so that the user can delete their recipes
     */
    private void buttonsForMyRecipes(int indexOfRecipe){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        editRecipeButton = findViewById(R.id.editRecipeButton);
        deleteRecipeButton=findViewById(R.id.deleteRecipeButton);
        if(db.recipeDao().getRecipeById(indexOfRecipe).isCustom){
            editRecipeButton.setVisibility(View.VISIBLE);
            deleteRecipeButton.setVisibility(View.VISIBLE);

            editRecipeButton.setOnClickListener(v -> {
                Intent intent = new Intent(ActivityRecipe.this, ActivityAddRecipe.class);
                intent.putExtra(EDIT_RECIPE_ID_KEY, indexOfRecipe);
                startActivity(intent);
            });

            deleteRecipeButton.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Delete " + db.recipeDao().getRecipeById(indexOfRecipe).name)
                        .setMessage("Are you sure you want to delete this recipe?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Recipe recipeToDelete = db.recipeDao().getRecipeById(indexOfRecipe);
                                List<Ingredient> ingredientsToDelete = db.recipeDao().getIngredientsById(indexOfRecipe);

                                db.recipeDao().deleteRecipeWithIngredients(recipeToDelete, ingredientsToDelete);
                                Intent intent = new Intent(ActivityRecipe.this, ActivityMyRecipes.class);
                                startActivity(intent);

                                Toast.makeText(ActivityRecipe.this,
                                        "Recipe deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            });
        }

    }

    /**
     * This function is called when the user presses the back button
     * Pressing the back button on the action bar will take the user back to the previous activity
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}