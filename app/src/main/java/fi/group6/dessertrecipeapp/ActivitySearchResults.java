package fi.group6.dessertrecipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fi.group6.dessertrecipeapp.classes.AppDatabase;
import fi.group6.dessertrecipeapp.classes.RecipeWithIngredients;

public class ActivitySearchResults extends AppCompatActivity {

    private static final String RESULTS = "SEARCH_RESULTS";

    ArrayList<Integer> resultsID = new ArrayList<>();
    List<RecipeWithIngredients> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        //Change title for the action bar
        getSupportActionBar().setTitle("SEARCH RESULTS");
        //Add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        resultsID = getIntent().getIntegerArrayListExtra(RESULTS);

        for(int i = 0; i < resultsID.size(); i++){
            searchResults.add(db.recipeDao().getRecipeWithIngredientsByRecipeId(resultsID.get(i)));
        }

        initRecyclerView();
    }

    //Pressing the back button on the action bar will take the user back to the previous activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initRecyclerView() {
        Log.d("ZSA", "initRecyclerView: init recyclerView");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(searchResults, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}