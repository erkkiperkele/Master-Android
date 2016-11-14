package com.erkkiperkele.master_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        initSeekBar();
    }

    public void calculatePi(View view) {
        TextView piTextView = (TextView) findViewById(R.id.pi_text);
        Double pi = calculatePi(_numberOfOperations);

        piTextView.setText(pi.toString());
    }

    private void setPi(Double piValue){
        TextView piTextView = (TextView) findViewById(R.id.pi_text);
        piTextView.setText(piValue.toString());
    }

    private void initSeekBar() {

        SeekBar sb = (SeekBar) findViewById(R.id.operations_seekbar);
        sb.setProgress(0);

        _seekBarFactor = _maxNumberOfOperations / (sb.getMax() + 1);
        _numberOfOperations = (sb.getProgress() + 1) * _seekBarFactor;

        updateSeekbarText();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                _numberOfOperations = (i + 1) * _seekBarFactor;
                updateSeekbarText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateSeekbarText() {
        TextView operationsText = (TextView) findViewById(R.id.operations_text);
        operationsText.setText(NumberFormat
                .getNumberInstance(Locale.getDefault())
                .format(_numberOfOperations));
    }

    /*****************************************************
     * Native libraries section (loading and declarations)
     */

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native double calculatePi(int numberOfOperations);

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("pi-lib");
    }
}
