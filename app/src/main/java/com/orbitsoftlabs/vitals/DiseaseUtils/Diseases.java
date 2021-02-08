package com.orbitsoftlabs.vitals.DiseaseUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.orbitsoftlabs.vitals.DiseaseUtils.TabUtils.DiseaseList;
import com.orbitsoftlabs.vitals.DiseaseUtils.TabUtils.DiseaseStatistics;
import com.orbitsoftlabs.vitals.DiseaseUtils.TabUtils.TabAdapter;
import com.orbitsoftlabs.vitals.HelperUtil;
import com.orbitsoftlabs.vitals.HomeUtils.HomeConstituents;
import com.orbitsoftlabs.vitals.HomeUtils.HomeHealthWorker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.orbitsoftlabs.vitals.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class Diseases extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private Class aClass;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String last_activity;

    private FloatingActionButton fab;

    private AppBarLayout appBarLayout;

    private Toolbar toolbar;
    private boolean app_bar_expanded = true;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseases);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new DiseaseList(), "Diseases");
        adapter.addFragment(new DiseaseStatistics(), "Statistics");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(this);

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                if (position == 0){
                    HelperUtil.fadeOut(fab, Diseases.this);
                    fab.setVisibility(View.INVISIBLE);
                    fab.callOnClick();
                } else {
                    HelperUtil.fadeIn(fab, Diseases.this);
                    fab.setVisibility(View.VISIBLE);
                    fab.callOnClick();
                }
            }
        });


        toolbar.setBackgroundColor( getResources().getColor(android.R.color.transparent) );
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        try {
            Intent i = getIntent();
            last_activity = i.getStringExtra("activity");
            Log.d("Last activity", last_activity+"");

            if (last_activity.contains("Worker")){
                aClass = HomeHealthWorker.class;
            }   else {
                aClass = HomeConstituents.class;
            }
        }catch (Exception e){

        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                //highLightCurrentTab(position); // for tab change
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset)
    {
        if (offset == 0)
        {
            // Fully expanded
            fab.setOnClickListener(v -> {
                    appBarLayout.setExpanded(false, true);
                    Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_fade_in);
                    toolbar.startAnimation(aniFade);
                    fab.setImageResource( R.drawable.ic_outline_fullscreen_exit_24px );
                    toolbar.setBackgroundColor( getResources().getColor(R.color.colorPrimaryDark) );
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

                    //fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.my_app_color_on_primary)));
            });
            Animation aniFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_fade_out);
            toolbar.startAnimation(aniFadeOut);
            fab.setImageResource( R.drawable.ic_outline_fullscreen_24px );
            toolbar.setBackgroundColor( getResources().getColor(android.R.color.transparent) );
            toolbar.setTitleTextColor(getResources().getColor(R.color.my_app_color_on_primary));
        }
        else
        {
            fab.setOnClickListener(v -> {
                appBarLayout.setExpanded(true, true);
                Animation aniFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_fade_out);
                toolbar.startAnimation(aniFadeOut);
                fab.setImageResource( R.drawable.ic_outline_fullscreen_24px );
                toolbar.setBackgroundColor( getResources().getColor(android.R.color.transparent) );
                toolbar.setTitleTextColor(getResources().getColor(R.color.my_app_color_on_primary));

                //fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.my_app_color_on_primary)));
                // Not fully expanded or collapsed
            });
            Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_fade_in);
            toolbar.startAnimation(aniFade);
            fab.setImageResource( R.drawable.ic_outline_fullscreen_exit_24px );
            toolbar.setBackgroundColor( getResources().getColor(R.color.colorPrimaryDark) );
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        HelperUtil.backNavigationFinish(this, aClass);
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
}
