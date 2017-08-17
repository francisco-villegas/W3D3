package com.example.francisco.w3d3.Broadcast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.francisco.w3d3.services.ImgService;

/**
 * Created by FRANCISCO on 16/08/2017.
 */

public class ImgResultReceiver extends ResultReceiver {

    ImageView img;
    Context context;

    public ImgResultReceiver(Handler handler, ImageView img, Context context) {
        super(handler);
        this.img = img;
        this.context = context;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case ImgService.DOWNLOAD_ERROR:
                Toast.makeText(context, "error in download",
                        Toast.LENGTH_SHORT).show();
                break;

            case ImgService.DOWNLOAD_SUCCESS:
                String filePath = resultData.getString("filePath");
                Bitmap bmp = BitmapFactory.decodeFile(filePath);
                if ( img != null && bmp != null){
                    img.setImageBitmap(bmp);
                    Toast.makeText(context,
                            "image download via IntentService is done",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(context,
                            "error in decoding downloaded file",
                            Toast.LENGTH_SHORT).show();
                }

                break;
        }
        super.onReceiveResult(resultCode, resultData);
    }

}
