var arr = window.location.pathname.split("/");
var projectId = arr[arr.length - 1];

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

var indexOfUndescoreForCreatorId = $(".preloader")
  .attr("id")
  .indexOf("_");
var creatorId = $(".preloader")
  .attr("id")
  .substring(indexOfUndescoreForCreatorId + 1);

var user = null;

$(document).ready(function() {
  addVisit();

  initialiseProjectPage();
});

function addVisit() {
  $.ajax({
    url: "/api/addProjectVisit?projectId=" + projectId,
    method: "PUT",
    success: function() {}
  });
}

function initialiseProjectPage() {
  $(".should-send-profile-id").on("click", function(event) {
    event.preventDefault();
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var userIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);

    if (id === userIdToSend) {
      window.location.href = "/profile";
    }

    window.location.href = "/user/" + userIdToSend;
  });

  $("#join-project").click(function() {
    var instance = $(this);
    $.confirm({
      title: "Потвърдете присъединяването",
      content: "Сигурни ли сте, че искате да се присъедините към този проект?",
      theme: "supervan",
      buttons: {
        Да: {
          btnClass: "btn-blue",
          action: function() {
            instance.html(
              '<i class="fa fa-thumbs-up"></i> Присъедини се към проекта <i class="fas fa-circle-notch fa-spin"></i>'
            );
            $.ajax({
              url:
                "/api/sendMessageForApproval?projectId=" +
                projectId +
                "&newMemberId=" +
                id +
                "&creatorId=" +
                creatorId,
              method: "GET",
              success: function(result) {
                instance.html(
                  '<i class="fa fa-thumbs-up"></i> Присъедини се към проекта'
                );
                if (result.successfull) {
                  iziToast.success({
                    title: "ОК!",
                    message: "Запитването беше изпратено успешно",
                    position: "topRight"
                  });
                } else {
                  iziToast.error({
                    title: "Грешка",
                    message:
                      "Този проект не се нуждае от потребител от вашата категория от способности",
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
              message: "Запитването не беше изпратено",
              position: "topRight"
            });
          }
        }
      }
    });
  });

  $("#send-advice").click(function() {
    var instance = $(this);
    vex.dialog.open({
      message: "Напиши съветът, който искаш да изпратиш:",
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
            '<i class="fa fa-stack-exchange"></i> Дай съвет <i class="fas fa-circle-notch fa-spin"></i>'
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

          $.ajax({
            url: "/api/sendAdvice",
            method: "POST",
            data: JSON.stringify({
              topic: data.topic,
              content: data.content,
              timestamp: timestamp,
              senderId: id,
              projectId: projectId
            }),
            contentType: "application/json",
            success: function(result) {
              instance.html('<i class="fa fa-stack-exchange"></i> Дай съвет');
              if (result.successfull) {
                iziToast.success({
                  title: "ОК!",
                  message: "Съветът беше изпратен успешно",
                  position: "topRight"
                });
              } else {
                iziToast.warning({
                  title: "Грешка",
                  message: "Съветът не беше изпратен успешно",
                  position: "topRight"
                });
              }
            }
          });
        }
      }
    });
  });

  $("#send-question").click(function() {
    var instance = $(this);
    vex.dialog.open({
      message: "Напиши въпросът, който искаш да попиташ:",
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
            '<i class="fa fa-question"></i> Попитай въпрос <i class="fas fa-circle-notch fa-spin"></i>'
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

          $.ajax({
            url: "/api/sendQuestion",
            method: "POST",
            data: JSON.stringify({
              topic: data.topic,
              content: data.content,
              timestamp: timestamp,
              senderId: id,
              projectId: projectId
            }),
            contentType: "application/json",
            success: function(result) {
              instance.html('<i class="fa fa-question"></i> Попитай въпрос');
              if (result.successfull) {
                iziToast.success({
                  title: "ОК!",
                  message: "Въпросът беше изпратен успешно",
                  position: "topRight"
                });
              } else {
                iziToast.warning({
                  title: "Грешка",
                  message: "Въпросът не беше изпратен успешно",
                  position: "topRight"
                });
              }
            }
          });
        }
      }
    });
  });

  $(".preloader").fadeOut(500);
}
