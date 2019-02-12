$(document).ready(function() {
  if ($(window).width() <= 1600) {
    // $(".changeBelow1280-2").attr("style", "width: 100%");
  }

  if ($(window).width() <= 1366 && $(window).width() >= 1355) {
    $(".navbar-brand").attr("style", "padding: 15px 5px;");
  }

  if ($(window).width() <= 1354) {
    $(".logo-text").remove();
    $(".logo-image").attr("src", "/static/images/logo2.png");
    $(".logo-image").addClass("width100");
    // $(".changeBelow1280-2").attr(
    //   "style",
    //   "display: flex;justify-content: space-evenly;width: 100%"
    // );
  }

  if ($(window).width() <= 1200) {
    // $(".changeBelow1280-2").attr(
    //   "style",
    //   "display: flex;justify-content: space-evenly;width: 100%"
    // );
  }

  if ($(window).width() <= 1100) {
    $(".logo-image").removeClass("width100");
    $(".logo-image").addClass("width80");
    $(".changeBelow1280-2").attr(
      "style",
      "display: flex;justify-content: space-evenly;width: 100%;font-size: 13px;padding-left: 0px; padding-right: 0px;"
    );
  }
});
