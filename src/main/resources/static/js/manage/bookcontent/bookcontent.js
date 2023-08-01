$(function () {
    $('.register').on("click", function (){
        const bookId = $('#bookId').val();
        const content = $('#content').val();

        if(bookId.trim() == ''){
            alert('도서 아이디를 입력해주세요');
            return false;
        }
        if(content.trim() == ''){
            alert('도서 내용을 입력해주세요');
            return false;
        }

        const data = {
            "bookId" : bookId,
            "content" : content
        }

        callAjax('post', '/manage/book-content', data);
    });

    $('.update-search').on("click", function (){
        const bookId = $('#bookId').val();
        if(bookId.trim() == ''){
            alert('도서 아이디를 입력해주세요');
            return false;
        }

        $('.bookcontent-form').submit();
    });

    $('.update').on("click", function (){
        const bookId = $('#bookId').val();
        const content = $('#content').val();

        if(bookId.trim() == ''){
            alert('도서 아이디를 입력해주세요');
            return false;
        }
        if(content.trim() == ''){
            alert('도서 내용을 입력해주세요');
            return false;
        }

        const data = {
            "content" : content
        }

        callAjax('patch', '/manage/book-content/'+bookId, data);
    });

    $('.delete').on("click", function (){
        const bookId = $('#bookId').val();

        if(bookId.trim() == ''){
            alert('도서 아이디를 입력해주세요.');
            return false;
        }

        const result = confirm('삭제하면 복구할 수 없습니다. 삭제하시겠습니까?');
        if(result == false){
            return false;
        }

        callAjax('delete', '/manage/book-content/'+bookId);
    });
});