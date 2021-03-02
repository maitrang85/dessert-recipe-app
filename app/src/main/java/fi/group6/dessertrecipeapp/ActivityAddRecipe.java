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
    LinearLayout instructionListLayout;
    Button addIngredient;
    Button addInstruction;

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

        instructionListLayout = findViewById(R.id.instruction_list);
        addInstruction = findViewById(R.id.addInstructionsButton);

        addIngredient.setOnClickListener(this);
        addInstruction.setOnClickListener(this);

        //Set the rating spinner to show numbers from 1-5
        Spinner ratingMenu = (Spinner) findViewById(R.id.ratingMenu);
        //Every possible value of the spinner
        String ratingLevels[] = {"easy", "medium", "hard"};
        //Set an adapter for the spinner
        ArrayAdapter<String> adapterRating = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ratingLevels);
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
        String rating = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        //Ingredient row is added when the user clicks the "ADD INGREDIENT" button
        switch (v.getId()) {
            case R.id.addIngredientButton:
                addIngredientRow();
                break;
            case R.id.addInstructionsButton:
                addInstructionRow();
                break;
        }
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

    private void addInstructionRow() {

        //Inflate the instructions row by one
        View instructionRow = getLayoutInflater().inflate(R.layout.add_instruction_row, null, false);

        //Connect the widgets inside the add_instructions_row.xml with the code
        EditText instructionStep = (EditText)instructionRow.findViewById(R.id.instructions);
        ImageView deleteInstructionRow = (ImageView)instructionRow.findViewById(R.id.image_delete_ins);

        //If the user clicks the delete button, that specific row will be deleted
        deleteInstructionRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeInsRow(instructionRow);
            }
        });
        instructionListLayout.addView(instructionRow);
    }

    private void removeRow(View view) {
        //When the user clicks the delete button, the ingredient row will be deleted
        ingredientListLayout.removeView(view);
    }

    private void removeInsRow(View view) {
        //When the user clicks the delete button, the ingredient row will be deleted
        instructionListLayout.removeView(view);
    }


}