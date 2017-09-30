package com.shiming.jsoupjianshu;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;


/**
 * @author shiming
 * @version v1.0 create at 2017/9/29
 * @des
 */
public class JianShuAdapter extends RecyclerView.Adapter {

    public Context mContext;
    private  ArrayList<JianShuDataBean> mData;
    private onClick mClick;

    public JianShuAdapter(Context context, ArrayList<JianShuDataBean> dataBeen) {
        if (dataBeen==null){
            mData = new ArrayList<>();
        }else {
            mData=dataBeen;
        }
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        final JianShuDataBean jianShuDataBean = mData.get(position);
        myViewHolder.mAuthorName.setText(jianShuDataBean.mAuthorName);
        myViewHolder.mContent.setText(jianShuDataBean.mContent);
        myViewHolder.mCreateTime.setText(jianShuDataBean.mTime);
        myViewHolder.mHommuchRead.setText(jianShuDataBean.mReadNum);
        myViewHolder.mSayWord.setText(jianShuDataBean.mTalkNum);
        myViewHolder.mPersonLike.setText(jianShuDataBean.mLikeNum);
        myViewHolder.mTitle.setText(jianShuDataBean.mTitle);

        Glide.with(mContext)
                .load("http:"+jianShuDataBean.mAvatarImg)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(myViewHolder.mAvatar);
        if (jianShuDataBean.mPrimaryImg.equals(jianShuDataBean.mAvatarImg)){
            myViewHolder.mPrimaryImg.setVisibility(View.GONE);
        }else {
            myViewHolder.mPrimaryImg.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load("http:" + jianShuDataBean.mPrimaryImg)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(myViewHolder.mPrimaryImg);
        }
        myViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClick!=null){
                    mClick.onClick(view,jianShuDataBean);
                }
            }
        });
    }
    public interface  onClick{
        void  onClick(View view,JianShuDataBean bean);
    }
    public void setClick(onClick click){
        mClick = click;
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(ArrayList<JianShuDataBean> dataBeanArray) {
        mData.clear();
        mData.addAll(dataBeanArray);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView mAvatar;
        public ImageView mPrimaryImg;
        public TextView mAuthorName;
        public TextView mCreateTime;
        public TextView mTitle;
        public TextView mContent;
        public TextView mHommuchRead;
        public TextView mSayWord;
        public TextView mPersonLike;
        private  CardView mCardView;

        MyViewHolder(View view) {
            super(view);
            mCardView = view.findViewById(R.id.card_view);
            mAvatar = view.findViewById(R.id.iv_avatar);
            mPrimaryImg = view.findViewById(R.id.iv_primary);
            mAuthorName = view.findViewById(R.id.tv_author);
            mCreateTime = view.findViewById(R.id.tv_time);
            mTitle = view.findViewById(R.id.tv_title);
            mContent = view.findViewById(R.id.tv_content);
            mHommuchRead = view.findViewById(R.id.tv_read);
            mSayWord = view.findViewById(R.id.tv_talk);
            mPersonLike = view.findViewById(R.id.tv_like);
        }
    }
}
