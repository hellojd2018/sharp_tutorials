package chap05.defaults;

public interface Company {
    default String getName() {
        return "Initech";
    }

    // String getName();
}
