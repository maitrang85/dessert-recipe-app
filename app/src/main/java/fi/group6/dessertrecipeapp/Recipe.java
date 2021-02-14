package fi.group6.dessertrecipeapp;

import java.util.ArrayList;
/**
 * Models a dessert recipe
 * @author Trang
 * @version 1.0
 */
public class Recipe {
    private String name;
    private ArrayList<Ingredient> ingredientsList;
    private ArrayList<String> instructions;
    private ArrayList<String> tagsList;
    private String photo;
    private boolean local;
    private int numberOfServings;
    private int prepareTime;
    private String authorName;
    private float grade;

    /**
     * Constructor
     */
    public Recipe() {
        ingredientsList = new ArrayList<Ingredient>();
        instructions = new ArrayList<String>();
        tagsList = new ArrayList<String>();
    }
    /**
     * Constructor
     * Creates a recipe
     * @param name
     * Name of recipe
     * @param local
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
    public Recipe(String name, ArrayList<Ingredient> ingredientsList, ArrayList<String> instructions, ArrayList<String> tagsList, String photo, boolean local, int numberOfServings, int prepareTime, String authorName, float grade) {
        this.name = name;
        this.ingredientsList = ingredientsList;
        this.instructions = instructions;
        this.tagsList = tagsList;
        this.photo = photo;
        this.local = local;
        this.numberOfServings = numberOfServings;
        this.prepareTime = prepareTime;
        this.authorName = authorName;
        this.grade = grade;
    }

    //Methods
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
    public ArrayList<Ingredient> getList() {
        return ingredientsList;
    }

    /**
     * To set the list of ingredients
     * @param ingredientsList
     * new list of ingredients
     */
    public void setList(ArrayList<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
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
     * @param local
     */
    public void setLocal(boolean local) {
        this.local = local;
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
     * local
     */
    public boolean isLocal() {
        return local;
    }

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
        this.ingredientsList.add(ingredient);
    }

    /**
     * To delete an ingredient from the ingredient list
     * @param ingredient
     */
    public void deleteIngredient(Ingredient ingredient) {
        this.ingredientsList.remove(ingredient);
    }

    /**
     * To modify an ingredient
     * @param ingredient
     */
    public void changeIngredient(Ingredient ingredient) {
        int indexIngredient = ingredientsList.indexOf(ingredient);
        if(indexIngredient >= 0)
            ingredientsList.set(indexIngredient, ingredient);
    }

    /**
     * To add instructions into the instruction list
     * @param instruction
     */
    public void addInstructions(String instruction) {
        this.instructions.add(instruction);
    }

    /**
     * To modify an instruction
     * @param instruction
     */
    public void modifyInstructions(String instruction) {
        int indexInstruction = instructions.indexOf(instruction);
        if(indexInstruction >= 0)
            instructions.set(indexInstruction, instruction);
    }

    /**
     * To get an ingredient by using its index from the list
     * @param index
     */
    public Ingredient getIngredient (int index) {
        return ingredientsList.get(index);
    }

    /**
     * To get how many ingredients in the list
     * @return
     * ingredients list size
     */
    public int getNumOfIngredients() {
        return ingredientsList.size();
    }

    /**
     * To get all instruction strings
     * @return
     * instructions
     */
    public String getAllInstructions() {
        return instructions.toString();
    }

    /**
     * To get instruction by its index
     * @return
     * instructions
     */
    public String getInstructionByIndex(int index) {
        return instructions.get(index).toString();
    }

    /**
     * To get total instructions in the list
     * @return
     * instructions list size
     */
    public int getNumOfInstructions() {
        return instructions.size();
    }

    @Override
    public java.lang.String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", ingredients=" + ingredientsList +
                ", instructions=" + instructions +
                ", tags=" + tagsList +
                ", photo='" + photo + '\'' +
                ", local=" + local +
                ", numberOfServings=" + numberOfServings +
                ", prepareTime=" + prepareTime +
                ", authorName='" + authorName + '\'' +
                ", grade=" + grade +
                '}';
    }

}

