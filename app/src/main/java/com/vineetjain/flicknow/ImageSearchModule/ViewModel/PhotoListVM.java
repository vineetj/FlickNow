package com.vineetjain.flicknow.ImageSearchModule.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.vineetjain.flicknow.ImageSearchModule.Model.ImageListModel;

import Listener.onUpdateViewListener;
import NetworkEngine.NetworkEngine;
import Utils.APIConstants;

public class PhotoListVM extends ViewModel implements onUpdateViewListener {



    private final MutableLiveData<APIResponseWrapper> responseLiveData = new MutableLiveData<>();

    public MutableLiveData<APIResponseWrapper> photoReceivedResponse() {
        return responseLiveData;
    }


    // called from FlickImageListActivity to fetch pictures
    public void getFlickrFeed(String searchString, Context context) // ApplicationContext passed, not Activity context as that may result in memory leak
    {
        StringBuilder urlbuilder = new StringBuilder();
        urlbuilder.append(APIConstants.FLICKBASEURL);
        urlbuilder.append("&tags=");
        urlbuilder.append(searchString);
        urlbuilder.append("&api_key="+APIConstants.FLICKAPIKEY);
        NetworkEngine.with(context).setRequestType(APIConstants.REQUESTTYPEGETPHOTO).setClassType(ImageListModel.class).setHttpMethodType(Request.Method.GET).setUrl(urlbuilder.toString()).setCacheResponse(true).setUpdateViewListener(this).build();
    }

    @Override
    public void returnResponse(Object responseObject, boolean isSuccess, int reqType) {

            APIResponseWrapper respwrapper = new APIResponseWrapper(responseObject,reqType,isSuccess);
            responseLiveData.setValue(respwrapper);
         }
    }
