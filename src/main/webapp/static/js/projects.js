$(".dropdown-item").click(function() {
  var value = $(this).text();
  $("#dropdown-filter").text("Търси за: " + value);

  sendGetPages($("#search-by-city").val());
});

$("#nav-button").click(function() {
  window.location.href = "/sign-in";
});

$(function() {
  function split(val) {
    return val.split(/,\s*/);
  }
  function extractLast(term) {
    return split(term).pop();
  }

  $("#search-by-city").autocomplete({
    source: "/api/getCities",
    minLength: 2,
    select: function(event, ui) {
      sendGetPages(ui.item.value);
    },
    autoFocus: true
  });
});

$("#btn-search-user-name").click(function(event) {
  event.preventDefault();
  sendGetPages($("#search-by-city").val());
});

function sendGetPages(city) {
  var name = $("#search-user-name").val();
  var userCategoryRaw = $("#dropdown-filter").text();
  var userCategory = userCategoryRaw.substring(10, userCategoryRaw.length);
  if (userCategory === "            Проекти търсещи: " || userCategory === "Всички") {
    userCategory = "";
  }

  getPages(name, userCategory, city);
}

getPages("", "", "");

function getPages(name, userCategory, city) {
  $.ajax({
    url:
      "/api/getProjectPages?name=" +
      name +
      "&userCategory=" +
      userCategory +
      "&city=" +
      city,
    method: "GET",
    success: function(result) {
      if (result.numberOfPages !== 0) {
        $(".card-title-people").text("Проекти търсещи партньори: " + result.numberOfEntries);
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
          onPageClick: function(event, page) {
            getProjects(name, userCategory, city);
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

function getProjects(name, userCategory, city) {
  $(".card-deck")
    .removeClass("animated")
    .removeClass("zoomIn");

  $(".card-deck").empty();

  $(".card-deck").append($("<div/>").addClass("loader-entries"));

  $.ajax({
    url:
      "/api/projects?page=" +
      $("#pagination-projects .active").text() +
      "&name=" +
      name +
      "&userCategory=" +
      userCategory +
      "&city=" +
      city,
    method: "GET",
    success: function(result) {
      populateProjects(result);
    }
  });
}

function populateProjects(result) {
  $(".card-deck").empty();
  var projects = result.projects;

  for (var i = 0; i < projects.length; i += 1) {
    var projectNeeds = "";
    for (var j = 0; j < projects[i].projectNeeds.length; j++) {
      projectNeeds += projects[i].projectNeeds[j].name;
      if (j !== projects[i].projectNeeds.length - 1) {
        projectNeeds += ", ";
      }
    }

    $("#card-deck-projects-logged")
      .addClass("animated")
      .addClass("zoomIn")
      .append(
        $("<div/>")
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
                          .attr("href", "/project/" + projects[i].id)
                          .attr("id", "user-cover_" + projects[i].id)
                          .attr(
                            "title",
                            "За да можеш да видиш този проект трябва да влезеш в акаунта си"
                          )
                          .addClass("prevent")
                          .append(
                            $("<div/>")
                              .addClass("cover-rotating")
                              .append(
                                $("<img/>").attr(
                                  "src",
                                  "data:image/png;base64, " + projects[i].cover
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
                              .append(
                                $("<a/>")
                                  .attr("id", "card-name_" + projects[i].id)
                                  .attr(
                                    "title",
                                    "За да можеш да видиш този проект трябва да влезеш в акаунта си"
                                  )
                                  .addClass("prevent")
                                  .attr("href", "/project/" + projects[i].id)
                                  .addClass("name")
                                  .addClass("card-name")
                                  .text(projects[i].name)
                              )
                              .append(
                                $("<p/>")
                                  .addClass("text-center")
                                  .html(
                                    '<i class="fa fa-map-marker fa-3x"></i>' +
                                      projects[i].city.name +
                                      ", България</p>"
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
                                projects[i].description.substring(0, 126) +
                                  "..."
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

  tippy("[title]", {
    placement: "top",
    animation: "perspective",
    duration: 700,
    arrow: true,
    arrowType: "round",
    interactive: true,
    size: "large"
  });

  $(".prevent").click(function(event) {
    event.preventDefault();
    iziToast.error({
      title: "Грешка",
      message:
        "За да можеш да видиш този проект трябва първо да влезеш в акаунта си",
      position: "topRight"
    });
  });

  $(".preloader").fadeOut(500);
}
