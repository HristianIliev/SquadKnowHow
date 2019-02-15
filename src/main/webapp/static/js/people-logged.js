var wasOverlapped = false;
var currentUserCategory = '';

$(".dropdown-item").click(function () {
  var selected = $(this).text();
  if (selected == 'Всички') {
    currentUserCategory = '';
  } else {
    currentUserCategory = selected;
  }

  $("#dropdown-filter").html('<i class="fa fa-caret-down" style="margin-right: 5px;"></i> ' + selected + '<div class="rippleJS"></div>');

  var skillToSend = $("#skilled-in")
    .val()
    .substring(0, $("#skilled-in").val().length - 2);
  var interestToSend = $("#interested-in")
    .val()
    .substring(0, $("#interested-in").val().length - 2);
  sendGetPages($("#search-by-city").val(), skillToSend, interestToSend);
})

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
    "отвори моят профил": function () {
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
    "благодаря ви за вниманието": function () {
      var audio2 = new Audio("/static/audio/endDemo.mp3");
      audio2.play();
    },
    "търси за *skillset": function (skillset) {
      var value = skillset;

      var skillToSend = $("#skilled-in")
        .val()
        .substring(0, $("#skilled-in").val().length - 2);
      var interestToSend = $("#interested-in")
        .val()
        .substring(0, $("#interested-in").val().length - 2);

      var text = $(".dropdown-item").text() + "";
      if (text.toLowerCase().includes(value.toLowerCase())) {
        $("#dropdown-filter").text("Търси за: " + value);
        sendGetPages($("#search-by-city").val(), skillToSend, interestToSend);
      } else {
        alert("Не съществува такава категория");
      }
    },
    "търси *name": function (name) {
      $("#search-user-name").val(name);
      var skillToSend = $("#skilled-in")
        .val()
        .substring(0, $("#skilled-in").val().length - 2);
      var interestToSend = $("#interested-in")
        .val()
        .substring(0, $("#interested-in").val().length - 2);
      sendGetPages($("#search-by-city").val(), skillToSend, interestToSend);
    },
    "живеещ в *city": function (city) {
      var skillToSend = $("#skilled-in")
        .val()
        .substring(0, $("#skilled-in").val().length - 2);
      var interestToSend = $("#interested-in")
        .val()
        .substring(0, $("#interested-in").val().length - 2);
      $("#search-by-city").val(city);
      sendGetPages(city, skillToSend, interestToSend);
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

// $(".dropdown-item").click(function () {
//   var value = $(this).text();
//   $("#dropdown-filter").html("Търси за: " + value + '<div class="rippleJS"></div>');

//   var skillToSend = $("#skilled-in")
//     .val()
//     .substring(0, $("#skilled-in").val().length - 2);
//   var interestToSend = $("#interested-in")
//     .val()
//     .substring(0, $("#interested-in").val().length - 2);
//   sendGetPages($("#search-by-city").val(), skillToSend, interestToSend);
// });

$("#sort-select").niceSelect();

$("#sort-select").on("change", function (e) {
  var skillToSend = $("#skilled-in")
    .val()
    .substring(0, $("#skilled-in").val().length - 2);
  var interestToSend = $("#interested-in")
    .val()
    .substring(0, $("#interested-in").val().length - 2);

  sendGetPages($("#search-by-city").val(), skillToSend, interestToSend);
});

$(function () {
  function split(val) {
    return val.split(/,\s*/);
  }

  function extractLast(term) {
    return split(term).pop();
  }

  $("#search-by-city").autocomplete({
    source: "/api/getCities",
    minLength: 2,
    select: function (event, ui) {
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
    .on("keydown", function (event) {
      if (
        event.keyCode === $.ui.keyCode.TAB &&
        $(this).autocomplete("instance").menu.active
      ) {
        event.preventDefault();
      }
    })
    .autocomplete({
      source: function (request, response) {
        $.getJSON(
          "/api/getSkills", {
            term: extractLast(request.term)
          },
          response
        );
      },
      search: function () {
        // custom minLength
        var term = extractLast(this.value);
        if (term.length < 2) {
          return false;
        }
      },
      focus: function () {
        // prevent value inserted on focus
        return false;
      },
      select: function (event, ui) {
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
    .on("keydown", function (event) {
      if (
        event.keyCode === $.ui.keyCode.TAB &&
        $(this).autocomplete("instance").menu.active
      ) {
        event.preventDefault();
      }
    })
    .autocomplete({
      source: function (request, response) {
        $.getJSON(
          "/api/getLanguages", {
            term: extractLast(request.term)
          },
          response
        );
      },
      search: function () {
        // custom minLength
        var term = extractLast(this.value);
        if (term.length < 2) {
          return false;
        }
      },
      focus: function () {
        // prevent value inserted on focus
        return false;
      },
      select: function (event, ui) {
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

$("#btn-search-user-name").click(function (event) {
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
  // var userCategoryRaw = $("#dropdown-filter").text();
  // var userCategory = userCategoryRaw.substring(10, userCategoryRaw.length);
  // userCategory =
  //   userCategory.substring(0, 1).toUpperCase() +
  //   userCategory.substring(1).toLowerCase();
  // if (
  //   userCategory === "                    Търси за: " ||
  //   userCategory === "Всички" ||
  //   userCategory === "            търси за: " ||
  //   userCategory === "            Търси за: " ||
  //   userCategory === "        търси за:              "
  // ) {
  //   userCategory = "";
  // }

  // console.log(userCategory);
  getPages(name, currentUserCategory, city, skill, interest);
}

getPages("", "", "", "", "");

function getPages(name, userCategory, city, skill, interest) {
  $.ajax({
    url: "/api/getPeoplePages?name=" +
      name +
      "&userCategory=" +
      userCategory +
      "&city=" +
      city +
      "&skills=" +
      skill +
      "&languages=" +
      interest +
      "&sortBy=" +
      $("#sort-select").val(),
    method: "GET",
    success: function (result) {
      if (result.numberOfPages !== 0) {
        $(".card-title-people").text(
          "Потребители: " + result.numberOfEntries
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
          first: "Първи",
          prev: "Предишен",
          next: "Следващ",
          last: "Последен",
          onPageClick: function (event, page) {
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
    url: "/api/users?page=" +
      $("#pagination-people .active").text() +
      "&name=" +
      name +
      "&userCategory=" +
      userCategory +
      "&city=" +
      city +
      "&skills=" +
      skill +
      "&languages=" +
      interest +
      "&sortBy=" +
      $("#sort-select").val(),
    method: "GET",
    success: function (result) {
      populateUsers(result);
    }
  });
}

function populateUsers(result) {
  $(".card-deck").empty();
  var users = result.users;
  var profileStyle = "";
  for (var i = users.length - 1; i >= 0; i -= 1) {
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
      case "Артист":
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
      image = users[i].image;
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
    description = description.substring(0, 70) + "...";


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
    for (var j = 0; j < users[i].languages.length; j += 1) {
      interests += users[i].languages[j].name;
      if (j !== users[i].languages.length - 1) {
        interests += ", ";
      }
    }
    if (interests === "") {
      interests = "Няма въведени езици";
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
        .addClass("changeCol")
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
                  .append('<select id="rating_' + users[i].id + '" ><option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option></select>')
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
                    .text("Езици")
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

    $('#rating_' + users[i].id).barrating({
      theme: 'fontawesome-stars-o',
      readonly: true,
      hoverState: false,
      initialRating: users[i].rating
    });
  }

  $(".should-send-user-id").on("click", function () {
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var userIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);

    if (userIdToSend === id) {
      $(this).attr("href", "/profile");
    } else {}
  });

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