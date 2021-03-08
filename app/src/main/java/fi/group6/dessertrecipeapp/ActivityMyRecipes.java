package fi.group6.dessertrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fi.group6.dessertrecipeapp.classes.AppDatabase;

/**
 * My recipes activity displays the user's own recipes
 * @author Tamas
 * @version 1.3
 */
public class ActivityMyRecipes extends AppCompatActivity {

    /**
     * String containing tag for sharedPreferences
     */
    protected static final String SHARED_PREF_FILE = "userTheme";

    /**
     * String containing parameter for selected user theme
     */
    protected static final String THEME_KEY = "theme";

    Button emptyButton;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    CheckBox darkModeSwitch;
    String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        checkDarkMode();

        //Change title for the top title bar
        getSupportActionBar().setTitle("MY RECIPES");

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        initRecyclerView();
        checkIfEmpty();
        initBottomNav();
    }

    /**
     * This function is called in onCreate
     * The method checks if there are any favorites in the app yet
     * If no favorites are found, the placeholder text is shown
     */
    private void checkIfEmpty() {
        emptyButton = findViewById(R.id.emptyActivityButton);
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        if(db.recipeDao().countLocalRecipes() == 0){
            emptyButton.setVisibility(View.VISIBLE);
            emptyButton.setOnClickListener(v -> {
                Intent intent_add_recipe = new Intent(ActivityMyRecipes.this, ActivityAddRecipe.class);
                startActivity(intent_add_recipe);
            });
        }
    }

    /**
     * This function is called in onCreate
     * The method let's the user switch between dark and light mode
     * The user preference is then saved to sharedPreferences
     */
    private void checkDarkMode() {
        //Get the shared preferences data and update the switch because onStart already ran once
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE, ActivityMyRecipes.MODE_PRIVATE);
        theme = sharedPreferences.getString(THEME_KEY, "light");

        //Check or uncheck the checkbox based on the current theme of the app
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            darkModeSwitch.setChecked(true);
        }

        //If the user clicks the checkbox, the theme will automatically be changed
        darkModeSwitch.setOnClickListener(v -> {
            if(theme.equals("dark")){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                theme = "light";
                darkModeSwitch.setChecked(false);
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                theme = "dark";
                darkModeSwitch.setChecked(true);
            }
        });
    }

    /**
     * This function is called in onCreate
     * The method implements the recyclerView for the activity
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(db.recipeDao().getLocalRecipeWithIngredients(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * This function is called in onCreate
     * The method implements the bottom navigation view for the activity
     * https://material.io/components/bottom-navigation/android#using-bottom-navigation
     * https://www.youtube.com/watch?v=xyGrdOqseuw&ab_channel=CodingWithMitch
     * https://www.youtube.com/watch?v=JjfSjMs0ImQ&t=526s&ab_channel=AndroidCoding
     */
    private void initBottomNav() {
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

    /**
     * Shared preferences data is updated upon start
     */
    @Override
    protected void onStart() {
        super.onStart();
        //Get the shared preferences data and update the switch because onStart already ran once
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE, ActivityMyRecipes.MODE_PRIVATE);
        theme = sharedPreferences.getString(THEME_KEY, "light");
    }

    /**
     * Shared preferences data is saved upon exiting the activity
     */
    @Override
    protected void onPause() {
        super.onPause();
        //Create new shared preferences and editor
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Add the data from the theme string and save it
        editor.clear();
        editor.putString(THEME_KEY, theme);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}