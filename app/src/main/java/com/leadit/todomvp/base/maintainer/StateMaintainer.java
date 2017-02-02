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
    public boolean firstTimeIn() {
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
            Log.e(TAG, "Error in firstTimeIn");
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
