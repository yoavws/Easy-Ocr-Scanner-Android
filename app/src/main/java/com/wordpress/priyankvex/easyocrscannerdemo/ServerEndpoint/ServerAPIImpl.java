package com.wordpress.priyankvex.easyocrscannerdemo.ServerEndpoint;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ServerAPIImpl implements ServerAPI {

    @Override
    public Gson getListOfNames() {
        String responseStr = getListFromServer();
        com.google.gson.Gson response = new Gson();
        NamesList names = response.fromJson(responseStr, NamesList.class);
        return response;
    }


    public String getListFromServer() {
        URL url = null;
        String resString=null;
        try {
            url = new URL("http://scan.incaptest.co/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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

}

