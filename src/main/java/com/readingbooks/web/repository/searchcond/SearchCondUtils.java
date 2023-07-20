package com.readingbooks.web.repository.searchcond;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.readingbooks.web.domain.entity.book.QBook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchCondUtils {
    public static <T> BooleanExpression eq(SimpleExpression<T> target, T value){
        return value == null ? null : target.eq(value);
    }

    public static <T> BooleanExpression startWith(StringExpression target, String value){
        return value == null ? null : target.startsWith(value);
    }

    public static <T> BooleanExpression contains(StringExpression target, String value){
        return value == null ? null : target.contains(value);
    }

    public static OrderSpecifier[] bookOrder(QBook book, String order) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        if(StringUtils.hasText(order) && order.equals("latest")){
            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, book.createdTime));
        }else if(StringUtils.hasText(order) && order.equals("reviews")){
            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, book.reviewCount));
        }else if(StringUtils.hasText(order) && order.equals("price")){
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, book.ebookPrice));
        }else{
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, book.createdTime));
        }
        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }

    public static StringPath checkSearchCond(String query, QBook book) {
        if(query.contains("출판사_")){
            return book.publisher;
        }else{
            return book.title;
        }
    }
}
