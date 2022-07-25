package com.mobiroller.core.models.request;

public class CheckLoginStateDto {

    public String sessionKey;
    public String id;
    public String udid;
    public String apiKey;
    public String appKey;

    public CheckLoginStateDto(String sessionKey, String id, String udid, String apiKey, String appKey) {
        this.sessionKey = sessionKey;
        this.id = id;
        this.udid = udid;
        this.apiKey = apiKey;
        this.appKey = appKey;
    }

}
