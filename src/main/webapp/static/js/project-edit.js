// var id = sessionStorage.getItem("userId");
var user = null;
// var projectId = sessionStorage.getItem("projectToEditId");
var changed = false;

var arr = window.location.pathname.split("/");
var projectId = arr[arr.length - 1];

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

// if (id === "null" || projectId === "null" || id === null) {
//   window.location.replace("/sign-in");
// } else {
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

setUpWizard();

function setUpNotifications() {
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

function setUpWizard() {
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
        waitingPath: "css/waiting-generic.png",
        notAvailablePath: "css/not_available-generic.png"
      }
    },
    validation: {
      sizeLimit: 2048576,
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
      onUploadChunk: function(id, name, chunkData) {},
      onUploadChunkSuccess: function(id, chunkData, responseJSON, xhr) {}
    }
  });

  // $.ajax({
  //   url: "/api/project?id=" + projectId,
  //   method: "GET",
  //   success: function(result) {
  // $("#projectName").val(result.name);

  // $("#textareaBasic").val(result.description);
  // $("#projectGoals").val(result.goal1);
  // $("#projectGoals2").val(result.goal2);
  // $("#projectGoals3").val(result.goal3);
  // $("#github-page").val(result.githubPage);
  // $("#video-of-project").val(result.youtubeLink);
  //   }
  // });
  $("#projectName").change(function() {
    changed = true;
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
              var name = $("#projectName").val();
              var description = $("#textareaBasic").val();
              var goal1 = $("#projectGoals").val();
              var goal2 = $("#projectGoals2").val();
              var goal3 = $("#projectGoals3").val();
              var githubPage = $("#github-page").val();
              var youtubeLink = $("#video-of-project").val();

              if (name.length === 0) {
                iziToast.error({
                  title: "Грешка",
                  message: "Името на проекта не може да е празно",
                  position: "topRight"
                });
                return false;
              }

              if (name.length < 5) {
                iziToast.error({
                  title: "Грешка",
                  message:
                    "Името на проекта не може да е по-малко от 5 символа",
                  position: "topRight"
                });
                return false;
              }

              if (changed) {
                $.ajax({
                  url: "/api/checkProjectName?name=" + name,
                  method: "GET",
                  async: false,
                  success: function(result) {
                    if (result.status === 200) {
                    } else {
                      iziToast.error({
                        title: "Грешка",
                        message: "Това име на проект вече е заето",
                        position: "topRight"
                      });
                    }
                  }
                });
              }

              if (description.length < 150) {
                iziToast.error({
                  title: "Грешка",
                  message:
                    "Описанието на проекта трябва да е минимум 150 символа.",
                  position: "topRight"
                });

                return false;
              }

              if (description.length === 0) {
                iziToast.error({
                  title: "Грешка",
                  message: "Описанието на проекта не мое да бъде празно.",
                  position: "topRight"
                });

                return false;
              }

              if (goal1.length === 0) {
                iziToast.error({
                  title: "Грешка",
                  message: "Проектът трябва да има поне една крайна цел.",
                  position: "topRight"
                });

                return false;
              }

              $.ajax({
                url: "/api/editProject",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                  id: projectId,
                  name: name,
                  description: description,
                  goal1: goal1,
                  goal2: goal2,
                  goal3: goal3,
                  githubPage: githubPage,
                  youtubeLink: youtubeLink
                }),
                success: function(result) {
                  if (result) {
                    window.location.href = "/my-projects";
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
  });

  $(".preloader").fadeOut(500);
}
// }
