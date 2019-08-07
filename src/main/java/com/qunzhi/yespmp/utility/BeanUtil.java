package com.qunzhi.yespmp.utility;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


/**
 * custom bean util
 */
public class BeanUtil {

  /**
   * common copy bean collection
   *
   * @param source source
   * @param clz target class
   * @param ignore ignore properties
   * @param <S> source type
   * @param <T> target type
   * @return target list
   */
  public static <S, T> List<T> copy(Collection<S> source, Class<T> clz, String... ignore) {
    if (source == null || source.isEmpty()) {
      return Collections.emptyList();
    }
    List<T> list = new ArrayList<>(source.size());
    try {
      for (S s : source) {
        T t = clz.newInstance();
        BeanUtils.copyProperties(s, t, ignore);
        list.add(t);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return list;
  }

  /**
   * copy bean and return
   *
   * @param source copy from
   * @param target copy to
   * @param ignore ignore properties
   * @param <T> target type
   * @return copied target
   */
  public static <T> T copy(Object source, T target, String... ignore) {
    BeanUtils.copyProperties(source, target, ignore);
    return target;
  }

  /**
   * copy only non null properties
   *
   * @param source copy from
   * @param target copy to
   * @param ignore ignore properties
   * @param <T> target type
   * @return copied target
   */
  public static <T> T copyNonNull(Object source, T target, String... ignore) {
    BeanUtils.copyProperties(source, target,
        ArrayUtils.addAll(getNullPropertyNames(source), ignore));
    return target;
  }

  private static String[] getNullPropertyNames(Object source) {
    final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
    return Stream.of(wrappedSource.getPropertyDescriptors())
        .map(FeatureDescriptor::getName)
        .filter(e -> wrappedSource.getPropertyValue(e) == null)
        .toArray(String[]::new);
  }
}
