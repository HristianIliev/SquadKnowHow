// var id = sessionStorage.getItem("userId");
var user = null;

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

// if (id === "null" || id === null) {
//   window.location.replace("/sign-in");
// } else {
// $.ajax({
//   url: "/api/user?id=" + id,
//   method: "GET",
//   success: function(result) {
//     user = result;
// $("#profile-image-navbar").attr(
//   "src",
//   "data:image/png;base64, " + user.image
// );

setUpNotifications();

// populateMessages(user);

$(".trigger-modal").click(function() {
  var instance = $(this);
  vex.dialog.open({
    message: "Напиши своето съобщение:",
    input: [
      '<input id="new-message-topic" name="topic" type="text" placeholder="Тема" required />',
      '<div class="form-group"><textarea id="new-message-content" name="content" class="form-control" placeholder="Съдържание" rows="10"></textarea></div>'
    ].join(""),
    buttons: [
      $.extend({}, vex.dialog.buttons.YES, { text: "Изпрати" }),
      $.extend({}, vex.dialog.buttons.NO, { text: "Затвори" })
    ],
    callback: function(data) {
      if (!data) {
        console.log("Cancelled");
      } else {
        instance.html(
          '<i class="fa fa-reply"></i> Отговори <i class="fas fa-circle-notch fa-spin"></i>'
        );
        console.log("Topic:", data.topic, "Content:", data.content);

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
          success: function(result) {
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

$(".delete-button-messages-sent").click(function() {
  var instance = $(this);
  $.confirm({
    title: "Потвърдете изтриването",
    content: "Сигурни ли сте, че искате да изтриете това съобщение",
    theme: "supervan",
    buttons: {
      Да: {
        btnClass: "btn-blue",
        action: function() {
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
            success: function(result) {
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
        action: function() {
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

$(".delete-button-messages").click(function() {
  var instance = $(this);
  $.confirm({
    title: "Потвърдете изтриването",
    content: "Сигурни ли сте, че искате да изтриете това съобщение",
    theme: "supervan",
    buttons: {
      Да: {
        btnClass: "btn-blue",
        action: function() {
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
            success: function(result) {
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
        action: function() {
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

$(".reject-button-messages").click(function() {
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
    url:
      "/api/sendRejectMessage?newMemberId=" +
      recipientId +
      "&projectName=" +
      projectName +
      "&messageId=" +
      messageId +
      "&creatorId=" +
      id,
    method: "GET",
    success: function(result) {
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

$(".approve-button-messages").click(function() {
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
    url:
      "/api/addProjectMember?projectName=" +
      projectName +
      "&newMemberId=" +
      recipientId +
      "&messageId=" +
      messageId,
    method: "GET",
    success: function(result) {
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

$(".preloader").fadeOut(500);
//   }
// });

function setUpNotifications() {
  // var notifications = result.notifications;
  // $(".counter").text(notifications.length);
  // if (notifications.length !== 0) {
  //   for (var i = notifications.length - 1; i >= 0; i -= 1) {
  //     $("#notifications-list").append(
  //       $("<li/>")
  //         .attr("id", "notification_" + notifications[i].id)
  //         .addClass("should-delete-notification")
  //         .append(
  //           $("<a/>")
  //             .attr("href", "#")
  //             .addClass("peers")
  //             .addClass("fxw-nw")
  //             .addClass("td-n")
  //             .addClass("p-20")
  //             .addClass("bdB")
  //             .addClass("c-grey-800")
  //             .addClass("cH-blue")
  //             .addClass("bgcH-grey-100")
  //             .append(
  //               $("<div/>")
  //                 .addClass("peer")
  //                 .addClass("mR-15")
  //                 .append(
  //                   $("<img/>")
  //                     .addClass("w-3r")
  //                     .addClass("bdrs-50p")
  //                     .attr(
  //                       "src",
  //                       "data:image/png;base64, " +
  //                         notifications[i].notificationSender.picture
  //                     )
  //                 )
  //             )
  //             .append(
  //               $("<div/>")
  //                 .addClass("peer")
  //                 .addClass("peer-greed")
  //                 .append(
  //                   $("<span/>")
  //                     .addClass("fw-500")
  //                     .text(notifications[i].notificationSender.name)
  //                 )
  //                 .append(
  //                   $("<span/>")
  //                     .addClass("c-grey-600")
  //                     .text(notifications[i].content)
  //                 )
  //                 .append(
  //                   $("<p/>")
  //                     .addClass("m-0")
  //                     .append(
  //                       $("<small/>")
  //                         .addClass("fsz-xs")
  //                         .text(notifications[i].timestamp)
  //                     )
  //                 )
  //             )
  //         )
  //     );
  //   }
  // } else {
  //   $("#notifications-list").append(
  //     $("<h4/>")
  //       .addClass("text-center")
  //       .addClass("no-notifications")
  //       .html('Няма нови известия <i class="far fa-frown"></i>')
  //   );
  // }

  $(".should-delete-notification").on("click", function() {
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var notificationId = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);

    $.ajax({
      url: "/api/deleteNotification?notificationId=" + notificationId,
      method: "DELETE",
      success: function(result) {
        if (result.successfull) {
          window.location.href = "/messages";
        }
      }
    });
  });

  $(".should-delete-notifications").on("click", function(event) {
    event.preventDefault();
    var lis = $("#notifications-list li");
    for (var i = 0; i < lis.length; i += 1) {
      var indexOfUndescore = $(lis[i])
        .attr("id")
        .indexOf("_");
      var notificationId = $(lis[i])
        .attr("id")
        .substring(indexOfUndescore + 1);

      if (i === lis.length - 1) {
        $.ajax({
          url: "/api/deleteNotification?notificationId=" + notificationId,
          method: "DELETE",
          success: function(result) {
            document.location.reload();
          }
        });
      } else {
        $.ajax({
          url: "/api/deleteNotification?notificationId=" + notificationId,
          method: "DELETE",
          success: function(result) {}
        });
      }
    }
  });
}

function populateMessages(user) {
  // Received
  // var messages = user.messages;
  // $("#pills-home-tab").html(
  //   '<i class="fa fa-envelope"></i> Получени съобщения (' +
  //     messages.length +
  //     ")"
  // );
  // var active = "";
  // var counterNumberOfMessage = 1;
  // if (messages.length !== 0) {
  //   for (
  //     var i = messages.length - 1;
  //     i >= 0;
  //     i -= 1, counterNumberOfMessage += 1
  //   ) {
  //     if (i === messages.length - 1) {
  //       active = "active";
  //     } else {
  //       active = "";
  //     }
  // $("#myList").append(
  //   $("<a/>'")
  //     .addClass("list-group-item")
  //     .addClass("list-group-item-action")
  //     .addClass(active)
  //     .attr("id", "message_" + messages[i].id)
  //     .attr("data-toggle", "list")
  //     .attr(
  //       "href",
  //       "#message-" + messages[i].id + "_" + messages[i].messageSender.id
  //     )
  //     .attr("role", "tab")
  //     .html(
  //       '<span class="badge badge-primary badge-pill">' +
  //         messages[i].timestamp +
  //         "</span>" +
  //         counterNumberOfMessage +
  //         ". От: " +
  //         messages[i].messageSender.name
  //     )
  // );
  //     if (messages[i].kind === "normal") {
  //       $("#received-messages-content").append(
  //         $("<div/>")
  //           .addClass("tab-pane")
  //           .addClass(active)
  //           .addClass("animated")
  //           .addClass("fadeInUp")
  //           .attr(
  //             "id",
  //             "message-" + messages[i].id + "_" + messages[i].messageSender.id
  //           )
  //           .attr("role", "tabpanel")
  //           .text("Тема: " + messages[i].topic)
  //           .append($("<hr/>").addClass("divider"))
  //           .append($("<div/>").text(messages[i].content))
  //           .append(
  //             $("<button/>")
  //               .addClass("delete-button-messages")
  //               .attr("type", "button")
  //               .addClass("button")
  //               .addClass("button-caution")
  //               .addClass("button-pill")
  //               .html('<i class="fa fa-minus-circle"></i> Изтрий')
  //           )
  //           .append(
  //             $("<button/>")
  //               .attr("type", "button")
  //               .addClass("trigger-modal")
  //               .addClass("button")
  //               .addClass("button-primary")
  //               .addClass("button-pill")
  //               .addClass("button-outline")
  //               .html('<i class="fa fa-reply"></i> Отговори')
  //           )
  //       );
  //     } else if (messages[i].kind === "requestToJoin") {
  //       $("#received-messages-content").append(
  //         $("<div/>")
  //           .addClass("tab-pane")
  //           .addClass(active)
  //           .addClass("animated")
  //           .addClass("fadeInUp")
  //           .attr(
  //             "id",
  //             "message-" + messages[i].id + "_" + messages[i].messageSender.id
  //           )
  //           .attr("role", "tabpanel")
  //           .text("Тема: " + messages[i].topic)
  //           .append($("<hr/>").addClass("divider"))
  //           .append($("<div/>").text(messages[i].content))
  //           .append(
  //             $("<button/>")
  //               .addClass("reject-button-messages")
  //               .attr("type", "button")
  //               .addClass("button")
  //               .addClass("button-caution")
  //               .addClass("button-pill")
  //               .html('<i class="fa fa-minus-circle"></i> Отхвърли')
  //           )
  //           .append(
  //             $("<button/>")
  //               .attr("type", "button")
  //               .addClass("button")
  //               .addClass("button-pill")
  //               .addClass("approve-button-messages")
  //               .html('<i class="fa fa-reply"></i> Одобри')
  //           )
  //       );
  //     } else if (messages[i].kind === "rejectionMessage") {
  //       $("#received-messages-content").append(
  //         $("<div/>")
  //           .addClass("tab-pane")
  //           .addClass(active)
  //           .addClass("animated")
  //           .addClass("fadeInUp")
  //           .attr(
  //             "id",
  //             "message-" + messages[i].id + "_" + messages[i].messageSender.id
  //           )
  //           .attr("role", "tabpanel")
  //           .text("Тема: " + messages[i].topic)
  //           .append($("<hr/>").addClass("divider"))
  //           .append($("<div/>").text(messages[i].c ontent))
  //           .append(
  //             $("<button/>")
  //               .addClass("delete-button-messages")
  //               .attr("type", "button")
  //               .addClass("button")
  //               .addClass("button-caution")
  //               .addClass("button-pill")
  //               .html('<i class="fa fa-minus-circle"></i> Изтрий')
  //           )
  //       );
  //     }
  //   }
  // } else {
  //   $("#pills-home").append($("<h3/>").text("Няма получени съобщения"));
  // }
  // Sent
  // var sentMessages = user.sentMessages;
  // $("#pills-profile-tab").html(
  //   '<i class="fa fa-envelope"></i> Изпратени съобщения (' +
  //     sentMessages.length +
  //     ")"
  // );
  // var activeSent = "";
  // counterNumberOfMessage = 1;
  // if (sentMessages.length !== 0) {
  //   for (
  //     var i = sentMessages.length - 1;
  //     i >= 0;
  //     i -= 1, counterNumberOfMessage += 1
  //   ) {
  //     if (i === sentMessages.length - 1) {
  //       activeSent = "active";
  //     } else {
  //       activeSent = "";
  //     }
  // $("#myList2").append(
  //   $("<a/>'")
  //     .addClass("list-group-item")
  //     .addClass("list-group-item-action")
  //     .addClass(activeSent)
  //     .attr("data-toggle", "list")
  //     .attr("href", "#message_" + sentMessages[i].id)
  //     .attr("role", "tab")
  //     .html(
  //       '<span class="badge badge-primary badge-pill">' +
  //         sentMessages[i].timestamp +
  //         "</span>" +
  //         counterNumberOfMessage +
  //         ". До: " +
  //         sentMessages[i].messageRecipient.name
  //     )
  // );
  //       $("#sent-messages-content").append(
  //         $("<div/>")
  //           .addClass("tab-pane")
  //           .addClass(activeSent)
  //           .addClass("animated")
  //           .addClass("fadeInUp")
  //           .attr("id", "message_" + sentMessages[i].id)
  //           .attr("role", "tabpanel")
  //           .text("Тема: " + sentMessages[i].topic)
  //           .append($("<hr/>").addClass("divider"))
  //           .append($("<div/>").text(sentMessages[i].content))
  //           .append(
  //             $("<button/>")
  //               .addClass("delete-button-messages-sent")
  //               .attr("type", "button")
  //               .addClass("button")
  //               .addClass("button-caution")
  //               .addClass("button-pill")
  //               .html('<i class="fa fa-minus-circle"></i> Изтрий')
  //           )
  //       );
  //     }
  //   } else {
  //     $("#pills-profile").append($("<h3/>").text("Няма изпратени съобщения"));
  //   }
  // }
}

$("#pills-home-tab").click(function(e) {
  $(this).tab("show");
});

$("#pills-profile-tab").click(function(e) {
  $(this).tab("show");
});
