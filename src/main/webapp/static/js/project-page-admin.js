// var id = sessionStorage.getItem("userId");
// var projectId = sessionStorage.getItem("projectId");

// if (
//   id === "null" ||
//   projectId === "null" ||
//   projectId === null ||
//   id === null
// ) {
//   window.location.replace("/sign-in");
// } else {
var arr = window.location.pathname.split("/");
var projectId = arr[arr.length - 1];

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

// var user = null;
// $.ajax({
//   url: "/api/user?id=" + id,
//   method: "GET",
//   success: function(result) {
//     user = result;
//     $("#profile-image-navbar").attr(
//       "src",
//       "data:image/png;base64, " + user.image
//     );

//     $("body").attr("id", projectId);
//   }
// });

setUpNotifications();

initialiseProjectPage();

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

function initialiseProjectPage() {
  // $(".fb-share-button").attr(
  //   "data-href",
  //   "https://squadknowhow.com/project/" + projectId
  // );
  // $(".fb-share-button").attr(
  //   "fb-iframe-plugin-query",
  //   "app_id=&container_width=0&href=https%3A%2F%2Fsquadknowhow.com%2Fproject%2F" +
  //     projectId +
  //     "&layout=button_count&locale=de_DE&mobile_iframe=true&sdk=joey&size=large"
  // );
  // $('[name="fc31f6aa09cbc4"]').attr("data-original-title", "Share to Facebook");

  $.ajax({
    url: "/api/project?id=" + projectId,
    method: "GET",
    success: function(result) {
      // Name
      // $("#project-name").text(result.name);

      // Cover photo
      // $("#project-cover-photo-a").attr(
      //   "href",
      //   "data:image/png;base64, " + result.cover
      // );
      // $("#project-cover-photo-img").attr(
      //   "src",
      //   "data:image/png;base64, " + result.cover
      // );

      // Photo 1
      // $("#project-picture-1-a").attr(
      //   "href",
      //   "data:image/png;base64, " + result.picture1
      // );
      // $("#project-picture-1-img").attr(
      //   "src",
      //   "data:image/png;base64, " + result.picture1
      // );

      // Photo 2
      // if (result.picture2 !== null) {
      //   $(".project-picture-1").after(
      //     $("<div/>")
      //       .addClass("col-md-2")
      //       .addClass("project-picture-2")
      //       .append(
      //         $("<a/>")
      //           .attr("href", "data:image/png;base64, " + result.picture2)
      //           .attr("data-lightbox", "image-1")
      //           .attr("data-title", "My caption")
      //           .append(
      //             $("<img/>")
      //               .addClass("z-depth-3")
      //               .addClass("img-thumbnail")
      //               .addClass("smaller-picture")
      //               .attr("data-lightbox", "image-1")
      //               .attr("data-title", "My caption")
      //               .attr("src", "data:image/png;base64, " + result.picture2)
      //           )
      //       )
      //   );
      // }

      // Photo 3
      // if (result.picture3 !== null) {
      //   $(".project-picture-2").after(
      //     $("<div/>")
      //       .addClass("col-md-2")
      //       .addClass("project-picture-3")
      //       .append(
      //         $("<a/>")
      //           .attr("href", "data:image/png;base64, " + result.picture3)
      //           .attr("data-lightbox", "image-1")
      //           .attr("data-title", "My caption")
      //           .append(
      //             $("<img/>")
      //               .addClass("z-depth-3")
      //               .addClass("img-thumbnail")
      //               .addClass("smaller-picture")
      //               .attr("data-lightbox", "image-1")
      //               .attr("data-title", "My caption")
      //               .attr("src", "data:image/png;base64, " + result.picture3)
      //           )
      //       )
      //   );
      // }

      // Photo 4
      // if (result.picture4 !== null) {
      //   $(".project-picture-3").after(
      //     $("<div/>")
      //       .addClass("col-md-2")
      //       .addClass("project-picture-4")
      //       .append(
      //         $("<a/>")
      //           .attr("href", "data:image/png;base64, " + result.picture4)
      //           .attr("data-lightbox", "image-1")
      //           .attr("data-title", "My caption")
      //           .append(
      //             $("<img/>")
      //               .addClass("z-depth-3")
      //               .addClass("img-thumbnail")
      //               .addClass("smaller-picture")
      //               .attr("data-lightbox", "image-1")
      //               .attr("data-title", "My caption")
      //               .attr("src", "data:image/png;base64, " + result.picture4)
      //           )
      //       )
      //   );
      // }

      // Photo 5
      // if (result.picture5 !== null) {
      //   $(".project-picture-4").after(
      //     $("<div/>")
      //       .addClass("col-md-2")
      //       .addClass("project-picture-5")
      //       .append(
      //         $("<a/>")
      //           .attr("href", "data:image/png;base64, " + result.picture5)
      //           .attr("data-lightbox", "image-1")
      //           .attr("data-title", "My caption")
      //           .append(
      //             $("<img/>")
      //               .addClass("z-depth-3")
      //               .addClass("img-thumbnail")
      //               .addClass("smaller-picture")
      //               .attr("data-lightbox", "image-1")
      //               .attr("data-title", "My caption")
      //               .attr("src", "data:image/png;base64, " + result.picture5)
      //           )
      //       )
      //   );
      // }

      // Photo 6
      // if (result.picture6 !== null) {
      //   $(".project-picture-5").after(
      //     $("<div/>")
      //       .addClass("col-md-2")
      //       .addClass("project-picture-6")
      //       .append(
      //         $("<a/>")
      //           .attr("href", "data:image/png;base64, " + result.picture6)
      //           .attr("data-lightbox", "image-1")
      //           .attr("data-title", "My caption")
      //           .append(
      //             $("<img/>")
      //               .addClass("z-depth-3")
      //               .addClass("img-thumbnail")
      //               .addClass("smaller-picture")
      //               .attr("data-lightbox", "image-1")
      //               .attr("data-title", "My caption")
      //               .attr("src", "data:image/png;base64, " + result.picture6)
      //           )
      //       )
      //   );
      // }

      // Photo 7
      // if (result.picture7 !== null) {
      //   $(".project-picture-6").after(
      //     $("<div/>")
      //       .addClass("col-md-2")
      //       .addClass("project-picture-7")
      //       .append(
      //         $("<a/>")
      //           .attr("href", "data:image/png;base64, " + result.picture7)
      //           .attr("data-lightbox", "image-1")
      //           .attr("data-title", "My caption")
      //           .append(
      //             $("<img/>")
      //               .addClass("z-depth-3")
      //               .addClass("img-thumbnail")
      //               .addClass("smaller-picture")
      //               .attr("data-lightbox", "image-1")
      //               .attr("data-title", "My caption")
      //               .attr("src", "data:image/png;base64, " + result.picture7)
      //           )
      //       )
      //   );
      // }

      // Photo 8
      // if (result.picture8 !== null) {
      //   $(".project-picture-7").after(
      //     $("<div/>")
      //       .addClass("col-md-2")
      //       .addClass("project-picture-8")
      //       .append(
      //         $("<a/>")
      //           .attr("href", "data:image/png;base64, " + result.picture8)
      //           .attr("data-lightbox", "image-1")
      //           .attr("data-title", "My caption")
      //           .append(
      //             $("<img/>")
      //               .addClass("z-depth-3")
      //               .addClass("img-thumbnail")
      //               .addClass("smaller-picture")
      //               .attr("data-lightbox", "image-1")
      //               .attr("data-title", "My caption")
      //               .attr("src", "data:image/png;base64, " + result.picture8)
      //           )
      //       )
      //   );
      // }

      // Photo 9
      // if (result.picture9 !== null) {
      //   $(".project-picture-8").after(
      //     $("<div/>")
      //       .addClass("col-md-2")
      //       .addClass("project-picture-9")
      //       .append(
      //         $("<a/>")
      //           .attr("href", "data:image/png;base64, " + result.picture9)
      //           .attr("data-lightbox", "image-1")
      //           .attr("data-title", "My caption")
      //           .append(
      //             $("<img/>")
      //               .addClass("z-depth-3")
      //               .addClass("img-thumbnail")
      //               .addClass("smaller-picture")
      //               .attr("data-lightbox", "image-1")
      //               .attr("data-title", "My caption")
      //               .attr("src", "data:image/png;base64, " + result.picture9)
      //           )
      //       )
      //   );
      // }

      // Project Needs
      // if (result.projectNeeds.length !== 0) {
      //   for (var i = 0; i < result.projectNeeds.length; i += 1) {
      //     $(".project-needs").append(
      //       $("<li/>").text(result.projectNeeds[i].name)
      //     );
      //   }
      // } else {
      //   $(".project-needs").text("Този проект не се нуждае от други хора");
      // }

      // Members
      // for (var i = 0; i < result.projectMembers.length; i += 1) {
      //   $(".project-members").append(
      //     $("<li/>").append(
      //       $("<a/>")
      //         .addClass("about-a")
      //         .attr("id", "profile-id_" + result.projectMembers[i].id)
      //         .addClass("should-send-profile-id")
      //         .attr("href", "/user/" + result.projectMembers[i].id)
      //         .text(result.projectMembers[i].name)
      //     )
      //   );
      // }

      // Description
      // if (
      //   result.githubPage !== "" &&
      //   result.githubPage !== null &&
      //   result.githubPage !== " "
      // ) {
      //   $(".project-description")
      //     .append($("<h4/>").text("Описание на проекта"))
      //     .text(result.description)
      //     .append($("<hr/>").addClass("divider"))
      //     .append($("<h4/>").text("Цели на проекта:"))
      //     .append($("<ul/>").addClass("project-goals"))
      //     .append($("<hr/>").addClass("divider"))
      //     .append($("<h4/>").text("Github страница:"))
      //     .append(
      //       $("<a/>")
      //         .addClass("about-a")
      //         .attr("href", result.githubPage)
      //         .text(result.githubPage)
      //     )
      //     .append($("<hr/>").addClass("divider"))
      //     .append(
      //       $("<h4/>")
      //         .addClass("project-video-link")
      //         .text("Линк към видео на проекта:")
      //     );
      // } else {
      //   $(".project-description")
      //     .append($("<h4/>").text("Описание на проекта"))
      //     .text(result.description)
      //     .append($("<hr/>").addClass("divider"))
      //     .append($("<h4/>").text("Цели на проекта:"))
      //     .append($("<ul/>").addClass("project-goals"))
      //     .append($("<hr/>").addClass("divider"))
      //     .append(
      //       $("<h4/>")
      //         .addClass("project-video-link")
      //         .text("Линк към видео на проекта:")
      //     );
      // }

      // Project goals
      // $(".project-goals").append($("<li/>").text(result.goal1));

      // if (result.goal2 !== "") {
      //   $(".project-goals").append($("<li/>").text(result.goal2));
      // }

      // if (result.goal3 !== "") {
      //   $(".project-goals").append($("<li/>").text(result.goal3));
      // }

      // Youtube link
      // if (result.youtubeLink !== "") {
      //   $(".project-description").append(
      //     $("<a/>")
      //       .addClass("about-a")
      //       .attr("href", result.youtubeLink)
      //       .text(result.youtubeLink)
      //       .attr("style", "margin-bottom: 20px;")
      //   );
      // } else {
      //   $(".project-description").append(
      //     "Няма въведен линк към видео на проекта"
      //   );
      // }

      // Visits
      // $("#visits-count").text(result.visits);

      // Project Members Table
      // for (var i = 0; i < result.projectMembers.length; i += 1) {
      //   if (result.projectMembers[i].id != id) {
      //     $(".project-members-table").append(
      //       $("<tr/>")
      //         .append(
      //           $("<th/>")
      //             .attr("scope", "row")
      //             .text(i + 1)
      //         )
      //         .append($("<td/>").text(result.projectMembers[i].firstName))
      //         .append($("<td/>").text(result.projectMembers[i].lastName))
      //         .append($("<td/>").text(result.projectMembers[i].skillset))
      //         .append(
      //           $("<td/>")
      //             .append(
      //               $("<a/>")
      //                 .addClass("about-a")
      //                 .attr("href", "mailto:" + result.projectMembers[i].email)
      //                 .text(result.projectMembers[i].email)
      //             )
      //             .append(
      //               $("<i/>")
      //                 .addClass("fas")
      //                 .addClass("fa-pencil-alt")
      //                 .addClass("ml-1")
      //                 .addClass("trigger-modal")
      //                 .attr("id", result.projectMembers[i].id)
      //                 .attr(
      //                   "title",
      //                   "Натисни тази икона за да напишеш съобщение до потребителя"
      //                 )
      //             )
      //         )
      //     );
      //   } else {
      //     $(".project-members-table").append(
      //       $("<tr/>")
      //         .append(
      //           $("<th/>")
      //             .attr("scope", "row")
      //             .text(i + 1)
      //         )
      //         .append($("<td/>").text(result.projectMembers[i].firstName))
      //         .append($("<td/>").text(result.projectMembers[i].lastName))
      //         .append($("<td/>").text(result.projectMembers[i].skillset))
      //         .append($("<td/>").text("Ти"))
      //     );
      //   }
      // }

      // Project Advices
      // if (result.advices.length !== 0) {
      // for (var i = 0; i < result.advices.length; i += 1) {
      //   $(".advices-project-list").append(
      //     $("<li/>")
      //       .addClass("mdl-list__item mdl-list__item--three-line")
      //       .append(
      //         $("<span/>")
      //           .addClass("mdl-list__item-primary-content")
      //           .append(
      //             $("<i/>")
      //               .addClass("material-icons")
      //               .addClass("mdl-list__item-avatar")
      //               .text("person")
      //           )
      //           .append(
      //             $("<span/>")
      //               .html(
      //                 '<b class="blue-text-my">' +
      //                   result.advices[i].adviceSender.name +
      //                   ": </b>" +
      //                   result.advices[i].title
      //               )
      //               .attr(
      //                 "id",
      //                 "profile-id_" + result.advices[i].adviceSender.id
      //               )
      //               .addClass("should-send-profile-id")
      //           )
      //           .append(
      //             $("<span/>")
      //               .addClass("mdl-list__item-text-body")
      //               .text(result.advices[i].content)
      //           )
      //       )
      //       .append(
      //         $("<span/>")
      //           .addClass("mdl-list__item-secondary-content")
      //           .text(result.advices[i].timestamp)
      //       )
      //   );
      // }
      // } else {
      //   $(".advices-project-list").append(
      //     $("<h4/>")
      //       .addClass("text-center")
      //       .text("За момента няма никакви съвети -_-")
      //   );
      // }

      $(".should-send-profile-id").on("click", function(event) {
        event.preventDefault();
        var indexOfUndescore = $(this)
          .attr("id")
          .indexOf("_");
        var userIdToSend = $(this)
          .attr("id")
          .substring(indexOfUndescore + 1);
        // sessionStorage.setItem("profileIdFromPeopleLogged", userIdToSend);
        window.location.href = "/user/" + userIdToSend;
      });

      // Project questions
      // if (result.questions.length !== 0) {
      // for (var i = 0; i < result.questions.length; i += 1) {
      //   $(".questions-project-list").append(
      //     $("<li/>")
      //       .addClass("mdl-list__item")
      //       .addClass("mdl-list__item--three-line")
      //       .append(
      //         $("<span/>")
      //           .addClass("mdl-list__item-primary-content")
      //           .append(
      //             $("<i/>")
      //               .addClass("material-icons")
      //               .addClass("mdl-list__item-avatar")
      //               .text("person")
      //           )
      //           .append(
      //             $("<span/>").html(
      //               "<b>" +
      //                 result.questions[i].questionSender.name +
      //                 ": </b>" +
      //                 result.questions[i].title
      //             )
      //           )
      //           .append(
      //             $("<span/>")
      //               .addClass("mdl-list__item-text-body")
      //               .text(result.questions[i].content)
      //           )
      //       )
      //       .append(
      //         $("<span/>")
      //           .addClass("mdl-list__item-secondary-content")
      //           .text(result.questions[i].timestamp)
      //           .append($("<br/>"))
      //           .append($("<br/>"))
      //           .append(
      //             $("<i/>")
      //               .addClass("trigger-modal")
      //               .addClass("fas")
      //               .addClass("fa-reply")
      //               .addClass("fa-2x")
      //               .attr("id", result.questions[i].questionSender.id)
      //               .attr(
      //                 "title",
      //                 "Натисни тази икона за да отговориш на този въпрос"
      //               )
      //           )
      //       )
      //   );
      // }
      // } else {
      //   $(".questions-project-list").append(
      //     $("<h4/>")
      //       .addClass("text-center")
      //       .text("За момента няма никакви въпроси -_-")
      //   );
      // }

      $(".trigger-modal").click(function(e) {
        var recipientId = $(this).attr("id");

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
                  senderId: id,
                  recipientId: recipientId,
                  kind: "normal"
                }),
                contentType: "application/json",
                success: function(result) {
                  if (result.successfull) {
                    iziToast.success({
                      title: "ОК!",
                      message: "Съобщениета беше изпратено успешно",
                      position: "topRight"
                    });
                  } else {
                    iziToast.warning({
                      title: "Грешка",
                      message: "Съобщениета не беше изпратено успешно",
                      position: "topRight"
                    });
                  }
                }
              });
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

      setUpTODOList(result);

      $(".preloader").fadeOut(500);
    }
  });
}

