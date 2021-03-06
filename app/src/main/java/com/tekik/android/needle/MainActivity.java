package com.tekik.android.needle;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;

import com.tekik.android.needle.sync.NeedleSyncAdapter;


public class MainActivity extends Activity
        implements SortbyDialog.NoticeDialogListener{
    private static final String KEY_REGISTER = "register";
    public static final String ALL_FRAGMENT_TAG = "AllFragment";
    private ActionBar actionBar;


    private  final String TAG = getClass().getSimpleName();
    private boolean mRegister = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, new AllFragment(), ALL_FRAGMENT_TAG)
                .commit();


        actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);

        if(savedInstanceState == null) {
            new GcmRegistrationAsyncTask(this).execute();
        }


        NeedleSyncAdapter.initializeSyncAdapter(
                getApplicationContext()
        );


    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_REGISTER, mRegister);
    }

    // This activity implements a dialog listener and according to the result of this dialog
    // one of the below functions will be called. yet, these functions also call afterDialog()
    // function of AllFragment to resync the sync adapter and to restart the loader.
    @Override
    public void onDialogSortByAscending(DialogFragment dialog) {
        AllFragment fragment = (AllFragment) getFragmentManager()
                    .findFragmentByTag(ALL_FRAGMENT_TAG);
        fragment.afterDialog(true);
    }

    @Override
    public void onDialogSortByDescending(DialogFragment dialog) {
        AllFragment fragment = (AllFragment) getFragmentManager()
                .findFragmentByTag(ALL_FRAGMENT_TAG);
        fragment.afterDialog(false);
    }
}
