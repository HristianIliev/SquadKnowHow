$(".form-register-design").submit(function(event) {
  event.preventDefault();

  var email = $("#email").val();

  $("#forgot-password-btn").html(
    'Продължи <i class="fas fa-circle-notch fa-spin"></i>'
  );

  $.ajax({
    url: "/api/forgot-password?email=" + email,
    method: "GET",
    success: function(result) {
      $("#forgot-password-btn").html("Продължи");

      if (result) {
        iziToast.success({
          title: "ОК!",
          message: "Имейл беше изпратен на посочения адрес",
          position: "topRight"
        });
      } else {
        iziToast.error({
          title: "Грешка!",
          message: "Възникна грешка",
          position: "topRight"
        });
      }
    }
  });
});
