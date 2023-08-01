function starRatingEvent(){
    $('input[name=rating]').on("change", function () {
        let rating = $('input[name=rating]:checked').val();
        if (rating == 1) {
            $(".star-comment").html("별로예요");
        }
        else if (rating == 2) {
            $(".star-comment").html("그저 그래요");
        }
        else if (rating == 3) {
            $(".star-comment").html("보통이에요");
        }
        else if (rating == 4) {
            $(".star-comment").html("좋아요");
        }
        else if (rating == 5) {
            $(".star-comment").html("최고의 책!");
        }
    });
}
$(function () {
    $('input[name=rating]').on("change", function () {
        starRatingEvent();
    });

    $('.wishlist-btn').on("click", function (){
        const bookId = $(this).val();
        const data = {
            "bookId" : bookId
        }
        callAjax("post", "/wishlist", data);
    });

    $('.author-btn').on("click", async function () {
        const authorId = $(this).val();
        const currentUrl = window.location.href;
        const data = await getData("get", currentUrl + "/" + authorId);
        console.log(data);
        $('.author-name').html(data.name);
        $('.author-nationality').html(data.nationality);
        $('.author-birthYear').html(data.birthYear);
        $('.author-gender').html(data.gender);
        $('.author-description').html(data.description);
    });
});