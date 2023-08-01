function initCheckbox() {
    $(".checkbox").prop("checked", true);
}

function callAjaxWithList(method, url, data) {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        method: method,
        url: url,
        data: data,
        traditional: true,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            console.log(data);
            alert(data.message)
            location.reload();
        },
        error: function (data) {
            if (data.status == 401) {
                location.href = '/account/login';
                return false;
            }
            const response = data.responseJSON;
            console.log(response);
            alert(response.message);
            location.reload();
        }
    });
}

function numberWithCommas(numbers) {
    return numbers.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

$(function (){
    initCheckbox();

    $('#select-all').on("click", function (){
        $('.check-book').prop("checked", $(this).prop("checked")).trigger("change");
    });

    $('.check-book').on("change", function (){
        let totalDiscountPrice = 0;
        let totalEbookPrice = 0;
        let totalSalePrice = 0;

        $('.check-book').each(function () {
            if ($(this).prop("checked")) {
                const salePrice = $(this).data('sale-price');
                totalSalePrice += salePrice;

                const discountPrice = $(this).data('discount-price');
                totalDiscountPrice += discountPrice;

                const ebookPrice = $(this).data('ebook-price');
                totalEbookPrice += ebookPrice;
            }
        });

        if(totalSalePrice >= 1000){
            totalSalePrice = numberWithCommas(totalSalePrice);
        }
        if(totalDiscountPrice >= 1000){
            totalDiscountPrice = numberWithCommas(totalDiscountPrice);
        }
        if(totalEbookPrice >= 1000){
            totalEbookPrice = numberWithCommas(totalEbookPrice);
        }
        $('.total-discount-price').html(totalDiscountPrice);
        $('.total-sale-price').html(totalSalePrice);
        $('.total-price').html(totalEbookPrice);
    });

    $(".check-book").on("click", function (){
        let isAllChecked = true;
        $('.check-book').each(function() {
            if (!$(this).prop("checked")) {
                isAllChecked = false;
                return false;
            }
        });

        $('#select-all').prop("checked", isAllChecked);
    });

    $('.delete-all').on("click", function (){
        const wishlistIdList = new Array();
        addWishlistIdList(wishlistIdList);

        if(wishlistIdList.length == 0){
            return false;
        }

        const data = {
            wishlistIdList : wishlistIdList
        }
        callAjaxWithList("delete", "/wishlist", data);
    });

    $('.delete-one').on("click", function (){
        const wishlistId = $(this).data('wishlist');
        const wishlistIdList = new Array();
        wishlistIdList.push(wishlistId);

        if(wishlistIdList.length == 0){
            return false;
        }

        const data = {
            wishlistIdList : wishlistIdList
        }
        callAjaxWithList("delete", "/wishlist", data);
    });


    $(".order-all").on("click", async function (){
        const bookIdList = new Array();

        addBookIdList(bookIdList);

        if(bookIdList.length == 0){
            return false;
        }

        const data = await getData("post", "/wishlist/pay-information", bookIdList);
        console.log(data);
        IMP.request_pay(
            {
                pg: "html5_inicis.INIpayTest",
                pay_method: "card",
                merchant_uid: data.orderNo,
                name: data.orderName,
                amount: 100,
                buyer_email: data.email,
                buyer_name: data.name,
            },
            function (response) {
                console.log(response);

                if (response.success == false) {
                    alert("결제에 실패하였습니다. 에러 내용: " + response.error_msg);
                    return false;
                }
                const request = {
                    "orderName" : response.name,
                    "orderNo" : response.merchant_uid,
                    "impUid" : response.imp_uid,
                    "choosingOption" : response.pay_method,
                    "email" : response.buyer_email,
                    "orderAmount" : data.orderAmount,
                    "discountAmount" : data.discountAmount,
                    "paymentAmount" : data.paymentAmount,
                    "bookIdList" : bookIdList
                }

                callAjaxWithList("post", "/order", request);
            }
        );
    })

    function addBookIdList(bookIdList) {
        $('.check-book').each(function () {
            if ($(this).prop("checked")) {
                const bookId = $(this).data('book');
                bookIdList.push(bookId);
            }
        });
    }

    function addWishlistIdList(wishlistIdList) {
        $('.check-book').each(function () {
            if ($(this).prop("checked")) {
                const wishlistId = $(this).data('wishlist');
                wishlistIdList.push(wishlistId);
            }
        });
    }
});