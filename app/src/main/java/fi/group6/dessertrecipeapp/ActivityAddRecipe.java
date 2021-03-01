package fi.group6.dessertrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//TODO - configure "ADD RECIPE" button
//TODO - add recipe tag selection to the UI and the code
//TODO - figure out how to work with photos

public class ActivityAddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    LinearLayout ingredientListLayout;
    Button addIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        //Change title for the action bar
        getSupportActionBar().setTitle("Add recipe");
        //Add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ingredientListLayout = findViewById(R.id.ingredient_list);
        addIngredient = findViewById(R.id.addIngredientButton);

        addIngredient.setOnClickListener(this);

        //Set the rating spinner to show numbers from 1-5
        Spinner ratingMenu = findViewById(R.id.ratingMenu);
        //Every possible value of the spinner
        Integer[] ratingLevels = new Integer[]{1,2,3,4,5};
        //Set an adapter for the spinner
        ArrayAdapter<Integer> adapterRating = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, ratingLevels);
        //Set the style for the spinner menu
        adapterRating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingMenu.setAdapter(adapterRating);
        //Upon clicking the spinner, the user can choose the difficulty level from the ratingLevels array
        ratingMenu.setOnItemSelectedListener(this);

    }



    //Pressing the back button on the action bar will take the user back to the previous activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //The difficulty rating will be saved to an integer value when clicked
        Integer rating = (int) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        //Ingredient row is added when the user clicks the "ADD INGREDIENT" button
        addIngredientRow();
    }

    private void addIngredientRow() {

        //Inflate the ingredients row by one
        View ingredientRow = getLayoutInflater().inflate(R.layout.add_ingredient_row, null, false);

        //Connect the widgets inside the add_ingredient_row.xml with the code
        EditText ingredientAmount = (EditText)ingredientRow.findViewById(R.id.ingredientAmount);
        EditText ingredientMeasure = (EditText)ingredientRow.findViewById(R.id.ingredientMeasure);
        EditText ingredientName = (EditText)ingredientRow.findViewById(R.id.ingredientName);
        ImageView deleteIngredientRow = (ImageView)ingredientRow.findViewById(R.id.image_delete);

        //If the user clicks the delete button, that specific row will be deleted
        deleteIngredientRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeRow(ingredientRow);
            }
        });

        ingredientListLayout.addView(ingredientRow);
    }

    private void removeRow(View view) {
        //When the user clicks the delete button, the ingredient row will be deleted
        ingredientListLayout.removeView(view);
    }
}