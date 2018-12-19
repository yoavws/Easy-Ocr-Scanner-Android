package com.wordpress.priyankvex.easyocrscannerdemo.ServerEndpoint;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.google.gson.Gson;

public interface ServerAPI {
    NamesList getListOfNames(String OcrTxt, Context context) throws NetworkErrorException;
    void SendConfirmationToServer(ServerAPIImpl.Operation operation, String mail , ServerAPIImpl.Floor floor, Context context)throws NetworkErrorException;

    }
