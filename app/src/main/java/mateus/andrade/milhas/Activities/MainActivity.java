package mateus.andrade.milhas.Activities;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import mateus.andrade.milhas.Models.Flight;
import mateus.andrade.milhas.R;
import mateus.andrade.milhas.Utils.DelayedProgressDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.etIATAOrigin) EditText etIATAOrigin;
    @BindView(R.id.etIATADestination) EditText etIATADestination;
    @BindView(R.id.dateofdeparture) EditText etdateofdeparture;
    @BindView(R.id.etdateofreturn) EditText etdateofreturn;
    @BindView(R.id.etNumberOfPassengers) EditText etNumberOfPassengers;
    @BindView(R.id.btSearch) Button btSearch;
    Calendar myCalendar = Calendar.getInstance();
    private JsonDownloader jsonDownloader = null;
    String requestURL;
    Flight[] flightsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        //Trocar cor da status bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.topaz));
        }

        etIATAOrigin.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        etIATADestination.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        //Setting Listeners
        btSearch.setOnClickListener(this);
        etdateofdeparture.setOnClickListener(this);
        etdateofreturn.setOnClickListener(this);
    }

    private void updateLabel(EditText editText) {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.dateofdeparture:

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        updateLabel(etdateofdeparture);
                    }

                };
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.etdateofreturn:
                DatePickerDialog.OnDateSetListener dateReturn = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(etdateofreturn);
                    }

                };
                new DatePickerDialog(MainActivity.this, dateReturn, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.btSearch:
                createRequest();
                jsonDownloader = new JsonDownloader();
                jsonDownloader.execute((Void) null);
                break;

        }
    }

    private void createRequest() {

        requestURL = getString(R.string.GOIBIBO_REQUEST) +
                "app_id=" + getString(R.string.GOIBIBO_APP_ID) +
                "&app_key=" + getString(R.string.GOIBIBO_APP_KEY) +
                "&source=CNF" + //etIATAOrigin.getText() +
                "&destination=GIG" + //etIATADestination.getText() +
                "&dateofdeparture=20180110" +
                //"&dateofarrival=20180112" +
                "&seatingclass=E" +
                "&adults=1" + //etNumberOfPassengers.getText() +
                "&children=0" +
                "&infants=0" +
                "&counter=100";

    }


    public class JsonDownloader extends AsyncTask<Void, Void, JsonArray> {

        DelayedProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new DelayedProgressDialog();
            progressDialog.show(getSupportFragmentManager(), "tag");

        }

        @Override
        protected JsonArray doInBackground(Void... params) {
            JsonArray flights = null;
            // Connect to the URL using java's native library
            try {
                URL url = new URL(requestURL);
                HttpURLConnection request = null;
                request = (HttpURLConnection) url.openConnection();
                request.connect();
                JsonParser jp = new JsonParser(); //from gson
                JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
                flights = ((JsonObject) root).get("data").getAsJsonObject().get("onwardflights").getAsJsonArray();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Erro na solicitação",Toast.LENGTH_SHORT).show();
            }

            return flights;
        }

        @Override
        protected void onPostExecute(final JsonArray json) {
            if(json==null)
                return;
            Gson gson = new Gson();
            flightsArray = gson.fromJson(json, Flight[].class);
            //dismiss or cancel the dialog
            progressDialog.cancel();
            Intent myIntent = new Intent(MainActivity.this, Result.class);
            MainActivity.this.startActivity(myIntent);
        }
    }
}
