package com.ferllop.evermind.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ferllop.evermind.AndroidApplication;
import com.ferllop.evermind.R;
import com.ferllop.evermind.activities.fragments.DeleteCardDialogFragment;
import com.ferllop.evermind.models.Card;
import com.ferllop.evermind.repositories.GlobalUser;
import com.ferllop.evermind.repositories.fields.DataStoreError;
import com.ferllop.evermind.models.Subscription;
import com.ferllop.evermind.repositories.SubscriptionRepository;
import com.ferllop.evermind.repositories.listeners.CardDataStoreListener;
import com.ferllop.evermind.controllers.CardController;
import com.ferllop.evermind.repositories.fields.CardField;
import com.ferllop.evermind.repositories.listeners.SubscriptionDataStoreListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardDataActivity extends AppCompatActivity implements
        CardDataStoreListener, SubscriptionDataStoreListener, DeleteCardDialogFragment.DeleteDialogListener {
    final private String TAG = "MYAPP-CardDataActivity";

    boolean isNew = false;
    EditText question;
    EditText answer;
    EditText labels;
    Button save;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_data);

        question = findViewById(R.id.questionTextMultiLine);
        answer = findViewById(R.id.answerTextMultiLine);
        labels = findViewById(R.id.labelsText);
        save = findViewById(R.id.saveButton);
        delete = findViewById(R.id.deleteButton);
        delete.setEnabled(false);

        String id = this.getIntent().getStringExtra(CardField.ID.getValue());
        if(id != null){
            new CardController(this).load(id);
        } else {
            isNew = true;
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(question.getText().toString().isEmpty() ||
                            answer.getText().toString().isEmpty() ||
                            labels.getText().toString().isEmpty()
                    ){
                        showToast("All fields are required");
                    } else {
                        new CardController(CardDataActivity.this).insert(
                                GlobalUser.getInstance().getUser().getId(),
                                GlobalUser.getInstance().getUser().getUsername(),
                                question.getText().toString(),
                                answer.getText().toString(),
                                labels.getText().toString()
                        );
                    }
                }
            });
        }
    }

    @Override
    public void onLoad(Card card) {
        question.setText(card.getQuestion());
        answer.setText(card.getAnswer());
        labels.setText(card.getLabelling().toString());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CardController(CardDataActivity.this).update(
                        card.getId(), card.getAuthorID(), card.getAuthorUsername(),
                        question.getText().toString(),
                        answer.getText().toString(),
                        labels.getText().toString()
                );
            }
        });
        delete.setEnabled(true);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onDeleteClick: cardId -> " + card.getId());
                openDeleteConfirmDialog(card.getId());
            }
        });
    }

    public void openDeleteConfirmDialog(String cardID){
        DialogFragment dialog = new DeleteCardDialogFragment(cardID);
        dialog.show(getSupportFragmentManager(), "DeleteDialogFragment");
    }

    @Override
    public void onLoad(Subscription subscription) {

    }

    @Override
    public void onLoadAll(List<Subscription> subscriptions) {

    }

    @Override
    public void onDelete(String id) {
        this.showToast(getString(R.string.card_deleted));
        new SubscriptionRepository(this).deleteSubscriptionsWithCardID(id);
        finish();
    }

    @Override
    public void onError(DataStoreError error) {
        Map<DataStoreError, String> errorMessages = new HashMap<>();
        errorMessages.put(DataStoreError.ON_INSERT, getString(R.string.error_inserting_card));
        errorMessages.put(DataStoreError.ON_LOAD, getString(R.string.error_loading_card));
        errorMessages.put(DataStoreError.ON_LOAD_ALL, getString(R.string.error_loading_all_cards));
        errorMessages.put(DataStoreError.ON_SEARCH, getString(R.string.error_searching_cards));
        errorMessages.put(DataStoreError.ON_UPDATE, getString(R.string.error_updateing_card));
        errorMessages.put(DataStoreError.ON_DELETE, getString(R.string.error_deleting_card));
        this.showToast(errorMessages.get(error));
    }

    @Override
    public void onSave(Subscription subscription) {

    }

    @Override
    public void onSave(Card card) {
        this.showToast(getString(R.string.card_saved));
        if (isNew){
            new SubscriptionRepository((SubscriptionDataStoreListener) this)
                    .subscribeUserToCard(GlobalUser.getInstance().getUser().getId(), card.getId());
            finish();
        }
    }

    @Override
    public void onLoadAllCards(List<Card> cards) {

    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteDialogConfirmClick(DialogFragment dialog, String cardID) {
        new CardController(CardDataActivity.this).delete(cardID);
    }

    @Override
    public void onDialogCancelClick(DialogFragment dialog) {

    }
}