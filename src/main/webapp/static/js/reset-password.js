var arr = window.location.pathname.split("/");
var id = arr[arr.length - 1];

$(".form-register-design").submit(function(event) {
  event.preventDefault();

  var password = $("#password").val();
  var repassword = $("#repassword").val();

  if (password === repassword) {
    $("#reset-password-btn").html(
      'Промени <i class="fas fa-circle-notch fa-spin"></i>'
    );

    $.ajax({
      url: "/api/reset-password?password=" + password + "&id=" + id,
      method: "GET",
      success: function(result) {
        $("#reset-password-btn").html(
          'Промени'
        );
        window.location.href = "/sign-in?changed";
      }
    });
  } else {
    iziToast.error({
      title: "Грешка!",
      message: "Паролите не съвпадат",
      position: "topRight"
    });
  }
});
