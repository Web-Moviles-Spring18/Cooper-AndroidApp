package com.cooper.cooper.Menu;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

public class GetImageContent extends AsyncTask<String, Void, Drawable> {

    private Activity context;

    public GetImageContent(Activity context) {
        this.context = context;
    }

    @Override
    protected Drawable doInBackground(String... strings) {
        return LoadImageFromWebOperations(strings[0]);
    }

    public Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");

            Bitmap b = ((BitmapDrawable)d).getBitmap();
            Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 150, 50, false);
            return new BitmapDrawable(context.getResources(), bitmapResized);

        } catch (Exception e) {
            Log.d("E_image:", e.toString());
            return null;
        }
    }
}
