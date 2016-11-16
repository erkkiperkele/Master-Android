package com.erkkiperkele.master_android.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.erkkiperkele.master_android.R;
import com.erkkiperkele.master_android.entity.JResult;
import com.erkkiperkele.master_android.service.MultiPiDataService;
import com.erkkiperkele.master_android.utility.DateTimeProvider;

import java.text.NumberFormat;
import java.util.Locale;

public class MultiPiActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressWarnings("FieldCanBeLocal")
    private final int _maxNumberOfOperations = 20000000;

    private final DateTimeProvider _dateTimeProvider = DateTimeProvider.getInstance();
    private final MultiPiDataService _multiPiDataService = MultiPiDataService.getInstance();

    private int _numberOfOperations = 0;
    private int _seekBarFactor = 0;
    private int _numberOfThreads = 2;
    private int _minNumberOfThreads = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multi_pi);

        initNavigationDrawer();
        initOperationsSeekBar();
        initThreadsSeekBar();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.simple_pi) {
            Intent i = new Intent(this, SimplePiActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // This method is directly called by the UI, hence the public accessor and the view param
    public void calculateMultiPi(@SuppressWarnings("UnusedParameters") View view) {

        new MultiPiActivity.CalculatePiTask().execute(_numberOfOperations);
    }

    private void initNavigationDrawer(){
        // Set the action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.activity_name_multiPi);
        setSupportActionBar(toolbar);

        // Set the navigation pane
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initOperationsSeekBar() {

        SeekBar sb = (SeekBar) findViewById(R.id.operations_seekbar);
        sb.setProgress(0);

        _seekBarFactor = _maxNumberOfOperations / (sb.getMax() + 1);
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

    private void initThreadsSeekBar() {

        SeekBar sb = (SeekBar) findViewById(R.id.threads_seekbar);
        sb.setProgress(0);

        _numberOfThreads = _minNumberOfThreads;

        updateThreadsSeekBarText();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                _numberOfThreads =  (int) Math.pow(_minNumberOfThreads, i+1);
                updateThreadsSeekBarText();
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

    private void updateThreadsSeekBarText() {
        TextView operationsText = (TextView) findViewById(R.id.threads_text);
        operationsText.setText(NumberFormat
                .getNumberInstance(Locale.getDefault())
                .format(_numberOfThreads));
    }

    /**
     * Loading Native Libraries and declaring native methods
     */
    public native JResult calculatePi(int numberOfOperations, int numberOfThreads);

    static {
        System.loadLibrary("parallel-pi-lib");
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

            JResult result = calculatePi(numberOfOperations[0], _numberOfThreads)
                    .setId(timeStamp)
                    .setAlgorithmName(getResources().getString(R.string.activity_name_multiPi))
                    .setTaskSize(_numberOfOperations)
                    .setTaskSizeUnit(getString(R.string.task_size_unit_operations))
                    .setExecutionDateTimePretty(simpleDate)
                    .setThreadsCount(_numberOfThreads);

            _multiPiDataService.saveResult(result);

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