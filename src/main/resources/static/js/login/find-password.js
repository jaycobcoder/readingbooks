$(function () {
    $('.find-password').on("click", function () {
        const email = $('.email').val();
        const phoneNo = $('.phoneNo').val();

        const data = {
            email: email,
            phoneNo: phoneNo
        }

        console.log(data);

        $.ajax({
            method: "post",
            url: "/account/find-password",
            data: data,
            success: function (data) {
                console.log(data);
            },
            error: function (data) {
                const response = data.responseJSON;
                console.log(response);
            }
        });
        $('.main').html('<h3 class="text-center fs-5 fw-bold">이메일이 전송되었습니다!</h3><div class="text-center fw-bold text-primary mb-2">'+email+'</div><div class="text-grey text-center mb-4">이메일로 전송받은 임시 비밀번호를 확인해주세요.</div><a href="/account/login" class="btn btn-primary w-100 p-3">로그인</a>');
    });

    $('.email').on("blur", function () {
        const email = $(this).val();
        const regEmail = /^[\w-\.]{1,25}@([\w-]+\.)+[\w-]{2,4}$/i;

        if(email.trim() == ''){
            alert('이메일을 입력해주세요.');
            return false;
        }

        if(!email.match(regEmail)){
            alert('이메일 주소를 올바르게 입력해주세요.');
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