package ca.ualberta.cs.w18t11.whoselineisitanyway.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <<h1>Bitmap Manager</h1>
 * Allow the creation of Bitmaps from multiple inputs and facilitate bitmap manipulations in this project
 * such as String-storage and getting a thumnail. Also retains some useful metadata for easy access.
 * @author Lucas Thalen
 * @see Bitmap
 */
public class BitmapManager {
    private long MAX_FILE_SIZE = 65535;
    private Bitmap img;
    private boolean status;
    private long size;
    private Context context;

    /**
     * Constructor, Uri input variant (Used with image gallery picker)
     * @param resourceLocation  the resource identifier in the filesystem
     * @param context Context of the contentResolver()
     */
    public BitmapManager(final Uri resourceLocation, Context context) {
        this.context = context; // set context
        // get bitmap from filesystem
        try {
            InputStream readStream = context.getContentResolver().openInputStream(resourceLocation);
            this.img = BitmapFactory.decodeStream(readStream);

        } catch (IOException ex) {
            Log.i("BitmapManager Uri", "Failed to create bitmap from image.");
        }

        setCompressedSize(); // get size of compressed bitmap
        status = size <= MAX_FILE_SIZE; // set status as valid attachment
    }

    /**
     * Constructor, Bitmap input variant
     * @param img A pre-made bitmap to be applied to the manager
     */
    public BitmapManager(final Bitmap img) {
        this.img = img;
        setCompressedSize();
        status = size <= MAX_FILE_SIZE;
    }

    /**
     * Constructor Base64 variant
     * @param img Base-64 string representing a bitmap image
     */
    public BitmapManager(final String img) {
        this.img = decodeB64Str(img); // decode base-64 string into bitmap
        setCompressedSize(); // set size of compressed bitmap image
        status = size <= MAX_FILE_SIZE; // set status as a valid attachment
    }

    /**
     * Get the file size in bytes
     * @return file size (long)
     */
    public long getSize() {
        return size;
    }

    /**
     * Get reference to full bitmap image
     * @return bitmap (Bitmap)
     */
    public Bitmap getFullImg() {
        return img;
    }

    /**
     * Get thumbnailed bitmap for image boxes and small controls
     * @return thuumbnail (Bitmap)
     */
    public Bitmap getScaledBitmap() {
        final double IMG_SCALE_THRESHOLD = 300.0; // Adjust this for scaling
        int scaledHeight = 0;
        int scaledWidth = 0;

        // don't bother scaling if the image is smaller than IMG_SCALE_THRESHOLD ** 2
        if (img.getWidth() > IMG_SCALE_THRESHOLD || img.getHeight() > IMG_SCALE_THRESHOLD) {
            scaledHeight = (int)((IMG_SCALE_THRESHOLD / img.getHeight()) * img.getHeight()); // scale down height
            scaledWidth = (int)((IMG_SCALE_THRESHOLD / img.getWidth()) * img.getWidth()); // scale down width
            return Bitmap.createScaledBitmap(img, scaledWidth, scaledHeight, true); // teturn scaled bitmap
        } else { return img; }
    }

    /**
     * Get the base64 representation of the image (in str format)
     * @return bitmap (base64 String)
     */
    public String getBase64Bitmap() {
        return encodeB64Bitmap();
    }

    // https://stackoverflow.com/a/9768973/4914842
    // Roman Truba | Creative Commons 3.0 SA
    // Posted 03/19/2012 | Accessed 04/01/2018
    // Base 64 encode and decode for bitmap
    private Bitmap decodeB64Str(final String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    private String encodeB64Bitmap () {
        ByteArrayOutputStream encodingArray = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 30, encodingArray);
        return Base64.encodeToString(encodingArray.toByteArray(), Base64.DEFAULT);
    }

    /**
     * Evaluate if the comprising images of the manager are the same as another manager
     * @param other another BitmapManager
     * @return true/false based on match
     */
    public boolean getSame(BitmapManager other) {
        return img.sameAs(other.getFullImg());
    }

    private void setCompressedSize() {
        // compress the bitmap into a jpeg .30 quality and get its size; this is what will be encoded
        // when it is submitted to the server, if it is a valid attachment
        ByteArrayOutputStream encodingArray = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 30, encodingArray);
        size = (long) encodingArray.size();
        encodingArray.reset();
    }

    /**
     * Return if the image is a valid attachment or not
     * @return true false depending on the image size
     */
    public boolean getStatus() {
        return status;
    }
}
