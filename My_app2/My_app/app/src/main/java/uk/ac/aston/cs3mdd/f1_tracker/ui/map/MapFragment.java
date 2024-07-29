package uk.ac.aston.cs3mdd.f1_tracker.ui.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.aston.cs3mdd.f1_tracker.R;
import uk.ac.aston.cs3mdd.f1_tracker.databinding.FragmentMapBinding;
import uk.ac.aston.cs3mdd.f1_tracker.model.Races;
import uk.ac.aston.cs3mdd.f1_tracker.service.apiFormula;

import com.google.android.gms.location.FusedLocationProviderClient;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap displayMap;
    private FragmentMapBinding binding;
    private AutoCompleteTextView autoComplete;
    private Marker currentMarker;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Polyline currentPolyline;
    private List<Races.ResponseItem> apiData;
    private TextView displayDistance;
    private boolean kmToMiles = true;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autoComplete = view.findViewById(R.id.autoComplete);
        //auto complete the dropdown when i press enter and prevent the user to switch line when clicking the enter button
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line);
        autoComplete.setAdapter(adapter);

        displayDistance = requireView().findViewById(R.id.showDistance);

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedLocation = (String) parent.getItemAtPosition(position);
                searchTrack(selectedLocation);
            }
        });

        autoComplete.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String query = autoComplete.getText().toString();
                    searchTrack(query);
                    return true;
                }
                return false;
            }
        });

        getTrackNamesFromApi();

        Button kmToMileConvertorButton = view.findViewById(R.id.kmToMileButton);
        kmToMileConvertorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //switch between Km to Mi when pressing on the button next to the distance
                String distanceText = displayDistance.getText().toString();
                double distanceValue = Double.parseDouble(distanceText.split(" ")[1]);
                if (kmToMiles) {
                    distanceValue *= 0.621371;
                    displayDistance.setText(String.format("Distance: %.2f Mi away", distanceValue));
                } else {
                    distanceValue *= 1.60934;
                    displayDistance.setText(String.format("Distance: %.2f Km away", distanceValue));
                }

                kmToMiles = !kmToMiles;
            }
        });
    }


    private void searchTrack(String query) {
        List<String> validLocations = extractCircuitNames(apiData);
        //pop-up to tell to the user that they can only choose a track from the dropdown list
        if (!validLocations.contains(query)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Please select a circuit from the list").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.create().show();
            return;
        }

        Geocoder geocoder = new Geocoder(requireContext());
        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng destination = new LatLng(address.getLatitude(), address.getLongitude());

                if (currentMarker != null) {
                    currentMarker.remove();
                }
                displayMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 12));
                currentMarker = displayMap.addMarker(new MarkerOptions().position(destination).title(query));
                //drawing the line from the user location to the searched location on the map (polyline)
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }

                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                                    PolylineOptions polyline = new PolylineOptions()
                                            .add(currentLocation)
                                            .add(destination)
                                            .color(Color.BLUE);

                                    currentPolyline = displayMap.addPolyline(polyline);
                                }
                            }
                        });

                autoComplete.dismissDropDown();

                //hide the keyboard when the user selects one of the tracks from the drop down list
                InputMethodManager hideKeyboard = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                hideKeyboard.hideSoftInputFromWindow(autoComplete.getWindowToken(), 0);

                //users current locations as well as the calculation of the distance between the user and the map
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        LatLng currentLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        Location currentLocation = new Location("Current Location");
                                        currentLocation.setLatitude(currentLocationLatLng.latitude);
                                        currentLocation.setLongitude(currentLocationLatLng.longitude);

                                        float distanceInMeters = currentLocation.distanceTo(new Location("Destination Location") {{
                                            setLatitude(destination.latitude);
                                            setLongitude(destination.longitude);
                                        }});
                                        float distanceInKilometers = distanceInMeters / 1000;
                                        displayDistance.setText(String.format("Distance: %.2f Km away", distanceInKilometers));
                                        Log.d("Distance", "Distance: " + distanceInKilometers + " Km");
                                    }
                                }
                            });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getTrackNamesFromApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-formula-1.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();
                                Request request = original.newBuilder()
                                        .header("X-RapidAPI-Host", "api-formula-1.p.rapidapi.com")
                                        .header("X-RapidAPI-Key", "APIKEYHERE")
                                        .method(original.method(), original.body())
                                        .build();
                                return chain.proceed(request);
                            }
                        })
                        .build())
                .build();

        apiFormula api = retrofit.create(apiFormula.class);
        Call<Races> call = api.returnRaces();
        call.enqueue(new Callback<Races>() {
            @Override
            public void onResponse(Call<Races> call, Response<Races> response) {
                if (response.isSuccessful()) {
                    Races races = response.body();
                    if (races != null && races.getResults() > 0) {
                        handleApiData(races.getResponse());
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("map", "Error: " + response.code() + ", " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Races> call, Throwable t) {
                Log.e("map", "api failed on the map fragment " + t.getMessage());
            }
        });
    }

    private void handleApiData(List<Races.ResponseItem> apiData) {
        this.apiData = apiData;
        List<String> circuitNames = extractCircuitNames(apiData);

        //add all the track names from the api to the dropdown list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, circuitNames);
        autoComplete.setAdapter(adapter);
    }

    private List<String> extractCircuitNames(List<Races.ResponseItem> apiData) {
        List<String> locationNames = new ArrayList<>();
        for (Races.ResponseItem responseItem : apiData) {
            if (responseItem.getCompetition() != null && responseItem.getCompetition().getLocation() != null) {
                String circuitName = responseItem.getCircuit().getName();
                locationNames.add(circuitName);
            }
        }
        return locationNames;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        displayMap = map;
        enableMyLocation();
        //display the user location when they open the app or give the access to the app to their location
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                displayMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12));
                            }
                        }
                    });
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    enableMyLocation();
                } else {
                    Toast.makeText(requireActivity(), "Location permission was denied. Please accept them.", Toast.LENGTH_SHORT).show();
                }
            });

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (displayMap != null) {
                try {
                    displayMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (displayMap != null) {
            enableMyLocation();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        displayMap.setMyLocationEnabled(false);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}