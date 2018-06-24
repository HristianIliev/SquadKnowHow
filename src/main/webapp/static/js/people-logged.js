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

  var skillToSend = $("#skilled-in")
    .val()
    .substring(0, $("#skilled-in").val().length - 2);
  var interestToSend = $("#interested-in")
    .val()
    .substring(0, $("#interested-in").val().length - 2);
  sendGetPages($("#search-by-city").val(), skillToSend, interestToSend);
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
      var skillToSend = $("#skilled-in")
        .val()
        .substring(0, $("#skilled-in").val().length - 2);
      var interestToSend = $("#interested-in")
        .val()
        .substring(0, $("#interested-in").val().length - 2);
      sendGetPages(ui.item.value, skillToSend, interestToSend);
    },
    autoFocus: true
  });

  $("#skilled-in")
    .on("keydown", function(event) {
      if (
        event.keyCode === $.ui.keyCode.TAB &&
        $(this).autocomplete("instance").menu.active
      ) {
        event.preventDefault();
      }
    })
    .autocomplete({
      source: function(request, response) {
        $.getJSON(
          "/api/getSkills",
          {
            term: extractLast(request.term)
          },
          response
        );
      },
      search: function() {
        // custom minLength
        var term = extractLast(this.value);
        if (term.length < 2) {
          return false;
        }
      },
      focus: function() {
        // prevent value inserted on focus
        return false;
      },
      select: function(event, ui) {
        var terms = split(this.value);
        // remove the current input
        terms.pop();
        // add the selected item
        terms.push(ui.item.value);
        // add placeholder to get the comma-and-space at the end
        terms.push("");
        this.value = terms.join(", ");

        var interestToSend = $("#interested-in")
          .val()
          .substring(0, $("#interested-in").val().length - 2);
        sendGetPages(
          $("#search-by-city").val(),
          $("#skilled-in")
            .val()
            .substring(0, $("#skilled-in").val().length - 2),
          interestToSend
        );
        return false;
      },
      autoFocus: true
    });

  $("#interested-in")
    .on("keydown", function(event) {
      if (
        event.keyCode === $.ui.keyCode.TAB &&
        $(this).autocomplete("instance").menu.active
      ) {
        event.preventDefault();
      }
    })
    .autocomplete({
      source: function(request, response) {
        $.getJSON(
          "/api/getInterests",
          {
            term: extractLast(request.term)
          },
          response
        );
      },
      search: function() {
        // custom minLength
        var term = extractLast(this.value);
        if (term.length < 2) {
          return false;
        }
      },
      focus: function() {
        // prevent value inserted on focus
        return false;
      },
      select: function(event, ui) {
        var terms = split(this.value);
        // remove the current input
        terms.pop();
        // add the selected item
        terms.push(ui.item.value);
        // add placeholder to get the comma-and-space at the end
        terms.push("");
        this.value = terms.join(", ");

        var skillToSend = $("#skilled-in")
          .val()
          .substring(0, $("#skilled-in").val().length - 2);
        var interestToSend = $("#interested-in")
          .val()
          .substring(0, $("#interested-in").val().length - 2);
        sendGetPages($("#search-by-city").val(), skillToSend, interestToSend);
        return false;
      },
      autoFocus: true
    });
});

$("#btn-search-user-name").click(function(event) {
  event.preventDefault();
  var skillToSend = $("#skilled-in")
    .val()
    .substring(0, $("#skilled-in").val().length - 2);
  var interestToSend = $("#interested-in")
    .val()
    .substring(0, $("#interested-in").val().length - 2);
  sendGetPages($("#search-by-city").val(), skillToSend, interestToSend);
});

function sendGetPages(city, skill, interest) {
  var name = $("#search-user-name").val();
  var userCategoryRaw = $("#dropdown-filter").text();
  var userCategory = userCategoryRaw.substring(10, userCategoryRaw.length);
  if (userCategory === "            Търси за: " || userCategory === "Всички") {
    userCategory = "";
  }

  getPages(name, userCategory, city, skill, interest);
}

getPages("", "", "", "", "");

