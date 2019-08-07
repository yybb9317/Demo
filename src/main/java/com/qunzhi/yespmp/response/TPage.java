package com.qunzhi.yespmp.response;

import com.github.pagehelper.Page;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class TPage<T> implements Serializable {

  private static final long serialVersionUID = -7415482017007087308L;

  private long total;
  private List<T> list;

  public TPage(List<T> list, long total) {
    this.list = list;
    this.total = total;
  }

  public static <S> TPage<S> empty() {
    return new TPage<>(Collections.emptyList(), 0);
  }

  public static <S> TPage<S> of(List<S> list, long total) {
    return new TPage<>(list, total);
  }

  public static <S> TPage<S> of(List<S> list) {
    return new TPage<>(list, list.size());
  }

  public static <S> TPage<S> of(Page<S> page) {
    return new TPage<>(page.getResult(), page.getTotal());
  }

}
