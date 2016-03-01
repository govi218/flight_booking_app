package cs.bo7.p3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cs.bo7.p3.flight.FlightManager;
import cs.bo7.p3.itinerary.Itinerary;
import cs.bo7.p3.itinerary.ItineraryManager;
import cs.bo7.p3.user.Client;

import java.text.ParseException;
import java.util.ArrayList;



public class ItineraryListView extends AppCompatActivity {

  private Intent intent;
  private Context context;
  private ArrayList<String> params;
  private Client client;
  private ItineraryManager im;
  private ArrayList<Itinerary> itineraries;
  private PlaceholderFragment phf;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_itineraries);
    this.intent = getIntent();
    this.params = intent.getStringArrayListExtra("params");
    this.client = (Client) intent.getSerializableExtra(ClientView.CLIENT);
    this.context = this;
    this.phf = new PlaceholderFragment();

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction().add(R.id.container, phf).commit();
    }
  }

  @Override
  public void onBackPressed() {
    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Client client = (Client) data.getSerializableExtra(ClientView.CLIENT);
    Intent newIntent = new Intent();
    newIntent.putExtra(ClientView.CLIENT, client);
    setResult(RESULT_OK, newIntent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.sort_by_price) {
      try {
        im.sortByPrice();
      } catch (NullPointerException e) {
        client.sortByPrice();
      }
      phf.refresh(itineraries);
    } else if (id == R.id.sort_by_time) {
      try {
        im.sortByTime();
      } catch (NullPointerException e) {
        client.sortByTime();
      }
      phf.refresh(itineraries);
    }
    return super.onOptionsItemSelected(item);
  }

  private void search() {
    im = new ItineraryManager();
    im.search(params.get(0), params.get(1), params.get(2));
    itineraries = im.getAllPossibleItineraries();
    if (itineraries.size() == 0) {
      AlertDialog.Builder exists = new AlertDialog.Builder(this).setTitle("No Itineraries");
      exists.setMessage("Search returned no itineraries, try other queries.");
      exists.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          finish();
        }
      });
      exists.show();
    }
  }

  private void displayBookings() {
    itineraries = client.getBookings();
    if (itineraries.size() == 0) {
      AlertDialog.Builder exists = new AlertDialog.Builder(this).setTitle("No Itineraries");
      exists.setMessage("You have no booked itineraries.");
      exists.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          finish();
        }
      });
      exists.show();
    }
  }

  public class PlaceholderFragment extends Fragment {
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    public PlaceholderFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
        savedInstanceState) {
      return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      recyclerView = (RecyclerView) getView().findViewById(R.id.my_list);
      recyclerView.setHasFixedSize(true);
      LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
      layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      recyclerView.setLayoutManager(layoutManager);
      try {
        search();
        adapter = new RecyclerViewAdapter(context, itineraries, client);
      } catch (IllegalArgumentException e)  {
        displayBookings();
        adapter = new RecyclerViewAdapter(context, itineraries, client);
      } catch (NullPointerException e) {
        displayBookings();
        adapter = new RecyclerViewAdapter(context, itineraries, client);
      }
      recyclerView.setAdapter(adapter);
    }

    public void refresh(ArrayList<Itinerary> itineraries) {
      adapter = new RecyclerViewAdapter(context, itineraries, client);
      recyclerView.setAdapter(adapter);
    }
  }
}
