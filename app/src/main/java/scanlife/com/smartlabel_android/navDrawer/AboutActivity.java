package scanlife.com.smartlabel_android.navDrawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import scanlife.com.smartlabel_android.R;
import scanlife.com.smartlabel_android.navDrawer.base.NavigationDrawerBaseActivity;

/**
 * Created by hiren on 5/2/16.
 */
public class AboutActivity extends NavigationDrawerBaseActivity implements View.OnClickListener {
    private TextView mAboutCpRightTxt;
    private TextView mAboutEmailTxt;
    private TextView mAboutPoweredByTxt;
    private TextView mAboutAttributionTxt;
    private TextView mAboutTermsTxt;
    private TextView mAboutPrivacyTxt;
    private TextView mAboutEulaTxt;
    private Resources mResources;

    private String mAttributionUrl;
    private String mTermsConditionsUrl;
    private String mPrivacyUrl;

    // We use the following sequence of clicks as the password to open up the developer config settings
    final static int[] clicksPassword = {R.id.aboutversion, R.id.aboutbuildid, R.id.aboutversion, R.id.aboutbuildid};
    static List<Integer> clickSequence = new LinkedList<Integer>(); // Sequence of clicks for matching with password

    private static final int ATTRIBUTION_REQUEST_CODE = 101;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        super.onCreateDrawer(savedInstanceState, this);

        // mDrawerList.setItemChecked(7,true);//Selection(6);

        mResources = getResources();
        ///mNetworkClient = new NetworkClient(this, null);// used to manage and show alert dialog

        //deal with custom message
        TextView aboutcustomtxt = (TextView) findViewById(R.id.aboutcustom);
        String aboutcustommsg = mResources.getString(R.string.conf_about_custom_message);
        if (!aboutcustommsg.equals("EMPTY"))
            aboutcustomtxt.setText(aboutcustommsg);
        else
            aboutcustomtxt.setVisibility(View.GONE);

      /*  //deal with copyright text
        mAboutCpRightTxt = (TextView) findViewById(R.id.aboutcpright);
        if (!LanguageManager.isStringSetTrue(this, R.string.conf_about_display_copyright))
            mAboutCpRightTxt.setVisibility(View.GONE);

        //deal with email text
        mAboutEmailTxt = (TextView) findViewById(R.id.aboutsptemail);
        if (!LanguageManager.isStringSetTrue(this, R.string.conf_about_display_email)) {
            mAboutEmailTxt.setVisibility(View.GONE);
        } else {
            mAboutEmailTxt.setOnClickListener(this);
        }

        //deal with Powered By text
        mAboutPoweredByTxt = (TextView) findViewById(R.id.aboutpoweredby);
        if (!LanguageManager.isStringSetTrue(this, R.string.conf_about_display_poweredby))
            mAboutPoweredByTxt.setVisibility(View.GONE);
*/
        //set attribute link
        mAttributionUrl = mResources.getString(R.string.conf_app_attribution_url);
        mAboutAttributionTxt = (TextView) findViewById(R.id.aboutattribution);
        if (mAttributionUrl.startsWith("http://")) {//check validity
            if (mAttributionUrl.indexOf('?') < 0)
                mAttributionUrl += "?"; //no parameters appended yet
            else
                mAttributionUrl += "&"; //some parameters already appended
          ///  mAttributionUrl += "language=" + getDefaultLanguage();

            mAboutAttributionTxt.setOnClickListener(this);
        } else
            mAboutAttributionTxt.setVisibility(View.GONE);

        // set the terms and conditions link
        mTermsConditionsUrl = mResources.getString(R.string.conf_app_tandc_url);
        boolean useAlternateTandC = mResources.getString(R.string.conf_app_use_alternate_tandc_url).equals("true");
        String country = Locale.getDefault().getCountry();
        // use bidi T&C url for ScanLife app
        if(useAlternateTandC && country.equalsIgnoreCase("es")) {
            mTermsConditionsUrl = mResources.getString(R.string.conf_app_alternate_tandc_url);
        }
        mAboutTermsTxt = (TextView) findViewById(R.id.abouttermsconditions);
        if (null != mTermsConditionsUrl && !"".equals(mTermsConditionsUrl.trim())) {//check validity
            mAboutTermsTxt.setOnClickListener(this);
        } else {
            mAboutTermsTxt.setVisibility(View.GONE);
        }

