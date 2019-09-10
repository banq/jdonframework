/*
 * $Id: URLUtil.java,v 1.2 2005/01/31 05:27:54 jdon Exp $
 *
 *  Copyright (c) 2001, 2002 The Open For Business Project - www.ofbiz.org
 *
 *  Permission is hereby granted, free of charge, to any person obtaining event
 *  copy of this software and associated documentation files (the "Software"),
 *  to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included
 *  in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 *  OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 *  OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 *  THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.jdon.util;

import java.net.*;
import java.io.*;

public class URLUtil {

  private URLConnection connection = null;
  private URL url = null;
  private boolean timedOut = false;

  protected URLUtil() {}

  protected URLUtil(URL url) {
    this.url = url;
  }

  protected synchronized URLConnection openConnection(int timeout) throws
      IOException {
    Thread t = new Thread(new URLConnectorThread());
    t.start();

    try {
      this.wait(timeout);
    } catch (InterruptedException e) {
      if (connection == null)
        timedOut = true;
      else
        close(connection);
      throw new IOException("Connection never established");
    }

    if (connection != null) {
      return connection;
    } else {
      timedOut = true;
      throw new IOException("Connection timed out");
    }
  }

  public static URLConnection openConnection(URL url) throws IOException {
    return openConnection(url, 30000);
  }

  public static URLConnection openConnection(URL url, int timeout) throws
      IOException {
    URLUtil uc = new URLUtil(url);
    return uc.openConnection(timeout);
  }

  // special thread to open the connection
  private class URLConnectorThread implements Runnable {
    public void run() {
      URLConnection con = null;
      try {
        con = url.openConnection();
      } catch (IOException e) {}

      synchronized (URLUtil.this) {
        if (timedOut && con != null)
          close(con);
        else {
          connection = con;
          URLUtil.this.notify();
        }
      }
    }
  }

  // closes the HttpURLConnection does nothing to others
  private static void close(URLConnection con) {
    if (con instanceof HttpURLConnection) {
      ( (HttpURLConnection) con).disconnect();
    }
  }


}
