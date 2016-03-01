package cs.bo7.p3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import cs.bo7.p3.flight.FullFlightException;
import cs.bo7.p3.itinerary.Itinerary;
import cs.bo7.p3.itinerary.ItineraryManager;
import cs.bo7.p3.user.AccountManager;
import cs.bo7.p3.user.BookingAlreadyExistsException;
import cs.bo7.p3.user.Client;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ItineraryView extends AppCompatActivity {

  private Itinerary itinerary;
  private Client client;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_itinerary_view);

    Intent intent = getIntent();
    itinerary = (Itinerary) intent.getSerializableExtra("itinerary");
    client = (Client) intent.getSerializableExtra(ClientView.CLIENT);

    TextView totalCost = (TextView) findViewById(R.id.client_cost);
    NumberFormat priceFormat = new DecimalFormat("#0.00");
    String price = "Total Cost: " + priceFormat.format(itinerary.getTotalCost());
    totalCost.setText(price);

    TextView totalTime = (TextView) findViewById(R.id.client_time);
    String time = "Total Time: " + Itinerary.getConvertedTime(itinerary.getTotalTime());
    totalTime.setText(time);


    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction().add(R.id.container2, new PlaceholderFragment(
              
      )).commit();
    }
  }

  public void bookItinerary(View view) {
    try {
      AccountManager am = new AccountManager();
      am.createBooking(itinerary, client);
      Intent newIntent = new Intent();
      newIntent.putExtra(ClientView.CLIENT, client);
      setResult(RESULT_OK, newIntent);
      finish();
    } catch (BookingAlreadyExistsException e) {
      AlertDialog.Builder exists = new AlertDialog.Builder(this).setTitle("Itinerary Exists");
      exists.setMessage("You have already booked this itinerary");
      exists.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          Intent newIntent = new Intent();
          newIntent.putExtra(ClientView.CLIENT, client);
          setResult(RESULT_CANCELED, newIntent);
          finish();
        }
      });
      exists.show();
    } catch (FullFlightException e) {
      AlertDialog.Builder exists = new AlertDialog.Builder(this).setTitle("Flight is full");
      exists.setMessage("Sorry, one of the flights you want to book is full.");
      exists.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          Intent newIntent = new Intent();
          newIntent.putExtra(ClientView.CLIENT, client);
          setResult(RESULT_CANCELED, newIntent);
          finish();
        }
      });
      exists.show();
    }
  }

  @Override
  public void onBackPressed() {
    Intent newIntent = new Intent();
    newIntent.putExtra(ClientView.CLIENT, client);
    setResult(RESULT_OK, newIntent);
    finish();
  }

  public class PlaceholderFragment extends Fragment {
    ItineraryViewAdapter adapter;
    RecyclerView recyclerView;

    public PlaceholderFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
        savedInstanceState) {
      return inflater.inflate(R.layout.flights_fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      recyclerView = (RecyclerView) getView().findViewById(R.id.flight_list);
      recyclerView.setHasFixedSize(true);
      LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
      layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      recyclerView.setLayoutManager(layoutManager);
      adapter = new ItineraryViewAdapter(itinerary);
      recyclerView.setAdapter(adapter);
    }
  }


}
