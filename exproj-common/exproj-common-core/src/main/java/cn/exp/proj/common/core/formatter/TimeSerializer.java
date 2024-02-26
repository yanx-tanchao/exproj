package cn.exp.proj.common.core.formatter;

import cn.exp.proj.common.core.util.TimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

public class TimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator generator, SerializerProvider serializerProvider)
            throws IOException {
        generator.writeNumber(TimeUtil.toEpochMilli(localDateTime));
    }
}
