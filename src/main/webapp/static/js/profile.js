$(document).ready(function () {
  var indexOfUndescoreForCommunication = $(".example-slider-communication")
    .attr("id")
    .indexOf("_");
  var ratingCommunication = $(".example-slider-communication")
    .attr("id")
    .substring(indexOfUndescoreForCommunication + 1);

  var previousA = $(".my-active");

  $(".nav-item").click(function () {
    previousA.removeClass("my-active");
    previousA = $(this);
    $(this).addClass("my-active");
  });

  $(".example-slider-communication").ionRangeSlider({
    min: 0,
    max: 100,
    grid: true,
    from: ratingCommunication,
    from_fixed: true,
    postfix: "%"
  });

  var indexOfUndescoreForInitiative = $(".example-slider-initiative")
    .attr("id")
    .indexOf("_");
  var ratingInitiative = $(".example-slider-initiative")
    .attr("id")
    .substring(indexOfUndescoreForInitiative + 1);

  $(".example-slider-initiative").ionRangeSlider({
    min: 0,
    max: 100,
    grid: true,
    from: ratingInitiative,
    from_fixed: true,
    postfix: "%"
  });

  var indexOfUndescoreForLeadership = $(".example-slider-leadership")
    .attr("id")
    .indexOf("_");
  var ratingLeadership = $(".example-slider-leadership")
    .attr("id")
    .substring(indexOfUndescoreForLeadership + 1);

  $(".example-slider-leadership").ionRangeSlider({
    min: 0,
    max: 100,
    grid: true,
    from: ratingLeadership,
    from_fixed: true,
    postfix: "%"
  });

  var indexOfUndescoreForInnovation = $(".example-slider-innovation")
    .attr("id")
    .indexOf("_");
  var ratingInnovation = $(".example-slider-innovation")
    .attr("id")
    .substring(indexOfUndescoreForInnovation + 1);

  $(".example-slider-innovation").ionRangeSlider({
    min: 0,
    max: 100,
    grid: true,
    from: ratingInnovation,
    from_fixed: true,
    postfix: "%"
  });

  var indexOfUndescoreForResponsibility = $(".example-slider-responsibility")
    .attr("id")
    .indexOf("_");
  var ratingResponsibility = $(".example-slider-responsibility")
    .attr("id")
    .substring(indexOfUndescoreForResponsibility + 1);

  $(".example-slider-responsibility").ionRangeSlider({
    min: 0,
    max: 100,
    grid: true,
    from: ratingResponsibility,
    from_fixed: true,
    postfix: "%"
  });

  var occupation = $("#profile-occupation").text();
  startTime();

  function startTime() {
    var today = new Date();
    var h = today.getHours();
    var m = today.getMinutes();
    var s = today.getSeconds();
    m = checkTime(m);
    s = checkTime(s);
    $("#profile-occupation").html('<span id="occupation-alone">' + occupation + '</span>' +
      ' <span style="margin-left: 15px;font-size: 16px;color: rgb(161, 161, 161);">' +
      h +
      ":" +
      m +
      ":" +
      s +
      " локално време</span>"
    );
    var t = setTimeout(startTime, 500);
  }

  function checkTime(i) {
    if (i < 10) {
      i = "0" + i;
    } // add zero in front of numbers < 10
    return i;
  }

  $(".preloader").fadeOut(500);
});

var user = null;

var arr = window.location.pathname.split("/");
var idForUser = arr[arr.length - 1];

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var idForProfilePicture = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

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
          '<i class="fa fa-comments"></i> Изпрати съобщение <i class="fas fa-circle-notch fa-spin"></i>'
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

        $.ajax({
          url: "/api/sendMessage",
          method: "POST",
          data: JSON.stringify({
            topic: data.topic,
            content: data.content,
            timestamp: timestamp,
            senderId: idForProfilePicture,
            recipientId: idForUser,
            kind: "normal"
          }),
          contentType: "application/json",
          success: function (result) {
            instance.html('<i class="fa fa-comments"></i> Изпрати съобщение');
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

$(".invite-modal").click(function (event) {
  var instance = $(this);
  var name = $("#profile-name").text();
  var skillset = $("#occupation-alone").text();
  instance.html(
    '<i class="far fa-handshake"></i> Покани в проект <i class="fas fa-circle-notch fa-spin"></i>'
  );
  $.ajax({
    url: "/api/getAvailableProjectsForInvite?ownerId=" +
      idForProfilePicture +
      "&skillset=" +
      skillset,
    method: "GET",
    success: function (result) {
      if (result.length !== 0) {
        var input = '<select name="projects">';
        for (var i = 0; i < result.length; i += 1) {
          input +=
            '<option value="' +
            result[i].id +
            '">' +
            result[i].name +
            "</option>";
        }

        input += "</select>";

        vex.dialog.open({
          message: "Избери проект, за който да поканиш " + name + ":",
          input: [input].join(""),
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

              var projectId = data.projects;

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
                url: "/api/sendInviteMessage?projectId=" +
                  projectId +
                  "&recipient=" +
                  idForUser +
                  "&sender=" +
                  idForProfilePicture,
                method: "GET",
                success: function (result) {
                  instance.html(
                    '<i class="far fa-handshake"></i> Покани в проект'
                  );
                  if (result.successfull) {
                    iziToast.success({
                      title: "ОК!",
                      message: "Поканата беше изпратено успешно",
                      position: "topRight"
                    });
                  } else {
                    iziToast.warning({
                      title: "Грешка",
                      message: "Поканата не беше изпратено успешно",
                      position: "topRight"
                    });
                  }
                }
              });
            }
          }
        });
      } else {
        instance.html('<i class="far fa-handshake"></i> Покани в проект');
        iziToast.warning({
          title: "Грешка",
          message: "Вие не притежавате проект, който да се нуждае от " + name,
          position: "topRight"
        });
      }
    }
  });
});