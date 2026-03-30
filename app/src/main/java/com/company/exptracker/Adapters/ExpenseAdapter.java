package com.company.exptracker.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.company.exptracker.Models.Expense;
import com.company.exptracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    List<Expense> list;
    OnDeleteClick listener;


    public interface OnDeleteClick {
        void onDelete(Expense expense);
    }

    public ExpenseAdapter(List<Expense> list, OnDeleteClick listener) {
        this.list = list;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView category, date, amount;
        ImageView delete;

        @SuppressLint("WrongViewCast")
        public ViewHolder(View v) {
            super(v);
            category = v.findViewById(R.id.title);
            date = v.findViewById(R.id.date);
            amount = v.findViewById(R.id.amount);
            delete = v.findViewById(R.id.delete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Expense e = list.get(position);

        holder.category.setText(e.category);
        holder.amount.setText("₹" + e.amount);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String date = sdf.format(new Date(e.date));

        Calendar today = Calendar.getInstance();
        Calendar itemDate = Calendar.getInstance();
        itemDate.setTimeInMillis(e.date);

        boolean isToday =
                today.get(Calendar.YEAR) == itemDate.get(Calendar.YEAR) &&
                        today.get(Calendar.DAY_OF_YEAR) == itemDate.get(Calendar.DAY_OF_YEAR);

        if (isToday) {
            holder.date.setText("Today");
        } else {
            holder.date.setText(date);
        }

        holder.delete.setOnClickListener(v -> {
            listener.onDelete(e);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}