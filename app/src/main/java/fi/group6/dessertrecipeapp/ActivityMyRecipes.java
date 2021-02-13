package fi.group6.dessertrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityMyRecipes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        //Change title for the top title bar
        getSupportActionBar().setTitle("My recipes");

        //Basic textView to test the bottom navigation
        TextView title = (TextView) findViewById(R.id.testTextMyRecipes);
        title.setText("THIS IS THE MY RECIPES ACTIVITY");

        //Set up the bottom navigation bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav_ViewBar);

        //Check the correct icon in the bottom navigation bar
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        //According to which button the user clicks, the navigation bar will change the activity
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        //User will be taken to the main activity
                        Intent intent_home = new Intent(ActivityMyRecipes.this, MainActivity.class);
                        startActivity(intent_home);
                        break;
                    case R.id.navigation_search:
                        //User will be taken to the search activity
                        Intent intent_search = new Intent(ActivityMyRecipes.this, ActivitySearch.class);
                        startActivity(intent_search);
                        break;
                    case R.id.navigation_add_recipe:
                        //User will be taken to the add recipe activity
                        Intent intent_add_recipe = new Intent(ActivityMyRecipes.this, ActivityAddRecipe.class);
                        startActivity(intent_add_recipe);
                        break;
                    case R.id.navigation_favorites:
                        //User will be taken to the favorites activity
                        Intent intent_favorites = new Intent(ActivityMyRecipes.this, ActivityFavorites.class);
                        startActivity(intent_favorites);
                        break;
                    case R.id.navigation_my_recipes:
                        //Current activity, nothing happens
                        break;
                }
                return false;
            }
        });
    }
}