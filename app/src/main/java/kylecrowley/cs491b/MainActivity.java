package kylecrowley.cs491b;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BufferedHeader;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {
    int longitude, latitude, radius;
    String requesturl;
    HttpGet req;
    HttpResponse res;
    JSONObject jsonobj;
    JSONArray resarray;
    DefaultHttpClient client;
    HttpEntity jsonentity;
    InputStream in;

    private String APIKey="AIzaSyCeoJkIK5Jgv3pWYQ0VSUy41QFEDE29PME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requesturl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + 18.0000 + "," + 100.0000 + "&radius=" + radius + "&key=" + APIKey;
        System.out.println("Request "+requesturl);
        client=new DefaultHttpClient();
        System.out.println("hello");

        req=new HttpGet(requesturl);
        System.out.println("hello");

        try {
            res=client.execute(req);
            StatusLine status = res.getStatusLine();
            int code = status.getStatusCode();
            System.out.println(code);
            if(code!=200)
            {
                System.out.println("Request Has not succeeded");
                finish();
            }

            jsonentity=res.getEntity();
            in = jsonentity.getContent();

            jsonobj=new JSONObject(convertToString(in));


            resarray = jsonobj.getJSONArray("results");

            if(resarray.length()==0){
            }
            else{
                int len=resarray.length();
                for(int j=0;j<len;j++)
                {
                    Toast.makeText(getApplicationContext(), resarray.getJSONObject(j).getString("name") , Toast.LENGTH_LONG).show();
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void buttonSearch(View view) {
        Intent intent = new Intent(this, Results.class);
    }

    private String convertToString(InputStream x)   {
        BufferedReader br=new BufferedReader(new InputStreamReader(x));
        StringBuilder jsonstr=new StringBuilder();
        String line;
        try{
            while((line=br.readLine())!=null)
            {
                String t=line+"\n";
                jsonstr.append(t);
            }
            br.close();
        }catch (IOException e)  {
            e.printStackTrace();
        }
        return jsonstr.toString();
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
