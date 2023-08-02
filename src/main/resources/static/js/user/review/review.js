$(function () {
    $('.btn-form').on("click", function () {
        const textareaWrap = $(this).closest('.review_box').find('.textarea-wrap');
        const content = $(this).closest('.review_box').data('content');
        const textarea = $('<textarea class="form-control review-textarea">' + content + '</textarea>');
        const editButton = $('<button>', {
            class: 'btn btn-primary btn-edit',
            text: '수정'
        });
        const modifyBtn = $(this);

        if (!modifyBtn.data('editing')) {
            // 수정 버튼을 누를 때
            textareaWrap.empty().append(textarea);
            $(this).closest('.modify-box').append(editButton);
            modifyBtn.text('취소').data('editing', true);
        } else {
            // 취소 버튼을 누를 때
            textareaWrap.text(content);
            modifyBtn.text('수정').data('editing', false);
            $(this).closest('.modify-box').find('.btn-edit').remove();
        }
    });

    $(document).on("click", ".btn-edit", function (){
        const reviewId = $(this).closest('.review_box').data('review-id');
        const content = $(this).closest('.review_box').find('.review-textarea').val();
        const starRating = $(this).closest('.review_box').data('star-rating');

        const data = {
            content : content,
            starRating : starRating
        }

        callAjaxNoAlert("patch", "/review/"+reviewId, data);
    });

    $('.btn-delete').on("click", function (){
        const reviewId = $(this).closest('.review_box').data('review-id');
        const result = confirm('삭제하면 복구할 수 없습니다. 삭제하시겠습니까?');

        if(result == false){
            return false;
        }

        callAjaxNoAlert("delete", "/review/"+reviewId);
    });

})