package com.leadit.todomvp.main;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leadit.todomvp.R;
import com.leadit.todomvp.base.maintainer.StateMaintainer;
import com.leadit.todomvp.entities.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.RequiredViewOps, View.OnClickListener {

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


    //main components
    private ListView mList;
    private Button mAddNoteBtn;
    private EditText mNewNoteEdit;
    private NotesAdapter mAdapter;

    private List<Note> mNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startsMVP();
        initView();
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

    @Override
    public void updateList() {
        mNotes.clear();
        mNotes.addAll(mPresenter.getNotes());
        mAdapter.notifyDataSetChanged();
    }


    /**
     * init all view components and listener
     */
    private void initView() {

        mNewNoteEdit = (EditText) findViewById(R.id.edit_new_note);
        mAddNoteBtn = (Button) findViewById(R.id.btn_add_note);
        mList = (ListView) findViewById(R.id.list_notes);

        mAddNoteBtn.setOnClickListener(this);
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteNote(mAdapter.getItem(i));
                return true;
            }
        });
        mNotes = mPresenter.getNotes();
        mAdapter = new NotesAdapter(this, mNotes);
        mList.setAdapter(mAdapter);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_note:
                addNote();
                break;
        }
    }

    /**
     * adds a new note
     */
    private void addNote() {
        if (!TextUtils.isEmpty(mNewNoteEdit.getText())) {
            mPresenter.newNote(mNewNoteEdit.getText().toString());
        }
    }

    /**
     * delete given note
     *
     * @param note
     */
    private void deleteNote(final Note note) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_delete_title)
                .setMessage(R.string.alert_delete_msg)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.deleteNote(note);
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }


    private class NotesAdapter extends ArrayAdapter<Note> {


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");

        /**
         * note list
         */
        List<Note> mNotes = new ArrayList<>();

        public NotesAdapter(Context context, List<Note> notes) {
            super(context, R.layout.item_note);
            this.mNotes = notes;
        }

        class ViewHolder {
            private TextView message;
            private TextView date;
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_note, null);
                viewHolder = new ViewHolder();
                viewHolder.message = (TextView) convertView.findViewById(R.id.note_message);
                viewHolder.date = (TextView) convertView.findViewById(R.id.note_date);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //binding data
            Note note = getItem(position);
            viewHolder.message.setText(note.getMessage());
            viewHolder.date.setText(sdf.format(note.getCreationDate()));

            return convertView;
        }

        @Nullable
        @Override
        public Note getItem(int position) {
            return mNotes.get(position);
        }

        @Override
        public int getCount() {
            return mNotes.size();
        }
    }
}
