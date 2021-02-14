package fi.group6.dessertrecipeapp.logic;

/**
 * Ingredient represents one ingredient used in a recipe.
 */
public class Ingredient {
    private String name;
    private double amount;
    private String measure;
    //**************//
    // Constructors //
    //**************//
    /**
     * Creates new Ingredient
     * @param name
     * Name of the ingredient
     * @param amount
     * Amount of the ingredient given by decimal number
     * @param measure
     * Measure for the amount ( kg, l, etc... )
     */
    public Ingredient ( String name, int amount, String measure ) {
        this.name = name;
        this.amount = amount;
        this.measure = measure;
    }

    //*********//
    // Methods //
    //*********//

    /**
     * Changes amount of the ingredient
     * @param newNum
     * New amount
     */
    public void changeAmount(double newNum) {
        this.amount = newNum;
    }

    /**
     * Gives name of the ingredient
     * @return
     * Name of the Ingredient
     */
    public String getIngredientName() {
        return this.name;
    }

    /**
     * Gives amount of the ingredient
     * @return
     * Amount
     */
    public double getIngredientAmount() {
        return this.amount;
    }

    /**
     * Gives measure for the ingredient amount
     * @return
     * Measure
     */
    public String getIngredientMeasure() {
        return this.measure;
    }

    @Override
    public String toString() {
        return this.name + " " + Double.toString(this.amount) + " " + measure;
    }

}
