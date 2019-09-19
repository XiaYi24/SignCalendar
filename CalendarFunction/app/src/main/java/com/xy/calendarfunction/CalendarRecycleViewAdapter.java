package com.xy.calendarfunction;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Xia_焱 on  2019/9/17.
 * 邮箱：XiahaotianV@163.com
 */
public class  CalendarRecycleViewAdapter extends RecyclerView.Adapter<CalendarRecycleViewAdapter.ViewHolder> {


    private Context mContext;
    private ArrayList<DateEntity> mDateEntityList;
    private OnItemListener mItemListener;


    public CalendarRecycleViewAdapter(Context context, ArrayList<DateEntity> dateEntities) {
        mContext = context;
        mDateEntityList = dateEntities;
    }

    @NonNull
    @Override
    public CalendarRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_calendar, viewGroup, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarRecycleViewAdapter.ViewHolder viewHolder, int position) {

        final DateEntity entity = mDateEntityList.get(position);

        if(mItemListener != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onItemClick(entity);
                }
            });
        }

        viewHolder.tv_day.setText(String.valueOf(entity.day));
        if (entity.isNowDate && entity.isSelfMonthDate) {// 如果为当前号数，则设置字体为蓝色粗体
            viewHolder.tv_day.setTextColor(mContext.getResources().getColorStateList(R.drawable.bg_text_black));

        } else if (!entity.isSelfMonthDate) {// 不是本月号数显示灰色
            viewHolder.tv_day.setTextColor(mContext.getResources().getColorStateList(R.drawable.bg_text_gray));
        } else {
            viewHolder.tv_day.setTextColor(mContext.getResources().getColorStateList(R.drawable.bg_text_black));
        }



        if (entity.hasRecord) {//是否有记录
            viewHolder.image_record.setVisibility(View.VISIBLE);
            viewHolder.image_record.setBackgroundResource(R.mipmap.event_date_active);
            viewHolder.image_record_dot.setVisibility(View.VISIBLE);
            viewHolder.tv_day.setTextColor(mContext.getResources().getColor(R.color.white_one));

        } else {
            viewHolder.image_record.setVisibility(View.INVISIBLE);
            viewHolder.image_record_dot.setVisibility(View.INVISIBLE);
            viewHolder.tv_day.setTextColor(mContext.getResources().getColor(R.color.text_one));
        }
    }

    @Override
    public int getItemCount() {
        return mDateEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image_record;
        private TextView tv_day;
        private  ImageView image_record_dot;

        public ViewHolder(View itemView) {
            super(itemView);
            image_record = (ImageView) itemView.findViewById(R.id.image_record);
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            image_record_dot = itemView.findViewById(R.id.image_record_dot);
        }
    }

    interface OnItemListener{
        void onItemClick(DateEntity dateEntity);
    }

    public void setOnItemListener(OnItemListener listener){
        this.mItemListener = listener;
    }
}
