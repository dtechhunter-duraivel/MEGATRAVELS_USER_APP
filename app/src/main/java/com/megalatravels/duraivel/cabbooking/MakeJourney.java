package com.megalatravels.duraivel.cabbooking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MakeJourney extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback, LocationListener {
GoogleMap map;
    LocationManager locationManager;
    GoogleApiClient googleApiClient;
    double lat;
    double lan;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String ipos;
    SharedPreferences pref;
    String categ;
    String un, ps;
    TextView t1;
    TextView userid;
    TextView mobi;
    String a;
    RequestQueue SQueue;
    Button cone, logout;
    PlaceAutocompleteFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_journey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SQueue = Volley.newRequestQueue(this);
        View view = getLayoutInflater().inflate(R.layout.actionlayout, null);
        android.support.v7.app.ActionBar.LayoutParams params = new android.support.v7.app.ActionBar.LayoutParams(
                android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView Title = (TextView) view.findViewById(R.id.actionbar_title);
        Title.setText("START YOUR TRIP");
        getSupportActionBar().setCustomView(view, params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        pref = getApplicationContext().getSharedPreferences("sma", Context.MODE_MULTI_PROCESS); // 0 - for private mode
        View header = navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView name = (TextView) header.findViewById(R.id.uid);
        TextView mob = (TextView) header.findViewById(R.id.num);

        if (pref.contains("fne") && pref.contains("unum")) {
            String na = pref.getString("fne", null).toString();
            String mo = pref.getString("unum", null).toString();
            name.setText(na);
            mob.setText(mo);
            //Toast.makeText(getApplicationContext(),pref.getString("fne",null).toString(),Toast.LENGTH_SHORT).show();
        }

        if (pref.contains("username") && pref.contains("password")) {
            un = pref.getString("username", null).toString();
            ps = pref.getString("password", null).toString();
            //  Toast.makeText(getApplicationContext(),un+ps,Toast.LENGTH_SHORT).show();
        }


        cone = (Button) findViewById(R.id.n1);

        ipos = "0";
        categ = "0";

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("IN")
                .build();
        cone.setVisibility(View.GONE);
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        LatLng southwestLatLng = new LatLng(10.736344, 78.615761);
        LatLng northeastLatLng = new LatLng(10.895345, 78.762295);
        autocompleteFragment.setBoundsBias(new LatLngBounds(southwestLatLng, northeastLatLng));
        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(15);
        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("Pick Up Location");
        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setBackgroundResource(R.drawable.searchb);
        ((ImageButton) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);
        autocompleteFragment.setFilter(autocompleteFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(final Place place)
            {
                // Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_LONG).show();
                rgeocode(place.getLatLng());
                cone.setVisibility(View.VISIBLE);
                // Toast.makeText(getApplicationContext(),String.valueOf(lat)+String.valueOf(lan),Toast.LENGTH_LONG).show();
                LatLng currentLocation = new LatLng(lat, lan);
                final double clat = place.getLatLng().latitude;
                final double clon = place.getLatLng().longitude;
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(place.getLatLng());
                markerOptions.title("Pickup Point");
                map.clear();
                map.addMarker(markerOptions);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 14.0f));

                cone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MakeJourney.this, Main2Activity.class);
                        Bundle extras = new Bundle();
                        extras.putString("Latitude", String.valueOf(clat));
                        extras.putString("Longitude", String.valueOf(clon));
                        extras.putString("pos", ipos);
                        extras.putString("categ", categ);
                        extras.putString("pickloc", place.getAddress().toString());
                        i.putExtras(extras);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }
                });



                //        snackbar.show();

            }

            @Override
            public void onError(Status status) {

            }
        });
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //  Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);



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
        getMenuInflater().inflate(R.menu.make_journey, menu);
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


            if (id == android.R.id.home) {
                onBackPressed();  return true;
            }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logouting){
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            Intent i = new Intent(this, Startpage.class);
            Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_SHORT).show();
            startActivity(i);
            this.finishAffinity();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_rigt);
            // Handle the camera action
        }
        else if (id == R.id.comp)
        {
            Intent i = new Intent(this, ComplaintActivity.class);
            //Toast.makeText(getApplicationContext(),",Toast.LENGTH_SHORT).show();
            startActivity(i);
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_rigt);

        }
        else if(id==R.id.hist)
        {
            Intent is =new Intent(MakeJourney.this,HistoryCancel.class);
            startActivity(is);
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_rigt);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        LatLng currentLocation = new LatLng(10.7905, 78.7047);
        LatLng endLocation = new LatLng(10.7905, 78.7047);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(endLocation);
        markerOptions.title("Tiruchirappalli,Tamilnadu,India.");
        map.addMarker(markerOptions);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(endLocation.latitude, endLocation.longitude), 14.0f));
        float[] results = new float[1];
    }
    void rgeocode(LatLng l) {
        lat = l.latitude;
        lan = l.longitude;
        //Toast.makeText(getApplicationContext(),String.valueOf(lat),Toast.LENGTH_SHORT);
      /*  Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            String locationName =loc;
            List<Address> addressList = geocoder.getFromLocationName(locationName, 5);
            if (addressList != null && addressList.size() > 0) {
                lat = (double) (addressList.get(0).getLatitude());
                lan = (double) (addressList.get(0).getLongitude());
                 }
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }


    private void getUser() {

        String url = "http://***************/Mobile/api/Account/UserInfo";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("TariffData"); //To get the inventory as an array

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject car = jsonArray.getJSONObject(i);

                            }


                        } catch (JSONException e) {
                            // Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }


                // Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }
        });

        SQueue.add(request);
    }

}
