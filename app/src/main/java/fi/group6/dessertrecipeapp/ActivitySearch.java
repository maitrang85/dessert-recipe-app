package fi.group6.dessertrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fi.group6.dessertrecipeapp.classes.AppDatabase;
import fi.group6.dessertrecipeapp.classes.Recipe;
import fi.group6.dessertrecipeapp.classes.RecipeWithIngredients;

public class ActivitySearch extends AppCompatActivity {

    private static final String SEARCH_TAG = "SEARCH";

    //Variables for the tag selector
    TextView tagSelectorTv;
    String[] tagArray = {"Dairy-free", "Gluten-free", "Nut-free", "Keto diet", "Paleo diet", "Vegan",
            "Low-calorie", "Low-fat", "Low-carb", "Plant based", "Sweet", "No cooking needed", "Frozen dessert"};
    ArrayList<String> tagList = new ArrayList<>();
    boolean[] selectedTag;

    //THIS CAN BE USED FOR SEARCH, IT'S AN ARRAY MADE ONLY FROM THE SELECTED ITEMS
    List<String> tagInput;

    //Variables for the difficulty selector
    TextView difficultySelectorTv;
    String[] difficultyArray = {"easy", "medium", "hard"};
    ArrayList<String> difficultyList = new ArrayList<>();
    boolean[] selectedDifficulty;

    //THIS CAN BE USED FOR SEARCH, IT'S AN ARRAY MADE ONLY FROM THE SELECTED ITEMS
    List<String> difficultyInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        //Change title for the top title bar
        getSupportActionBar().setTitle("SEARCH");

        //Basic textView to test the bottom navigation
        TextView title = (TextView) findViewById(R.id.testTextSearch);
        title.setText("Test here");

        //CODE FOR THE TAG SELECTOR
        tagSelectorTv = findViewById(R.id.tagSelectorTv);
        selectedTag = new boolean[tagArray.length];

        tagSelectorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySearch.this);
                builder.setTitle("Select tags for your recipe");
                builder.setCancelable(false);

                builder.setMultiChoiceItems(tagArray, selectedTag, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            tagList.add(tagArray[which]);
                        } else {
                            tagList.remove(tagArray[which]);
                        }
                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tagInput = new ArrayList<>();
                        String items = "";

                        for(int element = 0; element < tagList.size(); element++){

                            items = items + tagList.get(element);
                            tagInput.add(tagList.get(element).toString());

                            if(element != tagList.size() - 1){
                                items = items + ", ";
                            }
                        }
                        tagSelectorTv.setText(items);
                        Log.d("Tags selected", tagInput.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                builder.show();
            }
        });

        //CODE FOR THE DIFFICULTY SELECTOR
        difficultySelectorTv = findViewById(R.id.difficultySelectorTv);
        selectedDifficulty = new boolean[difficultyArray.length];

        difficultySelectorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySearch.this);
                builder.setTitle("Select tags for your recipe");
                builder.setCancelable(false);

                builder.setMultiChoiceItems(difficultyArray, selectedDifficulty, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            difficultyList.add(difficultyArray[which]);
                        } else {
                            difficultyList.remove(difficultyArray[which]);
                        }
                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        difficultyInput = new ArrayList<>();
                        String items2 = "";

                        for(int element = 0; element < difficultyList.size(); element++){

                            items2 = items2 + difficultyList.get(element);
                            difficultyInput.add(difficultyList.get(element).toString());

                            if(element != difficultyList.size() - 1){
                                items2 = items2 + ", ";
                            }
                        }
                        difficultySelectorTv.setText(items2);
                        Log.d("Tags selected", difficultyInput.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                builder.show();
            }
        });


        //Set up the bottom navigation bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav_ViewBar);

        //Check the correct icon in the bottom navigation bar
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        //According to which button the user clicks, the navigation bar will change the activity
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        //User will be taken to the main activity
                        Intent intent_home = new Intent(ActivitySearch.this, MainActivity.class);
                        startActivity(intent_home);
                        break;
                    case R.id.navigation_search:
                        //Current activity, nothing happens
                        break;
                    case R.id.navigation_add_recipe:
                        //User will be taken to the add recipe activity
                        Intent intent_add_recipe = new Intent(ActivitySearch.this, ActivityAddRecipe.class);
                        startActivity(intent_add_recipe);
                        break;
                    case R.id.navigation_favorites:
                        //User will be taken to the favorites activity
                        Intent intent_favorites = new Intent(ActivitySearch.this, ActivityFavorites.class);
                        startActivity(intent_favorites);
                        break;
                    case R.id.navigation_my_recipes:
                        //User will be taken to the my recipes activity
                        Intent intent_my_recipes = new Intent(ActivitySearch.this, ActivityMyRecipes.class);
                        startActivity(intent_my_recipes);
                        break;
                }
                return false;
            }
        });

        Button searchButton = (Button) findViewById(R.id.searchButton);
        boolean onlyExact = false; //TODO: Checkbox for that. Maybe inside filters

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchBar = (EditText) findViewById(R.id.searchBar);


                List<RecipeWithIngredients> recipesList = db.recipeDao().getRecipeWithIngredients();
                List<RecipeWithIngredients> searchResults = searchInRecipesList(recipesList,searchBar.getText().toString().trim(), onlyExact);

                //For Testing:
                Log.d(SEARCH_TAG, Integer.toString(searchResults.size()) + " results found");
                int i;
                for (i = 0; i < searchResults.size(); i++) {
                    Log.d(SEARCH_TAG, Integer.toString(i+1) + ".\n" + searchResults.get(i).recipe.toString());
                }

            }
        });
    }

    /**
     * Gives a list of recipes that matches the name given, or is similar to the name given
     * @param toFind
     * Recipe name String
     */
    private List<RecipeWithIngredients> searchInRecipesList (List<RecipeWithIngredients> recipesList, String toFind, boolean onlyExact) {
        List<RecipeWithIngredients> resultRecipesList = new ArrayList<>();

        //Searching for exact matches --> Fast
        if(onlyExact){
            for (RecipeWithIngredients recipe : recipesList) {
                if (recipe.recipe.name.equals(toFind)) {
                    resultRecipesList.add(recipe);
                }
            }
        }

        //Searching for word matches --> Slow
        if(!onlyExact) {
            //Searching for word matches in the recipe names
            //Position in the list depends on the number of word matches
            //Example: searching "chocolate cupcakes"
            //Found: 1."chocolate cupcakes", 2."chocolate cupcakes with ...", 3."chocolate cake", 4."cupcakes" ...

            List<String> toFindList = Arrays.asList(toFind.split(" "));
            int requiredNumberOfMatches = toFindList.size(); // in order to sort them by number of matches
            while (requiredNumberOfMatches > 0) { // looping until we will have 0 required amount of matches
                for (RecipeWithIngredients recipe: recipesList) {
                    if (countWordMatchesInLists(toFindList, Arrays.asList(recipe.recipe.name.split(" "))) == requiredNumberOfMatches) { // if we get exactly needed amount of matches
                        resultRecipesList.add(recipe);                                                                                 // add it to the results array
                    }
                }
                requiredNumberOfMatches--;
            }
        }

        return resultRecipesList;
    }

    /**
     * Compares 2 lists of strings for identical words ignoring the case.
     * Example: "Chocolate chocolate cake" and "Chocolate cake"
     * Result: 2
     * @param strList1
     * First list of strings
     * @param strList2
     * Second list of strings
     * @return
     * Matches count
     */
    private int countWordMatchesInLists(List<String> strList1, List<String> strList2) {
        int counter = 0;
        ArrayList<String> strArrayList2 = new ArrayList<>(); // array list is needed for .remove. Without that code becomes much more complicated
        strArrayList2.addAll(strList2);

        for (String str1: strList1) {
            for (String str2: strArrayList2) {
                if(str1.equalsIgnoreCase(str2)) {
                    counter++;
                    strArrayList2.remove(str2); // If we encounter the same word - it should be removed from the second array in order not to count it accidentally second time
                    break;
                }
            }
        }
        return counter;
    }
}