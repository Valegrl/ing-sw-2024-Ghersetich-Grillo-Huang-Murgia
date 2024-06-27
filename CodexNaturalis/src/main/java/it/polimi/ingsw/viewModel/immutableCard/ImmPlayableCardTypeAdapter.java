package it.polimi.ingsw.viewModel.immutableCard;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * This class is a custom deserializer for the ImmPlayableCard class.
 * It implements JsonDeserializer interface provided by Gson.
 * The deserialize method converts a JsonElement back into an ImmPlayableCard object.
 */
public class ImmPlayableCardTypeAdapter implements JsonDeserializer<ImmPlayableCard> {

    /**
     * This method is used to convert a JsonElement back into an ImmPlayableCard object.
     *
     * @param json The JsonElement being deserialized.
     * @param typeOfT The type of the Object to deserialize to.
     * @param context The context for deserialization.
     * @return An ImmPlayableCard object corresponding to the specified JsonElement.
     * @throws JsonParseException if json is not in the expected format of ImmPlayableCard.
     */
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
