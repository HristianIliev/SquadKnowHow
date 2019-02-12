var arr = window.location.pathname.split("/");
var projectId = arr[arr.length - 1];
$(document).ready(function () {
    $.ajax({
        url: "/api/getEmbedCode?projectId=" + projectId,
        method: "GET",
        success: function (result) {
            $(".here").append(result);
        }
    });

    $('.preloader').fadeOut(500);
});