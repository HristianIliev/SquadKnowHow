var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

$(".should-delete-notification").on("click", function () {
  var indexOfUndescore = $(this)
    .attr("id")
    .indexOf("_");
  var notificationId = $(this)
    .attr("id")
    .substring(indexOfUndescore + 1);

  $.ajax({
    url: "/api/deleteNotification?notificationId=" + notificationId,
    method: "DELETE",
    success: function (result) {
      if (result.successfull) {
        window.location.href = "/messages";
      }
    }
  });
});

$(".should-delete-notifications").on("click", function (event) {
  event.preventDefault();

  $.ajax({
    url: "/api/deleteNotifications?id=" + id,
    method: "DELETE",
    success: function (result) {
      document.location.reload();
    }
  });

});