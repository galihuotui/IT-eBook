package com.cky.rx.util;

import com.cky.rx.model.BookItem;
import com.cky.rx.model.BookItemToShow;
import com.cky.rx.model.SearchBookResult;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by cuikangyuan on 16/5/8.
 */
public class SearchBookResultToItemsMapper implements Func1<SearchBookResult, List<BookItemToShow>> {

    private static SearchBookResultToItemsMapper INSTANCE = new SearchBookResultToItemsMapper();

    private SearchBookResultToItemsMapper() {

    }

    public static SearchBookResultToItemsMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public List<BookItemToShow> call(SearchBookResult searchBookResult) {
        if (!searchBookResult.Total.equals("0")) {
            List<BookItem> bookItemsOriginal = searchBookResult.Books;
            String pageIndex = searchBookResult.Page;
            Boolean isExtraPage = (Integer.valueOf(searchBookResult.Total)%10 != 0 ? true : false);
            String totalPage = null;
            if (isExtraPage) {
                totalPage = String.valueOf((Integer.valueOf(searchBookResult.Total) / 10) + 1);
            } else {
                totalPage = String.valueOf((Integer.valueOf(searchBookResult.Total) / 10));
            }

            List<BookItemToShow> items = new ArrayList<>(bookItemsOriginal.size());

            for (BookItem bookItem : bookItemsOriginal) {
                BookItemToShow item = new BookItemToShow();

                item.imageUrl = bookItem.Image;
                item.id = bookItem.ID;
                item.desc = bookItem.Description;
                item.title = bookItem.Title;
                item.index = pageIndex;
                item.totalPage = totalPage;
                items.add(item);
            }

            return items;
        }

        return null;
    }
}
