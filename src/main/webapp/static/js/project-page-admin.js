var chart;
var arr = window.location.pathname.split("/");
var projectId = arr[arr.length - 1];

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

var previousA = $(".my-active");

// GOOGLE MAPS SETUP
var map;

function initMap() {
  $.ajax({
    url: "/api/project?id=" + projectId,
    method: "GET",
    success: function (result) {
      map = new google.maps.Map(document.getElementById("map"), {
        center: {
          lat: -34.397,
          lng: 150.644
        },
        zoom: 14
      });

      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
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

      map.setCenter({
        lat: pos.lat,
        lng: pos.lng
      });

      //   GETS THE ADDRESS HUMAN READABLE
      var geocoder = new google.maps.Geocoder();
      geocoder.geocode({
          location: pos
        },
        function (results, status) {
          if (status === "OK") {
            if (results[0]) {
              $(".arena-address").text(results[0].formatted_address);
            } else {
              window.alert("No results found");
            }
          } else {
            window.alert("Geocoder failed due to: " + status);
          }
        }
      );
    }
  });
}

$(document).ready(function () {
  if ($(".comment-count").length !== 0) {
    alert();
  }

  $(".zoom-btn-sm").click(function () {
    vex.dialog.open({
      message: "Генериране на презентация",
      input: [
        '<div class="vex-custom-field-wrapper">',
        '<label for="embed">Линк към кодът за ембедване на презентацията (инструкции: https://support.office.com/en-us/article/embed-a-presentation-in-a-web-page-or-blog-19668a1d-2299-4af3-91e1-ae57af723a60)</label>',
        '<div class="vex-custom-input-wrapper">',
        '<input name="embed" type="text" placeholder="Линк" required />',
        "</div>",
        "</div>"
      ].join(""),
      buttons: [
        jq.extend({}, vex.dialog.buttons.YES, {
          text: "Изпрати"
        }),
        jq.extend({}, vex.dialog.buttons.NO, {
          text: "Затвори"
        })
      ],
      callback: function (data) {
        if (!data) {} else {
          var link = data.embed;

          $.ajax({
            url: "/api/addPowerpointEmbedCode?projectId=" +
              projectId +
              "&link=" +
              link,
            method: "GET",
            success: function (result) {}
          });
        }
      }
    });
  });

  $.ajax({
    url: "/api/getChartData?projectId=" + projectId,
    method: "GET",
    success: function (result) {
      moment.locale("bg");
      var finalData = [];
      for (var i = result.dates.length - 1; i >= 0; i -= 1) {
        var array = [];
        array.push(moment(result.dates[i]).valueOf());
        array.push(result.visits[i]);
        finalData.push(array);
      }

      chart = Highcharts.chart("highcharts-box", {
        chart: {
          reflow: true
        },
        title: {
          text: "Графика на посещенията на проекта"
        },

        subtitle: {
          text: "през последните 7 дни"
        },

        yAxis: {
          title: {
            text: "Брой посещения"
          }
        },
        xAxis: {
          title: {
            text: "Дата"
          },
          type: "datetime",
          tickInterval: 24 * 3600 * 1000
        },
        legend: {
          layout: "vertical",
          align: "right",
          verticalAlign: "middle"
        },

        plotOptions: {
          series: {
            label: {
              connectorAllowed: true
            }
          }
        },

        series: [{
          name: "Посещения",
          data: finalData,
          type: "spline"
        }],

        responsive: {
          rules: [{
            condition: {
              maxWidth: 500
            },
            chartOptions: {
              legend: {
                layout: "horizontal",
                align: "center",
                verticalAlign: "bottom"
              }
            }
          }]
        }
      });
    }
  });

  $(".pricing-button").click(function () {
    $(".is-featured").removeClass("is-featured");
    $(this).addClass("is-featured");
    var priceToPay = $(".is-featured")
      .prev()
      .html();
    $(".priceToPay").html(priceToPay);
  });

  initialiseProjectPage();
});

