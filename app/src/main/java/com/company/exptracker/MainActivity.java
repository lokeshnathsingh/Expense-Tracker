package com.company.exptracker;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.company.exptracker.Adapters.ExpenseAdapter;
import com.company.exptracker.Models.Expense;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ExpenseAdapter adapter;
    List<Expense> expenseList;
    AppDatabase db;
    TextView monthlyTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        monthlyTotal = findViewById(R.id.monthlyTotal);

        expenseList = new ArrayList<>();

        adapter = new ExpenseAdapter(expenseList, expense -> {

            Expense deletedExpense = expense;

            db.expenseDao().delete(expense);

            int position = expenseList.indexOf(expense);
            expenseList.remove(position);
            adapter.notifyItemRemoved(position);

            updateMonthlyTotal();

            Snackbar.make(recyclerView, "Expense Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", v -> {

                        db.expenseDao().insert(deletedExpense);

                        expenseList.add(position, deletedExpense);
                        adapter.notifyItemInserted(position);

                        updateMonthlyTotal();

                    }).show();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "expense-db")
                .allowMainThreadQueries()
                .build();

        loadTodayData();
        setupChart();
        updateMonthlyTotal();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_icon) {

            AddExpenseBottomSheet sheet =
                    new AddExpenseBottomSheet((category, amount, date ) -> {

                        Expense expense = new Expense(category, amount, date );

                        db.expenseDao().insert(expense);
                        loadTodayData();
                        setupChart();
                        updateMonthlyTotal();
                    });

            sheet.show(getSupportFragmentManager(), "AddExpense");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadTodayData() {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long start = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long end = calendar.getTimeInMillis();

        List<Expense> list = db.expenseDao().getTodayExpenses(start, end);

        expenseList.clear();
        expenseList.addAll(list);

        adapter.notifyDataSetChanged();
    }

    private void setupChart() {

        BarChart chart = findViewById(R.id.barChart);

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, -6);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        long start = calendar.getTimeInMillis();
        long end = System.currentTimeMillis();

        List<Expense> expenses = db.expenseDao().getExpensesBetween(start, end);

        float[] dailyTotals = new float[7];

        for (Expense e : expenses) {
            Calendar expCal = Calendar.getInstance();
            expCal.setTimeInMillis(e.date);

            int diff = getDayDifference(expCal);

            if (diff >= 0 && diff < 7) {
                dailyTotals[6 - diff] += e.amount;
            }
        }

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i, dailyTotals[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(getResources().getColor(R.color.orange));
        dataSet.setDrawValues(false);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getLegend().setEnabled(false);

        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        chart.animateY(800);
        chart.invalidate();
    }

    private int getDayDifference(Calendar expenseDate) {
        Calendar today = Calendar.getInstance();

        long diffMillis = today.getTimeInMillis() - expenseDate.getTimeInMillis();

        return (int) (diffMillis / (1000 * 60 * 60 * 24));
    }

    private double getCurrentMonthTotal() {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long start = calendar.getTimeInMillis();

        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long end = calendar.getTimeInMillis();

        return db.expenseDao().getMonthlyTotal(start, end);
    }
    private void updateMonthlyTotal() {
        double total = getCurrentMonthTotal();
        monthlyTotal.setText("₹" + String.format("%.0f", total));
    }
}