package com.company.exptracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseBottomSheet extends BottomSheetDialogFragment {

    EditText amountEdt;
    Button saveBtn;
    TextView dateText;
    long selectedDate = System.currentTimeMillis();

    OnExpenseAdded listener;
    Spinner categorySpinner;
    String selectedCategory = "Others";

    public interface OnExpenseAdded {
        void onAdded(String category, double amount, long date );
    }

    public AddExpenseBottomSheet(OnExpenseAdded listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_add_expense, container, false);

        amountEdt = view.findViewById(R.id.amountEdt);
        dateText = view.findViewById(R.id.dateText);
        saveBtn = view.findViewById(R.id.saveBtn);
        categorySpinner = view.findViewById(R.id.categorySpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.categories,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        dateText.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(
                    getContext(),
                    (view1, year, month, dayOfMonth) -> {

                        Calendar selected = Calendar.getInstance();
                        selected.set(year, month, dayOfMonth);

                        selectedDate = selected.getTimeInMillis();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                        dateText.setText(sdf.format(selected.getTime()));

                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });

        saveBtn.setOnClickListener(v -> {
            double amount = Double.parseDouble(amountEdt.getText().toString());

            selectedCategory = categorySpinner.getSelectedItem().toString();

            listener.onAdded(selectedCategory, amount, selectedDate);
            dismiss();
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);

            if (bottomSheet != null) {
                bottomSheet.setBackgroundResource(android.R.color.transparent);

                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
            }
        });

        return dialog;
    }
    @Override
    public int getTheme() {
        return R.style.BottomSheetAnimation;
    }
}