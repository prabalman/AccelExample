package com.sshakya.accelexample;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    final String url = "http://192.168.0.101/create.php";

    private SensorManager sensorManager;
    TextView tvX, tvY, tvZ;

    String sx, sy, sz, sw;
    double xxval, yyval, zzval;

    int num = 0;
    final double alpha = 0.8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvX = (TextView) findViewById(R.id.tvX);
        tvY = (TextView) findViewById(R.id.tvY);
        tvZ = (TextView) findViewById(R.id.tvZ);
        final TextView tv = (TextView) findViewById(R.id.tv1);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        tv.setText("Works here.");
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double xVal, yVal, zVal;
            double[] gravity = new double[]{0, 1, 2};

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            xVal = event.values[0] - gravity[0];
            yVal = event.values[1] - gravity[1];
            zVal = event.values[2] - gravity[2];

            xxval = rountToDouble(xVal);
            yyval = rountToDouble(yVal);
            zzval = rountToDouble(zVal);
            new AccelTask(xxval,yyval,zzval).execute();

            tvX.setText("" + xxval);
            tvY.setText("" + yyval);
            tvZ.setText("" + zzval);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    double rountToDouble(double num) {
            return Double.valueOf(new DecimalFormat("#.###").format(num));
        }

    public class AccelTask extends AsyncTask<Void, Void, Void> {

        public Double x;
        public Double y;
        public Double z;
        AccelTask(Double x, Double y, Double z){
           this.x=x;
            this.y=y;
            this.z=z;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String xx = String.valueOf(x);
            String yy= String.valueOf(y);
            String zz = String.valueOf(z);
            final Map<String, String> parameters = new HashMap<>();
            try {
                parameters.put("x",xx);
                parameters.put("y",yy);
                parameters.put("z", zz);
                Log.d("Insert", "params == " + params.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("JSONsend", e.toString());
            }
            SystemClock.sleep(1000);
            CustomRequest jsonObject = new CustomRequest(Request.Method.POST, url,parameters, new Response.Listener<JSONObject>() {

                @Override

                public void onResponse(JSONObject response) {

                    try {
                        Boolean success = response.getBoolean("success");
                        String message = response.getString("message");
                        Log.d("JSONsend", "success: " + success);
                        if (success) {

                            Log.d("JSONsend", "success" + message);


                        } else {
                            Log.d("JSONsend", "no success-------");
                        }
                    } catch (JSONException e) {
                        Log.d("JSONsend", "Exception ---------" + e.toString());
                    }

                    Log.d("Response",response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("JSONsend", "ERROR listener "+ error.toString());
                }
            });
            requestQueue.add(jsonObject);

            return null;
        }

    }

}
