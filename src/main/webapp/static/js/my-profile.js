$(".should-send-group-id").on("click", function() {
  var indexOfUndescore = $(this)
    .attr("id")
    .indexOf("_");
  var groupIdToSend = $(this)
    .attr("id")
    .substring(indexOfUndescore + 1);
});

$(".should-send-project-id").on("click", function() {
  var indexOfUndescore = $(this)
    .attr("id")
    .indexOf("_");
  var projectIdToSend = $(this)
    .attr("id")
    .substring(indexOfUndescore + 1);
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
