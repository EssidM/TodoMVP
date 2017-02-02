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
