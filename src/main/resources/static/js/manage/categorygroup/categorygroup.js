$(function () {
    $('.register').on("click", function (){
        const name = $('#name').val();

        if(name.trim() == ''){
            alert('카테고리 그룹을 입력하세요.');
            return false;
        }

        const data = {
            "name" : name
        }
        callAjax('post', '/manage/category-group', data);
    });

    $('.update').on("click", function (){
        const categoryGroupId = $('#categoryGroupId').val();
        const name = $('#name').val();

        if(categoryGroupId.trim() == ''){
            alert('카테고리 그룹 아이디를 입력하세요.');
            return false;
        }

        if(name.trim() == ''){
            alert('카테고리 그룹을 입력하세요.');
            return false;
        }

        const data = {
            "name" : name
        }
        callAjax('patch', '/manage/category-group/'+categoryGroupId, data);
    });

    $('.search').on("click", function (){
        const name = $('#name').val();

        if(name.trim() == ''){
            alert('카테고리 그룹을 입력하세요.');
            return false;
        }

        $('.categorygroup-form').submit();
    });

    $('.delete').on("click", function (){
        const categoryGroupId = $('#categoryGroupId').val();

        if(categoryGroupId.trim() == ''){
            alert('카테고리 그룹 아이디를 입력하세요.');
            return false;
        }

        callAjax('delete', '/manage/category-group/'+categoryGroupId);
    });
});