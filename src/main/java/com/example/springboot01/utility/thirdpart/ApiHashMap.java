package com.example.springboot01.utility.thirdpart;

import com.example.springboot01.utility.TUtil;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * api hashmap
 */
public class ApiHashMap extends HashMap<String, String> {

  private static final long serialVersionUID = 5458024332180994723L;

  public ApiHashMap() {
  }

  public ApiHashMap(Map<? extends String, ? extends String> m) {
    super(m);
  }

  public String put(String key, Object value) {
    if (value == null) {
      return null;
    }
    return this.put(key, value.toString());
  }

  public String put(String key, Date value, String format, String timezone) {
    if (value == null) {
      return null;
    }
    DateFormat df = new SimpleDateFormat(format);
    df.setTimeZone(TimeZone.getTimeZone(timezone));

    return this.put(key, df.format(value));
  }

  @Override
  public String put(String key, String value) {
    return TUtil.anyNull(key, value) ? null : super.put(key, value);
  }

  public ApiHashMap charset(Charset charset) {
    Set<Entry<String, String>> entries = entrySet();
    for (Entry<String, String> e : entries) {
      put(e.getKey(), new String(e.getValue().getBytes(), charset));
    }
    return this;
  }

}
