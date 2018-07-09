package com.ic.invoicecapture.connection.response.validators;

import com.ic.invoicecapture.exceptions.IcIoException;
import org.javatuples.Pair;

public interface IResponseValidator {
  /**
   * Checks for response validity.
   * 
   * @return a pair where the boolean as true means the response is valid and as false means it's
   *         not valid and the exception contains the reason
   */
  Pair<Boolean, ? extends IcIoException> validate();
}
