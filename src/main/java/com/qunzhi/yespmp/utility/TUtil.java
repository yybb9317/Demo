package com.qunzhi.yespmp.utility;

import com.qunzhi.yespmp.exception.TException;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Common Util for this project
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TUtil {

  private final static String secret = "7c313c2da4b9fb0e60814250ff7aa750ecd29f60f2c889d2bc1e963d25ab0448";


  /**
   * encode a string
   * @param str
   * @return
   */
  public static String base64Encode(String str){
    try {
      return new String(Base64.getEncoder().encode(str.getBytes("UTF-8")));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * convert an object to a map (field as key,content as value),
   * usually,we need ignore serialVersionUID and id.
   * @param source An object need to convert.
   * @param ignore The field need to ignore.
   * @param <T> object type
   * @return A map
   */
  public static <T> Map<String,Object> objToMap(T source,String... ignore){
    Map<String,Object> map = new HashMap<>();
    if (source == null) return map;
    Class<?> clz = source.getClass();
    Field[] fields = clz.getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      Field field = fields[i];
      String name = field.getName();
      if (Stream.of(ignore).anyMatch(e -> name.equals(e)))
        continue;
      try {
        PropertyDescriptor propDesc = new PropertyDescriptor(name, clz);
        Method readMethod = propDesc.getReadMethod();
        Object invoke = readMethod.invoke(source);
        map.put(name,invoke);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return map;
  }

  /**
   * get uuid without dashes
   *
   * @return uuid
   */
  public static String uuid() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  /**
   * save password using hmacSha256
   *
   * @param password sha256ed password
   * @return hmacSha256 password with secret key
   */
  public static String saltHash(String password) {
//    if (password.length() != 64) {
//      throw new RuntimeException("Invalid Input Password");
//    }

    SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    String result = Hashing.hmacSha256(key).hashString(password, Charset.forName("UTF-8")).toString();
    return result;
  }

  /**
   *  common split with comma
   * @param str String
   * @return
   */
  public static List<String> split(String str) {
    if (str == null) {
      return Collections.emptyList();
    }
    return Lists.newArrayList(str.split(","));
  }


  /**
   *  check string length equals
   * @param str    String
   * @param length contrast length
   */
  public static void strEqual(String str, int length) {
    if (str.length() != length) {
      throw TException.badRequest();
    }
  }

  /**
   * limit string length
   * @param str     string
   * @param length  length
   */
  public static void strLimit(String str, int length) {
    if (str.length() > length) {
      throw TException.badRequest();
    }
  }

  /**
   *  join objects with comma
   * @param objs java bean
   * @return
   */
  public static String join(Object... objs) {
    return Joiner.on(",").join(objs);
  }

  /**
   * collection to array with generic type
   * @param data  object
   * @param clz   class type
   * @param <T>   object type
   * @return
   */
  public static <T> T[] toArray(Collection<T> data, Class<?> clz) {
    return data.toArray((T[]) Array.newInstance(clz, data.size()));
  }

  /**
   * check if all param is null
   * @param objects the filed need to judge
   * @return
   */
  public static boolean allNull(@Nonnull Object... objects) {
    for (Object o : objects) {
      if (o != null) {
        return false;
      }
    }
    return true;
  }

  /**
   * check if any param is null
   * @param objects the filed need to judge
   * @return
   */
  public static boolean anyNull(@Nonnull Object... objects) {
    for (Object o : objects) {
      if (o == null) {
        return true;
      }
    }
    return false;
  }

  /**
   * check if any param is null
   * @param strs  the filed need to judge
   * @return
   */
  public static boolean anyNull(@Nonnull String... strs) {
    for (String o : strs) {
      if (o == null || o.isEmpty()) {
        return true;
      }
    }
    return false;
  }

  /**
   * find the min price
   * @param prices  the filed need to judge
   * @return
   */
  public static long minPrice(@Nonnull Long... prices) {
    return Arrays.stream(prices).filter(Objects::nonNull).sorted().findFirst().orElse(0L);
  }

  /**
   * generate current time based code
   */
  public static String currentTimeCode() {
    return DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());
  }

  /**
   * Get all available values of enum for generate api doc
   * @param e class of Enum
   * @return  String after Enum splicing
   */
  public static String enames(Class<? extends Enum<?>> e) {
    return Arrays.stream(e.getEnumConstants()).map(Enum::name).collect(Collectors.joining(","));
  }

  /**
   * get all user sets
   *
   * @param objs objects include user id
   */
  public static Set<Long> userSet(Object... objs) {
    Set<Long> userIds = new HashSet<>();
    for (Object o : objs) {
      if (o instanceof Collection) {
        userIds.addAll((Collection) o);
      } else if (o instanceof String) {
        userIds.addAll(Arrays.stream(o.toString().split(",")).filter(e -> !e.isEmpty())
            .map(e -> Long.valueOf(e)).collect(Collectors.toSet()));
      } else if (o instanceof Number) {
        userIds.add(((Number) o).longValue());
      }
    }
    return userIds;
  }

  /**
   * get user from map
   *
   * @param userFunc how to get user with user id
   * @param userStr user ids split by ,
   * @param <R> user entity
   */
  public static <R> List<R> listUsersViaMap(Function<Long, R> userFunc, String userStr) {
    return Arrays.stream(userStr.split(",")).filter(e -> !e.isEmpty()).map(e -> Long.valueOf(e))
        .map(userFunc).collect(Collectors.toList());
  }

  /**
   *  check if any param is null
   * @param ids
   */
  public static void nonZero(long... ids) {
    for (long id : ids) {
      if (id == 0) {
        throw new IllegalArgumentException();
      }
    }
  }

  /**
   *  Reflection generation object
   * @param type  java bean
   * @param <T>   object type
   * @return      new objects of reflected
   */
  public static <T> T newInstance(Class<T> type) {
    try {
      return type.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static String nullToEmpty(String str) {
    return str == null ? "" : str;
  }
}
