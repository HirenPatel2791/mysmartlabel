package scanlife.com.smartlabel_android.navDrawer.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import scanlife.com.smartlabel_android.R;

/**
 * Created by hiren on 4/29/16.
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = ifAboutUseSmallLayout(position, mInflater);

            // hide certain buttons based on settings like (bidi or not, etc)
           /* hideCouponsIfNotUsed(position, convertView);
            hideRewardsIfNotUsed(position, convertView);
            hideDealsIfNotUsed(position, convertView);
            hideFeedsIfNotUsed(position, convertView);*/
        }

        setIconTitleAndCount(position, convertView);

        return convertView;
    }

    private void setIconTitleAndCount(int position, View convertView) {
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
        txtTitle.setText(navDrawerItems.get(position).getTitle());

        // displaying count
        // check whether it set visible or not
        if(navDrawerItems.get(position).getCounterVisibility()){
            txtCount.setText(navDrawerItems.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }
    }

/*    private void hideDealsIfNotUsed(int position, View convertView) {
        if(navDrawerItems.get(position).getTitle().toString().equalsIgnoreCase("Deals")) {
            boolean useDeals = false;
            if(!useDeals) {
                hideView(convertView);
            }
        }
    }

    private void hideRewardsIfNotUsed(int position, View convertView) {
        String rewardsButtonStr = context.getResources().getString(R.string.title_activity_rewards);
        if(navDrawerItems.get(position).getTitle().toString().equalsIgnoreCase(rewardsButtonStr)) {
            String useRewardsStr = context.getString(R.string.conf_rewards_enabled);
            boolean useRewards =  useRewardsStr.equalsIgnoreCase("true") ? true : false;

            if(!useRewards) {
                hideView(convertView);
            }
        }
    }

    private void hideCouponsIfNotUsed(int position, View convertView) {
        String buttonStr = context.getResources().getString(R.string.title_activity_coupons);
        if(navDrawerItems.get(position).getTitle().toString().equalsIgnoreCase(buttonStr)) {
            String useStr = context.getString(R.string.conf_coupons_enabled);
            boolean useCoupons =  useStr.equalsIgnoreCase("true") ? true : false;

            if(!useCoupons) {
                hideView(convertView);
            }
        }
    }

    private void hideFeedsIfNotUsed(int position, View convertView) {
        String feedStr = context.getResources().getString(R.string.title_activity_feed);
        if(navDrawerItems.get(position).getTitle().toString().equalsIgnoreCase(feedStr)) {
            String useStr = context.getString(R.string.conf_feeds_enabled);
            boolean useCoupons =  useStr.equalsIgnoreCase("true") ? true : false;

            if(!useCoupons) {
                hideView(convertView);
            }
        }
    }*/

    private View ifAboutUseSmallLayout(int position, LayoutInflater mInflater) {
        View convertView;
        String about = context.getResources().getString(R.string.about_smartlabel);
        if(navDrawerItems.get(position).getTitle().toString().equalsIgnoreCase(about)){
            convertView = mInflater.inflate(R.layout.drawer_list_info, null);
        } else {
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
        return convertView;
    }

    private void hideView(View convertView) {
        convertView.setClickable(false);
        convertView.setEnabled(false);
        convertView.setVisibility(View.GONE);
        ViewGroup.LayoutParams layoutParams = new AbsListView.LayoutParams(1, 1);
        convertView.setLayoutParams(layoutParams);
    }

}
