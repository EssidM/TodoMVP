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

    @DatabaseField(generatedId = true)
    private String id;
    @DatabaseField(canBeNull = false)
    private String message;
    @DatabaseField(canBeNull = false)
    private long date;

    public NoteTable() {
    }

    public NoteTable(String message, long date) {
        this.message = message;
        this.date = date;
    }

    public NoteTable(String id, String message, long date) {
        this.id = id;
        this.message = message;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
