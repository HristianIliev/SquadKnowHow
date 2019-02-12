var user = null;

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

var previousA = $(".my-active");

$(".trigger-modal").click(function () {
  var instance = $(this);
  vex.dialog.open({
    message: "Напиши своето съобщение:",
    input: [
      '<input id="new-message-topic" name="topic" type="text" placeholder="Тема" required />',
      '<div class="form-group"><textarea id="new-message-content" name="content" class="form-control" placeholder="Съдържание" rows="10"></textarea></div>'
    ].join(""),
    buttons: [
      $.extend({}, vex.dialog.buttons.YES, {
        text: "Изпрати"
      }),
      $.extend({}, vex.dialog.buttons.NO, {
        text: "Затвори"
      })
    ],
    callback: function (data) {
      if (!data) {
      } else {
        instance.html(
          '<i class="fa fa-reply"></i> Отговори <i class="fas fa-circle-notch fa-spin"></i>'
        );

        var dt = new Date();
        var timestamp =
          "" +
          dt.getDate() +
          "." +
          (dt.getMonth() + 1) +
          "." +
          dt.getFullYear() +
          " " +
          dt.getHours() +
          ":" +
          dt.getMinutes();

        var indexOfUndescore = $("#pills-home .row .list-group .active")
          .attr("href")
          .indexOf("_");
        var recipientId = $("#pills-home .row .list-group .active")
          .attr("href")
          .substring(indexOfUndescore + 1);

        $.ajax({
          url: "/api/sendMessage",
          method: "POST",
          data: JSON.stringify({
            topic: data.topic,
            content: data.content,
            timestamp: timestamp,
            senderId: id,
            recipientId: recipientId,
            kind: "normal"
          }),
          contentType: "application/json",
          success: function (result) {
            instance.html('<i class="fa fa-reply"></i> Отговори');
            if (result.successfull) {
              iziToast.success({
                title: "ОК!",
                message: "Съобщението беше изпратено успешно",
                position: "topRight"
              });
            } else {
              iziToast.warning({
                title: "Грешка",
                message: "Съобщението не беше изпратено успешно",
                position: "topRight"
              });
            }
          }
        });
      }
    }
  });
});

$(".delete-button-messages-sent").click(function () {
  var instance = $(this);
  $.confirm({
    title: "Потвърдете изтриването",
    content: "Сигурни ли сте, че искате да изтриете това съобщение",
    theme: "supervan",
    buttons: {
      Да: {
        btnClass: "btn-blue",
        action: function () {
          var indexOfUndescore = $("#pills-profile .row .list-group .active")
            .attr("href")
            .indexOf("_");
          var messageId = $("#pills-profile .row .list-group .active")
            .attr("href")
            .substring(indexOfUndescore + 1);

          instance.html(
            '<i class="fa fa-minus-circle"></i> Изтрий <i class="fas fa-circle-notch fa-spin"></i>'
          );

          $.ajax({
            url: "/api/deleteMessage?messageId=" + messageId,
            method: "DELETE",
            success: function (result) {
              instance.html('<i class="fa fa-minus-circle"></i> Изтрий');
              if (result.successfull) {
                $("#pills-profile .row .list-group .active").remove();
                $("#sent-messages-content .active").remove();
                iziToast.success({
                  title: "ОК!",
                  message: "Съобщението беше изтрито успешно",
                  position: "topRight"
                });
              } else {
                iziToast.error({
                  title: "Грешка",
                  message: "Съобщението не беше изтрито успешно",
                  position: "topRight"
                });
              }
            }
          });
        }
      },
      Не: {
        btnClass: "btn-blue",
        action: function () {
          iziToast.warning({
            title: "Отказ",
            message: "Съобщението не беше изтрито",
            position: "topRight"
          });
        }
      }
    }
  });
});

