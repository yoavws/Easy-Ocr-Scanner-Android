package com.wordpress.priyankvex.easyocrscannerdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Priyank(@priyankvex) on 27/8/15.
 *
 * Class to handle scanning of image.
 */
public class EasyOcrScanner {

    protected Activity mActivity;
    private String directoryPathOriginal;
    private String filePathOriginal;
    private int requestCode;
    private EasyOcrScannerListener mOcrScannerListener;
    private String trainedDataCode;

    public EasyOcrScanner(Activity activity, String directoryPath, int requestCode, String trainedDataCode){
        this.mActivity = activity;
        this.directoryPathOriginal = directoryPath;
        this.requestCode = requestCode;
        this.trainedDataCode = trainedDataCode;
    }

    public void takePicture(){
        Intent e = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /*
        this.filePathOriginal = FileUtils.getDirectory(this.directoryPathOriginal) + File.separator + Calendar.getInstance().getTimeInMillis() + ".jpg";



        e.putExtra("output", Uri.fromFile(new File(this.filePathOriginal)));
*/


        Uri imageUri = FileProvider.getUriForFile(
                mActivity,
                "com.wordpress.priyankvex.easyocrscannerdemo.provider", //(use your app signature + ".provider" )
                new File(FileUtils.getDirectory(this.directoryPathOriginal) + File.separator + Calendar.getInstance().getTimeInMillis() + ".jpg"));

        e.putExtra("output" , imageUri);
        startActivity(e);
    }

    public void onImageTaken(){
        Log.d(Config.TAG, "onImageTaken with path " + this.filePathOriginal);
        ImageProcessingThread thread = new ImageProcessingThread(this.mOcrScannerListener,
                this.filePathOriginal, this.directoryPathOriginal, this.mActivity, this.trainedDataCode);
        thread.execute();
    }

    public  void onImageTaken(int requestCode, int resultCode, Intent data){
        onImageTaken();
        /*
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImageView.setImageBitmap(imageBitmap);
            }
        }
         */

    }

    private void startActivity(Intent intent){
        if(this.mActivity != null) {
            this.mActivity.startActivityForResult(intent, this.requestCode);
        }
    }

    public void setOcrScannerListener(EasyOcrScannerListener mOcrScannerListener) {
        this.mOcrScannerListener = mOcrScannerListener;
    }
}