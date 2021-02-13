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

        //Change title for the top title bar
        getSupportActionBar().setTitle("Add recipe");

        //Basic textView to test the bottom navigation
        TextView title = (TextView) findViewById(R.id.testTextAddRecipe);
        title.setText("THIS IS THE ADD RECIPE ACTIVITY");

        //Set up the bottom navigation bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav_ViewBar);

        //Check the correct icon in the bottom navigation bar
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        //According to which button the user clicks, the navigation bar will change the activity
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        //User will be taken to the main activity
                        Intent intent_home = new Intent(ActivityAddRecipe.this, MainActivity.class);
                        startActivity(intent_home);
                        break;
                    case R.id.navigation_search:
                        //User will be taken to the search activity
                        Intent intent_search = new Intent(ActivityAddRecipe.this, ActivitySearch.class);
                        startActivity(intent_search);
                        break;
                    case R.id.navigation_add_recipe:
                        //Current activity, nothing happens
                        break;
                    case R.id.navigation_favorites:
                        //User will be taken to the favorites activity
                        Intent intent_favorites = new Intent(ActivityAddRecipe.this, ActivityFavorites.class);
                        startActivity(intent_favorites);
                        break;
                    case R.id.navigation_my_recipes:
                        //User will be taken to the my recipes activity
                        Intent intent_my_recipes = new Intent(ActivityAddRecipe.this, ActivityMyRecipes.class);
                        startActivity(intent_my_recipes);
                        break;
                }
                return false;
            }
        });
    }
}