package ua.koren.customAnnotations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnnotationHandlerTest {

    private Properties properties;

    @BeforeEach
    void setUp() {
        properties = AnnotationHandler.download();
    }

    @Test
    void downloadTest() {
        //given
        String name = "John";
        String num = "25";
        //then
        assertEquals(properties.getProperty("user.name"), name);
        assertEquals(properties.getProperty("user.number"), num);
    }

    @Test
    void initializeObjectTest() throws NoSuchFieldException, IllegalAccessException {
        //given
        User user = new User();
        Object object = user;
        //when
        AnnotationHandler.initializeObject(object, properties);

        //then
        assertNotNull(object);
        assertEquals(user.getName(), properties.getProperty("user.name"));
        assertEquals(user.getNum(), Integer.parseInt(properties.getProperty("user.number")));
    }

    @Test
    void initializeObjectNullTest() throws NoSuchFieldException, IllegalAccessException {
        //given
        Object object = null;
        //when, then
        Assertions.assertThrows(RuntimeException.class, () -> AnnotationHandler.initializeObject(object, properties));
    }
}