package com.erkkiperkele.master_android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.erkkiperkele.master_android.R;
import com.erkkiperkele.master_android.entity.JResult;
import com.erkkiperkele.master_android.service.SimplePiDataService;
import com.erkkiperkele.master_android.service.UserService;
import com.erkkiperkele.master_android.utility.DateTimeProvider;
import com.erkkiperkele.master_android.utility.ResultViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.Query;

import java.text.NumberFormat;
import java.util.Locale;

public class SimplePiActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_FIREBASE_AUTH = "firebase_auth";
    private static final String LOG_GOOGLE_AUTH = "firebase_auth";

    @SuppressWarnings("FieldCanBeLocal")
    private static final int MAX_NUMBER_OF_OPERATIONS = 20000000;
    private static final int QUERY_SIZE = 20;

    private final DateTimeProvider _dateTimeProvider = DateTimeProvider.getInstance();
    private final SimplePiDataService _simplePiDataService = SimplePiDataService.getInstance();

    private int _numberOfOperations = 0;
    private int _seekBarFactor = 0;

    private GoogleApiClient _GoogleApiClient;
    private final int RC_SIGN_IN = 1;
    private FirebaseAuth _firebaseAuth;
    private FirebaseAuth.AuthStateListener _firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple_pi);

        initNavigationDrawer();
        initSeekBar();

        initGoogleSignin();
        initFirebaseSignIn();

        initResultsRecyclerView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_simple_pi);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.multi_pi) {
            Intent i = new Intent(this, MultiPiActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_simple_pi);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initNavigationDrawer() {
        // Set the action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple_pi);
        toolbar.setTitle(R.string.activity_name_simplePi);
        setSupportActionBar(toolbar);

        // Set the navigation pane
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_simple_pi);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_simple_pi);
        navigationView.setNavigationItemSelectedListener(this);
        updateNavigationHeader();
    }

    private void initGoogleSignin() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        _GoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initFirebaseSignIn() {
        _firebaseAuth = FirebaseAuth.getInstance();
        _firebaseAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(LOG_FIREBASE_AUTH, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(LOG_FIREBASE_AUTH, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UserService userService = UserService.getInstance();

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                userService.setGoogleSignInAccount(result.getSignInAccount());
                firebaseAuthWithGoogle(userService.getGoogleSignInAccount());
                updateNavigationHeader();
            } else {
                Log.w(LOG_GOOGLE_AUTH, "signInWithCredential:" + result.getStatus().toString());
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(LOG_FIREBASE_AUTH, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        _firebaseAuth
                .signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_FIREBASE_AUTH, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(LOG_FIREBASE_AUTH, "signInWithCredential", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        _firebaseAuth.addAuthStateListener(_firebaseAuthListener);

        if (!UserService.getInstance().isConnected()) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(_GoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (_firebaseAuthListener != null) {
            _firebaseAuth.removeAuthStateListener(_firebaseAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d(LOG_GOOGLE_AUTH, "Master! Google Authentication Failed!");
    }

    private void initResultsRecyclerView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_simple_pi);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        Query query = _simplePiDataService
                .getUserResultsReference()
                .orderByChild("id")
                .limitToLast(QUERY_SIZE);

        final Context context = getApplicationContext();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<JResult, ResultViewHolder>(
                JResult.class,
                R.layout.pi_result_item,
                ResultViewHolder.class,
                query) {

            @Override
            protected void populateViewHolder(ResultViewHolder viewHolder, JResult model, int position) {
                viewHolder.setResult(model, context);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void initSeekBar() {

        SeekBar sb = (SeekBar) findViewById(R.id.operations_seekbar);
        sb.setProgress(0);

        _seekBarFactor = MAX_NUMBER_OF_OPERATIONS / (sb.getMax() + 1);
        _numberOfOperations = (sb.getProgress() + 1) * _seekBarFactor;

        updateSeekBarText();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                _numberOfOperations = (i + 1) * _seekBarFactor;
                updateSeekBarText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void updateSeekBarText() {
        TextView operationsText = (TextView) findViewById(R.id.operations_text);
        operationsText.setText(NumberFormat
                .getNumberInstance(Locale.getDefault())
                .format(_numberOfOperations));
    }

    private void updateNavigationHeader(){
        GoogleSignInAccount user = UserService.getInstance().getGoogleSignInAccount();

        if (user != null){
            NavigationView navDrawer = (NavigationView) findViewById(R.id.nav_view_simple_pi);
            View header = navDrawer.getHeaderView(0);
            TextView headerUserName = (TextView) header.findViewById(R.id.nav_header_user_name);
            headerUserName.setText(user.getDisplayName());
        }
    }

    // This method is directly called by the UI, hence the public accessor and the view param
    public void calculatePi(@SuppressWarnings("UnusedParameters") View view) {

        new CalculatePiTask().execute(_numberOfOperations);
    }

    /**
     * Loading Native Libraries and declaring native methods
     */
    public native JResult calculatePi(int numberOfOperations);

    static {
        System.loadLibrary("pi-lib");
    }

    /**
     * An async task to update UI on UI thread but run calculation in the background
     */
    private class CalculatePiTask extends AsyncTask<Integer, Void, JResult> {

        @Override
        protected void onPreExecute() {
            Button piButton = (Button) findViewById(R.id.pi_button);
            piButton.setEnabled(false);

            TextView piTextView = (TextView) findViewById(R.id.pi_text);
            piTextView.setText(R.string.calculating_pi);
        }

        @Override
        protected JResult doInBackground(Integer... numberOfOperations) {

            Long timeStamp = _dateTimeProvider.getTimeStampNow();
            String simpleDate = _dateTimeProvider.getPrettyDateTime(timeStamp);

            JResult result = calculatePi(numberOfOperations[0])
                    .setId(timeStamp)
                    .setAlgorithmName(getResources().getString(R.string.activity_name_simplePi))
                    .setTaskSize(_numberOfOperations)
                    .setTaskSizeUnit(getString(R.string.task_size_unit_operations))
                    .setExecutionDateTimePretty(simpleDate)
                    .setThreadsCount(1);

            _simplePiDataService.saveResult(result);

            return result;
        }

        protected void onPostExecute(JResult result) {
            NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.getDefault());
            numberFormatter.setMinimumFractionDigits(10);

            // Display Pi calculation result
            TextView piTextView = (TextView) findViewById(R.id.pi_text);
            piTextView.setText(numberFormatter.format(result.getResult()));

            // Display Pi calculation execution time
            TextView piExecutionTimeTextView = (TextView) findViewById(R.id.pi_execution_time_text);
            String executionText = numberFormatter.format(result.getExecutionTimeInS()) + " sec.";
            piExecutionTimeTextView.setText(executionText);

            // Re-enable The pi button
            Button piButton = (Button) findViewById(R.id.pi_button);
            piButton.setEnabled(true);
        }
    }
}