package com.leadit.todomvp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.leadit.todomvp.R;
import com.leadit.todomvp.database.tables.NoteTable;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * database helper which is responsible for creating and accessing database with DAOs
 *
 * @author Mohamed Essid on 02/02/2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    public static final String DATABASE_NAME = "notes.db";
    public static int DATABASE_VERSION = 1;


    /**
     * notes dao
     */
    private Dao<NoteTable, Long> mNotesDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_db_config_file);
        this.mNotesDao = mNotesDao;
    }

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, int configFileId) {
        super(context, databaseName, factory, databaseVersion, configFileId);
    }

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, File configFile) {
        super(context, databaseName, factory, databaseVersion, configFile);
    }

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion, InputStream stream) {
        super(context, databaseName, factory, databaseVersion, stream);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, NoteTable.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create database", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, NoteTable.class, true);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database cause: ", e);
        }
    }

    /**
     * returns notes DAO
     *
     * @return
     * @throws SQLException
     */
    public Dao<NoteTable, Long> getNotesDao() throws SQLException {
        if (mNotesDao == null) {
            mNotesDao = getDao(NoteTable.class);
        }

        return mNotesDao;
    }
}
