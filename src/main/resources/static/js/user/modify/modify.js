$(function () {
    $('.btn-confirm').on("click", function (){
        const password = $('.password').val();

        if(password.trim() == ''){
            alert('비밀번호를 입력해주세요');
            return false;
        }

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            method: "post",
            url: "/user/password-confirm",
            data: {
                password : password
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                console.log(data);
                const modifyWrap = $('.modify-wrap');
                modifyWrap.html('<table class="table"><tr><th class="text-end fw-bold">이름</th><td class="d-flex justify-content-between"><span>'+data.name+'</span><a href="/account/leave" class="btn btn-outline-grey btn-leave">회원탈퇴</a></td></tr><tr><th class="text-end fw-bold">아이디(이메일)</th><td class="d-flex justify-content-between"><span>'+data.email+'</span></td></tr><tr><th class="text-end fw-bold">비밀번호변경</th><td class="d-flex justify-content-between d-flex flex-column"><input type="password"name=""class="form-control mb-1 password"placeholder="현재 비밀번호"/><input type="password"name=""class="form-control mb-1 new-password"placeholder="새 비밀번호"/><input type="password"name=""class="form-control mb-1 new-password-confirm"placeholder="새 비밀번호 확인"/><button type="button"class="btn btn-primary btn-change-password">비밀번호 변경</button></td></tr></table>'
                );
            },
            error: function (data) {
                if(data.status == 401){
                    const returnUrl = window.location.href;
                    location.href = '/account/login?returnUrl='+returnUrl;
                    return false;
                }
                if(data.status == 403){
                    const returnUrl = window.location.href;
                    location.href = '/account/login?returnUrl='+returnUrl;
                    return false;
                }
                const response = data.responseJSON;
                alert(response.message);
                location.reload();
            }
        });
    });

    $(document).on("click", ".btn-change-password", function (){
        const password = $('.password').val();
        const newPassword = $('.new-password').val();
        const newPasswordConfirm = $('.new-password-confirm').val();

        const regPassword = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/i;

        if(password.trim() == '' || newPassword.trim() == '' || newPasswordConfirm.trim() == ''){
            alert('현재 비밀번호나 새 비밀번호 또는 새 비밀번호 확인을 입력하세요.');
            return false;
        }

        if(!newPassword.match(regPassword) || !newPasswordConfirm.match(regPassword)){
            alert('변경할 비밀번호를 8~16자로 구성하세요. 숫자, 특수문자가 하나 이상 포함, 대문자 또는 소문자가 포함되어야 합니다. 특수문자는 @, !, %, *, #, ?, &를 사용할 수 있습니다.');
            return false;
        }

        if(newPassword != newPasswordConfirm){
            alert('변경할 비밀번호가 일치하지 않습니다.');
            return false;
        }

        const data = {
            password : password,
            newPassword : newPassword,
            newPasswordConfirm : newPasswordConfirm
        }
        callAjax("patch", "/user/password", data);
    });
});