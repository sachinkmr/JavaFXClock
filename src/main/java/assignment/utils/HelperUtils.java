package assignment.utils;

import java.net.URL;

public class HelperUtils {
    public static URL getResourceLocation(String relativePath) {
        return HelperUtils.class.getClassLoader().getResource(relativePath);
    }
}
