package com.xy.calendarfunction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xia_焱 on  2019/9/18.
 * 邮箱：XiahaotianV@163.com
 * 签到天数
 */
public class SignAwardListAdapter extends RecyclerView.Adapter<SignAwardListAdapter.ViewHoldX> {

    private List<String> mList;
    private Context mContext;

    public SignAwardListAdapter(List<String> listX, Context context) {
        this.mList = listX;
        this.mContext = context;
    }

    @NonNull
    @Override
    public SignAwardListAdapter.ViewHoldX onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_sign_award_list, parent, false);
        ViewHoldX viewHoldX = new ViewHoldX(inflate);
        return viewHoldX;
    }

    @Override
    public void onBindViewHolder(@NonNull SignAwardListAdapter.ViewHoldX holder, int position) {
        //（list.size()-1)
        if (position==(mList.size()-1)){
            holder.tvLine.setVisibility(View.GONE);
        }else {
            holder.tvLine.setVisibility(View.VISIBLE);
        }

        //伪数据状态
        if (position==3){
            holder.ivGift.setVisibility(View.VISIBLE);
            holder.tvIntegral.setVisibility(View.GONE);
            holder.tvDayName.setText("第4天");
        }else {
            holder.ivGift.setVisibility(View.GONE);
            holder.tvIntegral.setVisibility(View.VISIBLE);
            holder.tvDayName.setText(position+1+"");
        }

        if (position>3){
            holder.llBg.setBackgroundResource(R.mipmap.icon_qt_sign_bb);
        }else {
            holder.llBg.setBackgroundResource(R.mipmap.icon_qt_sign_aa);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHoldX extends RecyclerView.ViewHolder {


        TextView tvDayName;
        TextView tvLine;
        ImageView ivGift;
        LinearLayout llBg;
        TextView tvIntegral;

        public ViewHoldX(View itemView) {
            super(itemView);
            tvDayName = itemView.findViewById(R.id.tv_day_name);
            tvLine = itemView.findViewById(R.id.tv_line);
            ivGift = itemView.findViewById(R.id.iv_gift);
            llBg = itemView.findViewById(R.id.ll_bg);
            tvIntegral = itemView.findViewById(R.id.tv_integral);
        }
    }
}
