package com.pickupapp.gui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;



import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.navigation.NavigationView;
import com.pickupapp.R;
import com.pickupapp.gui.fragments.ListArbiterFragment;
import com.pickupapp.gui.fragments.ListBookingFragment;
import com.pickupapp.gui.fragments.ListPlayersFragment;
import com.pickupapp.gui.fragments.ListSpacesFragment;
import com.pickupapp.gui.fragments.MapsFragment;
import com.pickupapp.gui.fragments.PerfilFragment;
import com.pickupapp.gui.fragments.RegisterSpaceFragment;
import com.pickupapp.gui.fragments.ReservaFragment;
import com.pickupapp.gui.fragments.WelcomeFragment;
import com.pickupapp.infra.Sessao;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class DrawerJogador extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentManager fragmentManager;
    private static final int PERMISSAO_REQUERIDA = 1;
    private View hView;


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
        hView = navigationView.getHeaderView(0);
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
            fragment = new PerfilFragment();
        }else if (id == R.id.nav_locais_jogador){
            fragment = new ListSpacesFragment();
        }else if (id == R.id.nav_new_maps_acess){
            fragment = new MapsFragment();
        }else if (id == R.id.nav_new_teams){
            fragment = new MapsFragment();
        }else if (id == R.id.nav_reservas){
            fragment = new ListBookingFragment();
        }else if (id == R.id.nav_new_my_team){
            fragment = new MapsFragment();
        }else if (id == R.id.nav_players){
            fragment = new ListPlayersFragment();
        }else if (id == R.id.nav_arbitros){
            fragment = new ListArbiterFragment();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSAO_REQUERIDA: {
                // Se a solicitação de permissão foi cancelada o array vem vazio.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permissão cedida, recria a activity para carregar o mapa, só será executado uma vez
                    this.recreate();

                }

            }
        }
    }

}
