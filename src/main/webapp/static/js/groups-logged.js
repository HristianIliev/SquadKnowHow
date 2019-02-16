// if ($(window).width() < 1440) {
//   $("#dropdown-profile-image").attr(
//     "style",
//     "position: absolute;transform: translate3d(-10px, 93px, 0px);top: 0px;will-change: transform;"
//   );
// }

var wasOverlapped = false;

// if ($(window).width() < 1200) {
//   $(".navbar-header").remove();
//   $(".changeBelowRes").attr("style", "display: flex;justify-content: center;");
// }

if (annyang) {
  var commands = {
    "здравей": function () {
      var audio = new Audio("/static/audio/greeting.mp3");
      audio.play();
    },
    "спри": function () {
      var audio = new Audio("/static/audio/goodbye.mp3");
      audio.play();
      SpeechKITT.abortRecognition();
    },
    "изключи се": function () {
      var audio = new Audio("/static/audio/goodbye.mp3");
      audio.play();
      SpeechKITT.abortRecognition();
    },
    "отвори отвори моят профил": function () {
      window.location.href = "/profile";
    },
    "отвори търгове": function () {
      window.location.href = "/auctions";
    },
    "отвори търсещи": function () {
      window.location.href = "/users";
    },
    "отвори проекти": function () {
      window.location.href = "/projects-of-people";
    },
    "отвори групи": function () {
      window.location.href = "/groups-of-people";
    },
    "кажи здравей за демото": function () {
      var audio = new Audio("/static/audio/greeting.mp3");
      audio.play();
    },
    "вход": function () {
      window.location.href = "/sign-in";
    },
    "отиди на функции": function () {
      window.location.replace("/home");
    },
    "отиди на начало": function () {
      window.location.replace("/home");
    },
    "отиди на как работи": function () {
      window.location.replace("/home");
    },
    "отиди на въпроси": function () {
      window.location.replace("/home");
    },
    "отиди на контакти": function () {
      window.location.replace("/home");
    },
    "мими": function () {
      alert("Обичам те");
    },
    "кажи здравей за демото": function () {
      var audio = new Audio("/static/audio/greeting.mp3");
      audio.play();
    },
    "търси *name": function (name) {
    }
  };

  var goToFunc = function (segment) {
    alert(segment);
  };

  annyang.addCommands(commands);
  annyang.addCallback("resultNoMatch", function () {
    iziToast.warning({
      title: "Грешка",
      message: "Не успях да идентифицирам казаното от вас",
      position: "topRight"
    });
  });

  annyang.setLanguage("bg");

  SpeechKITT.annyang();

  SpeechKITT.setStylesheet(
    "//cdnjs.cloudflare.com/ajax/libs/SpeechKITT/0.3.0/themes/flat.css"
  );

  SpeechKITT.vroom();
}

var user = null;

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

$("#button-create-group").click(function () {
  window.location.href = "/create-group";
});

$("#btn-search-user-name").click(function (event) {
  event.preventDefault();
  sendGetPages();
});

function sendGetPages() {
  var name = $("#search-user-name").val();
  getPages(name);
}

getPages("");

function getPages(name) {
  $.ajax({
    url: "/api/getGroupPages?name=" + name,
    method: "GET",
    success: function (result) {
      if (result.numberOfPages !== 0) {
        $(".card-title-people").text(
          "Групи от потребители: " + result.numberOfEntries
        );
        $("#pagination-groups").remove();
        $(".pagination-box").append(
          $("<div/>")
          .attr("style", "text-align:center;")
          .append(
            $("<ul/>")
            .attr("id", "pagination-groups")
            .addClass("pagination-lg")
            .addClass("justify-content-end")
          )
        );
        $("#pagination-groups").twbsPagination({
          totalPages: result.numberOfPages,
          visiblePages: 7,
          first: "Първи",
          prev: "Предишен",
          next: "Следващ",
          last: "Последен",
          onPageClick: function (event, page) {
            getGroups(name);
          }
        });
      } else {
        iziToast.error({
          title: "Грешка",
          message: "Не бяха намерени групи по тези критерии",
          position: "topRight"
        });
      }
    }
  });
}

function getGroups(name) {
  $(".card-deck")
    .removeClass("animated")
    .removeClass("zoomIn");

  $(".card-deck").empty();

  $(".card-deck").append($("<div/>").addClass("loader-entries"));

  $.ajax({
    url: "/api/groups?page=" +
      $("#pagination-groups .active").text() +
      "&name=" +
      name,
    method: "GET",
    success: function (result) {
      populateGroups(result);
    }
  });
}

