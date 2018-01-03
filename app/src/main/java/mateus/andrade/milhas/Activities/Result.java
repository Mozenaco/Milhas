package mateus.andrade.milhas.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import mateus.andrade.milhas.Models.Flight;
import mateus.andrade.milhas.R;

/**
 * Created by mateusandrade on 01/01/2018.
 */

public class Result extends AppCompatActivity {

    List<Flight> flights;
    @BindView(R.id.backButton) ImageView backButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        ButterKnife.bind(this);

        //Trocar cor da status bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.topaz));
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            flights = (List<Flight>)extras.getSerializable("fly");
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        View recyclerView = findViewById(R.id.flight_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, flights, true));
    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final Result mParentActivity;
        private final List<Flight> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        SimpleItemRecyclerViewAdapter(Result parent,
                                      List<Flight> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            try {
                holder.origin.setText(mValues.get(position).getOrigin());
                holder.flightno.setText(mValues.get(position).getCarrierid()+"-"+mValues.get(position).getFlightno());
                holder.deptime.setText(mValues.get(position).getDeptime());
                holder.duration.setText(mValues.get(position).getDuration());
                holder.destination.setText(mValues.get(position).getDestination());
                holder.airline.setText(mValues.get(position).getAirline().replaceAll(" .+$", ""));

                String dtStart = "";
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = format.parse(mValues.get(position).getDepdate());
                    holder.depdate.setText(transformDate(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                holder.arrtime.setText(mValues.get(position).getArrtime());
                holder.origin_return.setText(mValues.get(position).getReturnfl()[0].getOrigin());
                holder.flightcode_return.setText(mValues.get(position).getReturnfl()[0].getCarrierid()+"-"+mValues.get(position).getReturnfl()[0].getFlightno());
                holder.deptime_return.setText(mValues.get(position).getReturnfl()[0].getDeptime());
                holder.duration_return.setText(mValues.get(position).getReturnfl()[0].getDuration());
                holder.destination_return.setText(mValues.get(position).getReturnfl()[0].getDestination());
                holder.airline_return.setText(mValues.get(position).getReturnfl()[0].getAirline().replaceAll(" .+$", ""));

                try {
                    Date date = format.parse(mValues.get(position).getReturnfl()[0].getDepdate());
                    holder.depdate_return.setText(transformDate(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.arrtime_return.setText(mValues.get(position).getReturnfl()[0].getArrtime());
                holder.fare.setText("Comprar " + mValues.get(position).getFare().getTotalfare() + ",00 goCash+");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final TextView origin;
            final TextView flightno;
            final TextView deptime;
            final TextView duration;
            final TextView destination;
            final TextView airline;
            final TextView depdate;
            final TextView arrtime;
            final Button fare;
            final TextView origin_return;
            final TextView flightcode_return;
            final TextView deptime_return;
            final TextView duration_return;
            final TextView destination_return;
            final TextView airline_return;
            final TextView depdate_return;
            final TextView arrtime_return;


            ViewHolder(View view) {
                super(view);
                origin = view.findViewById(R.id.origin);
                flightno = view.findViewById(R.id.flightno);
                deptime = view.findViewById(R.id.deptime);
                duration = view.findViewById(R.id.duration);
                destination = view.findViewById(R.id.destination);
                airline = view.findViewById(R.id.airline);
                depdate = view.findViewById(R.id.depdate);
                arrtime = view.findViewById(R.id.arrtime);
                origin_return = view.findViewById(R.id.origin_return);
                flightcode_return = view.findViewById(R.id.flightno_return);
                deptime_return = view.findViewById(R.id.deptime_return);
                duration_return = view.findViewById(R.id.duration_return);
                destination_return = view.findViewById(R.id.destination_return);
                airline_return = view.findViewById(R.id.airline_return);
                depdate_return = view.findViewById(R.id.depdate_return);
                arrtime_return = view.findViewById(R.id.arrtime_return);
                fare = view.findViewById(R.id.btBuy);

            }
        }

        public String transformDate(Date date){

            Date data =  new Date();
            Locale local = new Locale("pt","BR");
            DateFormat formato = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy",local);

            return formato.format(date);
        }
    }


}
