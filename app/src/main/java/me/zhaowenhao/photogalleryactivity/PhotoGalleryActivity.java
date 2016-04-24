package me.zhaowenhao.photogalleryactivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment(){
        return new PhotoGalleryFragment();
    }
}
