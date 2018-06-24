// var id = sessionStorage.getItem("userId");
var user = null;
var changed = false;

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

setUpWizard(result);

//   }
// });

setUpNotifications();

$(".preloader").fadeOut(500);

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

function setUpWizard(result) {
  var uploader = $("#fine-uploader").fineUploader({
    debug: true,
    request: {
      endpoint: "/api/uploadProfilePicture"
    },
    deleteFile: {
      enabled: true,
      endpoint: "/api/deleteProfilePicture"
    },
    retry: {
      enableAuto: true
    },
    multiple: false,
    onLeave:
      "Вашата снимка се качва вмомента. Сигурни ли сте че искате да напуснете страницата.",
    autoUpload: false,
    thumbnails: {
      placeholders: {
        waitingPath: "/source/placeholders/waiting-generic.png",
        notAvailablePath: "/source/placeholders/not_available-generic.png"
      }
    },
    validation: {
      sizeLimit: 20485760,
      itemLimit: 1,
      allowedExtensions: ["jpeg", "jpg", "gif", "png"]
    },
    callbacks: {
      onAllComplete: function(succeeded, failed) {
        window.location.href = "/profile";
      }
    }
  });

  $(document).ready(function() {
    $("#smartwizard").smartWizard({
      selected: 0,
      cycleSteps: false,
      backButtonSupport: true,
      transitionEffect: "slide",
      keyNavigation: false,
      toolbarSettings: {
        toolbarPosition: "bottom", // none, top, bottom, both
        toolbarButtonPosition: "right", // left, right
        showNextButton: true, // show/hide a Next button
        showPreviousButton: true, // show/hide a Previous button
        toolbarExtraButtons: [
          $("<button></button>")
            .text("Създай")
            .addClass("btn btn-info")
            .on("click", function() {
              var firstName = $("#profileFirstName").val();
              var lastName = $("#profileLastName").val();
              var email = $("#profileEmail").val();
              var description = $("#profileDescription").val();
              var password = $("#profilePassword").val();

              if (firstName.length === 0) {
                iziToast.error({
                  title: "Грешка",
                  message: "Първото име не може да е празно",
                  position: "topRight"
                });
                return false;
              }

              if (firstName.length < 2 || firstName.length > 16) {
                iziToast.error({
                  title: "Грешка",
                  message: "Първото име трябва да е между 2 и 16 символа",
                  position: "topRight"
                });
                return false;
              }

              if (lastName.length === 0) {
                iziToast.error({
                  title: "Грешка",
                  message: "Фамилията не може да е празно",
                  position: "topRight"
                });
                return false;
              }

              if (lastName.length < 2 || lastName.length > 16) {
                iziToast.error({
                  title: "Грешка",
                  message: "Фамилията трябва да е между 2 и 16 символа",
                  position: "topRight"
                });
                return false;
              }

              if (email.length === 0) {
                iziToast.error({
                  title: "Грешка",
                  message: "Емайлът не може да бъде празен",
                  position: "topRight"
                });
                return false;
              }

              if (description.length < 25) {
                iziToast.error({
                  title: "Грешка",
                  message: "Описанието трябва да е минимум 25 символа",
                  position: "topRight"
                });
                return false;
              }

              if (password.length === 0) {
                iziToast.error({
                  title: "Грешка",
                  message: "Паролата не може дае празна",
                  position: "topRight"
                });
                return false;
              }

              if (password.length < 8 || password.length > 20) {
                iziToast.error({
                  title: "Грешка",
                  message: "Паролата трябва да е между 8 и 20 символа",
                  position: "topRight"
                });
                return false;
              }

              if (password !== $("#profileConfirmPassword").val()) {
                iziToast.error({
                  title: "Грешка",
                  message: "Паролите не съвпадат",
                  position: "topRight"
                });
                return false;
              }

              // to check if email is free
              if (changed) {
                $.ajax({
                  url: "/api/checkIfEmailIsTaken?email=" + email,
                  method: "POST",
                  contentType: "application/json",
                  success: function(result) {
                    if (result === false) {
                      iziToast.error({
                        title: "Грешка",
                        message: "Емайлът вече е зает",
                        position: "topRight"
                      });
                    } else {
                      window.location.href = "/profile";
                    }
                  }
                });
              }

              $.ajax({
                url: "/api/editProfile",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                  id: id,
                  firstName: firstName,
                  lastName: lastName,
                  description: description,
                  email: email,
                  password: password
                }),
                success: function(result) {
                  if (result) {
                    uploader.fineUploader(
                      "setEndpoint",
                      "/api/uploadProfilePicture?id=" + id
                    );

                    if ($(".qq-file-id-0").length !== 0) {
                      $("#fine-uploader").fineUploader("uploadStoredFiles");
                    } else {
                      window.location.href = "/profile";
                    }
                  }
                }
              });
            })
        ]
      }
    });

    $("#smartwizard").on("leaveStep", function(
      e,
      anchorObject,
      stepNumber,
      stepDirection
    ) {});

    // $("#profileFirstName").val(result.firstName);
    // $("#profileLastName").val(result.lastName);
    // $("#profileDescription").val(result.description);
    // $("#profileEmail").val(result.email);
    $("#profileEmail").change(function() {
      changed = true;
    });
    // $("#profilePassword").val(result.password);
    // $("#profileConfirmPassword").val(result.password);
  });
}
// }
