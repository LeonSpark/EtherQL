package edu.suda.ada.io;

import edu.suda.ada.exception.ResourceNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author leon.
 */
@Component
@Scope("singleton")
public class ClasspathResourceLoader implements ResourceLoader {

    private Class<?> clazz;

    public ClasspathResourceLoader(Class<?> clazz){
        this.clazz = clazz;
    }

    @Override
    public Resource getResource(String path) throws IOException{
        assert (path != null);
        String location = FilenameUtils.normalize(path);
        if (location == null) throw new ResourceNotFoundException("Resource not found");
        return new ClasspathResource(location);
    }

    @Override
    public ClassLoader getClassLoader() {
        return clazz.getClassLoader();
    }
}
