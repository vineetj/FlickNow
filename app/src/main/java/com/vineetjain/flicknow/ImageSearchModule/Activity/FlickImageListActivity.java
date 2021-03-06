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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vineetjain.flicknow.ImageSearchModule.Adapter.PictureItem;
import com.vineetjain.flicknow.ImageSearchModule.Model.ImageListModel;
import com.vineetjain.flicknow.ImageSearchModule.ViewModel.APIResponseWrapper;
import com.vineetjain.flicknow.ImageSearchModule.ViewModel.PhotoListVM;
import com.vineetjain.flicknow.R;
import com.vineetjain.flicknow.databinding.ActivityFlickImageListBinding;

import java.util.ArrayList;

import Base.BaseActivity;
import Utils.APIConstants;

public class FlickImageListActivity extends BaseActivity implements LifecycleObserver {

    private PhotoListVM photoListVM;
    private PictureItem pictureItemAdapter;
    ActivityFlickImageListBinding activityFlickImageListBinding;
    private ArrayList<ImageListModel.SingleIImagetemModel> imgListModel = new ArrayList<>();
    private ImageView searchButton;
    private TextView textViewHead;
    private AutoCompleteTextView giftCardAutoComplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flick_image_list);

        activityFlickImageListBinding = DataBindingUtil.setContentView(this, R.layout.activity_flick_image_list);
       // activityFlickImageListBinding.setViewModel(new PhotoListVM());
        activityFlickImageListBinding.executePendingBindings();
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchButton = (ImageView) findViewById(R.id.searchIcon);
        textViewHead = (TextView)findViewById(R.id.txvHeader);
        giftCardAutoComplete = (AutoCompleteTextView) findViewById(R.id.giftCardAutoComplete);
        giftCardAutoComplete.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giftCardAutoComplete.setVisibility(View.VISIBLE);
                textViewHead.setVisibility(View.GONE);
            }
        });
        //RelativeLayout rltLayout = (RelativeLayout)findViewById(R.id.rltRecycler);
        //RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)rltLayout.getLayoutParams();

        //relativeParams.setMargins(toolbar.getHeight(),toolbar.getHeight(),toolbar.getHeight(),toolbar.getHeight());
        //rltLayout.setLayoutParams(relativeParams);
           RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            pictureItemAdapter = new PictureItem(this, imgListModel);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(pictureItemAdapter);

        photoListVM = ViewModelProviders.of(this).get(PhotoListVM.class);

        photoListVM.photoReceivedResponse().observe(this, new Observer<APIResponseWrapper>() {
            @Override
            public void onChanged(@Nullable APIResponseWrapper genericRespModel) {
                HandleOnChanged(genericRespModel);
            }
        });

        // start loading feed as soon as app is launched (landscape mode is currently disabled)
        showProgressdialog("loading",true);
        hideSoftKeyboard();
        photoListVM.getFlickrFeed(giftCardAutoComplete.getText().toString(), getApplicationContext());

        giftCardAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (giftCardAutoComplete.getText().toString().length() > 0) {
                    if (actionId == event.ACTION_DOWN) {
                            showProgressdialog("loading", true);
                            hideSoftKeyboard();
                            photoListVM.getFlickrFeed(giftCardAutoComplete.getText().toString(), getApplicationContext());
                        giftCardAutoComplete.setVisibility(v.GONE);
                        giftCardAutoComplete.setText("");
                        textViewHead.setVisibility(View.VISIBLE);
                        searchButton.setVisibility(v.VISIBLE);
                        return true;
                    }
                }
                return false;
            }
        });

        // detect touch on search icon placed with search Edit text & send query
        giftCardAutoComplete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (giftCardAutoComplete.getRight() - giftCardAutoComplete.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        giftCardAutoComplete.setVisibility(View.GONE);
                        textViewHead.setVisibility(View.VISIBLE);

                       /* if(!TextUtils.isEmpty(giftCardAutoComplete.getText())) {
                            showProgressdialog("loading", true);
                            hideSoftKeyboard();

                            photoListVM.getFlickrFeed(giftCardAutoComplete.getText().toString(), getApplicationContext());
                        }
                        else
                            Toast.makeText(FlickImageListActivity.this,getResources().getString(R.string.no_search_text),Toast.LENGTH_SHORT).show();
*/
                        return true;
                    }
                }
                return false;
            }
        });

    }

    public void HandleOnChanged(APIResponseWrapper genericRespModel)
    {
        activityFlickImageListBinding.noOperators.setVisibility(View.GONE);
        pictureItemAdapter.setListData(imgListModel); // if response resulted in zero items & then current list should become empty
        hideProgressDialog();
        hideSoftKeyboard();
        if(genericRespModel.apiResult) {

            switch (genericRespModel.requestType) {
                case APIConstants.REQUESTTYPEGETPHOTO: {
                    if (genericRespModel.apiResponse instanceof ImageListModel) {
                        if (((ImageListModel) genericRespModel.apiResponse).getCode() != 0) {
                            // error response received from flickr
                            Toast.makeText(FlickImageListActivity.this, ((ImageListModel) genericRespModel.apiResponse).getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            if (((ImageListModel) genericRespModel.apiResponse).getItems().size() > 0)
                                pictureItemAdapter.setListData(((ImageListModel) genericRespModel.apiResponse).getItems());
                            else {
                                activityFlickImageListBinding.noOperators.setVisibility(View.VISIBLE);
                                //activityFlickImageListBinding.searchSrcText.setText("");
                            }
                        }
                    }
                }
                break;
                default:break;
            }
        }
        else if (genericRespModel.apiResponse instanceof String)
        {
            // in case error string is received, like 'no internet connection'
            Toast.makeText(FlickImageListActivity.this, (String) genericRespModel.apiResponse, Toast.LENGTH_SHORT).show();
        }

        else
        {
            Toast.makeText(FlickImageListActivity.this, getResources().getString(R.string.general_error), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy()
    {
     super.onDestroy();
    }

}
