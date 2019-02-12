$(document).ready(function() {
  if($(window).width() >= 1350 && $(window).width() <= 1368){
    $("#primary-menu").attr("style", "padding-right: 0px;")
  }

  if ($(window).width() <= 1350) {
    $(".logo-text").remove();
    // $(".changeBelowRes").attr(
    //   "style",
    //   "width: 100%;display: flex;justify-content: space-evenly;"
    // );

    $(".logo-image").attr("src", "/static/images/logo2.png");
    $(".logo-image").addClass("width100");
    $(".logo-image").attr("style", "margin-top: 5px;height: 83px;");
  }

  if ($(window).width() <= 1289) {
    $(".changeBelowRes").attr(
      "style",
      "width: 100%;display: flex;justify-content: space-evenly;"
    );
  }

  if ($(window).width() <= 1100) {
    $(".changeBelowRes").attr(
      "style",
      "display: flex;justify-content: space-evenly;width:100%;padding-right: 0px; padding-left: 0px"
    );
    $(".logo-image").removeClass("width100");
    $(".logo-image").addClass("width80");
  }
});
