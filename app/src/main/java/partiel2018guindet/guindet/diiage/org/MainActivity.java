package partiel2018guindet.guindet.diiage.org;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import partiel2018guindet.guindet.diiage.org.Model.Release;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper sqlDb = new DbHelper(this);
        SQLiteDatabase db = sqlDb.getWritableDatabase();


        String baseUrlApi = "https://my-json-server.typicode.com/lpotherat/discogs-data/releases";
        URL baseUrl = null;
        try {
            baseUrl = new URL(baseUrlApi);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        @SuppressLint("StaticFieldLeak")
        final AsyncTask<URL, Integer, ArrayList<Release>> at = new AsyncTask<URL, Integer, ArrayList<Release>>()
        {
            @Override
            protected ArrayList<Release> doInBackground(URL... urls)
            {
                ArrayList<Release> releases = new ArrayList<>();
                try
                {
                    InputStream inputStream = urls[0].openStream();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder stringBuilder = new StringBuilder();
                    String lineBuffer = null;

                    while ((lineBuffer = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(lineBuffer);
                    }

                    String data = stringBuilder.toString();

                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0 ; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        releases.add(Release.build(jsonObject));
                    }

                }
                catch (Exception e)
                {
                    //e.printStackTrace();
                    Log.e("EXCEPTION",e.getLocalizedMessage());
                }

                return releases;
            }

            @Override
            protected void onPostExecute(ArrayList<Release> releases) {
                super.onPostExecute(releases);

                if (releases.size() != 0)
                {
                    ListView lst = findViewById(R.id.lstRealeases);

                    ArrayList<String> lstS = new ArrayList<>();


                    for (int i = 0; i < releases.size()-1; i++)
                    {
                        if (releases.get(i).getTitle() != null)
                        {
                            lstS.add(releases.get(i).getTitle());
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,lstS);

                    lst.setAdapter(adapter);
                }
            }
        };

        at.execute(baseUrl);
    }


}
