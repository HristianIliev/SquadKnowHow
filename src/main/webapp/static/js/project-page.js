// var id = sessionStorage.getItem("userId");
// var projectId = sessionStorage.getItem("projectId");

var arr = window.location.pathname.split("/");
var projectId = arr[arr.length - 1];

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

var indexOfUndescoreForCreatorId = $(".preloader")
  .attr("id")
  .indexOf("_");
var creatorId = $(".preloader")
  .attr("id")
  .substring(indexOfUndescoreForCreatorId + 1);

var user = null;

// if (
//   id === "null" ||
//   projectId === "null" ||
//   projectId === null ||
//   id === null
// ) {
//   window.location.replace("/sign-in");
// } else {

$(document).ready(function() {
  addVisit();

  initialiseProjectPage();

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
});

function addVisit() {
  $.ajax({
    url: "/api/addProjectVisit?projectId=" + projectId,
    method: "PUT",
    success: function() {}
  });
}

function initialiseProjectPage() {
  // $.ajax({
  //   url: "/api/project?id=" + projectId,
  //   method: "GET",
  //   success: function(result) {
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
  // var projectNeedsSeparatedWithComma = "";
  // if (result.projectNeeds.length !== 0) {
  // for (var i = 0; i < result.projectNeeds.length; i += 1) {
  //   projectNeedsSeparatedWithComma += result.projectNeeds[i].name + ",";
  // $(".project-needs").append(
  //   $("<li/>").text(result.projectNeeds[i].name)
  // );
  // }
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

  $(".should-send-profile-id").on("click", function(event) {
    event.preventDefault();
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var userIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);
    // sessionStorage.setItem("profileIdFromPeopleLogged", userIdToSend);

    if (id === userIdToSend) {
      window.location.href = "/profile";
    }

    window.location.href = "/user/" + result.projectMembers[i].id;
  });

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

  $("#join-project").click(function() {
    var instance = $(this);
    $.confirm({
      title: "Потвърдете присъединяването",
      content: "Сигурни ли сте, че искате да се присъедините към този проект?",
      theme: "supervan",
      buttons: {
        Да: {
          btnClass: "btn-blue",
          action: function() {
            instance.html(
              '<i class="fa fa-thumbs-up"></i> Присъедини се към проекта <i class="fas fa-circle-notch fa-spin"></i>'
            );
            // if (
            //   projectNeedsSeparatedWithComma.includes(user.skillset.name)
            // ) {
            $.ajax({
              url:
                "/api/sendMessageForApproval?projectId=" +
                projectId +
                "&newMemberId=" +
                id +
                "&creatorId=" +
                creatorId,
              method: "GET",
              success: function(result) {
                instance.html(
                  '<i class="fa fa-thumbs-up"></i> Присъедини се към проекта'
                );
                if (result.successfull) {
                  iziToast.success({
                    title: "ОК!",
                    message: "Запитването беше изпратено успешно",
                    position: "topRight"
                  });
                } else {
                  iziToast.error({
                    title: "Грешка",
                    message:
                      "Този проект не се нуждае от потребител от вашата категория от способности",
                    position: "topRight"
                  });
                }
              }
            });
            // } else {

            // }
          }
        },
        Не: {
          btnClass: "btn-blue",
          action: function() {
            iziToast.warning({
              title: "Отказ",
              message: "Запитването не беше изпратено",
              position: "topRight"
            });
          }
        }
      }
    });
  });

  $("#send-advice").click(function() {
    var instance = $(this);
    vex.dialog.open({
      message: "Напиши съветът, който искаш да изпратиш:",
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
            '<i class="fa fa-stack-exchange"></i> Дай съвет <i class="fas fa-circle-notch fa-spin"></i>'
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
            url: "/api/sendAdvice",
            method: "POST",
            data: JSON.stringify({
              topic: data.topic,
              content: data.content,
              timestamp: timestamp,
              senderId: id,
              projectId: projectId
            }),
            contentType: "application/json",
            success: function(result) {
              instance.html('<i class="fa fa-stack-exchange"></i> Дай съвет');
              if (result.successfull) {
                iziToast.success({
                  title: "ОК!",
                  message: "Съветът беше изпратен успешно",
                  position: "topRight"
                });
              } else {
                iziToast.warning({
                  title: "Грешка",
                  message: "Съветът не беше изпратен успешно",
                  position: "topRight"
                });
              }
            }
          });
        }
      }
    });
  });

  $("#send-question").click(function() {
    var instance = $(this);
    vex.dialog.open({
      message: "Напиши въпросът, който искаш да попиташ:",
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
            '<i class="fa fa-question"></i> Попитай въпрос <i class="fas fa-circle-notch fa-spin"></i>'
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
            url: "/api/sendQuestion",
            method: "POST",
            data: JSON.stringify({
              topic: data.topic,
              content: data.content,
              timestamp: timestamp,
              senderId: id,
              projectId: projectId
            }),
            contentType: "application/json",
            success: function(result) {
              instance.html('<i class="fa fa-question"></i> Попитай въпрос');
              if (result.successfull) {
                iziToast.success({
                  title: "ОК!",
                  message: "Въпросът беше изпратен успешно",
                  position: "topRight"
                });
              } else {
                iziToast.warning({
                  title: "Грешка",
                  message: "Въпросът не беше изпратен успешно",
                  position: "topRight"
                });
              }
            }
          });
        }
      }
    });
  });
  //   }
  // });

  $(".preloader").fadeOut(500);
}
// }
