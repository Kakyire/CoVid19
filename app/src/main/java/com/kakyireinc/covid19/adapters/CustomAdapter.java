package com.kakyireinc.covid19.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kakyireinc.covid19.R;
import com.kakyireinc.covid19.models.NationsCases;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.NationHoler> {

    Context context;
    List<NationsCases> list;

    public CustomAdapter(Context context, List<NationsCases> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NationHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nations_holder,
                parent, false);
        return new NationHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NationHoler holder, int position) {

        holder.nationName.setText(list.get(position).getCountry());
        holder.nationCase.setText(list.get(position).getCases().toString());
        holder.nationNewCase.setText(list.get(position).getTodayCases().toString());
        holder.nationDeath.setText(list.get(position).getDeaths().toString());
        holder.nationNewDeath.setText(list.get(position).getTodayDeaths().toString());
        holder.nationRecovered.setText(list.get(position).getRecovered().toString());
        holder.nationSerious.setText(list.get(position).getCritical().toString());
        holder.nationActive.setText(list.get(position).getActive().toString());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NationHoler extends RecyclerView.ViewHolder {
        TextView nationName, nationDeath, nationRecovered, nationActive,
                nationSerious, nationNewCase, nationNewDeath, nationCase;


        public NationHoler(@NonNull View itemView) {
            super(itemView);

            nationName = itemView.findViewById(R.id.nation_name);
            nationCase = itemView.findViewById(R.id.nation_cases);
            nationDeath = itemView.findViewById(R.id.nation_deaths);
            nationNewCase = itemView.findViewById(R.id.nation_new_cases);
            nationNewDeath = itemView.findViewById(R.id.nation_new_deaths);
            nationRecovered = itemView.findViewById(R.id.nation_recovered);
            nationActive = itemView.findViewById(R.id.nation_active_cases);
            nationSerious = itemView.findViewById(R.id.nation_critical);

        }
    }
}
