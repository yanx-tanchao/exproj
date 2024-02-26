package cn.exp.proj.common.core.formatter;

import cn.exp.proj.common.core.util.TimeUtil;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class TimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        if (Objects.isNull(jsonParser.getText())) {
            return null;
        }
        return TimeUtil.millsToDateTime(Long.parseLong(jsonParser.getText()));
    }
}
