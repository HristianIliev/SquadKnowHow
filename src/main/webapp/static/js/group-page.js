var wasOverlapped = false;
var user = null;

var arr = window.location.pathname.split("/");
var groupId = arr[arr.length - 1];

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

var previousA = $(".my-active");

$(".should-send-project-id").on("click", function () {
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

  if (creatorId === id) {
    instance.attr("href", "/project-admin/" + projectIdToSend);
  } else {
    instance.attr("href", "/project/" + projectIdToSend);
  }
});

$("#join-group-button").click(function () {
  $.ajax({
    url: "/api/addGroupMember?groupId=" + groupId + "&userId=" + id,
    method: "GET",
    success: function (result) {
      if (result) {
        iziToast.success({
          title: "ОК!",
          message: "Успешно бяхте добавен в тази група.",
          position: "topRight"
        });
      }
    }
  });
});


$(".nav-item").click(function () {
  previousA.removeClass("my-active");
  previousA = $(this);
  $(this).addClass("my-active");
});

$(".preloader").fadeOut(500);