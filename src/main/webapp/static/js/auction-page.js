$(document).ready(function() {
  var arr = window.location.pathname.split("/");
  var auctionId = arr[arr.length - 1];

  var indexOfUndescoreForUserId = $("body")
    .attr("id")
    .indexOf("_");
  var id = $("body")
    .attr("id")
    .substring(indexOfUndescoreForUserId + 1);

  var indexOfUndescoreForCreator = $(".preloader")
    .attr("id")
    .indexOf("_");
  var creatorId = $(".preloader")
    .attr("id")
    .substring(indexOfUndescoreForCreator + 1);

  $.ajax({
    url: "/api/getEndDate?auctionId=" + auctionId,
    method: "GET",
    success: function(result) {
      var tokens = result.split("-");

      $(".time-left")
        .countdown(
          tokens[0] +
            "/" +
            tokens[1] +
            "/" +
            tokens[2].substring(0, 2) +
            " " +
            tokens[2].substring(3, 8),
          function(event) {
            $(this).html(event.strftime("%-w седмици %-d дни %-H:%-M:%-S"));
          }
        )
        .on("finish.countdown", function() {
          $.ajax({
            url: "/api/finishAuction?auctionId=" + auctionId,
            method: "GET",
            success: function() {
            }
          });
          $(".time-left").text(
            tokens[0] +
              "/" +
              tokens[1] +
              "/" +
              tokens[2].substring(0, 2) +
              " " +
              tokens[2].substring(3, 8)
          );
        })
        .on("update.countdown", function(event) {
          if (event.offset.totalDays === 0) {
            $(this).addClass("red-text");
          }
        });

      if ($(".time-left").html() === "0 дни 0:0:0") {
        $(".time-left").html(
          "Приключил на: " +
            tokens[0] +
            "/" +
            tokens[1] +
            "/" +
            tokens[2].substring(0, 2) +
            " " +
            tokens[2].substring(3, 8)
        );
      }

      $("#contact-seller").click(function(event) {
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
            } else {
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
                  senderId: id,
                  recipientId: creatorId,
                  kind: "normal"
                }),
                contentType: "application/json",
                success: function(result) {
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

      $("#submit-bid-btn").click(function() {
        var instance = $(this);
        instance.html('Заложи <i class="fas fa-circle-notch fa-spin"></i>');
        if (
          $("#bid-input").val() === 0.0 ||
          $("#bid-input").val() === null ||
          $("#bid-input").val() === ""
        ) {
          instance.html("Заложи");
          iziToast.warning({
            title: "ОК!",
            message: "Не сте въвели цифра",
            position: "topRight"
          });
        }

        $.ajax({
          url: "/api/createBid?userId=" + id + "&auctionId=" + auctionId,
          method: "POST",
          data: JSON.stringify({
            amount: $("#bid-input").val()
          }),
          contentType: "application/json",
          success: function(result) {
            instance.html("Заложи");
            if (result === "true" || result === true) {
              iziToast.success({
                title: "ОК!",
                message: "Вие успешно направихте залагане",
                position: "topRight"
              });
              location.reload();
            } else if (result === "false" || result === false) {
              iziToast.warning({
                title: "Грешка!",
                message: "Нямате достатъчно средства в сметката си",
                position: "topRight"
              });
            }
          }
        });
      });

      $("#buy-now-btn").click(function() {
        var instance = $(this);
        instance.html('Купи сега <i class="fas fa-circle-notch fa-spin"></i>');
        $.ajax({
          url: "/api/buyNowAuction?userId=" + id + "&auctionId=" + auctionId,
          method: "GET",
          contentType: "application/json",
          success: function(result) {
            instance.html("Купи сега");
            if (result === "false" || result === false) {
              iziToast.warning({
                title: "Грешка!",
                message: "Нямате достатъчно средства в сметката си",
                position: "topRight"
              });
            }
          }
        });
      });

      $(".preloader").fadeOut(500);
    }
  });
});
