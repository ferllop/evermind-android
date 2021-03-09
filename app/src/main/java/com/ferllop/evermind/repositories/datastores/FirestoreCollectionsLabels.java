package com.ferllop.evermind.repositories.datastores;

public enum FirestoreCollectionsLabels {
    CARD("cards"),
    USER("users"),
    SUBSCRIPTION("subscriptions");

    private final String value;

    FirestoreCollectionsLabels(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
