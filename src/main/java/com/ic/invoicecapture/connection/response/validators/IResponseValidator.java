package com.ic.invoicecapture.connection.response.validators;

import org.javatuples.Pair;
import com.ic.invoicecapture.exceptions.ICIOxception;

public interface IResponseValidator {
  /**
   * Checks for response validity
   * 
   * @return a pair where the boolean as true means the response is valid and as false means it's
   *         not valid and the exception contains the reason
   */
  Pair<Boolean, ? extends ICIOxception> validate();
}
