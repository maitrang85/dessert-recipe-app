package fi.group6.dessertrecipeapp.classes;

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
 * @version 1.1
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
    private String tagsList; //csv
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
    public Recipe(String name, String ingredientsList, String instructions, String tagsList,
                    String photo, boolean custom, int numberOfServings, int prepareTime, String authorName, float grade) {
        this.name = name;
        this.ingredientsList = ingredientsList;
        this.instructions = instructions;
        this.tagsList = tagsList;
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
        return name;
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
    public ArrayList<Ingredient> getIngredientList() { // We have complex data inside that string, so some conversions are needed
        List<String> tempIngredients = new ArrayList<>();                                 // in order to get ArrayList out of that.
        tempIngredients = splitStringBySemicolon(this.ingredientsList); // This now holds "Name,Amount,Measure" in one place.

        int i;
        List<String> ingredient = new ArrayList<>();
        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        for ( i = 0; i < tempIngredients.size(); i++ ) {
            ingredient = splitStringByComma(tempIngredients.get(i)); // This now holds "Name", "Amount", "Measure" at 0,1,2

            ingredientArrayList.add(new Ingredient(ingredient.get(0), Double.valueOf(ingredient.get(1)), ingredient.get(2)));
        }
        return ingredientArrayList;
    }


    public void setIngredientsList(ArrayList<Ingredient> ingredientsList) { // Converting ArrayList into complex data String
        String list = "";
        for(Ingredient ingredient: ingredientsList) {
            list += ingredient.getIngredientName();
            list += ",";
            list += ingredient.getIngredientAmount();
            list += ",";
            list += ingredient.getIngredientMeasure();
            list += ";";
        }
        this.ingredientsList = list;
    }

    /**
     * To get the photo for recipe
     * @return
     * photo
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * To change photo for recipe
     * @param photo
     * new photo
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Whether the recipe will be saved locally or not
     * @param custom
     */
    public void setLocal(boolean custom) {
        this.custom = custom;
    }

    /**
     * To get number of servings in recipe
     * @return
     * number of people to served
     */
    public int getNumberOfServings() {
        return numberOfServings;
    }

    /**
     * To change number of servings
     * @param numberOfServings
     * new number of servings
     */
    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    /**
     * To get the preparation time
     * @return
     * prepare time
     */
    public int getPrepareTime() {
        return prepareTime;
    }

    /**
     * To change the time for making food
     * @param prepareTime
     * new prepare time
     */
    public void setPrepareTime(int prepareTime) {
        this.prepareTime = prepareTime;
    }

    /**
     * To get name of person who creates the recipe
     * @return
     * author name
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * To set the name of author
     * @param authorName
     * new author name
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * To get rating grade
     * @return
     * grade
     */
    public float getGrade() {
        return grade;
    }

    /**
     * To set the rating grade
     * @param grade
     * grade
     */
    public void setGrade(float grade) {
        this.grade = grade;
    }

    /**
     * To get whether the recipe is saved locally or not
     * @return
     * custom
     */
    public boolean isCustom() {
        return custom;
    }

    //TODO: addTag(), checkTagInRecipe(), removeTag()
    /**
     * To check whether a tag is in the tags list
     * @param tags
     * @return
     *
     */
    public boolean checkTagInRecipe(String tags) {
        return this.tagsList!=null && this.tagsList.contains(tags);
    }

    /**
     * To add an ingredient into the ingredient list
     * @param ingredient
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
     * To delete an ingredient from the ingredient list
     * @param ingredient
     */
    public void deleteIngredient(Ingredient ingredient) {
        // Converting to the ArrayList of Ingredients
        List<String> ingredientsStrings = new ArrayList<>();
        ingredientsStrings = splitStringBySemicolon(this.ingredientsList);

        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        List<String> tempIngredient = new ArrayList<>();
        int i;
        for ( i = 0; i < ingredientsStrings.size(); i++ ) {
            tempIngredient = splitStringByComma(ingredientsStrings.get(i));

            ingredientArrayList.add(new Ingredient(tempIngredient.get(0), Double.valueOf(tempIngredient.get(1)),tempIngredient.get(2)));
        }

        ingredientArrayList.remove(ingredient); // deleting ingredient from the array

        // Converting back to the String
        String newList = "";
        for(Ingredient ing: ingredientArrayList) {
            newList += ing.getIngredientName();
            newList += ",";
            newList += ing.getIngredientAmount();
            newList += ",";
            newList += ing.getIngredientMeasure();
            newList += ";";
        }

        this.ingredientsList = newList;
    }

    /**
     * To modify an ingredient
     * @param ingredient
     */
    public void changeIngredient(Ingredient ingredient) {
        // Converting to the ArrayList of Ingredients
        List<String> ingredientsStrings = new ArrayList<>();
        ingredientsStrings = splitStringBySemicolon(this.ingredientsList);

        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        List<String> tempIngredient = new ArrayList<>();
        int i;
        for ( i = 0; i < ingredientsStrings.size(); i++ ) {
            tempIngredient = splitStringByComma(ingredientsStrings.get(i));

            ingredientArrayList.add(new Ingredient(tempIngredient.get(0), Double.valueOf(tempIngredient.get(1)),tempIngredient.get(2)));
        }

        // Modifying chosen ingredient
        int indexIngredient = ingredientArrayList.indexOf(ingredient);
        if(indexIngredient >= 0) {
            ingredientArrayList.set(indexIngredient, ingredient);
        }

        // Converting back to the String
        String newList = "";
        for(Ingredient ing: ingredientArrayList) {
            newList += ing.getIngredientName();
            newList += ",";
            newList += ing.getIngredientAmount();
            newList += ",";
            newList += ing.getIngredientMeasure();
            newList += ";";
        }

        this.ingredientsList = newList;
    }

    /**
     * To add instructions into the instruction list
     * @param instruction
     */
    public void addInstructions(String instruction) {
        this.instructions += instruction;
        this.instructions += "`";
    }

    /**
     * To modify an instruction
     * @param instruction
     */
    public void modifyInstructions(String instruction) {

        // Converting to the List of Instructions
        List<String> instructionStrings = new ArrayList<>();
        instructionStrings = splitString(this.instructions, "`");

        // Modifying instruction inside of the Array
        int indexInstruction = instructionStrings.indexOf(instruction);
        if(indexInstruction >= 0) {
            instructionStrings.set(indexInstruction, instruction);
        }

        // Converting back to the String
        String newinstructions = "";
        for(String instr: instructionStrings) {
            newinstructions += instr;
            newinstructions += "`";
        }
        this.instructions = newinstructions;
    }

    /**
     * To get an ingredient by using its index from the list
     * @param index
     * @return
     * Ingredient by index
     */
    public Ingredient getIngredient (int index) {
        // Converting to the ArrayList of Ingredients
        List<String> ingredientsStrings = new ArrayList<>();
        ingredientsStrings = splitStringBySemicolon(this.ingredientsList);

        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        List<String> tempIngredient = new ArrayList<>();
        int i;
        for ( i = 0; i < ingredientsStrings.size(); i++ ) {
            tempIngredient = splitStringByComma(ingredientsStrings.get(i));

            ingredientArrayList.add(new Ingredient(tempIngredient.get(0), Double.valueOf(tempIngredient.get(1)),tempIngredient.get(2)));
        }
        return ingredientArrayList.get(index);
    }

    /**
     * To get how many ingredients in the list
     * @return
     * ingredients list size
     */
    public int getNumOfIngredients() {
        // Converting to the List of Strings with ingredients for each index
        List<String> ingredientsStrings = new ArrayList<>();
        ingredientsStrings = splitStringBySemicolon(this.ingredientsList);

        return ingredientsStrings.size(); // it is enough
    }

    /**
     * To get all instruction strings
     * @return
     * instructions
     */
    public String getAllInstructions() {
        return instructions;
    }

    /**
     * To get instruction by its index
     * @return
     * instructions
     */
    public String getInstructionByIndex(int index) {
        // Converting to the List of Instructions
        List<String> instructionStrings = new ArrayList<>();
        instructionStrings = splitString(this.instructions, "`");

        return instructionStrings.get(index);
    }

    /**
     * To get total instructions in the list
     * @return
     * instructions list size
     */
    public int getNumOfInstructions() {
        // Converting to the List of Instructions
        List<String> instructionStrings = new ArrayList<>();
        instructionStrings = splitString(this.instructions, "`");

        return instructionStrings.size();
    }

    @Override
    public java.lang.String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", ingredients=" + ingredientsList +
                ", instructions=" + instructions +
                ", tags=" + tagsList +
                ", photo='" + photo + '\'' +
                ", custom=" + custom +
                ", numberOfServings=" + numberOfServings +
                ", prepareTime=" + prepareTime +
                ", authorName='" + authorName + '\'' +
                ", grade=" + grade +
                '}';
    }

    //*****************//
    //*PRIVATE METHODS*//
    //*****************//
    private List<String> splitStringByComma(String str) {
        String temp[];
        temp = str.split(",");
        List<String> finalArray = new ArrayList<>();
        finalArray = Arrays.asList(temp);
        return finalArray;
    }

    private List<String> splitStringBySemicolon(String str) {
        String temp[];
        temp = str.split(";");
        List<String> finalArray = new ArrayList<>();
        finalArray = Arrays.asList(temp);
        return finalArray;
    }

    private List<String> splitString(String str, String delimiter) {
        String temp[];
        temp = str.split(delimiter);
        List<String> finalArray = new ArrayList<>();
        finalArray = Arrays.asList(temp);
        return finalArray;
    }

    //TODO: Remove some ctrl+c and ctrl+v
    private ArrayList<Ingredient> ingredientStringToArrayList(String ingredientString) {
        return null;
    }
    private String ingredientArrayListToString(ArrayList<Ingredient> IngredientArrayList) {
        return null;
    }
    private List<String> instructionsStringToList(String instructionsString) {
        return null;
    }
    private String instructionsListToString(List<String> instructionsList) {
        return null;
    }

}

