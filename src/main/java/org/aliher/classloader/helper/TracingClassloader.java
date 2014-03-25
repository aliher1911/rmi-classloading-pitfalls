package org.aliher.classloader.helper;

import java.util.regex.Pattern;

public class TracingClassloader extends ClassLoader {
    private Pattern customClasses;

    public TracingClassloader(ClassLoader parent, String customClasses) {
        super(parent);
        this.customClasses = Pattern.compile(customClasses);
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (customClasses.matcher(name).matches()) {
            System.out.println("  Load " + name);
            throw new ClassNotFoundException("Pls invoke other classloader.");
        } else {
            return super.loadClass(name, resolve);
        }
    }
}
