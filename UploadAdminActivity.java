package cs.bo7.p3;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cs.bo7.p3.user.AccountManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Random;


public class UploadAdminActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_upload_admin);
  }

  /**
   * Uploads all the admins in the csv file given by the user.
   * @param view the view
   */
  public void uploadAdmin(View view) {
    TextView result = (TextView) findViewById(R.id.admin_result_text);
    result.setVisibility(View.INVISIBLE);
    result.setVisibility(View.INVISIBLE);
    Random rnd = new Random();
    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    EditText filePathText = (EditText) findViewById(R.id.upload_admin_filepath);
    String filePath = filePathText.getText().toString();
    // Concatenate user file data with the native directory of the application.
    filePath = getFilesDir() + "/" + filePath;

    try {
      // Upload Admin data.
      loadUserData();
      AccountManager.uploadAdminInfo(filePath);
      AccountManager.saveAllChanges();
      result.setText("Upload Success!");
      result.setTextColor(color);
      result.setVisibility(View.VISIBLE);
    } catch (ParseException | FileNotFoundException e) {
      e.printStackTrace();
      //failure so let them know with a vibration and a message
      result.setText("Upload Failed. Try a different file path.");
      result.setTextColor(color);
      result.setVisibility(View.VISIBLE);
      Vibrator vib = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
      vib.vibrate(100);
    }
  }

  /**
   * Initializes the file paths for the AccountManager's static Constants while also creating
   * a new clients and a new admins folder if they do not exist.
   */
  private void loadUserData() {

    // Make new folders for clients and admins if required.
    String filePath = getFilesDir() + "/clients/";
    File file = new File(filePath);
    file.mkdir();

    String filePath2 = getFilesDir() + "/admins/";
    File file2 = new File(filePath2);
    file2.mkdir();

    // Set the file path in AccountManager for serialization.
    AccountManager.filePath(getFilesDir().getPath());
  }
  public void uploadAdminBack(View view){ finish(); }

}
