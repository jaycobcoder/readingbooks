function validateForm(starRating, content){
    if (typeof starRating == "undefined") {
        alert('별점을 남겨주세요.');
        return false;
    }

    if(content.trim() == ''){
        alert('리뷰를 남겨주세요');
        return false;
    }

    if(content.length < 10){
        alert('10자 이상의 리뷰를 남겨주세요.');
        return false;
    }

    if(content.length > 2000){
        alert('2000자 미만의 리뷰를 남겨주세요.');
        return false;
    }
}

$(function (){
    $('.review-btn').on("click", function (){
        const starRating = $('input[name=rating]:checked').val();
        const content = $('.comment').val();

        const result = validateForm(starRating, content);
        if(result == false){
            return false;
        }

        const bookId = $(this).data('book');
        const data = {
            bookId : bookId,
            content : content,
            starRating : starRating
        }
        callAjax("post", "/review", data);
    });

    $(document).on("click", '.update-confirm', function (){
        const starRating = $('input[name=rating]:checked').val();
        const content = $('.comment').val();
        validateForm(starRating, content);

        const reviewId = $(this).data('review');
        const data = {
            content : content,
            starRating : starRating
        }

        callAjax("patch", "/review/"+reviewId, data);
    });

    $(".btn-review-delete").on("click", function (){
        const result = confirm('삭제하면 복구할 수 없습니다. 삭제하시겠습니까?');
        if(result == false){
            return false;
        }
        const reviewId = $(this).data('review');

        callAjax("delete", "/review/"+reviewId);
    });

    $(document).on("click", ".comment-submit", function (){
        const reviewId = $(this).closest('.review_wrap').find('.comment-btn').data('review');
        const content = $(this).closest('.review_wrap').find('textarea.comment-textarea').val();

        if(content.trim() == ''){
            alert('댓글을 남겨주세요');
            return false;
        }

        if(content.length > 2000){
            alert('2000자 미만의 댓글을 남겨주세요.');
            return false;
        }

        const data = {
            reviewId : reviewId,
            content : content
        }

        callAjax("post", "/review-comment", data);
    })

    $(document).on("click", ".delete-review-comment", function (){
        const result = confirm("정말 삭제하시겠습니까?");
        if(result == false){
            return false;
        }

        const reviewCommentId = $(this).data('review-comment');
        console.log(reviewCommentId);
        const data = {
            reviewCommentId : reviewCommentId
        }
        callAjax("delete", "/review-comment", data);
    })

    $('.like-btn').on("click", function (){
        const reviewId = $(this).data('review');
        const data = {
            reviewId : reviewId
        }
        callAjaxNoAlert('post', '/like', data);
    });

    // 댓글 토글
    let isCommentVisible = false;
    const textarea = $('<textarea>', {
        class: 'form-control mt-2 comment-textarea',
        placeholder: '댓글을 입력하세요.'
    });

    const button = $('<button>', {
        class: 'btn btn-primary mt-2 comment-submit',
        text: '댓글 작성'
    });

    $('.comment-btn').on("click", function () {
      if(isCommentVisible) {
        $('.review_wrap .comment-textarea, .review_wrap .comment-submit').remove();
        isCommentVisible = false;
      } else{
        $(this).closest('.review_wrap').append(textarea);
        $(this).closest('.review_wrap').append(button);
        isCommentVisible = true;
      }
    })
})