function setUpTODOList(result) {
  $(".lobilist").lobiList({
    sortable: false,
    controls: [],
    enableTodoEdit: false,
    lists: [
      {
        id: "todoList_" + projectId,
        title: "Задачи върху проекта:",
        defaultStyle: "lobilist-info",
        items: []
      }
    ],
    afterItemDelete: function(list, item) {
      $.ajax({
        url: "/api/deleteItem?itemId=" + item.id,
        method: "DELETE",
        success: function(result) {
          if (result) {
          }
        }
      });
    },
    afterMarkAsDone: function(list, checkbox) {
      var itemId = $(checkbox)
        .parent()
        .parent()
        .attr("data-id");
      var itemTitle = $(
        "li[data-id='" + itemId + "'] > .lobilist-item-title"
      ).text();
      $.ajax({
        url: "/api/markListItemAsDone?itemTitle=" + itemTitle,
        method: "GET",
        success: function(result) {
          if (result) {
          }
        }
      });
    },
    afterMarkAsUndone: function(list, checkbox) {
      var itemId = $(checkbox)
        .parent()
        .parent()
        .attr("data-id");
      var itemTitle = $(
        "li[data-id='" + itemId + "'] > .lobilist-item-title"
      ).text();
      $.ajax({
        url: "/api/markListItemAsUndone?itemTitle=" + itemTitle,
        method: "GET",
        success: function(result) {
          if (result) {
          }
        }
      });
    }
  });

  $(".btn-add-todo").click(function() {
    var title = $("input[name='title']");
    var description = $("textarea[name='description']");
    var dueDate = $("input[name='dueDate']");
    if (title.val().length !== 0) {
      $.ajax({
        url: "/api/createItem",
        method: "POST",
        data: JSON.stringify({
          title: title.val(),
          description: description.val(),
          dueDate: dueDate.val(),
          projectId: projectId
        }),
        contentType: "application/json",
        success: function(result) {
          if (result) {
          }
        }
      });
    }
  });

  var $list = $("#todoList_" + projectId).data("lobiList");
  var listEntries = result.listEntries;
  if (listEntries !== null) {
    if (listEntries.length !== 0) {
      for (var i = listEntries.length - 1; i >= 0; i -= 1) {
        $list.addItem({
          id: listEntries[i].id,
          title: listEntries[i].title,
          description: listEntries[i].description,
          dueDate: listEntries[i].dueDate,
          done: listEntries[i].done
        });
      }
    }
  }
}

$("#button-send-all-message").click(function(e) {
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
        instance.html('<i class="fa fa-comments"></i> Изпрати съобщение на всички <i class="fas fa-circle-notch fa-spin"></i>');
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
          url: "/api/sendMessageToAllMembers?projectId=" + projectId,
          method: "POST",
          data: JSON.stringify({
            topic: data.topic,
            content: data.content,
            timestamp: timestamp,
            senderId: id,
            recipientId: -1,
            kind: "normal"
          }),
          contentType: "application/json",
          success: function(result) {
            instance.html('<i class="fa fa-comments"></i> Изпрати съобщение на всички');
            if (result.successfull) {
              iziToast.success({
                title: "ОК!",
                message: "Съобщенията бяха изпратени успешно",
                position: "topRight"
              });
            } else {
              iziToast.warning({
                title: "Грешка",
                message: "Съобщенията не бяха изпратени успешно",
                position: "topRight"
              });
            }
          }
        });
      }
    }
  });
});

$("#pills-home-tab").click(function(e) {
  $(this).tab("show");
});

$("#pills-profile-tab").click(function(e) {
  $(this).tab("show");
});

$("#pills-calendar-tab").click(function(e) {
  $(this).tab("show");
});
// }
