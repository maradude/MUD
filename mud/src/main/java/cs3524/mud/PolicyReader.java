/*
* Martti Aukia 51657228
* used by both client and server to register a security policy
* for the RMI exchanges.
* the file is assumed to be located in the parent directory of cs3524's
* class files
* example tree view:
├── mud.policy
├── cs3524
│   └── mud
│       ├── PolicyReader.class
│       ├── client
│       └── server
│           └── game
*/
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