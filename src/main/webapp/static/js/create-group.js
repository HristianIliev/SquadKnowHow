// var id = sessionStorage.getItem("userId");

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

var user = null;

// if (id === "null" || id === null) {
//   window.location.replace("/sign-in");
// } else {
execute();
// }

function execute() {
  // $.ajax({
  //   url: "/api/user?id=" + id,
  //   method: "GET",
  //   success: function(result) {
  //     user = result;
  //     $("#profile-image-navbar").attr(
  //       "src",
  //       "data:image/png;base64, " + user.image
  //     );
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

  var uploader = $("#fine-uploader").fineUploader({
    debug: true,
    request: {
      endpoint: "/api/upload"
    },
    deleteFile: {
      enabled: true,
      endpoint: "/api/deleteProfilePicture"
    },
    retry: {
      enableAuto: true
    },
    multiple: true,
    onLeave:
      "Вашата снимка се качва вмомента. Сигурни ли сте че искате да напуснете страницата.",
    autoUpload: false,
    thumbnails: {
      placeholders: {
        waitingPath: "static/css/waiting-generic.png",
        notAvailablePath: "static/css/not_available-generic.png"
      }
    },
    validation: {
      sizeLimit: 20485760,
      itemLimit: 1,
      allowedExtensions: ["jpeg", "jpg", "gif", "png"]
    },
    maxConnections: 1,
    callbacks: {
      onAllComplete: function(succeeded, failed) {
        if (succeeded) {
          window.location.href = "/groups-of-people";
        }
      },
      onUploadChunk: function(id, name, chunkData) {
        alert(id + "is going to be uploaded");
      },
      onUploadChunkSuccess: function(id, chunkData, responseJSON, xhr) {
        alert(id + "has been successfully uploaded");
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
              var remainingAllowed = uploader.fineUploader(
                "getRemainingAllowedItems"
              );
              if (remainingAllowed === 1) {
                iziToast.error({
                  title: "Грешка",
                  message: "Трябва да има минимум една снимка качена",
                  position: "topRight"
                });
              } else {
                var name = $("#groupName").val();
                var description = $("#textareaBasic").val();
                var groupType = $("#groupType").val();

                $.ajax({
                  url: "/api/createGroup?creatorId=" + id,
                  method: "POST",
                  data: JSON.stringify({
                    name: name,
                    description: description,
                    groupType: groupType
                  }),
                  contentType: "application/json",
                  success: function(result) {
                    uploader.fineUploader(
                      "setEndpoint",
                      "/api/uploadLogoPicture?id=" + result.id
                    );

                    $("#fine-uploader").fineUploader("uploadStoredFiles");
                  }
                });
              }
            })
        ]
      }
    });

    $("#smartwizard").on("leaveStep", function(
      e,
      anchorObject,
      stepNumber,
      stepDirection
    ) {
      var nameIcon = $("#groupNameIcon");
      var name = $("#groupName");
      var nameLabel = $("#groupNameLabel");

      var description = $("#textareaBasic");
      var descriptionLabel = $("#textareaBasicLabel");

      var groupTypeIcon = $("#groupTypeIcon");
      var groupType = $("#groupType");
      var groupTypeLabel = $("#groupTypeLabel");

      if (stepNumber === 0) {
        if (name.val().length === 0) {
          name.webuiPopover("destroy");
          name.webuiPopover({
            placement: "right",
            trigger: "manual",
            content: "Това поле не може да бъде празно",
            style: "popover",
            closeable: true,
            animation: "pop",
            width: "auto", //can be set with  number
            height: "auto"
          });
          name.webuiPopover("show");

          nameIcon.attr("style", "color: rgb(230, 92, 92);");
          name.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
          nameLabel.attr("style", "color: rgb(230, 92, 92);");

          nameIcon.addClass("validated-icon");
          name.addClass("validated-form");
          nameLabel.addClass("validated-label");

          $(".validated-form").click(function() {
            $(this).attr("style", " ");
            $(".validated-label").attr("style", " ");
            $(".validated-icon").attr("style", " ");
          });

          return false;
        } else if (name.val().length < 5) {
          name.webuiPopover("destroy");
          name.webuiPopover({
            placement: "right",
            trigger: "manual",
            content: "Това поле не може да бъде по-малко от 5 символа",
            style: "popover",
            closeable: true,
            animation: "pop",
            width: "auto", //can be set with  number
            height: "auto"
          });
          name.webuiPopover("show");

          nameIcon.attr("style", "color: rgb(230, 92, 92);");
          name.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
          nameLabel.attr("style", "color: rgb(230, 92, 92);");

          nameIcon.addClass("validated-icon");
          name.addClass("validated-form");
          nameLabel.addClass("validated-label");

          $(".validated-form").click(function() {
            $(this).attr("style", " ");
            $(".validated-label").attr("style", " ");
            $(".validated-icon").attr("style", " ");
          });

          return false;
        } else {
          var exists = false;

          $.ajax({
            url: "/api/checkGroupName?name=" + name.val(),
            method: "GET",
            async: false,
            success: function(result) {
              if (result.status === 200) {
                name.webuiPopover("destroy");
                exists = false;
              } else {
                exists = true;
              }
            }
          });

          if (exists === true) {
            name.webuiPopover("destroy");
            name.webuiPopover({
              placement: "right",
              trigger: "manual",
              content: "Това име вече съществува",
              style: "popover",
              closeable: true,
              animation: "pop",
              width: "auto", //can be set with  number
              height: "auto"
            });
            name.webuiPopover("show");

            nameIcon.attr("style", "color: rgb(230, 92, 92);");
            name.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
            nameLabel.attr("style", "color: rgb(230, 92, 92);");

            nameIcon.addClass("validated-icon");
            name.addClass("validated-form");
            nameLabel.addClass("validated-label");

            $(".validated-form").click(function() {
              $(this).attr("style", " ");
              $(".validated-label").attr("style", " ");
              $(".validated-icon").attr("style", " ");
            });

            return false;
          }

          name.webuiPopover("destroy");
          return true;
        }
      } else if (stepNumber === 1) {
        if (description.val().length < 150) {
          description.webuiPopover("destroy");
          description.webuiPopover({
            placement: "right",
            trigger: "manual",
            content: "Това поле не може да бъде по-мало от 150 символа",
            style: "popover",
            closeable: true,
            animation: "pop",
            width: "auto", //can be set with  number
            height: "auto"
          });
          description.webuiPopover("show");

          description.attr(
            "style",
            "border-bottom: 1px solid rgb(230, 92, 92)"
          );
          descriptionLabel.attr("style", "color: rgb(230, 92, 92);");

          description.addClass("validated-form");
          descriptionLabel.addClass("validated-label");

          $(".validated-form").click(function() {
            $(this).attr("style", " ");
            $(".validated-label").attr("style", " ");
          });

          return false;
        } else if (description.val().length === 0) {
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

          description.attr(
            "style",
            "border-bottom: 1px solid rgb(230, 92, 92)"
          );
          descriptionLabel.attr("style", "color: rgb(230, 92, 92);");

          description.addClass("validated-form");
          descriptionLabel.addClass("validated-label");

          $(".validated-form").click(function() {
            $(this).attr("style", " ");
            $(".validated-label").attr("style", " ");
          });

          return false;
        } else if (groupType.val().length === 0) {
          groupType.webuiPopover("destroy");
          groupType.webuiPopover({
            placement: "right",
            trigger: "manual",
            content: "Това поле не може да бъде празно",
            style: "popover",
            closeable: true,
            animation: "pop",
            width: "auto", //can be set with  number
            height: "auto"
          });
          groupType.webuiPopover("show");

          groupTypeIcon.attr("style", "color: rgb(230, 92, 92);");
          groupType.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
          groupTypeLabel.attr("style", "color: rgb(230, 92, 92);");

          groupTypeIcon.addClass("validated-icon");
          groupType.addClass("validated-form");
          groupTypeLabel.addClass("validated-label");

          $(".validated-form").click(function() {
            $(this).attr("style", " ");
            $(".validated-label").attr("style", " ");
            $(".validated-icon").attr("style", " ");
          });

          return false;
        } else {
          description.webuiPopover("destroy");
          groupType.webuiPopover("destroy");
          return true;
        }
      } else {
        return true;
      }
    });
  });

  $(".preloader").fadeOut(500);
}
