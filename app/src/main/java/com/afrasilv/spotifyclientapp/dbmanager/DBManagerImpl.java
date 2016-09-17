package com.afrasilv.spotifyclientapp.dbmanager;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.afrasilv.spotifyclientapp.model.DBToken;
import com.afrasilv.spotifyclientapp.model.DBTokenDao;
import com.afrasilv.spotifyclientapp.model.DaoMaster;
import com.afrasilv.spotifyclientapp.model.DaoSession;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.database.Database;

/**
 * Created by Alejandro Franco on 17/09/16.
 */
public class DBManagerImpl implements DBManager, AsyncOperationListener {

    /**
     * Instance of DatabaseManager
     */
    private static DBManagerImpl instance;
    /**
     * The Android Activity reference for access to DatabaseManager.
     */
    private Context context;
    /**
     * Helper to access to encryptedDB
     */
    private DaoMaster.EncryptedDevOpenHelper mHelper;
    private Database database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AsyncSession asyncSession;
    private List<AsyncOperation> completedOperations;
    /**
     * Constructs a new DBManagerImpl    with the specified arguments.
     *
     * @param context The Android {@link android.content.Context}.
     */
    public DBManagerImpl(final Context context) {
        this.context = context;
        mHelper = new DaoMaster.EncryptedDevOpenHelper(this.context, "spotifyClientDB");
        completedOperations = new CopyOnWriteArrayList<>();
    }

    public static DBManagerImpl getInstance(Context context) {

        if (instance == null) {
            instance = new DBManagerImpl(context);
        }

        return instance;
    }

    @Override
    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null) {
            database.close();
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    public void dropDb() {
        try {
            openWritableDb();
            DaoMaster.dropAllTables(database, true); // drops all tables
            mHelper.onCreate(database);              // creates the tables
            asyncSession.deleteAll(DBToken.class);    // clear all elements from a table
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean insertToken(DBToken token) {
        try{
            if(token != null){
                openWritableDb();
                DBTokenDao tokenDao = daoSession.getDBTokenDao();
                tokenDao.insert(token);
                daoSession.clear();
                database.close();
                return true;
            }
            return false;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateToken(DBToken token) {
        try {
            if (token != null) {
                openWritableDb();
                daoSession.update(token);
                daoSession.clear();
                database.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<DBToken> listTokens() {
        List<DBToken> tokens = null;
        try{
            openReadableDb();
            DBTokenDao tokenDao = daoSession.getDBTokenDao();
            tokens = tokenDao.loadAll();

            daoSession.clear();
            database.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        if(tokens != null)
            return tokens;
        return null;
    }

    @Override
    public DBToken getToken(Long tokenid) {
        DBToken token = null;
        try {
            openReadableDb();
            DBTokenDao tokenDao = daoSession.getDBTokenDao();
            token = tokenDao.load(tokenid);
            daoSession.clear();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    @Override
    public boolean deleteToken() {
        try {
            openWritableDb();
            DBTokenDao tokenDao = daoSession.getDBTokenDao();
            tokenDao.deleteAll();
            daoSession.clear();
            database.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        completedOperations.add(operation);
    }

    /**
     * Query for readable DB
     */
    public void openReadableDb() throws SQLiteException {
        database = mHelper.getReadableDatabase("contraseñahipersecreta");
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    /**
     * Query for writable DB
     */
    public void openWritableDb() throws SQLiteException {
        database = mHelper.getWritableDatabase("contraseñahipersecreta");
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }
}
