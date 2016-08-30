package paul.caloriebuster;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public  class MainActivity extends Activity implements SensorEventListener {

    String s="M";
    float stepsCount= 0;

    private SensorManager mSensorManager;

    private float   mLimit = 10;
    private float   mLastValues[] = new float[3*2];
    private float   mScale[] = new float[2];
    private float   mYOffset;

    private float   mLastDirections[] = new float[3*2];
    private float   mLastExtremes[][] = { new float[3*2], new float[3*2] };
    private float   mLastDiff[] = new float[3*2];
    private int     mLastMatch = -1;
    TextView step;


    //Sensor Functions

    public void StepDetector() {
        int h = 480; // TODO: remove this constant
        mYOffset = h * 0.5f;
        mScale[0] = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = - (h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        step=(TextView)findViewById(R.id.Step);
        StepDetector();

        // Initialize Accelerometer sensor
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);



    }

    @Override
    public void onSensorChanged(SensorEvent event) {
                    float vSum = 0;
                    for (int i=0 ; i<3 ; i++) {
                        final float v = mYOffset + event.values[i] * mScale[1];
                        vSum += v;
                    }
                    int k = 0;
                    float v = vSum / 3;

                    float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                    if (direction == - mLastDirections[k]) {
                        // Direction changed
                        int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
                        mLastExtremes[extType][k] = mLastValues[k];
                        float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

                        if (diff > mLimit) {

                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k]*2/3);
                            boolean isPreviousLargeEnough = mLastDiff[k] > (diff/3);
                            boolean isNotContra = (mLastMatch != 1 - extType);

                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                stepsCount=stepsCount+1;
                                step.setText(""+stepsCount);
                                mLastMatch = extType;

                            }
                            else {
                                mLastMatch = -1;
                            }
                        }
                        mLastDiff[k] = diff;
                    }
                    mLastDirections[k] = direction;
                    mLastValues[k] = v;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }






    //UI Functions

    public void Start(View v) {

        EditText noise = (EditText) findViewById(R.id.noise);
        String tmp = noise.getText().toString();
        if (tmp.matches("")){
            Toast.makeText(MainActivity.this, "Please enter callibration data first", Toast.LENGTH_LONG).show();
            }
        else {
            mLimit = Float.valueOf(tmp);
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void Stop(View v)
    {

        mSensorManager.unregisterListener(this);
    }

    public void Reset(View v)
    {
        //Resetting Step Counter To 0
        TextView step = (TextView)findViewById(R.id.Step);
        step.setText("" + 0);
        stepsCount = 0;
        EditText tmp=(EditText)findViewById(R.id.noise);
        tmp.setText("");
        mSensorManager.unregisterListener(this);
    }


    public void rb(View v){
        switch (v.getId()){
            case R.id.radioButton:
                s="M";
                break;
            case  R.id.radioButton2:
                s="F";
                break;
        }
    }



    public void CalculateCalorie(View v) {
        int calo = 0;
        int flag = 0;//for exception handling when converting null string to float

        EditText ed1 = (EditText) findViewById(R.id.weight);
        EditText ed2 = (EditText) findViewById(R.id.age);
        EditText ed3 = (EditText) findViewById(R.id.stepsize);
        TextView tv = (TextView) findViewById(R.id.Step);

        String s1 = ed1.getText().toString();
        String s2 = ed2.getText().toString();
        String s3 = ed3.getText().toString();
        String s4 = tv.getText().toString();

        float steps=0;
        float wt=0;
        float age=0;
        float stepsize=0;

        if (s1.matches("")) {

        } else {
            flag = flag + 1;
            wt = Float.valueOf(s1);
        }


        if (s2.matches("")) {

        } else {
            flag = flag + 1;
            age = Float.valueOf(s2);
        }

        if (s1.matches("")) {

        } else {
            flag = flag + 1;
            stepsize = Float.valueOf(s3);
        }

        if (s1.matches("")) {

        } else {
            flag = flag + 1;
            steps = Float.valueOf(s4);
        }


        if (flag == 4 && steps != 0) {
            float dist = (float) ((stepsize * steps)*0.00018939);
            if (s.matches("M")) {
                if (age <= 18) {
                    calo = (int) ((((wt / 2.204) * 17.5) + 651) * 0.53 * (dist));
                } else if (age <= 30 && age > 18) {
                    calo = (int) ((((wt / 2.204) * 15.3) + 679) * 0.53 * (dist));
                } else if (age <= 60 && age > 30) {
                    calo = (int) ((((wt / 2.204) * 11.6) + 879) * 0.53 * (dist));
                } else if (age > 60) {
                    calo = (int) ((((wt / 2.204) * 13.5) + 487) * 0.53 * (dist));
                }
            } else if (s.matches("F")) {
                if (age <= 18) {
                    calo = (int) ((((wt / 2.204) * 12.2) + 746) * 0.53 * (dist));
                } else if (age <= 30 && age > 18) {
                    calo = (int) ((((wt / 2.204) * 14.7) + 496) * 0.53 * (dist));
                } else if (age <= 60 && age > 30) {
                    calo = (int) ((((wt / 2.204) * 8.7) + 829) * 0.53 * (dist));
                } else if (age > 60) {
                    calo = (int) ((((wt / 2.204) * 10.5) + 596) * 0.53 * (dist));
                }
            }
            TextView tv1 = (TextView) findViewById(R.id.textView6);
            tv1.setText("Calorie Burnt: " + calo);
        }
        else if(flag < 4){
            Toast.makeText(MainActivity.this, "Please fill all fields first", Toast.LENGTH_LONG).show();}
        else if(steps == 0){
            Toast.makeText(MainActivity.this, "Walk to increase step counter first", Toast.LENGTH_LONG).show();
        }
    }
}
