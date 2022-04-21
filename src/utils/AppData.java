package utils;

public class AppData {
    private static int id;
    private static String password = null;    

    public static void setId(int id) {
        AppData.id = id;
    }

    public static int getId() {
        return id;
    }

    public static void setPassword(String password) {
        AppData.password = password;
    }

    public static String getPassword() {
        return password;
    }
}
