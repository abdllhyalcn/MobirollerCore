package com.mobiroller.core.helpers;
import android.content.Context;
import android.os.Build;
import android.webkit.WebResourceResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebviewResourceMappingHelper {
    private static WebviewResourceMappingHelper instance;
    private List<LocalAssetMapModel> localAssetMapModelList;
    private List<String> overridableExtensions = new ArrayList<>(Arrays.asList("js", "css", "png", "jpg", "woff", "ttf", "eot", "ico"));

    private WebviewResourceMappingHelper(){

    }

    public static WebviewResourceMappingHelper getInstance(){
        if(instance == null){
            instance = new WebviewResourceMappingHelper();
        }
        return instance;
    }


    public String getLocalFilePath(String url, Context context){
        String localFilePath = "";
        String fileNameForUrl = getLocalFileNameForUrl(url);
        if(fileNameForUrl != null && !fileNameForUrl.isEmpty() && fileExists(fileNameForUrl,context)){
            localFilePath = getFileFullPath(fileNameForUrl,context);
        }
        return localFilePath;
    }

    public String getLocalFileNameForUrl(String url){
        String localFileName = "";
        String[] parts = url.split("/");
        if(parts.length > 0){
            localFileName = parts[parts.length-1];
        }
        return localFileName;
    }

    private boolean fileExists(String fileName, Context context){
        String path = context
                .getFilesDir() + "/cart/" + fileName;
        return new File(path).exists();
    }

    private String getFileFullPath(String relativePath, Context context){
        return context.getFilesDir() + "/cart/" + relativePath;
    }

    public List<String> getOverridableExtensions(){
        return overridableExtensions;
    }

    public String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    public String getMimeType(String fileExtension){
        String mimeType = "";
        switch (fileExtension){
            case "css" :
                mimeType = "text/css";
                break;
            case "js" :
                mimeType = "text/javascript";
                break;
            case "png" :
                mimeType = "image/png";
                break;
            case "jpg" :
                mimeType = "image/jpeg";
                break;
            case "ico" :
                mimeType = "image/x-icon";
                break;
            case "woff" :
            case "ttf" :
            case "eot" :
                mimeType = "application/x-font-opentype";
                break;
        }
        return mimeType;
    }


    public static WebResourceResponse getWebResourceResponseFromFile(String filePath, String mimeType, String encoding) throws FileNotFoundException {
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusCode = 200;
            String reasonPhase = "OK";
            Map<String, String> responseHeaders = new HashMap<String, String>();
            responseHeaders.put("Access-Control-Allow-Origin","*");
            return new WebResourceResponse(mimeType, encoding, statusCode, reasonPhase, responseHeaders, fileInputStream);
        }
        return new WebResourceResponse(mimeType, encoding, fileInputStream);
    }

    private class LocalAssetMapModel{
        String url;
        String asset_url;
    }
}