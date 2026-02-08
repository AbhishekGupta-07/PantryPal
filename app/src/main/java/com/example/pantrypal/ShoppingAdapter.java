package com.example.pantrypal;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
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

    public interface OnListChangedListener {
        void onListChanged();
    }

    private final Context context;
    private final List<ShoppingItem> list;
    private final ShoppingDao dao;
    private final OnListChangedListener listener;

    public ShoppingAdapter(Context context,
                           List<ShoppingItem> list,
                           ShoppingDao dao,
                           OnListChangedListener listener) {
        this.context = context;
        this.list = list;
        this.dao = dao;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;
        CheckBox cbDone;
        ImageView btnDelete;

        ViewHolder(@NonNull View v) {
            super(v);
            tvItem = v.findViewById(R.id.tvItem);
            cbDone = v.findViewById(R.id.cbDone);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        ShoppingItem item = list.get(position);

        h.tvItem.setText(item.getItemName());

        h.cbDone.setOnCheckedChangeListener(null);
        h.cbDone.setChecked(item.isPurchased());

        applyPurchasedStyle(h.tvItem, item.isPurchased());

        h.cbDone.setOnCheckedChangeListener((buttonView, checked) -> {
            int pos = h.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            ShoppingItem updated = list.get(pos);
            updated.setPurchased(checked);
            dao.update(updated);

            // 🔥 Refresh list to apply sorting
            if (listener != null) listener.onListChanged();
        });

        h.btnDelete.setOnClickListener(v -> {
            int pos = h.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            ShoppingItem toDelete = list.get(pos);

            new AlertDialog.Builder(context)
                    .setTitle(R.string.delete_item)
                    .setMessage(R.string.delete_item_confirm)
                    .setPositiveButton("Yes", (d, w) -> {
                        dao.delete(toDelete);

                        // 🔥 Refresh list after delete
                        if (listener != null) listener.onListChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void applyPurchasedStyle(TextView tv, boolean purchased) {
        if (purchased) {
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv.setAlpha(0.6f);
        } else {
            tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            tv.setAlpha(1f);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
