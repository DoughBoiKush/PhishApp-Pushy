package carga.tcss450.uw.edu.phishapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import carga.tcss450.uw.edu.phishapp.model.Credentials;
import carga.tcss450.uw.edu.phishapp.utils.SendPostAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnRegisterFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    private OnRegisterFragmentInteractionListener mListener;
    private Credentials mCredentials;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }

        View v = inflater.inflate(R.layout.fragment_register, container, false);
        // Inflate the layout for this fragment

        Button b = (Button) v.findViewById(R.id.btn_fragment_register_register);
        b.setOnClickListener(this::register);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentInteractionListener) {
            mListener = (OnRegisterFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSuccessFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void register(View view) {
        if (mListener != null) {
            boolean isValidated = true;
            TextView textview_username = getActivity().findViewById(R.id.editText_fragment_register_username);
            TextView textview_firstName = getActivity().findViewById(R.id.editText_fragment_register_firstname);
            TextView textview_lastName = getActivity().findViewById(R.id.editText_fragment_register_lastname);
            TextView textview_email = getActivity().findViewById(R.id.editText_fragment_register_email);
            TextView textview_password = getActivity().findViewById(R.id.editText_fragment_register_password);
            TextView textview_password_confirm = getActivity().findViewById(R.id.editText_fragment_register_password_confirm);


            if (TextUtils.isEmpty(textview_username.getText())) {
                textview_username.setError("Field cannot be empty");
                isValidated = false;
            } else if (TextUtils.isEmpty(textview_firstName.getText())) {
                textview_firstName.setError("Field cannot be empty");
                isValidated = false;
            } else if (TextUtils.isEmpty(textview_lastName.getText())) {
                textview_lastName.setError("Field cannot be empty");
                isValidated = false;
            }

            // Any empty EditText fields
            if(TextUtils.isEmpty(textview_email.getText())) {
                textview_email.setError("Field cannot be empty");
                isValidated = false;
            } else if (!textview_email.getText().toString().contains("@")) {
                textview_email.setError("Invalid email address");
                isValidated = false;
            }
            if(TextUtils.isEmpty(textview_password.getText())) {
                textview_password.setError("Field cannot be empty");
                isValidated = false;
            }
            if(TextUtils.isEmpty(textview_password_confirm.getText().toString())) {
                textview_password_confirm.setError("Field cannot be empty");
                isValidated = false;
            }

            // Check for password equality
            if (!(textview_password.getText().toString().equals(textview_password_confirm.getText().toString()))) {
                textview_password.setError("Password fields do not match!");
                textview_password_confirm.setError("Password fields do not match!");
                isValidated = false;
            }


            // Check for passwords less than 6 characters
            if(textview_password.getText().toString().length() < 6) {
                textview_password.setError("Password cannot be less than 6 characters");
                isValidated = false;
            }
            if(textview_password_confirm.getText().toString().length() < 6) {
                textview_password_confirm.setError("Password cannot be less than 6 characters");
                isValidated = false;
            }

            // Do not navigate to another fragment unless register inputs are in the correct format.
            if (isValidated) {
                Credentials credentials = new Credentials.Builder(
                        textview_email.getText().toString(),
                        textview_password.getText().toString())
                        .addUsername(textview_username.getText().toString())
                        .addFirstName(textview_firstName.getText().toString())
                        .addLastName(textview_lastName.getText().toString())
                        .build();

                //build the web service URL
                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_register))
                        .build();
                //build the JSONObject
                JSONObject msg = credentials.asJSONObject();

                mCredentials = credentials;

                //instantiate and execute the AsyncTask.
                new SendPostAsyncTask.Builder(uri.toString(), msg)
                        .onPreExecute(this::handleRegisterOnPre)
                        .onPostExecute(this::handleRegisterOnPost)
                        .onCancelled(this::handleErrorsInTask)
                        .build().execute();

//                mListener.onRegisterSuccess(credentials);
            }
        }
    }

//

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
    private void handleRegisterOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleRegisterOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_login_success));

            if (success) {
//                Registration was successful. Switch to the loginFragment.
                mListener.onRegisterSuccess(mCredentials);
                return;
            } else {
                //Registration was unsuccessful. Don’t switch fragments and
                // inform the user
                ((TextView) getView().findViewById(R.id.editText_fragment_register_email))
                        .setError("Registration Unsuccessful");
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
                    .setError("Registration Unsuccessful");
        }
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
    public interface OnRegisterFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener{
        void onRegisterSuccess(Credentials credentials);
    }



}
