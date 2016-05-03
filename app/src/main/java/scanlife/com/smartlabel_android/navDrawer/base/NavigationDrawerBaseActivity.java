package scanlife.com.smartlabel_android.navDrawer.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ViewUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import roboguice.activity.RoboActionBarActivity;
import scanlife.com.smartlabel_android.R;
import scanlife.com.smartlabel_android.navDrawer.AboutActivity;
import scanlife.com.smartlabel_android.navDrawer.HistoryListActivity;
import scanlife.com.smartlabel_android.navDrawer.SettingsActivity;
import scanlife.com.smartlabel_android.navDrawer.model.NavDrawerItem;
import scanlife.com.smartlabel_android.navDrawer.model.NavDrawerListAdapter;
import scanlife.com.smartlabel_android.navDrawer.view.NavDrawerHeader;

/**
 * Created by hiren on 5/2/16.
 */
public abstract class NavigationDrawerBaseActivity extends RoboActionBarActivity {
    public static final int NAV_DRAWER_FIRST_TIME_DELAY = 4000;
    public static final int MAX_USERNAME_LENGTH = 15;
    //private BadgeView mBadgeView;
    private Context mContext;
    private static int activityCount = 0;
    private static final String TAG = "NavDrawerBaseActivity";
    private static boolean didStartM2M = false;
    private boolean orientationChanged = false;

    //@Override
    public void actionFinished() {

    }

    public enum Screens {
        // inbox and favs are in nav header not the list
        // list items start at 1 due to header
        INBOX(-2), FAVORITES(-1), SCAN(1), FEED(2), HISTORY(3), DEALS(4), REWARDS(5), COUPONS(6), SETTINGS(7), ABOUT(8);
        private int value;

        private Screens(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Screens fromInt(int screen) {
            for (Screens s : values()) {
                if (s.getValue() == screen) return s;
            }
            return null;
        }
    }

    ;

    // facebook

    // Navigation Drawer
    private DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    private CharSequence mCurrentTitle;
    public NavDrawerHeader mNavHeaderView;
    public Boolean isBadgeViewAvailable = false;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] navMenuTitles;


