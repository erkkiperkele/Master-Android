package com.erkkiperkele.master_android;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

public class SimpleParallel extends AppCompatActivity {

    private int _numberOfOperations = 0;
    private int _maxNumberOfOperations = 20000000;
    private int _seekBarFactor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple_parallel);
        setTitle(getString(R.string.simplePi_activity_name));

        initSeekBar();
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

