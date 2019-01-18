package com.vineetjain.flicknow.ImageSearchModule.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.android.volley.Request;
import com.vineetjain.flicknow.ImageSearchModule.Model.ImageListModel;

import Listener.onUpdateViewListener;
import NetworkEngine.NetworkEngine;
import Utils.APIConstants;

public class PhotoListVM extends ViewModel implements onUpdateViewListener {

    private final MutableLiveData<Object> responseLiveData = new MutableLiveData<>();


    public MutableLiveData<Object> photoReceivedResponse() {
        return responseLiveData;
    }


    public void getDatafromAPI(String searchString, Context context)
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
            responseLiveData.setValue(responseObject);
         }
    }
