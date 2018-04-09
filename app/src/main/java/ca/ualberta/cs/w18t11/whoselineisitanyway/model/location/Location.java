package ca.ualberta.cs.w18t11.whoselineisitanyway.model.location;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public final class Location implements Serializable
{
    private static final long serialVersionUID = 2838946884503347862L;

    private final double latitude;

    private final double longitude;

    /**
     * Adapts a Google Maps LatLng to an equivalent, Serializable type.
     *
     * @param latLng The LatLng to adapt.
     * @see LatLng
     */
    public Location(@NonNull final LatLng latLng)
    {
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
    }

    /**
     * @return The latitude.
     */
    public final double getLatitude()
    {
        return this.latitude;
    }

    /**
     * @return The longitude.
     */
    public final double getLongitude()
    {
        return this.longitude;
    }

    public final String toString()
    {
        return "(" + String.valueOf(latitude) + ", " + String.valueOf(longitude) + ")";
    }
}
