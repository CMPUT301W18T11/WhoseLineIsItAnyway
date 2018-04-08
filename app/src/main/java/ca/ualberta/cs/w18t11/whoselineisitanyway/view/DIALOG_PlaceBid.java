package ca.ualberta.cs.w18t11.whoselineisitanyway.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.math.BigDecimal;
import java.util.ArrayList;
import ca.ualberta.cs.w18t11.whoselineisitanyway.R;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.validator.TextValidator;
import ca.ualberta.cs.w18t11.whoselineisitanyway.model.validator.TextValidatorResult;

/**
 * <h1>PlaceBid Dialog</h1>
 * Type: Dialog
 * Collects a bid amount as a BigDecimal from the bidder
 * @author Lucas Thalen
 * IN: NONE
 * OUT: BigDecimal Bid (Passed via Interface Callback)
 */
public class DIALOG_PlaceBid {
    public interface PlaceBidReturnListener {
        void PlaceBidDialog_PosResult(BigDecimal result);
    }

    // General Context and Intefface
    private Activity caller;
    private Context context;
    private PlaceBidReturnListener returnListener;

    // Dialog components and creation
    private AlertDialog diag;
    private View diagView;

    // Validate that the format of the currency is correct
    private TextValidator validator = new TextValidator();

    /**
     * Constructor to create a dialog
     * @param caller calling activity/intent for context purposes and such
     */
    public DIALOG_PlaceBid(final Activity caller) {
        this.caller = caller;
        context = (Context) caller;

        if (context instanceof PlaceBidReturnListener) {
            returnListener = (PlaceBidReturnListener) context;
        } else { throw new RuntimeException("Calling class must contain interface methods!"); }

        createDialog();

        // EVENTHANDLERS
        btn_save_onClick();
        btn_cancel_onClick();
        etxt_bid_onTextChanged();

    }

    private void createDialog() {
        final int LAYOUT_TEMPLATE = R.layout.dialog_place_bid; // Get the template for the dialog

        // CREATE DIALOG
        AlertDialog.Builder diagBuilder = new AlertDialog.Builder(context);
        diagView = View
                .inflate(context, LAYOUT_TEMPLATE, null );
        diagBuilder.setView(diagView);

        diag = diagBuilder.create();
        diag.setTitle("Place a Bid");
    }

    public void showDialog() {
        diag.show();
    }
    private void btn_save_onClick() {
        Button submit = diagView.findViewById(R.id.btn_save);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText bidAmount = diagView.findViewById(R.id.etxt_BidAmount);
                if (bidAmount.getText().length() == 0) {
                    bidAmount.setError("You must enter a bid amount!");
                    return;
                }
                TextValidatorResult res = validator.validateCurrency(bidAmount.getText().toString(), false);
                if (res.isError()) {
                    bidAmount.setError(res.getErrorMSG());
                } else {
                    ArrayList<String> components = res.getComponents();
                    String amount = components.get(1);
                    log("BidAmount - STR: amount");
                    BigDecimal decimalBid = BigDecimal.valueOf(Double.valueOf(amount)); // Convert string to BigDecimal
                    log("BidAmount - DBL: " + String.valueOf(decimalBid));
                    diag.dismiss();
                    returnListener.PlaceBidDialog_PosResult(decimalBid); // Return result via callback
                }
            }
        });
    }

    private void btn_cancel_onClick() {
        Button cancel = diagView.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diag.dismiss();
            }
        });

    }

    private void etxt_bid_onTextChanged() {
        final EditText field = diagView.findViewById(R.id.etxt_BidAmount);
        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (field.getText().length() == 0) { return; }
                String contents = field.getText().toString();
                if (contents.contains(".")) {
                    int decimalPos = contents.indexOf(".");
                    if (contents.length() > decimalPos + 3) {
                        field.setText(field.getText().toString().substring(0, field.getText().length() - 1));
                        field.setSelection(field.getText().length());
                        field.setError("You can only enter 2 decimal values.");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void log(String message) {
        Log.i("DIALOG_PLACEBID", message);
    }

}
