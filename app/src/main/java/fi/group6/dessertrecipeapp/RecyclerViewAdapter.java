package fi.group6.dessertrecipeapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import de.hdodenhof.circleimageview.CircleImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import fi.group6.dessertrecipeapp.classes.AppDatabase;
import fi.group6.dessertrecipeapp.classes.RecipeWithIngredients;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    // log t for debug
    private static final String TAG = "RecyclerViewAdapter";
    private static final String CLICKED_ITEM = "indexOfRecipe";

    private List<RecipeWithIngredients> recipeWithIngredients = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(List<RecipeWithIngredients> recipeWithIngredients, Context Context) {
        this.recipeWithIngredients = recipeWithIngredients;
        mContext = Context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
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

//        Glide.with(mContext).load("http://goo.gl/gEgYUd").into(holder.image);


        //get recipe name
        holder.recipeName.setText(recipeWithIngredients.get(position).recipe.name);

        holder.parentLayout.setOnClickListener((view) -> {
            Log.d(TAG, "onClick: clicked on: " + recipeWithIngredients.get(position).recipe.name);
            Intent intent = new Intent(mContext, ActivityRecipe.class);
            intent.putExtra(CLICKED_ITEM, recipeWithIngredients.get(position).recipe.recipeId);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeWithIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView recipeName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            recipeName = itemView.findViewById(R.id.recipe_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}

