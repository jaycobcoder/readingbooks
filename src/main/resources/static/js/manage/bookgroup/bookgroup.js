$(function () {
    $('.register').on("click", function (){
        const title = $('#title').val();
        const file = $('#file')[0].files[0];

        const isValidTitle = validateTitle(title);
        if(isValidTitle == false){
            return false;
        }

        const isValidFile = validateFile(file);
        if(isValidFile == false){
            return false;
        }

        const formData = new FormData();
        formData.append('file', file);
        formData.append('title', title)

        uploadFileUsingAjax('post', '/manage/book-group', formData);
    });

    $('.update-title').on("click", function (){
        const bookGroupId = $('#bookGroupId-content').val();
        const title = $('#title').val();

        const isValidId = validateId(bookGroupId);
        if(isValidId == false){
            return false;
        }

        const isValidTitle = validateTitle(title);
        if(isValidTitle == false){
            return false;
        }

        const data = {
            "title" : title
        }

        callAjax("patch", "/manage/book-group/title/"+bookGroupId, data);
    });

    $('.update-image').on("click", function (){
        const bookGroupId = $('#bookGroupId-image').val();
        const file = $('#file')[0].files[0];

        const isValidId = validateId(bookGroupId);
        if(isValidId == false){
            return false;
        }

        const isValidFile = validateFile(file);
        if(isValidFile == false){
            return false;
        }

        const formData = new FormData();
        formData.append('file', file);
        uploadFileUsingAjax('patch', '/manage/book-group/image/'+bookGroupId, formData);
    });

    $('.search').on("click", function (){
        const title = $('#title').val();

        const isValidTitle = validateTitle(title);
        if(isValidTitle == false){
            return false;
        }

        $('.bookgroup-form').submit();
    });

    $('.delete').on("click", function (){
        const bookGroupId = $('#bookGroupId').val();

        const isValidId = validateId(bookGroupId);
        if(isValidId == false){
            return false;
        }

        const result = confirm('삭제하면 복구할 수 없습니다. 삭제하시겠습니까?');
        if(result == false){
            return false;
        }

        callAjax('delete', '/manage/book-group/'+bookGroupId);
    });
});

function validateFile(file){
    const MAX_SIZE = 5*1024*1024;
    let fileSize = 0;

    if(file != undefined){
        fileSize = file.size;
    }

    if(fileSize == 0){
        alert('이미지를 업로드해주세요.');
        return false;
    }

    if(fileSize > MAX_SIZE){
        alert('이미지의 크기는 5MB까지 업로드할 수 있습니다.');
        return false;
    }
}

function validateTitle(title) {
    if(title.trim() == ''){
        alert('도서 그룹 제목을 입력해주세요.')
        return false;
    }
}

function validateId(bookGroupId) {
    if(bookGroupId.trim() == ''){
        alert('도서 그룹 아이디를 입력해주세요.')
        return false;
    }
}