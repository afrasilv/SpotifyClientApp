package com.afrasilv.spotifyclientapp.dbmanager;

import com.afrasilv.spotifyclientapp.model.DBToken;

import java.util.List;

/**
 * Created by Alejandro Franco on 17/09/16.
 */
public interface DBManager {

    void closeDbConnections();
    void dropDb();

    boolean insertToken(DBToken token);

    boolean updateToken(DBToken token);

    List<DBToken> listTokens();

    DBToken getToken(Long tokenid);

    boolean deleteToken();
}
