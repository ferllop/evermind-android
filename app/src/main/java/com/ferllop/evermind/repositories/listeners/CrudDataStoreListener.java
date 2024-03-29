package com.ferllop.evermind.repositories.listeners;


import com.ferllop.evermind.repositories.fields.DataStoreError;
import com.ferllop.evermind.models.Model;

import java.util.List;

public interface CrudDataStoreListener<T extends Model> {
    void onLoad(T item);
    void onDelete(String id);
    void onError(DataStoreError error);
    void onSave(T item);
    void onLoadAll(List<T> items);
}
