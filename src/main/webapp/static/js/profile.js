var user = null;

var arr = window.location.pathname.split("/");
var idForUser = arr[arr.length - 1];

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var idForProfilePicture = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

getProfileData();

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
          '<i class="fa fa-comments"></i> Изпрати съобщение <i class="fas fa-circle-notch fa-spin"></i>'
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
          success: function(result) {
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

function getProfileData() {
  $(".should-send-group-id").on("click", function() {
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var groupIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);
  });

  $(".should-send-project-id").on("click", function() {
    var instance = $(this);

    var indexOfUndescore = instance.attr("id").indexOf("_");
    var projectIdToSend = instance.attr("id").substring(indexOfUndescore + 1);

    var indexOfUndescore2 = instance
      .parent()
      .attr("id")
      .indexOf("_");
    var creatorId = instance
      .parent()
      .attr("id")
      .substring(indexOfUndescore2 + 1);

    if (creatorId === idForProfilePicture) {
      instance.attr("href", "/project-admin");
    } else {
      instance.attr("href", "/project/" + projectIdToSend);
    }
  });

  $(".preloader").fadeOut(500);
}
