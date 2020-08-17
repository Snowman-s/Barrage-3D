package barrage3d.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ResourceLoader {
    public static InputStream getInputStream(String s) throws IOException {
        Class<?> thisClass;
        try {
            thisClass = Class.forName("barrage3d.resource.ResourceLoader");
        } catch (ClassNotFoundException classNotFoundException) {
            throw new IOException(classNotFoundException);
        }
        return Objects.requireNonNull(thisClass.getResourceAsStream("/barrage3d/resource/" + s));
    }
}