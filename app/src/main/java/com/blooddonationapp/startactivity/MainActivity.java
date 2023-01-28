package com.blooddonationapp.startactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.blooddonationapp.startactivity.Fragment.developer_tools;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adminChecker();

        Toolbar toolbar = findViewById(R.id.MainActivity_TB_toolbar);
        setSupportActionBar(toolbar);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.MainActivity_NHF_fragmentContainer);
        NavController navController = host.getNavController();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        setupBottomNavMenu(navController);
    }

    private void adminChecker() {
        SharedPreferences sharedPref = getSharedPreferences("userCredentials", 0);
        isAdmin = sharedPref.getBoolean("isAdmin", false);
    }


    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.MainActivity_NHF_fragmentContainer).navigateUp();
    }

    private void setupBottomNavMenu(NavController navController) {
        BottomNavigationView bottomNav = findViewById(R.id.MainActivity_BN_bottomNav);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu if the user is admin:
        if (isAdmin)
            getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (isAdmin) {
                switch (item.getItemId()) {
                    case R.id.overflow_menu_registerAdmins:
                        startActivity(new Intent(this, AddAdminActivity.class));
                        return true;
                    case R.id.overflow_menu_sendRequest:
                        switchFragmentRequestBlood();
                        return true;
                    case R.id.overflow_menu_addEvent:
                        startActivity(new Intent(this, AddEventActivity.class));
                }
            }
//            Navigation.findNavController(this,R.id.MainActivity_NHF_fragmentContainer).navigate(item.getItemId());
            return true;
        } catch (Exception ex) {
            return super.onOptionsItemSelected(item);
        }

    }

    public void switchFragmentRequestBlood() {
        developer_tools fragment = new developer_tools();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainActivity_NHF_fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}