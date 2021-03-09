package fi.group6.dessertrecipeapp.classes;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Database class to hold the database and be the main point for accessing app's persisted data
 * @author Trang
 * @version 1.2
 */
@Database(entities = {Recipe.class, Ingredient.class}, version = 1)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    /**
     * This function to get access to query methods in RecipeDao class
     * @return RecipeDao
     */
    public abstract RecipeDao recipeDao();

    private static AppDatabase INSTANCE;

    /**
     * This function creates a Singleton instance to access to Room database.
     * If there is no instance yet, an instance will be built.
     * @param context
     * @return instance
     */
    public static AppDatabase getDbInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "DB_RECIPE")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
