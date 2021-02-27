package fi.group6.dessertrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.ArrayList;

import fi.group6.dessertrecipeapp.classes.Ingredient;
import fi.group6.dessertrecipeapp.classes.Recipe;
import fi.group6.dessertrecipeapp.classes.RecipeBook;

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

        RecipeBook recipes = RecipeBook.getInstance();

        //Connect the listView in the xml to the MainActivity
        ListView lv = findViewById(R.id.lvRecipe);

        //Set an adapter for the listView to show all the recipes in the RecipeBook class
        lv.setAdapter(new ArrayAdapter<Recipe>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                recipes.getAllRecipes()
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
                switch (item.getItemId()){
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

        if (DEBUG_RECIPE){
            debugPrintsForRecipeClass();
        }
    }

    //Write Recipe debugging code inside this method. (Database will probably require this anyway) ;)
    private void debugPrintsForRecipeClass() {
        // Just an example
        if (DEBUG_RECIPE_EXAMPLE){ // A bunch of the recipe debug prints. Mostly for copy/paste :)
            Recipe testRecipe = new Recipe("Pie", "Bread,100,kg;Hands,2,obj;", "Some instructions.\nI'm tired.`Hell.\nUh.`", "Not safe,Testing",
                    "nope", false, 228, 1337, "Daniil", (float) 1.0);
            Recipe testRecipe2 = new Recipe();
            Recipe testRecipe3 = new Recipe("Cake", "no, not this time", false, 2, 666, "Satan", (float) 10.10);
            Log.d("Decl", testRecipe.toString());
            Log.d("Decl", testRecipe2.toString());
            Log.d("Decl", testRecipe3.toString());

            Log.d("Decl", "Let's check methods!");

            Log.d("getName()", testRecipe.getName());
            Log.d("getName()", testRecipe2.getName());
            Log.d("getName()", testRecipe3.getName());

            testRecipe2.setName("pie 2.0");
            Log.d("setName()", testRecipe2.getName());

            Log.d("getIngredientList()", testRecipe.getIngredientList().toString());

            Log.d("getPhoto()", testRecipe.getPhoto());
            testRecipe2.setPhoto("ref");
            Log.d("setPhoto()", testRecipe2.getPhoto());

            Log.d("isCustom()", Boolean.toString(testRecipe.isCustom()));
            testRecipe.setCustom(true);
            Log.d("setCustom()", Boolean.toString(testRecipe.isCustom()));

            Log.d("getNumberOfServings()", Integer.toString(testRecipe3.getNumberOfServings()));
            testRecipe2.setNumberOfServings(25);
            Log.d("setNumberOfServings()", Integer.toString(testRecipe2.getNumberOfServings()));

            Log.d("getPrepareTime()", Integer.toString(testRecipe.getPrepareTime()));
            testRecipe2.setPrepareTime(10);
            Log.d("setPrepareTime()", Integer.toString(testRecipe2.getPrepareTime()));

            Log.d("getAuthorName()", testRecipe.getAuthorName());
            testRecipe2.setAuthorName("System");
            Log.d("setAuthorName()", testRecipe2.getAuthorName());

            Log.d("getGrade()", Float.toString(testRecipe.getGrade()));
            testRecipe2.setGrade((float) 3.5);
            Log.d("setGrade()", Float.toString(testRecipe2.getGrade()));

            testRecipe.addIngredient(new Ingredient("apples", 3, "obj"));
            Log.d("addIngredient()", testRecipe.getIngredientList().toString());

            Log.d("getNumOfIngredients()", Integer.toString(testRecipe.getNumOfIngredients()));

            Ingredient ingredientToDelete = testRecipe.getIngredient(testRecipe.getNumOfIngredients() - 1);
            Log.d("getIngredient()", ingredientToDelete.toString());

            testRecipe.deleteIngredient(testRecipe.getNumOfIngredients() - 1);
            Log.d("deleteIngredient()", testRecipe.getIngredientList().toString());

            testRecipe.changeIngredient(testRecipe.getNumOfIngredients() - 1, new Ingredient("Apple jam", 250, "l"));
            Log.d("changeIngredient()", testRecipe.getIngredientList().toString());

            Log.d("getAllInstructions()", testRecipe.getAllInstructions());

            testRecipe.addInstructions("Get to work!");
            Log.d("addInstructions()", testRecipe.getAllInstructions());

            Log.d("getNumOfInstructions()", Integer.toString(testRecipe.getNumOfInstructions()));

            Log.d("getInstructionByIndex()", testRecipe.getInstructionByIndex(testRecipe.getNumOfInstructions() - 1));

            testRecipe.modifyInstructions(testRecipe.getNumOfInstructions() - 1, "RABOTAY!");
            Log.d("modifyInstructions()", testRecipe.getInstructionByIndex(testRecipe.getNumOfInstructions() - 1));

            Log.d("FINALE", "AND FOR THE GRAND FINALE\n***************\n***************\n***************\n***************");
            Log.d("FINALE", testRecipe.toString());
            Log.d("FINALE", testRecipe2.toString());
            Log.d("FINALE", testRecipe3.toString());
            Log.d("FINALE", "***************\n***************\n***************\n***************");
        }
    }
}