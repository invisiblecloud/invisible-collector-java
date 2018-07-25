package com.ic.invisiblecollector.connection.builders;

import com.ic.invisiblecollector.exceptions.IcException;

public interface IThrowingBuilder<ReturnT, ArgumentT> {
  ReturnT build(ArgumentT arg) throws IcException;
}
