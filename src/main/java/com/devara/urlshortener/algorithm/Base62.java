package com.devara.urlshortener.algorithm;

public class Base62 {

  private static final String BASE62 =
      "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final int BASE = BASE62.length();

  /**
   * Encodes a number to a base62 string.
   *
   * @param value the number to encode, which is the database ID
   * @return the base62 encoded string
   */
  public static String encode(long value) {
    // If the value is 0, return the first character of the alphabet.
    if (value == 0) {
      return String.valueOf(BASE62.charAt(0));
    }

    StringBuilder sb = new StringBuilder();
    // Keep dividing the value by the base and append the remainder.
    // This is a standard algorithm for base conversion.
    while (value > 0) {
      sb.append(BASE62.charAt((int) (value % BASE)));

      // /= is the same as value = value / BASE
      value /= BASE;
    }

    // The characters were appended in reverse order, so we need to reverse the string.
    return sb.reverse().toString();
  }

  /**
   * Decodes a base62 string to a number.
   *
   * @param str the base62 encoded string
   * @return the decoded number, which is the database ID
   */
  public static long decode(String str) {
    long num = 0;
    for (char c : str.toCharArray()) {
      num = num * BASE + BASE62.indexOf(c);
    }
    return num;
  }
}
