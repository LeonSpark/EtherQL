package edu.suda.ada.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author leon.
 */
public abstract class AbstractResource implements Resource {
    @Override
    public boolean exists() {
        try {
            return getFile().exists();
        } catch (IOException e) {
            try {
                InputStream is = getInputStream();
                is.close();
                return true;
            } catch (IOException e1) {
               return false;
            }
        }
    }

    @Override
    public boolean isFile() {
        try {
            return getFile().isFile();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean canRead() {
        try {
            return getFile().canRead();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public File getFile() throws IOException{
        throw new FileNotFoundException("Resource file not found");
    }
}
