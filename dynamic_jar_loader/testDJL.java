package il.co.ilrd.dynamic_jar_loader;

import org.junit.jupiter.api.Test;

import java.util.List;

public class testDJL {
    private String interfaceName = "il.co.ilrd.simple_rps.commands.Command";
    private String jarFilePath = "/home/lior/Desktop/git/lior.shalom/java/projects/src/il/co/ilrd/dynamic_jar_loader/djl.jar";

    @Test
    public void loadTest(){
        DynamicJarLoader djl = new DynamicJarLoader(interfaceName , jarFilePath);

        djl.printAll();
        List<Class<?>> loadCalss = djl.load();

        System.out.println(loadCalss);

    }

}
