package ca.ualberta.cs.w18t11.whoselineisitanyway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailRowAdapter extends ArrayAdapter<Detail> {
    
    private final Context context;
    private final ArrayList<Detail> details;

    /**
     * Create a row adapter for a list of details
     * @param context the current context
     * @param details list of details to be adapted
     * @return New DetailRowAdapter
     */
    public DetailRowAdapter(Context context, ArrayList<Detail> details)
    {
        super(context, -1, details);
        this.context = context;
        this.details = details;
    }

    /**
     * Return a view for a specific row
     * @param position position in subscription list
     * @param convertView contentView to add to
     * @param parent parent view element
     * @return View view of the detail row
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the layout for the row
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View detailRow = inflater.inflate(R.layout.detailrow, parent, false);

        // Get TextView objects from the layout
        TextView rowTitle = detailRow.findViewById(R.id.textview_detailrow_title);
        TextView rowInfo = detailRow.findViewById(R.id.textview_detailrow_info);

        // Set the values of the TextViews
        rowTitle.setText(details.get(position).getTitle());
        rowInfo.setText(details.get(position).getInfo());

        return detailRow;
    }

    /**
     * Get item from the subscription list
     * @param position position in the subscription list
     * @return Detail the Detail from the position on the list that was clicked
     */
    @Override
    public Detail getItem(int position) {
        return details.get(position);
    }
}
