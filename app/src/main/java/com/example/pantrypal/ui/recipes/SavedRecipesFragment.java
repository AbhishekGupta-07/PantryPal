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

    public SavedRecipesFragment() {
        super(R.layout.fragment_saved_recipes);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerSaved);
        LinearLayout emptyLayout = view.findViewById(R.id.emptyLayout); // 🔥 FIXED

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadData(recyclerView, emptyLayout);
    }

    private void loadData(RecyclerView recyclerView, LinearLayout emptyLayout) {

        new Thread(() -> {

            List<SavedRecipe> list =
                    PantryDatabase.getInstance(requireContext())
                            .savedRecipeDao()
                            .getAllSavedRecipes();

            requireActivity().runOnUiThread(() -> {

                if (list == null || list.isEmpty()) {

                    // 🔴 EMPTY STATE
                    recyclerView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);

                } else {

                    emptyLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    SavedRecipeAdapter adapter =
                            new SavedRecipeAdapter(requireContext(), list);

                    recyclerView.setAdapter(adapter);

                    // 🔥 ANIMATION (clean)
                    recyclerView.setLayoutAnimation(
                            AnimationUtils.loadLayoutAnimation(
                                    requireContext(),
                                    R.anim.layout_fall_down
                            )
                    );
                    recyclerView.scheduleLayoutAnimation();
                }

            });

        }).start();
    }
}