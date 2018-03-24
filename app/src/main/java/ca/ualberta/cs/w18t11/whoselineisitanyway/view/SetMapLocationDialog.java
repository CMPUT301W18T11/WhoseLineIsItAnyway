package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import static android.content.Context.LOCATION_SERVICE;

import java.util.logging.Level;
import java.util.logging.Logger;


// TODO Remove toasts used in debugging
// TODO Write Javadoc UML
// TODO Allow finding loc by an address? See: GeoLoc

public class SetMapLocationDialog implements OnMapReadyCallback {
	// Allow passing data back to the calling activity using an event trigger to avoid async issues
	public interface MapDialogReturnListener {
		void onMapSetDialogReturn(LatLng result);
	}

	private AlertDialog mapDiag;
	private View diagView;
	private MapView mapView;
	private GoogleMap gMap;
	private LatLng locResult;
	private Location userLocation;
	private Activity caller;
	private Logger logi;

	private MapDialogReturnListener returnListener;
	/*
	* Implement like so: The calling activity should have a method called onMapSetDialogReturn(LatLng result)
	* Within the override code, make sure it sets a LatLng variable within the activity; this can probably be done with newListener
	*
	* */

	/**
	 * ShowDialog
	 * This creates a dialog with a mapview and the option to return a location from a user point.
	 * @param caller This is the calling activity. Activity.this or this
	 */
	public void showDialog(final Activity caller) {
		final int LAYOUT_TEMPLATE = R.layout.location_setdialog_layout; // Get the template for the dialog
		this.caller = caller; // Get the Activity for permissions checking
		final Context context = caller; // Get the context of the calling activity


		if (context instanceof MapDialogReturnListener) {
			returnListener = (MapDialogReturnListener) context;
		}

		// CREATE DIALOG
		AlertDialog.Builder diagBuilder = new AlertDialog.Builder(context);
		diagView = View
			.inflate(context, LAYOUT_TEMPLATE, null );
		diagBuilder.setView(diagView);

		mapDiag = diagBuilder.create();
		mapDiag.setContentView(diagView);
		mapDiag.setTitle("Set a Location for the Task");
		// END CREATE DIALOG
		
		// MAP CONTROL INITIALIZATION
		mapView = (MapView) diagView.findViewById(R.id.map_mapdialog_setloc);
		mapView.onCreate(mapDiag.onSaveInstanceState());
		mapView.onResume();
		
		// END MAP CONTROL INIT

		// Locations Components
		// Get locations permissions
		if (ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			initMap(context);
		} else {
			// Request location permissions
			int PERMISSION_LOCATION_REQUEST_CODE = 0;
			ActivityCompat.requestPermissions(caller, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST_CODE);
			if (PERMISSION_LOCATION_REQUEST_CODE == PackageManager.PERMISSION_DENIED) {
				returnListener.onMapSetDialogReturn(null);
				mapDiag.dismiss();
			} else if (PERMISSION_LOCATION_REQUEST_CODE == PackageManager.PERMISSION_GRANTED) {
				initMap(context);
			}
		}

		// EVENTHANDLERS
		btn_save_onClick();
		btn_cancel_onClick();

		mapDiag.show();

	}

	private void initMap(final Context context) {

		// TODO Improve location code - get location updates each time the dialog is called, based on current data, not old data
		// WORK ON LOCATION CODE
		final LocationManager locMan = (LocationManager) context.getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		final String locationProvider = locMan.getBestProvider(criteria, true);
		if (ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			setUserLoc(locMan.getLastKnownLocation(locationProvider));
		}
		if (userLocation == null) {
			logi.log(Level.ALL,"App crashed because of null user location");
		}

		mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                // Get a Google Map and center it on user position @ block level
                gMap = googleMap;
                // TODO: Toast to remove
                Toast test = Toast.makeText(context, userLocation.toString(),Toast.LENGTH_LONG);
                test.show();
                LatLng userCurPos = new LatLng(userLocation.getLatitude(), userLocation.getLongitude()); // get user's last known location
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((userCurPos), 15));
                gMap_onClickTarget();
            }
        });
	}


	private void gMap_onClickTarget() {
		gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setResult(latLng);
                gMap.clear();
                gMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Chosen Location"));
            }
        });
	}

	private void btn_save_onClick() {
		Button save = (Button) diagView.findViewById(R.id.btn_mapdialog_Save);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mapDiag.dismiss();
				returnListener.onMapSetDialogReturn(locResult);

			}
		});
	}
	private void btn_cancel_onClick() {
		Button cancel = (Button) diagView.findViewById(R.id.btn_mapdialog_Cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mapDiag.dismiss();
			}
		});
	}

	private void setResult(final LatLng res) {
		locResult = res;
	}
	
	private void setUserLoc(final Location userLoc) {
		userLocation = userLoc;
	}
	
	@Override
	public void onMapReady(GoogleMap googleMap) {}
}