package ua.koren.customAnnotations;

import java.util.Properties;

public class Demo {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Properties prop = AnnotationHandler.download();
        System.out.println(prop.getProperty("user.name"));
        User user = new User();
        AnnotationHandler.initializeObject(user, prop);
        System.out.println(user.toString());
    }
}
