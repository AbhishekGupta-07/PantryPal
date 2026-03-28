package com.example.pantrypal.ui.recipes;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.PantryDatabase;
import com.example.pantrypal.R;
import com.example.pantrypal.SavedRecipe;
import com.example.pantrypal.SavedRecipeAdapter;

import java.util.List;

public class SavedRecipesFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayout emptyLayout;

    public SavedRecipesFragment() {
        super(R.layout.fragment_saved_recipes);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerSaved);
        emptyLayout = view.findViewById(R.id.emptyLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadData();
    }

    private void loadData() {

        new Thread(() -> {

            // 🔥 SAFE context
            if (getContext() == null) return;

            List<SavedRecipe> list =
                    PantryDatabase.getInstance(getContext())
                            .savedRecipeDao()
                            .getAllSavedRecipes();

            // 🔥 CHECK fragment attached
            if (!isAdded()) return;

            requireActivity().runOnUiThread(() -> {

                if (!isAdded()) return;

                if (list == null || list.isEmpty()) {

                    recyclerView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);

                } else {

                    emptyLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    SavedRecipeAdapter adapter =
                            new SavedRecipeAdapter(getContext(), list);

                    recyclerView.setAdapter(adapter);

                    recyclerView.setLayoutAnimation(
                            AnimationUtils.loadLayoutAnimation(
                                    getContext(),
                                    R.anim.layout_fall_down
                            )
                    );
                    recyclerView.scheduleLayoutAnimation();
                }

            });

        }).start();
    }
}