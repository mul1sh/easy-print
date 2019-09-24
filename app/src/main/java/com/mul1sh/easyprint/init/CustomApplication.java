package com.mul1sh.easyprint.init;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.activeandroid.ActiveAndroid;
import com.mul1sh.easyprint.R;
import com.bumptech.glide.Glide;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import org.jetbrains.annotations.NotNull;


public class CustomApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        //Database ORMhelper class
        ActiveAndroid.initialize(this);

        context = getApplicationContext();
        //MyNotificationOpenedHandler : This will be called when a notification is tapped on.
        //MyNotificationReceivedHandler : This will be called when a notification is received while your app is running.
        /*OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                .setNotificationReceivedHandler( new MyNotificationReceivedHandler() )
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();*/

        //initialize and create the image loader logic
        DrawerImageLoader.Companion.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(@NotNull ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.with(getContext()).clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.INSTANCE.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ")
                            .backgroundColor(IconicsColor.colorRes(com.mikepenz.materialdrawer.R.color.primary))
                            .size(IconicsSize.dp(56));
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ")
                            .backgroundColor(IconicsColor.colorRes(R.color.md_red_500))
                            .size(IconicsSize.dp(56));
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });
    }
}
