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
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Change title for the top title bar
        getSupportActionBar().setTitle("DailyDesserts");

        //Basic textView to test the bottom navigation
        TextView title = (TextView) findViewById(R.id.testText);
        title.setText("THIS IS THE MAIN ACTIVITY");

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
        testRemoveList();
    }

    private void testRemoveList(){

//        List<String> tags = Arrays.asList("tag 1","tag 2","tag 3");
//        removeAll(tags, "tag 2");

        ArrayList<String> myStrings = new ArrayList<>();
        myStrings.add("Alpha");
        myStrings.add("Beta");
        myStrings.add("Gama");

        myStrings.remove("Beta");
    }

    void removeAll(List<String> list, String element) {
        while (list.contains(element)) {
            list.remove(element);
        }
    }

    //TESTING ROOM DATABASE
    private void loadRecipeList() {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        //Get All Recipes list
        List<Recipe> recipeList = db.recipeDao().getAllRecipes();
        //recipeListAdapter.setRecipeList(recipeList);

        Recipe recipe = new Recipe();
        recipe.name = "recipe 3";
        recipe.author = "Trang 3";
        recipe.rating = 4.5f;
        recipe.tags =  Arrays.asList("tag 1","tag 2","tag 3");
        recipe.instructions = Arrays.asList("instruction 1","instruction 2","instruction 3");

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
        //Example Insert Recipe and Ingredients
        db.recipeDao().insertRecipeWithIngredients(recipe, ingredients);

        //Example getAllRecipes and Ingredients belong to them.
        List<RecipeWithIngredients> recipeWithIngredients = db.recipeDao().getRecipeWithIngredients();
        List<Ingredient> allIngredients = db.recipeDao().getAllIngredients();

        List<RecipeWithIngredients> recipeWithIngredientsList = db.recipeDao().getRecipeWithIngredientsByRecipeId("1");
        //Get Recipe by ID example
        Recipe recipeById =  db.recipeDao().getRecipeById(1);

        //Search Recipe by Name
        List<Recipe> recipeListSearch = db.recipeDao().searchRecipeByName("el");

        //Delete Recipe
        db.recipeDao().deleteRecipe(recipeById);

         //Insert Recipe
        recipeById.recipeId = 4;
        db.recipeDao().insertRecipe(recipeById);
        List<Recipe> afterInsert = db.recipeDao().getAllRecipes();


    }
}