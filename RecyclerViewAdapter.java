package cs.bo7.p3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cs.bo7.p3.itinerary.Itinerary;
import cs.bo7.p3.user.Client;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * A Recycler View Adapter.
 * Created by Faizan on 2015-11-30.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter
    .ListItemViewHolder> {

  private ArrayList<Itinerary> itineraries;
  private SparseBooleanArray selectedItems;
  private Context context;
  private Client client;

  RecyclerViewAdapter(Context context, ArrayList<Itinerary> itineraries, Client client) {
    this.itineraries = itineraries;
    this.context = context;
    this.client = client;
    if (itineraries == null) {
      throw new IllegalArgumentException("Itineraries must not be null");
    }
    selectedItems = new SparseBooleanArray();
  }


  @Override
  public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    View itemView = LayoutInflater
        .from(viewGroup.getContext()).inflate(R.layout.custom_itinerary_list, viewGroup, false);
    return new ListItemViewHolder(itemView);
  }


  @Override
  public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
    Itinerary itinerary = itineraries.get(position);
    NumberFormat formatter = new DecimalFormat("#0.00");
    viewHolder.origin.setText(itinerary.getOrigin());
    viewHolder.destination.setText(itinerary.getDestination());
    viewHolder.totalCost.setText(formatter.format(itinerary.getTotalCost()));
    viewHolder.totalTime.setText(itinerary.getConvertedTime(itinerary.getTotalTime()));
    viewHolder.itinerary = itinerary;
    viewHolder.context = context;
    viewHolder.client = client;
    viewHolder.itemView.setActivated(selectedItems.get(position, false));
  }

  @Override
  public int getItemCount() {
    return itineraries.size();
  }

  public final class ListItemViewHolder extends RecyclerView.ViewHolder {
    TextView origin;
    TextView destination;
    TextView totalCost;
    TextView totalTime;
    Itinerary itinerary;
    Context context;
    Client client;


    public ListItemViewHolder(View itemView) {
      super(itemView);
      origin = (TextView) itemView.findViewById(R.id.txt_origin);
      destination = (TextView) itemView.findViewById(R.id.txt_destination);
      totalCost = (TextView) itemView.findViewById(R.id.txt_cost);
      totalTime = (TextView) itemView.findViewById(R.id.txt_time);


      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View vi) {
          Intent itineraryIntent = new Intent(context, ItineraryView.class);
          itineraryIntent.putExtra("itinerary", itinerary);
          itineraryIntent.putExtra(ClientView.CLIENT, client);
          ((ItineraryListView) context).startActivityForResult(itineraryIntent, 1);
        }
      });
    }
  }
}


