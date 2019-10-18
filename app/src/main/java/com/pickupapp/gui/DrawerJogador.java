package com.pickupapp.gui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.pickupapp.R;
import com.pickupapp.gui.fragments.ListSpacesFragment;
import com.pickupapp.gui.fragments.RegisterSpaceFragment;
import com.pickupapp.gui.fragments.WelcomeFragment;
import com.pickupapp.infra.Sessao;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class DrawerJogador extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_jogador);
        Toolbar toolbar = findViewById(R.id.toolbar_jogador);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout_jogador);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.nav_host_fragment, new WelcomeFragment());
        transaction.commitAllowingStateLoss();
        setTitle("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_jogador, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_home_jogador){
//            fragment = new ListSpacesFragment();
        }else if (id == R.id.nav_locais_jogador){
            fragment = new ListSpacesFragment();
        }else if (id == R.id.nav_logout_jogador) {
            new Sessao().clear(getApplicationContext());
            Intent i = new Intent(DrawerJogador.this, Login.class);
            startActivity(i);
            finish();
            return true;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_jogador);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
