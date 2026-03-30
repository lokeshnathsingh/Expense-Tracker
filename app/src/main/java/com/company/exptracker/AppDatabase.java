package com.company.exptracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.company.exptracker.Models.Expense;

@Database(entities = {Expense.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExpenseDao expenseDao();
}
