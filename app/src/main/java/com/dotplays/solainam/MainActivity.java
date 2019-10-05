package com.dotplays.solainam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

// nhanh cua Van
public class MainActivity extends AppCompatActivity {

    private ArrayList<TinTuc> tinTucs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tinTucs = new ArrayList<>();


        String abc = " Hello ";
        String b  = "ABC";

    }

    public void getData(View view) {

        Log.e("ACC", "AAA");
        String url = "https://vietnamnet.vn/rss/oto-xe-may.rss";
        GetData getData = new GetData();
        getData.execute(url);

    }


    class GetData extends AsyncTask<String, Long, ArrayList<TinTuc>> {


        // xu ly trong luong
        @Override
        protected ArrayList<TinTuc> doInBackground(String... strings) {

            String link = strings[0];
            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection =
                        (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();

                // khoi tao doi tuong parse XML

                XmlPullParserFactory xmlPullParserFactory =
                        XmlPullParserFactory.newInstance();

                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();

                xmlPullParser.setInput(inputStream, "utf-8");


                int eventType = xmlPullParser.getEventType();
                TinTuc tinTuc = null;
                String text = "";

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    eventType = xmlPullParser.getEventType();
                    String tag = xmlPullParser.getName();
                    if (tag != null)
                        Log.e("tag", tag);

                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            if (tag.equalsIgnoreCase("item")) {
                                tinTuc = new TinTuc();
                            }
                            break;
                        case XmlPullParser.TEXT:
                            text = xmlPullParser.getText();
                            Log.e("text", "text : " + text);
                            break;

                        case XmlPullParser.END_TAG:
                            if (tinTuc != null) {
                                if (tag.equalsIgnoreCase("title")) {
                                    tinTuc.title = text;
                                } else if (tag.equalsIgnoreCase("description")) {
                                    tinTuc.description = text;
                                } else if (tag.equalsIgnoreCase("pubDate")) {
                                    tinTuc.pubDate = text;
                                } else if (tag.equalsIgnoreCase("image")) {
                                    tinTuc.image = text;
                                } else if (tag.equalsIgnoreCase("item")) {
                                    tinTucs.add(tinTuc);
                                }
                            }
                            break;
                    }
                    xmlPullParser.next();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("111111", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("222222", e.getMessage());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                Log.e("333333", e.getMessage());
            }
            return tinTucs;
        }


        // sau khi ket thuc luong va can tuong taac voi giao dien
        @Override
        protected void onPostExecute(ArrayList<TinTuc> tinTucs) {
            super.onPostExecute(tinTucs);
            Toast.makeText(MainActivity.this, tinTucs.size() + "",
                    Toast.LENGTH_SHORT).show();

        }
    }


}
