package com.example.android.needle;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.needle.backend.needle.Needle;
import com.example.android.needle.backend.needle.model.Advertisement;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewAdvertisementFragment extends android.app.Fragment {

    private String mDescription;
    private String mName;
    private String mNumber;
    private String mEmail;
    private Boolean mEmailChanged = false;

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
        final String zipCode = intent.getStringExtra(LoginActivity.ZIPCODEEXTRA);
        final String countryCode = intent.getStringExtra(LoginActivity.COUNTRYCODEEXTRA);
        mNeedleApi = CloudEndPointBuilderHelper.getEndpoints();



        ((EditText) view.findViewById(R.id.description_editText)).addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mDescription = s.toString();
                    }
                }
        );
        ((EditText) view.findViewById(R.id.name_editText)).addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mName = s.toString();
                    }
                }
        );
        ((EditText) view.findViewById(R.id.number_editText)).addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mNumber = s.toString();
                    }
                }
        );

        EditText emailEditText = (EditText) view.findViewById(R.id.email_editText);
        emailEditText.setText(email);
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
                    return;
                }
                else {
                    mEmail = s.toString();
                    mEmailChanged = true;
                }
            }
        });


        final Button addButton = (Button) view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mDescription == null) {
                    Toast.makeText(getActivity(),
                            "Do not leave description field empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mName == null) {
                    Toast.makeText(getActivity(),
                            "Do not leave name field empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                new AdvertisementTask().execute(intent);


                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra(LoginActivity.ZIPCODEEXTRA, zipCode);
                i.putExtra(LoginActivity.COUNTRYCODEEXTRA, countryCode);
                i.putExtra(LoginActivity.EMAILEXTRA, email);
                startActivity(i);
            }
        });

        final Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra(LoginActivity.ZIPCODEEXTRA, zipCode);
                i.putExtra(LoginActivity.COUNTRYCODEEXTRA, countryCode);
                i.putExtra(LoginActivity.EMAILEXTRA, email);
                startActivity(i);
            }
        });


        return view;
    }



    private class AdvertisementTask extends AsyncTask<Intent, Void, Void> {

        @Override
        protected Void doInBackground(Intent... params) {
            Intent i = params[0];
            Advertisement advertisement = new Advertisement();
            String[] location = new String[2];
            advertisement.setAdvertisementDate(new DateTime(new Date().getTime()));
            location[0] = i.getStringExtra(LoginActivity.COUNTRYCODEEXTRA);
            location[1] = i.getStringExtra(LoginActivity.ZIPCODEEXTRA);

            advertisement.setCountryCode(
                    location[0]
            );
            if(mEmailChanged) {
                advertisement.setUserEmail(
                        mEmail
                );
            }
            else {
                advertisement.setUserEmail(
                        i.getStringExtra(LoginActivity.EMAILEXTRA)
                );
            }
            advertisement.setZipCode(
                    location[1]
            );
            advertisement.setName(mName);
            Log.d(TAG, "description: " + mDescription);

            advertisement.setDescription(mDescription);
            Log.d(TAG, "phoneNumber: " + mNumber);
            advertisement.setPhoneNumber(mNumber);

            try {
                mNeedleApi.ads().insertAdvertisement(advertisement).execute();
            } catch(IOException e) {
                Log.d(TAG, e.getMessage());
            }


            return null;
        }


    }
}
