package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.nio.ByteBuffer;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.BitmapManager;


/**
 * <h1>ImageBlowupDialog</h1>
 * This is a dialog to show a larger view of an image file and some basic metadata
 * @author Lucas Thalen
 * @see BitmapManager
 */
public class ImageBlowupDialog {
    private Activity caller;
    private View diagView;
    private AlertDialog diag;


    /**
     * Show the dialog
     * @param caller contexts necessary for generating a dialog
     * @param input (BitmapManager) This will contain the metadata + img data to display
     */
    public void showDialog(final Activity caller, final BitmapManager input) {
        this.caller = caller;
        Context context = (Context) caller;
        Bitmap srcImg = input.getFullImg();
        AlertDialog.Builder diagBuilder = new AlertDialog.Builder(context);
        diagView = View
                .inflate(context, R.layout.dialog_image_blowup, null );
        diagBuilder.setView(diagView);

        diag = diagBuilder.create();
        diag.setCanceledOnTouchOutside(true);
        ImageView img = (ImageView) diagView.findViewById(R.id.img_ShowImgBig);
        img.setImageBitmap(srcImg);
        TextView imgInfo = (TextView) diagView.findViewById(R.id.txt_imgSize);
        String validUpload;
        if (input.getStatus()) { validUpload = "Valid attachment. The filesize is small enough."; } else { validUpload = "Invalid attachment. The filesize is too large.\n" +
        "This image will not be attached."; }
        String description = "File Info:\n(Compressed) " + String.format("%,d",input.getSize()) + " Bytes (of " +
                String.format("%,d", 65535) + " allowed)\n\n" + validUpload;
        imgInfo.setText(description);
        diag.show();

        // EVENTHANDLERS
        btn_Close_onClick();
    }

    private void btn_Close_onClick() {
        Button close = (Button) diagView.findViewById(R.id.btn_imgBlowupClose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diag.dismiss();
            }
        });
    }

}
