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
var elems = Array.prototype.slice.call(document.querySelectorAll(".js-switch"));

elems.forEach(function(html) {
  var switchery = new Switchery(html, {
    color: "#138fc2",
    secondaryColor: "#cccccc",
    jackColor: "#c7e1f1",
    size: "small"
  });
});

$("#location").autocomplete({
  source: "/api/getCities",
  minLength: 2,
  select: function(event, ui) {
    console.log("Selected: " + ui.item.value + " aka " + ui.item.id);
  },
  autoFocus: true
});

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
    sizeLimit: 10048576,
    itemLimit: 10,
    allowedExtensions: ["jpeg", "jpg", "gif", "png"]
  },
  maxConnections: 1,
  callbacks: {
    onAllComplete: function(succeeded, failed) {
      if (succeeded) {
        window.location.href = "/projects-of-people";
      }
    },
    onUploadChunk: function(id, name, chunkData) {},
    onUploadChunkSuccess: function(id, chunkData, responseJSON, xhr) {}
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
            if (remainingAllowed === 10 || remainingAllowed === 9) {
              iziToast.error({
                title: "Грешка",
                message: "Трябва да има минимум две снимки качени",
                position: "topRight"
              });
            } else {
              var telephone = $("#telephone").val();
              var countryCode = $("#countryCode").val();

              sendVerificationSMS(telephone, countryCode);

              showCodeVerificationModal(telephone, countryCode);
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
    var nameIcon = $("#projectNameIcon");
    var name = $("#projectName");
    var nameLabel = $("#projectNameLabel");

    var locationIcon = $("#locationIcon");
    var location = $("#location");
    var locationLabel = $("#locationLabel");

    var description = $("#textareaBasic");
    var descriptionLabel = $("#textareaBasicLabel");

    var goalsIcon = $("#projectGoalsIcon");
    var goals = $("#projectGoals");
    var goalsLabel = $("#projectGoalsLabel");

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
      } else if (location.val() === "Не съществува такъв резултат") {
        location.webuiPopover("destroy");
        location.webuiPopover({
          placement: "right",
          trigger: "manual",
          content: "Не съществува такъв резултат",
          style: "popover",
          closeable: true,
          animation: "pop",
          width: "auto", //can be set with  number
          height: "auto"
        });
        location.webuiPopover("show");

        locationIcon.attr("style", "color: rgb(230, 92, 92);");
        location.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
        locationLabel.attr("style", "color: rgb(230, 92, 92);");

        locationIcon.addClass("validated-icon");
        location.addClass("validated-form");
        locationLabel.addClass("validated-label");

        $(".validated-form").click(function() {
          $(this).attr("style", " ");
          $(".validated-label").attr("style", " ");
          $(".validated-icon").attr("style", " ");
        });

        return false;
      } else {
        var exists = false;

        $.ajax({
          url: "/api/checkProjectName?name=" + name.val(),
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
        location.webuiPopover("destroy");
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

        description.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
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

        description.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
        descriptionLabel.attr("style", "color: rgb(230, 92, 92);");

        description.addClass("validated-form");
        descriptionLabel.addClass("validated-label");

        $(".validated-form").click(function() {
          $(this).attr("style", " ");
          $(".validated-label").attr("style", " ");
        });

        return false;
      } else if (goals.val().length === 0) {
        goals.webuiPopover("destroy");
        goals.webuiPopover({
          placement: "right",
          trigger: "manual",
          content: "Това поле не може да бъде празно",
          style: "popover",
          closeable: true,
          animation: "pop",
          width: "auto", //can be set with  number
          height: "auto"
        });
        goals.webuiPopover("show");

        goalsIcon.attr("style", "color: rgb(230, 92, 92);");
        goals.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
        goalsLabel.attr("style", "color: rgb(230, 92, 92);");

        goalsIcon.addClass("validated-icon");
        goals.addClass("validated-form");
        goalsLabel.addClass("validated-label");

        $(".validated-form").click(function() {
          $(this).attr("style", " ");
          $(".validated-label").attr("style", " ");
          $(".validated-icon").attr("style", " ");
        });

        return false;
      } else {
        description.webuiPopover("destroy");
        return true;
      }
    } else {
      return true;
    }
  });
});

function sendVerificationSMS(telephone, countryCode) {
  $.ajax({
    url: "/api/sendSMS",
    method: "POST",
    data: JSON.stringify({
      phoneNumber: telephone,
      countryCode: countryCode,
      via: "sms"
    }),
    contentType: "application/json",
    success: function(result) {
    }
  });
}

