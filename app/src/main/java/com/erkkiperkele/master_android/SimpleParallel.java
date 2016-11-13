package com.erkkiperkele.master_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SimpleParallel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple_parallel);
        setTitle(getString(R.string.simplePi_activity_name));

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    public void calculatePi(View view) {
        TextView piTextView = (TextView) findViewById(R.id.pi_text);
        piTextView.setText(getPiText());
    }

    private String getPiText() {
        Double pi = calculatePi();

        return pi.toString();
    }

    /*****************************************************
     * Native libraries section (loading and declarations)
     */

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native double calculatePi();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("pi-lib");
    }
}