function getPages(name, userCategory, city, skill, interest) {
  $.ajax({
    url:
      "/api/getPeoplePages?name=" +
      name +
      "&userCategory=" +
      userCategory +
      "&city=" +
      city +
      "&skills=" +
      skill +
      "&interests=" +
      interest,
    method: "GET",
    success: function(result) {
      if (result.numberOfPages !== 0) {
        $(".card-title-people").text(
          "Хора търсещи партньори за проект: " + result.numberOfEntries
        );
        $("#pagination-people").remove();
        $(".pagination-box").append(
          $("<div/>")
            .attr("style", "text-align:center;")
            .append(
              $("<ul/>")
                .attr("id", "pagination-people")
                .addClass("pagination-lg")
                .addClass("justify-content-end")
            )
        );
        $("#pagination-people").twbsPagination({
          totalPages: result.numberOfPages,
          visiblePages: 7,
          onPageClick: function(event, page) {
            getUsers(name, userCategory, city, skill, interest);
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

function getUsers(name, userCategory, city, skill, interest) {
  $(".card-deck")
    .removeClass("animated")
    .removeClass("zoomIn");

  $(".card-deck").empty();

  $(".card-deck").append($("<div/>").addClass("loader-entries"));

  $.ajax({
    url:
      "/api/users?page=" +
      $("#pagination-people .active").text() +
      "&name=" +
      name +
      "&userCategory=" +
      userCategory +
      "&city=" +
      city +
      "&skills=" +
      skill +
      "&interests=" +
      interest,
    method: "GET",
    success: function(result) {
      populateUsers(result);
    }
  });
}

function populateUsers(result) {
  $(".card-deck").empty();
  var users = result.users;
  var profileStyle = "";
  for (var i = 0; i < users.length; i += 1) {
    var profileCover = "";
    switch (users[i].skillset.name) {
      case "Програмист":
        profileCover = "static/images/rotating_card_thumb2.png";
        profileStyle = "";
        break;
      case "Инженер":
        profileCover = "";
        profileStyle =
          "background: linear-gradient(to bottom, #00bfa5 0%, #b2dfdb 100%);";
        break;
      case "Дизайнер":
        profileCover = "";
        profileStyle =
          "background: linear-gradient(to bottom, #64dd17 0%, #dcedc8 100%);";
        break;
      case "Учен":
        profileCover = "";
        profileStyle =
          "background: linear-gradient(to bottom, #263238 0%, #cfd8dc 100%);";
        break;
      case "Музикант":
        profileCover = "";
        profileStyle =
          "background: linear-gradient(to bottom, #6200ea 0%, #d1c4e9 100%);";
        break;
      case "Художник":
        profileCover = "";
        profileStyle =
          "background: linear-gradient(to bottom, #c51162 0%, #f8bbd0 100%);";
        break;
      case "Писател":
        profileCover = "";
        profileStyle =
          "background: linear-gradient(to bottom, #aeea00 0%, #fff9c4 100%);";
        break;
      case "Режисьор":
        profileCover = "";
        profileStyle =
          "background: linear-gradient(to bottom, #3e2723 0%, #d7ccc8 100%);";
        break;
      case "Продуктов мениджър":
        profileCover = "";
        profileStyle =
          "background: linear-gradient(to bottom, #ffab00 0%, #ffecb3 100%);";
        break;
    }

    var image = "";
    if (users[i].image !== null) {
      image = "data:image/jpg;base64, " + users[i].image;
    } else {
      image = "static/images/default-profile-picture.jpg";
    }

    var cityName = "";
    if (users[i].city !== null) {
      cityName = users[i].city.name + ", България";
    } else {
      cityName = "България";
    }

    var description = users[i].description;
    if (description.length > 177) {
      description = description.substring(0, 174) + "...";
    }

    var skills = "";
    for (var j = 0; j < users[i].skills.length; j += 1) {
      skills += users[i].skills[j].name;
      if (j !== users[i].skills.length - 1) {
        skills += ", ";
      }
    }
    if (skills === "") {
      skills = "Няма въведени умения";
    }

    var interests = "";
    for (var j = 0; j < users[i].interests.length; j += 1) {
      interests += users[i].interests[j].name;
      if (j !== users[i].interests.length - 1) {
        interests += ", ";
      }
    }
    if (interests === "") {
      interests = "Няма въведени интереси";
    }

    var previousEmployment = "";
    for (var j = 0; j < users[i].previousEmployment.length; j += 1) {
      previousEmployment += users[i].previousEmployment[j].name;
      if (j !== users[i].previousEmployment.length - 1) {
        previousEmployment += ", ";
      }
    }
    if (previousEmployment === "") {
      previousEmployment = "Няма въведени предишни работни места";
    }

    var degree = users[i].degree;
    if (degree === null) {
      degree = "Няма въведено образувание";
    }

    $(".card-deck")
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
                          .attr("href", "/user/" + users[i].id)
                          .attr("id", "user-cover_" + users[i].id)
                          .addClass("user-cover")
                          .addClass("should-send-user-id")
                          .append(
                            $("<div/>")
                              .addClass("cover")
                              .append($("<img/>").attr("src", profileCover))
                              .attr("style", profileStyle)
                          )
                      )
                      .append(
                        $("<a/>")
                          .attr("id", "user-image_" + users[i].id)
                          .attr("href", "/user/" + users[i].id)
                          .addClass("user-image")
                          .addClass("should-send-user-id")
                          .append(
                            $("<div/>")
                              .addClass("user")
                              .append(
                                $("<img/>")
                                  .addClass("img-circle")
                                  .attr("src", image)
                              )
                          )
                      )
                      .append(
                        $("<div/>")
                          .addClass("content")
                          .append(
                            $("<div/>")
                              .addClass("main")
                              .append(
                                $("<a/>")
                                  .attr("id", "card-name_" + users[i].id)
                                  .addClass("name")
                                  .addClass("card-name")
                                  .addClass("should-send-user-id")
                                  .attr("href", "/user/" + users[i].id)
                                  .text(
                                    users[i].firstName + " " + users[i].lastName
                                  )
                              )
                              .append(
                                $("<p/>")
                                  .addClass("profession")
                                  .text(users[i].skillset.name)
                              )
                              .append(
                                $("<p/>")
                                  .addClass("text-center")
                                  .html(
                                    '<i class="fa fa-map-marker fa-3x"></i> ' +
                                      cityName
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
                          .html('<h3 class="motto">"' + description + '"</h3>')
                      )
                      .append(
                        $("<div/>")
                          .attr("style", "position: relative;")
                          .addClass("content")
                          .append(
                            $("<div/>")
                              .addClass("main")
                              .append(
                                $("<h4/>")
                                  .addClass("text-center")
                                  .text("Умения")
                              )
                              .append(
                                $("<p/>")
                                  .addClass("text-center")
                                  .attr("style", "font-size: 13px;")
                                  .text(skills)
                              )
                              .append(
                                $("<h4/>")
                                  .addClass("text-center")
                                  .text("Интереси")
                              )
                              .append(
                                $("<p/>")
                                  .addClass("text-center")
                                  .text(interests)
                              )
                              .append(
                                $("<h4/>")
                                  .addClass("text-center")
                                  .text("Образувание")
                              )
                              .append(
                                $("<p/>")
                                  .addClass("text-center")
                                  .text(degree)
                              )
                              .append(
                                $("<h4/>")
                                  .addClass("text-center")
                                  .text("Работил за")
                              )
                              .append(
                                $("<p/>")
                                  .addClass("text-center")
                                  .text(previousEmployment)
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
                              .html('<i class="fa fa-reply"></i> Обърни')
                          )
                      )
                  )
              )
          )
      );
  }

  $(".should-send-user-id").on("click", function() {
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var userIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);

    if (userIdToSend === id) {
      $(this).attr("href", "/profile");
    } else {
      // sessionStorage.setItem("profileIdFromPeopleLogged", userIdToSend);
    }
  });

  $(".preloader").fadeOut(500);
}
// }
