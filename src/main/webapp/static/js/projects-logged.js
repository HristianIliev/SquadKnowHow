// var id = sessionStorage.getItem("userId");
var user = null;

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

// if (id === "null" || id === null) {
//   window.location.replace("/sign-in");
// } else {
$(".dropdown-item").click(function() {
  var value = $(this).text();
  $("#dropdown-filter").text("Търси за: " + value);

  sendGetPages($("#search-by-city").val());
});

$("#start-a-project-btn").click(function() {
  window.location.href = "/create-project";
});

// $.ajax({
//   url: "/api/user?id=" + id,
//   method: "GET",
//   success: function(result) {
//     user = result;
//     $("#profile-image-navbar").attr(
//       "src",
//       "data:image/png;base64, " + user.image
//     );

//   }
// });

getPages("", "", "");

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

$(function() {
  function split(val) {
    return val.split(/,\s*/);
  }
  function extractLast(term) {
    return split(term).pop();
  }

  $("#search-by-city").autocomplete({
    source: "/api/getCities",
    minLength: 2,
    select: function(event, ui) {
      sendGetPages(ui.item.value);
    },
    autoFocus: true
  });
});

$("#btn-search-user-name").click(function(event) {
  event.preventDefault();
  sendGetPages($("#search-by-city").val());
});

function sendGetPages(city) {
  var name = $("#search-user-name").val();
  var userCategoryRaw = $("#dropdown-filter").text();
  var userCategory = userCategoryRaw.substring(10, userCategoryRaw.length);
  if (
    userCategory === "            Проекти търсещи: " ||
    userCategory === "Всички"
  ) {
    userCategory = "";
  }

  getPages(name, userCategory, city);
}

function getPages(name, userCategory, city) {
  $.ajax({
    url:
      "/api/getProjectPages?name=" +
      name +
      "&userCategory=" +
      userCategory +
      "&city=" +
      city,
    method: "GET",
    success: function(result) {
      if (result.numberOfPages !== 0) {
        $(".card-title-people").text(
          "Проекти търсещи партньори: " + result.numberOfEntries
        );
        $("#pagination-projects").remove();
        $(".pagination-box").append(
          $("<div/>")
            .attr("style", "text-align:center;")
            .append(
              $("<ul/>")
                .attr("id", "pagination-projects")
                .addClass("pagination-lg")
                .addClass("justify-content-end")
            )
        );
        $("#pagination-projects").twbsPagination({
          totalPages: result.numberOfPages,
          visiblePages: 7,
          onPageClick: function(event, page) {
            getProjects(name, userCategory, city);
          }
        });
      } else {
        iziToast.error({
          title: "Грешка",
          message: "Не бяха намерени потребители по тези критерии",
          position: "topRight"
        });
      }
    }
  });
}

function getProjects(name, userCategory, city) {
  $(".card-deck")
    .removeClass("animated")
    .removeClass("zoomIn");

  $(".card-deck").empty();

  $(".card-deck").append($("<div/>").addClass("loader-entries"));

  $.ajax({
    url:
      "/api/projects?page=" +
      $("#pagination-projects .active").text() +
      "&name=" +
      name +
      "&userCategory=" +
      userCategory +
      "&city=" +
      city,
    method: "GET",
    success: function(result) {
      populateProjects(result);
    }
  });
}

