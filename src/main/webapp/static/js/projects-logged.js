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

  sendGetPages($("#search-by-city").val(), false);
})


var map;
var lastMarker = null;
var lastCircle = null;
var lat = 0;
var lng = 0;
var radius = 0;

function initMap() {
  $.ajax({
    url: "/api/getCoordinates",
    method: "GET",
    success: function (result) {
      var infowindow = new google.maps.InfoWindow();

      var defaultBoundsForAutocomplete = new google.maps.LatLngBounds(
        new google.maps.LatLng(41.10696293, 21.98533944),
        new google.maps.LatLng(44.41803141, 29.29457424)
      );

      var options = {
        bounds: defaultBoundsForAutocomplete
      };

      var input = document.getElementById("arena-address");
      var autocomplete = new google.maps.places.Autocomplete(input, options);

      map = new google.maps.Map(document.getElementById("map"), {
        center: {
          lat: -34.397,
          lng: 150.644
        },
        zoom: 14
      });
      // map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
          initialLocation = new google.maps.LatLng(
            position.coords.latitude,
            position.coords.longitude
          );
          map.setCenter(initialLocation);
          var marker = new google.maps.Marker({
            position: initialLocation,
            animation: google.maps.Animation.DROP,
            icon: "/static/icons/user-map-icon-2.png",
            map: map
          });
        });
      }

      for (var i = 0; i < result.length; i += 1) {
        position = new google.maps.LatLng(
          result[i].latitude,
          result[i].longitude
        );

        var newMarker = new google.maps.Marker({
          position: position,
          animation: google.maps.Animation.DROP,
          icon: "/static/icons/marker3.png",
          map: map,
          title: result[i].title,
          info: result[i].title
        });

        google.maps.event.addListener(newMarker, 'click', function () {
          infowindow.close();
          infowindow.setContent('<div style="text-align: center;">' + this.info + "</div>");
          infowindow.open(map, this);
        });
      }

      $(".searchService").on("submit", function () {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({
            address: $(".searchedCity").val()
          },
          function (results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
              pos = {
                lat: results[0].geometry.location.lat(),
                lng: results[0].geometry.location.lng()
              };
              lat = results[0].geometry.location.lat();
              lng = results[0].geometry.location.lng();
              map.setCenter(pos);

              if (lastMarker !== null) {
                lastMarker.setMap(null);
                lastMarker = null;
              }

              if (lastCircle !== null) {
                lastCircle.setMap(null);
                lastCircle = null;
              }

              radius = $(".custom-select").val();
              if (radius == "Избери радиус") {
                radius = 5000;
                map.setZoom(12);
              } else if (radius == "1") {
                radius = 5000;
                map.setZoom(12);
              } else if (radius == "2") {
                radius = 10000;
                map.setZoom(11);
              } else if (radius == "3") {
                radius = 50000;
                map.setZoom(9);
              }

              lastCircle = new google.maps.Circle({
                strokeColor: "#619bf9",
                strokeOpacity: 0.8,
                strokeWeight: 2,
                fillColor: "#a3c6ff",
                fillOpacity: 0.35,
                map: map,
                center: pos,
                radius: parseInt(radius)
              });

              lastMarker = new google.maps.Marker({
                position: pos,
                animation: google.maps.Animation.DROP,
                map: map,
                draggable: true
              });

              google.maps.event.addListener(lastMarker, "dragend", function (
                event
              ) {
                var latitude = event.latLng.lat();
                var longitude = event.latLng.lng();
                lat = latitude;
                lng = longitude;

                positionAfterDragend = {
                  lat: latitude,
                  lng: longitude
                };

                if (lastCircle !== null) {
                  lastCircle.setMap(null);
                  lastCircle = null;
                }

                lastCircle = new google.maps.Circle({
                  strokeColor: "#619bf9",
                  strokeOpacity: 0.8,
                  strokeWeight: 2,
                  fillColor: "#a3c6ff",
                  fillOpacity: 0.35,
                  map: map,
                  center: positionAfterDragend,
                  radius: parseInt(radius)
                });
                // alert(lat + " " + lng);
                geocodeLatLng(geocoder, map, latitude, longitude);
              });
            }
          }
        );

        return false;
      });
    }
  });
}

