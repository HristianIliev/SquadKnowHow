var myData = sessionStorage.getItem("occupation");
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
  case "художник":
    $("#first-proceed-btn").attr("href", "/register-artist");
    sessionStorage.setItem("occupation", "художник");
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

$(document).ready(function() {
  $(":checkbox").labelauty();
});

$("#checkbox1").change(function() {
  if ($(this).is(":checked")) {
    $("#employed-by").prop("disabled", true);
    $("#label-for-employed-by").attr("id", "disabled");
  } else {
    $("#employed-by").prop("disabled", false);
    $("#disabled").attr("id", "label-for-employed-by");
  }
});

$("#first-proceed-btn").click(function(ev) {
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
    firstName.webuiPopover("destroy");

    firstName.webuiPopover({
      placement: "right",
      trigger: "manual",
      content: "Това поле не може да е празно",
      style: "popover",
      closeable: true,
      animation: "pop",
      width: "auto", //can be set with  number
      height: "auto"
    });
    firstName.webuiPopover("show");

    firstNameIcon.attr("style", "color: rgb(230, 92, 92);");
    firstName.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    firstNameLabel.attr("style", "color: rgb(230, 92, 92);");

    firstNameIcon.addClass("validated-icon");
    firstName.addClass("validated-form");
    firstNameLabel.addClass("validated-label");

    $(".validated-form").click(function() {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });
    return false;
  }

  if (firstName.val().length < 2 || firstName.val().length > 16) {
    firstName.webuiPopover("destroy");

    firstName.webuiPopover({
      placement: "right",
      trigger: "manual",
      content: "Трябва да е между 2 и 16 символа",
      style: "popover",
      closeable: true,
      animation: "pop",
      width: "auto", //can be set with  number
      height: "auto"
    });
    firstName.webuiPopover("show");

    firstNameIcon.attr("style", "color: rgb(230, 92, 92);");
    firstName.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    firstNameLabel.attr("style", "color: rgb(230, 92, 92);");

    firstNameIcon.addClass("validated-icon");
    firstName.addClass("validated-form");
    firstNameLabel.addClass("validated-label");

    $(".validated-form").click(function() {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }
  //#endregion

  //#region last name validation
  if (lastName.val().length === 0) {
    lastName.webuiPopover("destroy");
    lastName.webuiPopover({
      placement: "right",
      trigger: "manual",
      content: "Това поле не може да бъде празно",
      style: "popover",
      closeable: true,
      animation: "pop",
      width: "auto", //can be set with  number
      height: "auto"
    });
    lastName.webuiPopover("show");

    lastNameIcon.attr("style", "color: rgb(230, 92, 92);");
    lastName.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    lastNameLabel.attr("style", "color: rgb(230, 92, 92);");

    lastNameIcon.addClass("validated-icon");
    lastName.addClass("validated-form");
    lastNameLabel.addClass("validated-label");

    $(".validated-form").click(function() {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }

  if (lastName.val().length < 2 || lastName.val().length > 16) {
    lastName.webuiPopover("destroy");
    lastName.webuiPopover({
      placement: "right",
      trigger: "manual",
      content: "Трябва да е между 2 и 16 символа",
      style: "popover",
      closeable: true,
      animation: "pop",
      width: "auto", //can be set with  number
      height: "auto"
    });
    lastName.webuiPopover("show");

    lastNameIcon.attr("style", "color: rgb(230, 92, 92);");
    lastName.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    lastNameLabel.attr("style", "color: rgb(230, 92, 92);");

    lastNameIcon.addClass("validated-icon");
    lastName.addClass("validated-form");
    lastNameLabel.addClass("validated-label");

    $(".validated-form").click(function() {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }
  //#endregion

  //#region email validation
  if (email.val().length === 0) {
    email.webuiPopover("destroy");
    email.webuiPopover({
      placement: "right",
      trigger: "manual",
      content: "Това поле не може да бъде празно",
      style: "popover",
      closeable: true,
      animation: "pop",
      width: "auto", //can be set with  number
      height: "auto"
    });
    email.webuiPopover("show");

    emailIcon.attr("style", "color: rgb(230, 92, 92);");
    email.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    emailLabel.attr("style", "color: rgb(230, 92, 92);");

    emailIcon.addClass("validated-icon");
    email.addClass("validated-form");
    emailLabel.addClass("validated-label");

    $(".validated-form").click(function() {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }
  //#endregion

  //#region password validation
  if (password.val().length === 0) {
    password.webuiPopover("destroy");
    password.webuiPopover({
      placement: "right",
      trigger: "manual",
      content: "Това поле не може да бъде празно",
      style: "popover",
      closeable: true,
      animation: "pop",
      width: "auto", //can be set with  number
      height: "auto"
    });
    password.webuiPopover("show");

    passwordIcon.attr("style", "color: rgb(230, 92, 92);");
    password.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    passwordLabel.attr("style", "color: rgb(230, 92, 92);");

    passwordIcon.addClass("validated-icon");
    password.addClass("validated-form");
    passwordLabel.addClass("validated-label");

    $(".validated-form").click(function() {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }

  if (password.val().length < 8 || password.val().length > 20) {
    password.webuiPopover("destroy");
    password.webuiPopover({
      placement: "right",
      trigger: "manual",
      content: "Трябва да е между 8 и 20",
      style: "popover",
      closeable: true,
      animation: "pop",
      width: "auto", //can be set with  number
      height: "auto"
    });
    password.webuiPopover("show");

    passwordIcon.attr("style", "color: rgb(230, 92, 92);");
    password.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    passwordLabel.attr("style", "color: rgb(230, 92, 92);");

    passwordIcon.addClass("validated-icon");
    password.addClass("validated-form");
    passwordLabel.addClass("validated-label");

    $(".validated-form").click(function() {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }
  //#endregion

  //#region confirm password validation
  if (confirmPassword.val().length === 0) {
    confirmPassword.webuiPopover("destroy");
    confirmPassword.webuiPopover({
      placement: "right",
      trigger: "manual",
      content: "Това поле не може да бъде празно",
      style: "popover",
      closeable: true,
      animation: "pop",
      width: "auto", //can be set with  number
      height: "auto"
    });
    confirmPassword.webuiPopover("show");

    confirmPasswordIcon.attr("style", "color: rgb(230, 92, 92);");
    confirmPassword.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    confirmPasswordLabel.attr("style", "color: rgb(230, 92, 92);");

    confirmPasswordIcon.addClass("validated-icon");
    confirmPassword.addClass("validated-form");
    confirmPasswordLabel.addClass("validated-label");

    $(".validated-form").click(function() {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }

  if (!(confirmPassword.val() === password.val())) {
    confirmPassword.webuiPopover("destroy");
    confirmPassword.webuiPopover({
      placement: "right",
      trigger: "manual",
      content: "Паролите не съвпадат",
      style: "popover",
      closeable: true,
      animation: "pop",
      width: "auto", //can be set with  number
      height: "auto"
    });
    confirmPassword.webuiPopover("show");

    confirmPasswordIcon.attr("style", "color: rgb(230, 92, 92);");
    confirmPassword.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    confirmPasswordLabel.attr("style", "color: rgb(230, 92, 92);");

    confirmPasswordIcon.addClass("validated-icon");
    confirmPassword.addClass("validated-form");
    confirmPasswordLabel.addClass("validated-label");

    $(".validated-form").click(function() {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }
  //#endregion

  //#region description validation
  if (description.val().length === 0) {
    description.webuiPopover("destroy");
    description.webuiPopover({
      placement: "right",
      trigger: "manual",
      content: "Това поле не може да бъде празно",
      style: "popover",
      closeable: true,
      animation: "pop",
      width: "auto", //can be set with  number
      height: "auto"
    });
    description.webuiPopover("show");

    descriptionIcon.attr("style", "color: rgb(230, 92, 92);");
    description.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    descriptionLabel.attr("style", "color: rgb(230, 92, 92);");

    descriptionIcon.addClass("validated-icon");
    description.addClass("validated-form");
    descriptionLabel.addClass("validated-label");

    $(".validated-form").click(function() {
      $(this).attr("style", " ");
      $(".validated-label").attr("style", " ");
      $(".validated-icon").attr("style", " ");
    });

    return false;
  }

  if (description.val().length < 25) {
    description.webuiPopover("destroy");
    description.webuiPopover({
      placement: "right",
      trigger: "manual",
      content: "Трябва да е минимум 25 символа",
      style: "popover",
      closeable: true,
      animation: "pop",
      width: "auto", //can be set with  number
      height: "auto"
    });
    description.webuiPopover("show");

    descriptionIcon.attr("style", "color: rgb(230, 92, 92);");
    description.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
    descriptionLabel.attr("style", "color: rgb(230, 92, 92);");

    descriptionIcon.addClass("validated-icon");
    description.addClass("validated-form");
    descriptionLabel.addClass("validated-label");

    $(".validated-form").click(function() {
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
    success: function(result) {
      $(".loader-entries").remove();

      if (result === false) {
        email.webuiPopover("destroy");
        email.webuiPopover({
          placement: "right",
          trigger: "manual",
          content: "Имейлът е вече зает",
          style: "popover",
          closeable: true,
          animation: "pop",
          width: "auto",
          height: "auto"
        });
        email.webuiPopover("show");

        emailIcon.attr("style", "color: rgb(230, 92, 92);");
        email.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
        emailLabel.attr("style", "color: rgb(230, 92, 92);");

        emailIcon.addClass("validated-icon");
        email.addClass("validated-form");
        emailLabel.addClass("validated-label");

        $(".validated-form").click(function() {
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
