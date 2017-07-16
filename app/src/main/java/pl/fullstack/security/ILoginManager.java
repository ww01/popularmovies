package pl.fullstack.security;

import android.content.Context;

/**
 * Created by waldek on 16.07.17.
 */

public interface ILoginManager {
    public String login(Context context, String name, String pass);
}
