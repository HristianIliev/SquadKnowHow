var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

var previousA = $(".my-active");
$(document).ready(function () {
  var indexOfUndescoreForCommunication = $(".example-slider-communication")
    .attr("id")
    .indexOf("_");
  var ratingCommunication = $(".example-slider-communication")
    .attr("id")
    .substring(indexOfUndescoreForCommunication + 1);

  $(".example-slider-communication").ionRangeSlider({
    min: 0,
    max: 100,
    grid: true,
    from: ratingCommunication,
    from_fixed: true,
    postfix: "%"
  });

  var indexOfUndescoreForInitiative = $(".example-slider-initiative")
    .attr("id")
    .indexOf("_");
  var ratingInitiative = $(".example-slider-initiative")
    .attr("id")
    .substring(indexOfUndescoreForInitiative + 1);

  $(".example-slider-initiative").ionRangeSlider({
    min: 0,
    max: 100,
    grid: true,
    from: ratingInitiative,
    from_fixed: true,
    postfix: "%"
  });

  var indexOfUndescoreForLeadership = $(".example-slider-leadership")
    .attr("id")
    .indexOf("_");
  var ratingLeadership = $(".example-slider-leadership")
    .attr("id")
    .substring(indexOfUndescoreForLeadership + 1);

  $(".example-slider-leadership").ionRangeSlider({
    min: 0,
    max: 100,
    grid: true,
    from: ratingLeadership,
    from_fixed: true,
    postfix: "%"
  });

  var indexOfUndescoreForInnovation = $(".example-slider-innovation")
    .attr("id")
    .indexOf("_");
  var ratingInnovation = $(".example-slider-innovation")
    .attr("id")
    .substring(indexOfUndescoreForInnovation + 1);

  $(".example-slider-innovation").ionRangeSlider({
    min: 0,
    max: 100,
    grid: true,
    from: ratingInnovation,
    from_fixed: true,
    postfix: "%"
  });

  var indexOfUndescoreForResponsibility = $(".example-slider-responsibility")
    .attr("id")
    .indexOf("_");
  var ratingResponsibility = $(".example-slider-responsibility")
    .attr("id")
    .substring(indexOfUndescoreForResponsibility + 1);

  $(".example-slider-responsibility").ionRangeSlider({
    min: 0,
    max: 100,
    grid: true,
    from: ratingResponsibility,
    from_fixed: true,
    postfix: "%"
  });

  $("#wallet-modal").iziModal({
    closeOnEscape: true,
    closeButton: true
  });

  $(".wallet-trigger").click(function () {
    $("#wallet-modal").iziModal("open");
  });

  $.ajax({
    url: "/api/checkIfUserNeedsTour?id=" + id,
    method: "GET",
    success: function (result) {
      $("#retrieve-money").click(function () {
        var instance = $(this);
        vex.dialog.open({
          message: "Въведи сума, която искаш да изтеглиш (USD):",
          input: [
            '<input name="amount" type="text" placeholder="сума" required />'
          ].join(""),
          buttons: [
            $.extend({}, vex.dialog.buttons.YES, {
              text: "Продължи"
            }),
            $.extend({}, vex.dialog.buttons.NO, {
              text: "Затвори"
            })
          ],
          callback: function (data) {
            if (!data) {} else {
              instance.html('Изтегли сума <i class="fas fa-circle-notch fa-spin"></i>');
              $.ajax({
                url: "/api/retrieveMoney?id=" + id + "&amount=" + data.amount,
                method: "GET",
                success: function (result) {
                  instance.html('Изтегли сума <i class="fas fa-circle-notch fa-spin"></i>');
                  if (result || result === "true") {
                    iziToast.success({
                      title: "ОК!",
                      message: "Вие успешно изтеглихте парите си!",
                      position: "topRight"
                    });

                    $(".iziModal-button-close").trigger("click");

                    location.reload();
                  } else {
                    iziToast.error({
                      title: "Грешка!",
                      message: "Сумата, която въведохте не е валидна.",
                      position: "topRight"
                    });
                  }
                }
              });
            }
          }
        });
      });

      if (result.needsTour) {
        setUpTour(result);
      }
    }
  });

  function setUpTour(result) {
    var tour;
    if ($(window).width() <= 1019) {
      tour = {
        id: "welcomeTour",
        i18n: {
          nextBtn: "Напред",
          prevBtn: "Назад",
          doneBtn: "Готово",
          skipBtn: "Пропусни"
        },
        steps: [{
            nextOnTargetClick: true,
            title: "Добре дошъл в SquadKnowHow, " + result.firstName + "!",
            content: "В следващите няколко стъпки ще ти покажем къде и как да изполваш функционалността на тази платформа, за да можеш да използваш пълния й капацитет.",
            target: "profile-name",
            width: "500",
            placement: "top"
          },
          {
            title: "Твоят профил",
            content: "Вмомента се намираш в твоя профил. Винаги можеш да се върнеш тук като натиснеш снимката си и след това 'Моят профил'",
            target: "tour-step-9",
            width: "250",
            placement: "left"
          },
          {
            title: "Известия",
            content: "След като натиснеш тази икона ще моеш да видиш всички твои известия.",
            target: "tour-step-10",
            width: "300",
            placement: "left",
            onNext: function () {
              $(".navbar-toggle").trigger("click");
            }
          },
          {
            title: "Моите проекти",
            content: "В този таб можеш да видиш всички твои активни проект, както и да го управляваш и достъпваш.",
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
            content: "В този таб можеш да видиш всички групи от потребители и да създадеш или да сеприсъединиш към някоя от тях.",
            target: "tour-step-6",
            width: "300",
            nextOnTargetClick: true,
            placement: "bottom"
          },
          {
            title: "Проекти търсещи партньори",
            content: "Тук се виждат всички проекти търсещи възможни партньори. Тук можеш и да създадеш свой собствен проект.",
            target: "tour-step-7",
            width: "300",
            nextOnTargetClick: true,
            placement: "bottom"
          },
          {
            title: "Потребителите",
            content: "На това място са показани всички потребители, които са възможни участници в твоите бъдещи проекти.",
            target: "tour-step-8",
            width: "300",
            nextOnTargetClick: true,
            placement: "bottom"
          },
          {
            title: "Търгове",
            content: "На това място са показани всички търгове за права върху проект.",
            target: "tour-step-11",
            width: "300",
            nextOnTargetClick: true,
            placement: "bottom"
          },
          {
            nextOnTargetClick: true,
            title: "Това беше всичко от нас, " + result.firstName + " !",
            content: "Надяваме се, че си разбрал функционалността на тази платформа и се надяваме също, че тя ще ти е полезна и ще е от помощ. Не се замисляй ако трябва да се свържеш с нас по какъвто и да е въпрос на следния Е-mail: hristian00i@abv.bg.",
            target: "profile-name",
            width: "550",
            placement: "top"
          }
        ],
        onEnd: function () {
          $.ajax({
            url: "/api/tourCompleted?id=" + id,
            method: "GET",
            success: function (result) {
              if (result) {}
            }
          });
        },
        onClose: function () {
          $.ajax({
            url: "/api/tourCompleted?id=" + id,
            method: "GET",
            success: function (result) {
              if (result) {}
            }
          });
        }
      };
    } else {
      tour = {
        id: "welcomeTour",
        i18n: {
          nextBtn: "Напред",
          prevBtn: "Назад",
          doneBtn: "Готово",
          skipBtn: "Пропусни"
        },
        steps: [{
            nextOnTargetClick: true,
            title: "Добре дошъл в SquadKnowHow, " + result.firstName + "!",
            content: "В следващите няколко стъпки ще ти покажем къде и как да изполваш функционалността на тази платформа, за да можеш да използваш пълния й капацитет.",
            target: "profile-name",
            width: "500",
            placement: "top"
          },
          {
            title: "Твоят профил",
            content: "Вмомента се намираш в твоя профил. Винаги можеш да се върнеш тук като натиснеш снимката си и след това 'Моят профил'",
            target: "tour-step-2",
            width: "250",
            placement: "left"
          },
          {
            title: "Известия",
            content: "След като натиснеш тази икона ще можеш да видиш всички твои известия.",
            target: "tour-step-3",
            width: "300",
            placement: "left"
          },
          {
            title: "Моите проекти",
            content: "В този таб можеш да видиш всички твои активни проект, както и да го управляваш и достъпваш.",
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
            content: "В този таб можеш да видиш всички групи от потребители и да създадеш или да сеприсъединиш към някоя от тях.",
            target: "tour-step-6",
            width: "300",
            nextOnTargetClick: true,
            placement: "bottom"
          },
          {
            title: "Проекти търсещи партньори",
            content: "Тук се виждат всички проекти търсещи възможни партньори. Тук можеш и да създадеш свой собствен проект.",
            target: "tour-step-7",
            width: "300",
            nextOnTargetClick: true,
            placement: "bottom"
          },
          {
            title: "Потребителите",
            content: "На това място са показани всички потребители, които са възможни участници в твоите бъдещи проекти.",
            target: "tour-step-8",
            width: "300",
            nextOnTargetClick: true,
            placement: "bottom"
          },
          {
            title: "Търгове",
            content: "На това място са показани всички търгове за права върху проект.",
            target: "tour-step-11",
            width: "300",
            nextOnTargetClick: true,
            placement: "bottom"
          },
          {
            nextOnTargetClick: true,
            title: "Това беше всичко от нас, " + result.firstName + " !",
            content: "Надяваме се, че си разбрал функционалността натази платформа и се надяваме също, че тя ще ти е полезна и ще е от помощ. Не се замисляй ако трябва да се свържеш с нас по какъвто и да е въпрос на следния Е-mail: hristian00i@abv.bg.",
            target: "profile-name",
            width: "550",
            placement: "top"
          }
        ],
        onEnd: function () {
          $.ajax({
            url: "/api/tourCompleted?id=" + id,
            method: "GET",
            success: function (result) {
              if (result) {}
            }
          });
        },
        onClose: function () {
          $.ajax({
            url: "/api/tourCompleted?id=" + id,
            method: "GET",
            success: function (result) {
              if (result) {}
            }
          });
        }
      };
    }

    // Start the tour!
    hopscotch.startTour(tour);
  }

  $(".nav-item").click(function () {
    previousA.removeClass("my-active");
    previousA = $(this);
    $(this).addClass("my-active");
  });

  $(".preloader").fadeOut(500);
  AOS.init();
});