package com.rongji.websiteMonitor.service.impl.message.vcm;
import java.security.*;

/**
 * MD5进行概要  进行MD5加密
 */
public final class MD5 {
  private static char[] digit = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
        'E', 'F'};

  public static String byteToHex(byte ib) {
    char[] ob = new char[2];
    ob[0] = digit[ (ib >>> 4) & 0X0F];
    ob[1] = digit[ib & 0X0F];
    String s = new String(ob);
    return s;
  }

  public static String md5(String src) {
    return md5(src.getBytes());
  }

  public static String md5(byte[] bb) {
    try {
      MessageDigest  md = MessageDigest.getInstance("MD5");
      md.update(bb);
      byte[] b = md.digest();
      String str = "";
      for (int i = 0; i < b.length; i++) {
        str += byteToHex(b[i]);
      }
      return str;
    }catch(NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

}
