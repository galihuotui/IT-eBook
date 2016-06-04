package com.cky.rx.util;

import com.cky.greendao.Book;
import com.cky.greendao.BookDao;
import com.cky.rx.App;
import com.cky.rx.data.Constants;

import java.util.List;

/**
 * Created by cuikangyuan on 16/6/4.
 *
 * 数据库操作封装类
 */
public class DaoUtil {

    public static void insertOneBook(Book book) {
        App.daoManager.insertBook(book);
    }

    /**
     * 查询所有记录
     * */
    public static List<Book> getBooks() {
        List<Book> queryResult = null;
        queryResult = App.daoManager.queryAllBook();
        return queryResult;
    }

    /**
     * 下载成功后 更新 数据库记录 状态
     * */
    public static boolean checkBookExistAndUpdate(String requestId) {
        List<Book> queryResult = App.daoManager.queryBook(BookDao.Properties.Request_id.eq(requestId));
        if (queryResult.size() > 0) {
            Book book = new Book(queryResult.get(0).getId(),
                    queryResult.get(0).getBook_name(),
                    queryResult.get(0).getBook_isbn(),
                    queryResult.get(0).getBook_id(),
                    queryResult.get(0).getRequest_id(),
                    Constants.STATUS_DOWNLOAD_SUCCESS
                    );
            App.daoManager.updateBook(book);
            return true;
        }

        return false;
    }
    public static boolean checkBookExistByIsbn(String bookIsbn) {
        List<Book> queryResult = App.daoManager.queryBook(BookDao.Properties.Book_isbn.eq(bookIsbn));
        if (queryResult.size() > 0) {

            return true;
        }

        return false;
    }
}
