package pl.fullstack.security;

import android.content.Context;

import org.greenrobot.greendao.query.Query;

import java.security.NoSuchAlgorithmException;

import pl.fullstack.movies.db.entity.AppUser;
import pl.fullstack.movies.db.entity.AppUserDao;
import pl.fullstack.movies.db.session.DbSession;

/**
 * Created by waldek on 16.07.17.
 */

public class LoginManager implements ILoginManager {

    protected AppUser currentUser;

    @Override
    public String login(Context context, String name, String pass) {

        Query<AppUser> appUserQuery = DbSession.getInstance(context).queryBuilder(AppUser.class)
                .where(AppUserDao.Properties.Name.eq("?"), AppUserDao.Properties.Password.eq("?"))
                .build();
        appUserQuery.setParameter(0, name);
        appUserQuery.setParameter(1, pass);

        this.currentUser = appUserQuery.unique();

        if(this.currentUser == null)
            return null;

        try {
            return Helper.SHA256(name);
        } catch(NoSuchAlgorithmException e){
            return null;
        }

    }
}
