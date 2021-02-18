package com.example.fnbapp.Module;

public class TableModule {
    String tableName;
    int count;

    public TableModule() {
    }

    public TableModule(String tableName, int count) {
        this.tableName = tableName;
        this.count = count;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
