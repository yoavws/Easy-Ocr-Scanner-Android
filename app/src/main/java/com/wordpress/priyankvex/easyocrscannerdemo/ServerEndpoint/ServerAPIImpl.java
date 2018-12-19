package com.wordpress.priyankvex.easyocrscannerdemo.ServerEndpoint;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ServerAPIImpl implements ServerAPI {
    private final static String SERVER_END_POINT="http://34.240.58.150";
    private final static String PORT="8090";
    private final static String SCAN_TEXT="api/scan-text/";
    private final static String CONFIRMATION="api/confirmation/";

    private static String serverEndPoint;


   public enum Operation
    {
        FOOD, MAIL;
    }

    public enum Floor
    {
        FIRST, SECOND,THIRD;
    }

    public ServerAPIImpl(){
        //init server endpoint
        serverEndPoint=SERVER_END_POINT+":"+PORT+"/";
    }
    //http://34.240.58.150:8090/api/scan-text/yoav"
    @Override
    public NamesList getListOfNames(String ocrTxt) {
        NamesList parsedList=null;
        String responseStr = "['Yoav weiss', 'yoav.weiss@imperva.com','Yoav weiss2', 'yoav.weiss@imperva.com','Yoav weiss3', 'yoav.weiss@imperva.com']";//getListFromServer(ocrTxt);
        com.google.gson.Gson response = new Gson();
        ArrayList<String> names = response.fromJson(responseStr, ArrayList.class);
        return parsedList;
    }
    @Override
    public void SendConfirmationToServer(Operation operation, String mail ,Floor floor) {
        String apiString=CONFIRMATION;
        String floorAPI="/"+floor+"/";
        String resString;
        switch (operation){
            case FOOD:
                apiString+="food/"+mail+floorAPI;
                break;
            case MAIL:
                apiString+="mail/"+mail+floorAPI;
                break;
        }
        URL url = buildAPI(apiString);
        resString = urlConnection(url);
        System.out.println(" SendConfirmationToServer result: "+resString);
    }


    public String getListFromServer(String ocrTxt) {
        String resString=null;
        URL url = buildAPI(SCAN_TEXT+ocrTxt);
        resString = urlConnection(url);
        return resString;
    }

    private String urlConnection(URL url) {
       System.out.println("URL of API: "+url);
       String resString="";
       HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            byte[] contents = new byte[1024];
            //resString = in.toString();

            int bytesRead = 0;
            String strFileContents;
            while((bytesRead = in.read(contents)) != -1) {
                resString += new String(contents, 0, bytesRead);
            }
            //readStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return resString;
    }

    @Nullable
    private URL buildAPI(String ocrTxt) {
        URL url = null;
        try {
            url = new URL(serverEndPoint+ocrTxt);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}

