$(function () {
    $('.register').on("click", function (){
        const name = $('#name').val();
        const authorOption = $('#authorOption').val();
        const nationality = $('#nationality').val();
        const birthYear = $('#birthYear').val();
        const gender = $('#gender').val();
        const description = $('#description').val();

        const result = validateForm(name, nationality, birthYear, description);
        if(result == false){
            return false;
        }

        const data = {
            "name" : name,
            "authorOption" : authorOption,
            "nationality" : nationality,
            "birthYear" : birthYear,
            "gender" : gender,
            "description" : description
        }

        callAjax('post', '/manage/author', data);
    });

    $('.update').on("click", function (){
        const authorId = $('#authorId').val();
        const name = $('#name').val();
        const authorOption = $('#authorOption').val();
        const nationality = $('#nationality').val();
        const birthYear = $('#birthYear').val();
        const gender = $('#gender').val();
        const description = $('#description').val();

        validateForm(name, nationality, birthYear, description);

        if(authorId.trim() == ''){
            alert('작가 아이디를 입력하세요.');
            return false;
        }

        const data = {
            "name" : name,
            "authorOption" : authorOption,
            "nationality" : nationality,
            "birthYear" : birthYear,
            "gender" : gender,
            "description" : description
        }

        callAjax('patch', '/manage/author/'+authorId, data);
    });

    $('.search').on("click", function (){
        const name = $('#name').val();

        if(name.trim() == ''){
            alert('카테고리 그룹을 입력하세요.');
            return false;
        }

        $('.author-form').submit();
    });

    $('.delete').on("click", function (){
        const authorId = $('#authorId').val();

        if(authorId.trim() == ''){
            alert('작가 아이디를 입력하세요.');
            return false;
        }

        callAjax('delete', '/manage/author/'+authorId);
    });

});

function validateForm(name, nationality, birthYear, description){
    if(name.trim() == ''){
        alert('이름을 입력하세요.');
        return false;
    }
    if(nationality.trim() == ''){
        alert('국적을 입력하세요.');
        return false;
    }
    if(birthYear.trim() == ''){
        alert('생년을 입력하세요.');
        return false;
    }
    if(description.trim() == ''){
        alert('설명을 입력하세요.');
        return false;
    }
}