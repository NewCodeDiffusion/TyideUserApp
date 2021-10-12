package com.codediffusion.tyideuser.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.databinding.LayoutAllbookingBinding;
import com.codediffusion.tyideuser.databinding.LayoutCompleteBookingBinding;
import com.codediffusion.tyideuser.model.modelAllBooking;
import com.codediffusion.tyideuser.model.modelUserBookingData;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;

public class AdapterCompleteBooking extends RecyclerView.Adapter<AdapterCompleteBooking.ComplainsHolder> {
    private List<modelUserBookingData.Datum>complainsList;
    private Context context;

   private String Rating="1.0";
    public ChangeStatus changeStatus;
    public interface ChangeStatus{
        void UpdateStatus();
    }


    public AdapterCompleteBooking(List<modelUserBookingData.Datum> complainsList, Context context) {
        this.complainsList = complainsList;
        this.context = context;

    }

    @NonNull
    @Override
    public ComplainsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_complete_booking,parent,false);

       return new ComplainsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCompleteBooking.ComplainsHolder holder, int position) {

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

       holder.binding.tvReview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openReviewDialog();
           }
       });


    }



    @Override
    public int getItemCount() {
        return complainsList.size();
    }

    public class ComplainsHolder extends RecyclerView.ViewHolder {
        private LayoutCompleteBookingBinding binding;
        public ComplainsHolder(@NonNull View itemView) {
            super(itemView);

            binding= DataBindingUtil.bind(itemView);

            if (binding!=null){
                binding.executePendingBindings();
            }


        }


    }
    public void openReviewDialog() {
        final Dialog dialog = new Dialog(context); // Context, this, etc.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_review_rating);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        EditText et_review=dialog.findViewById(R.id.et_review);
        Button btn_submitReview=dialog.findViewById(R.id.btn_submitReview);

/////////////////RatingBar//////////////
        //  ScaleRatingBar ratingBar = new ScaleRatingBar(SingleOrderDetailsActivity.this);
        ScaleRatingBar simpleRatingBar=dialog.findViewById(R.id.simpleRatingBar);


        //  Toast.makeText(context, Rating+"Rating", Toast.LENGTH_SHORT).show();




        dialog.setCanceledOnTouchOutside(true);

        dialog.show();


        btn_submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(context, "Review Submit SuccessFully", Toast.LENGTH_SHORT).show();

                if (!et_review.getText().toString().isEmpty()){
                    String Comment=et_review.getText().toString();

                    Toast.makeText(context, "Review Submit SuccessFully", Toast.LENGTH_SHORT).show();


                    //SubmitReviewApi(Comment);
                    dialog.dismiss();
                }else{

                    Toast.makeText(context, "Please Enter Comment", Toast.LENGTH_SHORT).show();

                }




            }
        });



        simpleRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                Log.e("jdfkjgk","onRatingChange: " + rating);

                Rating= String.valueOf(rating);

                // Toast.makeText(context, Rating+"Rating", Toast.LENGTH_SHORT).show();
            }


        });

    }


}
