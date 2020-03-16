package com.kakyireinc.covid_19.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.kakyireinc.covid_19.R;
import com.kakyireinc.covid_19.models.NationsCases;
import com.kakyireinc.covid_19.models.WorldCases;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecylerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int NATIVEAD_VIEWTYPE = 0;
    private final int NATIONS_MODEL_VIEWTYPE = 1;
    private final int WORLD_MODEL_VIEWTYPE = 2;

    private Context context;
    private List<Object> list;


    public RecylerAdapter(Context context, List<Object> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case NATIVEAD_VIEWTYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_unified, parent, false);
                return new NationsViewHolder(view);

            case NATIONS_MODEL_VIEWTYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nations_holder, parent, false);
                return new NationsViewHolder(view);

            case WORLD_MODEL_VIEWTYPE:
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.world_holder, parent, false);
                return new WorldViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewtype = getItemViewType(position);


        //binding items to various views
        switch (viewtype) {

            case NATIONS_MODEL_VIEWTYPE:
                NationsViewHolder nationsViewHolder = (NationsViewHolder) holder;
                NationsCases nationsCases = (NationsCases) list.get(position);

                nationsViewHolder.nationName.setText(nationsCases.getCountry());
                nationsViewHolder.nationCase.setText(nationsCases.getCases());
                nationsViewHolder.nationNewCase.setText(nationsCases.getTodayCases());
                nationsViewHolder.nationDeath.setText(nationsCases.getDeaths());
                nationsViewHolder.nationNewDeath.setText(nationsCases.getTodayDeaths());
                nationsViewHolder.nationRecovered.setText(nationsCases.getRecovered());
                nationsViewHolder.nationSerious.setText(nationsCases.getCritical());
                nationsViewHolder.nationActive.setText(nationsCases.getActive());


                break;


            case NATIVEAD_VIEWTYPE:
                UnifiedNativeAd unifiedNativeAd = (UnifiedNativeAd) list.get(position);
                PopulateAd(unifiedNativeAd, ((NativeAdViewHolder) holder).getAdView());


                break;


            case WORLD_MODEL_VIEWTYPE:
                WorldViewHolder worldViewHolder = (WorldViewHolder) holder;
                WorldCases world = (WorldCases) list.get(position);


                worldViewHolder.worldDeath.setText(world.getDeaths());
                worldViewHolder.worldRecoverd.setText(world.getRecovered());
                worldViewHolder.worldCase.setText(world.getCases());

                break;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {


        Object bothList = list.get(position);
        if (bothList instanceof UnifiedNativeAd) {
            return NATIVEAD_VIEWTYPE;
        } else if (bothList instanceof NationsCases) {
            return NATIONS_MODEL_VIEWTYPE;
        } else
            return WORLD_MODEL_VIEWTYPE;

    }


    //viewholder class for NationsModel
    public class NationsViewHolder extends RecyclerView.ViewHolder {

        TextView nationName, nationDeath, nationRecovered, nationActive,
                nationSerious, nationNewCase, nationNewDeath, nationCase;

        public NationsViewHolder(@NonNull View itemView) {
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

    //viewholder class for WorldModel
    public class WorldViewHolder extends RecyclerView.ViewHolder {

        TextView worldCase, worldRecoverd, worldDeath;

        public WorldViewHolder(@NonNull View itemView) {
            super(itemView);

            worldCase = itemView.findViewById(R.id.world_cases);
            worldRecoverd = itemView.findViewById(R.id.world_recovered);
            worldDeath = itemView.findViewById(R.id.world_deaths);


        }
    }


    //viewholder class for NativeAd
    public class NativeAdViewHolder extends RecyclerView.ViewHolder {

        private UnifiedNativeAdView adView;

        public UnifiedNativeAdView getAdView() {
            return adView;
        }

        public NativeAdViewHolder(@NonNull View itemView) {
            super(itemView);

            adView = (UnifiedNativeAdView) itemView.findViewById(R.id.ad_view);

            adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

            // Register the view used for each individual asset.
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        }
    }


    //populating the native ad
    private void PopulateAd(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {


        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());


        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);

        }


        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
            adView.getPriceView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            adView.getStoreView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

    }
}
