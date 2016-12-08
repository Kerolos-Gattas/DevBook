package devBook;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import encryption.*;
import restCommunication.ProjectCommunication;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class StartApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
    	final Set<Class<?>> classes = new HashSet<Class<?>>();
        // register root resource
    	classes.add(EncryptionResource.class);
        classes.add(Authentication.class);   
        classes.add(ProjectCommunication.class);
        return classes;
    }
}

