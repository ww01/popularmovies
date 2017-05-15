package com.test.movies.db.contract;

import android.net.Uri;

/**
 * Created by waldek on 11.05.17.
 */

public class ContractUriBuilder {

    protected final String authority;

    public ContractUriBuilder(String authority ){
        this.authority = "content://"+authority;
    }

    public Uri uriInsert(PopularMoviesContract.ContractName contractName, long id) {
        return Uri.parse(this.authority).buildUpon().appendPath(contractName.getName()).appendPath("add").appendPath(String.valueOf(id)).build();
    }

    public Uri uriFetchAll(PopularMoviesContract.ContractName contractName){
        return Uri.parse(this.authority).buildUpon().appendPath(contractName.getName()).appendPath("all").build();
    }
}