$(".delete-button-messages").click(function () {
  var instance = $(this);
  $.confirm({
    title: "Потвърдете изтриването",
    content: "Сигурни ли сте, че искате да изтриете това съобщение",
    theme: "supervan",
    buttons: {
      Да: {
        btnClass: "btn-blue",
        action: function () {
          instance.html(
            '<i class="fa fa-minus-circle"></i> Изтрий <i class="fas fa-circle-notch fa-spin"></i>'
          );
          var indexOfUndescore = $("#pills-home .row .list-group .active")
            .attr("id")
            .indexOf("_");
          var messageId = $("#pills-home .row .list-group .active")
            .attr("id")
            .substring(indexOfUndescore + 1);

          $.ajax({
            url: "/api/deleteMessage?messageId=" + messageId,
            method: "DELETE",
            success: function (result) {
              instance.html('<i class="fa fa-minus-circle"></i> Изтрий');
              if (result.successfull) {
                $("#pills-home .row .list-group .active").remove();
                $("#received-messages-content .active").remove();
                iziToast.success({
                  title: "ОК!",
                  message: "Съобщението беше изтрито успешно",
                  position: "topRight"
                });
              } else {
                iziToast.error({
                  title: "Грешка",
                  message: "Съобщението не беше изтрито успешно",
                  position: "topRight"
                });
              }
            }
          });
        }
      },
      Не: {
        btnClass: "btn-blue",
        action: function () {
          iziToast.warning({
            title: "Отказ",
            message: "Съобщението не беше изтрито",
            position: "topRight"
          });
        }
      }
    }
  });
});

$(".reject-button-messages").click(function () {
  var instance = $(this);
  instance.html(
    '<i class="fa fa-minus-circle"></i> Отхвърли <i class="fas fa-circle-notch fa-spin"></i>'
  );
  var indexOfUndescore = $("#received-messages-content .active")
    .attr("id")
    .indexOf("_");
  var recipientId = $("#received-messages-content .active")
    .attr("id")
    .substring(indexOfUndescore + 1);

  var indexOfUndescore2 = $("#pills-home .row .list-group .active")
    .attr("id")
    .indexOf("_");
  var messageId = $("#pills-home .row .list-group .active")
    .attr("id")
    .substring(indexOfUndescore2 + 1);

  var first = $("#received-messages-content .active")
    .text()
    .indexOf('"');
  var second = $("#received-messages-content .active")
    .text()
    .lastIndexOf('"');
  var projectName = $("#received-messages-content .active")
    .text()
    .substring(first + 1, second);

  $.ajax({
    url: "/api/sendRejectMessage?newMemberId=" +
      recipientId +
      "&projectName=" +
      projectName +
      "&messageId=" +
      messageId +
      "&creatorId=" +
      id +
      "&isInvite=" +
      false,
    method: "GET",
    success: function (result) {
      instance.html('<i class="fa fa-minus-circle"></i> Отхвърли');
      if (result.successfull) {
        iziToast.success({
          title: "ОК!",
          message: "Заявката беше отхвърлена успешно",
          position: "topRight"
        });
      } else {
        iziToast.error({
          title: "Грешка",
          message: "Заявката беше отхвърлена неуспешно",
          position: "topRight"
        });
      }
    }
  });
});

$(".reject-invite-button").click(function () {
  var instance = $(this);
  instance.html(
    '<i class="fa fa-minus-circle"></i> Отхвърли <i class="fas fa-circle-notch fa-spin"></i>'
  );
  var indexOfUndescore = $("#received-messages-content .active")
    .attr("id")
    .indexOf("_");
  var recipientId = $("#received-messages-content .active")
    .attr("id")
    .substring(indexOfUndescore + 1);

  var indexOfUndescore2 = $("#pills-home .row .list-group .active")
    .attr("id")
    .indexOf("_");
  var messageId = $("#pills-home .row .list-group .active")
    .attr("id")
    .substring(indexOfUndescore2 + 1);

  var first = $("#received-messages-content .active")
    .text()
    .indexOf('"');
  var second = $("#received-messages-content .active")
    .text()
    .lastIndexOf('"');
  var projectName = $("#received-messages-content .active")
    .text()
    .substring(first + 1, second);

  $.ajax({
    url: "/api/sendRejectMessage?newMemberId=" +
      recipientId +
      "&projectName=" +
      projectName +
      "&messageId=" +
      messageId +
      "&creatorId=" +
      id +
      "&isInvite=" +
      true,
    method: "GET",
    success: function (result) {
      instance.html('<i class="fa fa-minus-circle"></i> Отхвърли');
      if (result.successfull) {
        iziToast.success({
          title: "ОК!",
          message: "Заявката беше отхвърлена успешно",
          position: "topRight"
        });
      } else {
        iziToast.error({
          title: "Грешка",
          message: "Заявката беше отхвърлена неуспешно",
          position: "topRight"
        });
      }
    }
  });
});

