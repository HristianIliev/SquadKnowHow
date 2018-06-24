// var idForUser = sessionStorage.getItem("profileIdFromPeopleLogged");
// var idForProfilePicture = sessionStorage.getItem("userId");
var user = null;

var arr = window.location.pathname.split("/");
var idForUser = arr[arr.length - 1];

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var idForProfilePicture = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

// if (
//   idForProfilePicture === "null" ||
//   idForUser === "null" ||
//   idForUser === null ||
//   idForProfilePicture === null
// ) {
//   window.location.replace("/sign-in");
// } else {
// $.ajax({
//   url: "/api/user?id=" + idForProfilePicture,
//   method: "GET",
//   success: function(result) {
// user = result;
// $("#profile-image-navbar").attr(
//   "src",
//   "data:image/png;base64, " + user.image
// );
//   }
// });

getProfileData();

setUpNotifications();

function setUpNotifications() {
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

// if (idForUser === idForProfilePicture) {
//   $(".btn-send-message-container").remove();
// }

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
  //HERE
  // $.ajax({
  //   url: "/api/user?id=" + idForUser,
  //   method: "GET",
  //   success: function(result) {
  // $("#profile-image").attr("src", "data:image/png;base64, " + result.image);
  // if (result.city !== null) {
  //   $("#location").text(result.city.name + ", България");
  // } else {
  //   $("#location").text("България");
  // }
  // $("#email").text(result.email);
  // $("#email").attr("href", "mailto:" + result.email);
  // if (result.personalSite !== "") {
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
  //   if (result.skillset.name.length === 0) {
  //     $("#pills-home").append(
  //       '<div class="label-group">Github профил</div>Няма въведен Github профил.'
  //     );
  //   } else {
  //     $("#pills-home").append(
  //       '<div class="label-group">Github профил</div><a class="about-a" href="' +
  //         result.githubUsername +
  //         '">' +
  //         result.githubUsername +
  //         "</a>"
  //     );
  //   }
  // } else {
  //   if (result.workSample.length === 0) {
  //     $("#pills-home").append(
  //       '<div class="label-group">Линк към проект</div>Няма въведен линк към проект.'
  //     );
  //   } else {
  //     $("#pills-home").append(
  //       '<div class="label-group">Линк към проект</div><a class="about-a" href="' +
  //         result.workSample +
  //         '">' +
  //         result.workSample +
  //         "</a>"
  //     );
  //   }
  // }

  // var projects = result.projects;
  // if (projects.length !== 0) {
  //   for (var i = projects.length - 1; i >= 0; i -= 1) {
  //     var projectNeeds = "";
  //     for (var j = 0; j < projects[i].projectNeeds.length; j++) {
  //       projectNeeds += projects[i].projectNeeds[j].name;
  //       if (j !== projects[i].projectNeeds.length - 1) {
  //         projectNeeds += ", ";
  //       }
  //     }

  //     $("#card-deck-projects").append(
  //       $("<div/>")
  //         .addClass("col-md-6")
  //         .append(
  //           $("<div/>")
  //             .addClass("card-container")
  //             .addClass("manual-flip")
  //             .append(
  //               $("<div/>")
  //                 .addClass("card")
  //                 .append(
  //                   $("<div/>")
  //                     .attr("id", "creator-id2_" + projects[i].creator)
  //                     .addClass("front")
  //                     .append(
  //                       $("<a/>")
  //                         .attr("id", "user-cover_" + projects[i].id)
  //                         .addClass("should-send-project-id")
  //                         .append(
  //                           $("<div/>")
  //                             .addClass("cover-rotating")
  //                             .append(
  //                               $("<img/>").attr(
  //                                 "src",
  //                                 "data:image/png;base64, " +
  //                                   projects[i].cover
  //                               )
  //                             )
  //                         )
  //                     )
  //                     .append(
  //                       $("<div/>")
  //                         .addClass("content-rotating")
  //                         .append(
  //                           $("<div/>")
  //                             .attr(
  //                               "id",
  //                               "creator-id2_" + projects[i].creator
  //                             )
  //                             .addClass("main")
  //                             .append(
  //                               $("<a/>")
  //                                 .attr("id", "card-name_" + projects[i].id)
  //                                 .addClass("should-send-project-id")
  //                                 .attr(
  //                                   "href",
  //                                   "/project/" + projects[i].id
  //                                 )
  //                                 .addClass("name")
  //                                 .addClass("card-name")
  //                                 .text(projects[i].name)
  //                             )
  //                             .append(
  //                               $("<p/>")
  //                                 .addClass("text-center")
  //                                 .html(
  //                                   '<i class="fa fa-map-marker fa-3x"></i>' +
  //                                     projects[i].city.name +
  //                                     ", България</p>"
  //                                 )
  //                             )
  //                         )
  //                         .append(
  //                           $("<div/>")
  //                             .addClass("footer")
  //                             .append(
  //                               $("<button/>")
  //                                 .addClass("btn-rotating")
  //                                 .addClass("btn-rotating-simple")
  //                                 .attr("onclick", "rotateCard(this)")
  //                                 .html(
  //                                   '<i class="fa fa-mail-forward"></i> Разбери още'
  //                                 )
  //                             )
  //                         )
  //                     )
  //                 )
  //                 .append(
  //                   $("<div/>")
  //                     .addClass("back")
  //                     .append(
  //                       $("<div/>")
  //                         .attr("style", "position: relative;")
  //                         .addClass("header")
  //                         .append(
  //                           $("<h3/>")
  //                             .addClass("motto")
  //                             .text(
  //                               projects[i].description.substring(0, 126) +
  //                                 "..."
  //                             )
  //                         )
  //                     )
  //                     .append(
  //                       $("<div/>")
  //                         .attr(
  //                           "style",
  //                           "position: relative; margin-top: 75px;"
  //                         )
  //                         .addClass("content")
  //                         .append(
  //                           $("<div/>")
  //                             .addClass("main")
  //                             .append(
  //                               $("<h4/>")
  //                                 .addClass("text-center")
  //                                 .text("Проектът се нуждае от")
  //                             )
  //                             .append(
  //                               $("<p/>")
  //                                 .addClass("text-center")
  //                                 .attr("style", "font-size: 13px;")
  //                                 .text(projectNeeds)
  //                             )
  //                         )
  //                     )
  //                     .append(
  //                       $("<div/>")
  //                         .addClass("footer")
  //                         .append(
  //                           $("<button/>")
  //                             .addClass("btn-rotating")
  //                             .addClass("btn-rotating-simple")
  //                             .attr("title", "Обърни картата")
  //                             .attr("onclick", "rotateCard(this)")
  //                             .html('<i class="fa fa-reply"></i> Back')
  //                         )
  //                     )
  //                 )
  //             )
  //         )
  //     );
  //   }
  // } else {
  //   $("#pills-profile").append(
  //     $("<h2/>")
  //       .addClass("text-center")
  //       .text("Няма проекти")
  //   );
  // }

  // var groups = result.groupMemberOf;
  // if (groups.length !== 0) {
  //   for (var i = 0; i < groups.length; i += 1) {
  //     $("#card-deck-groups-logged")
  //       .addClass("animated")
  //       .addClass("zoomIn")
  //       .append(
  //         $("<div/>")
  //           .addClass("col-md-6")
  //           .append(
  //             $("<div/>")
  //               .addClass("card-container")
  //               .addClass("manual-flip")
  //               .append(
  //                 $("<div/>")
  //                   .addClass("card")
  //                   .append(
  //                     $("<div/>")
  //                       .addClass("front-group")
  //                       .append(
  //                         $("<a/>")
  //                           .attr("href", "/group/" + groups[i].id)
  //                           .attr("id", "user-cover_" + groups[i].id)
  //                           .addClass("should-send-group-id")
  //                           .append(
  //                             $("<div/>")
  //                               .addClass("view")
  //                               .addClass("overlay")
  //                               .addClass("cover-rotating")
  //                               .append(
  //                                 $("<img/>").attr(
  //                                   "src",
  //                                   "data:image/png;base64, " +
  //                                     groups[i].logo
  //                                 )
  //                               )
  //                               .append(
  //                                 $("<a/>")
  //                                   .attr("href", "/group/" + groups[i].id)
  //                                   .append(
  //                                     $("<div/>")
  //                                       .addClass("mask")
  //                                       .addClass("rgba-white-slight")
  //                                   )
  //                               )
  //                           )
  //                       )
  //                       .append(
  //                         $("<div/>")
  //                           .addClass("content-rotating")
  //                           .append(
  //                             $("<div/>")
  //                               .addClass("main")
  //                               .addClass("text-center")
  //                               .append(
  //                                 $("<a/>")
  //                                   .attr("id", "card-name_" + groups[i].id)
  //                                   .addClass("should-send-group-id")
  //                                   .attr("href", "/group/" + groups[i].id)
  //                                   .addClass("name")
  //                                   .text(groups[i].name)
  //                               )
  //                               .append(
  //                                 $("<div/>")
  //                                   .addClass("row")
  //                                   .addClass("stats-row")
  //                                   .append(
  //                                     $("<div/>")
  //                                       .addClass("stat")
  //                                       .addClass("has-right-border")
  //                                       .addClass("col-md-6")
  //                                       .append(
  //                                         $("<h4/>").text(
  //                                           groups[i].membersCount
  //                                         )
  //                                       )
  //                                       .append($("<p/>").text("Участващи"))
  //                                   )
  //                                   .append(
  //                                     $("<div/>")
  //                                       .addClass("stat")
  //                                       .addClass("col-md-6")
  //                                       .append(
  //                                         $("<h4/>").text(
  //                                           groups[i].projectsCount
  //                                         )
  //                                       )
  //                                       .append($("<p/>").text("Проекти"))
  //                                   )
  //                               )
  //                           )
  //                       )
  //                   )
  //               )
  //           )
  //       );
  //   }
  // } else {
  //   $("#pills-contact").append(
  //     $("<h2/>")
  //       .addClass("text-center")
  //       .text("Няма групи")
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
    // sessionStorage.setItem("projectId", projectIdToSend);
  });

  $(".preloader").fadeOut(500);
}
//   });
// }
// }
