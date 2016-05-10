/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codeonthefly;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 *
 * @author simon
 */
public class CodeOnTheFly {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        // generate source file
        PrintWriter writer = new PrintWriter("/home/simon/javafiles/src/ToBeCompiled.java", "UTF-8");
        writer.println("public class ToBeCompiled {\n"
                + "    public void callMe() {\n"
                + "        System.out.println(\"Simon the Sorcerer!\");\n"
                + "    }\n"
                + "}");

        writer.close();
        
        // compile the source code file we've just generated
        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager sjfm = jc.getStandardFileManager(null, null, null);

        File javaFile = new File("/home/simon/javafiles/src/ToBeCompiled.java");
        Iterable fileObjects = sjfm.getJavaFileObjects(javaFile);
        String[] options = new String[]{"-d", "/home/simon/javafiles/bin"};

        jc.getTask(null, null, null, Arrays.asList(options), null, fileObjects).call();
        sjfm.close();
        System.out.println("Class has been sucessfully compiled");

        // run the files method via reflection
        URL[] urls = new URL[]{new URL("file:/home/simon/javafiles/bin/")};
        URLClassLoader ucl = new URLClassLoader(urls);
        Class clazz = ucl.loadClass("ToBeCompiled");
        System.out.println("Class has been successfully loaded");

        Method method = clazz.getDeclaredMethod("callMe", null);
        Object object = clazz.newInstance();
        Object invoke = method.invoke(object, null);

    }

}
