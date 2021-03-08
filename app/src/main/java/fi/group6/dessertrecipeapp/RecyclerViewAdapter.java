package fi.group6.dessertrecipeapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import fi.group6.dessertrecipeapp.classes.RecipeWithIngredients;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private static final String CLICKED_ITEM = "indexOfRecipe";

    private List<RecipeWithIngredients> recipeWithIngredients = new ArrayList<>();
    private Context mContext;

    /**
     * Constructor RecyclerViewAdapter with parameters
     * @param recipeWithIngredients
     * @param Context
     */
    public RecyclerViewAdapter(List<RecipeWithIngredients> recipeWithIngredients, Context Context) {
        this.recipeWithIngredients = recipeWithIngredients;
        mContext = Context;
    }

    /**
     * This method is called whenever a new ViewHolder is created. It initializes the View holder and connects it
     * with View
     * @param parent
     * @param viewType
     * @return view holder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * This method is called to connect a ViewHolder with data. It gets appropriate data and fills data
     * to view holder's layout
     * Get image as bitmap from url source and then put it into image view widget
     * Get recipe name
     * setOnClicklistener : when user clicks on view holder, it starts a new activity, Recipe
     * activity and shows a new screen with whole description of recipe
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewholder called: " + recipeWithIngredients.get(position).recipe.photo);
        // get the Image
        Glide.with(mContext)
                .asBitmap()
                .centerCrop()
                .load(recipeWithIngredients.get(position).recipe.photo)
                .placeholder(R.drawable.ic_baseline_my_recipes_24)
                .into(holder.image);
        //get recipe name
        holder.recipeName.setText(recipeWithIngredients.get(position).recipe.name);
        //When user click on View holder, it will start a new activity: Activity Recipe which shows
        // a new screen with a full desciption of the recipe
        holder.parentLayout.setOnClickListener((view) -> {
            Intent intent = new Intent(mContext, ActivityRecipe.class);
            intent.putExtra(CLICKED_ITEM, recipeWithIngredients.get(position).recipe.recipeId);
            mContext.startActivity(intent);
        });
    }

    /**
     * This function tell how many items are in the list
     * @return number of items
     */
    @Override
    public int getItemCount() {
        return recipeWithIngredients.size();
    }

    /**
     * Model ViewHolder which holds image and recipe name
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView recipeName;
        RelativeLayout parentLayout;

        /**
         * Constructor ViewHolder with parameters
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            recipeName = itemView.findViewById(R.id.recipe_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}

