$(function (){
    $('.leave').on("click", function () {
        const password = $('.password').val();

        if(password.trim() == ''){
            alert('비밀번호를 입력하세요');
            return false;
        }

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            method: "delete",
            url: "/account/leave",
            data: {
                password:password
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (data) {
                console.log(data);
                alert(data.message)
                location.href = "/";
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
                console.log(response);
                alert(response.message);
                location.reload();
            }
        });
    });
});