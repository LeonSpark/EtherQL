package edu.suda.ada.io;

import java.io.IOException;

/**
 * @author leon.
 */
public interface ResourceLoader {
    Resource getResource(String path) throws IOException;
    ClassLoader getClassLoader();
}
