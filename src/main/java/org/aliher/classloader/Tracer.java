package org.aliher.classloader;

import org.aliher.classloader.helper.CustomClassLoaderStream;
import org.aliher.classloader.helper.TracingClassloader;
import org.aliher.classloader.objects.NestedImpl;
import org.aliher.classloader.objects.TopLevel;

import java.io.*;
import java.net.URLClassLoader;

public class Tracer {

    public static void main(String[] args) throws Exception {
//        case1();
//        case2();
        case3();
//        case4();
//        case5();
    }

    public static void case1() throws Exception {
        System.out.println("Case1 - Create class by name");
        System.out.println("Load class");
        createClass("org.aliher.classloader.objects.TopLevel");
        System.out.println();
    }

    public static void case2() throws Exception {
        System.out.println("Case2 - Create object by name");
        System.out.println("Load class");
        Class clazz = createClass("org.aliher.classloader.objects.TopLevel");
        System.out.println("Create new instance");
        clazz.newInstance();
        System.out.println();
    }

    public static void case3() throws Exception {
        System.out.println("Case3 - Deserialize object");
        System.out.println("Load object from byte array");
        loadObject(new TopLevel());
        System.out.println();
    }

    public static void case4() throws Exception {
        System.out.println("Case4 - Deserialize object with nested object");
        System.out.println("Load object from byte array");
        loadObject(new TopLevel(new NestedImpl()));
        System.out.println();
    }

    public static void case5() throws Exception {
        System.out.println("Case5 - Call method on deserialized");
        System.out.println("Load object from byte array");
        Object obj = loadObject(new TopLevel());
        System.out.println("Call method on object");
        callGetterByName(obj, "getNested");
        System.out.println();
    }

    private static Class createClass(String className) throws Exception {
        ClassLoader classLoader = createClassloader();
        return classLoader.loadClass(className);
    }

    private static Object loadObject(Object instance) throws Exception {
        ClassLoader classLoader = createClassloader();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput output = new ObjectOutputStream(baos);
        output.writeObject(instance);
        output.close();
        ObjectInput input = new CustomClassLoaderStream(new ByteArrayInputStream(baos.toByteArray()), classLoader);
        Object result = input.readObject();
        input.close();
        return result;
    }

    private static ClassLoader createClassloader() {
        // we work with standalone app, so local classloader is usually URLClassLoader
        // would not work under maven because of ClassWorlds
        URLClassLoader systemClassloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        ClassLoader tracingClassloader = new TracingClassloader(systemClassloader,
                "org\\.aliher\\.classloader\\..+");
        return new URLClassLoader(systemClassloader.getURLs(), tracingClassloader);
    }

    private static Object callGetterByName(Object object, String getter) throws Exception {
        return object.getClass().getMethod(getter, new Class[0]).invoke(object);
    }
}
