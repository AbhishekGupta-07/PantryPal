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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    private final Context context;
    private final PantryDao pantryDao;
    private final List<PantryItem> itemList = new ArrayList<>();

    public PantryAdapter(Context context, List<PantryItem> items) {
        this.context = context;
        this.pantryDao = PantryDatabase.getInstance(context).pantryDao();
        if (items != null) itemList.addAll(items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_pantry_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {

        PantryItem item = itemList.get(pos);

        // NAME
        h.tvName.setText(item.getName());

        // PRICE
        if (item.getPrice() > 0) {
            h.tvPrice.setText("₹" + item.getPrice());
        } else {
            h.tvPrice.setText("");
        }

        // QUANTITY (String)
        h.tvQty.setText("Qty: " + item.getQuantity());

        // EXPIRY
        String exp = item.getExpiryDate();
        h.tvExpiry.setText("Expiry: " + (exp == null ? "-" : exp));

        // STATUS
        String status = ExpiryUtils.getExpiryStatus(exp);
        h.tvStatus.setText(status);

        if ("Expired".equalsIgnoreCase(status)) {
            h.tvStatus.setTextColor(Color.RED);
        } else if ("Expiring Soon".equalsIgnoreCase(status)) {
            h.tvStatus.setTextColor(Color.parseColor("#FFA500"));
        } else {
            h.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
        }

        // MENU
        h.ivMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, h.ivMore);
            popup.getMenuInflater().inflate(R.menu.item_actions_menu, popup.getMenu());
            forceShowMenuIcons(popup);

            popup.setOnMenuItemClickListener(menuItem -> {

                int currentPos = h.getAdapterPosition();
                if (currentPos == RecyclerView.NO_POSITION) return false;

                PantryItem currentItem = itemList.get(currentPos);

                if (menuItem.getItemId() == R.id.action_edit) {
                    showEditDialog(currentItem, currentPos);
                    return true;
                } else if (menuItem.getItemId() == R.id.action_delete) {
                    showDeleteDialog(currentItem, currentPos);
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

    // 🔥 IMPORTANT (FILTER FIX)
    public void updateList(List<PantryItem> newList) {
        itemList.clear();
        if (newList != null) itemList.addAll(newList);
        notifyDataSetChanged();
    }

    // DELETE
    private void showDeleteDialog(PantryItem item, int pos) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Delete this item?")
                .setPositiveButton("Delete", (d, w) -> {
                    new Thread(() -> {
                        pantryDao.deleteItem(item);
                        runOnUi(() -> {
                            if (pos >= 0 && pos < itemList.size()) {
                                itemList.remove(pos);
                                notifyItemRemoved(pos);
                            } else {
                                notifyDataSetChanged();
                            }
                        });
                    }).start();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // EDIT WITH PRICE
    private void showEditDialog(PantryItem item, int pos) {

        View dialog = LayoutInflater.from(context)
                .inflate(R.layout.dialog_edit_item, null);

        EditText etName = dialog.findViewById(R.id.etEditName);
        EditText etQty = dialog.findViewById(R.id.etEditQty);
        EditText etExp = dialog.findViewById(R.id.etEditExpiry);
        EditText etPrice = dialog.findViewById(R.id.etEditPrice);

        etName.setText(item.getName());

        // ✅ FIXED (String quantity)
        etQty.setText(item.getQuantity());

        etExp.setText(item.getExpiryDate());
        etPrice.setText(String.valueOf(item.getPrice()));

        new AlertDialog.Builder(context)
                .setTitle("Edit Item")
                .setView(dialog)
                .setPositiveButton("Update", (d, w) -> {

                    item.setName(etName.getText().toString().trim());

                    // ✅ FIXED (no parse, direct string)
                    String qty = etQty.getText().toString().trim();
                    item.setQuantity(qty);

                    double price = etPrice.getText().toString().isEmpty()
                            ? 0
                            : Double.parseDouble(etPrice.getText().toString());

                    item.setPrice(price);
                    item.setExpiryDate(etExp.getText().toString());

                    new Thread(() -> {
                        pantryDao.updateItem(item);
                        runOnUi(() -> notifyItemChanged(pos));
                    }).start();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void runOnUi(Runnable r) {
        try {
            ((android.app.Activity) context).runOnUiThread(r);
        } catch (Exception e) {
            r.run();
        }
    }

    private void forceShowMenuIcons(PopupMenu popup) {
        try {
            Method m = popup.getMenu().getClass()
                    .getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            m.invoke(popup.getMenu(), true);
        } catch (Exception ignored) {}
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvExpiry, tvStatus, tvPrice, tvQty;
        ImageView ivMore;

        ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvExpiry = v.findViewById(R.id.tvExpiry);
            tvStatus = v.findViewById(R.id.tvStatus);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvQty = v.findViewById(R.id.tvQty);
            ivMore = v.findViewById(R.id.ivMore);
        }
    }
}