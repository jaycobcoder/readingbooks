$(function () {
    $('.register').on("click", function (){
        const categoryGroupId = $('#categoryGroupId').val();
        const name = $('#name').val();

        if(categoryGroupId.trim() == ''){
            alert('카테고리 그룹 아이디를 입력하세요.');
            return false;
        }

        if(name.trim() == ''){
            alert('카테고리를 입력하세요.');
            return false;
        }

        const data = {
            "categoryGroupId" : categoryGroupId,
            "name" : name
        }

        callAjax('post', '/manage/category', data);
    });

    $('.update').on("click", function (){
        const categoryId = $('#categoryId').val();
        const name = $('#name').val();

        const data = {
            "name" : name
        }

        callAjax('patch', '/manage/category/'+categoryId, data);
    });

    $('.search').on("click", function (){
        const name = $('#name').val();

        if(name.trim() == ''){
            alert('카테고리를 입력하세요.');
            return false;
        }

        $('.category-form').submit();
    });

    $('.delete').on("click", function (){
        const categoryId = $('#categoryId').val();

        callAjax('delete', '/manage/category/'+categoryId);
    });
});