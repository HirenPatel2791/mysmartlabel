package scanlife.com.smartlabel_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import scanlife.com.smartlabel_android.navDrawer.base.NavigationDrawerBaseActivity;

public class MainActivity extends NavigationDrawerBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

 //       Intent intent = new Intent(this, NavigationDrawerBaseActivity.class);
 //       startActivity(intent);
        super.onCreateDrawer(savedInstanceState, this);

    }
}
