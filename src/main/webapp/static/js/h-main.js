var previouslyClicked = $("#selected-box-reg");
$(".box-reg").click(function () {
  if (previouslyClicked != null) {
    previouslyClicked.attr("id", "not-selected-box-reg");
  }

  $(this).attr("id", "selected-box-reg");

  previouslyClicked = $(this);

  var innerTextOfBox = $(this)
    .find("h3")
    .text();

  switch (innerTextOfBox) {
    case "Програмист":
      $("#proceed-btn").attr("href", "/register");
      sessionStorage.setItem("occupation", "програмист");
      break;
    case "Инженер":
      $("#proceed-btn").attr("href", "/register");
      sessionStorage.setItem("occupation", "инженер");
      break;
    case "Дизайнер":
      $("#proceed-btn").attr("href", "/register");
      sessionStorage.setItem("occupation", "дизайнер");
      break;
    case "Учен":
      $("#proceed-btn").attr("href", "/register");
      sessionStorage.setItem("occupation", "учен");
      break;
    case "Музикант":
      $("#proceed-btn").attr("href", "/register");
      sessionStorage.setItem("occupation", "музикант");
      break;
    case "Артист":
      $("#proceed-btn").attr("href", "/register");
      sessionStorage.setItem("occupation", "артист");
      break;
    case "Писател":
      $("#proceed-btn").attr("href", "/register");
      sessionStorage.setItem("occupation", "писател");
      break;
    case "Режисьор":
      $("#proceed-btn").attr("href", "/register");
      sessionStorage.setItem("occupation", "режисьор");
      break;
    case "Продуктов мениджър":
      $("#proceed-btn").attr("href", "/register");
      sessionStorage.setItem("occupation", "продуктов мениджър");
      break;
  }
});

$("#proceed-btn").click(function () {
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

$(document).ready(function () {
  $('.contact-form').on('submit', function (ev) {
    var instance = $(".finish-contact")
    ev.preventDefault();
    instance.html('Изпрати  <i class="fas fa-circle-notch fa-spin"></i>');
    $.ajax({
      url: "/api/contact?name=" + $("#contact-name").val() +
        "&email=" + $("#contact-email").val() +
        "&subject=" + $("#contact-subject").val() +
        "&message=" + $("#contact-message").val(),
      method: "GET",
      success: function (result) {
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