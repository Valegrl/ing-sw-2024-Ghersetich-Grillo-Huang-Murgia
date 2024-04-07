package it.polimi.ingsw.model.Deck.factory;

import com.google.gson.*;
import it.polimi.ingsw.model.Evaluator.*;

import java.lang.reflect.Type;

public class EvaluatorDeserializer implements JsonDeserializer<Evaluator> {

    @Override
    public Evaluator deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        if(json == null) return null;
        JsonObject jsonObject = json.getAsJsonObject();
        String evaluatorType = jsonObject.get("type").getAsString();

        switch (evaluatorType) {
            case ("BasicEvaluator"):
                return context.deserialize(json, BasicEvaluator.class);
            case ("CornerEvaluator"):
                return context.deserialize(json, CornerEvaluator.class);
            case ("ItemEvaluator"):
                return context.deserialize(json, ItemEvaluator.class);
            case ("PatternEvaluator"):
                return context.deserialize(json, PatternEvaluator.class);
        }
        return null;
    }
}
