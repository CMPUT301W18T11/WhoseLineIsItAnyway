package ca.ualberta.cs.w18t11.whoselineisitanyway.model.detail;


import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Represents a key-value pair of title and information.
 *
 * @author Brad Ofrim, Samuel Dolha
 * @version 2.0
 */
public final class Detail implements Parcelable
{
    /**
     * Implement for Parcelable
     *
     * @return
     */
    public static final Parcelable.Creator<Detail> CREATOR
            = new Parcelable.Creator<Detail>()
    {
        public Detail createFromParcel(Parcel in)
        {
            return new Detail(in);
        }

        public Detail[] newArray(int size)
        {
            return new Detail[size];
        }
    };

    /**
     * An auto-generated, unique ID to support class versioning for Serializable.
     */
    private static final long serialVersionUID = -5725853446964245736L;

    /**
     * The title of the detail.
     */
    @NonNull
    private final String title;

    /**
     * Information associated with the detail.
     */
    @NonNull
    private final String information;

    /**
     * The intent associating the detail with certain activities.
     */
    @Nullable
    private final Intent linkingIntent;

    /**
     * @param title         String representing the detail title
     * @param information   String representing the detail's information
     * @param linkingIntent Intent to be used to show to another activity
     * @see Detailed
     */
    public Detail(@NonNull final String title, @NonNull final String information,
                  @Nullable final Intent linkingIntent)
    {
        this.title = title;
        this.information = information;
        this.linkingIntent = linkingIntent;
    }

    /**
     * Implement for Parcelable
     *
     * @return
     */
    private Detail(Parcel in)
    {
        title = in.readString();
        information = in.readString();
        linkingIntent = in.readParcelable(Intent.class.getClassLoader());
    }

    /**
     * @return The title of the detail.
     */
    @NonNull
    public final String getTitle()
    {
        return this.title;
    }

    /**
     * @return The information associated with the detail.
     */
    @NonNull
    public final String getInformation()
    {
        return this.information;
    }

    /**
     * @return The linked intent associated with the detail.
     */
    @NonNull
    public final Intent getLink()
    {
        return this.linkingIntent;
    }

    /**
     * @return Whether the detail is linked to an intent.
     */
    public final boolean isLinked()
    {
        return this.linkingIntent != null;
    }

    /**
     * Implement for Parcelable
     *
     * @return
     */
    public int describeContents()
    {
        return 0;
    }

    /**
     * Implement for Parcelable
     *
     * @return
     */
    public void writeToParcel(Parcel out, int flags)
    {
        // Note: order matters
        out.writeString(title);
        out.writeString(information);
        out.writeParcelable(linkingIntent, 0);
    }
}
