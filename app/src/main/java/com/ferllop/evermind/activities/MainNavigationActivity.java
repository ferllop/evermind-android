package com.ferllop.evermind.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ferllop.evermind.R;
import com.ferllop.evermind.repositories.UserRepository;

public class MainNavigationActivity extends AppCompatActivity {

    final String TAG = "MYAPP-mainNav";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        ActionBar toolbar = getSupportActionBar();
        Log.d(TAG, "onCreateOptionsMenu() called with: menu = [" +  "]");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected: " + id);
        switch (id) {
            case R.id.action_create_card:
                startActivity(new Intent(getApplicationContext(), CardDataActivity.class));
                return true;
            case R.id.action_search:
                startActivity(new Intent(getApplicationContext(), SearchCardsActivity.class));
            case R.id.action_profile:
            case R.id.action_my_cards:
            case R.id.action_my_subscriptions:
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                new UserRepository(null).signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}