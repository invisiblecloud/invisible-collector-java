package com.ic.invoicecapture;

public interface IBuilder<ReturnT, ArgumentT> {
  ReturnT build(ArgumentT arg);
}
