package com.test.movies.db.entity;

import android.content.ContentValues;

/**
 * Created by waldek on 10.05.17.
 */

public  interface IContentValuesCreator<T> {
    public ContentValues toContentValues(T entity);

    public T fromContentValues(ContentValues contentValues);
}
