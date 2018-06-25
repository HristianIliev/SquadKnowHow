var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

var user = null;

execute();

function execute() {
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
