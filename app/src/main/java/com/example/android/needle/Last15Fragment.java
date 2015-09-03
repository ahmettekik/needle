package com.example.android.needle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jonfisk on 02/09/15.
 */
public class Last15Fragment extends Fragment {

    public Last15Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_last15, container, false);
        return rootView;
    }

}
