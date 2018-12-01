package com.example.android.fittracker;

import android.content.Context;
import android.database.Cursor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.fittracker.data.FittrackerTableHandler;

public class FittrackerAdapter extends RecyclerView.Adapter<FittrackerAdapter.FittrackerViewHolder>{
    private static final String TAG = FittrackerAdapter.class.getSimpleName();
    private Context mContext;
    Cursor mCursor;
    int mPosition;
    private ClickListener clickListener;


    public FittrackerAdapter(Context context, Cursor cursor, ClickListener listener) {
        this.mContext = context;
        this.mCursor = cursor;
        this.clickListener = listener;
    }

    public class FittrackerViewHolder extends RecyclerView.ViewHolder  {
        public TextView date, type, distance, duration, calorie;

        public FittrackerViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.list_date);
            type = (TextView) itemView.findViewById(R.id.list_type);
            distance = (TextView) itemView.findViewById(R.id.list_distance);
            duration = (TextView) itemView.findViewById(R.id.list_duration);
            calorie = (TextView) itemView.findViewById(R.id.list_calorie);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    view.setBackgroundColor(mContext.getResources().
                            getColor(R.color.Pink));
                    clickListener.onItemClicked(view, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // send selected contact in callback
                    view.setBackgroundColor(mContext.getResources().
                            getColor(R.color.Pink));
                    clickListener.onItemLongClicked(view, getAdapterPosition());
                    return true;
                }
            });

        }



    }

    public interface ClickListener {
        public void onItemClicked(View view, int position);
        public boolean onItemLongClicked(View view, int position);
    }


    public FittrackerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tracker, parent, false);
        return new FittrackerViewHolder(view);
    }

    public void onBindViewHolder(FittrackerViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.date.setText(mCursor.getString(mCursor.getColumnIndex(FittrackerTableHandler.Date)));
        holder.type.setText(mCursor.getString(mCursor.getColumnIndex(FittrackerTableHandler.Type)));
        holder.distance.setText(mCursor.getString(mCursor.getColumnIndex(FittrackerTableHandler.Distance)));
        holder.duration.setText(mCursor.getString(mCursor.getColumnIndex(FittrackerTableHandler.Duration)));
        holder.calorie.setText(mCursor.getString(mCursor.getColumnIndex(FittrackerTableHandler.Calorie)));
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
    public void setData(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

}