package com.leadit.todomvp.database.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Note Table schema
 *
 * @author Mohamed Essid on 02/02/2017.
 */

@DatabaseTable(tableName = "notes")
public class NoteTable {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_DATE = "date";


    @DatabaseField(columnName = COLUMN_ID, generatedId = true)
    private Long id;
    @DatabaseField(columnName = COLUMN_MESSAGE, canBeNull = false)
    private String message;
    @DatabaseField(columnName = COLUMN_DATE, canBeNull = false)
    private long date;

    public NoteTable() {
    }

    public NoteTable(String message, long date) {
        this.message = message;
        this.date = date;
    }

    public NoteTable(Long id, String message, long date) {
        this.id = id;
        this.message = message;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
