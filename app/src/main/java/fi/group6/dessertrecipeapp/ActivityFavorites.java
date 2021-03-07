package fi.group6.dessertrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fi.group6.dessertrecipeapp.classes.AppDatabase;

/**
 * Favorites activity displays the user's favorite recipes
 * @author Tamas
 * @version 1.2
 */
public class ActivityFavorites extends AppCompatActivity {

    TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //Change title for the top title bar
        getSupportActionBar().setTitle("FAVORITES");

        //Attach room database to the activity
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        //Calls methods to setup the UI
        initRecyclerView();
        checkIfEmpty();
        initBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
        checkIfEmpty();
    }

    /**
     * This function is called in onCreate
     * The method checks if there are any favorites in the app yet
     * If no favorites are found, the placeholder text is shown
     */
    private void checkIfEmpty(){
        emptyText = findViewById(R.id.emptyActivityText);
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        //If there are no favorites, the text is set to visible
        if(db.recipeDao().countFavoriteRecipes() == 0){
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This function is called in onCreate
     * The method implements the recyclerView for the activity
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(db.recipeDao().getFavoriteRecipeWithIngredients(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * This function is called in onCreate
     * The method implements the bottom navigation view for the activity
     */
    private void initBottomNav(){
        //Set up the bottom navigation bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav_ViewBar);

        //Check the correct icon in the bottom navigation bar
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        //According to which button the user clicks, the navigation bar will change the activity
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        //User will be taken to the main activity
                        Intent intent_home = new Intent(ActivityFavorites.this, MainActivity.class);
                        startActivity(intent_home);
                        break;
                    case R.id.navigation_search:
                        //User will be taken to the search activity
                        Intent intent_search = new Intent(ActivityFavorites.this, ActivitySearch.class);
                        startActivity(intent_search);
                        break;
                    case R.id.navigation_add_recipe:
                        //User will be taken to the add recipe activity
                        Intent intent_add_recipe = new Intent(ActivityFavorites.this, ActivityAddRecipe.class);
                        startActivity(intent_add_recipe);
                        break;
                    case R.id.navigation_favorites:
                        //Current activity, nothing happens
                        break;
                    case R.id.navigation_my_recipes:
                        //User will be taken to the my recipes activity
                        Intent intent_my_recipes = new Intent(ActivityFavorites.this, ActivityMyRecipes.class);
                        startActivity(intent_my_recipes);
                        break;
                }
                return false;
            }
        });
    }
}