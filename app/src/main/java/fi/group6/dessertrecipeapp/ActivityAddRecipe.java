package fi.group6.dessertrecipeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import fi.group6.dessertrecipeapp.classes.AppDatabase;
import fi.group6.dessertrecipeapp.classes.Ingredient;
import fi.group6.dessertrecipeapp.classes.Recipe;
import fi.group6.dessertrecipeapp.classes.RecipeWithIngredients;

//TODO - figure out how to work with photos

public class ActivityAddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String ACTIVITY_ADD_RECIPE = "ACTIVITY_ADD_RECIPE";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_PICKER = 2;

    // Recipe editing
    private static final String EDIT_RECIPE_ID_KEY = "editRecipeId";
        //Recipe to be edited
    RecipeWithIngredients editedRecipe = null;
        //Flag set to true if editedRecipe is determined.
    boolean editing = false;
    // Recipe editing end

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
    ImageButton addPhotoPlaceholder;

    String nameInput;
    String step;
    String authorInput;
    String levelOfDifficultyInput;
    String ingredientNameInput;
    String ingredientMeasureInput;
    String photoInput;

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

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        //Change title for the action bar
        getSupportActionBar().setTitle("Add your own recipe");
        //Add back button to the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ingredientListLayout = findViewById(R.id.ingredient_list);
        addIngredientButton = findViewById(R.id.addIngredientButton);
        addPhotoPlaceholder = findViewById(R.id.addPhotoPlaceholder);

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
        addPhotoPlaceholder.setOnClickListener(this);

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

        //****** Edit recipe variables ******//
        Bundle b = getIntent().getExtras();
        int editRecipeId = -1;
        editing = false;
        if( b != null ) editRecipeId = b.getInt(EDIT_RECIPE_ID_KEY, -1);

        if( editRecipeId != -1 ) {
            editedRecipe = db.recipeDao().getRecipeWithIngredientsByRecipeId(editRecipeId);
            editing = true;
        }

        if (editing) Log.d(ACTIVITY_ADD_RECIPE, "Received:\n" + editedRecipe.recipe.toString()); //debug
        else Log.d(ACTIVITY_ADD_RECIPE, "Not editing any recipe."); //debug

        //****** Edit recipe variables end ******//
        //****** Edit recipe setup ******//
        if (editing) {

            //Renaming some adding stuff
            getSupportActionBar().setTitle("Modify recipe");
            addRecipeButton.setText(R.string.modify_recipe_caps);

            //Filling in contents from the received recipe
            fillInContents(editedRecipe);

            //Tags handling
            if (editedRecipe.recipe.tags != null) { //1. Only if recipe has tags
                for (String tag : editedRecipe.recipe.tags) { //2. For each tag in a recipe
                    for (int i = 0; i < tagArray.length; i++) { //3. Go for every possible tag
                        if (tag.equals(tagArray[i])) { //4. Check on which place this tag is found
                            selectedTag[i] = true; //5. Select that tag
                            tagList.add(tag);

                        }
                    }
                }
                //Fill tagInput here, because user can omit using the ui.
                tagInput = new ArrayList<>();
                String items = "";

                for(int element = 0; element < tagList.size(); element++){

                    items = items + tagList.get(element);
                    tagInput.add(tagList.get(element).toString());

                    if(element != tagList.size() - 1){
                        items += ", ";
                    }
                }
                tagSelectorTv.setText(items);
            }
        }
        //****** Edit recipe setup end ******//

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
                if (checkDataValidity()){
                    addNewRecipeWithIngredients();
                }
                break;
            case R.id.addPhotoPlaceholder:
                selectPhoto();
                break;

        }
    }

    /**
     * This function is called when user click imageButton in order to choose photo.
     * User can choose to take photo with camera(with permission) or select photo from Gallery, or
     * Cancel selecting photo.
     */
    private void selectPhoto() {
        final CharSequence[] options = {"Use your camera", "Choose a photo from  your gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ActivityAddRecipe.this);
        builder.setTitle("Add Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Use your camera")) {
                    // Request for camera Persmission
                    if (ContextCompat.checkSelfPermission(ActivityAddRecipe.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ActivityAddRecipe.this,
                                new String[]{
                                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                                }, 0);
                    } else {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else if (options[item].equals("Choose a photo from  your gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_GALLERY_PICKER);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * This function is the result after user selects photo. Photos are saved as bitmap, then be converted
     * into String by function getImageUri.
     * @param requestCode
     * @param resultCode
     * @param imageReturnedIntent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri selectedImage = null;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    selectedImage = getImageUri(this,imageBitmap);
                    photoInput = selectedImage.toString();
                    addPhotoPlaceholder.setImageURI(selectedImage);
                    break;
                case REQUEST_GALLERY_PICKER:
                    selectedImage = imageReturnedIntent.getData();
                    photoInput = selectedImage.toString();
                    addPhotoPlaceholder.setImageURI(selectedImage);
                    break;
            }
        }
    }

    /**
     * This getImageUri function first changes Bitmap image into Stream bytes. Then compress image into
     * JPEG format. Finally it returns the String of the Uri.
     * @param context
     * @param inImage
     * @return
     */
    public Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, UUID.randomUUID().toString() , null);
        return Uri.parse(path);
    }

    private void addNewRecipeWithIngredients() {
        instructions = new ArrayList<>();
        ingredients = new ArrayList<>();

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

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        myOwnRecipe = new Recipe(nameInput, instructions, tagInput, photoInput,
                true, false, portionInput, prepTimeInput, authorInput, levelOfDifficultyInput);

        if (editing) { //Editing a recipe
            //Data must not be the same
            if (!modified(editedRecipe, new RecipeWithIngredients(myOwnRecipe, ingredients))) {
                Log.d(ACTIVITY_ADD_RECIPE, "Recipe wasn't modified");
                return; //Should start all over if they are the same
            }

            //DEBUG // Do not remove it until photo will be added and no mistakes will be encountered
            /*
            if(editedRecipe.ingredients.equals(ingredients)) Log.e("ERROR", "ingredients - same");
            else Log.e("ERROR", "ingredients - not same");
            if(editedRecipe.recipe.name.equals(myOwnRecipe.name)) Log.e("ERROR", "name - same");
            else Log.e("ERROR", "name - not same");
            if(editedRecipe.recipe.author.equals(myOwnRecipe.author)) Log.e("ERROR", "author - same");
            else Log.e("ERROR", "author - not same");
            if(editedRecipe.recipe.levelOfDifficulty.equals(myOwnRecipe.levelOfDifficulty)) Log.e("ERROR", "difficulty - same");
            else Log.e("ERROR", "difficulty - not same");
            if(editedRecipe.recipe.prepareTime == myOwnRecipe.prepareTime) Log.e("ERROR", "prepareTime - same");
            else Log.e("ERROR", "prepareTime - not same");
            if(editedRecipe.recipe.numberOfServings == myOwnRecipe.numberOfServings) Log.e("ERROR", "servings - same");
            else Log.e("ERROR", "servings - not same");
            if(editedRecipe.recipe.photo.equals(myOwnRecipe.photo)) Log.e("ERROR", "photo - same");
            else Log.e("ERROR", "photo - not same");
            if(editedRecipe.recipe.instructions.equals(myOwnRecipe.instructions)) Log.e("ERROR", "instructions - same");
            else Log.e("ERROR", "instructions - not same");
            if(editedRecipe.recipe.tags.equals(myOwnRecipe.tags)) Log.e("ERROR", "tags - same");
            else Log.e("ERROR", "tags - not same");
            */
            //DEBUG

            //Rebuilding a recipe
            editedRecipe.recipe.name = nameInput;
            editedRecipe.recipe.instructions = instructions;
            editedRecipe.recipe.tags = tagInput;
            editedRecipe.recipe.photo = photoInput;
            editedRecipe.recipe.isCustom = true;
            editedRecipe.recipe.numberOfServings = portionInput;
            editedRecipe.recipe.prepareTime = prepTimeInput;
            editedRecipe.recipe.author = authorInput;
            editedRecipe.recipe.levelOfDifficulty = levelOfDifficultyInput;

            //Handling ingredients
            //Deleting all previous ingredients from the database
            for (int i = 0; i < editedRecipe.ingredients.size(); i++) {
                db.recipeDao().deleteIngredient(editedRecipe.ingredients.get(i));
            }

            //Changing the ingredients list
            editedRecipe.ingredients = ingredients;
            //Giving ingredients correct recipeId
            for (int i = 0; i < editedRecipe.ingredients.size(); i++) {
                editedRecipe.ingredients.get(i).recipeId = editedRecipe.recipe.recipeId;
            }

            //Adding new ingredients to the database
            db.recipeDao().insertAllIngredients(editedRecipe.ingredients);
            //Giving recipe back to the database
            db.recipeDao().updateIngredientWithRecipe(editedRecipe.recipe, editedRecipe.ingredients);

            Intent intent = new Intent(ActivityAddRecipe.this, ActivityMyRecipes.class);
            startActivity(intent);

            Toast.makeText(ActivityAddRecipe.this,
                    "Congrats, you've modified a recipe!", Toast.LENGTH_LONG).show();
        } else { //Creating new recipe
            db.recipeDao().insertRecipeWithIngredients(myOwnRecipe, ingredients);

            Intent intent = new Intent(ActivityAddRecipe.this, ActivityMyRecipes.class);
            startActivity(intent);

            Toast.makeText(ActivityAddRecipe.this,
                    "Congrats, you've created a new recipe!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkDataValidity() {

        if(name.getText().toString().trim().length() < 3){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter a recipe name, minimum 3 letters needed", Toast.LENGTH_SHORT).show();
            name.setError("Please enter a recipe name, minimum 3 letters needed");
            return false;
        }

        if(ingredientListLayout.getChildCount() == 0){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please add at least one ingredient", Toast.LENGTH_SHORT).show();
            return false;
        }

        for(int i = 0; i < ingredientListLayout.getChildCount(); i++){
            View ingredientStepLayout = ingredientListLayout.getChildAt(i);

            EditText ingredientName = (EditText)ingredientStepLayout.findViewById(R.id.ingredientName);
            EditText ingredientAmount = (EditText)ingredientStepLayout.findViewById(R.id.ingredientAmount);
            EditText ingredientMeasure = (EditText)ingredientStepLayout.findViewById(R.id.ingredientMeasure);

            if(ingredientName.getText().toString().trim().length() == 0 || ingredientAmount.getText().toString().trim().length() == 0 ||
                    ingredientMeasure.getText().toString().trim().length() == 0) {
                Toast.makeText(ActivityAddRecipe.this,
                        "Please fill in or delete the the empty ingredient text", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if(portions.getText().toString().trim().length() == 0 || portions.getText().toString().equals("00") ||
                portions.getText().toString().equals("000") || portions.getText().toString().equals("0") ){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter a correct portion size", Toast.LENGTH_SHORT).show();
            portions.setError("Please enter a correct portion size");
            return false;
        }

        if(prepTime.getText().toString().equals("0") || prepTime.getText().toString().equals("00") ||
                prepTime.getText().toString().equals("000") || prepTime.getText().toString().trim().length() == 0){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter a correct preparation time", Toast.LENGTH_SHORT).show();
            prepTime.setError("Please enter a correct preparation time");
            return false;
        }

        if(instructionListLayout.getChildCount() == 0){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please add at least one instruction step", Toast.LENGTH_SHORT).show();
            return false;
        }

        for(int i = 0; i < instructionListLayout.getChildCount(); i++){

            View instructionStepLayout = instructionListLayout.getChildAt(i);
            EditText instructionStep = (EditText)instructionStepLayout.findViewById(R.id.instructions);

            if(instructionStep.getText().toString().equals("")) {
                Toast.makeText(ActivityAddRecipe.this,
                        "Please fill in or delete the the empty instruction texts", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if(author.getText().toString().trim().length() == 0){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter an author name", Toast.LENGTH_SHORT).show();
            author.setError("Please enter an author name");
            return false;
        }

        return true;
    }

    /**
     * Checks whether recipe was modified
     * @param recipeToModify
     * Recipe chosen to modify
     * @param modifiedRecipe
     * Recipe created from fields
     * @return
     * true - something was modified, false - nothing was modified
     */
    private boolean modified(RecipeWithIngredients recipeToModify, RecipeWithIngredients modifiedRecipe) {
        if (!recipeToModify.equals(modifiedRecipe)) {
            return true;
        }
        Toast.makeText(ActivityAddRecipe.this,
                "Nothing was changed, recipe haven't been modified", Toast.LENGTH_LONG).show();
        return false;
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

    //*********************//
    //*EDIT RECIPE METHODS*//
    //*********************//

    /**
     * Fills all fields of the AddRecipeActivity with the data of the recipe to be edited.
     * @param recipe
     * Recipe to edit
     */
    private void fillInContents(RecipeWithIngredients recipe) {
        if (recipe == null) {
            Log.e(ACTIVITY_ADD_RECIPE, "No recipe received. Fields left empty.");
            return;
        }

        name = findViewById(R.id.editTextTextPersonName);
        author = findViewById(R.id.author);
        portions = findViewById(R.id.portionSize);
        prepTime = findViewById(R.id.prepTime);

        //Filling in fields
        //name
        name.setText(recipe.recipe.name);
        //author
        author.setText(recipe.recipe.author);
        //portions
        portions.setText(Integer.toString(recipe.recipe.numberOfServings));
        //Preparation time
        prepTime.setText(Integer.toString(recipe.recipe.prepareTime));
        //difficulty
        presetDifficulty(recipe.recipe.levelOfDifficulty);
        //instructions
        presetAddFilledInstructionRows(recipe.recipe.instructions);
        //ingredients
        presetAddFilledIngredientRows(recipe.ingredients);
        //photo //TODO: handle photo
        if (editedRecipe.recipe.photo != null){
            ImageButton addPhotoPlaceholder = (ImageButton) findViewById(R.id.addPhotoPlaceholder);
            photoInput = editedRecipe.recipe.photo;
            addPhotoPlaceholder.setImageURI(Uri.parse(editedRecipe.recipe.photo));
        }

        // tags are omitted here, because it is handled separately.


    }

    /**
     * Fills rows of instructions with the data of the recipe to be edited.
     * @param instructions
     * Instructions to fill in.
     */
    private void presetAddFilledInstructionRows(List<String> instructions) {
        for(String instruction: instructions) {
            //Inflate the instructions row by one
            View instructionRow = getLayoutInflater().inflate(R.layout.add_instruction_row, null, false);

            //Connect the widgets inside the add_instructions_row.xml with the code
            EditText instructionStep = (EditText) instructionRow.findViewById(R.id.instructions);
            ImageView deleteInstructionRow = (ImageView) instructionRow.findViewById(R.id.image_delete_ins);

            //Fill in the field with instruction from a recipe
            instructionStep.setText(instruction);

            //If the user clicks the delete button, that specific row will be deleted
            deleteInstructionRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeInstructionRow(instructionRow);
                }
            });
            instructionListLayout.addView(instructionRow);
        }
    }

    /**
     * Fills ingredient rows with ingredients of the recipe to be edited.
     * @param ingredients
     * Ingredients to fill in
     */
    private void presetAddFilledIngredientRows(List<Ingredient> ingredients) {

        for (Ingredient ingredient: ingredients) {
            //Inflate the ingredients row by one
            View ingredientRow = getLayoutInflater().inflate(R.layout.add_ingredient_row, null, false);

            //Connect the widgets inside the add_ingredient_row.xml with the code
            EditText ingredientAmount = (EditText) ingredientRow.findViewById(R.id.ingredientAmount);
            EditText ingredientMeasure = (EditText) ingredientRow.findViewById(R.id.ingredientMeasure);
            EditText ingredientName = (EditText) ingredientRow.findViewById(R.id.ingredientName);
            ImageView deleteIngredientRow = (ImageView) ingredientRow.findViewById(R.id.image_delete);

            //Filling in contents
            ingredientAmount.setText(Double.toString(ingredient.amount));
            ingredientMeasure.setText(ingredient.measure);
            ingredientName.setText(ingredient.name);

            //If the user clicks the delete button, that specific row will be deleted //TODO: Great place to add check?
            deleteIngredientRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeIngredientRow(ingredientRow);
                }
            });
            ingredientListLayout.addView(ingredientRow);
        }
    }

    /**
     * Sets difficulty from the recipe to be edited.
     * @param levelOfDifficulty
     * difficulty to set
     */
    private void presetDifficulty(String levelOfDifficulty) {
        Spinner ratingMenu = (Spinner) findViewById(R.id.levelOfDifficultyMenu);
        int index;

        switch (levelOfDifficulty) {
            case "easy":
                index = 0;
                break;
            case "medium":
                index = 1;
                break;
            case "hard":
                index = 2;
                break;
            default:
                index = -1;
                break;
        }
        //Check if a recipe doesn't have right info for difficulty
        if (index == -1) {
            Log.e(ACTIVITY_ADD_RECIPE, "Index for difficulty isn't set");
            return;
        }
        //Select right difficulty
        ratingMenu.setSelection(index);
    }
}
