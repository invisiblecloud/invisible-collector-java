package com.ic.invoicecapture;

import java.io.InputStream;
import java.util.Scanner;

public final class StringUtils {
  private StringUtils() {}
  
  // kindly provided by Pavel Repin @ https://stackoverflow.com/a/5445161
  public static String inputStreamToString(InputStream inputStream) {
    Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
    return scanner.hasNext() ? scanner.next() : "";
  }
}
