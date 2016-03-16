/*
 * Copyright (c) 2016. Jorge Pujol - Todos los derechos reservados.
 * Escrito por Jorge Pujol <jpujolji@gmail.com>, marzo 2016.
 */

package com.jpujolji.www.test;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.jpujolji.www.test.database.Database;
import com.jpujolji.www.test.fragments.EntryFragment;
import com.jpujolji.www.test.models.Category;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Database database;
    NavigationView navigationView;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = new Database(MainActivity.this);
        try {
            database = database.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupMenu();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Log.d("Depuracion", "id " + id);

        EntryFragment fragment = (EntryFragment) getSupportFragmentManager().findFragmentByTag("entries");
        if (fragment != null) {
            fragment.setLIst(id);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setupMenu() {
        List<Category> categories = database.getCategories();
        if (categories != null) {
            Menu m = navigationView.getMenu();
            SubMenu subMenu = m.addSubMenu("Categorias");
            subMenu.add(0, 0, 0, "Todos").setCheckable(true);
            for (Category category : categories) {
                subMenu.add(0, category.id, 0, category.description).setCheckable(true);
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, EntryFragment.newInstance(), "entries")
                    .commit();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Menu m = navigationView.getMenu();
        onNavigationItemSelected(m.getItem(0));
        navigationView.setCheckedItem(m.getItem(0).getItemId());
    }
}