package com.example.pantrypal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SavedRecipeAdapter
        extends RecyclerView.Adapter<SavedRecipeAdapter.ViewHolder> {

    private final Context context;
    private final List<SavedRecipe> list;
    private final SavedRecipeDao dao;

    public SavedRecipeAdapter(Context context, List<SavedRecipe> list) {
        this.context = context;
        this.list = list;
        this.dao = PantryDatabase.getInstance(context).savedRecipeDao();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent;
        ImageView btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_recipe, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SavedRecipe recipe = list.get(position);

        holder.tvTitle.setText(recipe.title);
        holder.tvContent.setText(recipe.content);

        // 🔥 OPEN DETAIL SCREEN
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("title", recipe.title);
            intent.putExtra("content", recipe.content);
            context.startActivity(intent);
        });

        // 🗑 DELETE (THREAD SAFE)
        holder.btnDelete.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            new AlertDialog.Builder(context)
                    .setTitle("Delete Recipe")
                    .setMessage("Are you sure you want to delete this recipe?")
                    .setPositiveButton("Delete", (d, w) -> {

                        // 🔥 BACKGROUND DELETE (IMPORTANT)
                        new Thread(() -> {

                            dao.delete(list.get(pos));

                            ((android.app.Activity) context).runOnUiThread(() -> {
                                list.remove(pos);
                                notifyItemRemoved(pos);
                                notifyItemRangeChanged(pos, list.size());
                            });

                        }).start();

                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}