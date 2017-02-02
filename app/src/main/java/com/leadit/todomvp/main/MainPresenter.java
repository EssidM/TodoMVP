package com.leadit.todomvp.main;

import android.content.Context;

import com.leadit.todomvp.entities.Note;

import java.lang.ref.WeakReference;

/**
 * Main Presenter
 *
 * Man In The Middle between Model @ View
 *
 * @author Mohamed Essid on 02/02/2017.
 */

public class MainPresenter implements MainContract.PresenterOps, MainContract.RequiredPresenterOps {
    /**
     * layer view reference
     * <p>
     * weak refernce used here because the view can be destroyed in a configuration change for example
     */
    private WeakReference<MainContract.RequiredViewOps> mView;
    /**
     * layer model reference
     */
    private MainContract.ModelOps mModel;

    /**
     * configuration change state
     */
    private boolean mIsChangingConfiguration;


    /**
     * Presenter constructor
     *
     * @param view the view reference
     */
    public MainPresenter(MainContract.RequiredViewOps view) {
        this.mView = new WeakReference<>(view);
        this.mModel = new MainModel(this);
    }

    /**
     * sent from activity after a configuration changes
     *
     * @param view
     */
    @Override
    public void onConfigurationChanged(MainContract.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    /**
     * receives {@link MainActivity#onDestroy()} event
     *
     * @param isChangingConfig config change state
     */
    @Override
    public void onDestroy(boolean isChangingConfig) {
        mIsChangingConfiguration = isChangingConfig;
        mView = null;

        if (!mIsChangingConfiguration) {
            mModel.onDestroy();
        }
    }

    @Override
    public void newNote(String note) {
        mModel.insertNote(new Note(note));
    }

    @Override
    public void deleteNote(Note note) {
        mModel.removeNote(note);
    }

    @Override
    public void onNoteInserted(Note note) {
        mView.get().showToast(String.format("Note %s was inserted", note.getId()));
    }

    @Override
    public void onNoteRemoved(Note note) {
        mView.get().showToast(String.format("NoteTable %s removed", note.getId()));
    }

    @Override
    public void onError(String err) {
        mView.get().showAlert(err);
    }

    @Override
    public Context getContext() {
        return mView.get().getContext();
    }
}
