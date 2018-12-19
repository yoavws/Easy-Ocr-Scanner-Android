package com.wordpress.priyankvex.easyocrscannerdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wordpress.priyankvex.easyocrscannerdemo.ServerEndpoint.NamesList;
import com.wordpress.priyankvex.easyocrscannerdemo.ServerEndpoint.ServerAPI;
import com.wordpress.priyankvex.easyocrscannerdemo.ServerEndpoint.ServerAPIImpl;


public class MainActivity extends AppCompatActivity implements EasyOcrScannerListener{

    EasyOcrScanner mEasyOcrScanner;
    TextView textView;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        /////
        System.out.println("on create starting..");
        ServerAPI serverapi=new ServerAPIImpl();
        NamesList listOfNames = ((ServerAPIImpl) serverapi).getListOfNames("yoav");

        //((ServerAPIImpl) serverapi).SendConfirmationToServer(ServerAPIImpl.Operation.MAIL,"yoav.weiss@imperva.com",ServerAPIImpl.Floor.FIRST);

        //System.out.println("server res"+listFromServer);

        /////
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        // initialize EasyOcrScanner instance.
        mEasyOcrScanner = new EasyOcrScanner(MainActivity.this, "EasyOcrScanner",
                Config.REQUEST_CODE_CAPTURE_IMAGE, "eng");

        // Set ocrScannerListener
        mEasyOcrScanner.setOcrScannerListener(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mEasyOcrScanner.takePicture();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Call onImageTaken() in onActivityResult.
        if (resultCode == RESULT_OK && requestCode == Config.REQUEST_CODE_CAPTURE_IMAGE){
            mEasyOcrScanner.onImageTaken();
        }
    }

    /**
     * Callback when after taking picture, scanning process starts.
     * Good place to show a progress dialog.
     * @param filePath file path of the image file being processed.
     */
    @Override
    public void onOcrScanStarted(String filePath) {
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Scanning...");
        mProgressDialog.show();
    }

    /**
     * Callback when scanning is finished.
     * Good place to hide teh progress dialog.
     * @param bitmap Bitmap of image that was scanned.
     * @param recognizedText Scanned text.
     */
    @Override
    public void onOcrScanFinished(Bitmap bitmap, String recognizedText) {
        textView.setText(recognizedText);
        if (mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }
}
