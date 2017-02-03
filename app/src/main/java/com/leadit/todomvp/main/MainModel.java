package com.leadit.todomvp.main;

import com.leadit.todomvp.database.DatabaseHelper;
import com.leadit.todomvp.database.tables.NoteTable;
import com.leadit.todomvp.entities.Note;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model class which implements all model operations accessible by presenter
 * it contains also a presenter reference
 *
 * @author Mohamed Essid on 02/02/2017.
 */

public class MainModel implements MainContract.ModelOps {


    /**
     * Presenter reference
     */
    private MainContract.RequiredPresenterOps mPresenter;

    private DatabaseHelper mDatabaseHelper;

    /**
     * Model constructor
     *
     * @param presenter
     */
    public MainModel(MainContract.RequiredPresenterOps presenter) {
        this.mPresenter = presenter;
        this.mDatabaseHelper = new DatabaseHelper(presenter.getContext());
    }

    @Override
    public void insertNote(Note note) {
        //data base logic
        try {
            mDatabaseHelper.getNotesDao().create(new NoteTable(note.getMessage(), note.getCreationDate().getTime()));
            mPresenter.onNoteInserted(note);
        } catch (SQLException e) {
            e.printStackTrace();
            mPresenter.onError(String.format("unable to insert note cause %s", e.getMessage()));
        }
    }

    @Override
    public void removeNote(Note note) {
        //data base logic
        try {
            mDatabaseHelper.getNotesDao().deleteById(note.getId());
            mPresenter.onNoteRemoved(note);
        } catch (SQLException e) {
            e.printStackTrace();
            mPresenter.onError(String.format("unable to delete note cause %s", e.getMessage()));
        }
    }

    @Override
    public List<Note> getNotes() {
        List<Note> result = new ArrayList<>();
        try {
            List<NoteTable> notesDB = mDatabaseHelper.getNotesDao().queryBuilder().orderBy(NoteTable.COLUMN_DATE, false).query();
            if (notesDB != null) {
                for (NoteTable noteTable : notesDB) {
                    result.add(new Note(noteTable.getId(), noteTable.getMessage(), new Date(noteTable.getDate())));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void onDestroy() {
        //data base logic
        //closing pending connection database helper
        if (mDatabaseHelper != null) {
            mDatabaseHelper.close();
        }
    }
}
