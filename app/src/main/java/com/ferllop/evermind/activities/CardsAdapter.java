package com.ferllop.evermind.activities;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferllop.evermind.AndroidApplication;
import com.ferllop.evermind.R;
import com.ferllop.evermind.models.Card;
import com.ferllop.evermind.models.Level;
import com.ferllop.evermind.models.Subscription;
import com.ferllop.evermind.repositories.SubscriptionFirestoreRepository;
import com.ferllop.evermind.repositories.SubscriptionsGlobal;
import com.ferllop.evermind.repositories.datastores.SubscriptionListener;
import com.ferllop.evermind.repositories.fields.CardField;
import com.google.firebase.Timestamp;


import java.util.ArrayList;
import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    final String TAG = "MYAPP:CardAdapter";
    List<Card> cards;

    public CardsAdapter() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card){
        cards.add(card);
        notifyDataSetChanged();
    }

    public void clear(){
        cards.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.view_card_preview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.bind(card);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void updateCard(String cardID) {
        for(int i = 0; i < cards.size(); i++){
            Log.d(TAG, "updateCard: " + cards.get(i).getId() + " -- " + cardID);
            if (cards.get(i).getId().equals(cardID)){
                notifyItemChanged(i);
                Log.d(TAG, "updateCard: posicion " + i);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        final String TAG = "MYAPP";
        TextView author;
        TextView labels;
        TextView question;
        TextView answer;
        Button action;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author_textView);
            labels = itemView.findViewById(R.id.labels_textView);
            question = itemView.findViewById(R.id.question_textView);
            answer = itemView.findViewById(R.id.answer_textView);
            action = itemView.findViewById(R.id.search_item_card_action);
        }

        public void bind(Card card) {
            Log.d(TAG, "bind: ");
            author.setText(card.getAuthorUsername());
            labels.setText(card.getLabelling().toString());
            question.setText(card.getQuestion());
            answer.setText(card.getAnswer());
//            Log.d(TAG, AndroidApplication.getUserID(author.getContext()));
//            Log.d(TAG, card.getAuthorID());
            if (!AndroidApplication.getUserID(author.getContext()).equals(card.getAuthorID())){
                String authorID = AndroidApplication.getUserID(author.getContext());
                String cardID = card.getId();
                String subscriptionID = SubscriptionsGlobal.getInstance().getSubscriptionID(authorID, cardID);
                if (subscriptionID != null) {
                    action.setText(R.string.unsubscribe_to_card);
                    action.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "onClick: " + subscriptionID);
                            new SubscriptionFirestoreRepository((SubscriptionListener) author.getContext()).delete(subscriptionID);

                            Log.d(TAG, "onClick: unsuscribe");
                        }
                    });
                } else {
                    action.setText(R.string.subscribe_to_card);
                    action.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!SubscriptionsGlobal.getInstance().isSubscribedTo(cardID)) {
                                Level level = Level.LEVEL_0;
                                Timestamp now = Timestamp.now();
                                Timestamp next = new Timestamp(level.getValue() * 86400 + Timestamp.now().getSeconds(), 0);
                                Subscription sub = new Subscription(authorID, cardID, level, now, next);
                                new SubscriptionFirestoreRepository((SubscriptionListener) author.getContext()).insert(sub);
                                Log.d(TAG, "onClick: suscribe");
                            }
                        }
                    });
                }
            } else {
                action.setText(R.string.edit_button);
                action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), CardDataActivity.class);
                        v.getContext().startActivity(intent.putExtra(CardField.ID.getValue(), card.getId()));
                    }
                });
            }
        }
    }
}
