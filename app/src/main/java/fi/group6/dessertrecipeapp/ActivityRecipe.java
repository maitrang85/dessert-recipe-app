package fi.group6.dessertrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fi.group6.dessertrecipeapp.classes.AppDatabase;

public class ActivityRecipe extends AppCompatActivity {

    public static final String CLICKED_ITEM = "indexOfRecipe";
    private static final String EDIT_RECIPE_ID_KEY = "editRecipeId";

    TextView name;

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

        //Change title for the action bar to the recipe name
        //getSupportActionBar().setTitle(db.recipeDao().getRecipeById(indexOfRecipe).name);
        //Add back button to the action bar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button editRecipeButton = (Button) findViewById(R.id.editRecipeButton);

        editRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityRecipe.this, ActivityAddRecipe.class);
                intent.putExtra(EDIT_RECIPE_ID_KEY, indexOfRecipe);
                startActivity(intent);
            }
        });

    }

    //Pressing the back button on the action bar will take the user back to the previous activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}