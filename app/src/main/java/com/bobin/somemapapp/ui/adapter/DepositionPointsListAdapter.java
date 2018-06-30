package com.bobin.somemapapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPartner;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.ui.holder.DepositionPointViewHolder;
import com.bobin.somemapapp.utils.GoogleMapUtils;

import java.util.List;

public class DepositionPointsListAdapter extends RecyclerView.Adapter<DepositionPointViewHolder> {
    private MapCoordinates userLocation;
    private PointClickListener clickListener;
    private List<BindData> data;

    @NonNull
    @Override
    public DepositionPointViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                        int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                DepositionPointViewHolder.layoutId,
                parent,
                false);
        return new DepositionPointViewHolder(view).withClickListener(clickListener);
    }

    public void setClickListener(PointClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull DepositionPointViewHolder holder,
                                 int position) {
        BindData data = getData(position);
        int meters = -1;
        if (userLocation != null)
            meters = (int) GoogleMapUtils.distanceBetween(userLocation, data.point.getMapCoordinates());
        holder.bind(data, position, meters);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private BindData getData(int position) {
        return data.get(position);
    }

    public void setDataset(List<BindData> data, MapCoordinates userLocation) {
        this.data = data;
        this.userLocation = userLocation;
        notifyDataSetChanged();
    }

    public void updateElement(BindData data, int position) {
        this.data.set(position, data);
        notifyItemChanged(position);
    }

    public interface PointClickListener {
        void onClickPoint(DepositionPoint point, View iconView, int position);
    }

    public static class BindData {
        private DepositionPoint point;
        private boolean watched;
        private String icon;
        private String partnerName;

        public BindData(DepositionPoint point, boolean watched, DepositionPartner partner) {
            this.point = point;
            this.watched = watched;
            this.icon = partner.getFullPictureUrl();
            this.partnerName = partner.getName();
        }

        public DepositionPoint getPoint() {
            return point;
        }

        public boolean isWatched() {
            return watched;
        }

        public String getIcon() {
            return icon;
        }

        public String getPartnerName() {
            return partnerName;
        }

        public void setWatched(boolean watched) {
            this.watched = watched;
        }
    }
}
