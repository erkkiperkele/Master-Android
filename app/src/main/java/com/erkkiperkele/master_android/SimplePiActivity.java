package com.erkkiperkele.master_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import java.text.NumberFormat;
import java.util.Locale;

public class SimplePiActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int _numberOfOperations = 0;
    private int _maxNumberOfOperations = 20000000;
    private int _seekBarFactor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple_pi);

        initSeekBar();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void calculatePi(View view) {
        new CalculatePiTask().execute(_numberOfOperations);
    }

    private void initSeekBar() {

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

    private void updateSeekBarText() {
        TextView operationsText = (TextView) findViewById(R.id.operations_text);
        operationsText.setText(NumberFormat
                .getNumberInstance(Locale.getDefault())
                .format(_numberOfOperations));
    }

    /**
     * Loading Native Libraries and declaring native methods
     */
    public native double calculatePi(int numberOfOperations);

    static {
        System.loadLibrary("pi-lib");
    }

    /**
     * An async task to update UI on UI thread but run calculation in the background
     */
    private class CalculatePiTask extends AsyncTask<Integer, Void, Double> {

        @Override
        protected void onPreExecute() {
            Button piButton = (Button) findViewById(R.id.pi_button);
            piButton.setEnabled(false);

            TextView piTextView = (TextView) findViewById(R.id.pi_text);
            piTextView.setText(R.string.calculating_pi);
        }

        @Override
        protected Double doInBackground(Integer... numberOfOperations) {
            return calculatePi(numberOfOperations[0]);
        }

        protected void onPostExecute(Double result) {
            NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.getDefault());
            numberFormatter.setMinimumFractionDigits(6);

            TextView piTextView = (TextView) findViewById(R.id.pi_text);
            piTextView.setText(numberFormatter.format(result));

            Button piButton = (Button) findViewById(R.id.pi_button);
            piButton.setEnabled(true);
        }
    }
}

