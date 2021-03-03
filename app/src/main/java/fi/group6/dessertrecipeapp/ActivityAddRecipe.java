package fi.group6.dessertrecipeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fi.group6.dessertrecipeapp.classes.AppDatabase;
import fi.group6.dessertrecipeapp.classes.Ingredient;
import fi.group6.dessertrecipeapp.classes.Recipe;

//TODO - figure out how to work with photos

public class ActivityAddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    LinearLayout ingredientListLayout;
    LinearLayout instructionListLayout;

    TextView tagSelectorTv;
    TextView ingredient;

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
    String levelOfDifficultyInput;
    String ingredientNameInput;
    String ingredientMeasureInput;

    Integer portionInput;
    Integer prepTimeInput;

    Double ingredientAmountInput;

    List<String> instructions = new ArrayList<>();
    List<Ingredient> ingredients = new ArrayList<>();
    Recipe myOwnRecipe = new Recipe();

    String[] tagArray = {"Dairy-free", "Gluten-free", "Nut-free", "Keto diet", "Paleo diet", "Vegetarian",
            "Low-calorie", "Low-fat", "Low-carb", "Plant based", "Sweet", "No cooking needed", "Frozen dessert"};
    ArrayList<String> tagList = new ArrayList<>();
    boolean[] selectedTag;
    List<String> tagInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        //Change title for the action bar
        getSupportActionBar().setTitle("Add your own recipe");
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
        ingredient = findViewById(R.id.ingredient);

        addIngredientButton.setOnClickListener(this);
        addInstructionButton.setOnClickListener(this);
        addRecipeButton.setOnClickListener(this);

        //Set the rating spinner to show numbers from 1-5
        Spinner ratingMenu = (Spinner) findViewById(R.id.levelOfDifficultyMenu);
        //Every possible value of the spinner
        String ratingLevels[] = {"easy", "medium", "hard"};
        //Set an adapter for the spinner
        ArrayAdapter<String> adapterRating = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ratingLevels);
        //Set the style for the spinner menu
        adapterRating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingMenu.setAdapter(adapterRating);
        //Upon clicking the spinner, the user can choose the difficulty level from the ratingLevels array
        ratingMenu.setOnItemSelectedListener(this);

        tagSelectorTv = findViewById(R.id.tagSelectorTv);
        selectedTag = new boolean[tagArray.length];

        tagSelectorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAddRecipe.this);
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
                if(checkDataValidity()){
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

        Spinner ratingMenu = (Spinner) findViewById(R.id.levelOfDifficultyMenu);
        levelOfDifficultyInput = ratingMenu.getSelectedItem().toString();

        for(int i = 0; i < instructionListLayout.getChildCount(); i++){

            View instructionStepLayout = instructionListLayout.getChildAt(i);
            EditText instructionStep = (EditText)instructionStepLayout.findViewById(R.id.instructions);
            step = instructionStep.getText().toString();

            instructions.add(step);
        }

        for(int i = 0; i < ingredientListLayout.getChildCount(); i++){

            View ingredientStepLayout = ingredientListLayout.getChildAt(i);

            EditText ingredientName = (EditText)ingredientStepLayout.findViewById(R.id.ingredientName);
            EditText ingredientAmount = (EditText)ingredientStepLayout.findViewById(R.id.ingredientAmount);
            EditText ingredientMeasure = (EditText)ingredientStepLayout.findViewById(R.id.ingredientMeasure);

            ingredientNameInput = ingredientName.getText().toString();
            ingredientAmountInput = Double.valueOf(ingredientAmount.getText().toString());
            ingredientMeasureInput = ingredientMeasure.getText().toString();

            ingredients.add(new Ingredient(ingredientNameInput, ingredientAmountInput, ingredientMeasureInput));
        }

        myOwnRecipe = new Recipe(nameInput, instructions, tagInput, "Photo 1",
                true, false, portionInput, prepTimeInput, authorInput, levelOfDifficultyInput);

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        db.recipeDao().insertRecipeWithIngredients(myOwnRecipe, ingredients);

        Intent intent = new Intent(ActivityAddRecipe.this, ActivityMyRecipes.class);
        startActivity(intent);

        Toast.makeText(ActivityAddRecipe.this,
                "Congrats, you've created a new recipe!", Toast.LENGTH_LONG).show();
    }

    private boolean checkDataValidity() {
        boolean result = true;

        if(name.getText().toString().trim().length() < 3){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter a recipe name, minimum 3 letters needed", Toast.LENGTH_LONG).show();
            name.setError("Please enter a recipe name, minimum 3 letters needed");
            result = false;
            return result;
        }

        if(ingredientListLayout.getChildCount() == 0){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please add at least one ingredient", Toast.LENGTH_LONG).show();
            result = false;
            return result;
        }

        for(int i = 0; i < ingredientListLayout.getChildCount(); i++){
            View ingredientStepLayout = ingredientListLayout.getChildAt(i);

            EditText ingredientName = (EditText)ingredientStepLayout.findViewById(R.id.ingredientName);
            EditText ingredientAmount = (EditText)ingredientStepLayout.findViewById(R.id.ingredientAmount);
            EditText ingredientMeasure = (EditText)ingredientStepLayout.findViewById(R.id.ingredientMeasure);

            if(ingredientName.getText().toString().trim().length() == 0 || ingredientAmount.getText().toString().trim().length() == 0 ||
                    ingredientMeasure.getText().toString().trim().length() == 0) {
                Toast.makeText(ActivityAddRecipe.this,
                        "Please fill out or delete the the empty ingredient text", Toast.LENGTH_LONG).show();
                result = false;
                return result;
            }
        }

        if(portions.getText().toString().trim().length() == 0 || portions.getText().toString().equals("00") ||
                portions.getText().toString().equals("000") || portions.getText().toString().equals("0") ){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter a correct portion size", Toast.LENGTH_LONG).show();
            portions.setError("Please enter a correct portion size");
            result = false;
            return result;
        }

        if(prepTime.getText().toString().equals("0") || prepTime.getText().toString().equals("00") ||
                prepTime.getText().toString().equals("000") || prepTime.getText().toString().trim().length() == 0){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter a correct preparation time", Toast.LENGTH_LONG).show();
            prepTime.setError("Please enter a correct preparation time");
            result = false;
            return result;
        }

        if(instructionListLayout.getChildCount() == 0){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please add at least one instruction step", Toast.LENGTH_LONG).show();
            result = false;
            return result;
        }

        for(int i = 0; i < instructionListLayout.getChildCount(); i++){

            View instructionStepLayout = instructionListLayout.getChildAt(i);
            EditText instructionStep = (EditText)instructionStepLayout.findViewById(R.id.instructions);

            if(instructionStep.getText().toString().equals("")) {
                Toast.makeText(ActivityAddRecipe.this,
                        "Please fill out or delete the the empty instruction texts", Toast.LENGTH_LONG).show();
                result = false;
                return result;
            }
        }

        if(author.getText().toString().trim().length() == 0){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter an author name", Toast.LENGTH_LONG).show();
            author.setError("Please enter an author name");
            result = false;
            return result;
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
