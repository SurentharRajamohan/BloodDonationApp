package com.blooddonationapp.startactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.MainActivity_TB_toolbar);

        setSupportActionBar(toolbar);



        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.MainActivity_NHF_fragmentContainer);
        NavController navController = host.getNavController();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);

        setupBottomNavMenu(navController);
    }


    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this,R.id.MainActivity_NHF_fragmentContainer).navigateUp();
    }

    private void  setupBottomNavMenu(NavController navController){
        BottomNavigationView bottomNav = findViewById(R.id.MainActivity_BN_bottomNav);
        NavigationUI.setupWithNavController(bottomNav,navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        try {
            Navigation.findNavController(this,R.id.MainActivity_NHF_fragmentContainer).navigate(item.getItemId());
            return true;

        } catch (Exception ex) {
            return super.onOptionsItemSelected(item);

        }
    }
}