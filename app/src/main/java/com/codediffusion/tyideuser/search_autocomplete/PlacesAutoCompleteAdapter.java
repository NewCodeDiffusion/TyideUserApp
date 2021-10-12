package com.codediffusion.tyideuser.search_autocomplete;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.codediffusion.tyideuser.R;

import java.util.ArrayList;
import java.util.List;


public class PlacesAutoCompleteAdapter extends ArrayAdapter<Prediction> {
    private Context context;
    public static final String ADDRESS_CLICK="AddressClick";
    public static String PICKED_ADDRESS="";
    private List<Prediction> predictions;
    public String information;


    public PlacesAutoCompleteAdapter(Context context, List<Prediction> predictions) {
        super(context, R.layout.place_row_layout, predictions);
        this.context = context;
        this.predictions = predictions;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_row_layout, null);
        if (predictions != null && predictions.size() > 0) {
            final Prediction prediction = predictions.get(position);
            TextView textViewName = view.findViewById(R.id.textViewName);
            textViewName.setText(prediction.getDescription());
            Intent intent=new Intent(ADDRESS_CLICK);
            intent.putExtra(PICKED_ADDRESS,true);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            Log.d("PredictionValue",prediction.getDescription());
            Log.d("types",""+prediction.getTypes());



        }
        return view;
    }



    @NonNull
    @Override
    public Filter getFilter() {
        return new PlacesAutoCompleteFilter(this, context);
    }

   // public static Place place;
    private class PlacesAutoCompleteFilter extends Filter {
        private PlacesAutoCompleteAdapter placesAutoCompleteAdapter;
        private Context context;

        public PlacesAutoCompleteFilter(PlacesAutoCompleteAdapter placesAutoCompleteAdapter, Context context) {
            super();
            this.placesAutoCompleteAdapter = placesAutoCompleteAdapter;
            this.context = context;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            try {
                placesAutoCompleteAdapter.predictions.clear();
                FilterResults filterResults = new FilterResults();
                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.values = new ArrayList<Prediction>();
                    filterResults.count = 0;

                } else {
                    GoogleMapAPI googleMapAPI = APIClient.getClient().create(GoogleMapAPI.class);
                    Predictions predictions = googleMapAPI.getPlacesAutoComplete(charSequence.toString(),  "geocode", "en", "country:in","AIzaSyC76Zr-75WbdmPZHnFmf0aRPgootI3McOo").execute().body();
                    filterResults.values = predictions.getPredictions();
                    filterResults.count = predictions.getPredictions().size();
                  //  UtilityFunction.filterAddress()
                   // Log.d("ApiResponse",filterResults.values.toString());
                }

                return filterResults;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
           try {
               placesAutoCompleteAdapter.predictions.clear();
               placesAutoCompleteAdapter.predictions.addAll((List<Prediction>) filterResults.values);
               placesAutoCompleteAdapter.notifyDataSetChanged();
           }catch (Exception e)
           {
               Log.d("PlaceAdapter",e.toString());
               e.printStackTrace();
           }


        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Prediction prediction = (Prediction) resultValue;
          //  Constants.prediction= (Prediction) resultValue;
           Log.d("PredictionValue",prediction.getPlaceId());

            return prediction.getDescription();
        }
    }
}
