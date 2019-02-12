$(document).ready(function () {
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

  var previousA = $(".my-active");

  $(".nav-item").click(function () {
    previousA.removeClass("my-active");
    previousA = $(this);
    $(this).addClass("my-active");
  });

  $.ajax({
    url: "/api/getEndDate?auctionId=" + auctionId,
    method: "GET",
    success: function (result) {
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
          function (event) {
            obj = event;
            $(this).html(event.strftime("%-w седмици %-d дни %-H:%-M:%-S"));
          }
        )
        .on("finish.countdown", function () {
          $.ajax({
            url: "/api/finishAuction?auctionId=" + auctionId,
            method: "GET",
            success: function () {
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
        .on("update.countdown", function (event) {
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

      $(".titanic").click(function (event) {
        $.confirm({
          title: "Потвърдете изтриването",
          content: "Сигурни ли сте, че искате да изтриете този търг",
          theme: "supervan",
          buttons: {
            Да: {
              btnClass: "btn-blue",
              action: function () {
                $.ajax({
                  url: "/api/deleteAuction?auctionId=" + auctionId,
                  method: "DELETE",
                  success: function (result) {
                    location.replace("/auctions");
                  }
                });
              }
            },
            Не: {
              btnClass: "btn-blue",
              action: function () {}
            }
          }
        });
      });

      tippy("[title]", {
        placement: "top",
        animation: "perspective",
        duration: 700,
        arrow: true,
        arrowType: "round",
        interactive: true,
        size: "large"
      });

      $(".preloader").fadeOut(500);
    }
  });
});