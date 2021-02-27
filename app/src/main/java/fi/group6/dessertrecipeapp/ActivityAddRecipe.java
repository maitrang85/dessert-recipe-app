package fi.group6.dessertrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityAddRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        //Change title for the action bar
        getSupportActionBar().setTitle("Add recipe");
        //Add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Basic textView to test the bottom navigation
        TextView title = (TextView) findViewById(R.id.testTextAddRecipe);
        title.setText("THIS IS THE ADD RECIPE ACTIVITY");
    }

    //Pressing the back button on the action bar will take the user back to the previous activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}