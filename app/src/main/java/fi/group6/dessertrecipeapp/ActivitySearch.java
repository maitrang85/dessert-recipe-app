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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fi.group6.dessertrecipeapp.classes.AppDatabase;
import fi.group6.dessertrecipeapp.classes.RecipeWithIngredients;

/**
 * Search activity lets the user search for recipes based on different filters
 * @author Daniil
 * Extra people filter added by Tamas
 * @version 1.2
 */
public class ActivitySearch extends AppCompatActivity {

    private static final String SEARCH_TAG = "SEARCH";
    private static final String RESULTS = "SEARCH_RESULTS";
    private static final boolean DEBUG_PRINTS = false; //Set to true to see search debug prints.

    //Variables for the tag selector
    TextView tagSelectorTv;
    String[] tagArray = {"Dairy-free", "Gluten-free", "Nut-free", "Keto diet", "Paleo diet", "Vegetarian",
            "Low-calorie", "Low-fat", "Low-carb", "Plant based", "Sweet", "No cooking needed", "Frozen dessert"};
    ArrayList<String> tagList = new ArrayList<>();
    boolean[] selectedTag;

    //THIS IS USED FOR SEARCH, IT'S AN ARRAY MADE ONLY FROM THE SELECTED ITEMS IN THE TAG SELECTOR
    List<String> tagInput;

    //Variables for the difficulty selector
    TextView difficultySelectorTv;
    String[] difficultyArray = {"easy", "medium", "hard"};
    ArrayList<String> difficultyList = new ArrayList<>();
    boolean[] selectedDifficulty;

    //THIS IS USED FOR SEARCH, IT'S AN ARRAY MADE ONLY FROM THE SELECTED ITEMS IN THE DIFFICULTY SELECTOR
    List<String> difficultyInput;

    //THIS IS USED TO GET A LIST WITH THE SEARCH RESULTS
    List<RecipeWithIngredients> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        //Change title for the top title bar
        getSupportActionBar().setTitle("SEARCH");

        tagSelector();
        difficultySelector();

