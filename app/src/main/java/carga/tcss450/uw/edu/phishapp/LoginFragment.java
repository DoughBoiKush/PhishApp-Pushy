package carga.tcss450.uw.edu.phishapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import carga.tcss450.uw.edu.phishapp.model.Credentials;
import carga.tcss450.uw.edu.phishapp.utils.SendPostAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnLoginFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

    private OnLoginFragmentInteractionListener mListener;
    private Credentials mCredentials;
    private String mJwt;

    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        Button b = (Button) v.findViewById(R.id.login_button_register);
        b.setOnClickListener(this::register);

        b = (Button) v.findViewById(R.id.login_button_signin);
        b.setOnClickListener(this::attemptLogin);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if (getArguments() != null) {
//            Credentials credentials = (Credentials) getArguments()
//                    .getSerializable(getString(R.string.key_credentials_object));
//            updateContent(credentials);
//        }
//    }


    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //retrieve the stored credentials from SharedPrefs
        if (prefs.contains(getString(R.string.keys_prefs_email)) &&
                prefs.contains(getString(R.string.keys_prefs_password))) {

            final String email = prefs.getString(getString(R.string.keys_prefs_email), "");
            final String password = prefs.getString(getString(R.string.keys_prefs_password), "");
            //Load the two login EditTexts with the credentials found in SharedPrefs
            EditText emailEdit = getActivity().findViewById(R.id.login_textview_email);
            emailEdit.setText(email);
            EditText passwordEdit = getActivity().findViewById(R.id.login_textview_password);
            passwordEdit.setText(password);

            doLogin(new Credentials.Builder(
                    emailEdit.getText().toString(),
                    passwordEdit.getText().toString())
                    .build());

        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void register(View view) {
        if (mListener != null) {
            mListener.onRegisterClicked();
        }
    }

//    public void signin(View view) {
//        if (mListener != null) {
//            boolean isValidated = true;
//            TextView textview_email = getActivity().findViewById(R.longDate.login_textview_email);
//            TextView textview_password = getActivity().findViewById(R.longDate.login_textview_password);
//            String username = textview_email.getText().toString();
//
//            // Any empty EditText fields
//            if(TextUtils.isEmpty(textview_email.getText())) {
//                textview_email.setError("Field cannot be empty");
//                isValidated = false;
//            } else if (!textview_email.getText().toString().contains("@")) {
//                textview_email.setError("Invalid email address");
//                isValidated = false;
//            }
//
//            if(TextUtils.isEmpty(textview_password.getText())) {
//                textview_password.setError("Field cannot be empty");
//                isValidated = false;
//            }
//
//            // Do not navigate to SuccessFragment unless login inputs are in the correct format.
//            if (isValidated) {
//                Credentials.Builder credBuilder = new Credentials.Builder(username, textview_password.getText().toString());
//                mListener.onLoginSuccess(credBuilder.build(), "");
//
//
//                Credentials credentials = new Credentials.Builder(
//                        textview_email.getText().toString(),
//                        textview_password.getText().toString())
//                        .build();
//
//                //build the web service URL
//                Uri uri = new Uri.Builder()
//                        .scheme("https")
//                        .appendPath(getString(R.string.ep_base_url))
//                        .appendPath(getString(R.string.ep_login))
//                        .build();
//
//                //build the JSONObject
//                JSONObject msg = credentials.asJSONObject();
//
//                mCredentials = credentials;
//
//                //instantiate and execute the AsyncTask.
//                new SendPostAsyncTask.Builder(uri.toString(), msg)
//                        .onPreExecute(this::handleLoginOnPre)
//                        .onPostExecute(this::handleLoginOnPost)
//                        .onCancelled(this::handleErrorsInTask)
//                        .build().execute();
//            }
//
//        }
//
//    }

    private void attemptLogin(final View theButton) {

        EditText emailEdit = getActivity().findViewById(R.id.login_textview_email);
        EditText passwordEdit = getActivity().findViewById(R.id.login_textview_password);

        boolean hasError = false;
        if (emailEdit.getText().length() == 0) {
            hasError = true;
            emailEdit.setError("Field must not be empty.");
        }  else if (emailEdit.getText().toString().chars().filter(ch -> ch == '@').count() != 1) {
            hasError = true;
            emailEdit.setError("Field must contain a valid email address.");
        }
        if (passwordEdit.getText().length() == 0) {
            hasError = true;
            passwordEdit.setError("Field must not be empty.");
        }

        if (!hasError) {

            doLogin(new Credentials.Builder(
                    emailEdit.getText().toString(),
                    passwordEdit.getText().toString())
                    .build());


//            Credentials credentials = new Credentials.Builder(
//                    emailEdit.getText().toString(),
//                    passwordEdit.getText().toString())
//                    .build();

            //build the web service URL
//            Uri uri = new Uri.Builder()
//                    .scheme("https")
//                    .appendPath(getString(R.string.ep_base_url))
//                    .appendPath(getString(R.string.ep_login))
//                    .build();

            //build the JSONObject
//            JSONObject msg = credentials.asJSONObject();

//            mCredentials = credentials;

            //instantiate and execute the AsyncTask.
//            new SendPostAsyncTask.Builder(uri.toString(), msg)
//                    .onPreExecute(this::handleLoginOnPre)
//                    .onPostExecute(this::handleLoginOnPost)
//                    .onCancelled(this::handleErrorsInTask)
//                    .build().execute();
        }
    }


    private void saveCredentials(final Credentials credentials) {
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //Store the credentials in SharedPrefs
        prefs.edit().putString(getString(R.string.keys_prefs_email), credentials.getEmail()).apply();
        prefs.edit().putString(getString(R.string.keys_prefs_password), credentials.getPassword()).apply();
    }



    public void updateContent(Credentials credentials) {
        TextView textView_email = getActivity().findViewById(R.id.login_textview_email);
        textView_email.setText(credentials.getEmail());

        TextView textView_password = getActivity().findViewById(R.id.login_textview_password);
        textView_password.setText(credentials.getPassword());
    }


    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNC_TASK_ERROR",  result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleLoginOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleLoginOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_login_success));

            if (success) {
                //Login was successful. Switch to the loadSuccessFragment.

                mJwt = resultsJSON.getString(
                        getString(R.string.keys_json_login_jwt));

                saveCredentials(mCredentials);
                mListener.onLoginSuccess(mCredentials, mJwt);


//                mListener.onLoginSuccess(mCredentials,
//                        resultsJSON.getString(
//                                getString(R.string.keys_json_login_jwt)));
                return;
            } else {
                //Login was unsuccessful. Don’t switch fragments and
                // inform the user
                ((TextView) getView().findViewById(R.id.login_textview_email))
                        .setError("Login Unsuccessful");
            }
            mListener.onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR",  result
                    + System.lineSeparator()
                    + e.getMessage());

            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.login_textview_email))
                    .setError("Login Unsuccessful");
        }
    }


    private void doLogin(Credentials credentials) {
        //build the web service URL
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_login))
                .build();

        //build the JSONObject
        JSONObject msg = credentials.asJSONObject();

        mCredentials = credentials;

        Log.d("JSON Credentials", msg.toString());

        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::handleLoginOnPre)
                .onPostExecute(this::handleLoginOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }





    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLoginFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {

        void onLoginSuccess(Credentials credentials, String jwt);

        void onRegisterClicked();
    }
}

