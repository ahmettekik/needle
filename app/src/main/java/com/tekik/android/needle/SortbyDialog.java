package com.tekik.android.needle;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * A simple {@link Fragment} subclass.
 */

    // This dialog fragment is for user to choose if the list will be sorted in asc or desc order.
    // it includes an interface implemented by MainActivity to inform the result of the dialog.
public class SortbyDialog extends DialogFragment {


    public SortbyDialog() {

    }

    public interface NoticeDialogListener {
        void onDialogSortByAscending(DialogFragment dialog);
        void onDialogSortByDescending(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;


    public static SortbyDialog newInstance() {
        return new SortbyDialog();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sort_by)
                .setItems(R.array.sort_by_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0: {
                                mListener.onDialogSortByAscending(SortbyDialog.this);
                                break;
                            }
                            case 1: {
                                mListener.onDialogSortByDescending(SortbyDialog.this);
                                break;
                            }
                        }
                    }
                });



        return builder.create();
    }


}
