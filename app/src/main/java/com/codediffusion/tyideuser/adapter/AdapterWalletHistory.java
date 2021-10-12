package com.codediffusion.tyideuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.databinding.LayoutWalletHistoryBinding;
import com.codediffusion.tyideuser.model.modelWalletHistory;
import com.google.android.gms.common.util.DataUtils;

import java.util.List;

public class AdapterWalletHistory extends RecyclerView.Adapter<AdapterWalletHistory.HistoryHolder> {
    private List<modelWalletHistory.WalletHistory>walletHistoryList;
    private Context context;


    public AdapterWalletHistory(List<modelWalletHistory.WalletHistory> walletHistoryList, Context context) {
        this.walletHistoryList = walletHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterWalletHistory.HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wallet_history,parent,false);

       return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterWalletHistory.HistoryHolder holder, int position) {

        modelWalletHistory.WalletHistory model=walletHistoryList.get(position);

        holder.binding.tvDescription.setText(model.getDescription());
        holder.binding.tvDateAndTime.setText(model.getAddDate()+" "+ model.getAddTime());
        if (model.getType().equalsIgnoreCase("ADD")){
            holder.binding.tvAmount.setText("+"+" "+model.getAmount()+" "+context.getString(R.string.Rs));

        }else{
            holder.binding.tvAmount.setText("-"+" "+model.getAmount()+" "+context.getString(R.string.Rs));

        }




    }

    @Override
    public int getItemCount() {
        return walletHistoryList.size();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        private LayoutWalletHistoryBinding binding;
        public HistoryHolder(@NonNull View itemView) {
            super(itemView);


            binding= DataBindingUtil.bind(itemView);

            if (binding!=null){
                binding.executePendingBindings();
            }
        }
    }
}
