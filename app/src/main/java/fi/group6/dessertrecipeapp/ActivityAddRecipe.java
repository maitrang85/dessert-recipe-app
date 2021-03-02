package fi.group6.dessertrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO - configure "ADD RECIPE" button
//TODO - add recipe tag selection to the UI and the code
//TODO - figure out how to work with photos

public class ActivityAddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    LinearLayout ingredientListLayout;
    LinearLayout instructionListLayout;

    EditText name;
    EditText author;
    EditText portions;
    EditText prepTime;

    Button addIngredientButton;
    Button addInstructionButton;
    Button addRecipeButton;

    String nameInput;
    String step;
    String authorInput;
    String difficultyRating;

    Integer portionInput;
    Integer prepTimeInput;

    List<String> instructions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        //Change title for the action bar
        getSupportActionBar().setTitle("Add recipe");
        //Add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ingredientListLayout = findViewById(R.id.ingredient_list);
        addIngredientButton = findViewById(R.id.addIngredientButton);

        instructionListLayout = findViewById(R.id.instruction_list);
        addInstructionButton = findViewById(R.id.addInstructionsButton);
        addRecipeButton = findViewById(R.id.addRecipeButton);

        name = findViewById(R.id.editTextTextPersonName);
        author = findViewById(R.id.author);
        portions = findViewById(R.id.portionSize);
        prepTime = findViewById(R.id.prepTime);

        addIngredientButton.setOnClickListener(this);
        addInstructionButton.setOnClickListener(this);
        addRecipeButton.setOnClickListener(this);

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
            case R.id.addRecipeButton:
                if(checkdataValidity()){
                    addNewRecipeWithIngredients();
                }
                break;
        }
    }

    private void addNewRecipeWithIngredients() {

        nameInput = name.getText().toString();
        authorInput = author.getText().toString();
        portionInput = Integer.parseInt(portions.getText().toString());
        prepTimeInput = Integer.parseInt(prepTime.getText().toString());

        Spinner ratingMenu = (Spinner) findViewById(R.id.ratingMenu);
        difficultyRating = ratingMenu.getSelectedItem().toString();

        Log.d("Rating is", difficultyRating);

        for(int i = 0; i < instructionListLayout.getChildCount(); i++){

            View instructionStepLayout = instructionListLayout.getChildAt(i);
            EditText instructionStep = (EditText)instructionStepLayout.findViewById(R.id.instructions);
            step = instructionStep.getText().toString();

            if(!instructionStep.getText().toString().equals("")){
                instructions.add(step);
            }
        }

    }

    private boolean checkdataValidity() {

        boolean result = true;

        if(name.getText().toString().equals("")){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter a recipe name", Toast.LENGTH_LONG).show();
        }

        //INGREDIENTS GO HERE

        if(portions.getText().toString().equals("0") || portions.getText().toString().equals("00") ||
                portions.getText().toString().equals("000") || portions.getText().toString().equals("")){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter a correct portion size", Toast.LENGTH_LONG).show();
            result = false;
        }

        if(prepTime.getText().toString().equals("0") || prepTime.getText().toString().equals("00") ||
                prepTime.getText().toString().equals("000") || prepTime.getText().toString().equals("")){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter a correct preparation time", Toast.LENGTH_LONG).show();
            result = false;
        }

        if(instructionListLayout.getChildCount() == 0){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please add at least one instruction step", Toast.LENGTH_LONG).show();
            result = false;
        }

        for(int i = 0; i < instructionListLayout.getChildCount(); i++){

            View instructionStepLayout = instructionListLayout.getChildAt(i);
            EditText instructionStep = (EditText)instructionStepLayout.findViewById(R.id.instructions);

            if(instructionStep.getText().toString().equals("")) {
                Toast.makeText(ActivityAddRecipe.this,
                        "Please fill out or delete the the empty instruction texts", Toast.LENGTH_LONG).show();
                result = false;
            }
        }

        if(author.getText().toString().equals("")){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter an author name", Toast.LENGTH_LONG).show();
        }

        return result;
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
                removeIngredientRow(ingredientRow);
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
                removeInstructionRow(instructionRow);
            }
        });
        instructionListLayout.addView(instructionRow);
    }

    private void removeIngredientRow(View view) {
        //When the user clicks the delete button, the ingredient row will be deleted
        ingredientListLayout.removeView(view);
    }

    private void removeInstructionRow(View view) {
        //When the user clicks the delete button, the ingredient row will be deleted
        instructionListLayout.removeView(view);
    }


}