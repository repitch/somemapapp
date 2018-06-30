package com.bobin.somemapapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bobin.somemapapp.model.MapCoordinates;
import com.bobin.somemapapp.model.tables.DepositionPoint;
import com.bobin.somemapapp.ui.holder.DepositionPointViewHolder;
import com.bobin.somemapapp.utils.GoogleMapUtils;

import java.util.List;

public class DepositionPointsListAdapter extends RecyclerView.Adapter<DepositionPointViewHolder> {
    private MapCoordinates userLocation;
    private PointClickListener clickListener;
    private List<BindData> data;
    private int lastPosition = -1;

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
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private BindData getData(int position) {
        return data.get(position);
    }

    public void setDataset(List<BindData> data, MapCoordinates userLocation) {
        lastPosition = -1;
        this.data = data;
        this.userLocation = userLocation;
        notifyDataSetChanged();
    }

    public void updateElement(BindData data, int position) {
        this.data.set(position, data);
        notifyItemChanged(position);
    }

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            view.setAlpha(0);
            view.animate().setDuration(300).alpha(1);
            lastPosition = position;
        }
    }

    public interface PointClickListener {
        void onClickPoint(DepositionPoint point, View iconView, int position);
    }

    public static class BindData {
        private DepositionPoint point;
        private boolean watched;
        private String icon;
        private String partnerName;

        public BindData(DepositionPoint point, boolean watched, String partnerName, String icon) {
            this.point = point;
            this.watched = watched;
            this.icon = icon;
            this.partnerName = partnerName;
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
