if ($(window).width() < 1100) {
  $(".smaller-collumn").attr("style", "padding-left: 35px;");
}

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

// GOOGLE MAPS SETUP
var map;

function initMap() {
  $.ajax({
    url: "/api/project?id=" + projectId,
    method: "GET",
    success: function(result) {
      map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: -34.397, lng: 150.644 },
        zoom: 14
      });

      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
          initialLocation = new google.maps.LatLng(
            position.coords.latitude,
            position.coords.longitude
          );

          var marker = new google.maps.Marker({
            position: initialLocation,
            icon: "/static/icons/user-map-icon-2.png",
            map: map
          });
        });
      }

      var pos = {
        lat: result.latitude,
        lng: result.longitude
      };

      var arena = new google.maps.Marker({
        position: pos,
        map: map,
        draggable: true
      });

      map.setCenter({ lat: pos.lat, lng: pos.lng });

      //   GETS THE ADDRESS HUMAN READABLE
      var geocoder = new google.maps.Geocoder();
      geocoder.geocode({ location: pos }, function(results, status) {
        if (status === "OK") {
          if (results[0]) {
            $(".arena-address").text(results[0].formatted_address);
          } else {
            window.alert("No results found");
          }
        } else {
          window.alert("Geocoder failed due to: " + status);
        }
      });
    }
  });
}

$(document).ready(function() {
  $(".heart").on("click", function() {
    $(this).toggleClass("is-active");
    if ($(this).hasClass("is-active")) {
      $.ajax({
        url: "/api/addFavorite?userId=" + id + "&projectId=" + projectId,
        method: "GET",
        success: function(result) {}
      });
    } else {
      $.ajax({
        url: "/api/removeFavorite?userId=" + id + "&projectId=" + projectId,
        method: "GET",
        success: function(result) {}
      });
    }
  });

  addVisit();

  initialiseProjectPage();
});

function addVisit() {
  $.ajax({
    url: "/api/addProjectVisit?projectId=" + projectId,
    method: "PUT",
    success: function() {}
  });
}

function initialiseProjectPage() {
  $(".should-send-profile-id").on("click", function(event) {
    event.preventDefault();
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var userIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);

    if (id === userIdToSend) {
      window.location.href = "/profile";
    }

    window.location.href = "/user/" + userIdToSend;
  });

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
        } else {
          instance.html(
            '<i class="fa fa-stack-exchange"></i> Дай съвет <i class="fas fa-circle-notch fa-spin"></i>'
          );

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
        } else {
          instance.html(
            '<i class="fa fa-question"></i> Попитай въпрос <i class="fas fa-circle-notch fa-spin"></i>'
          );

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

  $("#report-project").click(function(event) {
    var instance = $(this);
    var input =
      '<select name="category">' +
      '<option value="Препродажба">Препродажба</option>' +
      '<option value="Дарения(средствата трябва да отиват за финансиране на проекта)">Дарения(средствата трябва да отиват за финансиране на проекта)</option>' +
      '<option value="Инвестиции(проектите не могат да предлагат инвестиционни планове)">Инвестиции(проектите не могат да предлагат инвестиционни планове)</option>' +
      '<option value="Подвеждащо описание">Подвеждащо описание</option>' +
      '<option value="Лъжливи твърдения">Лъжливи твърдения</option>' +
      '<option value="Не е проект">Не е проект</option>' +
      '<option value="Спам">Спам</option>' +
      '<option value="Нарушава закона за интелектуална собственост">Нарушава закона за интелектуална собственост</option>' +
      "</select>";

    vex.dialog.open({
      message: "Избери проект, за който да поканиш " + name + ":",
      input: [input].join(""),
      buttons: [
        $.extend({}, vex.dialog.buttons.YES, { text: "Изпрати" }),
        $.extend({}, vex.dialog.buttons.NO, { text: "Затвори" })
      ],
      callback: function(data) {
        if (!data) {
        } else {
          instance.html(
            'Сигнализирай този проект <i class="fas fa-circle-notch fa-spin"></i>'
          );

          $.ajax({
            url:
              "/api/reportProject?category=" +
              data.category +
              "&projectId=" +
              projectId,
            method: "GET",
            success: function(result) {
              instance.html("Сигнализирай този проект");
              if (result) {
                iziToast.success({
                  title: "ОК!",
                  message:
                    "Сигналът беше изпратен успешно. Ще вземем мерки при първа възможност.",
                  position: "topRight"
                });
              }
            }
          });
        }
      }
    });
  });

  $("#project-description-ribbon").click(function() {
    if (!$("#project-description-ribbon").hasClass("active-project-ribbon")) {
      $(".project-description-tab-ribbon").attr("style", "display: block;");
      $(".project-updates-tab-ribbon").attr("style", "display: none;");
      $(".project-comments-tab-ribbon").attr("style", "display: none;");
      $("#project-description-ribbon").addClass("active-project-ribbon");
      $("#project-updates-ribbon").removeClass("active-project-ribbon");
      $("#project-comments-ribbon").removeClass("active-project-ribbon");

      $(".project-description-tab-ribbon").addClass("animated fadeIn");
    }
  });

  $("#project-updates-ribbon").click(function() {
    if (!$("#project-updates-ribbon").hasClass("active-project-ribbon")) {
      $(".project-description-tab-ribbon").attr("style", "display: none;");
      $(".project-updates-tab-ribbon").attr("style", "display: block;");
      $(".project-comments-tab-ribbon").attr("style", "display: none;");
      $("#project-updates-ribbon").addClass("active-project-ribbon");
      $("#project-comments-ribbon").removeClass("active-project-ribbon");
      $("#project-description-ribbon").removeClass("active-project-ribbon");

      $(".project-updates-tab-ribbon").addClass("animated fadeIn");
    }
  });

  $("#project-comments-ribbon").click(function() {
    if (!$("#project-comments-ribbon").hasClass("active-project-ribbon")) {
      $(".project-description-tab-ribbon").attr("style", "display: none;");
      $(".project-updates-tab-ribbon").attr("style", "display: none;");
      $(".project-comments-tab-ribbon").attr("style", "display: block;");
      $("#project-comments-ribbon").addClass("active-project-ribbon");
      $("#project-description-ribbon").removeClass("active-project-ribbon");
      $("#project-updates-ribbon").removeClass("active-project-ribbon");

      $(".project-comments-tab-ribbon").addClass("animated fadeIn");
    }
  });

  $(".preloader").fadeOut(500);
}
