var previouslyClicked = $("#selected-box-reg");
$(".box-reg").click(function() {
    if (previouslyClicked != null) {
        previouslyClicked.attr("id", "not-selected-box-reg");
    }

    $(this).attr("id", "selected-box-reg");

    previouslyClicked = $(this);

    var innerTextOfBox = $(this)
        .find("h3")
        .text();

    switch (innerTextOfBox) {
        case "Programmer":
            $("#proceed-btn").attr("href", "/register");
            sessionStorage.setItem("occupation", "programmer");
            break;
        case "Engineer":
            $("#proceed-btn").attr("href", "/register");
            sessionStorage.setItem("occupation", "engineer");
            break;
        case "Designer":
            $("#proceed-btn").attr("href", "/register");
            sessionStorage.setItem("occupation", "designer");
            break;
        case "Scientist":
            $("#proceed-btn").attr("href", "/register");
            sessionStorage.setItem("occupation", "scientist");
            break;
        case "Musician":
            $("#proceed-btn").attr("href", "/register");
            sessionStorage.setItem("occupation", "musician");
            break;
        case "Artist":
            $("#proceed-btn").attr("href", "/register");
            sessionStorage.setItem("occupation", "artist");
            break;
        case "Writer":
            $("#proceed-btn").attr("href", "/register");
            sessionStorage.setItem("occupation", "writer");
            break;
        case "Filmmaker":
            $("#proceed-btn").attr("href", "/register");
            sessionStorage.setItem("occupation", "filmmaker");
            break;
        case "Product manager":
            $("#proceed-btn").attr("href", "/register");
            sessionStorage.setItem("occupation", "product manager");
            break;
    }
});

$("#proceed-btn").click(function() {
    var selectedBox = $("#selected-box-reg");
    var innerTextOfBox = $(selectedBox)
        .find("h3")
        .text();
    if (innerTextOfBox === "") {
        iziToast.error({
            title: "Грешка",
            message: "За да продължите първо трябва да изберете сфера на вашите умения",
            position: "topRight"
        });

        return false;
    }
});

$(document).ready(function() {
    $('.contact-form').on('submit', function(ev) {
        var instance = $(".finish-contact")
        ev.preventDefault();
        instance.html('Изпрати  <i class="fas fa-circle-notch fa-spin"></i>');
        $.ajax({
            url: "/api/contact?name=" + $("#contact-name").val() +
                "&email=" + $("#contact-email").val() +
                "&subject=" + $("#contact-subject").val() +
                "&message=" + $("#contact-message").val(),
            method: "GET",
            success: function(result) {
                instance.html('Изпрати');
                if (result === 'true' || result === true) {
                    iziToast.success({
                        title: "OK",
                        message: "Нашият екип ще се свържи с вас възможно най-скоро",
                        position: "topRight"
                    });
                }
            }
        })
    })

    return false;
});