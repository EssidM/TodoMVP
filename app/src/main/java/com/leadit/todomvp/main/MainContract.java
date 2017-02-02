package com.leadit.todomvp.main;

import android.content.Context;

import com.leadit.todomvp.entities.Note;

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
    }


    /**
     * operations offered from presenter to Vie
     */
    interface PresenterOps {
        void onConfigurationChanged(RequiredViewOps view);

        void onDestroy(boolean isChangingConfig);

        void newNote(String note);

        void deleteNote(Note note);
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

        void onDestroy();
    }
}
