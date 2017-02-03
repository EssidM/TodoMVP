package com.leadit.todomvp.main;

import android.content.Context;

import com.leadit.todomvp.database.tables.NoteTable;
import com.leadit.todomvp.entities.Note;

import java.util.List;

/**
 * Main activity Contract
 * <p>
 * defines all Model view Presenter required operation for adding displaying and deleteing note
 *
 * @author Mohamed Essid on 02/02/2017.
 */

public interface MainContract {


    /**
     * view mandatory ops --> Presenter -> View
     */
    interface RequiredViewOps {
        void showToast(String msg);

        void showAlert(String msg);

        Context getContext();

        void updateList();
    }


    /**
     * operations offered from presenter to Vie
     */
    interface PresenterOps {
        void onConfigurationChanged(RequiredViewOps view);

        void onDestroy(boolean isChangingConfig);

        void newNote(String note);

        void deleteNote(Note note);

        List<Note> getNotes();
    }

    /**
     * operations offered from Presenter to Model
     */
    interface RequiredPresenterOps {

        void onNoteInserted(Note note);

        void onNoteRemoved(Note note);

        void onError(String err);

        Context getContext();
    }

    /**
     * Model operations offered to Presenter
     * <p>
     * Presenter -> Model
     */
    interface ModelOps {
        void insertNote(Note note);

        void removeNote(Note note);

        List<Note> getNotes();

        void onDestroy();

    }
}
