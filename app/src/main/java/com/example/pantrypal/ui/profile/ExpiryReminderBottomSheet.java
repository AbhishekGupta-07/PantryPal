package com.example.pantrypal.ui.profile;

import android.os.Bundle;
import android.view.*;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pantrypal.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

public class ExpiryReminderBottomSheet extends BottomSheetDialogFragment {

    public interface OnReminderSelectedListener {
        void onReminderSelected(int days);
    }

    private static final String ARG_DAYS = "arg_days";

    public static ExpiryReminderBottomSheet newInstance(int currentDays) {
        ExpiryReminderBottomSheet sheet = new ExpiryReminderBottomSheet();
        Bundle b = new Bundle();
        b.putInt(ARG_DAYS, currentDays);
        sheet.setArguments(b);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottomsheet_expiry_reminder, container, false);

        int currentDays = 3;
        if (getArguments() != null) currentDays = getArguments().getInt(ARG_DAYS, 3);

        RadioGroup rg = v.findViewById(R.id.rgDays);
        MaterialButton btnSave = v.findViewById(R.id.btnSaveDays);

        // Preselect
        int idToCheck = R.id.rb3;
        if (currentDays == 1) idToCheck = R.id.rb1;
        else if (currentDays == 5) idToCheck = R.id.rb5;
        else if (currentDays == 7) idToCheck = R.id.rb7;
        rg.check(idToCheck);

        btnSave.setOnClickListener(view -> {
            int checkedId = rg.getCheckedRadioButtonId();
            RadioButton rb = v.findViewById(checkedId);

            int days = 3;
            if (rb.getId() == R.id.rb1) days = 1;
            else if (rb.getId() == R.id.rb3) days = 3;
            else if (rb.getId() == R.id.rb5) days = 5;
            else if (rb.getId() == R.id.rb7) days = 7;

            OnReminderSelectedListener listener = null;
            if (getTargetFragment() instanceof OnReminderSelectedListener) {
                listener = (OnReminderSelectedListener) getTargetFragment();
            } else if (getParentFragment() instanceof OnReminderSelectedListener) {
                listener = (OnReminderSelectedListener) getParentFragment();
            }

            if (listener != null) listener.onReminderSelected(days);
            dismiss();
        });

        return v;
    }
}
