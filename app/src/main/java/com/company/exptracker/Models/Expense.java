package com.company.exptracker.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "category")
    public String category;


    public double amount;
    public long date;

    public Expense(String category, double amount, long date ) {
        this.category = category;
        this.amount = amount;
        this.date = date;

    }
}