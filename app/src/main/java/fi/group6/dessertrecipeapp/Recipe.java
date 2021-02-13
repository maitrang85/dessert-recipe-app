package fi.group6.dessertrecipeapp;
import java.util.ArrayList;

/**
 * Models a recipe for any desserts.
 * @ author Trang
 * version 1.0
 */
public class Recipe {

    private String name;
    private ArrayList<Ingredient> list;
    private ArrayList<String> list;
    private ArrayList<TagsEnum> list;
    private String photo;
    private boolean local;
    private int numberOfServings;
    private int prepareTime;
    private String authorName;
    private float grade;

    //Constructors

    /**
     * Creates a recipe
     * @param name
     * Name of recipe
     * @param photo
     * Photo of the food
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
    public Recipe(String name, ArrayList<Ingredient> list, ArrayList<String> list1, ArrayList<TagsEnum> list2, String photo, boolean local, int numberOfServings, int prepareTime, String authorName, float grade) {
        this.name = name;
        this.list = list;
        this.list = list1;
        this.list = list2;
        this.photo = photo;
        this.local = local;
        this.numberOfServings = numberOfServings;
        this.prepareTime = prepareTime;
        this.authorName = authorName;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getList() {
        return list;
    }

    public void setList(ArrayList<Ingredient> list) {
        this.list = list;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public int getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public int getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(int prepareTime) {
        this.prepareTime = prepareTime;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public boolean isLocal() {
        return local;
    }

    @Override
    public java.lang.String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", list=" + list +
                ", list=" + list +
                ", list=" + list +
                ", photo='" + photo + '\'' +
                ", local=" + local +
                ", numberOfServings=" + numberOfServings +
                ", prepareTime=" + prepareTime +
                ", authorName='" + authorName + '\'' +
                ", grade=" + grade +
                '}';
    }

}