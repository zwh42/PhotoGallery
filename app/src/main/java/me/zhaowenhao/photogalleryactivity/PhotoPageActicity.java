package me.zhaowenhao.photogalleryactivity;

import android.support.v4.app.Fragment;

/**
 * Created by zhaowenhao on 16/4/29.
 */
public class PhotoPageActicity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment(){
        return new PhotoPageFragment();
    }
}
