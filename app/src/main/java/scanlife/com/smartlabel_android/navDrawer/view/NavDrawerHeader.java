package scanlife.com.smartlabel_android.navDrawer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


import java.io.InputStream;

import javax.inject.Inject;

import roboguice.RoboGuice;
import scanlife.com.smartlabel_android.R;
import scanlife.com.smartlabel_android.navDrawer.base.NavigationDrawerBaseActivity;

/**
 * Created by hiren on 4/29/16.
 */
public class NavDrawerHeader extends LinearLayout {
    public boolean mUseDefaultImage = true;

    /*@Inject
    protected SLFlurryManager mFlurryManager;
    @Inject
    protected QustodianManager mQustodianManager;
    @Inject
    private SLBuildConfigManager mBuildConfigManager;*/

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    NavigationDrawerBaseActivity mNavigationDrawerBaseActivity;
    ImageButton mFavoritesButton;

    private ImageButton mInboxButton;

    public NavDrawerHeader(final Context context, DrawerLayout drawerLayout, ListView drawerList) {
        super(context);
        mDrawerLayout = drawerLayout;
        mDrawerList = drawerList;
        RoboGuice.injectMembers(context, this);

        mNavigationDrawerBaseActivity = (NavigationDrawerBaseActivity) context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.nav_drawer_header, this, true);

        if(mUseDefaultImage) { // this bitmap stuff is causing out of memory errors when running tests
           // setDefaultImage(context);
        }

        //sizeFacebookButton(context);
        setListeners(context);
    }

    private void setListeners(final Context context) {
        // set listeners for inbox and favorites
        mInboxButton = (ImageButton) findViewById(R.id.inbox);
        mInboxButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavigationDrawerBaseActivity.selectNavDrawerPosition(-2);
             //   launchQustodianInbox(context);
            }
        });

        mFavoritesButton = (ImageButton) findViewById(R.id.preferences);
        mFavoritesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavigationDrawerBaseActivity.selectNavDrawerPosition(-1);
               // launchFavorites(context);
            }
        });
    }
    public void setFavoritesSelected(boolean selected) {
        mFavoritesButton.setPressed(selected);
    }


/*    private void sizeFacebookButton(Context context) {
        // size the fb button
        Button facebookButton = (Button) findViewById(R.id.fbButtonImage);
        int buttonWidth = (int) (ViewUtils.findMinEdge(context) / 2.0);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(buttonWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        facebookButton.setLayoutParams(layoutParams);
    }


    private void launchQustodianInbox(Context context) {
        // launch the Qustodian inbox
        mFlurryManager.logQustodianViewInboxClicked();
        forwardToQustodian(new InboxQIntent(context), context);
        closeDrawer();
    }

    private void launchFavorites(Context context) {
        NavigationDrawerBaseActivity navigationDrawerBaseActivity = (NavigationDrawerBaseActivity) context;
        navigationDrawerBaseActivity.launchActivity(HistoryListActivity.class, HistoryListActivity.HIST_UPDATE,
                HistoryListActivity.HIST_UPDATE_TRUE, HistoryListActivity.HIST_FAVORITES_MODE, true);
        navigationDrawerBaseActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        closeDrawer();
    }

    private void closeDrawer() {
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    // user profile pic
    // get bitmap of the image and round it with border
    public void setDefaultImage(Context context) {
        ImageView profilePic = (ImageView) findViewById(R.id.profileImage);
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_icon_100x100);
        Bitmap withBoarder = ImageUtils.getRoundedCornerBitmap(imageBitmap, Color.WHITE, 100, 3, context);
        BitmapDrawable dwb = new BitmapDrawable(getResources(), withBoarder);
        profilePic.setImageDrawable(dwb);
        imageBitmap.recycle();
        // withBoarder.recycle();
    }

    public void forwardToQustodian(QIntent qustodianIntent, Context context) {
        //  mQustodianManager = new QustodianManager();
        if (mQustodianManager.isInitialized()) {
            //facelift debugging
            mNavigationDrawerBaseActivity.getIntent().removeExtra(GcmIntentService.QUSTODIAN_PUSH_ACTION);
            qustodianIntent.afterSuccessfulScreenLoad(context);
        } else {
            new WaitForQustodianToInitAsyncTask((NavigationDrawerBaseActivity) context, qustodianIntent).execute(null, null, null);
        }
    }

    public ImageButton getInboxButton() {
        return mInboxButton;
    }

    *//*
* Handle response from server for history request.
*//*
    public class MyNetworkHandler implements NetworkClient.NetworkResponseHandler {
        @Override
        public void handleNetworkFailure(String errorMsg, int httpErrorCode) {
            // start history list view
            mNavigationDrawerBaseActivity.launchActivity(HistoryActivity.class, HistoryActivity.HIST_UPDATE,
                    HistoryActivity.HIST_UPDATE_FAIL);
        }

        @Override
        public void handleNetworkSuccess(InputStream stream) {
            mNavigationDrawerBaseActivity.mDBManager.updateHistoryRecords(stream, false);
            mNavigationDrawerBaseActivity.launchActivity(HistoryActivity.class, HistoryActivity.HIST_UPDATE,
                    HistoryActivity.HIST_UPDATE_TRUE);
        }
    }*/

}
