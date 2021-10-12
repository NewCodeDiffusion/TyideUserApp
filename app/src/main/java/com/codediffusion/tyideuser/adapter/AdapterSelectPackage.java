package com.codediffusion.tyideuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.databinding.LayoutSelectPackageBinding;
import com.codediffusion.tyideuser.model.modelSelectPackage;

import java.util.List;

public class AdapterSelectPackage extends RecyclerView.Adapter<AdapterSelectPackage.PackageHolder> {

    private List<modelSelectPackage.Datum>packageList;
    private Context context;

    public SelectPackage selectPackage;

    public interface SelectPackage{
        void SelectPackageData(String PackageId,String PackageName);
    }

    public AdapterSelectPackage(List<modelSelectPackage.Datum> packageList, Context context, SelectPackage selectPackage) {
        this.packageList = packageList;
        this.context = context;
        this.selectPackage = selectPackage;
    }

    @NonNull
    @Override
    public AdapterSelectPackage.PackageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_package,parent,false);

       return new PackageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSelectPackage.PackageHolder holder, int position) {

        modelSelectPackage.Datum model=packageList.get(position);


        holder.binding.tvPackageName.setText(model.getPackageName()+"    "+context.getString(R.string.Rs)+" "+model.getPrice());

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPackage.SelectPackageData(model.getPackageId(), holder.binding.tvPackageName.getText().toString());
            }
        });




    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public class PackageHolder extends RecyclerView.ViewHolder {

        private LayoutSelectPackageBinding binding;

        public PackageHolder(@NonNull View itemView) {
            super(itemView);

            binding= DataBindingUtil.bind(itemView);

            if (binding!=null){

                binding.executePendingBindings();
            }


        }
    }
}
