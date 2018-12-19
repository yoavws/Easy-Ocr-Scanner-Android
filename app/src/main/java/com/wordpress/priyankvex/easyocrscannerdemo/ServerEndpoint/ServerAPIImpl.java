package com.wordpress.priyankvex.easyocrscannerdemo.ServerEndpoint;

import android.accounts.NetworkErrorException;
import android.app.DownloadManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
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
    //"['Yoav weiss', 'yoav.weiss@imperva.com','Yoav weiss2', 'yoav.weiss@imperva.com','Yoav weiss3', 'yoav.weiss@imperva.com']"
    @Override
    public NamesList getListOfNames(String ocrTxt , Context context) throws NetworkErrorException {
        NamesList parsedList=null;
        String responseStr =null;
        try {
            getListFromServer(ocrTxt, context);
        }catch (Exception e){
            throw new NetworkErrorException("failed to reach server");
        }
        //no names found
        if(responseStr.equals("]")){
            System.out.println("failed to find names on server");
            return null;
        }
        com.google.gson.Gson response = new Gson();
        ArrayList<String> names = response.fromJson(responseStr, ArrayList.class);
        parsedList.names2Mails=names;
        return parsedList;
    }
    @Override
    public void SendConfirmationToServer(Operation operation, String mail ,Floor floor, Context context)throws NetworkErrorException {
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
        try {
            urlConnection(url, context);
        }catch (Exception e) {
            throw new NetworkErrorException("failed to reach server");
        }
        //System.out.println(" SendConfirmationToServer result: "+resString);
    }


    public void getListFromServer(String ocrTxt , Context context) {
        URL url = buildAPI(SCAN_TEXT+ocrTxt);
        urlConnection(url, context);
    }

    private void urlConnection(URL url , final Context context) {
       System.out.println("URL of API: "+url);
       String resString="";
        // Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(context);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET , url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Mail sent successfully!!", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Server down. All the BASA.", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


/*
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
        return resString;*/
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