$(".approve-button-messages").click(function () {
  var instance = $(this);
  instance.html(
    '<i class="fa fa-reply"></i> Одобри <i class="fas fa-circle-notch fa-spin"></i>'
  );
  var first = $("#received-messages-content .active")
    .text()
    .indexOf('"');
  var second = $("#received-messages-content .active")
    .text()
    .lastIndexOf('"');
  var projectName = $("#received-messages-content .active")
    .text()
    .substring(first + 1, second);

  var indexOfUndescore = $("#received-messages-content .active")
    .attr("id")
    .indexOf("_");
  var recipientId = $("#received-messages-content .active")
    .attr("id")
    .substring(indexOfUndescore + 1);

  var indexOfUndescore2 = $("#pills-home .row .list-group .active")
    .attr("id")
    .indexOf("_");
  var messageId = $("#pills-home .row .list-group .active")
    .attr("id")
    .substring(indexOfUndescore2 + 1);

  $.ajax({
    url: "/api/addProjectMember?projectName=" +
      projectName +
      "&newMemberId=" +
      recipientId +
      "&messageId=" +
      messageId +
      "&isInvite=" +
      false,
    method: "GET",
    success: function (result) {
      instance.html('<i class="fa fa-reply"></i> Одобри');
      if (result.successfull) {
        iziToast.success({
          title: "ОК!",
          message: "Заявката беше одобрена успешно",
          position: "topRight"
        });
      } else {
        iziToast.error({
          title: "Грешка",
          message: "Заявката беше одобрена неуспешно",
          position: "topRight"
        });
      }
    }
  });
});

$(".accept-invite-button").click(function () {
  var instance = $(this);
  instance.html(
    '<i class="fa fa-reply"></i> Приеми <i class="fas fa-circle-notch fa-spin"></i>'
  );
  var first = $("#received-messages-content .active")
    .text()
    .indexOf('"');
  var second = $("#received-messages-content .active")
    .text()
    .lastIndexOf('"');
  var projectName = $("#received-messages-content .active")
    .text()
    .substring(first + 1, second);

  var indexOfUndescore = $("#received-messages-content .active")
    .attr("id")
    .indexOf("_");
  var recipientId = $("#received-messages-content .active")
    .attr("id")
    .substring(indexOfUndescore + 1);

  var indexOfUndescore2 = $("#pills-home .row .list-group .active")
    .attr("id")
    .indexOf("_");
  var messageId = $("#pills-home .row .list-group .active")
    .attr("id")
    .substring(indexOfUndescore2 + 1);

  $.ajax({
    url: "/api/addProjectMember?projectName=" +
      projectName +
      "&newMemberId=" +
      id +
      "&messageId=" +
      messageId +
      "&isInvite=" +
      true,
    method: "GET",
    success: function (result) {
      instance.html('<i class="fa fa-reply"></i> Приеми');
      if (result.successfull) {
        iziToast.success({
          title: "ОК!",
          message: "Заявката беше одобрена успешно",
          position: "topRight"
        });
      } else {
        iziToast.error({
          title: "Грешка",
          message: "Заявката беше одобрена неуспешно",
          position: "topRight"
        });
      }
    }
  });
});

$(".nav-item").click(function () {
  previousA.removeClass("my-active");
  previousA = $(this);
  $(this).addClass("my-active");
});

$(".preloader").fadeOut(500);

$("#pills-home-tab").click(function (e) {
  $(this).tab("show");
});

$("#pills-profile-tab").click(function (e) {
  $(this).tab("show");
});