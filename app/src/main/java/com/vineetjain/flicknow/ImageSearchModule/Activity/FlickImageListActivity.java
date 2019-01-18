package com.vineetjain.flicknow.ImageSearchModule.Activity;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vineetjain.flicknow.ImageSearchModule.Adapter.PictureItem;
import com.vineetjain.flicknow.ImageSearchModule.Model.ImageListModel;
import com.vineetjain.flicknow.ImageSearchModule.ViewModel.PhotoListVM;
import com.vineetjain.flicknow.R;
import com.vineetjain.flicknow.databinding.ActivityFlickImageListBinding;

import java.util.ArrayList;

import Base.BaseActivity;

public class FlickImageListActivity extends BaseActivity implements LifecycleObserver {

    private PhotoListVM photoListVM;
    private PictureItem pictureItemAdapter;
    ActivityFlickImageListBinding activityFlickImageListBinding;
    private ArrayList<ImageListModel.SingleIImagetemModel> imgListModel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flick_image_list);

        activityFlickImageListBinding = DataBindingUtil.setContentView(this, R.layout.activity_flick_image_list);
        activityFlickImageListBinding.setViewModel(new PhotoListVM());
        activityFlickImageListBinding.executePendingBindings();

           RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            pictureItemAdapter = new PictureItem(this, imgListModel);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(pictureItemAdapter);

        photoListVM = ViewModelProviders.of(this).get(PhotoListVM.class);

        photoListVM.photoReceivedResponse().observe(this, new Observer<Object>() {
            @Override
            public void onChanged(@Nullable Object genericRespModel) {
                HandleOnChanged(genericRespModel);
            }
        });

        showProgressdialog("loading",true); // start loading feed as soon as app is launched
        hideSoftKeyboard();
        photoListVM.getDatafromAPI(activityFlickImageListBinding.searchSrcText.getText().toString(), FlickImageListActivity.this);

        // detect touch on search icon placed with search Edit text & send query
        activityFlickImageListBinding.searchSrcText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (activityFlickImageListBinding.searchSrcText.getRight() - activityFlickImageListBinding.searchSrcText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if(!TextUtils.isEmpty(activityFlickImageListBinding.searchSrcText.getText())) {
                            showProgressdialog("loading", true);
                            hideSoftKeyboard();
                            photoListVM.getDatafromAPI(activityFlickImageListBinding.searchSrcText.getText().toString(), FlickImageListActivity.this);
                        }
                        else
                            Toast.makeText(FlickImageListActivity.this,getResources().getString(R.string.no_search_text),Toast.LENGTH_SHORT).show();

                        return true;
                    }
                }
                return false;
            }
        });

    }

    public void HandleOnChanged(Object genericRespModel)
    {
        activityFlickImageListBinding.noOperators.setVisibility(View.GONE);
        pictureItemAdapter.setListData(imgListModel); // if response resulted in zero items & then current list should become empty
        hideProgressDialog();
        hideSoftKeyboard();
        if(genericRespModel instanceof ImageListModel) {
            if(((ImageListModel)genericRespModel).getCode()!=0)
            {
                // error condition from flickr
                Toast.makeText(FlickImageListActivity.this,((ImageListModel)genericRespModel).getMessage(),Toast.LENGTH_SHORT).show();
            }
            else {
                if (((ImageListModel) genericRespModel).getItems().size() > 0)
                    pictureItemAdapter.setListData(((ImageListModel) genericRespModel).getItems());
                else {
                    activityFlickImageListBinding.noOperators.setVisibility(View.VISIBLE);
                    activityFlickImageListBinding.searchSrcText.setText("");
                }
            }
        }
        else if (genericRespModel instanceof String)
        {
            Toast.makeText(FlickImageListActivity.this,(String)genericRespModel,Toast.LENGTH_SHORT).show();
        }
    }

}