    // to support highlighting the current or previously selected nav drawer item when
    // returning from Rewards or Deals or Inbox
    private static Screens mSelectedItem;
    private static Screens mPreviouslySelectedItem;
    // Called from inheriting classes as normal but onCreateDrawer is expected to be called also
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //check to see if the activity was created because of an orientation change or not
        orientationChanged = !(savedInstanceState == null);
        super.onCreate(savedInstanceState);
        //Intent svc = new Intent(this, GcmIntentService.class);
       // startService(svc);

    }
    // This method should be called in the onCreate of extending classes.
    // Usually right after setContentView() is called this method is called
    // to setup the navigation drawer.
    // Any callers needs to have the expected views in their layout, like R.id.drawer_layout
    protected void onCreateDrawer(Bundle savedInstanceState, Context context) {
        mContext = context;

        // init managers
       //
        // mSessionMManager = SessionMManager.getInstance(this);
        // init the nav drawer
        // R.id.drawer_layout should be in every activity with exactly the same id.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCurrentTitle = getTitle();

        // hamburger button stuff
        initActionBarToggle();

        // add the header view to the list
        // this is the facebook profile pic, fb login button, user details
        // and the inbox and favorites buttons
        mDrawerList = (ListView) findViewById(R.id.nav_drawer_list);
        mNavHeaderView = new NavDrawerHeader(this, mDrawerLayout, mDrawerList);
        mDrawerList.addHeaderView(mNavHeaderView);

        // setting the nav drawer list adapter
        ArrayList<NavDrawerItem> navDrawerItems = getNavDrawerItems();
        NavDrawerListAdapter adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // do facebook stuff
        //setupFacebook(savedInstanceState);

//        boolean useRewards = mPreferenceManager.getUseSessionM();
//        mDrawerList.getChildAt(4).setEnabled(false);

        // open the nav drawer if its the users 1st time seeing it
        handleNavDrawerFirstTime();

        // handle taps on nav drawer list items
        mDrawerList.setOnItemClickListener(new NavDrawerListOnItemClickListener());
    }
    private void handleNavDrawerFirstTime() {
       // mPreferenceManager = SLPreferenceManager.getInstance(this);
        if (true) //mPreferenceManager.isNavDrawerFirstTime()
        {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.openDrawer(mDrawerList);

            // close the drawer after a dalay
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDrawerLayout.closeDrawer(mDrawerList);
                        }
                    });
                }
            }, NAV_DRAWER_FIRST_TIME_DELAY);
        }
    }

    private void initActionBarToggle() {
        // the hamburger to open/close the drawer and listen for clicks on the toggle
       // mDrawerToggle = new SLActionBarDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // turns on the action bar hamburger button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public ArrayList<NavDrawerItem> getNavDrawerItems() {
        // nav drawer item names
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer item icons from resources
        TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        // adding nav drawer items to array
        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<>();
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
       /* navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
*/
        // Recycle the typed array
        navMenuIcons.recycle();
        return navDrawerItems;
    }
    public void selectNavDrawerPosition(int position) {
        // if position selected is not one of the special (Rewards, Deals, Inbox)
        // items then remember the previous selection
        // a special item does not have the nav drawer and cannot be shown selected
        if (mSelectedItem != Screens.DEALS && mSelectedItem != Screens.REWARDS && mSelectedItem != Screens.INBOX) {
            mPreviouslySelectedItem = mSelectedItem;
        }
        mSelectedItem = Screens.fromInt(position);
    }
    private void selectNavDrawerItem() {
        // handle selection and highlighting of proper nav drawer item
        if (mNavHeaderView != null) {
            if (mSelectedItem == Screens.DEALS || mSelectedItem == Screens.REWARDS || mSelectedItem == Screens.INBOX) {
                // select the last screen before going to Rewards or Deals or Inbox
                if (mPreviouslySelectedItem == Screens.FAVORITES) {
                    mNavHeaderView.setFavoritesSelected(true);
                    mDrawerList.setItemChecked(mSelectedItem.getValue(), false);
                } else {
                    mDrawerList.setItemChecked(mPreviouslySelectedItem.getValue(), true);
                }
            } else if (mSelectedItem == Screens.FAVORITES) {
                // select the fav button in the header
                mNavHeaderView.setFavoritesSelected(true);
            } else {
                // select the list item as normal
                mNavHeaderView.setFavoritesSelected(false);

                if (mSelectedItem == null) { // This should only happen at app start
                    mSelectedItem = Screens.SCAN;
                }

                mDrawerList.setItemChecked(mSelectedItem.getValue(), true);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    protected void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
    //
    // Helper methods
    //
    protected void launchActivity(Class<?> activity) {
        this.launchActivity(activity, null, null);
    }

    public void launchActivity(Class<?> activity, String intent_para, String value) {
        Intent i = new Intent();
        i.setClass(this, activity);
        if (null != intent_para) {
            i.putExtra(intent_para, value);
        }
        startActivity(i);
    }

    // TODO: Refactor to remove "helper methods" pattern that requires us make a new helper method every time we need to change the number or type of extras
    public void launchActivity(Class<?> activity, String intent_para1, String value1, String intent_para2, boolean value2) {
        Intent i = new Intent();
        i.setClass(this, activity);
        if (null != intent_para1) {
            i.putExtra(intent_para1, value1);
        }
        if (null != intent_para2) {
            i.putExtra(intent_para2, value2);
        }

        ///ViewUtils.startActivityWithTransition(this, i);
    }

    protected void launchActivity(Class<?> activity, String intent_para, boolean value) {
        Intent i = new Intent();
        i.setClass(this, activity);
        if (null != intent_para) {
            i.putExtra(intent_para, value);
        }
        startActivity(i);
    }
    //
    // drawer open, close, slide
    //
    private class SLActionBarDrawerToggle extends ActionBarDrawerToggle {
        public SLActionBarDrawerToggle() {
            super((Activity) NavigationDrawerBaseActivity.this, NavigationDrawerBaseActivity.this.mDrawerLayout, null, 0, 0);
        }

        public void onDrawerClosed(View view) {
            getSupportActionBar().setTitle(mCurrentTitle);
        }

        public void onDrawerOpened(View drawerView) {
            getSupportActionBar().setTitle(R.string.title_activity_smartLabel);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            // zoom the profile pic
            ImageView profilePic = (ImageView) findViewById(R.id.profileImage);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                profilePic.setScaleX(slideOffset);
                profilePic.setScaleY(slideOffset);
            }

            super.onDrawerSlide(drawerView, slideOffset);
        }
    }

    //
    // nav drawer list item selected
    //
    private class NavDrawerListOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String requestUrl;

            switch (Screens.fromInt(position)) {
                case SCAN: // scan
                   // launchActivity(HubActivity.HubItem.SCANNER_FEATURE.launchActivity);
                    // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
               /* case FEED: // feed
                    Intent feedListActivityIntent = new Intent(parent.getContext(), FeedListActivity.class);
                    startActivity(feedListActivityIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                   // getSessionMManager().registerDiscover();
                    break;*/
                case HISTORY: // history
                    Intent historyListActivityIntent = new Intent(parent.getContext(), HistoryListActivity.class);
                    startActivity(historyListActivityIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
               /* case DEALS: // deals
                    mFlurryManager.logQustodianMyOffersClicked();
                    forwardToQustodian(new DealsQIntent(parent.getContext()));
                    break;
                case REWARDS: // rewards
                    mDrawerList.clearChoices();
                    SessionM.getInstance().presentActivity(SessionM.ActivityType.PORTAL);
                    break;
                case COUPONS: // coupons
                    launchCouponWallet();
                    break;*/
                case SETTINGS: // settings
                    Intent intent = new Intent();
                    intent.setClass(parent.getContext(), SettingsActivity.class);
                    //boolean browserPromptEnabled = mBuildConfigManager.isWebPromptEnabled();
                    //intent.putExtra(SettingsActivity.KEY_BROWSER_PROMPT_CONFIG, browserPromptEnabled);
                    parent.getContext().startActivity(intent);
                    break;
                case ABOUT: // about
                    Intent aboutActivityIntent = new Intent(parent.getContext(), AboutActivity.class);
                    startActivity(aboutActivityIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;

            }

            // set action bar title to the screen we are going to
            setTitle(navMenuTitles[position - 1]);
            mDrawerLayout.closeDrawer(mDrawerList);

            // highlight the selected item
            selectNavDrawerPosition(position);
        }

       /* private void launchCouponWallet() {
            // launch the coupon url in webview
            SLLocationManager locationManager = SLLocationManager.getInstance(mContext);
            String couponWalletUrl = SDKManager.getInstance(mContext).getCouponWalletUrl(locationManager);
            String title = getStringResource(R.string.title_coupon_wallet);

            Intent intent = makeCouponIntent(couponWalletUrl, title);
            startActivity(intent);
        }*/

        /*@NonNull
        private Intent makeCouponIntent(String couponWalletUrl, String title) {
            Intent intent = new Intent();
            intent.setClass(mContext, WebViewActivity.class);
            intent.setData(android.net.Uri.parse(couponWalletUrl));
            intent.putExtra(WebViewActivity.KEY_PAGE_DISPLAYSTATE,
                    WebViewActivity.WEBVIEW_DISPLAY_STATE_INTERNAL);
            intent.putExtra(WebViewActivity.KEY_PAGE_NAME, SLFlurryManager.PAGE_COUPONWALLET);
            intent.putExtra(WebViewActivity.KEY_PAGE_TITLE, title);
            return intent;
        }*/
    }

}
