package mateus.andrade.milhas.Activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import mateus.andrade.milhas.Models.Flight;
import mateus.andrade.milhas.R;

/**
 * Created by mateusandrade on 01/01/2018.
 */

public class Result extends AppCompatActivity implements View.OnClickListener {

    List<Flight> flights;
    List<String> airlines;
    List<String> airlinesToFilter;
    @BindView(R.id.backButton) ImageView backButton;
    @BindView(R.id.filter) LinearLayout filter;
    @BindView(R.id.order) LinearLayout order;
    Boolean[] selectedItems;

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

        backButton.setOnClickListener(this);
        filter.setOnClickListener(this);
        order.setOnClickListener(this);

        View recyclerView = findViewById(R.id.flight_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        getAllAirlines();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backButton:
                finish();
            break;

            case R.id.filter:

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        airlines );

                final AlertDialog filterdialog = new AlertDialog.Builder(this)
                        .setTitle("Filtrar por companhia")
                        .setAdapter(arrayAdapter, null)
                        .setNegativeButton(getResources().getString(android.R.string.cancel), null)
                        .create();

                filterdialog.getListView().setItemsCanFocus(false);
                filterdialog.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                filterdialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        filterByAirline(airlines.get(position));
                        filterdialog.dismiss();
                    }
                });

                filterdialog.show();

                break;

            case R.id.order:

                List<String> order_array_list = new ArrayList<String>();
                order_array_list.add(" ↓ Menor Preço ");
                order_array_list.add(" ↑ Maior Preço ");

                ArrayAdapter<String> orderarrayAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        order_array_list );

                final AlertDialog orderdialog = new AlertDialog.Builder(this)
                        .setTitle("Ordenar por Preço")
                        .setAdapter(orderarrayAdapter, null)
                        .setNegativeButton(getResources().getString(android.R.string.cancel), null)
                        .create();

                orderdialog.getListView().setItemsCanFocus(false);
                orderdialog.getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                orderdialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if(position==0) {
                            orderByPriceAscedent();
                        }else{
                            orderByPriceDescedent();
                        }
                        orderdialog.dismiss();
                    }
                });

                orderdialog.show();
                break;

        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, flights, true));
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Flight> f) {

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, f, true));
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

    public void getAllAirlines(){

        Set<String> companies = new HashSet<String>();
        for (int i = 0; i< flights.size(); i++){
            companies.add(flights.get(i).getAirline());
        }
        airlines = new ArrayList<String>();
        airlines.addAll(companies);
        airlinesToFilter = airlines;
    }

    public void orderByPriceAscedent(){

        Collections.sort(flights, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Flight p1 = (Flight) o1;
                Flight p2 = (Flight) o2;
                return Integer.valueOf(p1.getFare().getTotalfare()).compareTo(Integer.valueOf(p2.getFare().getTotalfare()));
            }
        });
        View recyclerView = findViewById(R.id.flight_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    public void orderByPriceDescedent(){

        Collections.sort(flights, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Flight p1 = (Flight) o1;
                Flight p2 = (Flight) o2;
                return Integer.valueOf(p2.getFare().getTotalfare()).compareTo(Integer.valueOf(p1.getFare().getTotalfare()));
            }
        });
        View recyclerView = findViewById(R.id.flight_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    public void filterByAirline(String airlineName){

        airlinesToFilter = new ArrayList<String>();
        airlinesToFilter.add(airlineName);

        List<Flight> filtered = Lists.newArrayList();
        for(Flight p : flights) {
            if(airlinesToFilter.contains(p.getAirline())) {
                filtered.add(p);
            }
        }

        View recyclerView = findViewById(R.id.flight_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView, filtered);

    }

}
