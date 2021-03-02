package fi.group6.dessertrecipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fi.group6.dessertrecipeapp.classes.AppDatabase;
import fi.group6.dessertrecipeapp.classes.Ingredient;
import fi.group6.dessertrecipeapp.classes.Recipe;
import fi.group6.dessertrecipeapp.classes.RecipeWithIngredients;
@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    private static final boolean DEBUG_RECIPE = true; //Recipe debug prints //temporary
    private static final boolean DEBUG_RECIPE_EXAMPLE = false; //Just an example for recipe debug prints //temporary

    public static final String TAG = "indexOfRecipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Change title for the top title bar
        getSupportActionBar().setTitle("DailyDesserts");

        //Connect the listView in the xml to the MainActivity
        ListView lv = findViewById(R.id.recycleViewRecipes);

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        //Set an adapter for the listView to show all the recipes in the RecipeBook class
        lv.setAdapter(new ArrayAdapter<Recipe>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                db.recipeDao().getAllRecipes()
        ));

        //Upon clicking any of the recipes, the user will be taken to the specific recipe page
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Add intent to change activities
                Intent detailsActivity = new Intent(MainActivity.this, ActivityRecipe.class);
                detailsActivity.putExtra(TAG, i);
                startActivity(detailsActivity);
            }
        });

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


    @Override
    protected void onStart() {
        super.onStart();
        //loadRecipeList();
        initDatabase();
        loadRecipeList();
    }

    //TESTING ROOM DATABASE
    private void loadRecipeList() {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        // GET ALL RECIPES LIST if you don't need to have ingredients inside
        List<Recipe> recipes = db.recipeDao().getAllRecipes();
        recipes.forEach(recipe -> {
            Log.d("Log to see how to use", recipe.name + "------" + recipe.photo);
        });

        // GET ALL RECIPES LIST and Ingredients inside recipe.
        List<RecipeWithIngredients> recipeWithIngredientsList = db.recipeDao().getRecipeWithIngredients();
        recipeWithIngredientsList.forEach(recipeWithIngredient -> {
            Log.d("Log to see how to use", recipeWithIngredient.recipe.name + "------" + recipeWithIngredient.recipe.photo);
            recipeWithIngredient.ingredients.forEach(ingredient -> {
                Log.d("Log to see how to use", ingredient.name  + "----" + ingredient.measure);
            });
        });

        //Get Specific Recipe by ID example, in specific recipe Activity.
        Recipe recipe=  db.recipeDao().getRecipeById(1);

        //You can delete Recipe by this function
        if(recipe!=null)
            db.recipeDao().deleteRecipe(recipe);

        //If your recipe has ingredient you should use this function to delete recipe and ingredients
//        if(recipe!=null)
//          db.recipeDao().deleteRecipeWithIngredients(recipeWithIngredient.recipe, recipeWithIngredient.ingredients);

        //Example get all Ingredients inside database
        List<Ingredient> allIngredients = db.recipeDao().getAllIngredients();

        //Search Recipe by Name
        List<Recipe> recipeListSearch = db.recipeDao().searchRecipeByName("el");

        //Search Recipe and ingredients belong to recipe by Name
        List<RecipeWithIngredients> recipeWithIngredientsBySearch = db.recipeDao().searchRecipeWithIngredientsByName("el");

    }
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
                Log.d("Data", recipeWithIngredient.recipe.name + "------" + recipeWithIngredient.recipe.photo);
            });
        }
    }

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
}
