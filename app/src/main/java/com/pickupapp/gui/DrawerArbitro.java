package com.pickupapp.gui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import com.pickupapp.R;
import com.pickupapp.gui.fragments.PerfilFragment;
import com.pickupapp.gui.fragments.WelcomeFragment;
import com.pickupapp.infra.Sessao;

public class DrawerArbitro extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fragmentManager;
    private View hview;

    @Override
    protected void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_drawer_arbitro);
        Toolbar toolbar = findViewById(R.id.toolbar_arbitro);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        hview = navigationView.getHeaderView(0);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.nav_host_fragment, new WelcomeFragment());
        transaction.commitAllowingStateLoss();
        setTitle("");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_home_arbitro){
            fragment = new PerfilFragment();
        }else if (id == R.id.nov_logout_arbitro){
            new Sessao().clear(getApplicationContext());
            Intent i = new Intent(DrawerArbitro.this, Login.class);
            startActivity(i);
            finish();
            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_arbitro);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}