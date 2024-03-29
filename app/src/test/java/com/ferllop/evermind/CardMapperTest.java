package com.ferllop.evermind;

import com.ferllop.evermind.models.Card;
import com.ferllop.evermind.models.Labelling;
import com.ferllop.evermind.repositories.fields.CardField;
import com.ferllop.evermind.repositories.mappers.CardMapper;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CardMapperTest {

    String id;
    String authorID;
    String authorUsername;
    String question;
    String answer;
    String label1;
    String label2;
    Map<String, Object> cardAsMap;

    Card cardAsCard;

    @Before
    public void setData(){
        id = "xyz";
        authorID = "TheAuthorID";
        authorUsername = "TheAuthorUsername";
        question = "The question?";
        answer = "The answer";
        label1 = "label1";
        label2 = "label2";
    }

    public void createCardAsCard(){
        cardAsCard = new Card(id, authorID, authorUsername, question, answer, new Labelling(Arrays.asList(label1, label2)));
    }


    public void createCardAsMap(){
         Map<String, Boolean> labelling = new HashMap<>();
        labelling.put(label1, true);
        labelling.put(label2, true);

        cardAsMap = new HashMap<>();
        cardAsMap.put(CardField.AUTHOR_ID.getValue(), authorID);
        cardAsMap.put(CardField.AUTHOR_USERNAME.getValue(), authorUsername);
        cardAsMap.put(CardField.QUESTION.getValue(), question);
        cardAsMap.put(CardField.ANSWER.getValue(), answer);
        cardAsMap.put(CardField.LABELLING.getValue(), labelling);
    }

    @Test
    public void should_convert_a_Card_to_a_String_key_Object_value_Map_with_an_author_username(){
        createCardAsCard();
        assertEquals(authorUsername, new CardMapper().execute(cardAsCard).get(CardField.AUTHOR_USERNAME.getValue()));
    }

    @Test
    public void should_convert_a_Card_to_a_String_key_Object_value_Map_with_an_author_ID(){
        createCardAsCard();
        assertEquals(authorID, new CardMapper().execute(cardAsCard).get(CardField.AUTHOR_ID.getValue()));
    }

    @Test
    public void should_convert_a_Card_to_a_String_key_Object_value_Map_with_a_question(){
        createCardAsCard();
        assertEquals(question, new CardMapper().execute(cardAsCard).get(CardField.QUESTION.getValue()));
    }

    @Test
    public void should_convert_a_Card_to_a_String_key_Object_value_Map_with_an_answer(){
        createCardAsCard();
        assertEquals(answer, new CardMapper().execute(cardAsCard).get(CardField.ANSWER.getValue()));
    }

    @Test
    public void should_convert_a_Card_to_a_String_key_Object_value_Map_with_labels(){
        createCardAsCard();
        Map<String, Boolean> labelling = (Map<String, Boolean>) new CardMapper()
                .execute(cardAsCard)
                .get(CardField.LABELLING.getValue());
        assertTrue(labelling.get(label1));
        assertTrue(labelling.get(label2));
    }

    @Test
    public void should_convert_a_String_key_Object_value_map_to_a_Card_with_an_id(){
        createCardAsMap();
        assertEquals(id, new CardMapper().execute(id, cardAsMap).getId());
    }

    @Test
    public void should_convert_a_String_key_Object_value_map_to_a_Card_with_an_author_ID(){
        createCardAsMap();
        assertEquals("TheAuthorID", new CardMapper().execute(id, cardAsMap).getAuthorID());
    }
    @Test
    public void should_convert_a_String_key_Object_value_map_to_a_Card_with_an_author_username(){
        createCardAsMap();
        assertEquals("TheAuthorUsername", new CardMapper().execute(id, cardAsMap).getAuthorUsername());
    }

    @Test
    public void should_convert_a_String_key_Object_value_map_to_a_Card_with_a_question(){
        createCardAsMap();
        assertEquals("The question?", new CardMapper().execute(id, cardAsMap).getQuestion());
    }

    @Test
    public void should_convert_a_String_key_Object_value_map_to_a_Card_with_an_answer(){
        createCardAsMap();
        assertEquals("The answer", new CardMapper().execute(id, cardAsMap).getAnswer());
    }

    @Test
    public void should_convert_a_String_key_Object_value_map_to_a_Card_with_labels(){
        createCardAsMap();
        assertEquals("label1",
                new CardMapper().execute(id, cardAsMap).getLabelling().getLabels().get(0));
        assertEquals("label2",
                new CardMapper().execute(id, cardAsMap).getLabelling().getLabels().get(1));
    }
}
