package com.ccdt.ottclient.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;

/**
 * Created by iSun on 2015/10/29.
 */
public class StreamUtil {

    private StreamUtil() {

    }

    public static void close(Object stream) {
        if (stream != null) {

            try {
                if (stream instanceof InputStream) {
                    ((InputStream) stream).close();
                } else if (stream instanceof OutputStream) {
                    ((OutputStream) stream).close();
                } else if (stream instanceof Reader) {
                    ((Reader) stream).close();
                } else if (stream instanceof Writer) {
                    ((Writer) stream).close();
                } else if (stream instanceof HttpURLConnection) {
                    ((HttpURLConnection) stream).disconnect();
                }
            } catch (Exception ex) {

            }

        }
    }

    /**
     * 将输入流中的数据，读出来存储在自己数组中
     *
     * @param in
     * @return
     */
    public static byte[] readStream(InputStream in) throws IOException {
        byte[] ret = null;
        if (in != null) {
            byte[] buf = new byte[128];
            int len;

            ByteArrayOutputStream bout = null;

            bout = new ByteArrayOutputStream();

            while (true) {
                len = in.read(buf);
                if (len == -1) {
                    break;
                }
                bout.write(buf, 0, len);
            }

            buf = null;
            ret = bout.toByteArray();
            bout.close();
        }
        return ret;
    }
}
