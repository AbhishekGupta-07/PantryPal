package com.example.pantrypal;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrypal.utils.ExpiryUtils;

import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    private final Context context;
    private final PantryDao pantryDao;
    private final List<PantryItem> itemList = new ArrayList<>();

    public PantryAdapter(Context context, List<PantryItem> items) {
        this.context = context;
        this.pantryDao = PantryDatabase.getInstance(context).pantryDao();
        itemList.addAll(items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        PantryItem item = itemList.get(pos);

        h.tvName.setText(item.getName());
        h.tvExpiry.setText("Expiry: " + item.getExpiryDate());

        String status = ExpiryUtils.getExpiryStatus(item.getExpiryDate());
        h.tvStatus.setText(status);

        if (status.equals("Expired")) {
            h.tvStatus.setTextColor(Color.RED);
        } else if (status.equals("Expiring Soon")) {
            h.tvStatus.setTextColor(Color.parseColor("#FFA500"));
        } else {
            h.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
        }

        h.ivMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, h.ivMore);
            popup.getMenuInflater().inflate(R.menu.item_actions_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.action_edit) {
                    showEditDialog(item);
                    return true;
                } else if (menuItem.getItemId() == R.id.action_delete) {
                    showDeleteDialog(item);
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateList(List<PantryItem> newList) {
        itemList.clear();
        itemList.addAll(newList);
        notifyDataSetChanged();
    }

    private void showDeleteDialog(PantryItem item) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Delete this item?")
                .setPositiveButton("Delete", (d, w) -> {
                    pantryDao.deleteItem(item);
                    int safePos = itemList.indexOf(item);
                    if (safePos != -1) {
                        itemList.remove(safePos);
                        notifyItemRemoved(safePos);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditDialog(PantryItem item) {
        View dialog = LayoutInflater.from(context)
                .inflate(R.layout.dialog_edit_item, null);

        EditText etName = dialog.findViewById(R.id.etEditName);
        EditText etQty  = dialog.findViewById(R.id.etEditQty);
        EditText etExp  = dialog.findViewById(R.id.etEditExpiry);

        etName.setText(item.getName());
        etQty.setText(item.getQuantity());
        etExp.setText(item.getExpiryDate());

        new AlertDialog.Builder(context)
                .setTitle("Edit Item")
                .setView(dialog)
                .setPositiveButton("Update", (d, w) -> {
                    item.setName(etName.getText().toString());
                    item.setQuantity(etQty.getText().toString());
                    item.setExpiryDate(etExp.getText().toString());
                    pantryDao.updateItem(item);

                    int safePos = itemList.indexOf(item);
                    if (safePos != -1) {
                        notifyItemChanged(safePos);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvExpiry, tvStatus;
        ImageView ivMore;

        ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvExpiry = v.findViewById(R.id.tvExpiry);
            tvStatus = v.findViewById(R.id.tvStatus);
            ivMore = v.findViewById(R.id.ivMore);
        }
    }
}
