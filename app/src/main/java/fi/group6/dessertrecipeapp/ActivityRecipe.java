package fi.group6.dessertrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import fi.group6.dessertrecipeapp.classes.AppDatabase;

public class ActivityRecipe extends AppCompatActivity {

    public static final String TAG = "indexOfRecipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //Recall the intent and recall the index of the recipe
        Bundle b = getIntent().getExtras();
        int indexOfRecipe = b.getInt(TAG);
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        //Change title for the action bar to the recipe name
        getSupportActionBar().setTitle(db.recipeDao().getRecipeById(indexOfRecipe).name);
        //Add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Basic textView to test the bottom navigation
        TextView title = (TextView) findViewById(R.id.testTextAddRecipe);
        title.setText("THIS IS THE RECIPE PAGE ACTIVITY");
    }

    //Pressing the back button on the action bar will take the user back to the previous activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}