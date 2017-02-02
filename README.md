# Android MVP - Notes Application Sample
Android app with MVP architecture based on www.tinmegali.com MVP tutorial

This application is sample of implementing MVP on Android without using any third library apart from the Android SDK, ORMlite for database (will not be covered cause it's out of scope).

This is not <b>my personal tutorial</b> , I just re-implemented it in a way to learn how to implement MVP and to share it with all developers that are struggling with MVP.<br/>Based on http://www.tinmegali.com/en/model-view-presenter-mvp-in-android-part-2/ tutorial (you should check this link to have an idea about architecture and global concepts, you can follow this steps to implement a Note App ( a basic sample to add, remove notes).

If you like to learn by code, you can clone or download the project here.

Ps: I'll be pleased to hear from you any suggestion in order to improve this project and tutorial.

# Step 1 : New Project
On Android Studio, create a new project following the wizard and choose and "Empty activity".

# Step 2 : Main Contract

This class will define all behaviors of Presenter , Model and View by defining
  
   * <b>RequiredViewOps</b> : all required View operations available to Presenter
   * <b>PresenterOps</b> : all operations that a Presenter should offer to View
   * <b>RequiredPresenterOps</b> : all required Presenter operations available to Model
   * <b>ModelOps</b> : all Model operations offered to Presenter

As defined below,

```java
package com.leadit.todomvp.main;

import com.leadit.todomvp.entities.Note;

import java.lang.ref.WeakReference;

/**
 * Main Presenter
 *
 * Man In The Middle between Model & View
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
        mView.get().showToast(String.format("Note %s removed", note.getId()));
    }

    @Override
    public void onError(String err) {
        mView.get().showAlert(err);
    }
}
```

# Step 3: PresenterImplementation
* Create a new class MainPresenter which implements <b>PresenterOps</b> availble for View and <b>RequiredPresenterOps</b> available for Model.

```java
package com.leadit.todomvp.main;

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
        this.mModel = new MainMdel(this);
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
        mView.get().showToast(String.format("Note %s removed", note.getId()));
    }

    @Override
    public void onError(String err) {
        mView.get().showAlert(err);
    }
}
```
# Step 4 : Model class implementation
This class implements all model available operations for Presenter

```java
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
``

# Step 5 SateMaintainer
As said in the www.tinmegali.com tutorial, we got to consider some specifics related to
Android lifecycle, as the activity (View) is responsible for creating the presenter who is
instantiate the Model. That's why we need to introduce a new element which is the
<b>StateMaintainer</b>. Please check the <b>Dealing with Android particularities</b>'s section in
http://www.tinmegali.com/en/model-view-presenter-mvp-in-android-part-2/ for further information.

Now we move on to the StateMaintainer implementation.

2 classes will be introduced :

* <b>StateMaintainer</b>

```java
package com.leadit.todomvp.base.maintainer;

import android.app.FragmentManager;
import android.util.Log;

import com.leadit.todomvp.base.maintainer.StateMaintainerFragment;

import java.lang.ref.WeakReference;

/**
 * Main state maintainer
 * <p>
 * responsible for saving object state during Android lifecyle changes
 *
 * @author Mohamed Essid on 02/02/2017.
 */

public class StateMaintainer {
    protected final String TAG = getClass().getSimpleName();

    private final String mStateMaintainerTag;
    private final WeakReference<FragmentManager> mFragmentManager;
    private StateMaintainerFragment mStateMangerFragment;

    /**
     * Constructor
     *
     * @param mStateMaintainerTag the TAG used to insert the state maintainer
     * @param fragmentManager     FragmentManager reference
     */
    public StateMaintainer(String mStateMaintainerTag, FragmentManager fragmentManager) {
        this.mStateMaintainerTag = mStateMaintainerTag;
        this.mFragmentManager = new WeakReference<>(fragmentManager);
    }

    /**
     * creates the state maintainer fragment
     *
     * @return true : the frag was created for the first time
     * false: recovering the object
     */
    public boolean init() {
        boolean success = false;
        try {
            //recovering the reference
            mStateMangerFragment = (StateMaintainerFragment) mFragmentManager.get().findFragmentByTag(mStateMaintainerTag);

            //creating a new RetainedFragment
            if (mStateMangerFragment == null) {
                Log.d(TAG, "creating a new retained fragment " + mStateMangerFragment);
                mStateMangerFragment = new StateMaintainerFragment();
                mFragmentManager.get().beginTransaction().add(mStateMangerFragment, mStateMaintainerTag).commit();
                success = true;
            } else {
                Log.d(TAG, "Returns a existent retained fragment existent " + mStateMaintainerTag);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "Error in init");
        }

        return success;
    }

    /**
     * Insert object to be preserved during configuration change
     *
     * @param key Object's tag
     * @param obj object to maintain
     */
    public void put(String key, Object obj) {
        mStateMangerFragment.put(key, obj);
    }

    /**
     * Insert object to be maintained with and uses it's class name as TAG
     *
     * @param obj object to be maintained
     */
    public void put(Object obj) {
        mStateMangerFragment.put(obj.getClass().getName(), obj);
    }


    /**
     * recovers saved object
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T get(String key) {
        return mStateMangerFragment.get(key);
    }

    /**
     * checks if the fragment has the object saved
     *
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return mStateMangerFragment.hasKey(key);
    }
}
```

* <b>StateMaintainerFragment</b>: A Retained instance fragment with a Map of data to store objects

```java
package com.leadit.todomvp.base.maintainer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * StateMaintainerFragment
 * <p>
 * a fragment used for maintaing Presenter & View state during View lifecycle changes
 *
 * @author Mohamed Essid on 02/02/2017.
 */

public class StateMaintainerFragment extends Fragment {

    /**
     * map to save retained objects
     */
    private final Map<String, Object> mData;

    public StateMaintainerFragment() {
        mData = new HashMap<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fragment will be always preserved
        setRetainInstance(true);
    }


    /**
     * inserts objects in mData using it's class name as tag
     *
     * @param obj object to be inserted
     */
    public void put(@NonNull Object obj) {
        mData.put(obj.getClass().getName(), obj);
    }

    /**
     * inserts an object into mData using the given tag
     *
     * @param key object tag as key
     * @param obj the object to be inserted
     */
    public void put(@NonNull String key, @NonNull Object obj) {
        mData.put(key, obj);

    }

    public boolean hasKey(String key) {
        return mData.get(key) != null;
    }

    /**
     * recover object
     *
     * @param key reference TAG
     * @param <T> Class
     * @return object saved
     */
    public <T> T get(String key) {
        return (T) mData.get(key);
    }
}
```

# Step 6 MainActivity - View Implementation

The view will implement RequiredViewOps and instantiate Presenter and state maintainer

```java
package com.leadit.todomvp.main;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.leadit.todomvp.R;
import com.leadit.todomvp.base.maintainer.StateMaintainer;

public class MainActivity extends AppCompatActivity implements MainContract.RequiredViewOps {


    /**
     * tag
     */
    private final String TAG = getClass().getSimpleName();

    /**
     * state maintainer responsible for retaining objects during configuration changes
     */
    private StateMaintainer mStateMaintainer = new StateMaintainer(TAG, getFragmentManager());

    /**
     * presenter operations
     */
    private MainContract.PresenterOps mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startsMVP();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert")
                .setMessage(msg)
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * initialize and restart the presenter
     */
    private void startsMVP() {

        try {
            if (mStateMaintainer.firstTimeIn()) {
                Log.d(TAG, "init MVP called for the first time");
                init(this);
            } else {
                Log.d(TAG, "init MVP called more than once");
                reinit(this);
            }
        } catch (IllegalArgumentException e) {
            Log.d(TAG, " startMVP error : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * init relevant MVP object
     *
     * @param view
     */
    private void init(MainContract.RequiredViewOps view) {
        mPresenter = new MainPresenter(view);
        mStateMaintainer.put(MainContract.PresenterOps.class.getSimpleName(), mPresenter);
    }

    /**
     * recovers Presenter and informs Presenter that a config change has occurred
     *
     * @param view
     */
    private void reinit(MainContract.RequiredViewOps view) {
        mPresenter = mStateMaintainer.get(MainContract.PresenterOps.class.getSimpleName());

        if (mPresenter == null) {
            Log.d(TAG, "recreating Presenter");
            init(view);
        } else {
            mPresenter.onConfigurationChanged(view);
        }
    }
}
```



