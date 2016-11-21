package edu.suda.ada.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author leon.
 */
public interface Resource {
    File getFile() throws IOException;
    boolean exists();
    boolean isFile();
    boolean canRead();
    InputStream getInputStream();
}
