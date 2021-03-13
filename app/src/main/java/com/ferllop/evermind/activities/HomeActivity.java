package com.ferllop.evermind.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ferllop.evermind.AndroidApplication;
import com.ferllop.evermind.R;
import com.ferllop.evermind.models.DataStoreError;
import com.ferllop.evermind.models.Subscription;
import com.ferllop.evermind.repositories.SubscriptionFirestoreRepository;
import com.ferllop.evermind.repositories.SubscriptionsGlobal;
import com.ferllop.evermind.repositories.datastores.SubscriptionListener;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements SubscriptionListener {
    final String TAG = "MYAPP-Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getAllSubsFromUser();

        Button searchButton = findViewById(R.id.search_cards_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SearchCardsActivity.class));
            }
        });


        Button createButton = findViewById(R.id.create_card_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CardDataActivity.class));
            }
        });

        ((TextView) findViewById(R.id.logo_textView))
                .setText(AndroidApplication.getUser(this));

    }

    private void getAllSubsFromUser() {
        ((Button) findViewById(R.id.review_cards_button)).setEnabled(false);
        findViewById(R.id.review_cards_textView).setVisibility(View.INVISIBLE);
        //new SubscriptionFirestoreRepository(null, this).getCountForToday(AndroidApplication.getUserID(this));
        new SubscriptionFirestoreRepository(this).getAllFromUser(AndroidApplication.getUserID(this));
    }

    @Override
    public void onRestart() {
        super.onRestart();
        getAllSubsFromUser();
        Log.d(TAG, "onResume");
    }

    private void setCount(int count) {
        Log.d(TAG, "setCount: " + count);
        TextView view = findViewById(R.id.review_cards_textView);
        view.setVisibility(View.INVISIBLE);
        String cardsToReview = getString(R.string.cards_to_review);
        cardsToReview = cardsToReview.replace("#qty", String.valueOf(count));
        if(count == 1){
            cardsToReview = cardsToReview.replace("#s", "");
        } else {
            cardsToReview = cardsToReview.replace("#s", "s");
        }
        view.setText(cardsToReview);
        view.setVisibility(View.VISIBLE);

        Button start = (Button) findViewById(R.id.review_cards_button);

        if(count > 0){
            start.setEnabled(true);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this, ReviewActivity.class));
                }
            });
        }
    }

    @Override
    public void onLoad(List<Subscription> subs) {
        SubscriptionsGlobal.getInstance().setAllSubscriptions(subs);
        List<Subscription> subsForToday = SubscriptionsGlobal.getInstance().getSubscriptionsForToday();
        setCount(subsForToday.size());
    }

    @Override
    public void onLoad(Subscription subscription) {

    }

    @Override
    public void onNotFound() {

    }

    @Override
    public void onError(DataStoreError error) {

    }

    @Override
    public void onSave(Subscription subscription) {

    }

    @Override
    public void onDelete(String subscriptionID) {

    }

}