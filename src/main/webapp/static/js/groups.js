$("#nav-button").click(function() {
  window.location.href = "/sign-in";
});

$("#btn-search-user-name").click(function(event) {
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
    success: function(result) {
      if (result.numberOfPages !== 0) {
        $(".card-title-people").text("Групи от потребители: " + result.numberOfEntries);
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
          onPageClick: function(event, page) {
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
    url:
      "/api/groups?page=" +
      $("#pagination-groups .active").text() +
      "&name=" +
      name,
    method: "GET",
    success: function(result) {
      populateGroups(result);
    }
  });
}

function populateGroups(result) {
  $(".card-deck").empty();

  var groups = result.groups;
  for (var i = groups.length - 1; i >= 0; i -= 1) {
    var projectsCount = 0;
    for (var j = 0; j < groups[i].members.length; j += 1) {
      projectsCount += groups[i].members[j].projects.length;
    }

    $("#card-deck-groups-logged")
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
                          .attr("href", "/group/" + groups[i].id)
                          .attr("id", "user-cover_" + groups[i].id)
                          .addClass("prevent")
                          .attr(
                            "title",
                            "За да можеш да видиш тази група трябва първо да влезеш в акаунта си"
                          )
                          .append(
                            $("<div/>")
                              .addClass("view")
                              .addClass("overlay")
                              .addClass("cover-rotating")
                              .append(
                                $("<img/>").attr(
                                  "src",
                                  "data:image/png;base64, " + groups[i].logo
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
                                  .addClass("prevent")
                                  .attr(
                                    "title",
                                    "За да можеш да видиш тази група трябва първо да влезеш в акаунта си"
                                  )
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
                                      .append(
                                        $("<h4/>").text(
                                          groups[i].members.length
                                        )
                                      )
                                      .append($("<p/>").text("Участващи"))
                                  )
                                  .append(
                                    $("<div/>")
                                      .addClass("stat")
                                      .addClass("col-md-6")
                                      .append($("<h4/>").text(projectsCount))
                                      .append($("<p/>").text("Проекти"))
                                  )
                              )
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
        "За да можеш да видиш тази група трябва първо да влезеш в акаунта си",
      position: "topRight"
    });
  });

  $(".preloader").fadeOut(500);
}
