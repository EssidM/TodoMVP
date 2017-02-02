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