        // set the privacy link
        mPrivacyUrl = mResources.getString(R.string.conf_app_privacypolicy_url);
        mAboutPrivacyTxt = (TextView) findViewById(R.id.aboutprivacy);
        if (null != mPrivacyUrl && !"".equals(mPrivacyUrl.trim())) {//check validity
            mAboutPrivacyTxt.setOnClickListener(this);
        } else {
            mAboutPrivacyTxt.setVisibility(View.GONE);
        }


        mAboutEulaTxt = (TextView) findViewById(R.id.abouteula);
       /* if (LanguageManager.isStringSetTrue(this, R.string.conf_about_display_eula))
            mAboutEulaTxt.setOnClickListener(this);
        else
            mAboutEulaTxt.setVisibility(View.GONE);
*/
        // Set click listeners for clicks password
        findViewById(R.id.aboutbuildid).setOnClickListener(this);
        findViewById(R.id.aboutversion).setOnClickListener(this);
        findViewById(R.id.aboutbuildidvalue).setOnClickListener(this);
        findViewById(R.id.aboutversionvalue).setOnClickListener(this);
        findViewById(R.id.aboutcpright).setOnClickListener(this);
    }

    /**
     * Called when the activity is Resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
       /// mFlurryManager.logPageView(SLFlurryManager.PAGE_ABOUT);
      ///  updateLangText();
      ///  mNetworkClient.resume();
    }

    @Override
    protected void onPause() {
      //  mNetworkClient.pause();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (checkForClickSequenceMatch(id)) {
            EditPreferences(v.getContext()); // open developer back door
        }
    /*    switch (id) {
            case R.id.aboutattribution:
                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();

                if (info != null && info.isConnected()) //check whether Internet connection is available first
                {
                    Intent intent = new Intent();
                    intent.setClass(this, WebViewActivity.class);
                    intent.setData(android.net.Uri.parse(mAttributionUrl));
                    intent.putExtra(WebViewActivity.KEY_PAGE_DISPLAYSTATE, WebViewActivity.WEBVIEW_DISPLAY_STATE_INTERNAL);
                    intent.putExtra(WebViewActivity.KEY_PAGE_NAME, SLFlurryManager.PAGE_ATTRIBUTION);
                    startActivityForResult(intent, ATTRIBUTION_REQUEST_CODE);
                } else {
                    mNetworkClient.showContentRetrievalFailureDialog();
                }
                break;
            case R.id.abouteula:
                Intent intent = new Intent();
                intent.setClass(this, EulaActivity.class);
                intent.putExtra(EulaActivity.KEY_LAUNCH_MODE, false);
                startActivity(intent);
                break;
            case R.id.abouttermsconditions:
                mFlurryManager.logPageView(
                        SLFlurryManager.PAGE_TERMSCONDITION);
                Intent termsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTermsConditionsUrl));
                startActivity(termsIntent);
                break;
            case R.id.aboutprivacy:
                mFlurryManager.logPageView(
                        SLFlurryManager.PAGE_PRIVACY);
                Intent privacyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPrivacyUrl));
                startActivity(privacyIntent);
                break;
            case R.id.aboutsptemail:
                String email = getStringResource(R.string.screen_about_support_email);
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, ""));
                break;*/
       /// }
    }

    private void EditPreferences(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        ///startActivity(new Intent(this, DeveloperPreferencesActivity.class));


    }

    // the password is tapping on the following labels in order
    // version
    // build id
    // version
    // build id
    // doing so will open the developer options back door
    private boolean checkForClickSequenceMatch(int id) {
        clickSequence.add(0, id);

        // check if the sequence matches the password
        int i = clicksPassword.length;
        for (Integer clickId : clickSequence) {
            // since we add to the front of the list we compare with password in reverse order
            if (clickId != clicksPassword[--i]) {
                return false;
            }
            if (i == 0) {
                return true;
            }
        }
        return false; // clicksequence was shorter than the password
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ATTRIBUTION_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            ///mNetworkClient.resume(); //onActivityResult may be called before onResume
           /// mNetworkClient.showContentRetrievalFailureDialog();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
