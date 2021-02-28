package fi.group6.dessertrecipeapp.classes;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Models a dessert recipe
 * @author Trang
 * Refactored by Daniil
 * @version 1.2
 */
@Entity
public class Recipe implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long uid;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "ingredients")
    private String ingredientsList; // comma and semicolon separated values
    @ColumnInfo(name = "instructions")
    private String instructions; // ` - is a delimiter
    @ColumnInfo(name = "tags")
    private String tags; //csv
    @ColumnInfo(name = "photo_reference")
    private String photo;
    @ColumnInfo(name = "is_custom")
    private boolean custom;
    @ColumnInfo(name = "number_of_servings")
    private int numberOfServings;
    @ColumnInfo(name = "preparation_time")
    private int prepareTime;
    @ColumnInfo(name = "author")
    private String authorName;
    @ColumnInfo(name = "rating")
    private float grade;

    private static final String TAG = "RECIPE_CLASS";

    //**************//
    // Constructors //
    //**************//
    /**
     * Constructor
     * Creates a recipe
     * @param name
     * Name of recipe
     * @param custom
     * Whether the recipe is saved locally or not
     * @param authorName
     * Name of author
     * @param numberOfServings
     * Number of servings in that recipe
     * @param grade
     * Rating level
     * @param prepareTime
     * Time of preparation
     */
    public Recipe(String name, String ingredientsList, String instructions, String tags,
                    String photo, boolean custom, int numberOfServings, int prepareTime, String authorName, float grade) {
        this.name = name;
        this.ingredientsList = ingredientsList;
        this.instructions = instructions;
        this.tags = tags;
        this.photo = photo;
        this.custom = custom;
        this.numberOfServings = numberOfServings;
        this.prepareTime = prepareTime;
        this.authorName = authorName;
        this.grade = grade;
    }

    /**
     * Constructor
     * Creates a dummy for recipe
     */
    public Recipe() {
        this("Dummy Recipe", "IngredientName,13.2,kg;Ingredient2Name,22.16,l;",
                "Instruction`Instruction2`", "Tag,Tag2,", "", false, 1, 0, "Generic", 0);
    }

    /**
     * Constructor
     * Creates a recipe with empty arrays
     * @param name
     * Name of recipe
     * @param photo
     * Photo path
     * @param custom
     * Whether the recipe is saved locally or not
     * @param numberOfServings
     * Number of servings in that recipe
     * @param prepareTime
     * Time of preparation
     * @param authorName
     * Name of author
     * @param grade
     * Rating level
     */
    public Recipe(String name, String photo, boolean custom, int numberOfServings, int prepareTime, String authorName, float grade) {
        this (name, "IngredientName,13.2,kg;Ingredient2Name,22.16,l;",
                "Instruction`Instruction2`", "Tag,Tag2,", photo, custom, numberOfServings, prepareTime, authorName, grade);
    }

    //*********//
    // Methods //
    //*********//
    /**
     * Gives name of the food recipe
     * @return
     * name
     */
    public String getName() {
        return this.name;
    }
    /**
     * To set the name of the recipe
     * @param name
     * new name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * To get the list of ingredients
     * @return
     * ingredient
     */
    public ArrayList<Ingredient> getIngredientList() {
        return ingredientStringToArrayList(this.ingredientsList);
    }
    /**
     * Sets new Ingredient list
     * @param ingredientsList
     * New ingredient list to set
     */
    public void setIngredientsList(ArrayList<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientArrayListToString(ingredientsList);
    }
    /**
     * To get the photo for recipe
     * @return
     * Reference to photo
     */
    public String getPhoto() {
        return this.photo;
    }
    /**
     * To change photo for recipe
     * @param photo
     * New photo reference
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    /**
     * Whether the recipe will be saved locally or not
     * @param custom
     * Custom = true, Premade = false.
     */
    public void setCustom(boolean custom) {
        this.custom = custom;
    }
    /**
     * To get whether the recipe is custom or not
     * @return
     * true - if it is custom, false - if generic
     */
    public boolean isCustom() {
        return this.custom;
    }
    /**
     * To get number of servings in recipe
     * @return
     * Number of people to served
     */
    public int getNumberOfServings() {
        return this.numberOfServings;
    }
    /**
     * To change number of servings
     * @param numberOfServings
     * New number of servings
     */
    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }
    /**
     * To get the preparation time
     * @return
     * Preparation time in minutes
     */
    public int getPrepareTime() {
        return this.prepareTime;
    }
    /**
     * To change the time for making food
     * @param prepareTime
     * New preparation time
     */
    public void setPrepareTime(int prepareTime) {
        this.prepareTime = prepareTime;
    }
    /**
     * To get name of person who creates the recipe
     * @return
     * Author name
     */
    public String getAuthorName() {
        return this.authorName;
    }
    /**
     * To set the name of author
     * @param authorName
     * New author name
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    /**
     * To get rating grade
     * @return
     * Rating
     */
    public float getGrade() {
        return this.grade;
    }
    /**
     * To set the rating grade
     * @param grade
     * New grade
     */
    public void setGrade(float grade) {
        this.grade = grade;
    }
    /**
     * Add new tag to the tag list
     * @param newTag
     * Tag to add
     */
    public void addTag(String newTag) {
        this.tags += newTag + ",";
    }
    /**
     * To check whether a tag is in the tags list
     * @param tagToCheck
     * Tag to find in the list
     * @return
     * true - there is, false - there is none
     */
    public boolean checkTagInRecipe(String tagToCheck) {
        List<String> tagsList = new ArrayList<>();
        //Converting tags String to the tagsList
        tagsList = tagsStringToList(this.tags);
        for (String tag: tagsList) {
            if (tag.equals(tagToCheck)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Removes specified tag from the list of tags
     * @param tagToRemove
     * Tag to remove from the list
     */
    public void removeTag(String tagToRemove) {
        ArrayList<String> tagsArrayList = new ArrayList<>();
        //Converting tags String to the tagsList and then tagsArrayList
        tagsArrayList.addAll(tagsStringToList(this.tags));

        int i;
        for (i = 0; i < tagsArrayList.size(); i++) {
            if(tagToRemove.equals(tagsArrayList.get(i))) {
                tagsArrayList.remove(i);
                break; //To save time, because there should be only one such specific tag
            }
        }

        this.tags = tagsListToString(tagsArrayList);
    }
    /**
     * Gives a raw string of tags
     * @return
     * String of tags delimited by ","
     */
    public String getTagsString() {
        return this.tags;
    }
    /**
     * Gives a list of tags
     * @return
     * List of tags
     */
    public List<String> getTagsList() {
        return tagsStringToList(this.tags);
    }
    /**
     * To add an ingredient into the ingredient list
     * @param ingredient
     * Ingredient to add
     */
    public void addIngredient(Ingredient ingredient) {
        this.ingredientsList += ingredient.getIngredientName();
        this.ingredientsList += ",";
        this.ingredientsList += ingredient.getIngredientAmount();
        this.ingredientsList += ",";
        this.ingredientsList += ingredient.getIngredientMeasure();
        this.ingredientsList += ";";
    }
    /**
     * To get an ingredient by using its index from the list
     * @param index
     * Index of ingredient to get
     * @return
     * Ingredient by index
     */
    public Ingredient getIngredient (int index) {
        //Converting to the ArrayList of Ingredients and getting right ingredient
        return ingredientStringToArrayList(this.ingredientsList).get(index);
    }
    /**
     * To get how many ingredients in the list
     * @return
     * ingredients list size
     */
    public int getNumOfIngredients() {
        return splitString(this.ingredientsList, ";").size(); // it is enough, no need to convert any further
    }
    /**
     * To delete an ingredient from the ingredient list
     * @param indexIngredient
     * Index of the ingredient to delete
     */
    public void deleteIngredient(int indexIngredient) {
        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        // Converting String to the ArrayList of Ingredients
        ingredientArrayList = ingredientStringToArrayList(this.ingredientsList);

        if(indexIngredient >= 0 && indexIngredient < ingredientArrayList.size()) {
            ingredientArrayList.remove(indexIngredient); // deleting ingredient from the array
        } else {
            Log.e(TAG, "Incorrect index for the ingredient:" + Integer.toString(indexIngredient) +  ". Cannot remove it.");
        }

        // Converting back to the String
        String newList = ingredientArrayListToString(ingredientArrayList);

        this.ingredientsList = newList;
    }
    /**
     * To modify an ingredient
     * @param indexIngredient
     * Ingredient to be modified
     * @param ingredient
     * Ingredient to set on the previous ingredient place
     */
    public void changeIngredient(int indexIngredient, Ingredient ingredient) {
        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        // Converting String to the ArrayList of Ingredients
        ingredientArrayList = ingredientStringToArrayList(this.ingredientsList);

        // Modifying chosen ingredient
        if(indexIngredient >= 0 && indexIngredient < ingredientArrayList.size()) {
            ingredientArrayList.set(indexIngredient, ingredient);
        } else {
            Log.e(TAG, "Incorrect index for the ingredient:" + Integer.toString(indexIngredient) + ".Cannot modify it.");
        }

        // Converting back to the String
        this.ingredientsList = ingredientArrayListToString(ingredientArrayList);
    }
    /**
     * To add instructions into the instruction list
     * @param instruction
     * String with instruction text
     */
    public void addInstructions(String instruction) {
        this.instructions += instruction;
        this.instructions += "`";
    }
    /**
     * To modify an instruction
     * @param indexInstruction
     * String with the previous instruction text
     * @param instruction
     * String with the new instruction text
     */
    public void modifyInstructions(int indexInstruction, String instruction) {
        List<String> instructionStrings = new ArrayList<>();
        // Converting to the List of Instructions
        instructionStrings = instructionsStringToList(this.instructions);

        // Modifying instruction inside of the Array
        if(indexInstruction >= 0 && indexInstruction < instructionStrings.size()) {
            instructionStrings.set(indexInstruction, instruction);
        } else {
            Log.e(TAG, "Impossible to modify instruction, index is incorrect:" + Integer.toString(indexInstruction) + "\n" +
                    "In order to add new instruction use addInstructions() ");
        }

        // Converting back to the String
        this.instructions = instructionsListToString(instructionStrings);
    }
    /**
     * Gets raw instructions string.
     * @return
     * Instructions string delimited by "`"
     */
    public String getRawInstructions() {
        return this.instructions;
    }
    /**
     * To get all instruction strings
     * @return
     * Readable instructions
     */
    public String getAllInstructions() {
        String readableInstructions = "";
        List<String> instructionsList = new ArrayList<>();
        // Converting to the List of Instructions
        instructionsList = instructionsStringToList(this.instructions);

        for( String instruction: instructionsList ) {
            readableInstructions += instruction;
            readableInstructions += "\n";
        }

        return readableInstructions;
    }
    /**
     * To get instruction by its index
     * @return
     * instructions
     */
    public String getInstructionByIndex(int index) {
        // Converting to the List of Instructions
        return instructionsStringToList(this.instructions).get(index);
    }
    /**
     * To get total instructions in the list
     * @return
     * instructions list size
     */
    public int getNumOfInstructions() {
        // Converting to the List of Instructions
        return instructionsStringToList(this.instructions).size();
    }
    /**
     * Removes instruction from the instructions array
     * @param indexInstruction
     * Index of the instruction to be removed
     */
    public void removeInstruction(int indexInstruction) {
        List<String> instructionStrings = new ArrayList<>();
        // Converting to the List of Instructions
        instructionStrings = instructionsStringToList(this.instructions);

        // Removing specified instruction inside of the Array
        if(indexInstruction >= 0 && indexInstruction < instructionStrings.size()) {
            instructionStrings.remove(indexInstruction);
        } else {
            Log.e(TAG, "Impossible to remove instruction, index is incorrect:" + Integer.toString(indexInstruction));
        }

        // Converting back to the String
        this.instructions = instructionsListToString(instructionStrings);
    }
    //Mostly for debugging, now, at least
    @Override
    public java.lang.String toString() {
        return "Recipe{\n" +
                "1. name='" + this.name + '\'' +
                "\n2. ingredients='" + this.ingredientsList + '\'' +
                "\n3. instructions='" + this.instructions + '\'' +
                "\n4. tags='" + this.tags + '\'' +
                "\n5. photo='" + this.photo + '\'' +
                "\n6. custom=" + this.custom +
                "\n7. numberOfServings=" + this.numberOfServings +
                "\n8. prepareTime=" + this.prepareTime +
                "\n9. authorName='" + this.authorName + '\'' +
                "\n10. grade=" + this.grade +
                "\n}";
    }

    //*****************//
    //*PRIVATE METHODS*//
    //*****************//
    /**
     * Splits String by chosen delimiter
     * @param str
     * String to split
     * @param delimiter
     * Which symbol should be used as a delimiter
     * @return
     * List of Strings, separated by the delimiter
     */
    private List<String> splitString(String str, String delimiter) {
        String temp[];
        temp = str.split(delimiter);
        List<String> finalArray = new ArrayList<>();
        finalArray = Arrays.asList(temp);
        return finalArray;
    }

    /**
     * Converting complex data String to the simple ArrayList of ingredients
     * @param ingredientString
     * String with complex dara about the ingredients.
     * (Ingredient delimited by ",". Ingredients between each other delimited by ";")
     * @return
     * Array of ingredients
     */
    private ArrayList<Ingredient> ingredientStringToArrayList(String ingredientString) {
        List<String> ingredientsStrings = new ArrayList<>();
        ingredientsStrings = splitString(ingredientString, ";"); // This now holds "Name,Amount,Measure" in one place.

        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        List<String> tempIngredient = new ArrayList<>();
        int i;
        for ( i = 0; i < ingredientsStrings.size(); i++ ) {
            tempIngredient = splitString(ingredientsStrings.get(i), ","); // This now holds "Name", "Amount", "Measure" at 0,1,2

            ingredientArrayList.add(new Ingredient(tempIngredient.get(0), Double.valueOf(tempIngredient.get(1)),tempIngredient.get(2)));
        }
        return ingredientArrayList;
    }
    /**
     * Converting ArrayList into complex data String
     * @param ingredientArrayList
     * Array of ingredients
     * @return
     * String with complex dara about the ingredients.
     * (Ingredient delimited by ",". Ingredients between each other delimited by ";")
     */
    private String ingredientArrayListToString(ArrayList<Ingredient> ingredientArrayList) {
        String newList = "";
        for(Ingredient ing: ingredientArrayList) {
            newList += ing.getIngredientName();
            newList += ",";
            newList += ing.getIngredientAmount();
            newList += ",";
            newList += ing.getIngredientMeasure();
            newList += ";";
        }
        return newList;
    }
    /**
     * Coverts complex instructions String into a list of instructions
     * @param instructionsString
     * String with complex data. "`" - delimiter
     * @return
     * List with instructions separated by the delimiter
     */
    private List<String> instructionsStringToList(String instructionsString) {
        List<String> instructionStrings = new ArrayList<>();
        instructionStrings = splitString(instructionsString, "`");
        return instructionStrings;
    }
    /**
     * Converts list of instructions into the String with complex data
     * @param instructionsList
     * List of instructions
     * @return
     * String with complex data. "`" - delimiter
     */
    private String instructionsListToString(List<String> instructionsList) {
        String newInstructions = "";
        for (String instr: instructionsList) {
            newInstructions += instr;
            newInstructions += "`";
        }
        return newInstructions;
    }
    /**
     * Converts complex tags String into the List fo tags
     * @param tagsString
     * String with complex data. "," - delimiter
     * @return
     * List of tags
     */
    private List<String> tagsStringToList(String tagsString) {
        List<String> tagsList = new ArrayList<>();
        tagsList = splitString(tagsString, ",");
        return tagsList;
    }
    /**
     * Converts list of tags to the String with complex data
     * @param tagsList
     * List of tags
     * @return
     * String with tags delimited by ","
     */
    private String tagsListToString(List<String> tagsList) {
        String newTagsString = "";
        for (String tag: tagsList) {
            newTagsString += tag;
            newTagsString += ",";
        }
        return newTagsString;
    }
}