        initSearchButton();
        initNavigationBar();
    }

    //*******************//
    //* PRIVATE METHODS *//
    //*******************//

    /**
     * This function is called when the user clicks on the tag selector
     * Upon clicking, the user is taken to a pop-up view where they can select multiple difficulty ratings
     * After selecting the ratings, those will be saved inside an array
     * REFERENCES
     * https://www.youtube.com/watch?v=XrDVu3uPY3o&ab_channel=AndroidCoding
     */
    private void difficultySelector() {
        //CODE FOR THE DIFFICULTY SELECTOR
        difficultySelectorTv = findViewById(R.id.difficultySelectorTv);
        selectedDifficulty = new boolean[difficultyArray.length];

        difficultySelectorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySearch.this);
                builder.setTitle("Select recipe difficulties");
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
                        if (DEBUG_PRINTS) Log.d("Tags selected", difficultyInput.toString());
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
    }

    /**
     * This function is called when the user clicks on the tag selector
     * Upon clicking, the user is taken to a pop-up view where they can select multiple tags
     * After selecting the tags, those will be saved inside an array
     * REFERENCES
     * https://www.youtube.com/watch?v=XrDVu3uPY3o&ab_channel=AndroidCoding
     */
    public void tagSelector(){
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
                        if (DEBUG_PRINTS) Log.d("Tags selected", tagInput.toString());
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
    }

    /**
     * This function is called in onCreate
     * Adds functionality to the search button
     */
    private void initSearchButton(){

        Button searchButton = (Button) findViewById(R.id.searchButton);
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        searchButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Method in anonymous click listener for the search activation.
             * Activates and performs search based on all selected filters.
             * Use searchResults in order to get the result.
             * @param v
             * View
             */
            @Override
            public void onClick(View v) {
                //Search bar
                EditText searchBar = (EditText) findViewById(R.id.searchBar);
                //Preparation time from view
                EditText prepTimeFromView = (EditText) findViewById(R.id.prepTimeFrom);
                //Preparation time to view
                EditText prepTimeToView = (EditText) findViewById(R.id.prepTimeTo);
                //People from view
                EditText peopleFromView = (EditText) findViewById(R.id.peopleFrom);
                //People to view
                EditText peopleToView = (EditText) findViewById(R.id.peopleTo);
                //Checkbox for the exact search
                CheckBox exactNameCheckBox = (CheckBox)findViewById(R.id.exactNameCheckBox);
                //Boolean, determined by the checkbox for the exact name search
                boolean onlyExact = exactNameCheckBox.isChecked();
                //Filtering from this time
                int prepTimeFrom = -1;
                if(!prepTimeFromView.getText().toString().equals("")) { //Validating input
                    prepTimeFrom = Integer.parseInt(prepTimeFromView.getText().toString().trim());
                }
                //Filtering until this time
                int prepTimeTo = -1;
                if(!prepTimeToView.getText().toString().equals("")) { //Validating input
                    prepTimeTo = Integer.parseInt(prepTimeToView.getText().toString().trim());
                }
                //Filtering from this amount of people
                int peopleFrom = -1;
                if(!peopleFromView.getText().toString().equals("")) { //Validating input
                    peopleFrom = Integer.parseInt(peopleFromView.getText().toString().trim());
                }
                //Filtering to this amount of people
                int peopleTo = -1;
                if(!peopleToView.getText().toString().equals("")) { //Validating input
                    peopleTo = Integer.parseInt(peopleToView.getText().toString().trim());
                }
                //
                List<String> filteringTags = new ArrayList<>();
                if (tagInput != null) {
                    filteringTags = tagInput;
                }
                //
                List<String> filteringDifficulty = new ArrayList<>();
                if (difficultyInput != null) {
                    filteringDifficulty = difficultyInput;
                }
                //String to search for in the name
                String searchingFor = searchBar.getText().toString().trim();

                // There is no point to search for recipes if we aren't searching for anything. Home page exists for that purpose.
                if(searchingFor.equals("") && prepTimeFrom == -1 && prepTimeTo == -1 &&
                        filteringTags.size() == 0 && filteringDifficulty.size() == 0 && peopleFrom == -1 && peopleTo == -1) {
                    Log.e(SEARCH_TAG, "No searching filters or name provided");
                    Toast.makeText(ActivitySearch.this,
                            "No search filters added, please fill out at least one field", Toast.LENGTH_LONG).show();
                    return;
                }

                //List with all recipes from the database to be filtered
                List<RecipeWithIngredients> allRecipesList = db.recipeDao().getRecipeWithIngredients();
                //List, to be used for search
                List<RecipeWithIngredients> filteredRecipesList = new ArrayList<RecipeWithIngredients>();

                // Filtering recipes
                filteredRecipesList = filterRecipes(allRecipesList, filteringTags, filteringDifficulty, prepTimeFrom, prepTimeTo, peopleFrom, peopleTo);

                if(searchingFor.equals("")) { //If we have no name input - means we want to get filtered results without specified name
                    searchResults = filteredRecipesList;
                } else {
                    // Searching for the name inside the filtered recipes
                    searchResults = searchInRecipesList(filteredRecipesList, searchingFor, onlyExact);
                }

                //Useful search debug prints
                if (DEBUG_PRINTS) {
                    Log.d(SEARCH_TAG, "onlyExact=" + Boolean.toString(onlyExact));
                    Log.d(SEARCH_TAG, "Size: " + Integer.toString(filteringTags.size()) + " Tags to search for: " + filteringTags.toString());
                    Log.d(SEARCH_TAG, "Size: " + Integer.toString(filteringDifficulty.size()) + " Difficulties to search for: " + filteringDifficulty.toString());
                    Log.d(SEARCH_TAG, "Time From=" + Integer.toString(prepTimeFrom));
                    Log.d(SEARCH_TAG, "Time To  =" + Integer.toString(prepTimeTo));
                    Log.d(SEARCH_TAG, Integer.toString(searchResults.size()) + " results found");

                    int i;
                    for (i = 0; i < searchResults.size(); i++) {
                        Log.d(SEARCH_TAG, Integer.toString(i + 1) + ".\n" + searchResults.get(i).recipe.toString());
                    }
                }

                List<Integer> resultsRecipeId = new ArrayList<>();

                for (int i = 0; i < searchResults.size(); i++) {
                    resultsRecipeId.add(searchResults.get(i).recipe.recipeId);
                }

                Intent intent = new Intent(ActivitySearch.this, ActivitySearchResults.class);
                intent.putIntegerArrayListExtra(RESULTS, (ArrayList<Integer>) resultsRecipeId );
                startActivity(intent);
            }
        });
    }

    /**
     * This function is called in onCreate
     * The method implements the bottom navigation view for the activity
     * REFERENCES
     * https://material.io/components/bottom-navigation/android#using-bottom-navigation
     * https://www.youtube.com/watch?v=xyGrdOqseuw&ab_channel=CodingWithMitch
     * https://www.youtube.com/watch?v=JjfSjMs0ImQ&t=526s&ab_channel=AndroidCoding
     */
    private void initNavigationBar() {
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
    }

    /**
     * Gives a list of recipes that matches the name given, or is similar to the name given
     * @param recipesList
     * List to search for the name in
     * @param toFind
     * Recipe name String
     * @param onlyExact
     * If the search should be only for exact matches
     * @return
     * RecipeWithIngredients list with the result of the search
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

    /**
     * Filters given recipes by specified tags, difficulty preparation time and number of people
     * @param recipes
     * Recipe list to filter
     * @param filteringTags
     * Tags, which must be in a recipe in order to be accepted
     * @param filteringDifficulty
     * Accepted levels of difficulty
     * @param timeFrom
     * Minimal time of preparation
     * @param timeTo
     * Maximum time of preparation
     * @param peopleFrom
     * Minimal number of people
     *@param peopleTo
     * Maximum number of people
     * @return
     * Accepted by filter recipes list
     */
    private List<RecipeWithIngredients> filterRecipes(List<RecipeWithIngredients> recipes, List<String> filteringTags,
                                                      List<String> filteringDifficulty, int timeFrom, int timeTo, int peopleFrom, int peopleTo) {
        List<RecipeWithIngredients> filteredByTags = new ArrayList(); // 1st level
        List<RecipeWithIngredients> filteredByTagsAndDifficulty = new ArrayList<>(); // 2nd level
        List<RecipeWithIngredients> filteredByTagsDifficultyTime = new ArrayList<>(); // 3rd level
        List<RecipeWithIngredients> filteredResultList = new ArrayList<>(); // 4th level

        // 1st level. Tags
        if (filteringTags.size() == 0) { //No preferred tags
            filteredByTags = recipes;
        } else {
            for (RecipeWithIngredients recipe: recipes) {
                if (checkRecipeForAllGivenTags(filteringTags, recipe)) { // If all needed tags are in a recipe
                    filteredByTags.add(recipe);                          // - accept it
                }
            }
        }

        // 2nd level. Difficulty
        if (filteringDifficulty.size() == 0 || filteringDifficulty.size() == 3) { // None/all difficulties selected
            filteredByTagsAndDifficulty = filteredByTags;
        } else {
            for (RecipeWithIngredients recipe: filteredByTags) {
                for (String difficulty: filteringDifficulty) {
                    if( recipe.recipe.levelOfDifficulty.equals(difficulty) ) { // If needed difficulty is found
                        filteredByTagsAndDifficulty.add(recipe);               // - accept recipe
                    }
                }
            }
        }

        // 3rd level. Time
        if (timeFrom < 0 && timeTo < 0) { //No preferred time of preparation
            filteredByTagsDifficultyTime = filteredByTagsAndDifficulty;
        } else if (timeFrom >= 0 && timeTo < 0) { //Time limited only from below. ( "I'm planning to cook for an hour!" )
            for (RecipeWithIngredients recipe: filteredByTagsAndDifficulty) {
                if (recipe.recipe.prepareTime >= timeFrom) { // If preparation is longer or the same as needed
                    filteredByTagsDifficultyTime.add(recipe);          // - accept recipe
                }
            }
        } else { //Other two combinations are handled in normal way
            for (RecipeWithIngredients recipe: filteredByTagsAndDifficulty) {
                // If preparation time is between minimum and maximum or equals to one of them
                // - accept recipe
                if (recipe.recipe.prepareTime >= timeFrom && recipe.recipe.prepareTime <= timeTo) {
                    filteredByTagsDifficultyTime.add(recipe);
                }
            }
        }

        // 4th level. People
        if (peopleFrom < 0 && peopleTo < 0) { //No preferred amount of people
            filteredResultList = filteredByTagsDifficultyTime;
        } else if (peopleFrom >= 0 && peopleTo < 0) { //People limited only from below. ( "I'm planning to cook for at least 3 people" )
            for (RecipeWithIngredients recipe: filteredByTagsDifficultyTime) {
                if (recipe.recipe.numberOfServings >= peopleFrom) {
                    filteredResultList.add(recipe);          // - accept recipe
                }
            }
        } else { //Other two combinations are handled in normal way
            for (RecipeWithIngredients recipe: filteredByTagsDifficultyTime) {
                // If number of people is between minimum and maximum or equals to one of them
                // - accept recipe
                if (recipe.recipe.numberOfServings >= peopleFrom && recipe.recipe.numberOfServings <= peopleTo) {
                    filteredResultList.add(recipe);
                }
            }
        }

        return filteredResultList;
    }

    /**
     * Checks if a recipe has all given tags
     * @param tagsToFind
     * Tags that should be marked in a recipe
     * @param recipeToCheck
     * Checked recipe
     * @return
     * true - all tags are marked in a recipe, false - one tag wasn't found in a recipe.
     */
    private boolean checkRecipeForAllGivenTags(List<String> tagsToFind, RecipeWithIngredients recipeToCheck) {
        if (recipeToCheck.recipe.tags == null) { //Checking for null pointer
            return false;
        }

        List<String> tagsToCheck = recipeToCheck.recipe.tags;
        boolean exists = false;

        for (String tagToFind: tagsToFind) {
            for (String tagToCheck: tagsToCheck) {
                if (tagToFind.equals(tagToCheck)) { //Found tag in the recipe
                    exists = true;
                }
            }
            if (!exists) { //Haven't found tag in the list - so it doesn't exist in the recipe
                return false;
            }
            exists = false; //Setting boolean for the next tag
        }
        return true; //Found all tags
    }
}