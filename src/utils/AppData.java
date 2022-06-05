package utils;

public class AppData {
    private static int id;
    private static String token = null;    

    public static void setId(int id) {
        AppData.id = id;
    }

    public static int getId() {
        return id;
    }


    public static void setToken(String token) {
        AppData.token = token;
    }

    public static String getToken() {
        return token;
    }
}
