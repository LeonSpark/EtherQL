package edu.suda.ada.io;

import java.io.File;
import java.io.InputStream;

/**
 * @author leon.
 */
public class ClasspathResource extends AbstractResource {
    private String path;
    private ClassLoader classLoader;
    private Class<?> clazz;

    public ClasspathResource(String path, ClassLoader classLoader, Class<?> clazz) {
        this.path = path;
        this.classLoader = classLoader;
        this.clazz = clazz;
    }

    public ClasspathResource(String path, ClassLoader classLoader) {
       this(path, classLoader == null ? ClassLoader.getSystemClassLoader():classLoader, null);
    }

    public ClasspathResource(String path, Class<?> clazz) {
        this(path, clazz.getClassLoader());
    }

    public ClasspathResource(String path) {
        this(path, ClassLoader.getSystemClassLoader());
    }

    @Override
    public File getFile() {
        return new File(path);
    }

    @Override
    public InputStream getInputStream() {
        InputStream is;
        if (clazz != null){
            is = clazz.getClassLoader().getResourceAsStream(path);
        }else if (classLoader != null){
            is = classLoader.getResourceAsStream(path);
        }else {
            is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
        }
        return is;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
