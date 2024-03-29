package com.ferllop.evermind.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ferllop.evermind.R;
import com.ferllop.evermind.repositories.UserRepository;

public abstract class MainNavigationActivity extends AppCompatActivity {

    final String TAG = "MYAPP-mainNav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null) {
            toolbar.setTitle(getNavBarTitle());
        }
    }

    abstract protected String getNavBarTitle();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_card:
                startActivity(new Intent(getApplicationContext(), CardDataActivity.class));
                return true;
            case R.id.action_home:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                return true;
            case R.id.action_search:
                startActivity(new Intent(getApplicationContext(), SearchCardsActivity.class));
                return true;
            case R.id.action_my_cards:
                startActivity(new Intent(getApplicationContext(), MyCards.class));
                return true;
            case R.id.action_my_subscriptions:
                startActivity(new Intent(getApplicationContext(), MySubscriptions.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            case R.id.action_logout:
                new UserRepository(null).signOut(this);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}