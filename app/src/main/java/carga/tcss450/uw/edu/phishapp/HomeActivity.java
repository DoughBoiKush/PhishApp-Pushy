package carga.tcss450.uw.edu.phishapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import carga.tcss450.uw.edu.phishapp.blog.BlogPost;
import carga.tcss450.uw.edu.phishapp.model.Credentials;
import carga.tcss450.uw.edu.phishapp.setlist.SetList;
import carga.tcss450.uw.edu.phishapp.utils.GetAsyncTask;
import me.pushy.sdk.Pushy;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SuccessFragment.OnSuccessFragmentInteractionListener,
        BlogFragment.OnBlogListFragmentInteractionListener,
        BlogPostFragment.OnBlogPostFragmentInteractionListener,
        WaitFragment.OnFragmentInteractionListener,
        SetListFragment.OnSetListFragmentInteractionListener,
        SetListItemFragment.OnSetListItemFragmentInteractionListener {

    private String mJwToken;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if(savedInstanceState == null) {
            Log.wtf("WTF", "SavedInstance: NULL");

            if (findViewById(R.id.frame_main_container) != null) {
                Credentials credentials = (Credentials) getIntent()
                        .getSerializableExtra(getString(R.string.keys_intent_credentials));
                String emailAddress = mEmail = credentials.getEmail();
                final Bundle args = new Bundle();
                args.putString(getString(R.string.key_email), emailAddress);

                Fragment fragment;
                if (getIntent().getBooleanExtra(getString(R.string.keys_intent_notification_msg), false)) {
                    fragment = new ChatFragment();
                    fragment.setArguments(args);

                } else {
                    fragment = new SuccessFragment();
                    fragment.setArguments(args);
                }

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, fragment)
                        .commit();


            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Credentials credentials = (Credentials) getIntent().getExtras().getSerializable(getString(R.string.keys_intent_credentials));
            SuccessFragment successFragment = new SuccessFragment();
            Bundle args = new Bundle();
            args.putSerializable(getString(R.string.keys_intent_credentials), credentials);
            successFragment.setArguments(args);
            loadFragment(successFragment);
        } else if (id == R.id.nav_blog_posts) {
//            loadFragment(new BlogFragment());
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_phish))
                    .appendPath(getString(R.string.ep_blog))
                    .appendPath(getString(R.string.ep_get))
                    .build();
            GetAsyncTask b = new GetAsyncTask.Builder(uri.toString())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleBlogGetOnPostExecute)
                    .addHeaderField("authorization", mJwToken) //add the JWT as a header
                    .build();
            b.execute();
        } else if (id == R.id.nav_set_lists) {
//            loadFragment(new SetListFragment());
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_phish))
                    .appendPath(getString(R.string.ep_setlists))
                    .appendPath(getString(R.string.ep_recent))
                    .build();
            GetAsyncTask b = new GetAsyncTask.Builder(uri.toString())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleSetListGetOnPostExecute)
                    .addHeaderField("authorization", mJwToken) //add the JWT as a header
                    .build();
            b.execute();
        } else if (id == R.id.nav_gobal_chat) {
            Credentials credentials = (Credentials) getIntent().getExtras().getSerializable(getString(R.string.keys_intent_credentials));
            Fragment chatFragment = new ChatFragment();
            Bundle args = new Bundle();
            args.putSerializable(getString(R.string.key_email), credentials.getEmail());
            args.putSerializable(getString(R.string.keys_intent_jwt), mJwToken);
            chatFragment.setArguments(args);
            loadFragment(chatFragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSuccessFragmentInteraction(Credentials credentials) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadSuccess();
    }

    private void loadSuccess() {
        Credentials credentials = (Credentials)getIntent()
                .getExtras().getSerializable(getString(R.string.keys_intent_credentials));
        mJwToken = getIntent().getStringExtra(getString(R.string.keys_intent_jwt));

        Fragment successFragment = new SuccessFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.keys_intent_credentials), credentials);
        successFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, successFragment);

        transaction.commit();
    }

    @Override
    public void onBlogListFragmentInteraction(BlogPost item) {

        BlogPostFragment blogPostFragment = new BlogPostFragment();

        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.key_blog_post_object), item);
        blogPostFragment.setArguments(args);

        if (findViewById(R.id.fragmentContainer) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, blogPostFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBlogPostFragmentInteraction(View view) {

    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, new WaitFragment(), "WAIT")
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


    private void handleBlogGetOnPostExecute(final String result) {
        //parse JSON

        try {
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_json_blogs_response))) {
                JSONObject response = root.getJSONObject(
                        getString(R.string.keys_json_blogs_response));
                if (response.has(getString(R.string.keys_json_blogs_data))) {
                    JSONArray data = response.getJSONArray(
                            getString(R.string.keys_json_blogs_data));

                    List<BlogPost> blogs = new ArrayList<>();

                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonBlog = data.getJSONObject(i);

                        blogs.add(new BlogPost.Builder(
                                jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_pubdate)),
                                jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_title)))
                                .addTeaser(jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_teaser)))
                                .addUrl(jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_url)))
                                .build());
                    }

                    BlogPost[] blogsAsArray = new BlogPost[blogs.size()];
                    blogsAsArray = blogs.toArray(blogsAsArray);

                    Bundle args = new Bundle();
                    args.putSerializable(BlogFragment.ARG_BLOG_LIST, blogsAsArray);
                    Fragment frag = new BlogFragment();
                    frag.setArguments(args);

                    onWaitFragmentInteractionHide();
                    loadFragment(frag);
                } else {
                    Log.e("ERROR!", "No data array");
                    //notify user
                    onWaitFragmentInteractionHide();
                }
            } else {
                Log.e("ERROR!", "No response");
                //notify user
                onWaitFragmentInteractionHide();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    private void handleSetListGetOnPostExecute(final String result) {
        //parse JSON

        try {
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_json_setlist_response))) {
                JSONObject response = root.getJSONObject(
                        getString(R.string.keys_json_setlist_response));
                if (response.has(getString(R.string.keys_json_setlist_date))) {
                    JSONArray data = response.getJSONArray(
                            getString(R.string.keys_json_setlist_date));

                    List<SetList> setLists = new ArrayList<>();

                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonSetList = data.getJSONObject(i);

                        setLists.add(new SetList.Builder(
                                jsonSetList.getString(
                                        getString(R.string.keys_json_setlist_longdate)),
                                jsonSetList.getString(
                                        getString(R.string.keys_json_setlist_location)))
                                .addVenue(jsonSetList.getString(
                                        getString(R.string.keys_json_setlist_venue)))
                                .addSetListData(jsonSetList.getString(
                                        getString(R.string.keys_json_setlist_setlist_data)))
                                .addSetListNotes(jsonSetList.getString(
                                        getString(R.string.keys_json_setlist_setlist_notes)))
                                .addUrl(jsonSetList.getString(
                                        getString(R.string.keys_json_setlist_url)))
                                .build());
                    }

                    SetList[] setListAsArray = new SetList[setLists.size()];
                    setListAsArray = setLists.toArray(setListAsArray);


                    Bundle args = new Bundle();
                    args.putSerializable(SetListFragment.ARG_SETLIST, setListAsArray);
                    Fragment frag = new SetListFragment();
                    frag.setArguments(args);

                    onWaitFragmentInteractionHide();
                    loadFragment(frag);
                } else {
                    Log.e("ERROR!", "No data array");
                    //notify user
                    onWaitFragmentInteractionHide();
                }
            } else {
                Log.e("ERROR!", "No response");
                //notify user
                onWaitFragmentInteractionHide();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }


    private void logout() {

        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //remove the saved credentials from StoredPrefs
        prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();

        new DeleteTokenAsyncTask().execute();

        //close the app
        finishAndRemoveTask();

        //or close this activity and bring back the Login
        //Intent i = new Intent(this, MainActivity.class);
        //startActivity(i);
        //End this Activity and remove it from the Activity back stack.
        //finish();
    }



    private void loadFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onSetListFragmentInteraction(SetList item) {
        SetListItemFragment setListItemFragment = new SetListItemFragment();

        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.key_setlist_object), item);
        setListItemFragment.setArguments(args);

        if (findViewById(R.id.fragmentContainer) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, setListItemFragment)
                    .addToBackStack(null)
                    .commit();

        }
    }


    // Deleting the Pushy device token must be done asynchronously. Good thing
    // we have something that allows us to do that.
    class DeleteTokenAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onWaitFragmentInteractionShow();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //since we are already doing stuff in the background, go ahead
            //and remove the credentials from shared prefs here.
            SharedPreferences prefs =
                    getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);

            prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
            prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();

            //unregister the device from the Pushy servers
            Pushy.unregister(HomeActivity.this);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //close the app
            finishAndRemoveTask();

            //or close this activity and bring back the Login
//            Intent i = new Intent(this, MainActivity.class);
//            startActivity(i);
//            //Ends this Activity and removes it from the Activity back stack.
//            finish();
        }
    }



    @Override
    public void onSetListButtonFragmentInteraction(View view) {

    }
}
