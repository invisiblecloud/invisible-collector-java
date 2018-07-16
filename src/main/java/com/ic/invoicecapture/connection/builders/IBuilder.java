package com.ic.invoicecapture.connection.builders;

public interface IBuilder<ReturnT, ArgumentT> {
  ReturnT build(ArgumentT arg);
}
