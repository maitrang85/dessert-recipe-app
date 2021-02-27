package fi.group6.dessertrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ActivityRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //Change title for the action bar
        getSupportActionBar().setTitle("Recipe title here");
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