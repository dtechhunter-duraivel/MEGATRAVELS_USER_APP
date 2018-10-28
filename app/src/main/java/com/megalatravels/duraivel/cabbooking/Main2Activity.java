package com.megalatravels.duraivel.cabbooking;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener {


    GoogleMap map;
    LocationManager locationManager;
    GoogleApiClient googleApiClient;
    double lat;
    double lan;
    private RequestQueue mQueue,SQueue;
    double pickuplat;
    double pickuplon;
    double dist;
    SharedPreferences pref;
String picloc;
    double clat;
    double clon;
    String dista="1 km";
    String pos;
    String categ;
    int mincost;
    int km;
    int costkm;
    Button continu;

    PlaceAutocompleteFragment autocompleteFragment;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mQueue = Volley.newRequestQueue(this);
        SQueue = Volley.newRequestQueue(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView  name = (TextView)header.findViewById(R.id.uide);
        TextView  mob = (TextView)header.findViewById(R.id.nume);
        View view = getLayoutInflater().inflate(R.layout.actionlayout, null);
        android.support.v7.app.ActionBar.LayoutParams params = new android.support.v7.app.ActionBar.LayoutParams(
                android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT,
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView Title = (TextView) view.findViewById(R.id.actionbar_title);
        Title.setText("END YOUR TRIP");
        getSupportActionBar().setCustomView(view,params);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false); getTarrif();
        continu =(Button)findViewById(R.id.n1);
        pref = getApplicationContext().getSharedPreferences("sma", Context.MODE_MULTI_PROCESS); // 0 - for private mode



        if (pref.contains("fne") && pref.contains("unum"))
        {
            String na = pref.getString("fne", null).toString();
            String mo = pref.getString("unum", null).toString();
            name.setText(na);
            mob.setText(mo);
            //Toast.makeText(getApplicationContext(),pref.getString("fne",null).toString(),Toast.LENGTH_SHORT).show();
        }

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("IN")
                .build();
        continu.setVisibility(View.GONE);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        LatLng southwestLatLng= new LatLng(10.736344,78.615761);
        LatLng northeastLatLng= new LatLng(10.895345,78.762295);
        autocompleteFragment.setBoundsBias(new LatLngBounds(southwestLatLng, northeastLatLng));
        autocompleteFragment.setFilter(autocompleteFilter);
        //((ImageButton)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setBackgroundResource(R.drawable.loc);
        ((ImageButton)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(15);
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("Drop Location");
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setBackgroundResource(R.drawable.searchb);
        //((ImageButton)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        pickuplat = Double.parseDouble(extras.getString("Latitude"));
        pickuplon = Double.parseDouble(extras.getString("Longitude"));
        picloc = extras.getString("pickloc");
        pos =extras.getString("pos");
        categ=extras.getString("categ");
      //  Toast.makeText(getApplicationContext(),pos,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),categ,Toast.LENGTH_SHORT).show();
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                //Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_LONG).show();
                rgeocode(String.valueOf(place.getAddress()));

                continu.setVisibility(View.VISIBLE);
               // Toast.makeText(getApplicationContext(),String.valueOf(lat)+"&"+String.valueOf(lan),Toast.LENGTH_LONG).show();
                clat=place.getLatLng().latitude;
                clon=place.getLatLng().longitude;
                LatLng currentLocation = new LatLng(clat, clon);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(place.getLatLng());
                markerOptions.title("Pickup Point");
                map.clear();
                map.addMarker(markerOptions);

                LatLng prevloc = new LatLng(pickuplat, pickuplon);
               // String distan=getDistanceInfo(prevloc.latitude,prevloc.longitude,currentLocation.latitude,currentLocation.longitude);
            //    Toast.makeText(getApplicationContext(),String.valueOf(distr),Toast.LENGTH_SHORT).show();
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 14.0f));
               continu.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       String distan=getDistanceInfo(picloc,place.getAddress().toString());



                   }
               });

           //  snackbar.show();
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
         //
            //  Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1,1, this);

        if(pref.contains("fne")&&pref.contains("unum"))
        {
            String na=pref.getString("fne",null).toString();
            String mo=pref.getString("unum",null).toString();
            name.setText(na);
            mob.setText(mo);
            //Toast.makeText(getApplicationContext(),pref.getString("fne",null).toString(),Toast.LENGTH_SHORT).show();
        }

    }

