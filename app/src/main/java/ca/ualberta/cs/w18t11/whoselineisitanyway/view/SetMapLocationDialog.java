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
import com.google.android.gms.location.FusedLocationProviderClient;
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

/**
 * <h1>SetMapLocationDialog</h1>
 * Type: Dialog
 * This class is designed to create a dialog with a map (and potentially address location entry) for
 * a user to select a location to mark a task with.
 * In: None
 * Out: Location (Via callback)
 * @Author Lucas Thalen
 */
/*
 * Very important: IMPLEMENT THE CALLBACK METHODS IN THE CALLING CLASS
 * Due to how Android handles threads (almost entirely Async) these are the only way to effect
 * a code "pause" while awaiting a result. You must design your code such that this method is the
 * last executed in a block; use its callback methods to continue your code by calling an additional
 * method. Anything else will result in a race condition where code progresses without the returned
 * result being properly assigned.
 */
public class SetMapLocationDialog implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

	// Allow passing data back to the calling activity using an event trigger to avoid async issues
	public interface MapDialogReturnListener {
		void MapSetDialog_PosResult(LatLng result);
	}


	// Initialization
	private Activity caller;
	private Context context;
	private MapDialogReturnListener returnListener;
	private boolean permissionsGranted = false;

	// Dialog
	private AlertDialog mapDiag;
	private View diagView;
	private MapView mapView;
	private GoogleMap gMap;

	// Location
	private GoogleApiClient mGoogleApiClient;
	private LatLng locResult;
	private Location userLocation;
	private FusedLocationProviderClient mFusedLocationProvider;
	private LocationCallback mLocationCallback;
	private boolean foundLocation = false;
	private boolean canMoveCam = true;

	// App Alerts
	Toast appAlert;
	/**
	 * CONSTRUCTOR: Set context and activity references
	 * Check that proper mandatory methods are implemented
	 * Prompt for locations
	 * @param caller Activity reference for the parent activity to the dialog
	 */
	public SetMapLocationDialog(final Activity caller) {
		this.caller = caller;
		context = (Context) caller;
		appAlert = Toast.makeText(caller, "", Toast.LENGTH_SHORT);
		if (context instanceof MapDialogReturnListener) {
			returnListener = (MapDialogReturnListener) context;
		} else { throw new RuntimeException("Calling class must contain interface methods!"); }

		getLocation();
		initGoogleApi();
		createDialog();
		initMap();

		// EVENTHANDLERS
		btn_save_onClick();
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
		final int LAYOUT_TEMPLATE = R.layout.dialog_setlocation; // Get the template for the dialog

		// CREATE DIALOG
		AlertDialog.Builder diagBuilder = new AlertDialog.Builder(context);
		diagView = View
				.inflate(context, LAYOUT_TEMPLATE, null );
		diagBuilder.setView(diagView);

		mapDiag = diagBuilder.create();
		mapDiag.setTitle("Set a Location for the Task");

		mapView = diagView.findViewById(R.id.map_mapdialog_setloc);

	}

	public void showDialog() {
		// MAP CONTROL INITIALIZATION
		mapView = (MapView) diagView.findViewById(R.id.map_mapdialog_setloc);
		mapView.onCreate(mapDiag.onSaveInstanceState());
		mapView.onResume();
		mapDiag.show();
	}
	//region GetLocation GoodAccuracyButSlow
	private void getLocation() {
		mFusedLocationProvider = LocationServices.getFusedLocationProviderClient(caller);
		final LocationRequest locReq = LocationRequest.create()
				.setFastestInterval(1)
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setInterval(1)
				.setNumUpdates(1);

		mLocationCallback = new LocationCallback() {
			@Override
			public void onLocationResult(LocationResult locationResult) {
				if (!foundLocation && canMoveCam) {
					Location best = null;
					if (locationResult == null) {
						return;
					}
					for (Location location : locationResult.getLocations()) {
						if (best == null) {
							best = location;
						} else {
							if (best.hasAccuracy() && location.hasAccuracy()) {
								if (location.getAccuracy() < best.getAccuracy()) {
									best = location;
								}
							}
						}
					}
					if (best != null) {
						foundLocation = true;
						mFusedLocationProvider.removeLocationUpdates(mLocationCallback);
						setUserLoc(best);
						gMap_CamMove();
						appAlert.cancel();
						appAlert.makeText(caller, "Location acquired.", Toast.LENGTH_SHORT).show();
					}
				}
			}

		};

		try {
			mFusedLocationProvider.requestLocationUpdates(locReq,mLocationCallback, Looper.myLooper());
		} catch (SecurityException ex) {}

	}
//endregion

	private void initMap() {
		if (ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			Log.i("SetMapLocation","Location permissions allowed.");
			mapView.getMapAsync(new OnMapReadyCallback() {
				@Override
				public void onMapReady(GoogleMap googleMap) {
					appAlert.cancel();
					appAlert.makeText(caller, "Acquiring location...\nIf you don't want to wait, you can move the map.", Toast.LENGTH_SHORT).show();
					gMap = googleMap;
					gMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
						@Override
						public void onCameraMoveStarted(int i) {
							if (i == REASON_GESTURE) { setMoveAllowed(false); }
						}
					});
					try {
						gMap.setMyLocationEnabled(true);
					} catch (SecurityException ex) { Log.i("SetMapLocationDialog", "Permissions error."); }
					gMap_onClickTarget();
				}
			});
		}
	}
	private void setMoveAllowed(boolean allowed) {
		canMoveCam = allowed;
	}
	private void gMap_CamMove() {
		if (userLocation != null && gMap != null) {
			LatLng usrPoint = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
			CameraUpdate camChange = CameraUpdateFactory.newCameraPosition(
					CameraPosition.builder()
							.target(usrPoint)
							.zoom(14)
							.build()
			);
			gMap.moveCamera(camChange);
		}
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
				if (locResult == null) { return; }
				mapDiag.dismiss();
				returnListener.MapSetDialog_PosResult(locResult);

			}
		});
	}
	private void btn_cancel_onClick() {
		Button cancel = (Button) diagView.findViewById(R.id.btn_mapdialog_Cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				returnListener.MapSetDialog_PosResult(null);
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