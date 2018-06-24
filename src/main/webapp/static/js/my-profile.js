// var id = sessionStorage.getItem("userId");

// $("#profile-image-navbar").attr(
//   "src",
//   "data:image/png;base64, " + result.image
// );
// $("#profile-image").attr("src", "data:image/png;base64, " + result.image);
// if (result.city !== null) {
//   $("#location").text(result.city.name + ", България");
// } else {
//   $("#location").text("България");
// }
// $("#email").text(result.email);
// $("#email").attr("href", "mailto:" + result.email);
// if (result.personalSite.length !== 0) {
//   $("#personal-site").text(result.personalSite);
//   $("#personal-site").attr("href", result.personalSite);
// } else {
//   $("#personal-site").text("Няма въведен сайт");
// }

// if (result.unemployed) {
//   $("#employer").text("Не работи за никого");
// } else if (!result.unemployed) {
//   if (result.employer !== null) {
//     $("#employer").text(result.employer.name);
//   } else {
//     $("#employer").text("Няма въведено работно място");
//   }
// }

// if (result.previousEmployment.length === 0) {
//   $("#previous-employment").text("Няма предишни работни места");
// } else {
//   var previousEmployment = "";
//   for (var i = 0; i < result.previousEmployment.length; i += 1) {
//     previousEmployment += result.previousEmployment[i].name;
//     if (i !== result.previousEmployment.length - 1) {
//       previousEmployment += ", ";
//     }
//   }

//   $("#previous-employment").text(previousEmployment);
// }

// $("#profile-name").text(result.firstName + " " + result.lastName);
// if (result.city !== null) {
//   $("#profile-location").after(" " + result.city.name + ", България");
// } else {
//   $("#profile-location").after(" България");
// }
// $("#profile-occupation").text(result.skillset.name);
// $("#profile-description").text(result.description);

// if (result.skills.length === 0) {
//   $("#profile-skills").after("Няма въведени умения");
// } else {
//   var skills = "";
//   for (var i = 0; i < result.skills.length; i += 1) {
//     skills += result.skills[i].name + ", ";
//   }

//   $("#profile-skills").after(skills);
// }

// if (result.interests.length === 0) {
//   $("#profile-interests").after("Няма въведени интереси");
// } else {
//   var interests = "";
//   for (var i = 0; i < result.interests.length; i += 1) {
//     interests += result.interests[i].name + ", ";
//   }

//   $("#profile-interests").after(interests);
// }

// if (result.degree === null) {
//   $("#profile-education").after("Няма въведено образувание");
// } else {
//   $("#profile-education").after(result.degree);
// }

// if (result.skillset.name === "Програмист") {
//   $("#pills-home").append(
//     '<div class="label-group">Github профил</div><a class="about-a" href="' +
//       result.githubUsername +
//       '">' +
//       result.githubUsername +
//       "</a>"
//   );
// }

// var projects = result.projects;
// if (projects.length !== 0) {
// for (var i = projects.length - 1; i >= 0; i -= 1) {
//   var projectNeeds = "";
//   for (var j = 0; j < projects[i].projectNeeds.length; j++) {
//     projectNeeds += projects[i].projectNeeds[j].name;
//     if (j !== projects[i].projectNeeds.length - 1) {
//       projectNeeds += ", ";
//     }
//   }

