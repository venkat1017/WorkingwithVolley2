package reader.android.com.newsx;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by venkatesh on 24-09-2016.
 */
public class CustomApiRequest {
    private static CustomApiRequest customApiRequest;
    private static Context context;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private CustomApiRequest(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });

    }

    public static synchronized CustomApiRequest getInstance(Context context) {
        if (customApiRequest == null) {
            customApiRequest = new CustomApiRequest(context);
        }
        return customApiRequest;
    }

   // @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network);
            requestQueue.start();
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}




