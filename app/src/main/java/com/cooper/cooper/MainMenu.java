package com.cooper.cooper;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;

public class MainMenu extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        this.setupViewPager();

        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        this.tabLayout.setupWithViewPager(viewPager);
        this.setIconTabs();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Coops_Fragment());
        adapter.addFragment(new Coops_Fragment(), "Test");
        this.viewPager.setAdapter(adapter);
    }

    private void setIconTabs() {
        this.tabLayout.getTabAt(0).setIcon(R.drawable.cooper_icon);
    }
}
