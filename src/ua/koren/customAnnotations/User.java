package ua.koren.customAnnotations;

@SetObjectFields(name = "Just User",
        instantiated = false)
public class User {

    @UnInitialized
    private String name;

    @UnInitialized
    private int num;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", num=" + num +
                '}';
    }
}
