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
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

/**
 * Add recipe activity displays an ui for creating a recipe.
 * It has two functions: recipe creation and recipe modification,
 * which is determined by the extra data from intent, that it receives.
 * @author Tamas and Daniil
 * @version 1.2
 */
public class ActivityAddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String ACTIVITY_ADD_RECIPE = "ACTIVITY_ADD_RECIPE";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_PICKER = 2;
    /*
    * RECIPE EDITING // - all code here, which is responsible for recipe editing will be marked.
    */
    // Used to get EXTRA from intent. ( Receives it from ActivityRecipe.java )
    private static final String EDIT_RECIPE_ID_KEY = "editRecipeId";
    //Recipe to be edited
    RecipeWithIngredients editedRecipe = null;
    //Flag set to true if editedRecipe is determined ( when the user actually edits a recipe, not creates a new one ).
    boolean editing = false;
    /*
    * RECIPE EDITING END
    */
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

    //Arrays and lists for the tag selector
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

        connectWidgets();

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

        /*
        * RECIPE EDITING
        */
        //****** Edit recipe variables ******//
        // If we received an intent -> get extra data, which is the recipe ID of a recipe, that was displayed on a recipe page
        Bundle b = getIntent().getExtras();
        int editRecipeId = -1; //is -1 in order to check if it is incorrect
        editing = false;
        if( b != null ) editRecipeId = b.getInt(EDIT_RECIPE_ID_KEY, -1);

        if( editRecipeId != -1 ) { // It will be -1 only in case if there is no recipe ID received -> user is creating a new recipe.
            editedRecipe = db.recipeDao().getRecipeWithIngredientsByRecipeId(editRecipeId);
            editing = true;
        }
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
                //Fill tagInput here, because user can omit using the tag selector.
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
        /*
        * RECIPE EDITING END
        */

        selectTags();
    }

    /**
     * This function is called in onCreate
     * All the widgets are connected to the xml ids
     * All the onClickListeners are set up
     */
    private void connectWidgets(){
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
    }

    /**
     * This function is called when the user clicks on the tag selector
     * Upon clicking, the user is taken to a pop-up view where they can select multiple tags
     * After selecting the tags, those will be saved inside an array
     * REFERENCES
     * https://www.youtube.com/watch?v=XrDVu3uPY3o&ab_channel=AndroidCoding
     */
    private void selectTags(){
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

    /**
     * This function is called when the user presses the back button
     * Pressing the back button on the action bar will take the user back to the previous activity
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * This function is called when the user selects a difficulty rating
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //The difficulty rating will be saved to an integer value when clicked
        String rating = (String) parent.getItemAtPosition(position);
    }

    /**
     * This function is called when the user does not select a difficulty rating
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {    }

    /**
     * This function is called when user clicks any of the buttons available
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Ingredient row is added when the user clicks the "ADD INGREDIENT" button
            case R.id.addIngredientButton:
                addIngredientRow();
                break;
            //Instruction row is added when the user clicks the "ADD INSTRUCTION STEP" button
            case R.id.addInstructionsButton:
                addInstructionRow();
                break;
            //Data validation process begins and user recipe is added if the data is validated
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
     */
    public Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, UUID.randomUUID().toString() , null);
        return Uri.parse(path);
    }

    /**
     * This function adds a new recipe to the room database or modifies an existing one
     */
    private void addNewRecipeWithIngredients() {
        //Two empty arraLists for the dinamically added TextViews
        instructions = new ArrayList<>();
        ingredients = new ArrayList<>();

        //Get user inputted data from the TextViews
        nameInput = name.getText().toString();
        authorInput = author.getText().toString();
        portionInput = Integer.parseInt(portions.getText().toString());
        prepTimeInput = Integer.parseInt(prepTime.getText().toString());

        //Get user inputted data from difficulty rating spinner
        Spinner ratingMenu = (Spinner) findViewById(R.id.levelOfDifficultyMenu);
        levelOfDifficultyInput = ratingMenu.getSelectedItem().toString();

        //Add elements to the instructions array, the amount depends on how many
        //instructions the user has filled out
        for(int i = 0; i < instructionListLayout.getChildCount(); i++){

            View instructionStepLayout = instructionListLayout.getChildAt(i);
            EditText instructionStep = (EditText)instructionStepLayout.findViewById(R.id.instructions);
            step = instructionStep.getText().toString();

            instructions.add(step);
        }

        //Add elements to the ingredients array, the amount depends on how many
        //ingredients the user has filled out
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

        //Prepare a recipe to be added to the database
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        myOwnRecipe = new Recipe(nameInput, instructions, tagInput, photoInput,
                true, false, portionInput, prepTimeInput, authorInput, levelOfDifficultyInput);

        /*
        * RECIPE EDITING
        */
        if (editing) { //Editing a recipe
            //Data must not be the same
            if (!modified(editedRecipe, new RecipeWithIngredients(myOwnRecipe, ingredients))) {
                Log.d(ACTIVITY_ADD_RECIPE, "Recipe wasn't modified");
                return; //Should start all over if they are the same
            }

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

            //HANDLING INGREDIENTS//

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

            //Sending user to the MyRecipes activity
            Intent intent = new Intent(ActivityAddRecipe.this, ActivityMyRecipes.class);
            startActivity(intent);

            Toast.makeText(ActivityAddRecipe.this,
                    "Congrats, you've modified a recipe!", Toast.LENGTH_LONG).show();
            /*
            * RECIPE EDITING END
            */
        } else { //Creating new recipe
            db.recipeDao().insertRecipeWithIngredients(myOwnRecipe, ingredients);

            //Sending user to the MyRecipes activity
            Intent intent = new Intent(ActivityAddRecipe.this, ActivityMyRecipes.class);
            startActivity(intent);

            Toast.makeText(ActivityAddRecipe.this,
                    "Congrats, you've created a new recipe!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Checks if data received from fields matches the requirements of the recipe to be complete.
     * @return
     * true - data given is correct, false - data is flawed, throws a reason why
     * REFERENCES
     * https://www.youtube.com/watch?v=DETCfQ_EOXo&t=669s&ab_channel=DroidGuru
     */
    private boolean checkDataValidity() {

        //Validating name input, checking name length
        if(name.getText().toString().trim().length() < 3){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter a recipe name, minimum 3 letters needed", Toast.LENGTH_SHORT).show();
            name.setError("Please enter a recipe name, minimum 3 letters needed");
            return false;
        }

        //Validating ingredient input, checking whether at least one ingredient was added
        if(ingredientListLayout.getChildCount() == 0){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please add at least one ingredient", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validating instruction input, checking if the field is empty
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

        //Validating portions input, checking if the field is empty
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

        //Validating prepTime input, checking if the field is empty
        if(instructionListLayout.getChildCount() == 0){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please add at least one instruction step", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validating instruction input, checking whether at least one ingredient was added
        for(int i = 0; i < instructionListLayout.getChildCount(); i++){

            View instructionStepLayout = instructionListLayout.getChildAt(i);
            EditText instructionStep = (EditText)instructionStepLayout.findViewById(R.id.instructions);

            if(instructionStep.getText().toString().equals("")) {
                Toast.makeText(ActivityAddRecipe.this,
                        "Please fill in or delete the the empty instruction texts", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        //Validating instruction input, checking if the field is empty
        if(author.getText().toString().trim().length() == 0){
            Toast.makeText(ActivityAddRecipe.this,
                    "Please enter an author name", Toast.LENGTH_SHORT).show();
            author.setError("Please enter an author name");
            return false;
        }

        return true;
    }

    /**
     * Adds an ingredient row to the ui with empty fields to fill
     * REFERENCES
     * https://www.youtube.com/watch?v=EJrmgJT2NnI&ab_channel=DroidGuru
     */
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

    /**
     * Adds an instructions row to the ui with an empty field to fill.
     * REFERENCES
     * https://www.youtube.com/watch?v=EJrmgJT2NnI&ab_channel=DroidGuru
     */
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

    /**
     * Removes ingredient row from the ui
     * @param view
     * Ingredient row to delete
     */
    private void removeIngredientRow(View view) {
        //When the user clicks the delete button, the ingredient row will be deleted
        ingredientListLayout.removeView(view);
    }

    /**
     * Removes instructions row from the ui
     * @param view
     * Instructions row to delete
     */
    private void removeInstructionRow(View view) {
        //When the user clicks the delete button, the ingredient row will be deleted
        instructionListLayout.removeView(view);
    }

    /*
    * RECIPE EDITING
    */
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
        //photo
        if (editedRecipe.recipe.photo != null) { //photo can be null, do nothing in case if it is
            //filling in photo placeholder
            ImageButton addPhotoPlaceholder = (ImageButton) findViewById(R.id.addPhotoPlaceholder);
            photoInput = editedRecipe.recipe.photo;
            addPhotoPlaceholder.setImageURI(Uri.parse(editedRecipe.recipe.photo));
        }
        //tags are omitted here, because it is handled separately.
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

            //If the user clicks the delete button, that specific row will be deleted
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
        if (!recipeToModify.recipe.name.equals(modifiedRecipe.recipe.name)) {
            return true;
        }
        if (!recipeToModify.recipe.author.equals(modifiedRecipe.recipe.author)) {
            return true;
        }
        if (!recipeToModify.recipe.levelOfDifficulty.equals(modifiedRecipe.recipe.levelOfDifficulty)) {
            return true;
        }
        if (recipeToModify.recipe.prepareTime != modifiedRecipe.recipe.prepareTime) {
            return true;
        }
        if (recipeToModify.recipe.numberOfServings != modifiedRecipe.recipe.numberOfServings) {
            return true;
        }
        if (!recipeToModify.recipe.instructions.equals(modifiedRecipe.recipe.instructions)) {
            return true;
        }
        if (!recipeToModify.recipe.tags.equals(modifiedRecipe.recipe.tags)) {
            return true;
        }
        //Photo check
        boolean photoSame;
        if (recipeToModify.recipe.photo != null && modifiedRecipe.recipe.photo != null) {
            photoSame = recipeToModify.recipe.photo.equals(modifiedRecipe.recipe.photo);
        } else if( recipeToModify.recipe.photo == null && modifiedRecipe.recipe.photo == null ) {
            photoSame = true;
        } else {
            photoSame = false;
        }

        if (!photoSame) {
            return true;
        }
        //Ingredients check
        if (recipeToModify.ingredients.size() != modifiedRecipe.ingredients.size()) {
            return true;
        } else {
            for (int i = 0; i < recipeToModify.ingredients.size(); i++) {
                if (!recipeToModify.ingredients.get(i).name.equals(modifiedRecipe.ingredients.get(i).name)) {
                    return true;
                }
                if (recipeToModify.ingredients.get(i).amount != modifiedRecipe.ingredients.get(i).amount) {
                    return true;
                }
                if (!recipeToModify.ingredients.get(i).measure.equals(modifiedRecipe.ingredients.get(i).measure)) {
                    return true;
                }
            }
        }

        Toast.makeText(ActivityAddRecipe.this,
                "Nothing was changed, recipe haven't been modified", Toast.LENGTH_LONG).show();
        return false;
    }
    /*
    * RECIPE EDITING END
    */
}
