package carga.tcss450.uw.edu.phishapp;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

import carga.tcss450.uw.edu.phishapp.model.Credentials;
import me.pushy.sdk.Pushy;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener, RegisterFragment.OnRegisterFragmentInteractionListener, SuccessFragment.OnSuccessFragmentInteractionListener, WaitFragment.OnFragmentInteractionListener {


    private boolean mLoadFromChatNotification = false;
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushy.listen(this);

        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("type")) {
                mLoadFromChatNotification = getIntent().getExtras().getString("type").equals("msg");
            }
        }

        if (savedInstanceState == null) {
            if (findViewById(R.id.frame_main_container) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_main_container, new LoginFragment())
                        .commit();
            }
        }
    }


    @Override
    public void onLoginSuccess(Credentials credentials, String jwt) {
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra(getString(R.string.keys_intent_credentials), (Serializable) credentials);
        i.putExtra(getString(R.string.keys_intent_jwt), jwt);
        i.putExtra(getString(R.string.keys_intent_notification_msg), mLoadFromChatNotification);
        startActivity(i);
        //End this Activity and remove it from the Activity back stack.
        finish();
    }

    private void login(final Credentials credentials) {
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra(getString(R.string.key_email), (Serializable) credentials);
        i.putExtra(getString(R.string.keys_intent_notification_msg), mLoadFromChatNotification);
        startActivity(i);
        //Ends this Activity and removes it from the Activity back stack.
        finish();
    }



    @Override
    public void onRegisterClicked() {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_container, new RegisterFragment())
                .addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


    @Override
    public void onRegisterSuccess(Credentials credentials) {

        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        LoginFragment loginFragment = new LoginFragment();

        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.key_credentials_object), credentials);
        loginFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_container, loginFragment);

        // Commit the transaction
        transaction.commit();
    }


    @Override
    public void onSuccessFragmentInteraction(Credentials credentials) {
        // No code here because there is no interaction after a successful login
    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_main_container, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }
}
