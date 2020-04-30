package cs3524.mud;

import java.net.URI;
import java.net.URISyntaxException;

public class PolicyReader {

    public static void registerPolicy(String fileName) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        // below is a workaround for using getReource to handle filepaths with
        // non URL encodable characters e.g. spaces & –,
        URI url;
        try {
            url = classloader.getResource(fileName).toURI();
        } catch (URISyntaxException e) {
            System.out.println("Can't convert path to URI:\n" + e);
            return;
        }
        String policy = url.getPath();
        System.out.println(policy);
        System.setProperty("java.security.policy", policy);
        System.setSecurityManager(new SecurityManager());
    }
}