//   $("#card-deck-projects").append(
//     $("<div/>")
//       .addClass("col-md-6")
//       .append(
//         $("<div/>")
//           .addClass("card-container")
//           .addClass("manual-flip")
//           .append(
//             $("<div/>")
//               .addClass("card")
//               .append(
//                 $("<div/>")
//                   .addClass("front")
//                   .append(
//                     $("<a/>")
//                       .attr("href", "/project-admin")
//                       .attr("id", "user-cover_" + projects[i].id)
//                       .addClass("should-send-project-id")
//                       .append(
//                         $("<div/>")
//                           .addClass("cover-rotating")
//                           .append(
//                             $("<img/>").attr(
//                               "src",
//                               "data:image/png;base64, " +
//                                 projects[i].cover
//                             )
//                           )
//                       )
//                   )
//                   .append(
//                     $("<div/>")
//                       .addClass("content-rotating")
//                       .append(
//                         $("<div/>")
//                           .addClass("main")
//                           .append(
//                             $("<a/>")
//                               .attr("id", "card-name_" + projects[i].id)
//                               .addClass("should-send-project-id")
//                               .attr("href", "/project-admin")
//                               .addClass("name")
//                               .addClass("card-name")
//                               .text(projects[i].name)
//                           )
//                           .append(
//                             $("<p/>")
//                               .addClass("text-center")
//                               .html(
//                                 '<i class="fas fa-map-marker-alt fa-3x"></i>' +
//                                   projects[i].city.name +
//                                   ", България</p>"
//                               )
//                           )
//                       )
//                       .append(
//                         $("<div/>")
//                           .addClass("footer")
//                           .append(
//                             $("<button/>")
//                               .addClass("btn-rotating")
//                               .addClass("btn-rotating-simple")
//                               .attr("onclick", "rotateCard(this)")
//                               .html(
//                                 '<i class="fa fa-mail-forward"></i> Разбери още'
//                               )
//                           )
//                       )
//                   )
//               )
//               .append(
//                 $("<div/>")
//                   .addClass("back")
//                   .append(
//                     $("<div/>")
//                       .attr("style", "position: relative;")
//                       .addClass("header")
//                       .append(
//                         $("<h3/>")
//                           .addClass("motto")
//                           .text(
//                             projects[i].description.substring(0, 126) +
//                               "..."
//                           )
//                       )
//                   )
//                   .append(
//                     $("<div/>")
//                       .attr(
//                         "style",
//                         "position: relative; margin-top: 75px;"
//                       )
//                       .addClass("content")
//                       .append(
//                         $("<div/>")
//                           .addClass("main")
//                           .append(
//                             $("<h4/>")
//                               .addClass("text-center")
//                               .text("Проектът се нуждае от")
//                           )
//                           .append(
//                             $("<p/>")
//                               .addClass("text-center")
//                               .attr("style", "font-size: 13px;")
//                               .text(projectNeeds)
//                           )
//                       )
//                   )
//                   .append(
//                     $("<div/>")
//                       .addClass("footer")
//                       .append(
//                         $("<button/>")
//                           .addClass("btn-rotating")
//                           .addClass("btn-rotating-simple")
//                           .attr("title", "Обърни картата")
//                           .attr("onclick", "rotateCard(this)")
//                           .html('<i class="fa fa-reply"></i> Back')
//                       )
//                   )
//               )
//           )
//       )
//   );
// }
// } else {
//   $("#pills-profile").append(
//     $("<h2/>")
//       .addClass("text-center")
//       .text("Няма проекти")
//   );
// }

