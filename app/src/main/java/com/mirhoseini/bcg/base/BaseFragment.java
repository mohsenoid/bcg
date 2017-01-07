package com.mirhoseini.bcg.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.mirhoseini.bcg.BcgApplication;

/**
 * Created by Mohsen on 07/01/2017.
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        injectDependencies(BcgApplication.get(getContext()));

        // can be used for general purpose in all Fragments of Application
    }

    protected abstract void injectDependencies(BcgApplication application);

}