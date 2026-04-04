package com.example.pantrypal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class SimpleRecipeAdapter extends RecyclerView.Adapter<SimpleRecipeAdapter.ViewHolder> {

    private Context context;
    private List<String> list;
    private String type;

    public SimpleRecipeAdapter(Context context, List<String> list, String type) {
        this.context = context;
        this.list = list;
        this.type = type;
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

        String name = list.get(position);
        if (name == null) name = "Recipe";

        holder.tvName.setText(name);

        SharedPreferences prefs = context.getSharedPreferences("recipes", Context.MODE_PRIVATE);
        Set<String> favSet = new HashSet<>(prefs.getStringSet("fav", new HashSet<>()));

        // ❤️ Fav UI
        holder.btnFav.setText(favSet.contains(name) ? "❤️" : "🤍");

        String finalName = name;

        // 🔥 OPEN DETAIL
        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, RecipeDetailActivity.class);
            i.putExtra("name", finalName);
            context.startActivity(i);
        });

        // ❤️ TOGGLE FAVORITE
        holder.btnFav.setOnClickListener(v -> {

            Set<String> updated = new HashSet<>(prefs.getStringSet("fav", new HashSet<>()));

            if (updated.contains(finalName)) {
                updated.remove(finalName);
            } else {
                updated.add(finalName);
            }

            prefs.edit().putStringSet("fav", updated).apply();
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, btnFav;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            btnFav = itemView.findViewById(R.id.btnFav);
        }
    }
}