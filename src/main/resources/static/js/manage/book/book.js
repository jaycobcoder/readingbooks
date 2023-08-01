$(function () {
    $('.register').on("click", function (){
        const title = $('#title').val();
        const isbn = $('#isbn').val();
        const publisher = $('#publisher').val();
        const publishingDate = $('#publishingDate').val();
        let paperPrice = $('#paperPrice').val();
        const ebookPrice = $('#ebookPrice').val();
        const discountRate = $('#discountRate').val();
        const categoryId = $('#categoryId').val();
        const description = $('#description').val();
        let bookGroupId = $('#bookGroupId').val();
        const file = $('#file')[0].files[0];

        const isValidForm = validateRequestForm(title, isbn, publisher, publishingDate, ebookPrice, discountRate, categoryId, description);
        if(isValidForm == false){
            return false;
        }

        if(paperPrice.trim() == ''){
            paperPrice = 0;
        }
        if(bookGroupId.trim() == ''){
            bookGroupId = 0;
        }

        const isValidFile = validateFile(file);
        if(isValidFile == false){
            return false;
        }

        const formData = new FormData();
        formData.append('title', title);
        formData.append('isbn', isbn);
        formData.append('publisher', publisher);
        formData.append('publishingDate', publishingDate);
        formData.append('paperPrice', paperPrice);
        formData.append('ebookPrice', ebookPrice);
        formData.append('discountRate', discountRate);
        formData.append('categoryId', categoryId);
        formData.append('bookGroupId', bookGroupId);
        formData.append("description", description);
        formData.append('file', file);

        uploadFileUsingAjax('post', '/manage/book', formData);
    });

    $('.update-search').on("click", function (){
        const bookId = $('#bookId').val();
        if(bookId.trim() == ''){
            alert('도서 아이디를 입력해주세요');
            return false;
        }

        $('#bookSearchForm').submit();
    });

    $('.update-content').on("click", function (){
        const bookId = $('#bookId-content').val();
        const title = $('#title').val();
        const isbn = $('#isbn').val();
        const publisher = $('#publisher').val();
        const publishingDate = $('#publishingDate').val();
        let paperPrice = $('#paperPrice').val();
        const ebookPrice = $('#ebookPrice').val();
        const discountRate = $('#discountRate').val();
        const categoryId = $('#categoryId').val();
        let bookGroupId = $('#bookGroupId').val();
        const description = ckeditorInstance.getData()

        if(bookId.trim() == ''){
            alert('도서 아이디를 입력해주세요');
            return false;
        }

        validateRequestForm(title, isbn, publisher, publishingDate, ebookPrice, discountRate, categoryId, description);

        if(paperPrice.trim() == ''){
            paperPrice = 0;
        }
        if(bookGroupId.trim() == ''){
            bookGroupId = 0;
        }

        const data = {
            "title" : title,
            "isbn" : isbn,
            "publisher" : publisher,
            "publishingDate" : publishingDate,
            "paperPrice" : paperPrice,
            "ebookPrice" : ebookPrice,
            "discountRate" : discountRate,
            "categoryId" : categoryId,
            "bookGroupId" : bookGroupId,
            "description" : description
        }

        callAjax('patch', '/manage/book/content/'+bookId, data);
    });

    $('.update-image').on("click", function (){
        const bookId = $('#bookId-image').val();
        const file = $('#file')[0].files[0];

        if(bookId.trim() == ''){
            alert('도서 아이디를 입력해주세요');
            return false;
        }

        const isValidFile = validateFile(file);
        if(isValidFile == false){
            return false;
        }

        const formData = new FormData();
        formData.append('file', file);

        uploadFileUsingAjax('patch', '/manage/book/image/'+bookId, formData);
    });

    $('.search').on("click", function (){
        const title = $('#title').val();
        if(title.trim() == ''){
            alert('도서 제목을 입력하세요.');
            return false;
        }

        $(".book-form").submit();
    });

    $('.delete').on("click", function (){
        const bookId = $('#bookId').val();
        if(bookId.trim() == ''){
            alert('도서 아이디를 입력하세요.');
            return false;
        }

        const result = confirm('삭제하면 복구할 수 없습니다. 삭제하시겠습니까?');
        if(result == false){
            return false;
        }

        callAjax('delete', '/manage/book/'+bookId);
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

function validateRequestForm(title, isbn, publisher, publishingDate, ebookPrice, discountRate, categoryId, description) {
    if(title.trim() == ''){
        alert('도서 제목을 입력해주세요.')
        return false;
    }

    if(isbn.trim() == ''){
        alert('isbn을 입력해주세요.')
        return false;
    }

    if(publisher.trim() == ''){
        alert('출판사를 입력해주세요.')
        return false;
    }

    if(publishingDate.trim() == ''){
        alert('출판일을 입력해주세요.')
        return false;
    }

    if(ebookPrice.trim() == ''){
        alert('전자책 가격을 입력해주세요.')
        return false;
    }

    if(discountRate.trim() == ''){
        alert('할인율을 입력해주세요.')
        return false;
    }

    if(categoryId.trim() == ''){
        alert('카테고리 아이디를 입력해주세요.')
        return false;
    }
    
    if(description.trim() == ''){
        alert('도서 설명을 입력해주세요.')
        return false;
    }
}
