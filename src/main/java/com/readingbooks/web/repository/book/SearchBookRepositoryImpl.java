package com.readingbooks.web.repository.book;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.readingbooks.web.domain.entity.review.QReview;
import com.readingbooks.web.domain.enums.AuthorOption;
import com.readingbooks.web.repository.searchcond.SearchCondUtils;
import com.readingbooks.web.service.search.BookSearchCondition;
import com.readingbooks.web.service.search.BookSearchResponse;
import com.readingbooks.web.service.search.QBookSearchResponse;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.readingbooks.web.domain.entity.book.QBook.book;
import static com.readingbooks.web.domain.entity.book.QBookAuthorList.bookAuthorList;
import static com.readingbooks.web.domain.entity.review.QReview.*;

public class SearchBookRepositoryImpl implements SearchBookRepository{
    private final JPAQueryFactory queryFactory;

    public SearchBookRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 조회 메소드
     * @param query
     * @param pageable
     * @param condition
     * @return 페이징 DTO
     */
    @Override
    public Page<BookSearchResponse> search(String query, Pageable pageable, BookSearchCondition condition) {
        /* --- select절의 서브쿼리에서 limit을 먹지 않아서 where절에 서브 쿼리를 추가하여 limit을 대체 --- */
        JPQLQuery<Long> limitAuthorQuery = select(bookAuthorList.ordinal.min())
                .from(bookAuthorList)
                .join(bookAuthorList.author)
                .where(bookAuthorList.book.id.eq(book.id).and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR)));

        JPQLQuery<Long> limitTranslatorQuery = select(bookAuthorList.ordinal.min())
                .from(bookAuthorList)
                .join(bookAuthorList.author)
                .where(bookAuthorList.book.id.eq(book.id).and(bookAuthorList.author.authorOption.eq(AuthorOption.TRANSLATOR)));
        
        /* --- 페이지 count total 구하는 쿼리 --- */
        JPAQuery<Long> countQuery = queryFactory
                .select(book.count())
                .from(book)
                .where(SearchCondUtils.contains(book.title, query));
        
        /* --- 출판사로 검색했는지, 제목으로 검색했는지 확인하는 메소드 --- */
        boolean isPublisherSearching = isPublisherSearching(query);
        StringPath path = initPath(isPublisherSearching);
        query = initQuery(isPublisherSearching, query);

        List<BookSearchResponse> content = queryFactory
                .select(new QBookSearchResponse(
                                book.id,
                                book.isbn,
                                book.savedImageName,
                                book.title,
                                book.publisher,
                                book.description,
                                book.ebookPrice,
                                book.discountRate,
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
                                        select(bookAuthorList.author.name)
                                                .from(bookAuthorList)
                                                .join(bookAuthorList.author)
                                                .where(
                                                        bookAuthorList.book.id.eq(book.id)
                                                                .and(bookAuthorList.author.authorOption.eq(AuthorOption.TRANSLATOR))
                                                                .and(bookAuthorList.ordinal.eq(
                                                                                limitTranslatorQuery
                                                                        )
                                                                )
                                                )
                                                .orderBy(bookAuthorList.ordinal.asc())
                                                .limit(1),
                                        "translator"
                                ),
                                book.category.categoryGroup.name,
                            ExpressionUtils.as(
                                    select(review.starRating.sum())
                                            .from(review)
                                            .join(review.book)
                                            .where(book.id.eq(review.book.id)),
                                    "totalStarRating"
                            ),
                            book.reviewCount
                        )
                )
                .from(book)
                .where(
                        SearchCondUtils.contains(path, query)
                )
                .orderBy(SearchCondUtils.bookOrder(book, condition.getOrder()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private String initQuery(boolean isPublisherSearching, String query) {
        if(isPublisherSearching == false){
            return query;
        }

        query = query.substring(4);
        return query;
    }

    private StringPath initPath(boolean isPublisherSearching) {
        if(isPublisherSearching == true){
            return book.publisher;
        }
        return book.title;
    }

    private boolean isPublisherSearching(String query) {
        if(query == null){
            return false;
        }
        return query.contains("출판사_");
    }
}



