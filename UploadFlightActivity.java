package cs.bo7.p3;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cs.bo7.p3.flight.FlightManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Random;

public class UploadFlightActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_upload_flight);
  }

  /**
   * Uploads Flights from a specified CSV file.
   * @param view A View Object.
   */
  public void uploadFlight(View view) {
    TextView result = (TextView) findViewById(R.id.flight_result_text);
    result.setVisibility(View.INVISIBLE);
    Random rnd = new Random();
    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    EditText filePathText = (EditText) findViewById(R.id.upload_flight_filepath);
    String filePath = filePathText.getText().toString();
    filePath = getFilesDir() + "/" + filePath;
    try {
      // Load Flight data from file.
      loadUserData();
      FlightManager.uploadFlightInfo(filePath);
      FlightManager.saveAllChanges();
      result.setText("Upload Succeeded!");
      result.setTextColor(color);
      result.setVisibility(View.VISIBLE);
    } catch (ParseException | FileNotFoundException e) {
      e.printStackTrace();
      //failure so let them know with a vibration and a message
      result.setText("Upload Failed. Try a different path");
      result.setTextColor(color);
      result.setVisibility(View.VISIBLE);
      Vibrator vib = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
      vib.vibrate(100);
    }
  }

  /**
   * Initializes the file paths for the AccountManager's static Constants while also creating
   * a new flights folder if it does not exist.
   */
  private void loadUserData() {

    String filePath = getFilesDir() + "/flights/";
    File file = new File(filePath);
    file.mkdir();

    FlightManager.filePath(getFilesDir().getPath());
  }
  public void uploadFlightBack(View view){ finish(); }

}
