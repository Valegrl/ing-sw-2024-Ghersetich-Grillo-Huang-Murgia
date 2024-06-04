package it.polimi.ingsw.viewModel.immutableCard;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ImmPlayableCardTypeAdapter implements JsonDeserializer<ImmPlayableCard> {

    @Override
    public ImmPlayableCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        if (type.equals("GOLD"))
            return context.deserialize(json, ImmGoldCard.class);
        else if (type.equals("RESOURCE"))
            return context.deserialize(json, ImmResourceCard.class);
        else
            throw new JsonParseException("Invalid card type");
    }
}
