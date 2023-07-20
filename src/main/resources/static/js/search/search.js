$(function (){
    $('.condition-select').on("change", function () {
        const order = $(this).val();
        const query = $('.query').val();
        const page = $('.page').val();
        location.href='/search?query='+query+'&page='+page+'&order='+order;
    })
});