void calculateCost(String pos,String Cat)
{

}


    void rgeocode(String loc)
    {
        Geocoder geocoder;
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
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
       LatLng currentLocation = new LatLng(10.7905, 78.7047);
        LatLng endLocation = new LatLng(10.7905, 78.7047);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(endLocation);
        markerOptions.title("Tiruchirappalli,Tamilnadu,India.");
        map.addMarker(markerOptions);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(endLocation.latitude, endLocation.longitude), 14.0f));
       //Toast.makeText(getApplicationContext(), String.valueOf(SphericalUtil.computeDistanceBetween(currentLocation, endLocation)),Toast.LENGTH_LONG).show();
       // double distance;

    }




    void setmyloc(double lati,double longi)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lati, longi, 5); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            // Toast.makeText(getApplicationContext(),city,Toast.LENGTH_SHORT).show();
           // ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setText(address);

        }
        catch ( Exception e)
        {
          //  Toast.makeText(getApplicationContext(),String.valueOf(e.getMessage()),Toast.LENGTH_SHORT).show();


        }
    }




    private String getDistanceInfo(final String picking, final String droping) {
        StringBuilder stringBuilder = new StringBuilder();
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+picking.replaceAll(" ","%20")+"&destinations="+droping.replaceAll(" ","%20")+"&key=AIzaSyAmkb3OfGp_AYsdEVLnSvy_3ivO1aNC8Ew";
        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = response.getJSONArray("rows"); //To get the inventory as an array
                    JSONObject routes = jsonArray.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("elements");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    Log.i("Distance", distance.toString());

                    dista =distance.getString("text");
              //      Toast.makeText(getApplicationContext(),dista,Toast.LENGTH_SHORT).show();
                    Intent inten = new Intent(Main2Activity.this,FirstActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("pos",pos);
                    extras.putString("cate",categ);

                    extras.putString("distance",dista);
                    extras.putString("picklocation",picking);
                    extras.putString("droplocation",droping);

                    //Toast.makeText(getApplicationContext(),String.valueOf(costkm),Toast.LENGTH_SHORT).show();
                    inten.putExtras(extras);
                    startActivity(inten);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                } catch (JSONException e) {

                    e.printStackTrace();
                  //  Toast.makeText(getApplicationContext(),String.valueOf(e.getMessage()),Toast.LENGTH_SHORT).show();

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
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
             //   Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

            }
        });

        mQueue.add(request);
        return dista;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_rigt);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }


    @Override
    public void onLocationChanged(Location location)
    {
        setmyloc(location.getLatitude(),location.getLongitude());
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17.));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        if (id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logouting) {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            Intent i =new Intent(this,Startpage.class);
            startActivity(i);
            this.finishAffinity();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_rigt);

        }
        else if (id == R.id.nav_manage)
        {
            Intent i = new Intent(this, ComplaintActivity.class);
            //Toast.makeText(getApplicationContext(),",Toast.LENGTH_SHORT).show();
            startActivity(i);
            this.finishAffinity();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_rigt);
        }
        else if(id==R.id.histo)
        {
            Intent is =new Intent(Main2Activity.this,HistoryCancel.class);
            startActivity(is);

            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_rigt);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getTarrif() {

        String url = "http://***************/mobile/api/tariffList";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("TariffData"); //To get the inventory as an array

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject car= jsonArray.getJSONObject(i);

                            }


                        } catch (JSONException e) {
                        //    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
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


             //   Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }
        });

        SQueue.add(request);
    }


}