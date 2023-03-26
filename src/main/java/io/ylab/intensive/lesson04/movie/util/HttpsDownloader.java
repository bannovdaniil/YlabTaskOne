package io.ylab.intensive.lesson04.movie.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpsDownloader {
  private final Logger LOGGER = LoggerFactory.getLogger(HttpsDownloader.class);

  public void downloadFile(String url, File file, String charset) throws IOException {
    LOGGER.info("Start download: {}", url);
    HttpsURLConnection connection = getConnection(url);
    int responseCode = connection.getResponseCode();
    if (responseCode != 200) {
      throw new IOException("Error code: " + responseCode);
    }
    try (PrintWriter pw = new PrintWriter(file, Charset.forName(charset));
         BufferedReader in = new BufferedReader(
             new InputStreamReader(connection.getInputStream(), Charset.forName(charset))
         )
    ) {
      char[] buffer = new char[4096];
      int count = 1;
      while (count > 0) {
        count = in.read(buffer);
        if (count > 0) {
          pw.write(buffer, 0, count);
        }
      }

      pw.flush();
      LOGGER.info("Download - OK");
    }
  }

  private HttpsURLConnection getConnection(String url) throws IOException {
    URL obj = new URL(url);
    HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();

    connection.setRequestMethod("GET");
    connection.setRequestProperty("User-Agent", "Mozilla/5.0");
    connection.setUseCaches(false);
    connection.setConnectTimeout(55000);

    return connection;
  }

}
