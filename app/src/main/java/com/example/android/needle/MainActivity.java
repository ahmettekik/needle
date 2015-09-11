package com.example.android.needle;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity implements ActionBar.TabListener {
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs =  { "All", "Last15" };
    private static final String TAG_ALL_FRAGMENT = "AllFragment";
    private static final String TAG_LAST_15_FRAGMENT = "Last15Fragment";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getActionBar();

        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for(String tab_name: tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if(tab.getText().equals("All")) {
            ft.replace(R.id.container, new AllFragment(), TAG_ALL_FRAGMENT);
        }
        else if(tab.getText().equals("Last15")) {
            ft.replace(R.id.container, new Last15Fragment(), TAG_LAST_15_FRAGMENT);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if(tab.getText().equals("All")) {
            ft.remove(getFragmentManager().findFragmentByTag(TAG_ALL_FRAGMENT));
        }
        else if(tab.getText().equals("Last15")) {
            ft.remove(getFragmentManager().findFragmentByTag(TAG_LAST_15_FRAGMENT));
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
