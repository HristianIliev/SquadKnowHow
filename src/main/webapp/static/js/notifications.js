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
