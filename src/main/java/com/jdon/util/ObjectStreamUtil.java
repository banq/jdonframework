package com.jdon.util;

import java.io.*;

public class ObjectStreamUtil {

  /**
   * Converts event serializable object to event byte array.
   */
  public static byte[] objectToBytes(Object object) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream os = new ObjectOutputStream(baos);
    os.writeObject(object);
    return baos.toByteArray();
  }

  /**
   * Converts event byte array to event serializable object.
   */
  public static Object bytesToObject(byte[] bytes) throws IOException,
      ClassNotFoundException {
    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    ObjectInputStream is = new ObjectInputStream(bais);
    return is.readObject();
  }

}