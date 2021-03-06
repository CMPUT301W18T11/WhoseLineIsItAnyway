package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;

import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.user.User;

/*
 * This is just a generic activity for implementing parts of code; it is not intended to run as part of the app
 * and should be deleted before commiting to the final master release.
 *
 * Note to TA: Please basically ignore this file. It's a scrap area mostly for making dialogs. It will
 * not be present in the final edition of the project and is not intended for assessment.
 *
 * @author Lucas Thalen
 */
public class testActivity extends AppCompatActivity
        implements DIALOG_PlaceBid.PlaceBidReturnListener,
        SetMapLocationDialog.MapDialogReturnListener,
        UserRegisterDialog.diagUserRegistrationListener
{
    private LatLng location;

    private boolean res = false;

    private User resultUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        setContentView(R.layout.activity_test);
        super.onCreate(savedInstanceState);

        TextView debugText = findViewById(R.id.debug_textview);
        Bitmap.Config config = Bitmap.Config.RGB_565;
        Bitmap test = BitmapFactory.decodeResource(getResources(), R.drawable.bone_chew_dog)
                .copy(config, false);
        ImageView debugimg = findViewById(R.id.debug_imgview);
        debugimg.setImageBitmap(test);
        debugimg.setScaleType(ImageView.ScaleType.FIT_XY);
        double area = test.getWidth() * test.getHeight();
        int size = test.getByteCount();
        int overhead = test.getRowBytes();

        debugText.setText(String.valueOf(area) + "    :    " + String.valueOf(size) + "\n" + String
                .valueOf(overhead));

        Button btnTest = findViewById(R.id.btnTest);
        Button btnMapSetDialog = findViewById(R.id.btn_mapdiag);
        Button button = findViewById(R.id.debug_Button);
        btnTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)

            {
                UserRegisterDialog usrDiag = new UserRegisterDialog();
                usrDiag.showDialog(testActivity.this, "test");
            }
        });
        btnMapSetDialog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dothings();
            }
        });
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                DIALOG_WriteReview diag = new DIALOG_WriteReview(testActivity.this);
//                diag.showDialog();
            }
        });


    }

    private void dothings()
    {
        LatLng testLoc = new LatLng(53.5232, -113.5263);
        ShowTaskLocationDialog mapDiag = new ShowTaskLocationDialog(this);
        mapDiag.showDialog(testLoc);
    }

    @Override
    public void MapSetDialog_PosResult(LatLng result)
    {
        if (result != null)
        {
            Toast.makeText(this,
                    String.valueOf(result.latitude) + " " + String.valueOf(result.longitude),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void RegisterDiag_PosResultListener(User result)
    {

    }

    @Override
    public void RegisterDiag_NegResultListener()
    {

    }

    @Override
    public void PlaceBidDialog_PosResult(BigDecimal result)
    {
        Toast.makeText(this, "Bid Value:" + String.valueOf(result), Toast.LENGTH_LONG).show();
    }
}
