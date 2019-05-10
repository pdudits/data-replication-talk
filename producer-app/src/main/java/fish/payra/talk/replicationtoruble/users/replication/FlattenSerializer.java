
package fish.payra.talk.replicationtoruble.users.replication;

import javax.json.Json;
import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.io.StringReader;

public class FlattenSerializer implements JsonbSerializer<String> {
    @Override
    public void serialize(String obj, JsonGenerator generator, SerializationContext ctx) {
        try (JsonParser jsonParser = Json.createParser(new StringReader(obj))) {
            while (jsonParser.hasNext()) {
                JsonParser.Event event = jsonParser.next();
                switch (event) {
                    case START_OBJECT:
                        generator.writeStartObject();
                        break;

                    case KEY_NAME:
                        generator.writeKey(jsonParser.getString());
                        break;

                    case VALUE_STRING:
                    case VALUE_NUMBER:
                    case VALUE_TRUE:
                    case VALUE_FALSE:
                        generator.write(jsonParser.getValue());
                        break;

                    case VALUE_NULL:
                        generator.writeNull();
                        break;

                    case START_ARRAY:
                        generator.writeStartArray();
                        break;

                    case END_ARRAY:
                    case END_OBJECT:
                        generator.writeEnd();
                }
            }
        }
    }
}
