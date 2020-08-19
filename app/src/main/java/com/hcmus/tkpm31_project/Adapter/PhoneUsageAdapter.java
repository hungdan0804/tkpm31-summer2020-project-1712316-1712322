package com.hcmus.tkpm31_project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.tkpm31_project.Object.PhoneUsage;
import com.hcmus.tkpm31_project.R;
import com.hcmus.tkpm31_project.Util.UtilPhoneUsage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PhoneUsageAdapter extends RecyclerView.Adapter<PhoneUsageAdapter.ViewHolder> {
    Context context;
    ArrayList<PhoneUsage> listApp;

    public PhoneUsageAdapter(Context context, ArrayList<PhoneUsage> listApp) {
        this.context = context;
        this.listApp = listApp;
    }

    @NonNull
    @Override
    public PhoneUsageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // gán view
        View view = LayoutInflater.from(context).inflate(R.layout.usage_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneUsageAdapter.ViewHolder holder, int position) {
        // Gán dữ liêu
        PhoneUsage appUsage = listApp.get(position);
        holder.txtAppName.setText(appUsage.get_nameApp());
        String usage= UtilPhoneUsage.getDurationBreakdown(appUsage.get_time());
        holder.txtAppUsage.setText(usage);
        holder.imgApp.setImageDrawable(appUsage.get_thumbnail());
        holder.txtNum.setText(position);
        Locale locale = new Locale("vn", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

    }

    @Override
    public int getItemCount() {
        return listApp.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgApp;
        TextView txtAppName,txtAppUsage,txtNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgApp = itemView.findViewById(R.id.imgApp);
            txtAppName = itemView.findViewById(R.id.txtAppName);
            txtAppUsage = itemView.findViewById(R.id.txtAppUsage);
            txtNum=itemView.findViewById(R.id.txtNum);
        }
    }
}
