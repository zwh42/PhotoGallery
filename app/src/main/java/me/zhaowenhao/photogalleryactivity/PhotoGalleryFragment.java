package me.zhaowenhao.photogalleryactivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by zhaowenhao on 16/4/20.
 */
public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";

    GridView mGridView;
    ArrayList<GalleryItem> mItems;
    ThumbnailDownloader<ImageView> mThumbnailThread;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);

        updateItems();

        mThumbnailThread = new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            @Override
            public void onThumbnailDownloaded(ImageView imageView, Bitmap thumbnail) {
                if (isVisible()){
                    imageView.setImageBitmap(thumbnail);
                }
            }
        });
        mThumbnailThread.start();
        mThumbnailThread.getLooper();
        Log.i(TAG, "Background thread started");
    }

    public void updateItems(){
        new FetchItemsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container,false);

        mGridView = (GridView) v.findViewById(R.id.gridView);

        setupAdapter();

        return v;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mThumbnailThread.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mThumbnailThread.cleanQueue();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                return true;
            case R.id.menu_item_clear:
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(FlickrFetchr.PREF_SEARCH_QUERY, null).commit();
                updateItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    void setupAdapter(){
        if (getActivity() == null || mGridView == null) return;

        if (mItems != null){
            Log.e(TAG, "set adapter with mItems");
            mGridView.setAdapter(new GalleryItemAdapter(mItems));

        } else{
            mGridView.setAdapter(null);
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>>{
        @Override
        protected ArrayList<GalleryItem> doInBackground(Void ...params){
            Log.e(TAG, "start fetching...");

            Activity activity = getActivity();
            if (activity == null){
                return new ArrayList<GalleryItem>();
            }

            String query = PreferenceManager.getDefaultSharedPreferences(activity).getString(FlickrFetchr.PREF_SEARCH_QUERY, null);
            if (query != null){
                return new FlickrFetchr().search(query);
            } else {
                return new FlickrFetchr().fetchItems();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items){
            mItems = items;
            Log.e(TAG, "post processing");
            setupAdapter();
        }
    }

    private class GalleryItemAdapter extends ArrayAdapter<GalleryItem>{
        public GalleryItemAdapter(ArrayList<GalleryItem> items){
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int positon, View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.gallery_item_imageView);
            imageView.setImageResource(R.drawable.doge);

            GalleryItem item = getItem(positon);
            mThumbnailThread.queueThumbnail(imageView, item.getUrl());

            return convertView;
        }



    }

}
