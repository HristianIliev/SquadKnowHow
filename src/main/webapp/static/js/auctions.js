$(document).ready(function () {
  $("#tour-step-3").addClass("show");
  $(".showHere").addClass("show");

  function stripHtml(html) {
    // Create a new div element
    var temporalDivElement = document.createElement("div");
    // Set the HTML content with the providen
    temporalDivElement.innerHTML = html;
    // Retrieve the text property of the element (cross-browser support)
    return temporalDivElement.textContent || temporalDivElement.innerText || "";
  }

  var indexOfUndescoreForUserId = $("body")
    .attr("id")
    .indexOf("_");
  var id = $("body")
    .attr("id")
    .substring(indexOfUndescoreForUserId + 1);

  $("select").niceSelect();

  populateAuctions("timeAsc");

  $("#sort-select").on("change", function (e) {
    populateAuctions($("#sort-select").val());
  });

  function populateAuctions(sortBy) {
    $.ajax({
      url: "/api/getAuctionPages",
      method: "GET",
      success: function (result) {
        if (result.numberOfPages !== 0) {
          $(".card-title-people").text(
            "Търгове за права на проект: " + result.numberOfEntries
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
            onPageClick: function (event, page) {
              $(".auctions-content")
                .removeClass("animated")
                .removeClass("zoomIn");

              $(".auctions-content").empty();

              $(".auctions-content").append(
                $("<div/>")
                .addClass("loader-entries")
                .attr(
                  "style",
                  "margin-bottom: 10px; width: 50px;height: 50px; border: 4px solid #f3f3f3;border-top: 4px solid #3498db;"
                )
              );
              $.ajax({
                url: "/api/getAuctions?sortBy=" +
                  sortBy +
                  "&page=" +
                  $("#pagination-projects .active").text(),
                method: "GET",
                success: function (result) {
                  $.ajax({
                    url: "/api/getFollowingAuctions?userId=" + id,
                    method: "GET",
                    success: function (followingAuctions) {
                      $(".auctions-content").empty();

                      if (result.length === 0) {} else {
                        for (var i = result.length - 1; i >= 0; i -= 1) {
                          var highestBidAmount = 0;
                          for (var j = 0; j < result[i].bids.length; j++) {
                            if (result[i].bids[j].amount > highestBidAmount) {
                              highestBidAmount = result[i].bids[j].amount;
                            }
                          }

                          $(".auctions-content")
                            .addClass("animated")
                            .addClass("zoomIn")
                            .append(
                              $("<div/>")
                              .addClass("row")
                              .append(
                                $("<div/>")
                                .addClass("col-md-12")
                                .append(
                                  $("<div/>")
                                  .addClass("changeRow")
                                  .addClass("row")
                                  .append(
                                    $("<div/>")
                                    .addClass("col-md-3")
                                    .addClass("col-sm-3")
                                    .addClass("needs-regulating-2")
                                    .attr("style", "margin: 20px;")
                                    .attr(
                                      "id",
                                      "creatorId_" + result[i].creatorId
                                    )
                                    .append(
                                      $("<img/>")
                                      .addClass("auction-picture")
                                      .addClass(
                                        "auction-redirect-page"
                                      )
                                      .attr(
                                        "id",
                                        "auctionId_" + result[i].id
                                      )
                                      .attr(
                                        "src",
                                        result[i].picture
                                      )
                                    )
                                  )
                                  .append(
                                    $("<div/>")
                                    .addClass("col-md-7")
                                    .addClass("col-sm-7")
                                    .addClass("auction-description")
                                    .attr(
                                      "id",
                                      "creatorIdTitle_" +
                                      result[i].creatorId
                                    )
                                    .append(
                                      $("<h3/>")
                                      .addClass("auction-title")
                                      .addClass(
                                        "auction-redirect-page"
                                      )
                                      .attr(
                                        "id",
                                        "auctionIdTitle_" +
                                        result[i].id
                                      )
                                      .text(result[i].title)
                                    )
                                    .append(
                                      $("<h4/>")
                                      .addClass("secondary-info")
                                      .addClass(
                                        "auction-item-description"
                                      )
                                      .text(
                                        stripHtml(
                                          result[i]
                                          .strippedDescription
                                        ).substring(0, 180) + "..."
                                      )
                                    )
                                    .append(
                                      $("<div/>")
                                      .addClass("row")
                                      .append(
                                        $("<div/>")
                                        .addClass("col-md-6")
                                        .addClass("col-sm-6")
                                        .append(
                                          $("<div/>")
                                          .attr(
                                            "id",
                                            "itemPrice_" +
                                            result[i].id
                                          )
                                          .addClass(
                                            "auction-item-price"
                                          )
                                          .html(
                                            '<img class="item-icon" src="data:image/png;base64,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"> ' +
                                            highestBidAmount +
                                            " USD"
                                          )
                                        )
                                        .append(
                                          $("<div/>")
                                          .addClass(
                                            "secondary-info"
                                          )
                                          .attr(
                                            "style",
                                            "margin-top: 10px;"
                                          )
                                          .html(
                                            '<img class="item-icon" src="data:image/png;base64,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"> ' +
                                            result[i].bids
                                            .length +
                                            " залагания"
                                          )
                                        )
                                        .append(
                                          $("<div/>")
                                          .addClass(
                                            "secondary-info"
                                          )
                                          .attr(
                                            "style",
                                            "margin-top: 10px;"
                                          )
                                          .html(
                                            '<img class="item-icon" src="data:image/png;base64,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"> ' +
                                            result[i]
                                            .usersWatchingNumber +
                                            " следват този търг"
                                          )
                                        )
                                      )
                                      .append(
                                        $("<div/>")
                                        .addClass("col-md-6")
                                        .addClass("col-sm-6")
                                        .append(
                                          $("<div/>")
                                          .addClass(
                                            "secondary-info"
                                          )
                                          .attr(
                                            "id",
                                            "display_" + i
                                          )
                                          .html(
                                            '<img class="item-icon" src="data:image/png;base64,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"> Остават: <div id="time_' +
                                            i +
                                            '" style="display: inline-block;"></div>'
                                          )
                                        )
                                        .append(
                                          $("<div/>")
                                          .addClass(
                                            "secondary-info"
                                          )
                                          .attr(
                                            "style",
                                            "margin-top: 10px;"
                                          )
                                          .html(
                                            '<img class="item-icon" src="data:image/png;base64,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"> От ' +
                                            result[i].location
                                          )
                                        )
                                      )
                                    )
                                  )
                                  .append(
                                    $("<div/>")
                                    .addClass("col-md-1")
                                    .addClass("col-sm-1")
                                    .append(
                                      $("<input/>")
                                      .attr("type", "checkbox")
                                      .attr(
                                        "id",
                                        "follow_" + result[i].id
                                      )
                                      .text("+ Последвай")
                                    )
                                  )
                                )
                                .append(
                                  $("<hr/>").addClass("auction-divider")
                                )
                              )
                            );

                          var tokens = result[i].endDate.split("-");

                          $("#time_" + i)
                            .countdown(
                              tokens[0] +
                              "/" +
                              tokens[1] +
                              "/" +
                              tokens[2].substring(0, 2) +
                              " " +
                              tokens[2].substring(3, 8),
                              function (event) {
                                $(this).html(
                                  event.strftime("%-w седмици %-d дни %-H:%-M:%-S")
                                );
                              }
                            )
                            .on("finish.countdown", function () {
                              $.ajax({
                                url: "/api/finishAuction?auctionId=" + auctionId,
                                method: "GET",
                                success: function () {
                                }
                              });
                              $("#display_" + i).html(
                                '<img class="item-icon" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAUSSURBVHhe7ZtvbBNlHMc7eWF4YYxKNL5TE40GNUZtt8QwNSFiBBeEGbnbkGTtwLBIzBYVDC6QLK2KDDFTWNTJ1la24trB2FAoo8PJpsPg/ii8sBrZvw5B3AZM2PDn/Y7nbne9p91Nr9vD8nySb9LdPf093+9369Yud7bpBgDSPL6Q6PYGm92+4AWUxxuKSF8LeI4sm50EAoE5UlC/VABQ5Q35cA1ZPvvweIPFE2GDY1LglmuSHk+U8BZZPrvYWlMz1+MLDmFI6cd+2O2ttZNTNnwsBR+5VkJwaHtj443k1OzB46vLVL/LvlAJOawiFeCeOF+XSQ6zyWPC6nnponOdQ3B+ni66AloVfvBpD00bdlT9oQRcv7NqDxmlgseU87iWNgMVvx96cIiuAvRERqUWR44rWwr/l7Q50KSESKZNFdUXyTiVTZ9VX6KtjRdtT1mC63y6kL+cjEsNdsG1SGp7nGqAiGaapo3l/t7XSnc+g9pY7uujraGJtqci9JYh5i8kdi0nzSE6T9I21opm2krR9tRKKuFn4tdapGYf0m70xMo1UFV/AMJt7TrRTFup+P0q9zbKXrTeHs/Ne5DYtg57jusF7SZFWz6U3rgZecdfRzVuhXA2DfSi9YZeiW3rsItOUbtJcdknZHs9FQ1NVPNWCGfTQC9ab+iV2LYOswWcHjwL2wIN1AD/Rziz58xZsosepgpARi9fgWhfDE7+3muJcBbOTARzBUw3vABeAC+AF6D1xgvgBfAC2C7g4tgodP4ZhcbeVln4GI/9V66bAvaeboHnD6+Hud6FYNu1QCc8liWdq+/5lqw2D/MF/DLcCwsOFBhCJ9JTX62D6HAfefbkMF1AJHYCbtu9mBo0mebtXgJHYx1kSnKYLQBf2zd/8Sw1oBnhc7vO/0qmJYbJAkbHL8M9tS9Rg01F9wVF+Hs88SdBhMkC3u3yUwOZ0ZzKTN3X73dXk6l0mCtg/J+rcHt1li6EGaXtyoQNx7fA1q5yXQl31GTBVWlmIpgroDn2oy6YGWH4t38ohZ/OtcrafGK77nzLYCeZboS5Ako6KnXmFd2S4BfiDZVPyoGV8O1nmsFev0q3xt3pJdONMFfA2tZSnXnUI/tyoS12BLKbinTH8Ufd3VGmhv9u8Ag8uu9l3RpUQVspmW6EuQJWflOiM4/feQyPAbvPHVNLwPDvdexQw+Oah+tydM9VhDMTwVwBhd+XGQJgaAyvlLAi8gZs6y5Xwx8bOAzzQ4LheYqK2svIdCPMFfDRqSA1xPKmQrUErVoGwnB/KPl7ho9Phch0I8wVgO/jaSFQ8SUc7T8E99ZmU9dq9dvIAJluhLkCEMf+1dQgKKWESP/XcPeXy6hrtMpoeIVMpcNkAQf72qlhFC0Ovwp37Zk8PCrcf5xMpcNkAYjQvJkaaCpK9ttfgdkC8L88yV4Kkyl9/xpT/ylitgBk6MoFeC78OjVgMi0Jvyk/1wxMF4DghyP802jmA9KdNUvltck+/MTDfAEKI2OXwBc9CC9GiuGBUC7c5F8kCx/jMX/0kLxmqlw3BaQKXgAvgBfAC9B64wXwAngBvABi2zrMXio7E8RfKusQ8pYS29ZBu1gaL1SOv3h5ukW7WNq+wjWf2LYWvBRduxGbcnYTu9aDNyNMdsPETAq9OcS8p4nd1IC3pci3p1AMzKhkT85lxGZqkW+aEvLXSo1XSJvrbmKabskeJC8Z2c5biT0Oh8PhmMNm+xfD36yEUJ6ReQAAAABJRU5ErkJggg=="> Приключил на ' +
                                tokens[0] +
                                "/" +
                                tokens[1] +
                                "/" +
                                tokens[2].substring(0, 2) +
                                " " +
                                tokens[2].substring(3, 8)
                              );
                            })
                            .on("update.countdown", function (event) {
                              if (event.offset.totalDays === 0) {
                                $(this).addClass("red-text");
                              }
                            });

                          if ($("#time_" + i).html() === "0 дни 0:0:0") {
                            $("#display_" + i).html(
                              '<img class="item-icon" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAUSSURBVHhe7ZtvbBNlHMc7eWF4YYxKNL5TE40GNUZtt8QwNSFiBBeEGbnbkGTtwLBIzBYVDC6QLK2KDDFTWNTJ1la24trB2FAoo8PJpsPg/ii8sBrZvw5B3AZM2PDn/Y7nbne9p91Nr9vD8nySb9LdPf093+9369Yud7bpBgDSPL6Q6PYGm92+4AWUxxuKSF8LeI4sm50EAoE5UlC/VABQ5Q35cA1ZPvvweIPFE2GDY1LglmuSHk+U8BZZPrvYWlMz1+MLDmFI6cd+2O2ttZNTNnwsBR+5VkJwaHtj443k1OzB46vLVL/LvlAJOawiFeCeOF+XSQ6zyWPC6nnponOdQ3B+ni66AloVfvBpD00bdlT9oQRcv7NqDxmlgseU87iWNgMVvx96cIiuAvRERqUWR44rWwr/l7Q50KSESKZNFdUXyTiVTZ9VX6KtjRdtT1mC63y6kL+cjEsNdsG1SGp7nGqAiGaapo3l/t7XSnc+g9pY7uujraGJtqci9JYh5i8kdi0nzSE6T9I21opm2krR9tRKKuFn4tdapGYf0m70xMo1UFV/AMJt7TrRTFup+P0q9zbKXrTeHs/Ne5DYtg57jusF7SZFWz6U3rgZecdfRzVuhXA2DfSi9YZeiW3rsItOUbtJcdknZHs9FQ1NVPNWCGfTQC9ab+iV2LYOswWcHjwL2wIN1AD/Rziz58xZsosepgpARi9fgWhfDE7+3muJcBbOTARzBUw3vABeAC+AF6D1xgvgBfAC2C7g4tgodP4ZhcbeVln4GI/9V66bAvaeboHnD6+Hud6FYNu1QCc8liWdq+/5lqw2D/MF/DLcCwsOFBhCJ9JTX62D6HAfefbkMF1AJHYCbtu9mBo0mebtXgJHYx1kSnKYLQBf2zd/8Sw1oBnhc7vO/0qmJYbJAkbHL8M9tS9Rg01F9wVF+Hs88SdBhMkC3u3yUwOZ0ZzKTN3X73dXk6l0mCtg/J+rcHt1li6EGaXtyoQNx7fA1q5yXQl31GTBVWlmIpgroDn2oy6YGWH4t38ohZ/OtcrafGK77nzLYCeZboS5Ako6KnXmFd2S4BfiDZVPyoGV8O1nmsFev0q3xt3pJdONMFfA2tZSnXnUI/tyoS12BLKbinTH8Ufd3VGmhv9u8Ag8uu9l3RpUQVspmW6EuQJWflOiM4/feQyPAbvPHVNLwPDvdexQw+Oah+tydM9VhDMTwVwBhd+XGQJgaAyvlLAi8gZs6y5Xwx8bOAzzQ4LheYqK2svIdCPMFfDRqSA1xPKmQrUErVoGwnB/KPl7ho9Phch0I8wVgO/jaSFQ8SUc7T8E99ZmU9dq9dvIAJluhLkCEMf+1dQgKKWESP/XcPeXy6hrtMpoeIVMpcNkAQf72qlhFC0Ovwp37Zk8PCrcf5xMpcNkAYjQvJkaaCpK9ttfgdkC8L88yV4Kkyl9/xpT/ylitgBk6MoFeC78OjVgMi0Jvyk/1wxMF4DghyP802jmA9KdNUvltck+/MTDfAEKI2OXwBc9CC9GiuGBUC7c5F8kCx/jMX/0kLxmqlw3BaQKXgAvgBfAC9B64wXwAngBvABi2zrMXio7E8RfKusQ8pYS29ZBu1gaL1SOv3h5ukW7WNq+wjWf2LYWvBRduxGbcnYTu9aDNyNMdsPETAq9OcS8p4nd1IC3pci3p1AMzKhkT85lxGZqkW+aEvLXSo1XSJvrbmKabskeJC8Z2c5biT0Oh8PhmMNm+xfD36yEUJ6ReQAAAABJRU5ErkJggg=="> Приключил на ' +
                              tokens[0] +
                              "/" +
                              tokens[1] +
                              "/" +
                              tokens[2].substring(0, 2) +
                              " " +
                              tokens[2].substring(3, 8)
                            );
                          }

                          $("#follow_" + result[i].id).change(function () {
                            var indexOfUndescoreForAuctionId = $(this)
                              .attr("id")
                              .indexOf("_");
                            var auctionId = $(this)
                              .attr("id")
                              .substring(indexOfUndescoreForAuctionId + 1);

                            if (this.checked) {
                              $.ajax({
                                url: "/api/startFollowingAuction?auctionId=" +
                                  auctionId +
                                  "&userId=" +
                                  id,
                                method: "GET",
                                success: function (result) {}
                              });
                            } else {
                              $.ajax({
                                url: "/api/stopFollowingAuction?auctionId=" +
                                  auctionId +
                                  "&userId=" +
                                  id,
                                method: "GET",
                                success: function (result) {}
                              });
                            }
                          });

                          var isCheckboxChecked = false;
                          for (
                            var k = 0; k < followingAuctions.length; k += 1
                          ) {
                            if (result[i].id === followingAuctions[k]) {
                              isCheckboxChecked = true;
                              break;
                            }
                          }

                          if (isCheckboxChecked) {
                            $("#follow_" + result[i].id).prop("checked", true);
                          }

                          $(".auction-redirect-page").click(function () {
                            var indexOfUndescoreForAuctionId = $(this)
                              .attr("id")
                              .indexOf("_");
                            var auctionId = $(this)
                              .attr("id")
                              .substring(indexOfUndescoreForAuctionId + 1);

                            var indexOfUndescoreForCreatorId = $(this)
                              .parent()
                              .attr("id")
                              .indexOf("_");
                            var creatorId = $(this)
                              .parent()
                              .attr("id")
                              .substring(indexOfUndescoreForCreatorId + 1);

                            if (creatorId === id) {
                              window.location.href =
                                "/auction-admin/" + auctionId;
                            } else {
                              window.location.href = "/auction/" + auctionId;
                            }
                          });

                          if (
                            result[i].buyMeNow !== null &&
                            result[i].buyMeNow !== "null"
                          ) {
                            $("#itemPrice_" + result[i].id).html(
                              '<img class="item-icon" src="data:image/png;base64,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"> ' +
                              highestBidAmount +
                              " $ или купи веднага за " +
                              result[i].buyMeNow +
                              " $"
                            );
                          }
                        }
                      }
                    }
                  });
                }
              });
            }
          });
        } else {
          $(".card-title-people").text("Търгове за права на проект: " + 0);
          $(".auctions-content")
            .removeClass("animated")
            .removeClass("zoomIn");

          $(".auctions-content").empty();

          $(".auctions-content").append(
            $("<div/>")
            .addClass("loader-entries")
            .attr(
              "style",
              "margin-bottom: 10px; width: 50px;height: 50px; border: 4px solid #f3f3f3;border-top: 4px solid #3498db;"
            )
          );
          $(".auctions-content").empty();
          $(".auctions-content")
            .addClass("animated")
            .addClass("zoomIn")
            .append(
              $("<div/>")
              .attr("style", "text-align: center;font-size: 27px;")
              .text("Не бяха намерени търгове")
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

        $(".preloader").fadeOut(500);
      }
    });
  }
});