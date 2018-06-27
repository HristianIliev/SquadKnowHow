var user = null;
var needsMoney = $(".fund-me").is(":checked");

$(".fund-me").change(function() {
  if ($(".fund-me").is(":checked")) {
    needsMoney = true;
    $("#step-1").append(
      $("<div/>")
        .attr("id", "money-form")
        .addClass("md-form")
        .addClass("form-lg")
        .addClass("form-margins")
        .append(
          $("<i/>")
            .attr("id", "neededMoneyIcon")
            .addClass("far")
            .addClass("fa-money-bill-alt")
            .addClass("prefix")
            .addClass("fa-7x")
        )
        .append(
          $("<input/>")
            .attr("id", "neededMoney")
            .attr("type", "number")
            .attr("min", "0.01")
            .attr("step", "0.01")
            .attr("value", "0.01")
            .addClass("form-control")
        )
        .append(
          $("<label/>")
            .attr("id", "neededMoneyLabel")
            .attr("for", "neededMoney")
            .text("Сума от която се нуждае проекта ти (Euro)")
        )
    );
  } else {
    $("#money-form").remove();
    needsMoney = false;
  }
});

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

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
    success: function(result) {}
  });
}

function showCodeVerificationModal(telephone, countryCode) {
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
            var neededMoney;
            if (needsMoney) {
              neededMoney = $("#neededMoney").val();
            } else {
              neededMoney = 0.0;
            }

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
                telephone: telephone,
                neededMoney: neededMoney,
                receivedMoney: 0,
                needsMoney: needsMoney
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
