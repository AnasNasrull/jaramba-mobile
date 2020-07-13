package com.example.jarambamobile;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarambamobile.adapter.Constants;
import com.example.jarambamobile.adapter.FetchAddress;
import com.example.jarambamobile.adapter.PlaceAutoSuggestionAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class TripUser extends FragmentActivity implements OnMapReadyCallback {
    AutoCompleteTextView start_point, destination_point;
    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 2000;
    private ResultReceiver startPointReceiver;
    private ResultReceiver destinationPointReceiver;
    private Double startLat,startLong,destinationLat,destinationLong;

    private Double totalDistance = 0.0;

    ArrayList<LatLng> listPoints;
    Button btn_go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_user);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();
        btn_go = findViewById(R.id.btn_go);

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(start_point.getText().toString())) {
                    Toast.makeText(TripUser.this, "Silahkan tentukan Start Point", Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(destination_point.getText().toString())) {
                    Toast.makeText(TripUser.this, "Silahkan tentukan Destination Point", Toast.LENGTH_SHORT).show();
                }

                if((!TextUtils.isEmpty(destination_point.getText().toString()))&&(!TextUtils.isEmpty(start_point.getText().toString()))){
                    startLat = listPoints.get(0).latitude;
                    startLong = listPoints.get(0).longitude;
                    destinationLat = listPoints.get(1).latitude;
                    destinationLong = listPoints.get(1).longitude;

                    Intent intent = new Intent(getApplicationContext(),DamriStartTrip.class);
                    intent.putExtra("start_address", start_point.getText().toString());
                    intent.putExtra("destination_address", destination_point.getText().toString());
                    intent.putExtra("start_lati", startLat.toString());
                    intent.putExtra("start_long", startLong.toString());
                    intent.putExtra("destination_lati", destinationLat.toString());
                    intent.putExtra("destination_long", destinationLong.toString());
                    intent.putExtra("From", "Trip User");
                    intent.putExtra("Jarak", totalDistance.toString());
                    startActivity(intent);
                }
            }
        });

        start_point = findViewById(R.id.start_point);
        destination_point = findViewById(R.id.destination_point);

        start_point.setAdapter(new PlaceAutoSuggestionAdapter(TripUser.this, android.R.layout.simple_list_item_1));
        destination_point.setAdapter(new PlaceAutoSuggestionAdapter(TripUser.this, android.R.layout.simple_list_item_1));

        startPointReceiver = new StartPointReceiver(new Handler());
        destinationPointReceiver = new DestinationPointReceiver(new Handler());

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Reset marker apabila sudah terdapat 2
                if (listPoints.size() == 2) {
                    listPoints.clear();
                    mMap.clear();
                }
                //Save marker start
                listPoints.add(latLng);

                //Inisialisasi marker
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                if (listPoints.size() == 1) {
                    //Menambahkan marker pertama ke map
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    Location location = new Location("provideNA");
                    location.setLatitude(listPoints.get(0).latitude);
                    location.setLongitude(listPoints.get(0).longitude);
                    fetchStartAddress(location);
                    markerOptions.title(start_point.getText().toString());

                } else {
                    //Menambahkan marker kedua ke map
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    Location location = new Location("provideNA");
                    location.setLatitude(listPoints.get(1).latitude);
                    location.setLongitude(listPoints.get(1).longitude);
                    fetchDestinationAddress(location);
                    totalDistance = distances(listPoints.get(0).latitude,listPoints.get(0).longitude,listPoints.get(1).latitude,listPoints.get(1).longitude);
                    markerOptions.title(String.format(Locale.US, " Jarak %2f Km", totalDistance));


                }
                mMap.addMarker(markerOptions);

                if (listPoints.size() == 2) {
                    //Membuat url dari marker start ke marker destination
                    String url = getRequestUrl(listPoints.get(0), listPoints.get(1));
                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                    taskRequestDirections.execute(url);

                    Log.v("Distance",String.format(Locale.US, "%2f Kilometers", distances(listPoints.get(0).latitude,listPoints.get(0).longitude,listPoints.get(1).latitude,listPoints.get(1).longitude)));
                }
            }
        });

    }

    private double distances(double latStart, double longStart, double latDest, double longDest){
        //Menghitung selisih longitude
        double longDiff = longStart - longDest;
        //Menghitung jarak
        double distance = Math.sin(deg2rad(latStart))
                * Math.sin(deg2rad(latDest))
                + Math.cos(deg2rad(latStart))
                * Math.cos(deg2rad(latDest))
                * Math.cos(deg2rad(longDiff));
        distance = Math.acos(distance);
        
        //Convert distance radian ke degree
        distance = rad2deg(distance);

        //Distance dalam miles
        distance = distance * 60 * 1.1515;

        //Distance dalam kilomaters
        distance = distance * 1.609344;

        return distance;
    }

    //Convert radian ke degree
    private double rad2deg(double distance) {
        return (distance * 180.0 / Math.PI);
    }

    //Convert degree ke radian
    private double deg2rad(double latStart) {
        return (latStart * Math.PI/180.0);
    }

    private String getRequestUrl(LatLng origin, LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude +","+origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude+","+dest.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=driving";
        // API Key
        String api_key = "key=AIzaSyAOz14F3XcQn-mFOWRX-D7WhPfiCTjs744";
        // Build the full param
        String param = str_org +"&" + str_dest + "&" +sensor+"&" +mode+ "&" +api_key;
        //Output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    @SuppressLint("Missing Permission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Membuat list route dan menampilkan ke map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(5);
                polylineOptions.color(Color.rgb(234, 96, 78));
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Rute tidak ditemukan", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void fetchStartAddress(Location location){
        Intent intent = new Intent(this, FetchAddress.class);
        intent.putExtra(Constants.RECEIVER, startPointReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);

    }

    private void fetchDestinationAddress(Location location){
        Intent intent = new Intent(this, FetchAddress.class);
        intent.putExtra(Constants.RECEIVER, destinationPointReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);

    }

    private class StartPointReceiver extends ResultReceiver {
        public StartPointReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode == Constants.SUCCESS_RESULT){
                start_point.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            }else {
                Toast.makeText(TripUser.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DestinationPointReceiver extends ResultReceiver {
        public DestinationPointReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode == Constants.SUCCESS_RESULT){
                destination_point.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            }else {
                Toast.makeText(TripUser.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

