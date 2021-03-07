package fi.group6.dessertrecipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fi.group6.dessertrecipeapp.classes.AppDatabase;
import fi.group6.dessertrecipeapp.classes.RecipeWithIngredients;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "indexOfRecipe";
    private Toolbar ActionBarToolbar;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Change title for the top title bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffffff));

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        initBottomNav();
    }

    /**
     * Database and recyclerView are loaded in the onStart
     */
    @Override
    protected void onStart() {
        super.onStart();
        initDatabase();
        initRecyclerView();
    }

    /**
     * This function is called in onStart
     * The method loads the database and loads the raw json data into the database
     */
    private void initDatabase (){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        if(db.recipeDao().countRecipes()<=0){ //Data is empty
            Gson gson = new Gson();
            InputStream recipeJsonInputStream = getResources().openRawResource(R.raw.recipe); // getting recipe.jsonfile
            String recipeJson = readTextFile(recipeJsonInputStream);
            Type listType = new TypeToken<List<RecipeWithIngredients>>() {}.getType();
            List<RecipeWithIngredients> recipeWithIngredientsList = gson.fromJson(recipeJson, listType);
            //Insert data into database
            recipeWithIngredientsList.forEach(recipeWithIngredient -> {
                db.recipeDao().insertRecipeWithIngredients(recipeWithIngredient.recipe, recipeWithIngredient.ingredients);
            });
        }
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
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        //According to which button the user clicks, the navigation bar will change the activity
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        //Current activity, nothing happens
                        break;
                    case R.id.navigation_search:
                        //User will be taken to the search activity
                        Intent intent_search = new Intent(MainActivity.this, ActivitySearch.class);
                        startActivity(intent_search);
                        break;
                    case R.id.navigation_add_recipe:
                        //User will be taken to the add recipe activity
                        Intent intent_add_recipe = new Intent(MainActivity.this, ActivityAddRecipe.class);
                        startActivity(intent_add_recipe);
                        break;
                    case R.id.navigation_favorites:
                        //User will be taken to the favorites activity
                        Intent intent_favorites = new Intent(MainActivity.this, ActivityFavorites.class);
                        startActivity(intent_favorites);
                        break;
                    case R.id.navigation_my_recipes:
                        //User will be taken to the my recipes activity
                        Intent intent_my_recipes = new Intent(MainActivity.this, ActivityMyRecipes.class);
                        startActivity(intent_my_recipes);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * This function reads the data from the raw recipe json
     * @param inputStream
     * Recipe json file input data
     */
    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
        }
        return outputStream.toString();
    }

    /**
     * This function is called in onStart
     * The method implements the recyclerView for the activity
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(db.recipeDao().getPremadeRecipeWithIngredients(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
