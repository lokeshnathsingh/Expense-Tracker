package com.company.exptracker;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.company.exptracker.Models.Expense;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void insert(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    List<Expense> getAllExpenses();

    @Query("SELECT * FROM expenses WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    List<Expense> getTodayExpenses(long start, long end);
    @Query("SELECT * FROM expenses WHERE date BETWEEN :start AND :end")
    List<Expense> getExpensesBetween(long start, long end);
    @Query("SELECT SUM(amount) FROM expenses WHERE date BETWEEN :start AND :end")
    double getMonthlyTotal(long start, long end);
}