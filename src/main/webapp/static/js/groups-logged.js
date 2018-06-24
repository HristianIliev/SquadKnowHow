// var id = sessionStorage.getItem("userId");
var user = null;

// if (id === "null" || id === null) {
//   window.location.replace("/sign-in");
// } else {

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

$("#button-create-group").click(function() {
  window.location.href = "/create-group";
});

// $.ajax({
//   url: "/api/user?id=" + id,
//   method: "GET",
//   success: function(result) {
//     user = result;
// $("#profile-image-navbar").attr(
//   "src",
//   "data:image/png;base64, " + user.image
// );

//   }
// });

setUpNotifications();

function setUpNotifications() {
  // var notifications = result.notifications;
  // $(".counter").text(notifications.length);
  // if (notifications.length !== 0) {
  //   for (var i = notifications.length - 1; i >= 0; i -= 1) {
  //     $("#notifications-list").append(
  //       $("<li/>")
  //         .attr("id", "notification_" + notifications[i].id)
  //         .addClass("should-delete-notification")
  //         .append(
  //           $("<a/>")
  //             .attr("href", "#")
  //             .addClass("peers")
  //             .addClass("fxw-nw")
  //             .addClass("td-n")
  //             .addClass("p-20")
  //             .addClass("bdB")
  //             .addClass("c-grey-800")
  //             .addClass("cH-blue")
  //             .addClass("bgcH-grey-100")
  //             .append(
  //               $("<div/>")
  //                 .addClass("peer")
  //                 .addClass("mR-15")
  //                 .append(
  //                   $("<img/>")
  //                     .addClass("w-3r")
  //                     .addClass("bdrs-50p")
  //                     .attr(
  //                       "src",
  //                       "data:image/png;base64, " +
  //                         notifications[i].notificationSender.picture
  //                     )
  //                 )
  //             )
  //             .append(
  //               $("<div/>")
  //                 .addClass("peer")
  //                 .addClass("peer-greed")
  //                 .append(
  //                   $("<span/>")
  //                     .addClass("fw-500")
  //                     .text(notifications[i].notificationSender.name)
  //                 )
  //                 .append(
  //                   $("<span/>")
  //                     .addClass("c-grey-600")
  //                     .text(notifications[i].content)
  //                 )
  //                 .append(
  //                   $("<p/>")
  //                     .addClass("m-0")
  //                     .append(
  //                       $("<small/>")
  //                         .addClass("fsz-xs")
  //                         .text(notifications[i].timestamp)
  //                     )
  //                 )
  //             )
  //         )
  //     );
  //   }
  // } else {
  //   $("#notifications-list").append(
  //     $("<h4/>")
  //       .addClass("text-center")
  //       .addClass("no-notifications")
  //       .html('Няма нови известия <i class="far fa-frown"></i>')
  //   );
  // }

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
}

$("#btn-search-user-name").click(function(event) {
  event.preventDefault();
  sendGetPages();
});

function sendGetPages() {
  var name = $("#search-user-name").val();
  getPages(name);
}

getPages("");

function getPages(name) {
  $.ajax({
    url: "/api/getGroupPages?name=" + name,
    method: "GET",
    success: function(result) {
      if (result.numberOfPages !== 0) {
        $(".card-title-people").text(
          "Групи от потребители: " + result.numberOfEntries
        );
        $("#pagination-groups").remove();
        $(".pagination-box").append(
          $("<div/>")
            .attr("style", "text-align:center;")
            .append(
              $("<ul/>")
                .attr("id", "pagination-groups")
                .addClass("pagination-lg")
                .addClass("justify-content-end")
            )
        );
        $("#pagination-groups").twbsPagination({
          totalPages: result.numberOfPages,
          visiblePages: 7,
          onPageClick: function(event, page) {
            getGroups(name);
          }
        });
      } else {
        iziToast.error({
          title: "Грешка",
          message: "Не бяха намерени групи по тези критерии",
          position: "topRight"
        });
      }
    }
  });
}

function getGroups(name) {
  $(".card-deck")
    .removeClass("animated")
    .removeClass("zoomIn");

  $(".card-deck").empty();

  $(".card-deck").append($("<div/>").addClass("loader-entries"));

  $.ajax({
    url:
      "/api/groups?page=" +
      $("#pagination-groups .active").text() +
      "&name=" +
      name,
    method: "GET",
    success: function(result) {
      populateGroups(result);
    }
  });
}

function populateGroups(result) {
  $(".card-deck").empty();

  var groups = result.groups;
  for (var i = groups.length - 1; i >= 0; i -= 1) {
    var projectsCount = 0;
    for (var j = 0; j < groups[i].members.length; j += 1) {
      projectsCount += groups[i].members[j].projects.length;
    }

    $("#card-deck-groups-logged")
      .addClass("animated")
      .addClass("zoomIn")
      .append(
        $("<div/>")
          .addClass("col-md-3")
          .append(
            $("<div/>")
              .addClass("card-container")
              .addClass("manual-flip")
              .append(
                $("<div/>")
                  .addClass("card")
                  .append(
                    $("<div/>")
                      .addClass("front")
                      .append(
                        $("<a/>")
                          .attr("href", "/group/" + groups[i].id)
                          .attr("id", "user-cover_" + groups[i].id)
                          .addClass("should-send-group-id")
                          .append(
                            $("<div/>")
                              .addClass("view")
                              .addClass("overlay")
                              .addClass("cover-rotating")
                              .append(
                                $("<img/>").attr(
                                  "src",
                                  "data:image/png;base64, " + groups[i].logo
                                )
                              )
                              .append(
                                $("<a/>")
                                  .attr("href", "/group/")
                                  .append(
                                    $("<div/>")
                                      .addClass("mask")
                                      .addClass("rgba-white-slight")
                                  )
                              )
                          )
                      )
                      .append(
                        $("<div/>")
                          .addClass("content-rotating")
                          .append(
                            $("<div/>")
                              .addClass("main")
                              .addClass("text-center")
                              .append(
                                $("<a/>")
                                  .attr("id", "card-name_" + groups[i].id)
                                  .addClass("should-send-group-id")
                                  .attr("href", "/group/" + groups[i].id)
                                  .addClass("name")
                                  .text(groups[i].name)
                              )
                              .append(
                                $("<div/>")
                                  .addClass("row")
                                  .addClass("stats-row")
                                  .append(
                                    $("<div/>")
                                      .addClass("stat")
                                      .addClass("has-right-border")
                                      .addClass("col-md-6")
                                      .append(
                                        $("<h4/>").text(
                                          groups[i].members.length
                                        )
                                      )
                                      .append($("<p/>").text("Участващи"))
                                  )
                                  .append(
                                    $("<div/>")
                                      .addClass("stat")
                                      .addClass("col-md-6")
                                      .append($("<h4/>").text(projectsCount))
                                      .append($("<p/>").text("Проекта"))
                                  )
                              )
                          )
                      )
                  )
              )
          )
      );
  }

  $(".should-send-group-id").on("click", function() {
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var groupIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);
    // sessionStorage.setItem("groupIdFromGroupsLogged", groupIdToSend);
  });

  $(".preloader").fadeOut(500);
}
// }
