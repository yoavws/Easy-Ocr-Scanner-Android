package com.wordpress.priyankvex.easyocrscannerdemo.ServerEndpoint;

import com.google.gson.Gson;

public interface ServerAPI {
    NamesList getListOfNames(String OcrTxt);
    void SendConfirmationToServer(ServerAPIImpl.Operation operation, String mail , ServerAPIImpl.Floor floor);

    }