function populateProjects(result) {
  $(".card-deck").empty();
  var projects = result.projects;

  for (var i = projects.length - 1; i >= 0; i -= 1) {
    var projectNeeds = "";
    if (projects[i].projectNeeds.length !== 0) {
      for (var j = 0; j < projects[i].projectNeeds.length; j += 1) {
        projectNeeds += projects[i].projectNeeds[j].name;
        if (j !== projects[i].projectNeeds.length - 1) {
          projectNeeds += ", ";
        }
      }
    } else {
      projectNeeds = "Този проект не се нуждае от никого за момента";
    }

    var isMember = false;
    for (var k = 0; k < projects[i].projectMembers.length; k += 1) {
      if (projects[i].projectMembers[k].id === parseInt(id)) {
        isMember = true;
      }
    }

    if (!isMember) {
      $("#card-deck-projects-logged")
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
                        .attr("id", "creator-id_" + projects[i].creator)
                        .addClass("front")
                        .append(
                          $("<a/>")
                            .attr("id", "user-cover_" + projects[i].id)
                            .addClass("should-send-project-id")
                            .append(
                              $("<div/>")
                                .addClass("cover-rotating")
                                .addClass("overlay")
                                .addClass("view")
                                .append(
                                  $("<img/>").attr(
                                    "src",
                                    "data:image/png;base64, " +
                                      projects[i].cover
                                  )
                                )
                                .append(
                                  $("<a/>").append(
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
                                .attr(
                                  "id",
                                  "creator-id2_" + projects[i].creator
                                )
                                .addClass("main")
                                .append(
                                  $("<a/>")
                                    .attr("id", "card-name_" + projects[i].id)
                                    .attr("href", "/project/" + projects[i].id)
                                    .addClass("should-send-project-id")
                                    .addClass("name")
                                    .addClass("card-name")
                                    .text(projects[i].name)
                                )
                                .append(
                                  $("<p/>")
                                    .addClass("text-center")
                                    .html(
                                      '<i class="fas fa-map-marker-alt fa-3x"></i>' +
                                        projects[i].city.name +
                                        ", България</p>"
                                    )
                                )
                            )
                            .append(
                              $("<div/>")
                                .addClass("footer")
                                .append(
                                  $("<button/>")
                                    .addClass("btn-rotating")
                                    .addClass("btn-rotating-simple")
                                    .attr("onclick", "rotateCard(this)")
                                    .html(
                                      '<i class="fa fa-mail-forward"></i> Разбери още'
                                    )
                                )
                            )
                        )
                    )
                    .append(
                      $("<div/>")
                        .addClass("back")
                        .append(
                          $("<div/>")
                            .attr("style", "position: relative;")
                            .addClass("header")
                            .append(
                              $("<h3/>")
                                .addClass("motto")
                                .text(
                                  projects[i].description.substring(0, 126) +
                                    "..."
                                )
                            )
                        )
                        .append(
                          $("<div/>")
                            .attr(
                              "style",
                              "position: relative; margin-top: 75px;"
                            )
                            .addClass("content")
                            .append(
                              $("<div/>")
                                .addClass("main")
                                .append(
                                  $("<h4/>")
                                    .addClass("text-center")
                                    .text("Проектът се нуждае от")
                                )
                                .append(
                                  $("<p/>")
                                    .addClass("text-center")
                                    .attr("style", "font-size: 13px;")
                                    .text(projectNeeds)
                                )
                            )
                        )
                        .append(
                          $("<div/>")
                            .addClass("footer")
                            .append(
                              $("<button/>")
                                .addClass("btn-rotating")
                                .addClass("btn-rotating-simple")
                                .attr("title", "Обърни картата")
                                .attr("onclick", "rotateCard(this)")
                                .html('<i class="fa fa-reply"></i> Back')
                            )
                        )
                    )
                )
            )
        );
    } else {
      $("#card-deck-projects-logged")
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
                        .attr("id", "creator-id_" + projects[i].creator)
                        .addClass("front")
                        .append(
                          $("<a/>")
                            .attr("id", "user-cover_" + projects[i].id)
                            .addClass("should-send-project-id")
                            .addClass("isProjectMember")
                            .append(
                              $("<div/>")
                                .addClass("cover-rotating")
                                .addClass("overlay")
                                .addClass("view")
                                .append(
                                  $("<img/>").attr(
                                    "src",
                                    "data:image/png;base64, " +
                                      projects[i].cover
                                  )
                                )
                                .append(
                                  $("<a/>").append(
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
                                .attr(
                                  "id",
                                  "creator-id2_" + projects[i].creator
                                )
                                .addClass("main")
                                .append(
                                  $("<a/>")
                                    .attr("id", "card-name_" + projects[i].id)
                                    .attr("href", "/project/" + projects[i].id)
                                    .addClass("isProjectMember")
                                    .addClass("should-send-project-id")
                                    .addClass("name")
                                    .addClass("card-name")
                                    .text(projects[i].name)
                                )
                                .append(
                                  $("<p/>")
                                    .addClass("text-center")
                                    .html(
                                      '<i class="fas fa-map-marker-alt fa-3x"></i>' +
                                        projects[i].city.name +
                                        ", България</p>"
                                    )
                                )
                            )
                            .append(
                              $("<div/>")
                                .addClass("footer")
                                .append(
                                  $("<button/>")
                                    .addClass("btn-rotating")
                                    .addClass("btn-rotating-simple")
                                    .attr("onclick", "rotateCard(this)")
                                    .html(
                                      '<i class="fa fa-mail-forward"></i> Разбери още'
                                    )
                                )
                            )
                        )
                    )
                    .append(
                      $("<div/>")
                        .addClass("back")
                        .append(
                          $("<div/>")
                            .attr("style", "position: relative;")
                            .addClass("header")
                            .append(
                              $("<h3/>")
                                .addClass("motto")
                                .text(
                                  projects[i].description.substring(0, 126) +
                                    "..."
                                )
                            )
                        )
                        .append(
                          $("<div/>")
                            .attr(
                              "style",
                              "position: relative; margin-top: 75px;"
                            )
                            .addClass("content")
                            .append(
                              $("<div/>")
                                .addClass("main")
                                .append(
                                  $("<h4/>")
                                    .addClass("text-center")
                                    .text("Проектът се нуждае от")
                                )
                                .append(
                                  $("<p/>")
                                    .addClass("text-center")
                                    .attr("style", "font-size: 13px;")
                                    .text(projectNeeds)
                                )
                            )
                        )
                        .append(
                          $("<div/>")
                            .addClass("footer")
                            .append(
                              $("<button/>")
                                .addClass("btn-rotating")
                                .addClass("btn-rotating-simple")
                                .attr("title", "Обърни картата")
                                .attr("onclick", "rotateCard(this)")
                                .html('<i class="fa fa-reply"></i> Back')
                            )
                        )
                    )
                )
            )
        );
    }
  }

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

    var isProjectMember = instance.hasClass("isProjectMember");

    if (isProjectMember && creatorId !== id) {
      instance.attr("href", "/project-member/" + projectIdToSend);
    } else if (creatorId === id) {
      instance.attr("href", "/project-admin/" + projectIdToSend);
    } else {
      instance.attr("href", "/project/" + projectIdToSend);
    }
    // sessionStorage.setItem("projectId", projectIdToSend);
  });

  $(".preloader").fadeOut(500);
}
// }
