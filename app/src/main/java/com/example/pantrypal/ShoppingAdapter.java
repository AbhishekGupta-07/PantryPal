package com.example.pantrypal;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {

    Context context;
    List<ShoppingItem> list;
    ShoppingDao dao;

    public ShoppingAdapter(Context context, List<ShoppingItem> list, ShoppingDao dao) {
        this.context = context;
        this.list = list;
        this.dao = dao;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;
        CheckBox cbDone;
        ImageView btnDelete;

        ViewHolder(View v) {
            super(v);
            tvItem = v.findViewById(R.id.tvItem);
            cbDone = v.findViewById(R.id.cbDone);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_shopping, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        ShoppingItem item = list.get(pos);

        h.tvItem.setText(item.itemName);
        h.cbDone.setChecked(item.isPurchased);

        h.cbDone.setOnCheckedChangeListener((b, checked) -> {
            item.isPurchased = checked;
            dao.update(item);
        });

        h.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.delete_item)
                    .setMessage("Delete this item?")
                    .setPositiveButton("Yes", (d, w) -> {
                        dao.delete(item);
                        list.remove(pos);
                        notifyItemRemoved(pos);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
