package com.ferllop.evermind.repositories.datastores.technologies;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ferllop.evermind.models.DataStoreError;
import com.ferllop.evermind.models.Model;
import com.ferllop.evermind.repositories.DatastoreListener;
import com.ferllop.evermind.repositories.fields.CardField;
import com.ferllop.evermind.repositories.mappers.ModelMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirestoreDataStore<T extends Model> extends DataStore<T> {
    final private String TAG = "Firestore Service";

    String collection;

    public FirestoreDataStore(String collection, ModelMapper mapper, DatastoreListener<T> listener) {
        super(mapper, listener);
        this.collection = collection;
    }

    @Override
    public void insert(T item) {
        FirebaseFirestore.getInstance().collection(collection)
                .add(mapper.execute(item))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        listener.onSave();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        listener.onError(DataStoreError.ON_INSERT);
                    }
                });
    }

    @Override
    public void load(String id){
        FirebaseFirestore.getInstance().collection(collection).document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                listener.onLoad(mapper.execute(document.getId(), document.getData()));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "failed loading card");
                listener.onError(DataStoreError.ON_LOAD);
            }
        });
    }


    @Override
    public void getAll(){
        FirebaseFirestore.getInstance().collection(collection).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            listener.onLoad(mapper.execute(document.getId(), document.getData()));
                        }
                    } else {
                        Log.d(TAG, "failed getting all cards");
                        listener.onError(DataStoreError.ON_LOAD_ALL);
                    }
                });
    }

    @Override
    public void getFromSearch(String query) {
        Search search = new Search(query);

        Query dbQuery = FirebaseFirestore.getInstance().collection(collection);
        if(search.hasLabels()){
            for(String label: search.getLabels()){
                dbQuery = dbQuery.whereEqualTo(CardField.LABELLING.getValue()+"."+label, true);
            }
        }
        if(search.hasAuthor()){
            dbQuery = dbQuery.whereEqualTo(CardField.AUTHOR.getValue(), search.getAuthor());
        }

        dbQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        listener.onLoad(mapper.execute(document.getId(), document.getData()));
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    listener.onError(DataStoreError.ON_SEARCH);
                }
            }
        });
    }

    @Override
    public void update(String id, T item) {
        FirebaseFirestore.getInstance().collection(collection).document(id)
                .set(mapper.execute(item))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        listener.onSave();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        listener.onError(DataStoreError.ON_UPDATE);
                    }
                });
    }

    @Override
    public void delete(String id) {
        FirebaseFirestore.getInstance().collection(collection).document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        listener.onDelete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        listener.onError(DataStoreError.ON_DELETE);
                    }
                });
    }
}