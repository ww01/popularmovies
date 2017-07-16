package pl.fullstack.security;

import android.content.Context;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import pl.fullstack.movies.db.entity.AppUser;
import pl.fullstack.movies.db.entity.AppUserDao;
import pl.fullstack.movies.db.entity.DaoSession;
import pl.fullstack.movies.db.session.DbSession;

/**
 * Created by waldek on 16.07.17.
 */

public class RegistrationManager implements IRegisterManager {

    @Override
    public boolean register(Context context, String name, String pass) {

        DaoSession db = DbSession.getInstance(context);

        Query<AppUser> userQueryBuilder = db.queryBuilder(AppUser.class).where(AppUserDao.Properties.Name.eq("?")).build();
        userQueryBuilder.setParameter(0, name);

        AppUser inDb = userQueryBuilder.unique();

        if(inDb != null)
            return false;

        inDb = new AppUser();
        inDb.setName(name);
        inDb.setPassword(pass);

        db.getAppUserDao().insert(inDb);

        return true;
    }
}
