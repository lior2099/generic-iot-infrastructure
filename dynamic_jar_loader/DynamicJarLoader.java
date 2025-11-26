/*------------------------------------------------------------------------
Name: DynamicJarLoader.java
Version : 1.00
Author: Lior shalom
Reviewer: Yarin
Date: 2/09/2024
------------------------------------------------------------------------*/

package dynamic_jar_loader;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DynamicJarLoader {
    private String interfaceName;
    private File jarFile;
    private String packageName;

    public DynamicJarLoader(String interfaceName, String jarFilePath) {

        setInterfaceName(interfaceName);
        setJarFilePath(jarFilePath);
        cutStartName(interfaceName);
    }

    public List<Class<?>> load() {
        Set<String> classNames;

        try {
            classNames = getClassNamesFromJarFile(jarFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return loadClass(classNames);

    }

    private void setInterfaceName(String interfaceName) {
        Objects.requireNonNull(interfaceName);

        try {
            if (Class.forName(interfaceName).isInterface()) {
                this.interfaceName = interfaceName;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setJarFilePath(String jarFilePath) {
        Objects.requireNonNull(jarFilePath);

        if (!jarFilePath.endsWith(".jar")) {
            throw new DynamicException("must be a jar file");
        }

        this.jarFile = new File(jarFilePath);

        if (!jarFile.exists()) {
            throw new FileSystemNotFoundException("cant find a file");
        }
    }

    private void cutStartName(String name) {
        packageName = name.substring(0, name.lastIndexOf('.') + 1);
    }

    public static Set<String> getClassNamesFromJarFile(File givenFile) throws IOException {
        Set<String> classNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(givenFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                            .replace("/", ".")
                            .replace(".class", "");

                    classNames.add(className.substring(className.lastIndexOf('.') + 1));
                }
            }
            return classNames;
        }
    }

    private List<Class<?>> loadClass(Set<String> classNames) {
        Class<?> interClass = null;

        try {
            interClass = Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<Class<?>> classes = new ArrayList<>(classNames.size());
        try (URLClassLoader cl = URLClassLoader.newInstance(
                new URL[]{new URL("jar:file:" + jarFile + "!/")})) {  // make Url for jar
            for (String name : classNames) {
                Class<?> clazz = cl.loadClass(packageName + name); // Load the class by its name
                if (interClass.isAssignableFrom(clazz) && !clazz.isInterface()) {
                    classes.add(clazz);
                }
            }
        } catch (ClassNotFoundException ignored) {  ;  }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }

    private static final class DynamicException extends RuntimeException {
        private DynamicException(String message) {
            this(message, null);
        }

        private DynamicException(Exception e) {
            this(null, e);
        }

        private DynamicException(String message, Exception e) {
            super(message, e);
        }
    }

    public void printAll() {
        System.out.println("interfaceName = " + interfaceName + "\njarFilePath was ok " + jarFile.exists());
        System.out.println("start name  = " + packageName);
    }

}
