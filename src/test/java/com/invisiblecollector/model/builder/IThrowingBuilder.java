package com.invisiblecollector.model.builder;

import com.invisiblecollector.exceptions.IcException;

public interface IThrowingBuilder<ReturnT, ArgumentT> {
  ReturnT build(ArgumentT arg) throws IcException;
}