function showCodeVerificationModal(telephone, countryCode) {
  alert(telephone, countryCode);
  vex.dialog.open({
    message:
      "За да завършите успешно създаването на проекта си трябва първо да потвърдите телефонния си номер. Ние ви изпратихме SMS, въведете кода за достъп:",
    input: [
      '<div class="input-group"><span class="input-group-addon"><input type="text" name="code" class="form-control" placeholder="Код"></span"</div>'
    ].join(""),
    callback: function(data) {
      if (!data) {
        return console.log("Cancelled");
      }
      console.log(data.code);

      $.ajax({
        url: "/api/checkVerificationCode",
        method: "POST",
        data: JSON.stringify({
          phoneNumber: telephone,
          countryCode: countryCode,
          token: data.code
        }),
        contentType: "application/json",
        success: function(result) {
          if (result.ok) {
            var name = $("#projectName").val();
            var description = $("#textareaBasic").val();
            var goal1 = $("#projectGoals").val();
            var goal2 = $("#projectGoals2").val();
            var goal3 = $("#projectGoals3").val();
            var githubPage = $("#github-page").val();
            var city = $("#location").val();
            var videoLink = $("#video-of-project").val();
            var needsProgrammer = document.querySelector("#needsProgrammer")
              .checked;
            var needsDesigner = document.querySelector("#needsDesigner")
              .checked;
            var needsEngineer = document.querySelector("#needsEngineer")
              .checked;
            var needsWriter = document.querySelector("#needsWriter").checked;
            var needsScientist = document.querySelector("#needsScientist")
              .checked;
            var needsMusician = document.querySelector("#needsMusician")
              .checked;
            var needsFilmmaker = document.querySelector("#needsFilmmaker")
              .checked;
            var needsProductManager = document.querySelector(
              "#needsProductManager"
            ).checked;
            var needsArtist = document.querySelector("#needsArtist").checked;

            var programmer = "";
            var designer = "";
            var engineer = "";
            var writer = "";
            var scientist = "";
            var musician = "";
            var filmmaker = "";
            var productManager = "";
            var artist = "";

            if (needsProgrammer) {
              programmer = "Програмист";
            }

            if (needsDesigner) {
              designer = "Дизайнер";
            }

            if (needsEngineer) {
              engineer = "Инженер";
            }

            if (needsWriter) {
              writer = "Писател";
            }

            if (needsScientist) {
              scientist = "Учен";
            }

            if (needsMusician) {
              musician = "Музикант";
            }

            if (needsFilmmaker) {
              filmmaker = "Режисьор";
            }

            if (needsProductManager) {
              productManager = "Продуктов мениджър";
            }

            if (needsArtist) {
              artist = "Художник";
            }

            $.ajax({
              url: "/api/createProject?creatorId=" + id,
              method: "POST",
              data: JSON.stringify({
                name: name,
                description: description,
                goal1: goal1,
                goal2: goal2,
                goal3: goal3,
                githubPage: githubPage,
                city: {
                  name: city
                },
                youtubeLink: videoLink,
                projectNeeds: [
                  {
                    name: programmer
                  },
                  {
                    name: designer
                  },
                  {
                    name: engineer
                  },
                  {
                    name: writer
                  },
                  {
                    name: scientist
                  },
                  {
                    name: musician
                  },
                  {
                    name: filmmaker
                  },
                  {
                    name: productManager
                  },
                  {
                    name: artist
                  }
                ],
                telephone: telephone
              }),
              contentType: "application/json",
              success: function(result) {
                uploader.fineUploader(
                  "setEndpoint",
                  "/api/uploadProjectPicture?id=" + result.id
                );
                uploader.fineUploader("setName", 0, "0");

                if ($(".qq-file-id-1").length !== 0) {
                  uploader.fineUploader("setName", 1, "1");
                }

                if ($(".qq-file-id-2").length !== 0) {
                  uploader.fineUploader("setName", 2, "2");
                }

                if ($(".qq-file-id-3").length !== 0) {
                  uploader.fineUploader("setName", 3, "3");
                }

                if ($(".qq-file-id-4").length !== 0) {
                  uploader.fineUploader("setName", 4, "4");
                }

                if ($(".qq-file-id-5").length !== 0) {
                  uploader.fineUploader("setName", 5, "5");
                }

                if ($(".qq-file-id-6").length !== 0) {
                  uploader.fineUploader("setName", 6, "6");
                }

                if ($(".qq-file-id-7").length !== 0) {
                  uploader.fineUploader("setName", 7, "7");
                }

                if ($(".qq-file-id-8").length !== 0) {
                  uploader.fineUploader("setName", 8, "8");
                }

                if ($(".qq-file-id-9").length !== 0) {
                  uploader.fineUploader("setName", 9, "9");
                }

                $("#fine-uploader").fineUploader("uploadStoredFiles");
                // var coverBase64 = $(".qq-file-id-0 .qq-thumbnail-wrapper img").attr("src");
                // alert(coverBase64);
                // $.ajax({
                //   url: "/api/uploadProjectPicture?id=" + result.id,
                //   method: "POST",
                //   data: JSON.stringify({
                //     base64: coverBase64
                //   }),
                //   contentType: "application/json",
                //   success: function(result) {
                //     alert("cover picture upload complete")
                //   }
                // });
              }
            });
          } else {
            alert("Вашият код беше грешен :(");
          }
        }
      });
    }
  });
}

$(".preloader").fadeOut(500);
// }
