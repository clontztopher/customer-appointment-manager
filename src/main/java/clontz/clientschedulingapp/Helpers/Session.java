package clontz.clientschedulingapp.Helpers;

import clontz.clientschedulingapp.Models.User;

public abstract class Session {
    private static User current_user;
    public static User getUser() {
        return current_user;
    }

    public static void setUser(User user) {
        Session.current_user = user;
    }
}
