
package com.greenriver.commons.jar;

import java.io.InputStream;
import java.net.URI;

/**
 * Interface to be implemented by those classes that are used to provide
 * access to the resources existing in their jar file.
 * @author Miguel Angel
 */
public interface JarResourceProvider {
    /**
     * Gets a resource by uri
     * @param uri Uri of the resource
     * @return
     */
    InputStream getResource(URI uri);
    /**
     * Gets a resource by component name and path
     * @param component Name of the component to get
     * @param path Path of the resource relative to the component
     * @return
     */
    InputStream getResource(String component, String path);
    /**
     * Gets a resource by the path
     * @param path Absolute path to the resource
     * @return
     */
    InputStream getResource(String path);
}
