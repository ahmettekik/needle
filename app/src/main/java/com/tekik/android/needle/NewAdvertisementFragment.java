package com.tekik.android.needle;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tekik.android.needle.backend.needle.Needle;
import com.tekik.android.needle.backend.needle.model.Advertisement;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */

    // this fragment is a form fragment. that is, it gets the information via text fields
    // to add another announcement for the neighborhood. This activity can be called for two
    // purposes. One is to add a new announcement or edit an old announcement.
public class NewAdvertisementFragment extends android.app.Fragment {

    public static final String NEWANNOUNCEMENTEXTRA = "newAnnouncement";
    private String mDescription;
    private String mName;
    private String mNumber;
    private String mEmail;
    private boolean mEmailChanged = false;
    private boolean mDescriptionChanged = false;
    private boolean mNameChanged = false;
    private boolean mNumberChanged = false;
    private boolean toBeEdited = false;
    private final String TAG = getClass().getSimpleName();

    private Needle mNeedleApi;

    public NewAdvertisementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_advertisement, container, false);
        final Intent intent = getActivity().getIntent();
        final String email = intent.getStringExtra(LoginActivity.EMAILEXTRA);
        final String description = intent.getStringExtra(DetailsActivityFragment.DESCRIPTION_EXTRA);
        final String number = intent.getStringExtra(DetailsActivityFragment.NUMBER_EXTRA);
        final String name = intent.getStringExtra(DetailsActivityFragment.NAME_EXTRA);
        final long oldId = intent.getLongExtra(DetailsActivityFragment.DATABASE_ID_EXTRA, -1);

        // if description is not null, that means this activity is started by details fragment.
        // otherwise, the initiator activity is main activity.
        if (description != null) toBeEdited = true;
        mNeedleApi = CloudEndPointBuilderHelper.getEndpoints();



        // if the initiator activity is details activity (toBeEdited is keeping that information.),
        // then extras such as number, name, and description will be set for editing.
        EditText descEditText = (EditText) view.findViewById(R.id.description_editText);
        if (toBeEdited) {
            mDescription = description;
            descEditText.setText(description);
        }

        descEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (description != null && !description.equals(s.toString())) {
                            mDescription = s.toString();
                            mDescriptionChanged = true;
                        } else {
                            mDescription = s.toString();
                        }

                    }
                }
        );
        EditText nameEditText = (EditText) view.findViewById(R.id.name_editText);
        if (toBeEdited) {
            mName = name;
            nameEditText.setText(name);
        }
        nameEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (name != null && !name.equals(s.toString())) {
                            mName = s.toString();
                            mNameChanged = true;
                        } else {
                            mName = s.toString();
                        }
                    }
                }
        );
        EditText numberEditText = (EditText) view.findViewById(R.id.number_editText);
        if (toBeEdited) {
            mNumber = number;
            numberEditText.setText(number);
        }
        numberEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (number != null && !number.equals(s.toString())) {
                            mNumber = s.toString();
                            mNumberChanged = true;
                        } else {
                            mNumber = s.toString();
                        }

                    }
                }
        );

        EditText emailEditText = (EditText) view.findViewById(R.id.email_editText);
        emailEditText.setText(email);
        mEmail = email;
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (email.equals(s.toString())) {
                    mEmail = s.toString();
                } else {
                    mEmail = s.toString();
                    mEmailChanged = true;
                }
            }
        });

        // no matter which button is clicked, the app will return to main activity. So, we need to
        // supply the email as an extra for that activity.
        final Button addButton = (Button) view.findViewById(R.id.add_button);
        if (toBeEdited) addButton.setText("Edit");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEmail == null) {
                    Toast.makeText(getActivity(),
                            "Do not leave email field empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mDescription == null) {
                    Toast.makeText(getActivity(),
                            "Do not leave description field empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mName == null) {
                    Toast.makeText(getActivity(),
                            "Do not leave name field empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                if (toBeEdited) {
                    if (mEmailChanged || mNumberChanged || mNameChanged || mDescriptionChanged) {
                        new DeleteAdTask().execute(oldId);
                    } else {
                        Toast.makeText(
                                getActivity(),
                                "You have not made any changes",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                }


                new AdvertisementTask().execute(intent);

                boolean newAnnouncement = true;
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra(LoginActivity.EMAILEXTRA, email);
                i.putExtra(NEWANNOUNCEMENTEXTRA, newAnnouncement);
                startActivity(i);
            }
        });

        final Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra(LoginActivity.EMAILEXTRA, email);
                startActivity(i);
            }
        });


        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings: {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // this asynctask will add the new announcement.
    private class AdvertisementTask extends AsyncTask<Intent, Void, Void> {

        @Override
        protected Void doInBackground(Intent... params) {
            Intent i = params[0];
            Advertisement advertisement = new Advertisement();
            String[] location = Utility.getLocation(getActivity());
            advertisement.setAdvertisementDate(new DateTime(new Date().getTime()));


            advertisement.setCountryCode(
                    location[0]
            );
            if (mEmailChanged) {
                advertisement.setUserEmail(
                        mEmail
                );
            } else {
                advertisement.setUserEmail(
                        i.getStringExtra(LoginActivity.EMAILEXTRA)
                );
            }
            advertisement.setZipCode(
                    location[1]
            );
            advertisement.setName(mName);
            advertisement.setDescription(mDescription);
            advertisement.setPhoneNumber(mNumber);

            try {
                mNeedleApi.ads().insertAdvertisement(advertisement).execute();
            } catch (IOException e) {
                Toast.makeText(getActivity(),
                        "An error occurred while performing the task",
                        Toast.LENGTH_SHORT
                ).show();
            }


            return null;
        }


    }

    private class DeleteAdTask extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... params) {
            long id = params[0];


            try {
                mNeedleApi.ads().removeAdvertisement(id).execute();
            } catch (IOException e) {
                Toast.makeText(getActivity(),
                        "An error occurred while performing the task",
                        Toast.LENGTH_SHORT
                ).show();
            }


            return null;
        }
    }
}
