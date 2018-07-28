package com.invisiblecollector.connection.builders;

public interface IBuilder<ReturnT, ArgumentT> {
  ReturnT build(ArgumentT arg);
}
