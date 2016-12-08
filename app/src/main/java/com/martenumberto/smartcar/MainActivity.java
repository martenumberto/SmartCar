package com.martenumberto.smartcar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String tag = "MainActivity";
    final int SPEECHINTENT_REQ_CODE = 11833;
    TextToSpeech sprecher;

    //TODO: Implement Spotify Function
    final String SpotifySearch = "https://api.spotify.com/v1/search?type=artist&limit=1";

    //public View view = this;
    public Context context;

    setState set = new setState();

    Boolean speechIsRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        speechIsRunning = false;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (speechIsRunning == false) {
                    Snackbar.make(view, "Sprachsteuerung gestartet", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    speechIsRunning = true;
                } else {
                    Snackbar.make(view, "Sprachsteuerung beendet", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    speechIsRunning = false;
                }

                Intent speechRecognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechRecognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());
                startActivityForResult(speechRecognitionIntent, SPEECHINTENT_REQ_CODE);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Setzte das Seitliche Menü auf Start
        navigationView.setCheckedItem(R.id.nav_start);

        //Intialisiere den FragmentManager und zeige StartFragment an
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_main, Fragment.instantiate(this, StartFragment.class.getName()));
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();

        sprecher = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    sprecher.setLanguage(Locale.GERMANY);
                }
            }
        });

        context = this;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (id == R.id.nav_start) {
            ft.replace(R.id.content_main, Fragment.instantiate(this, StartFragment.class.getName())).commit();
            navigationView.setCheckedItem(R.id.nav_start);
        } else if (id == R.id.nav_air) {
            ft.replace(R.id.content_main, Fragment.instantiate(this, AirFragment.class.getName())).commit();
            navigationView.setCheckedItem(R.id.nav_air);
        } else if (id == R.id.nav_light) {

        } else if (id == R.id.nav_music) {

        } else if (id == R.id.nav_tv) {
            ft.replace(R.id.content_main, Fragment.instantiate(this, tvFragment.class.getName())).commit();
            navigationView.setCheckedItem(R.id.nav_tv);
        } else if (id == R.id.nav_info) {

        } else if (id == R.id.nav_settings) {
            new getState(this, getCurrentFocus()).execute("UMLuft");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO Extend the Voice Recognitation

        String[] greetingArray = {"Moin Typ!", "Moin Marten!", "Was geht ab?", "Gib gas!"};
        String greeting = greetingArray[new Random().nextInt(greetingArray.length)];

        if (requestCode == SPEECHINTENT_REQ_CODE && resultCode == RESULT_OK) {
            ArrayList<String> speechResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String input = speechResults.get(0);

            Log.d(tag, "Spracheingabe: " + input);

            if (input.contains("Hallo Auto")) {

                if (input.contains("Temperatur")) {

                    if (input.contains("Rechts")) {

                    } else if (input.contains("Links")) {

                    }

                }

                if (input.contains("Klima")) {
                    if (input.contains("an")) {
                        set.setState("ECSwitch", "on");
                        sprecher.speak("Ok, Ich habe die Klimaanlage eingeschaltet", TextToSpeech.QUEUE_FLUSH, null);
                        new getState(this, getCurrentFocus()).execute("ECSwitch");
                    }
                    if (input.contains("aus")) {
                        set.setState("ECSwitch", "off");
                        sprecher.speak("Ok, Ich habe die Klimaanlage ausgeschaltet", TextToSpeech.QUEUE_FLUSH, null);
                        new getState(this, getCurrentFocus()).execute("ECSwitch");
                    }
                } else {
                    Toast.makeText(this, greeting, Toast.LENGTH_LONG).show();
                    sprecher.speak(greeting, TextToSpeech.QUEUE_FLUSH, null);
                }

            }

        }
    }


}
