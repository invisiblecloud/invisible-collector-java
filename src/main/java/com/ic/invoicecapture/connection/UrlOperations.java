package com.ic.invoicecapture.connection;

public class UrlOperations {
  public static String joinUrls(String baseUrl, String endUrl) {
    String url = baseUrl + "/" + endUrl;
    url = url.replaceAll("//+", "/"); // removes duplicate "/"
    return url;
  }
}
