package apps.nocturnuslabs.pestcontrol;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class MainActivity extends Activity {
    private EditText locationText;
    private Button weatherBtn;
    public TextView showData;
    static JSONObject data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationText = findViewById(R.id.main_locationText);
        weatherBtn = findViewById(R.id.main_weatherbtn);
        showData = findViewById(R.id.main_showData);

        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJSON(locationText.getText().toString());
            }
        });
    }

    public void getJSON(final String city) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=609306c389d2ab7bd4d3159c99186cae");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";
                    while ((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();
                    data = new JSONObject(json.toString());
                    if (data.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("Exception " + e.getMessage());
                    return null;
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void Void) {
                if (data != null) {
                    try {
                        showData.setText(formatJSON(data));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    private String formatJSON(JSONObject data) throws JSONException {
        String result = "Latitude is: "+data.getJSONObject("coord").getString("lat");
        result+="\n Min Temp is: "+data.getJSONObject("main").getString("temp_min");
        return result;
    }
}
