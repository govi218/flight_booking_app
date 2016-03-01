package cs.bo7.p3;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cs.bo7.p3.driver.Constants;
import cs.bo7.p3.flight.Flight;
import cs.bo7.p3.itinerary.Itinerary;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by Faizan on 2015-11-30.
 */
public class ItineraryViewAdapter extends RecyclerView.Adapter<ItineraryViewAdapter
    .ListItemViewHolder> {
  private ArrayList<Flight> flights;
  private SparseBooleanArray selectedItems;
  private ArrayList<String> layoverTimes;

  ItineraryViewAdapter(Itinerary itinerary) {
    this.flights = itinerary.getFlights();
    this.layoverTimes = itinerary.getLayoverTimes();
    selectedItems = new SparseBooleanArray();
    if (flights == null) {
      throw new IllegalArgumentException("Flights must not be null");
    }
  }

  @Override
  public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    View itemView = LayoutInflater
        .from(viewGroup.getContext())
        .inflate(R.layout.custom_itinerary_view, viewGroup, false);
    return new ListItemViewHolder(itemView);
  }


  @Override
  public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
    Flight flight = flights.get(position);
    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_AND_TIME);
    viewHolder.airline.setText(flight.getAirline());
    viewHolder.flightNum.setText(flight.getFlightnum());
    viewHolder.arrival.setText(dateFormat.format(flight.getArrival()));
    viewHolder.depart.setText(dateFormat.format(flight.getDeparture()));
    viewHolder.origin.setText(flight.getOrigin());
    viewHolder.destination.setText(flight.getDestination());
    NumberFormat priceFormat = new DecimalFormat("#0.00");
    viewHolder.cost.setText(priceFormat.format(flight.getPrice()));
    viewHolder.time.setText(Itinerary.getConvertedTime(flight.getFlighttime()));
    if (layoverTimes != null) {
      String layover = "Layover of " + String.valueOf(layoverTimes.remove(0));
      viewHolder.layover.setText(layover);
      if (layoverTimes.size() == 0) {
        layoverTimes = null;
      }
    }
    viewHolder.itemView.setActivated(selectedItems.get(position, false));
  }

  @Override
  public int getItemCount() {
    return flights.size();
  }

  public final class ListItemViewHolder extends RecyclerView.ViewHolder {
    TextView airline;
    TextView flightNum;
    TextView arrival;
    TextView depart;
    TextView origin;
    TextView destination;
    TextView cost;
    TextView time;
    TextView layover;

    public ListItemViewHolder(View itemView) {
      super(itemView);
      airline = (TextView) itemView.findViewById(R.id.rec_airline);
      flightNum = (TextView) itemView.findViewById(R.id.rec_flight_num);
      arrival = (TextView) itemView.findViewById(R.id.rec_arrive);
      depart = (TextView) itemView.findViewById(R.id.rec_depart);
      origin = (TextView) itemView.findViewById(R.id.rec_origin);
      destination = (TextView) itemView.findViewById(R.id.rec_destination);
      cost = (TextView) itemView.findViewById(R.id.rec_cost);
      time = (TextView) itemView.findViewById(R.id.rec_time);
      layover = (TextView) itemView.findViewById(R.id.rec_layover);
    }
  }
}
