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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fi.group6.dessertrecipeapp.classes.AppDatabase;
import fi.group6.dessertrecipeapp.classes.Ingredient;
import fi.group6.dessertrecipeapp.classes.Recipe;
import fi.group6.dessertrecipeapp.classes.RecipeWithIngredients;

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
        ListView lv = findViewById(R.id.lvRecipe);

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
    }

    void removeAll(List<String> list, String element) {
        while (list.contains(element)) {
            list.remove(element);
        }
    }

    //TESTING ROOM DATABASE
    private void loadRecipeList() {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        // GET ALL RECIPES LIST and Ingredients inside recipe.
        List<RecipeWithIngredients> recipeWithIngredientsList = db.recipeDao().getRecipeWithIngredientsByRecipeId("1");

        //Get Specific Recipe by ID example, in specific recipe Activity.
        Recipe recipe=  db.recipeDao().getRecipeById(1);

        //You can delete Recipe by this function
        db.recipeDao().deleteRecipe(recipe);

        //If your recipe has ingredient you should use this function to delete recipe and ingredients
        List<Ingredient> ingredients = new ArrayList<>();
        db.recipeDao().deleteRecipeWithIngredients(recipe, ingredients);

        //Example getAllRecipes and Ingredients belong to them.
        List<RecipeWithIngredients> recipeWithIngredients = db.recipeDao().getRecipeWithIngredients();
        List<Ingredient> allIngredients = db.recipeDao().getAllIngredients();

        //Search Recipe by Name
        List<Recipe> recipeListSearch = db.recipeDao().searchRecipeByName("el");

        //Search Recipe and ingredients belong to recipe by Name
        List<RecipeWithIngredients> recipeWithIngredientsBySearch = db.recipeDao().searchRecipeWithIngredientsByName("el");

        List<Recipe> afterInsert = db.recipeDao().getAllRecipes();
    }
    private void initDatabase (){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        Recipe recipe = new Recipe();
        recipe.name = "recipe 3";
        recipe.author = "Trang 3";
        recipe.rating = 4.5f;
        recipe.tags =  Arrays.asList("tag 1","tag 2","tag 3");
        recipe.instructions = Arrays.asList("instruction 1","instruction 2","instruction 3");
        recipe.isCustom = false;
        recipe.photo = "image 1";
        recipe.numberOfServings = 8;
        recipe.prepareTime = 10;

        Ingredient ingredient1 = new Ingredient();
        ingredient1.name = "recipe 3 ingredient1";
        ingredient1.measure = "recipe 3 ingredient1";
        ingredient1.amount = 1;

        Ingredient ingredient2 = new Ingredient();
        ingredient2.name = "recipe 3 ingredient2";
        ingredient2.measure = "recipe 3 ingredient2";
        ingredient2.amount = 2;

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);

        //recipe.tags.remove("tag 1");
        //Example Insert Recipe and Ingredients into database
        db.recipeDao().insertRecipeWithIngredients(recipe, ingredients);

        List<RecipeWithIngredients> recipeWithIngredientsList = db.recipeDao().getRecipeWithIngredients();
        Gson gson = new Gson();
        String json = gson.toJson(recipeWithIngredientsList);
        Log.d("json", "duma: " + json);
    }
}
