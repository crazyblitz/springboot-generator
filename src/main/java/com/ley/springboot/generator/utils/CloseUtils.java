package com.ley.springboot.generator.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * closeable utility class
 *
 * @author liuenyuan
 * @see Closeable
 * @see AutoCloseable
 **/
public class CloseUtils {

    /**
     * close resource when the resource implements {@link Closeable}
     **/
    public static void closeable(Closeable... closeables) throws IOException {
        if (closeables != null && closeables.length > 0) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        }
    }

    /**
     * close resource when the resource implements {@link AutoCloseable}
     *
     * @see AutoCloseable
     * @see java.sql.ResultSet
     * @see java.sql.PreparedStatement
     * @see java.sql.Connection
     **/
    public static void closeable(AutoCloseable... autoCloseables) throws Exception {
        if (autoCloseables != null && autoCloseables.length > 0) {
            for (AutoCloseable autoCloseable : autoCloseables) {
                if (autoCloseable != null) {
                    autoCloseable.close();
                }
            }
        }
    }
}
