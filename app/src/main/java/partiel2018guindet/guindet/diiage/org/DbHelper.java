package partiel2018guindet.guindet.diiage.org;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import partiel2018guindet.guindet.diiage.org.Model.Release;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "db_api", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //First Version
        db.execSQL("CREATE TABLE `release` ( `status` TEXT, `thumb` TEXT, `format` TEXT, `title` TEXT, `catno` TEXT, `year` INTEGER, `resource_url` TEXT, `artist` TEXT, `id` INTEGER, PRIMARY KEY(`id`) )");
        loadAndSaveData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion < 2 && newVersion <= 2)
        {
            versionTwo(db);
        }
    }

    private void versionTwo(SQLiteDatabase db)
    {
        //Create Table Artist
        db.execSQL("CREATE TABLE `artist` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` TEXT )");

        //Add current artist from table release
        Cursor cursor = db.query("release",new String[] {"id","artist"},"",new String[] {},null,null,null);
        while(cursor.moveToNext())
        {
            String artistName = cursor.getString(1);
            int cnt = db.query("artist",new String[] {"*"},"name=?",new String[] {artistName},null,null,null).getCount();
            if (cnt == 0)
            {
                ContentValues cv = new ContentValues();

                cv.put("name",artistName);

                db.insert("artist",null,cv);
            }
        }

    }

    private void loadAndSaveData(SQLiteDatabase db)
    {
        final SQLiteDatabase myDB = db;
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
                    for (int i = 0; i< releases.size();i++)
                    {
                        Release r = releases.get(i);
                        ContentValues cv = new ContentValues();
                        cv.put("status",r.getStatus());
                        cv.put("thumb",r.getThumb());
                        cv.put("format",r.getFormat());
                        cv.put("title",r.getTitle());
                        cv.put("catno",r.getCatno());
                        cv.put("year",r.getYear());
                        cv.put("resource_url",r.getResourceUrl());
                        cv.put("artist",r.getArtist());
                        cv.put("id",r.getId());

                        //Log.e("EXCEPTION","Insertion "+i + " - " + r.getId());
                        myDB.insert("release",null,cv);
                    }

                    //Second Version
                    versionTwo(myDB);
                }
            }
        };

        at.execute(baseUrl);
    }
}
