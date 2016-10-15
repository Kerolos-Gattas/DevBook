package devBook;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class StartApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
    	final Set<Class<?>> classes = new HashSet<Class<?>>();
        // register root resource
    	//classes.add(EncryptionResource.class);
        return classes;
    }
}

