package com.example.springboot01.utility;

import com.example.springboot01.constant.TConst;
import com.example.springboot01.exception.TException;
import com.example.springboot01.response.ResponseEnum;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Jackson 工具包
 */
public class JsonUtil {


  public static ObjectMapper MAPPER = new ObjectMapper();

  public static ObjectMapper YML_MAPPER = new ObjectMapper(new YAMLFactory());

//  public static ObjectMapper XML_MAPPER = new XmlMapper();

  static {

    // 不序列化null字段
//    MAPPER.setSerializationInclusion(Include.NON_NULL);

    // 解序列化时不处理未知字段
    MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    JavaTimeModule module = new JavaTimeModule();
    module.addSerializer(LocalDate.class, new LocalDateSerializer(TConst.DATE_FORMAT));
    module.addDeserializer(LocalDate.class, new LocalDateDeserializer(TConst.DATE_FORMAT));
    module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(TConst.DATETIME_FORMAT));
    module.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(TConst.DATETIME_FORMAT));
    //deal string
    module.addDeserializer(String.class,new TStringTrimmerDeserializer());
    MAPPER.registerModule(module);

    // 仅处理成员变量
    MAPPER.setVisibility(MAPPER.getSerializationConfig().getDefaultVisibilityChecker()
        .withFieldVisibility(Visibility.ANY).withGetterVisibility(Visibility.NONE)
        .withSetterVisibility(Visibility.NONE).withCreatorVisibility(Visibility.NONE)
        .withIsGetterVisibility(Visibility.NONE));
  }

  public static <T> T read(String str, Class<T> clz) {
    try {
      return MAPPER.readValue(str, clz);
    } catch (Exception e) {
      e.printStackTrace();
      throw TException.of(ResponseEnum.JSON_ERROR).with(e);
    }
  }

  public static <T> T read(InputStream in, Class<T> clz) {
    try {
      return MAPPER.readValue(in, clz);
    } catch (Exception e) {
      e.printStackTrace();
      throw TException.of(ResponseEnum.JSON_ERROR).with(e);
    }
  }

  public static String write(Object obj) {
    try {
      return MAPPER.writeValueAsString(obj);
    } catch (Exception e) {
      e.printStackTrace();
      throw TException.of(ResponseEnum.JSON_ERROR).with(e);
    }
  }

  public static JsonNode readTree(String jsonStr) {
    try {
      return MAPPER.readTree(jsonStr);
    } catch (Exception e) {
      throw TException.of(ResponseEnum.JSON_ERROR).with(e);
    }
  }

  public static JsonNode findValue(String jsonStr, String key) {
    try {
      JsonNode result = MAPPER.readTree(jsonStr);
      return result.findValue(key);
    } catch (Exception e) {
      throw TException.of(ResponseEnum.JSON_ERROR).with(e);
    }
  }

  public static List<JsonNode> findValues(String jsonStr, String key) {
    try {
      JsonNode result = MAPPER.readTree(jsonStr);
      return result.findValues(key);
    } catch (Exception e) {
      throw TException.of(ResponseEnum.JSON_ERROR).with(e);
    }
  }
}

class TStringTrimmerDeserializer extends JsonDeserializer<String> {

  @Override
  public String deserialize(JsonParser jp, DeserializationContext deserializationContext)
          throws IOException{
    JsonNode node = jp.getCodec().readTree(jp);
    String text = node.asText();

    String result = null;
    if (text != null){
      String value = text.trim();
      if ("".equals(value)) {
        result = null;
      }
      else {
        result = value;
      }
    }
    return result;
  }

}