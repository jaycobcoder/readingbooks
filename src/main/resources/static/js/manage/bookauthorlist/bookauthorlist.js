$(function () {
    $('.register').on("click", function (){
        const bookId = $('#bookId').val();
        const authorId = $('#authorId').val();
        const ordinal = $('#ordinal').val();

        if(bookId.trim() == ''){
            alert('도서 아이디를 입력하세요.');
            return false;
        }

        if(authorId.trim() == ''){
            alert('작가 아이디를 입력하세요.');
            return false;
        }

        if(ordinal.trim() == ''){
            alert('서수를 입력하세요.');
            return false;
        }

        const data = {
            "bookId" : bookId,
            "authorId" : authorId,
            "ordinal" : ordinal
        }

        callAjax('post', '/manage/book-author-list', data);
    });

    $('.delete').on("click", function (){
        const bookId = $('#bookId').val();
        const authorId = $('#authorId').val();

        if(bookId.trim() == ''){
            alert('도서 아이디를 입력하세요.');
            return false;
        }

        if(authorId.trim() == ''){
            alert('작가 아이디를 입력하세요.');
            return false;
        }

        const data = {
            "bookId" : bookId,
            "authorId" : authorId
        }

        callAjax('delete', '/manage/book-author-list', data);
    });
});