package com.wordpress.priyankvex.easyocrscannerdemo.ServerEndpoint;

import android.accounts.NetworkErrorException;

import com.google.gson.Gson;

public interface ServerAPI {
    NamesList getListOfNames(String OcrTxt) throws NetworkErrorException;
    void SendConfirmationToServer(ServerAPIImpl.Operation operation, String mail , ServerAPIImpl.Floor floor)throws NetworkErrorException;

    }