function populateGroups(result) {
  $(".card-deck").empty();

  var groups = result.groups;
  for (var i = groups.length - 1; i >= 0; i -= 1) {
    $("#card-deck-groups-logged")
      .addClass("animated")
      .addClass("zoomIn")
      .append(
        $("<div/>")
        .addClass("needsRegulatingAfter")
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
                .attr("href", "/group/" + groups[i].id)
                .attr("id", "user-cover_" + groups[i].id)
                .addClass("should-send-group-id")
                .append(
                  $("<div/>")
                  .addClass("view")
                  .addClass("overlay")
                  .addClass("cover-rotating")
                  .append(
                    $("<img/>").attr(
                      "src",
                      groups[i].logo
                    )
                  )
                  .append(
                    $("<a/>")
                    .attr("href", "/group/")
                    .append(
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
                  .addClass("main")
                  .addClass("text-center")
                  .append(
                    $("<a/>")
                    .attr("id", "card-name_" + groups[i].id)
                    .addClass("should-send-group-id")
                    .attr("href", "/group/" + groups[i].id)
                    .addClass("name")
                    .text(groups[i].name)
                  )
                  .append(
                    $("<div/>")
                    .addClass("row")
                    .addClass("stats-row")
                    .append(
                      $("<div/>")
                      .addClass("stat")
                      .addClass("has-right-border")
                      .addClass("col-md-6")
                      .addClass("col-sm-6")
                      .addClass("col-xs-6")
                      .append(
                        $("<h4/>").text(groups[i].membersCount)
                      )
                      .append($("<p/>").text("Участващи"))
                    )
                    .append(
                      $("<div/>")
                      .addClass("stat")
                      .addClass("col-md-6")
                      .addClass("col-sm-6")
                      .addClass("col-xs-6")
                      .append(
                        $("<h4/>").text(groups[i].projectsCount)
                      )
                      .append($("<p/>").text("Проекта"))
                    )
                  )
                )
              )
            )
          )
        )
      );
  }

  // if ($(window).width() <= 1270) {
  //   $(".needsRegulatingAfter").removeClass("col-md-3");
  //   $(".needsRegulatingAfter").addClass("col-md-6");
  // } else if ($(window).width() <= 1440) {
  //   $(".needsRegulatingAfter").removeClass("col-md-3");
  //   $(".needsRegulatingAfter").addClass("col-md-4");
  // }

  $(".preloader").fadeOut(500);

  var overlap = isOverlapping(
    document.getElementById("footer"),
    document.getElementById("skitt-ui")
  );
  if (overlap) {
    SpeechKITT.setStylesheet(
      "//cdnjs.cloudflare.com/ajax/libs/SpeechKITT/1.0.0/themes/flat-concrete.css"
    );
    wasOverlapped = true;
  } else {
    if (wasOverlapped) {
      SpeechKITT.setStylesheet(
        "//cdnjs.cloudflare.com/ajax/libs/SpeechKITT/1.0.0/themes/flat.css"
      );
      wasOverlapped = false;
    }
  }

  $(window).scroll(function (event) {
    var overlap = isOverlapping(
      document.getElementById("footer"),
      document.getElementById("skitt-ui")
    );
    if (overlap) {
      SpeechKITT.setStylesheet(
        "//cdnjs.cloudflare.com/ajax/libs/SpeechKITT/1.0.0/themes/flat-concrete.css"
      );
      wasOverlapped = true;
    } else {
      if (wasOverlapped) {
        SpeechKITT.setStylesheet(
          "//cdnjs.cloudflare.com/ajax/libs/SpeechKITT/1.0.0/themes/flat.css"
        );
        wasOverlapped = false;
      }
    }
  });
}

function isOverlapping(e1, e2) {
  if (e1.length && e1.length > 1) {
    e1 = e1[0];
  }
  if (e2.length && e2.length > 1) {
    e2 = e2[0];
  }
  var rect1 = e1 instanceof Element ? e1.getBoundingClientRect() : false;
  var rect2 = e2 instanceof Element ? e2.getBoundingClientRect() : false;

  // window.console ? console.log(rect1, rect2) : null;
  var overlap = null;
  if (rect1 && rect2) {
    overlap = !(
      rect1.right < rect2.left ||
      rect1.left > rect2.right ||
      rect1.bottom < rect2.top ||
      rect1.top > rect2.bottom
    );
    return overlap;
  } else {
    window.console ? console.warn("Please pass native Element object") : null;
    return overlap;
  }
}