package com.bq.openglcamera.opengl;

import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class OnImageAvailableListener implements ImageReader.OnImageAvailableListener {
    private static final String TAG = "OnImageAvailableListene";
    DisplayMetrics metrics;
    Function1 function1;

    public OnImageAvailableListener(DisplayMetrics metrics, Function1<Bitmap, Object> f) {
        this.metrics = metrics;
        function1 = f;
    }

    public void onImageAvailable(ImageReader reader) {
        Log.i(TAG, "in OnImageAvailable");
        FileOutputStream fos = null;
        Bitmap bitmap = null;
        Image img = null;
        try {
            img = reader.acquireLatestImage();
            if (img != null) {
                Image.Plane[] planes = img.getPlanes();
                if (planes[0].getBuffer() == null) {
                    return;
                }
                int width = img.getWidth();
                int height = img.getHeight();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * width;
                byte[] newData = new byte[width * height * 4];

                int offset = 0;
                bitmap = Bitmap.createBitmap(metrics, width, height, Bitmap.Config.ARGB_8888);
                ByteBuffer buffer = planes[0].getBuffer();
                for (int i = 0; i < height; ++i) {
                    for (int j = 0; j < width; ++j) {
                        int pixel = 0;
                        pixel |= (buffer.get(offset) & 0xff) << 16;     // R
                        pixel |= (buffer.get(offset + 1) & 0xff) << 8;  // G
                        pixel |= (buffer.get(offset + 2) & 0xff);       // B
                        pixel |= (buffer.get(offset + 3) & 0xff) << 24; // A
                        bitmap.setPixel(j, i, pixel);
                        offset += pixelStride;
                    }
                    offset += rowPadding;
                }
                function1.invoke(bitmap);

//                String name = "/myscreen" + count + ".png";
//                count++;
//                File file = new File(Environment.getExternalStorageDirectory(), name);
//                fos = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                Log.i(TAG, "image saved in" + Environment.getExternalStorageDirectory() + name);
                img.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != bitmap) {
                bitmap.recycle();
            }
            if (null != img) {
                img.close();
            }

        }
    }
}
