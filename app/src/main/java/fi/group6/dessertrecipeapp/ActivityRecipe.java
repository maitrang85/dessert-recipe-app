package fi.group6.dessertrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import fi.group6.dessertrecipeapp.classes.AppDatabase;

public class ActivityRecipe extends AppCompatActivity {

    public static final String CLICKED_ITEM = "indexOfRecipe";
    private static final String EDIT_RECIPE_ID_KEY = "editRecipeId";

    TextView name;
    Button editRecipeButton;
    ImageView photoView;
    ImageView favorites;
    boolean isFavorite;

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

        String imageUrl = db.recipeDao().getRecipeById(indexOfRecipe).photo;
        photoView = findViewById(R.id.image);

        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(photoView);

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
                        "Recipe removed from favorites", Toast.LENGTH_LONG).show();
                favorites.setImageResource(R.drawable.ic_baseline_favorite_border_color_24);
                isFavorite = false;
            }else{
                db.recipeDao().addRecipeToFavorites(indexOfRecipe);
                Toast.makeText(ActivityRecipe.this,
                        "Recipe added to favorites", Toast.LENGTH_LONG).show();
                favorites.setImageResource(R.drawable.ic_baseline_favorite_full_color24);
                isFavorite = true;
            }
        });

        //Change title for the action bar to the recipe name
        //getSupportActionBar().setTitle(db.recipeDao().getRecipeById(indexOfRecipe).name);
        //Add back button to the action bar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editRecipeButton = findViewById(R.id.editRecipeButton);
        if(db.recipeDao().getRecipeById(indexOfRecipe).isCustom){
            editRecipeButton.setVisibility(View.VISIBLE);

            editRecipeButton.setOnClickListener(v -> {
                Intent intent = new Intent(ActivityRecipe.this, ActivityAddRecipe.class);
                intent.putExtra(EDIT_RECIPE_ID_KEY, indexOfRecipe);
                startActivity(intent);
            });
        }
    }

    //Pressing the back button on the action bar will take the user back to the previous activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}