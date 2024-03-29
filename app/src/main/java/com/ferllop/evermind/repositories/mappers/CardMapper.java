package com.ferllop.evermind.repositories.mappers;

import com.ferllop.evermind.models.Card;
import com.ferllop.evermind.models.Labelling;
import com.ferllop.evermind.repositories.fields.CardField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardMapper implements ModelMapper<Card>{
    @Override
    public Card execute(String id, Map<String, Object> map) {
        String authorID = (String) map.get(CardField.AUTHOR_ID.getValue());
        String authorUsername = (String) map.get(CardField.AUTHOR_USERNAME.getValue());
        String question = (String) map.get(CardField.QUESTION.getValue());
        String answer = (String) map.get(CardField.ANSWER.getValue());
        Map<String, Boolean> rawLabels = (Map<String, Boolean>) map.get(CardField.LABELLING.getValue());
        Labelling labelling = new Labelling(booleanMapToList(rawLabels));
        return new Card(id, authorID, authorUsername, question, answer, labelling);
    }

    @Override
    public Map<String, Object> execute(Card card) {
        Map<String, Object> result = new HashMap<>();
        result.put(CardField.AUTHOR_ID.getValue(), card.getAuthorID());
        result.put(CardField.AUTHOR_USERNAME.getValue(), card.getAuthorUsername());
        result.put(CardField.QUESTION.getValue(), card.getQuestion());
        result.put(CardField.ANSWER.getValue(), card.getAnswer());
        result.put(CardField.LABELLING.getValue(), listToBooleanMap(card.getLabelling().getLabels()));
        return result;
    }

    private List<String> booleanMapToList(Map<String, Boolean> rawLabels) {
        List<String> result = new ArrayList<>();
        for(Map.Entry<String, Boolean> label : rawLabels.entrySet()){
            result.add(label.getKey());
        }
        return result;
    }


    private Map<String, Boolean> listToBooleanMap(List<String> labels) {
        Map<String, Boolean> result = new HashMap<>();
        for(String label : labels){
            if(!label.trim().isEmpty()) {
                result.put(label.trim(), true);
            }
        }
        return result;
    }
}