// var groups = result.groupMemberOf;
// if (groups.length !== 0) {
// for (var i = 0; i < groups.length; i += 1) {
//   $("#card-deck-groups-logged").append(
//     $("<div/>")
//       .addClass("col-md-6")
//       .append(
//         $("<div/>")
//           .addClass("card-container")
//           .addClass("manual-flip")
//           .append(
//             $("<div/>")
//               .addClass("card")
//               .append(
//                 $("<div/>")
//                   .addClass("front-group")
//                   .append(
//                     $("<a/>")
//                       .attr("href", "/group/" + groups[i].id)
//                       .attr("id", "user-cover_" + groups[i].id)
//                       .addClass("should-send-group-id")
//                       .append(
//                         $("<div/>")
//                           .addClass("view")
//                           .addClass("overlay")
//                           .addClass("cover-rotating")
//                           .append(
//                             $("<img/>").attr(
//                               "src",
//                               "data:image/png;base64, " + groups[i].logo
//                             )
//                           )
//                           .append(
//                             $("<a/>")
//                               .attr("href", "/group/" + groups[i].id)
//                               .append(
//                                 $("<div/>")
//                                   .addClass("mask")
//                                   .addClass("rgba-white-slight")
//                               )
//                           )
//                       )
//                   )
//                   .append(
//                     $("<div/>")
//                       .addClass("content-rotating")
//                       .append(
//                         $("<div/>")
//                           .addClass("main")
//                           .addClass("text-center")
//                           .append(
//                             $("<a/>")
//                               .attr("id", "card-name_" + groups[i].id)
//                               .addClass("should-send-group-id")
//                               .attr("href", "/group/" + groups[i].id)
//                               .addClass("name")
//                               .text(groups[i].name)
//                           )
//                           .append(
//                             $("<div/>")
//                               .addClass("row")
//                               .addClass("stats-row")
//                               .append(
//                                 $("<div/>")
//                                   .addClass("stat")
//                                   .addClass("has-right-border")
//                                   .addClass("col-md-6")
//                                   .append(
//                                     $("<h4/>").text(
//                                       groups[i].membersCount
//                                     )
//                                   )
//                                   .append($("<p/>").text("Участващи"))
//                               )
//                               .append(
//                                 $("<div/>")
//                                   .addClass("stat")
//                                   .addClass("col-md-6")
//                                   .append(
//                                     $("<h4/>").text(
//                                       groups[i].projectsCount
//                                     )
//                                   )
//                                   .append($("<p/>").text("Проекти"))
//                               )
//                           )
//                       )
//                   )
//               )
//           )
//       )
//   );
// }
// } else {
//   $("#pills-contact").append(
//     $("<h2/>")
//       .addClass("text-center")
//       .text("Няма групи")
//   );
// }

// var notifications = result.notifications;
// $(".counter").text(notifications.length);
// if (notifications.length !== 0) {
// for (var i = notifications.length - 1; i >= 0; i -= 1) {
//   $("#notifications-list").append(
//     $("<li/>")
//       .attr("id", "notification_" + notifications[i].id)
//       .addClass("should-delete-notification")
//       .append(
//         $("<a/>")
//           .attr("href", "#")
//           .addClass("peers")
//           .addClass("fxw-nw")
//           .addClass("td-n")
//           .addClass("p-20")
//           .addClass("bdB")
//           .addClass("c-grey-800")
//           .addClass("cH-blue")
//           .addClass("bgcH-grey-100")
//           .append(
//             $("<div/>")
//               .addClass("peer")
//               .addClass("mR-15")
//               .append(
//                 $("<img/>")
//                   .addClass("w-3r")
//                   .addClass("bdrs-50p")
//                   .attr(
//                     "src",
//                     "data:image/png;base64, " +
//                       notifications[i].notificationSender.picture
//                   )
//               )
//           )
//           .append(
//             $("<div/>")
//               .addClass("peer")
//               .addClass("peer-greed")
//               .append(
//                 $("<span/>")
//                   .addClass("fw-500")
//                   .text(notifications[i].notificationSender.name)
//               )
//               .append(
//                 $("<span/>")
//                   .addClass("c-grey-600")
//                   .text(notifications[i].content)
//               )
//               .append(
//                 $("<p/>")
//                   .addClass("m-0")
//                   .append(
//                     $("<small/>")
//                       .addClass("fsz-xs")
//                       .text(notifications[i].timestamp)
//                   )
//               )
//           )
//       )
//   );
// }
// } else {
//   $("#notifications-list").append(
//     $("<h4/>")
//       .addClass("text-center")
//       .addClass("no-notifications")
//       .html('Няма нови известия <i class="far fa-frown"></i>')
//   );
// }

$(".should-send-group-id").on("click", function() {
  var indexOfUndescore = $(this)
    .attr("id")
    .indexOf("_");
  var groupIdToSend = $(this)
    .attr("id")
    .substring(indexOfUndescore + 1);
  // sessionStorage.setItem("groupIdFromGroupsLogged", groupIdToSend);
});

