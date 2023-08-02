package com.readingbooks.web.repository.book;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.readingbooks.web.domain.entity.book.QBook;
import com.readingbooks.web.domain.enums.AuthorOption;
import com.readingbooks.web.service.home.HomeBooksResponse;
import com.readingbooks.web.service.home.QHomeBooksResponse;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.readingbooks.web.domain.entity.book.QBook.book;
import static com.readingbooks.web.domain.entity.book.QBookAuthorList.bookAuthorList;
import static com.readingbooks.web.domain.entity.orders.QOrderBooks.orderBooks;
import static com.readingbooks.web.domain.entity.review.QReview.review;

public class HomeRepositoryImpl implements HomeRepository{

    private final JPAQueryFactory queryFactory;

    public HomeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<HomeBooksResponse> findBestBooks() {
        JPQLQuery<Long> limitAuthorQuery = select(bookAuthorList.ordinal.min())
                .from(bookAuthorList)
                .join(bookAuthorList.author)
                .where(bookAuthorList.book.id.eq(book.id).and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR)));

        JPQLQuery<Long> bestBooksQuery = select(orderBooks.book.id)
                .from(orderBooks)
                .join(orderBooks.book)
                .groupBy(orderBooks.book.id)
                .orderBy(orderBooks.book.reviewCount.count().desc())
                .limit(6);

        return queryFactory
                .select(new QHomeBooksResponse(
                        orderBooks.book.isbn,
                        orderBooks.book.savedImageName,
                        orderBooks.book.title,
                        ExpressionUtils.as(
                                select(bookAuthorList.author.name)
                                        .from(bookAuthorList)
                                        .join(bookAuthorList.author)
                                        .where(
                                                bookAuthorList.book.id.eq(orderBooks.book.id)
                                                        .and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR))
                                                        .and(bookAuthorList.ordinal.eq(
                                                                        limitAuthorQuery
                                                                )
                                                        )
                                        )
                                        .orderBy(bookAuthorList.ordinal.asc()),
                                "author"
                        ),
                        ExpressionUtils.as(
                                select(bookAuthorList.author.name.count())
                                        .from(bookAuthorList)
                                        .join(bookAuthorList.author)
                                        .where(bookAuthorList.book.id.eq(orderBooks.book.id).and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR)))
                                        .groupBy(bookAuthorList.book.id),
                                "authorCount"
                        ),
                        ExpressionUtils.as(
                                select(review.starRating.sum())
                                        .from(review)
                                        .join(review.book)
                                        .where(review.book.id.eq(orderBooks.book.id)),
                                "totalStarRating"
                        ),
                        orderBooks.book.reviewCount
                    )
                )
                .from(orderBooks)
                .join(orderBooks.book)
                .where(orderBooks.book.id.in(bestBooksQuery))
                .limit(7)
                .fetch();
    }

    @Override
    public List<HomeBooksResponse> findNewbooks() {
        JPQLQuery<Long> limitAuthorQuery = select(bookAuthorList.ordinal.min())
                .from(bookAuthorList)
                .join(bookAuthorList.author)
                .where(bookAuthorList.book.id.eq(book.id).and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR)));
        return queryFactory
                .select(new QHomeBooksResponse(
                        book.isbn,
                        book.savedImageName,
                        book.title,
                        ExpressionUtils.as(
                                select(bookAuthorList.author.name)
                                        .from(bookAuthorList)
                                        .join(bookAuthorList.author)
                                        .where(
                                                bookAuthorList.book.id.eq(book.id)
                                                        .and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR))
                                                        .and(bookAuthorList.ordinal.eq(
                                                                        limitAuthorQuery
                                                                )
                                                        )
                                        )
                                        .orderBy(bookAuthorList.ordinal.asc()),
                                "author"
                        ),
                        ExpressionUtils.as(
                                select(bookAuthorList.author.name.count())
                                        .from(bookAuthorList)
                                        .join(bookAuthorList.author)
                                        .where(bookAuthorList.book.id.eq(book.id).and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR)))
                                        .groupBy(bookAuthorList.book.id),
                                "authorCount"
                        ),
                        ExpressionUtils.as(
                                select(review.starRating.sum())
                                        .from(review)
                                        .join(review.book)
                                        .where(review.book.id.eq(book.id)),
                                "totalStarRating"
                        ),
                        book.reviewCount
                    )
                )
                .from(book)
                .orderBy(book.createdTime.desc())
                .limit(6)
                .fetch();
    }
}
