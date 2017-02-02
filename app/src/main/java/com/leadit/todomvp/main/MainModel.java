package com.leadit.todomvp.main;

import com.leadit.todomvp.database.DatabaseHelper;
import com.leadit.todomvp.database.tables.NoteTable;
import com.leadit.todomvp.entities.Note;

import java.sql.SQLException;

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
        } catch (SQLException e) {
            e.printStackTrace();
            mPresenter.onError(String.format("unable to delete note cause %s", e.getMessage()));
        }
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
