package kylecrowley.cs491b;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MainActivity extends FragmentActivity {
    // load default values for Sparks, NV
    double longitude = -119.7356;
    double latitude = 39.5544;
    double radius = 1000.0;

    private String APIKey = "AIzaSyCeoJkIK5Jgv3pWYQ0VSUy41QFEDE29PME";
    private EditText longi;
    private EditText lati;
    private EditText radi;

    //http vars
    private TextView resultText;
    private String baseUrl = "https://maps.googleapis.com/maps/api/place/search/json?";
    private String finalUrl = "";
    int pix1, pix2, pix3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longi = (EditText) findViewById(R.id.editTextLong);
        lati = (EditText) findViewById(R.id.editTextLat);
        radi = (EditText) findViewById(R.id.editTextRadius);

        // create arrays for table data
        pix1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, getResources().getDisplayMetrics());
        pix2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        pix3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.gps_action:
                GPSTracker gps = new GPSTracker(MainActivity.this);
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                } else {
                    gps.showSettingsAlert();
                }
                lati.setText(String.valueOf(latitude));
                longi.setText(String.valueOf(longitude));
                return true;

            case R.id.clear_action:
                ViewGroup layout = (LinearLayout) findViewById(R.id.LinearLayout1);
                layout.removeAllViews();
                return true;
        }
        return false;
    }

    public void buttonSearch(View view) {
        if (longi.getText().toString().length() > 0) {
            longitude = Double.valueOf(longi.getText().toString());
        }
        if (lati.getText().toString().length() > 0) {
            latitude = Double.valueOf(lati.getText().toString());
        }
        if (radi.getText().toString().length() > 0) {
            radius = Double.valueOf(radi.getText().toString());
        }

        finalUrl = baseUrl + "location=" + latitude + "," + longitude + "&radius=" + radius + "&key=" + APIKey;
        // https://maps.googleapis.com/maps/api/place/search/json?location=18.00,100.00&radius=5000&key=AIzaSyCeoJkIK5Jgv3pWYQ0VSUy41QFEDE29PME
        new DownloadTask().execute(finalUrl);
    }

    private class DownloadTask extends AsyncTask<String, Void, JSONObject> {
        String str = "";
        JSONObject json;
        View poiTable = (LinearLayout) findViewById(R.id.LinearLayout1);

        TableRow tr;
        TextView name, printLong, printLat, id;
        String pN, pLo, pLa;
        JSONObject currentResult, geometry, location;
        JSONArray results;

        @Override
        protected JSONObject doInBackground(String... urls) {
            Log.d(getClass().getSimpleName(), "Starting parsing");
            JSONDownloader jsonGet = new JSONDownloader();
               // str = loadFromNetwork(urls[0]);
                return jsonGet.getJSONFromUrl(urls[0]);
        }
        protected void onPostExecute(JSONObject json){
            //retrieve main json object
            try {
                results = json.getJSONArray("results");
                Log.d(getClass().getSimpleName(), "made it to parsing");
                for (int i = 0; i < results.length(); i++) {
                    Log.d(getClass().getSimpleName(), "parsing" + i);

                    currentResult = results.getJSONObject(i);
                    Log.d(getClass().getSimpleName(), "got Current Result");

                    geometry = currentResult.getJSONObject("geometry");

                    Log.d(getClass().getSimpleName(), "got Geometry");

                    location = geometry.getJSONObject("location");

                    Log.d(getClass().getSimpleName(), "got location");
                    try {
                        pN = currentResult.getString("name");
                        pLo = location.getString("lng");
                        pLa = location.getString("lat");

                        Log.d(getClass().getSimpleName(), "1");
                        tr = new TableRow(MainActivity.this);
                        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        name = new TextView(MainActivity.this);
                        name.setText(pN);
                        name.setLayoutParams(new TableRow.LayoutParams(pix1, TableRow.LayoutParams.WRAP_CONTENT, 1));
                        ((TableRow) tr).addView(name);
                        Log.d(getClass().getSimpleName(), "2");
                        printLong = new TextView(MainActivity.this);
                        printLong.setText(pLo);
                        printLong.setLayoutParams(new TableRow.LayoutParams(pix2, TableRow.LayoutParams.WRAP_CONTENT, 1));
                        ((TableRow) tr).addView(printLong);
                        Log.d(getClass().getSimpleName(), "3");
                        printLat = new TextView(MainActivity.this);
                        printLat.setText(pLa);
                        printLat.setLayoutParams(new TableRow.LayoutParams(pix2, TableRow.LayoutParams.WRAP_CONTENT, 1));
                        ((TableRow) tr).addView(printLat);
                        Log.d(getClass().getSimpleName(), "4");
                        id = new TextView(MainActivity.this);
                        id.setText(String.valueOf(i));
                        id.setLayoutParams(new TableRow.LayoutParams(pix3, TableRow.LayoutParams.WRAP_CONTENT, 1));
                        Log.d(getClass().getSimpleName(), "5");
                        ((TableRow) tr).addView(id);

                        Log.d(getClass().getSimpleName(), "Adding Layout");
                        ((LinearLayout) poiTable).addView(tr);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class JSONDownloader {
        static InputStream is = null;
        static JSONObject jObj = null;
        static String json = "";
        // constructor
        public JSONDownloader() {
        }
        public JSONObject getJSONFromUrl(String url) {
            // Making HTTP request
            try {
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            // try parse the string to a JSON object
            try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            // return JSON String
            return jObj;
        }
    }
}