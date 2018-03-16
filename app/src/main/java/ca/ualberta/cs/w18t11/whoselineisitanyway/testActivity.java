package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class testActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        testActivity.this);
                AlertDialog alertDialog = alertDialogBuilder.create();
                LayoutInflater inflater = testActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.layout_register_user, null);
                alertDialogBuilder.setView(dialogView);




            }
        });
    }
}
