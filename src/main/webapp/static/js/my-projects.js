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
//   }
// });

setUpNotifications();

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

getProjectsOfUser();

function getProjectsOfUser() {
  // var createdProjects = result.createdProjects;
  // $(".label h1").text("Създадени: " + createdProjects.length);
  // for (var i = createdProjects.length - 1; i >= 0; i -= 1) {
  //   $("#created-projects").append(
  //     $("<li/>")
  //       .addClass("project-card")
  //       .append(
  //         $("<div/>")
  //           .addClass("card")
  //           .append(
  //             $("<div/>")
  //               .addClass("card-body")
  //               .append(
  //                 $("<i/>")
  //                   .addClass("project-icon")
  //                   .addClass("far")
  //                   .addClass("fa-newspaper")
  //                   .addClass("fa-3x")
  //               )
  //               .append(
  //                 $("<h4/>")
  //                   .addClass("project-name")
  //                   .addClass("should-send-project-id")
  //                   .attr("id", "project-id_" + createdProjects[i].id)
  //                   .html("<b>" + createdProjects[i].name + "</b>")
  //               )
  //               .append(
  //                 $("<div/>")
  //                   .addClass("container-buttons")
  //                   .append(
  //                     $("<button/>")
  //                       .attr("type", "button")
  //                       .attr(
  //                         "id",
  //                         "project-id-edit_" + createdProjects[i].id
  //                       )
  //                       .addClass("project-edit-button")
  //                       .addClass("button")
  //                       .addClass("button-primary")
  //                       .addClass("button-pill")
  //                       .addClass("button-outline")
  //                       .html('<i class="fas fa-pencil-alt"></i> Промени')
  //                   )
  //                   .append(
  //                     $("<button/>")
  //                       .attr(
  //                         "id",
  //                         "project-id-delete_" + createdProjects[i].id
  //                       )
  //                       .attr("type", "button")
  //                       .addClass("button")
  //                       .addClass("button-caution")
  //                       .addClass("button-pill")
  //                       .addClass("project-delete-button")
  //                       .html(
  //                         '<i class="fas fa-minus-circle"></i> Изтрий&nbsp;&nbsp;&nbsp;'
  //                       )
  //                   )
  //               )
  //           )
  //       )
  //   );
  // }

  // var memberOfProjects = result.memberInProjects;
  // $("#label2").text("Участващ в: " + memberOfProjects.length);
  // for (var i = memberOfProjects.length - 1; i >= 0; i -= 1) {
  //   $("#member-of-projects").append(
  //     $("<li/>")
  //       .addClass("project-card")
  //       .append(
  //         $("<div/>")
  //           .addClass("card")
  //           .append(
  //             $("<div/>")
  //               .addClass("card-body")
  //               .append(
  //                 $("<i/>")
  //                   .addClass("project-icon")
  //                   .addClass("fas")
  //                   .addClass("fa-puzzle-piece")
  //                   .addClass("fa-3x")
  //               )
  //               .append(
  //                 $("<h4/>")
  //                   .addClass("project-name")
  //                   .addClass("should-send-project-id-member")
  //                   .attr("id", "project-id_" + memberOfProjects[i].id)
  //                   .html("<b>" + memberOfProjects[i].name + "</b>")
  //               )
  //               .append(
  //                 $("<div/>")
  //                   .addClass("container-buttons")
  //                   .append(
  //                     $("<button/>")
  //                       .attr("type", "button")
  //                       .attr(
  //                         "id",
  //                         "project-id-delete_" + memberOfProjects[i].id
  //                       )
  //                       .addClass("button")
  //                       .addClass("button-caution")
  //                       .addClass("button-pill")
  //                       .addClass("project-remove-member-button")
  //                       .html(
  //                         '<i class="fas fa-sign-out-alt"></i> Напусни&nbsp;&nbsp;&nbsp;'
  //                       )
  //                   )
  //               )
  //           )
  //       )
  //   );
  // }

  $(".project-edit-button").click(function() {
    var instance = $(this);
    var indexOfUnderscore = instance.attr("id").indexOf("_");
    var projectId = instance.attr("id").substring(indexOfUnderscore + 1);

    // sessionStorage.setItem("projectToEditId", projectId);
    window.location.href = "/edit-project/" + projectId;
  });

  $(".project-remove-member-button").click(function() {
    var instance = $(this);
    $.confirm({
      title: "Потвърдете напускането",
      content: "Сигурни ли сте, че искате да напуснете този проект",
      theme: "supervan",
      buttons: {
        Да: {
          btnClass: "btn-blue",
          action: function() {
            instance.html(
              '<i class="fas fa-sign-out-alt"></i> Напусни&nbsp;&nbsp;&nbsp; <i class="fas fa-circle-notch fa-spin"></i>'
            );
            var indexOfUnderscore = instance.attr("id").indexOf("_");
            var projectId = instance
              .attr("id")
              .substring(indexOfUnderscore + 1);

            $.ajax({
              url:
                "/api/removeProjectMember?projectId=" +
                projectId +
                "&memberId=" +
                id,
              method: "PUT",
              success: function(result) {
                instance.html(
                  '<i class="fas fa-sign-out-alt"></i> Напусни&nbsp;&nbsp;&nbsp;'
                );
                if (result.successfull) {
                  instance
                    .parent()
                    .parent()
                    .parent()
                    .parent()
                    .remove();
                  iziToast.success({
                    title: "ОК!",
                    message: "Напускането беше успешно",
                    position: "topRight"
                  });
                } else {
                  iziToast.error({
                    title: "Грешка",
                    message: "Напускането не беше успешно",
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
              message: "Този проект не беше напуснат",
              position: "topRight"
            });
          }
        }
      }
    });
  });

  $(".project-delete-button").click(function() {
    var instance = $(this);
    $.confirm({
      title: "Потвърдете изтриването",
      content: "Сигурни ли сте, че искате да изтриете този проект",
      theme: "supervan",
      buttons: {
        Да: {
          btnClass: "btn-blue",
          action: function() {
            instance.html(
              '<i class="fas fa-minus-circle"></i> Изтрий&nbsp;&nbsp;&nbsp; <i class="fas fa-circle-notch fa-spin"></i>'
            );
            var indexOfUnderscore = instance.attr("id").indexOf("_");
            var projectId = instance
              .attr("id")
              .substring(indexOfUnderscore + 1);

            $.ajax({
              url: "/api/deleteProject?projectId=" + projectId,
              method: "DELETE",
              success: function(result) {
                instance.html(
                  '<i class="fas fa-minus-circle"></i> Изтрий&nbsp;&nbsp;&nbsp; <i class="fas fa-circle-notch fa-spin"></i>'
                );
                if (result.successfull) {
                  instance
                    .parent()
                    .parent()
                    .parent()
                    .parent()
                    .remove();
                  iziToast.success({
                    title: "ОК!",
                    message: "Проектът беше изтрит успешно",
                    position: "topRight"
                  });
                } else {
                  iziToast.error({
                    title: "Грешка",
                    message: "Проектът не беше изтрит успешно",
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
              message: "Проектът не беше изтрит",
              position: "topRight"
            });
          }
        }
      }
    });
  });

  $(".should-send-project-id").on("click", function() {
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var projectIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);
    // sessionStorage.setItem("projectId", projectIdToSend);
    window.location.href = "/project-admin/" + projectIdToSend;
  });

  $(".should-send-project-id-member").on("click", function() {
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var projectIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);
    // sessionStorage.setItem("projectId", projectIdToSend);
    window.location.href = "/project-member/" + projectIdToSend;
  });

  $(".preloader").fadeOut(500);
}
// }
