package pl.fullstack.movies.db.session;

import android.content.Context;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import pl.fullstack.movies.db.entity.DaoMaster;
import pl.fullstack.movies.db.entity.DaoSession;

/**
 * Created by waldek on 09.07.17.
 */

public class DbSession {

    private static DaoSession daoSession;

    public static final String DB_NAME = "popular_movies";

    public static final DaoSession getInstance(Context context){
        if(DbSession.daoSession == null){
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME);
            Database db = helper.getWritableDb();
            DbSession.daoSession = new DaoMaster(db).newSession();
        }

        return DbSession.daoSession;
    }

}
