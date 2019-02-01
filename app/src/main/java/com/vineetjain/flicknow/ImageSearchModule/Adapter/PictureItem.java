package com.vineetjain.flicknow.ImageSearchModule.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.vineetjain.flicknow.ImageSearchModule.Model.ImageListModel;
import com.vineetjain.flicknow.R;
import com.vineetjain.flicknow.databinding.PictureItemBinding;

import java.util.ArrayList;

import Utils.DateConvertUtils;

public class PictureItem extends RecyclerView.Adapter<PictureItem.MyViewHolder> {

    private ArrayList<ImageListModel.SingleIImagetemModel> itemsList;
    private Context mContext;

    public PictureItem(Context context, ArrayList<ImageListModel.SingleIImagetemModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private PictureItemBinding pictureItemBinding;

        TextView phot_title,phot_description,phot_date,phot_author;
        ImageView phot_flick;

        MyViewHolder(PictureItemBinding pictureItemBinding) {
            super(pictureItemBinding.getRoot());
            this.pictureItemBinding = pictureItemBinding;

            this.phot_flick=(ImageView)pictureItemBinding.getRoot().findViewById(R.id.phot_flick);
            /*this.phot_title = (TextView) view.findViewById(R.id.title);
            this.phot_description = (TextView) view.findViewById(R.id.description);
            this.phot_author = (TextView) view.findViewById(R.id.author);
            this.phot_date = (TextView) view.findViewById(R.id.dateTaken);
            this.phot_flick=(ImageView)view.findViewById(R.id.phot_flick);*/
        }

        public void bind(ImageListModel.SingleIImagetemModel singleItem) {
            this.pictureItemBinding.setViewModel(singleItem);
            this.pictureItemBinding.executePendingBindings();
        }
    }

    public void setListData(ArrayList<ImageListModel.SingleIImagetemModel> itemsList) {
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }

    @Override
    public PictureItem.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item, parent, false);
        PictureItemBinding pictureItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.picture_item, parent, false);
        return new PictureItem.MyViewHolder(pictureItemBinding);
    }


    @Override
    public void onBindViewHolder(final PictureItem.MyViewHolder holder, int i) {

        ImageListModel.SingleIImagetemModel singleItem = itemsList.get(i);
       // holder.pictureItemBinding.dateTaken.setText(TextUtils.isEmpty(singleItem.getDate_taken())?"":DateConvertUtils.formatDate("yyyy-MM-dd'T'HH:mm:ss-hh:mm","MMM dd yyyy",singleItem.getDate_taken()));
        holder.bind(singleItem);
        // holder.phot_author.setText(TextUtils.isEmpty(singleItem.getAuthor())?"":singleItem.getAuthor());
       // holder.phot_title.setText(TextUtils.isEmpty(singleItem.getTitle())?"":singleItem.getTitle());

        // html content parsing is working but content is not proper
        //holder.phot_description.setText(Html.fromHtml(singleItem.getDescription()));
        //holder.phot_description.setVisibility(View.GONE);

       /* if (!TextUtils.isEmpty(singleItem.getMedia().getM())) {
            Glide.with(holder.phot_flick.getContext()).applyDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.no_image))
                    .load(singleItem.getMedia().getM())
                    .apply(RequestOptions.fitCenterTransform())
                    .into(holder.phot_flick);
            //Picasso.with(mContext).load(singleItem.getMedia().getM()).placeholder(R.drawable.no_image).into(holder.phot_flick);
        } else {
            holder.phot_flick.setImageResource(R.drawable.no_image);
        }*/
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }
}
