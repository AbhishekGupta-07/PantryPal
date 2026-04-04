package com.example.pantrypal.ui.recipes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.R;
import com.example.pantrypal.RecipeDetailActivity;
import com.example.pantrypal.data.model.Recipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> list;

    public RecipeAdapter(List<Recipe> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Recipe recipe = list.get(position);
        Context context = holder.itemView.getContext();

        SharedPreferences prefs = context.getSharedPreferences("recipes", Context.MODE_PRIVATE);

        // 🔥 SAFE VALUES
        String name = recipe.getName() != null ? recipe.getName() : "Recipe 🍽";
        String desc = recipe.getDescription() != null ? recipe.getDescription() : "No description";
        String emoji = recipe.getEmoji() != null ? recipe.getEmoji() : "🍴";

        holder.name.setText(emoji + " " + name);
        holder.desc.setText(desc);

        // ⭐ Highlight
        holder.highlight.setVisibility(
                recipe.isUsesExpiringItems() ? View.VISIBLE : View.GONE
        );

        // 🔥 LOAD DATA FROM STORAGE
        Set<String> favSet = new HashSet<>(prefs.getStringSet("fav", new HashSet<>()));
        Set<String> savedSet = new HashSet<>(prefs.getStringSet("saved", new HashSet<>()));

        // ❤️ FAVORITE UI
        holder.btnFav.setText(favSet.contains(name) ? "❤️ Favorited" : "🤍 Favorite");

        // 💾 SAVE UI
        holder.btnSave.setText(savedSet.contains(name) ? "✅ Saved" : "💾 Save");

        // ❤️ FAVORITE CLICK
        holder.btnFav.setOnClickListener(v -> {

            if (favSet.contains(name)) {
                favSet.remove(name);
                Toast.makeText(context, "Removed from Favorites ❌", Toast.LENGTH_SHORT).show();
            } else {
                favSet.add(name);
                Toast.makeText(context, "Added to Favorites ❤️", Toast.LENGTH_SHORT).show();
            }

            prefs.edit().putStringSet("fav", favSet).apply();
            notifyItemChanged(position);
        });

        // 💾 SAVE CLICK
        holder.btnSave.setOnClickListener(v -> {

            if (savedSet.contains(name)) {
                savedSet.remove(name);
                Toast.makeText(context, "Removed ❌", Toast.LENGTH_SHORT).show();
            } else {
                savedSet.add(name);
                Toast.makeText(context, "Saved Successfully 💾", Toast.LENGTH_SHORT).show();
            }

            prefs.edit().putStringSet("saved", savedSet).apply();
            notifyItemChanged(position);
        });

        // 🔥 OPEN DETAIL
        holder.itemView.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("desc", desc);
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Error opening recipe", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, desc, highlight, btnFav, btnSave;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvName);
            desc = itemView.findViewById(R.id.tvDesc);
            highlight = itemView.findViewById(R.id.tvHighlight);
            btnFav = itemView.findViewById(R.id.btnFav);
            btnSave = itemView.findViewById(R.id.btnSave);
        }
    }
}