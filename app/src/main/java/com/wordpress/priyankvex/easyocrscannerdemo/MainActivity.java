package com.wordpress.priyankvex.easyocrscannerdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.priyankvex.easyocrscannerdemo.ServerEndpoint.NamesList;
import com.wordpress.priyankvex.easyocrscannerdemo.ServerEndpoint.ServerAPI;
import com.wordpress.priyankvex.easyocrscannerdemo.ServerEndpoint.ServerAPIImpl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements EasyOcrScannerListener{

    EasyOcrScanner mEasyOcrScanner;
    TextView textView;
    ProgressDialog mProgressDialog;
    Context context;

    String mCurrentPhotoPath;
    String imageFileNameWithSuffix = "";
    String imageFileStorageDir = "";

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_TAKE_PHOTO = 1;


    Button button2;
    Button proccessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        textView = (TextView) findViewById(R.id.textView);

        // initialize EasyOcrScanner instance.
        mEasyOcrScanner = new EasyOcrScanner(MainActivity.this, "EasyOcrScanner",
                Config.REQUEST_CODE_CAPTURE_IMAGE, "eng");

        // Set ocrScannerListener
        mEasyOcrScanner.setOcrScannerListener(this);

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mEasyOcrScanner.takePicture();
            }
        });


        button2 = (Button) findViewById(R.id.buttonThatWillWork);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File...
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(context,
                                "com.wordpress.priyankvex.easyocrscannerdemo.provider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }

                }
            }
        });
        proccessButton = (Button) findViewById(R.id.proccessImageButton);




    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("dhdhd", "dasdasda");
        super.onActivityResult(requestCode, resultCode, data);
        // Call onImageTaken() in onActivityResult.
        if (resultCode == RESULT_OK && requestCode == Config.REQUEST_CODE_CAPTURE_IMAGE){
            mEasyOcrScanner.onImageTaken();
            //mEasyOcrScanner.onImageTaken(requestCode, resultCode, data);
        } else {
            //ImageView image = (ImageView) findViewById(R.id.pic);
            //image.setImageDrawable(Drawable.createFromPath(mCurrentPhotoPath));


            /////
            if (android.os.Build.VERSION.SDK_INT > 9)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            ServerAPI serverapi=new ServerAPIImpl();
            try {
                NamesList listOfNames = ((ServerAPIImpl) serverapi).getListOfNames("yoav weiss");
                ((ServerAPIImpl) serverapi).SendConfirmationToServer(ServerAPIImpl.Operation.MAIL, "yoav.weiss@imperva.com", ServerAPIImpl.Floor.FIRST);
                System.out.println("server res: "+listOfNames.names2Mails.get(0));
            }catch (Exception e){
                System.err.println("failed to reach server");
            }
            Toast toast = Toast.makeText(this.context, "E-Mail sent successfuly!" , Toast.LENGTH_LONG);
            toast.show();

            ////
            proccessButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageProcessingThread thread = new ImageProcessingThread(MainActivity.this,
                            mCurrentPhotoPath, //imageFileStorageDir +imageFileNameWithSuffix,
                            imageFileStorageDir,
                            MainActivity.this,
                            "eng");
                    thread.execute();//EasyOcrScannerListener ocrScannerListener, String filePath, String directoryPath, Activity activity, String trainedDataCode);
                }
            });

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

    private File createImageFile() throws IOException {
        Log.d("createImageFile()" , "Started.");
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        imageFileNameWithSuffix = imageFileName + ".jpg";
        imageFileStorageDir = storageDir.toString();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
