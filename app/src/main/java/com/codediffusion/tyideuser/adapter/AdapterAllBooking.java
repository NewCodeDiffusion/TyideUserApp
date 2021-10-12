package com.codediffusion.tyideuser.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.databinding.LayoutAllbookingBinding;
import com.codediffusion.tyideuser.model.modelUserBookingData;

import java.util.List;

public class AdapterAllBooking extends RecyclerView.Adapter<AdapterAllBooking.ComplainsHolder> {
    private List<modelUserBookingData.Datum>complainsList;
    private Context context;


    public ChangeStatus changeStatus;
    public interface ChangeStatus{
        void UpdateStatus();
    }


    public AdapterAllBooking(List<modelUserBookingData.Datum> complainsList, Context context) {
        this.complainsList = complainsList;
        this.context = context;

    }

    @NonNull
    @Override
    public ComplainsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_allbooking,parent,false);

       return new ComplainsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAllBooking.ComplainsHolder holder, int position) {

       // modelAllBooking.CustomerDetails model=complainsList.get(position);

       holder.binding.tvCustomerCode.setText(complainsList.get(position).getBookingId());
        holder.binding.tvDate.setText(complainsList.get(position).getTripType());
        holder.binding.tvCustomerNAme.setText(complainsList.get(position).getName());
        holder.binding.tvCustomerMobile.setText(complainsList.get(position).getMobile());
        holder.binding.tvAddress.setText(complainsList.get(position).getFromPlace());
        holder.binding.tvDescription.setText(complainsList.get(position).getToPlace());
        holder.binding.tvPaymentType.setText("Online");
        holder.binding.tvTotalAmount.setText(context.getString(R.string.Rs)+" "+complainsList.get(position).getTotalBookingAmount());
        holder.binding.tvStatusType.setText(complainsList.get(position).getBookingStatus());

        //holder.binding.tvReturnDate.setText(complainsList.get(position).getReturnDate());

        if (!complainsList.get(position).getReturnDate().equalsIgnoreCase("")){
            holder.binding.tvReturen.setVisibility(View.VISIBLE);
            holder.binding.tvReturnDate.setVisibility(View.VISIBLE);
            holder.binding.tvReturnDate.setText(complainsList.get(position).getReturnDate());

            Log.e("dsjfhfj","Not Empty");
        }else{
            Log.e("dsjfhfj","Empty");

            holder.binding.tvReturen.setVisibility(View.GONE);
            holder.binding.tvReturnDate.setVisibility(View.GONE);
        }
        holder.binding.tvPickupTime.setText(complainsList.get(position).getDepartureDate());
        holder.binding.tvCarName.setText(complainsList.get(position).getCarName());




    }



    @Override
    public int getItemCount() {
        return complainsList.size();
    }

    public class ComplainsHolder extends RecyclerView.ViewHolder {
        private LayoutAllbookingBinding binding;
        public ComplainsHolder(@NonNull View itemView) {
            super(itemView);

            binding= DataBindingUtil.bind(itemView);

            if (binding!=null){
                binding.executePendingBindings();
            }


        }


    }


}
