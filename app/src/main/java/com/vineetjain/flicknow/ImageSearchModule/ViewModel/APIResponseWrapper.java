package com.vineetjain.flicknow.ImageSearchModule.ViewModel;

public class APIResponseWrapper {

    public final Object apiResponse;

    public final int requestType;

    public final boolean apiResult;

    public APIResponseWrapper(Object apiresponse, int requestType, boolean apiResult) {
        this.apiResponse = apiresponse;
        this.requestType = requestType;
        this.apiResult = apiResult;
    }
}
