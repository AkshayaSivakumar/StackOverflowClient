package com.example.stackoverflowclient.feed;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.stackoverflowclient.R;
import com.example.stackoverflowclient.base.BaseActivity;
import com.example.stackoverflowclient.login.LoginActivity;
import com.example.stackoverflowclient.feed.fragments.QuestionsByActivityFragment;
import com.example.stackoverflowclient.feed.fragments.QuestionsByCreationFragment;
import com.example.stackoverflowclient.feed.fragments.QuestionsByHotFragment;
import com.example.stackoverflowclient.feed.fragments.QuestionsByVoteFragment;
import com.example.stackoverflowclient.feed.adapters.ViewPagerAdapter;
import com.example.stackoverflowclient.userquestions.UserQuestionsActivity;
import com.example.stackoverflowclient.utils.PreferenceHelper;
import com.example.stackoverflowclient.utils.StringConstants;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, IFeedInterface.IFeedView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private PreferenceHelper preference;
    private String accessToken;
    private FeedPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initialize();
    }

    private void initialize() {
        setUpViews();

        preference = PreferenceHelper.getInstance(this);
        accessToken = preference.get(StringConstants.ACCESS_TOKEN);

        setMenuItems();

        presenter = new FeedPresenter(this, this);
    }

    private void setUpViews() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        addTabsToTabLayout();
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new QuestionsByActivityFragment(), "ACTIVITY");
        adapter.addFragment(new QuestionsByCreationFragment(), "CREATION");
        adapter.addFragment(new QuestionsByHotFragment(), "HOT");
        adapter.addFragment(new QuestionsByVoteFragment(), "VOTES");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
    }

    private void addTabsToTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("ACTIVITY"));
        tabLayout.addTab(tabLayout.newTab().setText("VOTES"));
        tabLayout.addTab(tabLayout.newTab().setText("HOT"));
        tabLayout.addTab(tabLayout.newTab().setText("CREATION"));
    }

    private void setMenuItems() {
        if (null == accessToken || "".equals(accessToken)) {
            navigationView.getMenu().findItem(R.id.login).setVisible(true);
            navigationView.getMenu().findItem(R.id.logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.user_questions).setVisible(false);
        } else {
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
            navigationView.getMenu().findItem(R.id.logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.user_questions).setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showExitAlert();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.login) {
            goToNextScreen(this, LoginActivity.class);
            finish();
        } else if (id == R.id.logout) {
            logout();
        } else if (id == R.id.user_questions) {
            Bundle bundle = new Bundle();
            bundle.putString(StringConstants.ACCESS_TOKEN, accessToken);
            goToNextScreen(MainActivity.this, UserQuestionsActivity.class, bundle);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void alertDialogPositiveClicked() {
        showProgress(this, "Loading...");
        presenter.initiateLogout(accessToken);
    }

    @Override
    public void hideProgressView() {
        hideProgress();
    }

    @Override
    public void showAlertMessage(String errorMessage) {
        showAlertDialog(getString(R.string.alert_title), errorMessage, getString(R.string.neutral_btn_text));
    }

    /**
     * Since Stackoverflow API doesn't provide a graceful way to logout, the accessToken is invalidated and webview cache is cleared
     * References:
     * https://meta.stackexchange.com/questions/270938/how-to-logout-using-the-api
     * https://stackapps.com/questions/2831/how-to-provide-a-logout-button-for-authentication
     */
    @Override
    public void handleLogoutResponse() {
        preference.put(StringConstants.ACCESS_TOKEN, null);
        accessToken = preference.get(StringConstants.ACCESS_TOKEN);
        setMenuItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