function initialiseProjectPage() {
  $.ajax({
    url: "/api/project?id=" + projectId,
    method: "GET",
    success: function (result) {
      $(".should-send-profile-id").on("click", function (event) {
        event.preventDefault();
        var indexOfUndescore = $(this)
          .attr("id")
          .indexOf("_");
        var userIdToSend = $(this)
          .attr("id")
          .substring(indexOfUndescore + 1);
        window.location.href = "/user/" + userIdToSend;
      });

      $(".trigger-modal").click(function (e) {
        var recipientId = $(this).attr("id");

        vex.dialog.open({
          message: "Напиши своето съобщение:",
          input: [
            '<input id="new-message-topic" name="topic" type="text" placeholder="Тема" required />',
            '<div class="form-group"><textarea id="new-message-content" name="content" class="form-control" placeholder="Съдържание" rows="10"></textarea></div>'
          ].join(""),
          buttons: [
            $.extend({}, vex.dialog.buttons.YES, {
              text: "Изпрати"
            }),
            $.extend({}, vex.dialog.buttons.NO, {
              text: "Затвори"
            })
          ],
          callback: function (data) {
            if (!data) {} else {

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
                success: function (result) {
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

      $(".titanic-shopping").click(function (event) {
        todayDateString = new Date().toJSON().slice(0, 10);
        currentTimeString = Date.now();

        vex.dialog.open({
          message: "Създаване на търг:",
          input: [
            '<div class="vex-custom-field-wrapper">',
            '<label for="date">Дата на приключване (mm/dd/yyyy)</label>',
            '<div class="vex-custom-input-wrapper">',
            '<input name="date" type="date" value="' +
            todayDateString +
            '" required />',
            "</div>",
            "</div>",
            '<div class="vex-custom-field-wrapper">',
            '<label for="time">Час за приключване на търга</label>',
            '<div class="vex-custom-input-wrapper">',
            '<input name="time" type="time" value="' +
            currentTimeString +
            '" required />',
            "</div>",
            "</div>",
            '<div class="vex-custom-field-wrapper">',
            '<label for="buyMeNow">Цена за моментално купуване (Опционално)</label>',
            '<div class="vex-custom-input-wrapper">',
            '<input placeholder="Цената е в лева" name="buyMeNow" type="number" min="1" step=".01"/>',
            "</div>",
            "</div>"
          ].join(""),
          buttons: [
            $.extend({}, vex.dialog.buttons.YES, {
              text: "Създай"
            }),
            $.extend({}, vex.dialog.buttons.NO, {
              text: "Затвори"
            })
          ],
          callback: function (data) {
            if (!data) {} else {
              var buyMeNow;
              if (typeof data.buyMeNow === "undefined") {
                buyMeNow = 0;
              } else {
                buyMeNow = data.buyMeNow;
              }

              $.ajax({
                url: "/api/createAuction",
                method: "POST",
                data: JSON.stringify({
                  endDate: data.date,
                  endHour: data.time,
                  projectId: projectId,
                  buyMeNow: buyMeNow
                }),
                contentType: "application/json",
                success: function (result) {
                  if (2 === parseInt(result.responseId)) {
                    iziToast.success({
                      title: "ОК!",
                      message: "Търгът беше създаден успешно",
                      position: "topRight"
                    });
                  } else if (0 === parseInt(result.responseId)) {
                    iziToast.error({
                      title: "Грешка",
                      message: "Вече съществува търг за този проект",
                      position: "topRight"
                    });
                  } else if (1 === parseInt(result.responseId)) {
                    iziToast.error({
                      title: "Грешка",
                      message: "Посочената дата е преди сегашната",
                      position: "topRight"
                    });
                  }
                }
              });
            }
          }
        });
      });

      tippy(".note-btn__uLJR", {
        placement: "left",
        animation: "perspective",
        duration: 700,
        arrow: true,
        arrowType: "round",
        interactive: true,
        size: "large"
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

      setUpAddUpdate();

      $("#project-description-ribbon").click(function () {
        if (
          !$("#project-description-ribbon").hasClass("active-project-ribbon")
        ) {
          $(".project-description-tab-ribbon").attr("style", "display: block;");
          $(".project-updates-tab-ribbon").attr("style", "display: none;");
          $(".project-comments-tab-ribbon").attr("style", "display: none;");
          $("#project-description-ribbon").addClass("active-project-ribbon");
          $("#project-updates-ribbon").removeClass("active-project-ribbon");
          $("#project-comments-ribbon").removeClass("active-project-ribbon");

          $(".project-description-tab-ribbon").addClass("animated fadeIn");
        }
      });

      $("#project-updates-ribbon").click(function () {
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

      $("#project-comments-ribbon").click(function () {
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
  });
}

var chosenImage = 0;
var updateTitle = "";
var updateContent = "";
var updateDate = "";

function setUpAddUpdate() {
  $(".changeHere").click(function () {
    Swal({
      title: "<span>Избери икона</span>",
      type: "info",
      html: '<div style="display: flex; justify-content: space-between;flex-direction: row">' +
        '<div class="cd-timeline__img cd-timeline__img--picture js-cd-img image-to-choose green-image" style="position: relative;left: 0;margin-left: 0px;cursor:pointer;">' +
        '<img src="/static/images/cd-icon-picture.svg" alt="Picture"></div>' +
        '<div style="position: relative;left: 0;margin-left: 0px;cursor: pointer;" class="red-image cd-timeline__img cd-timeline__img--movie js-cd-img image-to-choose">' +
        '<img src="/static/images/cd-icon-movie.svg" alt="Picture"></div>' +
        '<div class="yellow-image cd-timeline__img cd-timeline__img--location js-cd-img image-to-choose" style="cursor:pointer; position: relative;left: 0;margin-left: 0px;"><img src="/static/images/cd-icon-location.svg" alt="Picture"></div></div>',
      showCloseButton: false,
      showCancelButton: false,
      onOpen: () => {
        $(".image-to-choose").click(function () {
          $(".active-choose-image").removeClass("active-choose-image");
          $(this).addClass("active-choose-image");
          if ($(this).hasClass("green-image")) {
            chosenImage = 0;
          } else if ($(this).hasClass("red-image")) {
            chosenImage = 1;
          } else if ($(this).hasClass("yellow-image")) {
            chosenImage = 2;
          }
        });
      }
    }).then(result => {
      $(".changeHere").html();
      if (chosenImage === 0) {
        $(".changeHere").html(
          '<img src="/static/images/cd-icon-picture.svg" alt="Picture">'
        );
        $(".changeHere").addClass("cd-timeline__img--picture");
        $(".changeHere").removeClass("cd-timeline__img--movie");
        $(".changeHere").removeClass("cd-timeline__img--location");
        $(".changeHere").removeClass(".addPictureButton");
      } else if (chosenImage === 1) {
        $(".changeHere").html(
          '<img src="/static/images/cd-icon-movie.svg" alt="Picture">'
        );
        $(".changeHere").addClass("cd-timeline__img--movie");
        $(".changeHere").removeClass("cd-timeline__img--picture");
        $(".changeHere").removeClass("cd-timeline__img--location");
        $(".changeHere").removeClass(".addPictureButton");
      } else if (chosenImage === 2) {
        $(".changeHere").html(
          '<img src="/static/images/cd-icon-location.svg" alt="Picture">'
        );
        $(".changeHere").addClass("cd-timeline__img--location");
        $(".changeHere").removeClass("cd-timeline__img--movie");
        $(".changeHere").removeClass("cd-timeline__img--picture");
        $(".changeHere").removeClass(".addPictureButton");
      }
    });
  });

  $(".createUpdateBtn").click(function () {
    updateTitle = $(".newUpdateTitle").val();
    updateContent = $(".newUpdateContent").val();
    updateDate = $(".newUpdateDate").val();

    if (
      (chosenImage !== 0 && chosenImage !== 1 && chosenImage !== 2) ||
      updateTitle === "" ||
      updateContent === "" ||
      updateDate === ""
    ) {
      iziToast.error({
        title: "Грешка",
        message: "Имате невъведени данни",
        position: "topRight"
      });
    } else {
      var type = "";
      if (chosenImage === 0) {
        type = "green";
      } else if (chosenImage === 1) {
        type = "red";
      } else {
        type = "yellow";
      }

      $.ajax({
        url: "/api/createNewUpdate?projectId=" +
          projectId +
          "&title=" +
          updateTitle +
          "&content=" +
          updateContent +
          "&date=" +
          updateDate +
          "&type=" +
          type,
        method: "GET",
        success: function (result) {
          if (result.id != 0) {
            chosenImage = 3;
            $(".newUpdateTitle").val();
            $(".newUpdateContent").val();
            $(".newUpdateDate").val();
            $(".changeHere").html(
              '<i class="fas fa-plus" style="cursor: pointer;"></i>'
            );
            $(".changeHere").removeClass("cd-timeline__img--location");
            $(".changeHere").removeClass("cd-timeline__img--movie");
            $(".changeHere").removeClass("cd-timeline__img--picture");
            $(".changeHere").addClass("addPictureButton");

            if (result.type === "green") {
              $(".blockBefore").before(
                '<div class="cd-timeline__block js-cd-block"><div class="cd-timeline__img cd-timeline__img--picture js-cd-img animated bounceIn"><img src="/static/images/cd-icon-picture.svg" alt="Picture"></div><div class="cd-timeline__content js-cd-content animated fadeInLeft"><h2>' +
                result.title +
                "</h2><p>" +
                result.content +
                '</p><span class="cd-timeline__date">' +
                result.date +
                "</span></div></div>"
              );
            } else if (result.type === "red") {
              $(".blockBefore").before(
                '<div class="cd-timeline__block js-cd-block"><div class="cd-timeline__img cd-timeline__img--movie js-cd-img animated bounceIn"><img src="/static/images/cd-icon-movie.svg" alt="Picture"></div><div class="cd-timeline__content js-cd-content animated fadeInLeft"><h2>' +
                result.title +
                "</h2><p>" +
                result.content +
                '</p><span class="cd-timeline__date">' +
                result.date +
                "</span></div></div>"
              );
            } else {
              $(".blockBefore").before(
                '<div class="cd-timeline__block js-cd-block"><div class="cd-timeline__img cd-timeline__img--location js-cd-img animated bounceIn"><img src="/static/images/cd-icon-location.svg" alt="Picture"></div><div class="cd-timeline__content js-cd-content animated fadeInLeft"><h2>' +
                result.title +
                "</h2><p>" +
                result.content +
                '</p><span class="cd-timeline__date">' +
                result.date +
                "</span></div></div>"
              );
            }
          } else {
            iziToast.error({
              title: "Грешка",
              message: "Датата не е валидна",
              position: "topRight"
            });

            $(".newUpdateDate").val();
          }
        }
      });
    }
  });
}

function setUpTODOList(result) {
  $(".lobilist").lobiList({
    sortable: false,
    controls: [],
    enableTodoEdit: false,
    lists: [{
      id: "todoList_" + projectId,
      title: "Задачи върху проекта:",
      defaultStyle: "lobilist-info",
      items: []
    }],
    afterItemDelete: function (list, item) {
      console.log(item);
      $.ajax({
        url: "/api/deleteItem?itemTitle=" + item.title,
        method: "DELETE",
        success: function (result) {
          if ($(".lobilist-items > li").length == 0) {
            $(".lobilist-body").attr("style", "display: flex;flex-direction: column;");
            $(".lobilist-items").after('<span id="lobilist-empty-msg" style="text-align: center;font-size: 25px;margin-bottom: 10px;color: rgb(109, 109, 109);">Все още няма добавени задачи</span>');
          }
        }
      });
    },
    afterMarkAsDone: function (list, checkbox) {
      var itemId = $(checkbox)
        .parent()
        .parent()
        .attr("data-id");
      var itemTitle = $("li[data-id='" + itemId + "'] > .lobilist-item-title").text();
      $.ajax({
        url: "/api/markListItemAsDone?itemTitle=" + itemTitle,
        method: "GET",
        success: function (result) {
          if (result) {}
        }
      });
    },
    afterMarkAsUndone: function (list, checkbox) {
      var itemId = $(checkbox)
        .parent()
        .parent()
        .attr("data-id");
      var itemTitle = $("li[data-id='" + itemId + "'] > .lobilist-item-title").text();
      $.ajax({
        url: "/api/markListItemAsUndone?itemTitle=" + itemTitle,
        method: "GET",
        success: function (result) {
          if (result) {}
        }
      });
    },
    afterItemAdd: function (list, item) {
      $("#lobilist-empty-msg").remove();
    }
  });

  $(".btn-add-todo").click(function () {
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
        success: function (result) {
          if (result == false || result == "false") {
            iziToast.error({
              title: "Грешка",
              message: "Това име вече е заето",
              position: "topRight"
            });
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
    } else {
      $(".lobilist-body").attr("style", "display: flex;flex-direction: column;");
      $(".lobilist-items").after('<span id="lobilist-empty-msg" style="text-align: center;font-size: 25px;margin-bottom: 10px;color: rgb(109, 109, 109);">Все още няма добавени задачи</span>');
    }
  }
}

$("#button-send-all-message").click(function (e) {
  var instance = $(this);
  vex.dialog.open({
    message: "Напиши своето съобщение:",
    input: [
      '<input id="new-message-topic" name="topic" type="text" placeholder="Тема" required />',
      '<div class="form-group"><textarea id="new-message-content" name="content" class="form-control" placeholder="Съдържание" rows="10"></textarea></div>'
    ].join(""),
    buttons: [
      $.extend({}, vex.dialog.buttons.YES, {
        text: "Изпрати"
      }),
      $.extend({}, vex.dialog.buttons.NO, {
        text: "Затвори"
      })
    ],
    callback: function (data) {
      if (!data) {} else {
        instance.html(
          '<i class="fa fa-comments"></i> Изпрати съобщение на всички <i class="fas fa-circle-notch fa-spin"></i>'
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
          success: function (result) {
            instance.html(
              '<i class="fa fa-comments"></i> Изпрати съобщение на всички'
            );
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

$("#pills-home-tab").click(function (e) {
  $(this).tab("show");
});

$("#pills-profile-tab").click(function (e) {
  $(this).tab("show");
  var width = $("#highcharts-box").width();
  var height = $("#highcharts-box").height();
  chart.setSize(width, height);

  $(window).resize(function () {
    var width1 = $("#highcharts-box").width();
    var height1 = $("#highcharts-box").height();
    chart.setSize(width1, height1);
  });
});

$(".nav-item").click(function () {
  previousA.removeClass("my-active");
  previousA = $(this);
  $(this).addClass("my-active");
});

$("#pills-calendar-tab").click(function (e) {
  $(this).tab("show");
});

$("#pills-pricing-tab").click(function (e) {
  $(this).tab("show");
});
// }