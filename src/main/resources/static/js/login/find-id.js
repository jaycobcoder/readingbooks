$(function () {
    $('.find-id').on("click", function (){
        const name = $('.name').val();
        const phoneNo=$('.phoneNo').val();
        const data = {
            name : name,
            phoneNo : phoneNo
        }
        const result = getData("post", "/account/find-id", data);

        result.then((maskedEmailArray) => {
            $('.main').html('<h3>회원님의 아이디는 다음과 같습니다.</h3>');

            maskedEmailArray.forEach((email) => {
                $('.main').append(email);
            });

            $('.main').append('<a href="/account/login" class="btn btn-primary w-100 mt-3">로그인</a>')
            $('.main').append('<a href="/account/find-password" class="btn btn-outline-grey w-100 mt-3">비밀번호 찾기</a>')
        });
    });

    $('.name').on("blur", function (){
        const name = $('.name').val();

        if(name.trim() == ''){
            alert('이름을 입력해주세요.');
            return false;
        }
    });

    $('.phoneNo').on("blur", function (){
        const regPhoneNo = /^010\d{8}$/;
        const phoneNo=$('.phoneNo').val().replace(/-/g, '');
        $('.phoneNo').val(phoneNo);

        if(!phoneNo.match(regPhoneNo)){
            alert('핸드폰 번호를 정확히 입력해주세요.');
            return false;
        }
    });
});