function geocodeLatLng(geocoder, map, lat, lng) {
  // var input = document.getElementById("latlng").value;
  // var latlngStr = input.split(",", 2);
  var latlng = {
    lat: parseFloat(lat),
    lng: parseFloat(lng)
  };
  geocoder.geocode({
      location: latlng
    },
    function (results, status) {
      if (status === "OK") {
        if (results[0]) {
          map.setZoom(11);
          // alert(results[0].formatted_address);
          $(".searchedCity").val(results[0].formatted_address);
        } else {
          window.alert("No results found");
        }
      } else {
        window.alert("Geocoder failed due to: " + status);
      }
    }
  );
}

function stripHtml(html) {
  // Create a new div element
  var temporalDivElement = document.createElement("div");
  // Set the HTML content with the providen
  temporalDivElement.innerHTML = html;
  // Retrieve the text property of the element (cross-browser support)
  return temporalDivElement.textContent || temporalDivElement.innerText || "";
}

if (annyang) {
  var commands = {
    здравей: function () {
      var audio = new Audio("/static/audio/greeting.mp3");
      audio.play();
    },
    спри: function () {
      var audio = new Audio("/static/audio/goodbye.mp3");
      audio.play();
      SpeechKITT.abortRecognition();
    },
    "изключи се": function () {
      var audio = new Audio("/static/audio/goodbye.mp3");
      audio.play();
      SpeechKITT.abortRecognition();
    },
    "създай проект": function () {
      window.location.href = "/create-project";
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
    вход: function () {
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
    мими: function () {
      alert("Обичам те");
    },
    "кажи здравей за демото": function () {
      var audio = new Audio("/static/audio/greeting.mp3");
      audio.play();
    },
    "проекти търсещи *skillset": function (skillset) {
      var value = skillset;

      var text = $(".dropdown-item").text() + "";
      if (text.toLowerCase().includes(value.toLowerCase())) {
        $("#dropdown-filter").html('<i class="fa fa-caret-down" style="margin-right: 5px;"></i> ' + text + '<div class="rippleJS"></div>');
        sendGetPages($("#search-by-city").val(), false);
      } else {
        alert("Не съществува такава категория");
      }
    },
    "търси *name": function (name) {
      $("#search-user-name").val(name);
      sendGetPages($("#search-by-city").val(), false);
    },
    "намиращ се в *city": function (city) {
      $("#search-by-city").val(city);
      sendGetPages(city, false);
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

$(".dropdown-item").click(function () {
  var value = $(this).text();
  $("#dropdown-filter").html(
    "Търси за: " + value + '<div class="rippleJS"></div>'
  );
  // $("#dropdown-filter").html('');
  sendGetPages($("#search-by-city").val(), false);
});

$("#start-a-project-btn").click(function () {
  window.location.href = "/create-project";
});

getPages("", "", "", false);

$("#search-by-city").keypress(function (event) {
  if (event.keyCode == 13 || event.which == 13) {
    sendGetPages($("#search-by-city").val(), false);
    event.preventDefault();
  }
});

$("#btn-search-user-name").click(function (event) {
  event.preventDefault();
  sendGetPages($("#search-by-city").val(), false);
});

$("#filter-by-map").click(function (event) {
  event.preventDefault();
  if (lastMarker == null) {
    iziToast.error({
      title: "Грешка",
      message: "Трябва да се въведе адрес",
      position: "topRight"
    });
  } else {
    sendGetPages($("#search-by-city").val(), true);
    $("#close-map").trigger("click");
  }
});

function sendGetPages(city, isByMap) {
  var name = $("#search-user-name").val();
  // var userCategoryRaw = $("#dropdown-filter").text();
  // var userCategory = userCategoryRaw.substring(10, userCategoryRaw.length);
  // userCategory =
  //   userCategory.substring(0, 1).toUpperCase() +
  //   userCategory.substring(1).toLowerCase();
  // if (
  //   userCategory === "            Проекти търсещи: " ||
  //   userCategory === "Всички" ||
  //   userCategory === "            проекти търсещи: " ||
  //   userCategory === "        проекти търсещи:              "
  // ) {
  //   userCategory = "";
  // }

  if (isByMap) {
    getPages(name, currentUserCategory, city, true);
  } else {
    getPages(name, currentUserCategory, city, false);
  }
}

function getPages(name, userCategory, city, isByMap) {
  $.ajax({
    url: "/api/getProjectPages?name=" +
      name +
      "&userCategory=" +
      userCategory +
      "&city=" +
      city +
      "&isByMap=" +
      isByMap +
      "&latitude=" +
      lat +
      "&longitude=" +
      lng +
      "&radius=" +
      radius,
    method: "GET",
    success: function (result) {
      if (result.numberOfPages !== 0) {
        $(".card-title-people").text(
          "Проекти търсещи партньори: " + result.numberOfEntries
        );
        $("#pagination-projects").remove();
        $(".pagination-box").append(
          $("<div/>")
          .attr("style", "text-align:center;")
          .append(
            $("<ul/>")
            .attr("id", "pagination-projects")
            .addClass("pagination-lg")
            .addClass("justify-content-end")
          )
        );
        $("#pagination-projects").twbsPagination({
          totalPages: result.numberOfPages,
          visiblePages: 7,
          first: "Първи",
          prev: "Предишен",
          next: "Следващ",
          last: "Последен",
          onPageClick: function (event, page) {
            getProjects(name, userCategory, city, isByMap);
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

function getProjects(name, userCategory, city, isByMap) {
  $(".card-deck")
    .removeClass("animated")
    .removeClass("zoomIn");

  $(".card-deck").empty();

  $(".card-deck").append($("<div/>").addClass("loader-entries"));

  $.ajax({
    url: "/api/projects?page=" +
      $("#pagination-projects .active").text() +
      "&name=" +
      name +
      "&userCategory=" +
      userCategory +
      "&city=" +
      city +
      "&isByMap=" +
      isByMap +
      "&latitude=" +
      lat +
      "&longitude=" +
      lng +
      "&radius=" +
      radius,
    method: "GET",
    success: function (result) {
      populateProjects(result);
    }
  });
}

function populateProjects(result) {
  $(".card-deck").empty();
  var projects = result.projects;

  for (var i = projects.length - 1; i >= 0; i -= 1) {
    var projectNeeds = "";
    if (projects[i].projectNeeds.length !== 0) {
      for (var j = 0; j < projects[i].projectNeeds.length; j += 1) {
        projectNeeds += projects[i].projectNeeds[j].name;
        if (j !== projects[i].projectNeeds.length - 1) {
          projectNeeds += ", ";
        }
      }
    } else {
      projectNeeds = "Този проект не се нуждае от никого за момента";
    }

    var isMember = false;
    for (var k = 0; k < projects[i].projectMembers.length; k += 1) {
      if (projects[i].projectMembers[k].id === parseInt(id)) {
        isMember = true;
      }
    }

    if (!isMember) {
      $("#card-deck-projects-logged")
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
                .attr("id", "creator-id_" + projects[i].creator)
                .addClass("front")
                .append(
                  $("<a/>")
                  .attr("id", "user-cover_" + projects[i].id)
                  .addClass("should-send-project-id")
                  .append(
                    $("<div/>")
                    .addClass("cover-rotating")
                    .addClass("overlay")
                    .addClass("view")
                    .append(
                      $("<img/>").attr("src", projects[i].cover)
                    )
                    .append(
                      $("<a/>").append(
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
                  .attr("style", "min-height: 215px;")
                  .append(
                    $("<div/>")
                    .attr(
                      "id",
                      "creator-id2_" + projects[i].creator
                    )
                    .addClass("main")
                    .append(
                      $("<a/>")
                      .attr("id", "card-name_" + projects[i].id)
                      .attr("href", "/project/" + projects[i].id)
                      .addClass("should-send-project-id")
                      .addClass("name")
                      .addClass("card-sname")
                      .text(projects[i].name)
                    )
                    .append(
                      $("<p/>")
                      .addClass("text-center")
                      .html(
                        '<i class="fas fa-map-marker-alt fa-3x"></i>' +
                        projects[i].city +
                        ", България"
                      )
                    )
                    .append('<div class="informations-holder"><div class="sub-informations-holder custom-class-for-js_' + projects[i].id + '"><span class="information-by">От <a id="card-creator-id_' + projects[i].creator + '" class="informations-holder-creator-name-redirector"><span class="project-creator-name-link">' + projects[i].creatorName + '</span></a></span></div><div class="sub1-informations-holder">' + projects[i].timesBacked + ' спонсорирания</div></div>')
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
                  .append(
                    $("<h3/>")
                    .addClass("motto")
                    .text(
                      stripHtml(
                        projects[i].strippedDescription
                      ).substring(0, 126) + "..."
                    )
                  )
                )
                .append(
                  $("<div/>")
                  .attr(
                    "style",
                    "position: relative; margin-top: 75px;"
                  )
                  .addClass("content")
                  .append(
                    $("<div/>")
                    .addClass("main")
                    .append(
                      $("<h4/>")
                      .addClass("text-center")
                      .text("Проектът се нуждае от")
                    )
                    .append(
                      $("<p/>")
                      .addClass("text-center")
                      .attr("style", "font-size: 13px;")
                      .text(projectNeeds)
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
                    .html('<i class="fa fa-reply"></i> Back')
                  )
                )
              )
            )
          )
        );
    } else {
      $("#card-deck-projects-logged")
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
                .attr("id", "creator-id_" + projects[i].creator)
                .addClass("front")
                .append(
                  $("<a/>")
                  .attr("id", "user-cover_" + projects[i].id)
                  .addClass("should-send-project-id")
                  .addClass("isProjectMember")
                  .append(
                    $("<div/>")
                    .addClass("cover-rotating")
                    .addClass("overlay")
                    .addClass("view")
                    .append(
                      $("<img/>").attr("src", projects[i].cover)
                    )
                    .append(
                      $("<a/>").append(
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
                  .attr("style", "min-height: 215px;")
                  .append(
                    $("<div/>")
                    .attr(
                      "id",
                      "creator-id2_" + projects[i].creator
                    )
                    .addClass("main")
                    .append(
                      $("<a/>")
                      .attr("id", "card-name_" + projects[i].id)
                      .attr("href", "/project/" + projects[i].id)
                      .addClass("isProjectMember")
                      .addClass("should-send-project-id")
                      .addClass("name")
                      .addClass("card-name")
                      .text(projects[i].name)
                    )
                    .append(
                      $("<p/>")
                      .addClass("text-center")
                      .html(
                        '<i class="fas fa-map-marker-alt fa-3x"></i>' +
                        projects[i].city +
                        ", България</p>"
                      )
                    )
                    .append('<div class="informations-holder"><div class="sub-informations-holder custom-class-for-js_' + projects[i].id + '"><span class="information-by">От <a id="card-creator-id_' + projects[i].creator + '" class="informations-holder-creator-name-redirector"><span class="project-creator-name-link">' + projects[i].creatorName + '</span></a></span></div><div class="sub1-informations-holder">' + projects[i].timesBacked + ' спонсорирания</div></div>')
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
                  .append(
                    $("<h3/>")
                    .addClass("motto")
                    .text(
                      stripHtml(
                        projects[i].strippedDescription
                      ).substring(0, 126) + "..."
                    )
                  )
                )
                .append(
                  $("<div/>")
                  .attr(
                    "style",
                    "position: relative; margin-top: 75px;"
                  )
                  .addClass("content")
                  .append(
                    $("<div/>")
                    .addClass("main")
                    .append(
                      $("<h4/>")
                      .addClass("text-center")
                      .text("Проектът се нуждае от")
                    )
                    .append(
                      $("<p/>")
                      .addClass("text-center")
                      .attr("style", "font-size: 13px;")
                      .text(projectNeeds)
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
                    .html('<i class="fa fa-reply"></i> Back')
                  )
                )
              )
            )
          )
        );
    }

    if (projects[i].topProject === 'true' || projects[i].topProject === true) {
      $(".custom-class-for-js_" + projects[i].id).append('<div><i class="fab fa-gratipay" style="color: #2196f3;"></i> <span style="font-size: 12px; color: #282828">топ проект</span></div>')
    } else {
      $(".custom-class-for-js_" + projects[i].id).parent().attr("style", "min-height: 45px;");
    }
  }

  $(".informations-holder-creator-name-redirector").on("click", function () {
    var instance = $(this);
    var indexOfUndescore = instance.attr("id").indexOf("_");
    var userIdToSend = instance.attr("id").substring(indexOfUndescore + 1);

    if (userIdToSend !== id) {
      instance.attr("href", "/user/" + userIdToSend);
    } else {
      instance.attr("href", "/profile");
    }
  });

  $(".should-send-project-id").on("click", function () {
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

    var isProjectMember = instance.hasClass("isProjectMember");

    if (isProjectMember && creatorId !== id) {
      instance.attr("href", "/project-member/" + projectIdToSend);
    } else if (creatorId === id) {
      instance.attr("href", "/project-admin/" + projectIdToSend);
    } else {
      instance.attr("href", "/project/" + projectIdToSend);
    }
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