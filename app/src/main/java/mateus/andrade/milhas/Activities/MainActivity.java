package mateus.andrade.milhas.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import mateus.andrade.milhas.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.etIATAOrigin) EditText etIATAOrigin;
    @BindView(R.id.etIATADestination) EditText etIATADestination;
    @BindView(R.id.dateofdeparture) EditText etdateofdeparture;
    @BindView(R.id.dateofreturn) EditText dateofreturn;
    @BindView(R.id.etNumberOfPassengers) EditText etNumberOfPassengers;
    @BindView(R.id.btSearch) Button btSearch;

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

        //Setting Listeners
        btSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.dateofdeparture:
                break;
            case R.id.dateofreturn:
                break;
            case R.id.btSearch:
                Intent myIntent = new Intent(MainActivity.this, Result.class);
                MainActivity.this.startActivity(myIntent);
                break;



        }
    }
}
