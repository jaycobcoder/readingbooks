$(function () {
    let isValidatedEmail = false;
    let isValidatedPassword = false;
    let isValidatedName = false;
    let isValidatedBirthYear = false;
    let isValidPhoneNo = false;


    $('#email').blur(function () {
        const email = $(this).val();
        const regEmail = /^[\w-\.]{1,25}@([\w-]+\.)+[\w-]{2,4}$/i;
        $('.email-result').html('');

        if(email.trim() == ''){
            $('.email-result').html('이메일을 입력해주세요');
            isValidatedEmail = false;
            return false;
        }

        if(!email.match(regEmail)){
            $('.email-result').html('이메일 주소를 올바르게 입력해주세요.');
            isValidatedEmail = false;
            return false;
        }

        $.ajax({
            method: "post",
            url: "/register/validate/email",
            data: {
                email: email
            },
            async: false,
            success: function (result){
                console.log(result);
            },
            error: function (result){
                const response = result.responseJSON;
                console.log(response);
                $('.email-result').html(response.message);
                isValidatedEmail = false;
            }
        });
        isValidatedEmail = true;
    });

    $('#password').blur(function () {
        const password = $(this).val();
        const passwordConfirm = $("#passwordConfirm").val();
        const regPassword = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/i;

        if(password.trim() == ''){
            $('.password-result').html('비밀번호를 입력해주세요');
            isValidatedPassword = false;
            return false;
        }

        if(password != passwordConfirm){
            $('.password-result').html('비밀번호가 일치하지 않습니다.');
            isValidatedPassword = false;
            return false;
        }

        if(!password.match(regPassword)){
            $('.password-result').html('8~16자로 구성하세요. 숫자, 특수문자가 하나 이상 포함, 대문자 또는 소문자가 포함되어야 합니다. 특수문자는 @, !, %, *, #, ?, &를 사용할 수 있습니다.');
            isValidatedPassword = false;
            return false;
        }

        isValidatedPassword = true;
        $('.password-result').html('');
    });

    $('#passwordConfirm').blur(function () {
        const password = $('#password').val();
        const passwordConfirm = $(this).val();
        const regPassword = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/i;

        if(passwordConfirm.trim() == ''){
            $('.password-result').html('비밀번호를 입력해주세요');
            isValidatedPassword = false;
            return false;
        }

        if(password != passwordConfirm){
            $('.password-result').html('비밀번호가 일치하지 않습니다.');
            isValidatedPassword = false;
            return false;
        }

        if(!passwordConfirm.match(regPassword)){
            $('.password-result').html('8~16자로 구성하세요. 숫자, 특수문자가 하나 이상 포함, 대문자 또는 소문자가 포함되어야 합니다. 특수문자는 @, !, %, *, #, ?, &를 사용할 수 있습니다.');
            isValidatedPassword = false;
            return false;
        }
        isValidatedPassword = true;
        $('.password-result').html('');
    });

    $('#name').blur(function () {
        const name = $(this).val();
        const regName = /^[가-힣a-zA-Z]{2,25}$/i;

        if(name.trim() == ''){
            $('.name-result').html('이름을 입력해주세요.');
            isValidatedName = false;
            return false;
        }

        if(!name.match(regName)){
            $('.name-result').html('2~25자로 구성하세요. 이름을 정확히 입력하세요.');
            isValidatedName = false;
            return false;
        }
        isValidatedName = true;
        $('.name-result').html('');
    });

    $('#phoneNo').blur(function () {
        const regPhoneNo = /^010\d{8}$/;
        const phoneNo=$('#phoneNo').val().replace(/-/g, '');
        $('#phoneNo').val(phoneNo);
        if(!phoneNo.match(regPhoneNo)){
            $('.phoneNo-result').html('정확히 입력해주세요.');
            isValidPhoneNo = false;
            return;
        }
        $('.phoneNo-result').html('');
        isValidPhoneNo = true;
    });

    $('#birthYear').blur(function () {
        const birthYear = $(this).val();

        const replaceYear = birthYear.replace(/[^0-9]/g, '');
        const regBirthYear = /^(19|20)\d{2}$/
        $(this).val(replaceYear);

        if(replaceYear.trim() == ''){
            $('.birthYear-result').html('생년월일을 입력해주세요.');
            isValidatedBirthYear = false;
            return false;
        }

        if(!replaceYear.match(regBirthYear)){
            $('.birthYear-result').html('생년월일을 올바르게 입력해주세요.');
            isValidatedBirthYear = false;
            return false;
        }

        isValidatedBirthYear = true;
        $('.birthYear-result').html('');
    });

    $('#register').on("click", function (){
        if(isValidatedEmail == false || isValidatedPassword == false || isValidatedName == false || isValidatedBirthYear == false){
            alert('양식을 다시 확인해주세요');
            return false;
        }

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        const email = $('#email').val();
        const password = $('#password').val();
        const passwordConfirm = $('#passwordConfirm').val();
        const name = $('#name').val();
        const birthYear = $('#birthYear').val();
        const gender = $('#gender').val();
        const phoneNo = $('#phoneNo').val();

        const data = {
            "email" : email,
            "password": password,
            "passwordConfirm" : passwordConfirm,
            "name" : name,
            "birthYear" : birthYear,
            "gender": gender,
            "phoneNo": phoneNo
        }
        $.ajax({
            type: "post",
            url: "/register",
            data: data,
            beforeSend : function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data){
                console.log(data);
                alert(data.message)
                location.replace("/");
            },
            error: function (data){
                const response = data.responseJSON;
                alert(response.message);
            }
        });
    });

});