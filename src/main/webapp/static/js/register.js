var myData = sessionStorage.getItem("occupation");
if (myData === null) {
  location.replace("/home");
}

$(".var-text").text("за " + myData);

switch (myData) {
  case "програмист":
    $("#first-proceed-btn").attr("href", "/register-developer");
    sessionStorage.setItem("occupation", "програмист");
    break;
  case "инженер":
    $("#first-proceed-btn").attr("href", "/register-engineer");
    sessionStorage.setItem("occupation", "инженер");
    break;
  case "дизайнер":
    $("#first-proceed-btn").attr("href", "/register-designer");
    sessionStorage.setItem("occupation", "дизайнер");
    break;
  case "учен":
    $("#first-proceed-btn").attr("href", "/register-scientist");
    sessionStorage.setItem("occupation", "учен");
    break;
  case "музикант":
    $("#first-proceed-btn").attr("href", "/register-musician");
    sessionStorage.setItem("occupation", "музикант");
    break;
  case "артист":
    $("#first-proceed-btn").attr("href", "/register-artist");
    sessionStorage.setItem("occupation", "артист");
    break;
  case "писател":
    $("#first-proceed-btn").attr("href", "/register-writer");
    sessionStorage.setItem("occupation", "писател");
    break;
  case "режисьор":
    $("#first-proceed-btn").attr("href", "/register-filmmaker");
    sessionStorage.setItem("occupation", "режисьор");
    break;
  case "продуктов мениджър":
    $("#first-proceed-btn").attr("href", "/register-product-manager");
    sessionStorage.setItem("occupation", "продуктов мениджър");
    break;
}

$(document).ready(function () {
  // $(":checkbox").labelauty();
});

$("#checkbox1").change(function () {
  if ($(this).is(":checked")) {
    $("#employed-by").prop("disabled", true);
    $("#label-for-employed-by").attr("id", "disabled");
  } else {
    $("#employed-by").prop("disabled", false);
    $("#disabled").attr("id", "label-for-employed-by");
  }
});

