package com.cky.rx.data;

import android.content.Context;

import com.cky.greendao.Book;
import com.cky.greendao.BookDao;
import com.cky.greendao.DaoSession;
import com.cky.rx.App;

import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by cuikangyuan on 16/6/4.
 *
 * 数据库操作具体功能实现类
 */

public class DaoManager {
    private static DaoManager mInstance;
    private static Context appContext;
    private static DaoSession mDaoSession;
    private static BookDao mBookDao;

    public DaoManager() {

    }

    public static DaoManager getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new DaoManager();
            if (appContext == null) {
                appContext = context.getApplicationContext();
            }
            mInstance.mDaoSession = App.getDaoSession(appContext);
            mInstance.mBookDao = mInstance.mDaoSession.getBookDao();
        }

        return mInstance;
    }

    /**
     * Book 插入方法
     * */
    public void insertBook(Book book) {
        mBookDao.insert(book);
    }
    /**
     * Book 更新方法
     * */
    public void updateBook(Book book) {
        mBookDao.update(book);
    }

    /***
     * Book 查询所有记录
     */

    public List<Book> queryAllBook() {
        return mBookDao.queryBuilder().orderDesc(BookDao.Properties.Id).list();
    }

    /**
     * Book 条件查询方法
     * */
    public List<Book> queryBook(WhereCondition arg0, WhereCondition... conditions) {
        QueryBuilder<Book> queryBuilder = mBookDao.queryBuilder();
        queryBuilder.where(arg0, conditions);
        List<Book> queryResult = queryBuilder.list();

        return queryResult;
    }

    /**
     * Book 删除所有方法
     * */
    public void deleteAllBook() {
        mBookDao.deleteAll();
    }

    /**
     * Book 根据下载ID删除方法
     * */
    public void deleteBookByRequestId(String requestId) {
        QueryBuilder<Book> queryBuilder = mBookDao.queryBuilder();
        DeleteQuery<Book> deleteQuery = queryBuilder.where(BookDao.Properties.Request_id.eq(requestId)).buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    public void deleteBook(Book book) {
        mBookDao.delete(book);
    }
}
