package com.ic.invoicecapture.builders;

public interface IBuilder<ReturnT, ArgumentT> {
  ReturnT build(ArgumentT arg);
}
