package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;


// TODO Remove toasts used in debugging
// TODO Write Javadoc UML
// TODO Allow finding loc by an address? See: GeoLoc

/**
 * <h1>SetMapLocationDialog</h1>
 * This class is designed to create a dialog with a map (and potentially address location entry) for
 * a user to select a location to mark a task with.
 */
/*
 * Very important: IMPLEMENT THE CALLBACK METHODS IN THE CALLING CLASS
 * Due to how Android handles threads (almost entirely Async) these are the only way to effect
 * a code "pause" while awaiting a result. You must design your code such that this method is the
 * last executed in a block; use its callback methods to continue your code by calling an additional
 * method. Anything else will result in a race condition where code progresses without the returned
 * result being properly assigned.
 */
public class ShowTaskLocationDialog implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

	// Initialization
	private Activity caller;
	private Context context;

	// Dialog
	private AlertDialog mapDiag;
	private View diagView;
	private MapView mapView;
	private GoogleMap gMap;

	// Location
	private GoogleApiClient mGoogleApiClient;
	private LatLng taskCoord;


	// App Alerts
	Toast appAlert;
	/**
	 * CONSTRUCTOR: Set context and activity references
	 * Check that proper mandatory methods are implemented
	 * Prompt for locations
	 * @param caller Activity reference for the parent activity to the dialog
	 */
	public ShowTaskLocationDialog(final Activity caller) {
		this.caller = caller;
		context = (Context) caller;

		initGoogleApi();
		createDialog();
		initMap();

		// EVENTHANDLERS
		btn_cancel_onClick();

	}

	//region Google API
	private void initGoogleApi() {
		mGoogleApiClient = new GoogleApiClient.Builder(context)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();

		mGoogleApiClient.connect();
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
	}

	@Override
	public void onConnectionSuspended(int i) {
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.i("SetMapLocationDialog","Failed to establish a Google Api Connection: " + connectionResult.getErrorMessage());
	}
	//endregion

	private void createDialog() {
		final int LAYOUT_TEMPLATE = R.layout.dialog_tasklocation; // Get the template for the dialog

		// CREATE DIALOG
		AlertDialog.Builder diagBuilder = new AlertDialog.Builder(context);
		diagView = View
				.inflate(context, LAYOUT_TEMPLATE, null );
		diagBuilder.setView(diagView);

		mapDiag = diagBuilder.create();
		mapDiag.setTitle("View Task Location");

		mapView = diagView.findViewById(R.id.map_mapdialog_setloc);

	}

	public void showDialog(LatLng loc) {
		this.taskCoord = loc;
		// MAP CONTROL INITIALIZATION
		mapView = (MapView) diagView.findViewById(R.id.map_mapdialog_setloc);
		mapView.onCreate(mapDiag.onSaveInstanceState());
		mapView.onResume();


		mapDiag.show();


	}

	private void initMap() {
		if (ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			Log.i("SetMapLocation","Location permissions allowed.");
			mapView.getMapAsync(new OnMapReadyCallback() {
				@Override
				public void onMapReady(GoogleMap googleMap) {
				gMap = googleMap;
					try {
						gMap.setMyLocationEnabled(false);
						gMap_CamMove();
						gMap.addMarker(new MarkerOptions()
								.position(taskCoord)
								.title("Task Location"));
					} catch (SecurityException ex) { Log.i("ShowTaskLoc", "Permissions error."); }
				}
			});
		}
	}
		private void gMap_CamMove() {
		if (taskCoord != null && gMap != null) {
			LatLng usrPoint = taskCoord;
			CameraUpdate camChange = CameraUpdateFactory.newCameraPosition(
					CameraPosition.builder()
							.target(usrPoint)
							.zoom(14)
							.build()
			);
			gMap.moveCamera(camChange);
		}
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

	@Override
	public void onMapReady(GoogleMap googleMap) {}
}