$(".should-send-project-id").on("click", function() {
  var indexOfUndescore = $(this)
    .attr("id")
    .indexOf("_");
  var projectIdToSend = $(this)
    .attr("id")
    .substring(indexOfUndescore + 1);
  // sessionStorage.setItem("projectId", projectIdToSend);
});

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

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

$.ajax({
  url: "/api/user?id=" + id,
  method: "GET",
  success: function(result) {
    if (result.needsTour) {
      setUpTour(result);
    }

    $(".preloader").fadeOut(500);
  }
});

function setUpTour(result) {
  // Define the tour!
  var tour = {
    id: "welcomeTour",
    i18n: {
      nextBtn: "Напред",
      prevBtn: "Назад",
      doneBtn: "Готово",
      skipBtn: "Пропусни"
    },
    steps: [
      {
        nextOnTargetClick: true,
        title: "Добре дошъл в SquadKnowHow, " + result.firstName + "!",
        content:
          "В следващите няколко стъпки ще ти покажем къде и как да изполваш функционалността на тази платформа за да можеш да използваш пълния й капацитет.",
        target: "profile-name",
        width: "500",
        placement: "top"
      },
      {
        title: "Твоят профил",
        content:
          "Вмомента се намираш в твоя профил. Винаги можеш да се върнеш тук като натиснеш снимката си и след това 'Моят профил'",
        target: "tour-step-2",
        width: "250",
        placement: "bottom"
      },
      {
        title: "Известия",
        content:
          "След като натиснеш тази икона ще моеш да видиш всички твои известия.",
        target: "tour-step-3",
        width: "300",
        placement: "bottom"
      },
      {
        title: "Моите проекти",
        content:
          "В този таб можеш да видиш всички твои активни проект, както и да го управляваш и достъпваш.",
        target: "tour-step-4",
        width: "300",
        nextOnTargetClick: true,
        placement: "bottom"
      },
      {
        title: "Моите съобщения",
        content: "Тук ще откриеш всички входящи и изходящи съобщения.",
        target: "tour-step-5",
        width: "300",
        nextOnTargetClick: true,
        placement: "bottom"
      },
      {
        title: "Групи от потребител",
        content:
          "В този таб можеш да видиш всички групи от потребители и да създадеш или да сеприсъединиш към някоя от тях.",
        target: "tour-step-6",
        width: "300",
        nextOnTargetClick: true,
        placement: "bottom"
      },
      {
        title: "Проекти търсещи партньори",
        content:
          "Тук се виждат всички проекти търсещи възможни партньори. Тук можеш и да създадеш свой собствен проект.",
        target: "tour-step-7",
        width: "300",
        nextOnTargetClick: true,
        placement: "bottom"
      },
      {
        title: "Потребителите",
        content:
          "На това място са показани всички потребители, които са възможни участници в твоите бъдещи проекти.",
        target: "tour-step-8",
        width: "300",
        nextOnTargetClick: true,
        placement: "bottom"
      },
      {
        nextOnTargetClick: true,
        title: "Това беше всичко от нас, " + result.firstName + " !",
        content:
          "Надяваме се, че си разбрал функционалността натази платформа и се надяваме също, че тя ще ти е полезна и ще е от помощ. Не се замисляй ако трябва да се свържеш с нас по какъвто и да е въпрос на следния Е-mail: hristian00i@abv.bg.",
        target: "profile-name",
        width: "550",
        placement: "top"
      }
    ],
    onEnd: function() {
      $.ajax({
        url: "/api/tourCompleted?id=" + id,
        method: "GET",
        success: function(result) {
          if (result) {
            console.log("tourCompleted");
          }
        }
      });
    },
    onClose: function() {}
  };

  // Start the tour!
  hopscotch.startTour(tour);
}
