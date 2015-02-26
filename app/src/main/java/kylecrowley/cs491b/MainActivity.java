package kylecrowley.cs491b;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends ActionBarActivity {
    double longitude = 0.0;
    double latitude = 0.0;
    double radius = 100.0;

    private String APIKey="AIzaSyCeoJkIK5Jgv3pWYQ0VSUy41QFEDE29PME";
    private TextView tv;
    private EditText longi;
    private EditText lati;
    private EditText radi;

    //http vars
    private TextView resultText;
    private String baseUrl = "https://maps.googleapis.com/maps/api/place/search/json?";
    private String finalUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode();
        StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        longi = (EditText)findViewById(R.id.editTextLong);
        lati = (EditText)findViewById(R.id.editTextLat);
        radi = (EditText)findViewById(R.id.editTextRadius);
        tv = (TextView) findViewById(R.id.saved_location);
        resultText = (TextView) findViewById(R.id.resultsDisplay);
    }

    public void buttonGet(View view) {
        if( longi.getText().toString().length() > 0 ) {
            longitude = Double.valueOf(longi.getText().toString());
        }
        else{ longitude = 100.0;}
        if( lati.getText().toString().length() > 0 ) {
            latitude = Double.valueOf(lati.getText().toString());
        }
        else{latitude = 18.0;}
        if( radi.getText().toString().length() > 0 ) {
            radius = Double.valueOf(radi.getText().toString());
        }
        else{radius = 500;}

        tv.setText("Location: " + "\n" +
                    latitude + "\n" +
                    longitude + "\n" +
                    radius);
    }
/*
    public void buttonGetPhone(View view){

    }
*/

    public void buttonSearch(View view) {
        finalUrl = baseUrl + "location=" + latitude + "," + longitude + "&radius=" + radius + "&key=" + APIKey;
        // https://maps.googleapis.com/maps/api/place/search/json?location=18.00,100.00&radius=5000&key=AIzaSyCeoJkIK5Jgv3pWYQ0VSUy41QFEDE29PME
        new LongOperation().execute("");
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params){
            GetMethodEx test = new GetMethodEx();
            String returned = null;

            try{
                returned = test.getInternetData();
            } catch(Exception e){
                e.printStackTrace();
            }
            return returned;
        }
        protected void onPostExecute(String result){
            resultText.setText(result);
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}