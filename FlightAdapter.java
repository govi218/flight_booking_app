package cs.bo7.p3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import cs.bo7.p3.flight.Flight;
import cs.bo7.p3.user.Admin;

import java.util.ArrayList;


/**
 * Created by Daerian on 2015-11-27.
 * Custom Adapter to add buttons and text in the list together.
 * Used to enable viewing flights with ease in the FlightsView activity.
 */
public class FlightAdapter extends BaseAdapter implements ListAdapter {

  private ArrayList<Flight> flights;
  private Context context;
  private Admin admin;


  /**
   * Constructor that takes a context and a list of flights for use in the adapter.
   * @param context - Background info required for referencing (usually "this").
   * @param flights - Array list of flights to display.
   */
  public FlightAdapter(Context context, ArrayList<Flight> flights, Admin admin) {
    this.flights = flights;
    this.context = context;
    this.admin = admin;
  }

  @Override
  public int getCount() {
    return flights.size();
  }

  @Override
  public Object getItem(int pos) {
    return flights.get(pos);
  }

  @Override
  public long getItemId(int pos) {
    return 0;
  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent) {
    View view = convertView;
    if (view == null) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
          .LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.custom_flight_list, null);
    }

    Flight curFlight = flights.get(position);

    //Handle TextView and display flight number and airline
    TextView flightNumText = (TextView) view.findViewById(R.id.flight_num); // flight number
    flightNumText.setText(curFlight.getFlightnum());
    TextView airlineText = (TextView) view.findViewById(R.id.airline); // airline
    airlineText.setText(curFlight.getAirline());

    //Handle buttons and add onClickListeners
    ImageButton viewFlightButton = (ImageButton) view.findViewById(R.id.view_flight_button);
    viewFlightButton.setOnClickListener(new View.OnClickListener() {
      /**
       * Edit or view the flight.
       * @param vi - the view to pass
       */
      @Override
      public void onClick(View vi) {
        Intent editFlightIntent = new Intent(context, FlightEditActivity.class);
        Flight curFlight = flights.get(position);
        editFlightIntent.putExtra("ADMIN", admin);
        editFlightIntent.putExtra(FlightEditActivity.FLIGHT, curFlight);
        context.startActivity(editFlightIntent);
      }
    });
    notifyDataSetChanged();
    return view;
  }
}
