package com.ic.invoicecapture.connection.builders;

import com.ic.invoicecapture.exceptions.IcException;

public interface IThrowingBuilder<ReturnT, ArgumentT> {
  ReturnT build(ArgumentT arg) throws IcException;
}