$("#first-proceed-btn").click(function (ev) {
  ev.preventDefault();
  var firstNameIcon = $("#profileFirstNameIcon");
  var lastNameIcon = $("#profileLastNameIcon");
  var emailIcon = $("#profileEmailIcon");
  var passwordIcon = $("#profilePasswordIcon");
  var confirmPasswordIcon = $("#profileConfirmPasswordIcon");
  var descriptionIcon = $("#profileDescriptionIcon");

  var firstName = $("#profileFirstName");
  var lastName = $("#profileLastName");
  var email = $("#profileEmail");
  var password = $("#profilePassword");
  var confirmPassword = $("#profileConfirmPassword");
  var description = $("#profileDescription");

  var firstNameLabel = $("#profileFirstNameLabel");
  var lastNameLabel = $("#profileLastNameLabel");
  var emailLabel = $("#profileEmailLabel");
  var passwordLabel = $("#profilePasswordLabel");
  var confirmPasswordLabel = $("#profileConfirmPasswordLabel");
  var descriptionLabel = $("#profileDescriptionLabel");

  //#region first name validation
  if (firstName.val().length === 0) {
    iziToast.error({
      title: "Грешка",
      message: "Името не може да е празно",
      position: "topRight"
    });

    firstNameIcon.attr("style", "color: rgb(230, 92, 92);");
    firstName.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    firstNameLabel.attr("style", "color: rgb(230, 92, 92);");

    firstNameIcon.addClass("validated-icon");
    firstName.addClass("validated-form");
    firstNameLabel.addClass("validated-label");

    $(".validated-form").click(function () {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });
    return false;
  }

  if (firstName.val().length < 2 || firstName.val().length > 16) {
    iziToast.error({
      title: "Грешка",
      message: "Името трябва да е между 2 и 16 символа",
      position: "topRight"
    });

    firstNameIcon.attr("style", "color: rgb(230, 92, 92);");
    firstName.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    firstNameLabel.attr("style", "color: rgb(230, 92, 92);");

    firstNameIcon.addClass("validated-icon");
    firstName.addClass("validated-form");
    firstNameLabel.addClass("validated-label");

    $(".validated-form").click(function () {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }
  //#endregion

  //#region last name validation
  if (lastName.val().length === 0) {
    iziToast.error({
      title: "Грешка",
      message: "Фамилията не може да е празно",
      position: "topRight"
    });

    lastNameIcon.attr("style", "color: rgb(230, 92, 92);");
    lastName.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    lastNameLabel.attr("style", "color: rgb(230, 92, 92);");

    lastNameIcon.addClass("validated-icon");
    lastName.addClass("validated-form");
    lastNameLabel.addClass("validated-label");

    $(".validated-form").click(function () {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }

  if (lastName.val().length < 2 || lastName.val().length > 16) {
    iziToast.error({
      title: "Грешка",
      message: "Фамилията трябва да е мжду 2 и 16 символа",
      position: "topRight"
    });

    lastNameIcon.attr("style", "color: rgb(230, 92, 92);");
    lastName.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    lastNameLabel.attr("style", "color: rgb(230, 92, 92);");

    lastNameIcon.addClass("validated-icon");
    lastName.addClass("validated-form");
    lastNameLabel.addClass("validated-label");

    $(".validated-form").click(function () {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }
  //#endregion

  //#region email validation
  if (email.val().length === 0) {
    iziToast.error({
      title: "Грешка",
      message: "Имейлът не може да е празен",
      position: "topRight"
    });

    emailIcon.attr("style", "color: rgb(230, 92, 92);");
    email.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    emailLabel.attr("style", "color: rgb(230, 92, 92);");

    emailIcon.addClass("validated-icon");
    email.addClass("validated-form");
    emailLabel.addClass("validated-label");

    $(".validated-form").click(function () {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }
  //#endregion

  //#region password validation
  if (password.val().length === 0) {
    iziToast.error({
      title: "Грешка",
      message: "Паролата не може да е празна",
      position: "topRight"
    });

    passwordIcon.attr("style", "color: rgb(230, 92, 92);");
    password.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    passwordLabel.attr("style", "color: rgb(230, 92, 92);");

    passwordIcon.addClass("validated-icon");
    password.addClass("validated-form");
    passwordLabel.addClass("validated-label");

    $(".validated-form").click(function () {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }

  // if (password.val().length < 8 || password.val().length > 20) {

  // }

  var cyrillicTest = new RegExp("[а-яА-ЯЁё]");
  if(password.val().match(cyrillicTest)){
    iziToast.error({
      title: "Грешка",
      message: "Паролата не може да е на кирилица",
      position: "topRight"
    });

    passwordIcon.attr("style", "color: rgb(230, 92, 92);");
    password.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    passwordLabel.attr("style", "color: rgb(230, 92, 92);");

    passwordIcon.addClass("validated-icon");
    password.addClass("validated-form");
    passwordLabel.addClass("validated-label");

    $(".validated-form").click(function () {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }

  var passw = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,20})");
  if (!password.val().match(passw)) {
    iziToast.error({
      title: "Грешка",
      message: "Паролата е в некоректен формат",
      position: "topRight"
    });

    passwordIcon.attr("style", "color: rgb(230, 92, 92);");
    password.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    passwordLabel.attr("style", "color: rgb(230, 92, 92);");

    passwordIcon.addClass("validated-icon");
    password.addClass("validated-form");
    passwordLabel.addClass("validated-label");

    $(".validated-form").click(function () {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }
  //#endregion

  //#region confirm password validation
  if (confirmPassword.val().length === 0) {
    iziToast.error({
      title: "Грешка",
      message: "Моля повторете паролата",
      position: "topRight"
    });

    confirmPasswordIcon.attr("style", "color: rgb(230, 92, 92);");
    confirmPassword.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    confirmPasswordLabel.attr("style", "color: rgb(230, 92, 92);");

    confirmPasswordIcon.addClass("validated-icon");
    confirmPassword.addClass("validated-form");
    confirmPasswordLabel.addClass("validated-label");

    $(".validated-form").click(function () {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }

  if (!(confirmPassword.val() === password.val())) {
    iziToast.error({
      title: "Грешка",
      message: "Паролите не съвпадат",
      position: "topRight"
    });

    confirmPasswordIcon.attr("style", "color: rgb(230, 92, 92);");
    confirmPassword.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    confirmPasswordLabel.attr("style", "color: rgb(230, 92, 92);");

    confirmPasswordIcon.addClass("validated-icon");
    confirmPassword.addClass("validated-form");
    confirmPasswordLabel.addClass("validated-label");

    $(".validated-form").click(function () {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }
  //#endregion

  //#region description validation
  if (description.val().length === 0) {
    iziToast.error({
      title: "Грешка",
      message: "Описанието не може да е празно",
      position: "topRight"
    });

    descriptionIcon.attr("style", "color: rgb(230, 92, 92);");
    description.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    descriptionLabel.attr("style", "color: rgb(230, 92, 92);");

    descriptionIcon.addClass("validated-icon");
    description.addClass("validated-form");
    descriptionLabel.addClass("validated-label");

    $(".validated-form").click(function () {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }

  if (description.val().length < 25) {
    iziToast.error({
      title: "Грешка",
      message: "Описанието трябва да е минимум 25 символа",
      position: "topRight"
    });

    descriptionIcon.attr("style", "color: rgb(230, 92, 92);");
    description.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    descriptionLabel.attr("style", "color: rgb(230, 92, 92);");

    descriptionIcon.addClass("validated-icon");
    description.addClass("validated-form");
    descriptionLabel.addClass("validated-label");

    $(".validated-form").click(function () {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }
  //#endregion

  var firstNameVal = $("#profileFirstName").val();
  var lastNameVal = $("#profileLastName").val();
  var emailVal = $("#profileEmail").val();
  var passwordVal = $("#profilePassword").val();
  var descriptionVal = $("#profileDescription").val();

  sessionStorage.setItem("firstName", firstNameVal);
  sessionStorage.setItem("lastName", lastNameVal);
  sessionStorage.setItem("email", emailVal);
  sessionStorage.setItem("password", passwordVal);
  sessionStorage.setItem("description", descriptionVal);

  $(".var-text")
    .after($("<div/>")
      .addClass("loader-entries"));

  // to check if email is free
  $.ajax({
    url: "/api/checkIfEmailIsTaken?email=" + emailVal,
    method: "POST",
    contentType: "application/json",
    success: function (result) {
      $(".loader-entries").remove();

      if (result === false) {
        iziToast.error({
          title: "Грешка",
          message: "Вече съществува акаунт с този имейл",
          position: "topRight"
        });

        emailIcon.attr("style", "color: rgb(230, 92, 92);");
        email.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
        emailLabel.attr("style", "color: rgb(230, 92, 92);");

        emailIcon.addClass("validated-icon");
        email.addClass("validated-form");
        emailLabel.addClass("validated-label");

        $(".validated-form").click(function () {
          $(this).attr("style", " ");
          $(".validated-label").attr("style", " ");
          $(".validated-icon").attr("style", " ");
        });
      } else {
        window.location.href = $("#first-proceed-btn").attr("href");
      }
    }
  });
});