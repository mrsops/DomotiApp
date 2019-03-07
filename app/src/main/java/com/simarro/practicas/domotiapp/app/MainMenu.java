package com.simarro.practicas.domotiapp.app;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.simarro.practicas.domotiapp.R;
import com.simarro.practicas.domotiapp.fragments.FragmentLights;
import com.simarro.practicas.domotiapp.fragments.FragmentPower;
import com.simarro.practicas.domotiapp.fragments.FragmentSetup;
import com.simarro.practicas.domotiapp.fragments.FragmentShare;
import com.simarro.practicas.domotiapp.fragments.FragmentTemperature;
import com.simarro.practicas.domotiapp.fragments.FragmentWindows;
import com.simarro.practicas.domotiapp.pojos.Temperature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static Configuration config;
    private static Locale locale;
    private SearchDatabase searchDatabase;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Locale locale;
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (sharedPreferences.getString("language","").equals("es")){
                    Toast.makeText(MainMenu.this, "Español", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainMenu.this, "Ingles", Toast.LENGTH_SHORT).show();
                }
            }
        };


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.config = new Configuration();


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
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
        FragmentManager fragmentManager= getSupportFragmentManager();

        if (id == R.id.nav_temperature) {
            fragmentManager.beginTransaction().replace(R.id.container, new FragmentTemperature()).commit();

        } else if (id == R.id.nav_lights) {

            fragmentManager.beginTransaction().replace(R.id.container, new FragmentLights()).commit();
        } else if (id == R.id.nav_power) {
            fragmentManager.beginTransaction().replace(R.id.container, new FragmentPower()).commit();
        } else if (id == R.id.nav_windows) {

            fragmentManager.beginTransaction().replace(R.id.container, new FragmentWindows()).commit();
        } else if (id == R.id.nav_alarm) {
            searchDatabase = new SearchDatabase();
            searchDatabase.execute((Void) null);
            //fragmentManager.beginTransaction().replace(R.id.container, new FragmentAlarm()).commit();
        } else if (id == R.id.nav_setup) {
            Intent i = new Intent(this, FragmentSetup.class);
            startActivity(i);
            //fragmentManager.beginTransaction().replace(R.id.container, null).commit();
            //getFragmentManager().beginTransaction().replace(R.id.container, new FragmentSetup()).commit();
        } else if (id == R.id.nav_share) {
            fragmentManager.beginTransaction().replace(R.id.container, new FragmentShare()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private static boolean updateResources (Context context, String laguage){
        Locale locale = new Locale(laguage);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale=locale;
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        return true;
    }

    private void getTemperaturesByDay(){
        RequestQueue requestQueue;
        ArrayList<Temperature> temperatures;
        //JsonArrayRequest jsonUsuarios = new JsonArrayRequest("http://localhost/domotiapp/vertemperaturas.php?dia="+day+"&month="+month, new Response.Listener<JSONArray>() {
        JsonArrayRequest jsonUsuarios = new JsonArrayRequest("http://localhost/domotiapp/temperatures.php", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject=null;

                for (int i=0; i<response.length();i++){
                    try {
                        jsonObject = response.getJSONObject(i);
                        int nregistry = jsonObject.getInt("nregistry");
                        String date = jsonObject.getString("date");
                        double temperature = jsonObject.getDouble("temperature");

                        Log.i("DomotiApp", "Num registro: " + nregistry);
                        Log.i("DomotiApp", "date: " + date);
                        Log.i("DomotiApp", "temperature: "+temperature );

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainMenu.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonUsuarios);
    }

    public class SearchDatabase extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                getTemperaturesByDay();
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
