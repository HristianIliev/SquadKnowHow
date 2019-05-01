$(document).ready(function() {
  var hasGoneToSecondPage = false;
  var hasNewImages = false;
  var secondUpload = false;
  var uploader;

  var user = null;
  var changed = false;

  var arr = window.location.pathname.split("/");
  var projectId = arr[arr.length - 1];

  var indexOfUndescoreForUserId = $("body")
    .attr("id")
    .indexOf("_");
  var id = $("body")
    .attr("id")
    .substring(indexOfUndescoreForUserId + 1);

  var elems = Array.prototype.slice.call(
    document.querySelectorAll(".js-switch")
  );

  function split(val) {
    return val.split(/,\s*/);
  }

  function extractLast(term) {
    return split(term).pop();
  }

  elems.forEach(function(html) {
    var switchery = new Switchery(html, {
      color: "#138fc2",
      secondaryColor: "#cccccc",
      jackColor: "#c7e1f1",
      size: "small"
    });

    html.onchange = function() {
      if (html.checked) {
        if (html.id === "needsProgrammer") {
          showInputs(
            ".needsProgrammer",
            "needsProgrammerSkills",
            "needsProgrammerLanguages",
            "needsProgrammerCity",
            "needsProgrammerEducation"
          );
        } else if (html.id === "needsDesigner") {
          showInputs(
            ".needsDesigner",
            "needsDesignerSkills",
            "needsDesignerLanguages",
            "needsDesignerCity",
            "needsDesignerEducation"
          );
        } else if (html.id === "needsEngineer") {
          showInputs(
            ".needsEngineer",
            "needsEngineerSkills",
            "needsEngineerLanguages",
            "needsEngineerCity",
            "needsEngineerEducation"
          );
        } else if (html.id === "needsWriter") {
          showInputs(
            ".needsWriter",
            "needsWriterSkills",
            "needsWriterLanguages",
            "needsWriterCity",
            "needsWriterEducation"
          );
        } else if (html.id === "needsScientist") {
          showInputs(
            ".needsScientist",
            "needsScientistSkills",
            "needsScientistLanguages",
            "needsScientistCity",
            "needsScientistEducation"
          );
        } else if (html.id === "needsMusician") {
          showInputs(
            ".needsMusician",
            "needsMusicianSkills",
            "needsMusicianLanguages",
            "needsMusicianCity",
            "needsMusicianEducation"
          );
        } else if (html.id === "needsFilmmaker") {
          showInputs(
            ".needsFilmmaker",
            "needsFilmmakerSkills",
            "needsFilmmakerLanguages",
            "needsFilmmakerCity",
            "needsFilmmakerEducation"
          );
        } else if (html.id === "needsProductManager") {
          showInputs(
            ".needsProductManager",
            "needsProductManagerSkills",
            "needsProductManagerLanguages",
            "needsProductManagerCity",
            "needsProductManagerEducation"
          );
        } else if (html.id === "needsArtist") {
          showInputs(
            ".needsArtist",
            "needsArtistSkills",
            "needsArtistLanguages",
            "needsArtistCity",
            "needsArtistEducation"
          );
        }
      } else {
        if (html.id === "needsProgrammer") {
          hideInputs(
            "needsProgrammerSkills",
            "needsProgrammerLanguages",
            "needsProgrammerCity",
            "needsProgrammerEducation"
          );
        } else if (html.id === "needsDesigner") {
          hideInputs(
            "needsDesignerSkills",
            "needsDesignerLanguages",
            "needsDesignerCity",
            "needsDesignerEducation"
          );
        } else if (html.id === "needsEngineer") {
          hideInputs(
            "needsEngineerSkills",
            "needsEngineerLanguages",
            "needsEngineerCity",
            "needsEngineerEducation"
          );
        } else if (html.id === "needsWriter") {
          hideInputs(
            "needsWriterSkills",
            "needsWriterLanguages",
            "needsWriterCity",
            "needsWriterEducation"
          );
        } else if (html.id === "needsScientist") {
          hideInputs(
            "needsScientistSkills",
            "needsScientistLanguages",
            "needsScientistCity",
            "needsScientistEducation"
          );
        } else if (html.id === "needsMusician") {
          hideInputs(
            "needsMusicianSkills",
            "needsMusicianLanguages",
            "needsMusicianCity",
            "needsMusicianEducation"
          );
        } else if (html.id === "needsFilmmaker") {
          hideInputs(
            "needsFilmmakerSkills",
            "needsFilmmakerLanguages",
            "needsFilmmakerCity",
            "needsFilmmakerEducation"
          );
        } else if (html.id === "needsProductManager") {
          hideInputs(
            "needsProductManagerSkills",
            "needsProductManagerLanguages",
            "needsProductManagerCity",
            "needsProductManagerEducation"
          );
        } else if (html.id === "needsArtist") {
          hideInputs(
            "needsArtistSkills",
            "needsArtistLanguages",
            "needsArtistCity",
            "needsArtistEducation"
          );
        }
      }
    };
  });

  function hideInputs(skillsId, languagesId, cityId, educationId) {
    $("#" + skillsId).fadeOut(100, function() {
      $(this).remove();
    });
    $("#" + languagesId).fadeOut(100, function() {
      $(this).remove();
    });
    $("#" + cityId).fadeOut(100, function() {
      $(this).remove();
    });
    $("#" + educationId).niceSelect("destroy");
    $("#" + educationId).remove();
  }

  function showInputs(
    majorSelector,
    skillsId,
    languagesId,
    cityId,
    educationid
  ) {
    $(
      '<input type="text" id="' +
        skillsId +
        '" class="form-control my-form-control skills-create" placeholder="Умеещ (Опционално)" style="margin-bottom:0px;margin-left: 10px;width: 20%;height: 27px;">' +
        '<input type="text" id="' +
        languagesId +
        '" class="form-control my-form-control" placeholder="Говори (Опционално)" style="margin-bottom:0px;margin-left: 10px;width: 20%;height: 27px;">' +
        '<input type="text" id="' +
        cityId +
        '" class="form-control my-form-control" placeholder="Живеещ в (Опционално)" style="margin-bottom:0px;margin-left: 10px;width: 20%;height: 27px;">' +
        '<select id="' +
        educationid +
        '">' +
        '<option value="0">Неуказано</option>' +
        '<option value="1">В училище</option>' +
        '<option value="2">Професионален бакалавър</option>' +
        '<option value="3">Бакалавър</option>' +
        '<option value="4">Магистър</option>' +
        '<option value="5">Доктор</option>' +
        "</select>"
    )
      .hide()
      .appendTo(majorSelector)
      .fadeIn(100);

    $("#" + educationid).niceSelect();

    setUpAutocomplete("#" + skillsId, "#" + languagesId, "#" + cityId);
  }

  function setUpAutocomplete(skillsSel, languagesSel, citySel) {
    $(skillsSel)
      .on("keydown", function(event) {
        if (
          event.keyCode === $.ui.keyCode.TAB &&
          $(this).autocomplete("instance").menu.active
        ) {
          event.preventDefault();
        }
      })
      .autocomplete({
        source: function(request, response) {
          $.getJSON(
            "/api/getSkills",
            {
              term: extractLast(request.term)
            },
            response
          );
        },
        search: function() {
          // custom minLength
          var term = extractLast(this.value);
          if (term.length < 2) {
            return false;
          }
        },
        focus: function(event, ui) {
          // prevent value inserted on focus
          return false;
        },
        select: function(event, ui) {
          var terms = split(this.value);
          // remove the current input
          terms.pop();
          // add the selected item
          terms.push(ui.item.value);
          // add placeholder to get the comma-and-space at the end
          terms.push("");
          this.value = terms.join(", ");
          return false;
        },
        autoFocus: true
      });

    $(languagesSel)
      .on("keydown", function(event) {
        if (
          event.keyCode === $.ui.keyCode.TAB &&
          $(this).autocomplete("instance").menu.active
        ) {
          event.preventDefault();
        }
      })
      .autocomplete({
        source: function(request, response) {
          $.getJSON(
            "/api/getLanguages",
            {
              term: extractLast(request.term)
            },
            response
          );
        },
        search: function() {
          // custom minLength
          var term = extractLast(this.value);
          if (term.length < 2) {
            return false;
          }
        },
        focus: function() {
          // prevent value inserted on focus
          return false;
        },
        select: function(event, ui) {
          var terms = split(this.value);
          // remove the current input
          terms.pop();
          // add the selected item
          terms.push(ui.item.value);
          // add placeholder to get the comma-and-space at the end
          terms.push("");
          this.value = terms.join(", ");
          return false;
        },
        autoFocus: true
      });

    $(citySel).autocomplete({
      source: "/api/getCities",
      minLength: 2,
      select: function(event, ui) {
      },
      autoFocus: true
    });
  }

  function giveValuesToInputs() {
    $.ajax({
      url: "/api/getWantedMembers?id=" + projectId,
      method: "GET",
      success: function(result) {
        for (var i = 0; i < result.length; i += 1) {
          switch (result[i].userCategory.id) {
            case 1:
              $("#needsProgrammer").trigger("click");
              giveValue(
                ".needsProgrammer",
                "needsProgrammerSkills",
                "needsProgrammerLanguages",
                "needsProgrammerCity",
                "needsProgrammerEducation",
                result[i]
              );
              break;
            case 2:
              $("#needsEngineer").trigger("click");
              giveValue(
                ".needsEngineer",
                "needsEngineerSkills",
                "needsEngineerLanguages",
                "needsEngineerCity",
                "needsEngineerEducation",
                result[i]
              );
              break;
            case 3:
              $("#needsDesigner").trigger("click");
              giveValue(
                ".needsDesigner",
                "needsDesignerSkills",
                "needsDesignerLanguages",
                "needsDesignerCity",
                "needsDesignerEducation",
                result[i]
              );
              break;
            case 7:
              $("#needsWriter").trigger("click");
              giveValue(
                ".needsWriter",
                "needsWriterSkills",
                "needsWriterLanguages",
                "needsWriterCity",
                "needsWriterEducation",
                result[i]
              );
              break;
            case 4:
              $("#needsScientist").trigger("click");
              giveValue(
                ".needsScientist",
                "needsScientistSkills",
                "needsScientistLanguages",
                "needsScientistCity",
                "needsScientistEducation",
                result[i]
              );
              break;
            case 5:
              $("#needsMusician").trigger("click");
              giveValue(
                ".needsMusician",
                "needsMusicianSkills",
                "needsMusicianLanguages",
                "needsMusicianCity",
                "needsMusicianEducation",
                result[i]
              );
              break;
            case 8:
              $("#needsFilmmaker").trigger("click");
              giveValue(
                ".needsFilmmaker",
                "needsFilmmakerSkills",
                "needsFilmmakerLanguages",
                "needsFilmmakerCity",
                "needsFilmmakerEducation",
                result[i]
              );
              break;
            case 9:
              $("#needsProductManager").trigger("click");
              giveValue(
                ".needsProductManager",
                "needsProductManagerSkills",
                "needsProductManagerLanguages",
                "needsProductManagerCity",
                "needsProductManagerEducation",
                result[i]
              );
              break;
            case 6:
              $("#needsArtist").trigger("click");
              giveValue(
                ".needsArtist",
                "needsArtistSkills",
                "needsArtistLanguages",
                "needsArtistCity",
                "needsArtistEducation",
                result[i]
              );
              break;
            default:
            // code block
          }
        }

        turnOnProjectNeeds();
      }
    });
  }

  function turnOnProjectNeeds() {
    $.ajax({
      url: "/api/getProjectNeeds?id=" + projectId,
      method: "GET",
      success: function(result) {
        for (var i = 0; i < result.length; i += 1) {
          switch (result[i].id) {
            case 1:
              if ($("#needsProgrammerSkills").length === 0) {
                $("#needsProgrammer").trigger("click");
              }

              break;
            case 2:
              if ($("#needsEngineerSkills").length === 0) {
                $("#needsEngineer").trigger("click");
              }

              break;
            case 3:
              if ($("#needsDesignerSkills").length === 0) {
                $("#needsDesigner").trigger("click");
              }

              break;
            case 7:
              if ($("#needsWriterSkills").length === 0) {
                $("#needsWriter").trigger("click");
              }

              break;
            case 4:
              if ($("#needsScientistSkills").length === 0) {
                $("#needsScientist").trigger("click");
              }

              break;
            case 5:
              if ($("#needsMusicianSkills").length === 0) {
                $("#needsMusician").trigger("click");
              }

              break;
            case 8:
              if ($("#needsFilmmakerSkills").length === 0) {
                $("#needsFilmmaker").trigger("click");
              }

              break;
            case 9:
              if ($("#needsProductManagerSkills").length === 0) {
                $("#needsProductManager").trigger("click");
              }

              break;
            case 6:
              if ($("#needsArtistSkills").length === 0) {
                $("#needsArtist").trigger("click");
              }

              break;
            default:
            // code block
          }
        }
      }
    });
  }

  function changeValueOfNiceSelect(educationId, dataValue) {
    $("#" + educationId + ' option[value="' + dataValue + '"]').attr(
      "selected",
      "selected"
    );
    $("#" + educationId)
      .next()
      .children("ul")
      .children(".selected")
      .removeClass("selected");
    $("#" + educationId)
      .next()
      .children("ul")
      .children('[data-value="' + dataValue + '"]')
      .addClass("selected");
    $("#" + educationId)
      .next()
      .children("span")
      .text(
        $("#" + educationId)
          .next()
          .children("ul")
          .children(".selected")
          .text()
      );
  }

  function giveValue(
    majorSelector,
    skillsId,
    languagesId,
    cityId,
    educationId,
    values
  ) {
    if (values.skills.length !== 0) {
      var skills = "";
      for (var j = 0; j < values.skills.length; j += 1) {
        skills += values.skills[j].name + ", ";
      }

      $("#" + skillsId).val(skills);
    }

    if (values.languages.length !== 0) {
      var languages = "";
      for (var j = 0; j < values.languages.length; j += 1) {
        languages += values.languages[j].name + ", ";
      }

      $("#" + languagesId).val(languages);
    }

    if (values.city !== null) {
      $("#" + cityId).val(values.city.name);
    }

    if (values.education !== null && values.education !== "") {
      switch (values.education) {
        case "В училище":
          changeValueOfNiceSelect(educationId, "1");
          break;
        case "Професионален бакалавър":
          changeValueOfNiceSelect(educationId, "2");
          break;
        case "Бакалавър":
          changeValueOfNiceSelect(educationId, "3");
          break;
        case "Магистър":
          changeValueOfNiceSelect(educationId, "4");
          break;
        case "Доктор":
          changeValueOfNiceSelect(educationId, "5");
          break;
        default:
          changeValueOfNiceSelect(educationId, "0");
      }
    } else {
      changeValueOfNiceSelect(educationId, "0");
    }
  }

  function areMemberDetailsCorrect() {
    var data = JSON.stringify(createParametersJSONData());

    $.ajax({
      url: "/api/checkIfNeedsAreCorrect",
      method: "POST",
      data: data,
      contentType: "application/json",
      success: function(result) {

        if (result === true || result === "true") {
          var name = $("#projectName").val();
          var description = $("#textareaBasic");
          var goal1 = $("#projectGoals").val();
          var goal2 = $("#projectGoals2").val();
          var goal3 = $("#projectGoals3").val();
          var githubPage = $("#github-page").val();
          var youtubeLink = $("#video-of-project").val();

          if (name.length === 0) {
            iziToast.error({
              title: "Грешка",
              message: "Името на проекта не може да е празно",
              position: "topRight"
            });
            return false;
          }

          if (name.length < 5) {
            iziToast.error({
              title: "Грешка",
              message: "Името на проекта не може да е по-малко от 5 символа",
              position: "topRight"
            });
            return false;
          }

          if (changed) {
            $.ajax({
              url: "/api/checkProjectName?name=" + name,
              method: "GET",
              async: false,
              success: function(result) {
                if (result.status === 200) {
                } else {
                  iziToast.error({
                    title: "Грешка",
                    message: "Това име на проект вече е заето",
                    position: "topRight"
                  });
                }
              }
            });
          }

          if (tinyMCE.get("textareaBasic").getContent().length < 150) {
            iziToast.error({
              title: "Грешка",
              message: "Описанието на проекта трябва да е минимум 150 символа.",
              position: "topRight"
            });

            return false;
          }

          if (tinyMCE.get("textareaBasic").getContent().length === 0) {
            iziToast.error({
              title: "Грешка",
              message: "Описанието на проекта не мое да бъде празно.",
              position: "topRight"
            });

            return false;
          }

          if (goal1.length === 0) {
            iziToast.error({
              title: "Грешка",
              message: "Проектът трябва да има поне една крайна цел.",
              position: "topRight"
            });

            return false;
          }

          var needsProgrammer = document.querySelector("#needsProgrammer")
            .checked;
          var needsDesigner = document.querySelector("#needsDesigner").checked;
          var needsEngineer = document.querySelector("#needsEngineer").checked;
          var needsWriter = document.querySelector("#needsWriter").checked;
          var needsScientist = document.querySelector("#needsScientist")
            .checked;
          var needsMusician = document.querySelector("#needsMusician").checked;
          var needsFilmmaker = document.querySelector("#needsFilmmaker")
            .checked;
          var needsProductManager = document.querySelector(
            "#needsProductManager"
          ).checked;
          var needsArtist = document.querySelector("#needsArtist").checked;

          var programmer = "";
          var designer = "";
          var engineer = "";
          var writer = "";
          var scientist = "";
          var musician = "";
          var filmmaker = "";
          var productManager = "";
          var artist = "";

          if (needsProgrammer) {
            programmer = "Програмист";
          }

          if (needsDesigner) {
            designer = "Дизайнер";
          }

          if (needsEngineer) {
            engineer = "Инженер";
          }

          if (needsWriter) {
            writer = "Писател";
          }

          if (needsScientist) {
            scientist = "Учен";
          }

          if (needsMusician) {
            musician = "Музикант";
          }

          if (needsFilmmaker) {
            filmmaker = "Режисьор";
          }

          if (needsProductManager) {
            productManager = "Продуктов мениджър";
          }

          if (needsArtist) {
            artist = "Артист";
          }

          var parametersData = createParametersJSONData();
          var otherData = JSON.stringify({
            id: projectId,
            name: name,
            description: tinyMCE.get("textareaBasic").getContent(),
            goal1: goal1,
            goal2: goal2,
            goal3: goal3,
            githubPage: githubPage,
            youtubeLink: youtubeLink,
            projectNeeds: [
              {
                name: programmer
              },
              {
                name: designer
              },
              {
                name: engineer
              },
              {
                name: writer
              },
              {
                name: scientist
              },
              {
                name: musician
              },
              {
                name: filmmaker
              },
              {
                name: productManager
              },
              {
                name: artist
              }
            ],
            parameters: parametersData
          });

          $.ajax({
            url: "/api/editProject",
            method: "POST",
            data: otherData,
            contentType: "application/json",
            success: function(result) {
              if (hasNewImages) {
                uploader.fineUploader(
                  "setEndpoint",
                  "/api/uploadProjectPicture?id=" + result.id
                );

                $(".qq-upload-success").removeClass("qq-upload-success");

                uploader.fineUploader("setName", 0, "0");

                if ($(".qq-file-id-1").length !== 0) {
                  uploader.fineUploader("setName", 1, "1");
                }

                if ($(".qq-file-id-2").length !== 0) {
                  uploader.fineUploader("setName", 2, "2");
                }

                if ($(".qq-file-id-3").length !== 0) {
                  uploader.fineUploader("setName", 3, "3");
                }

                if ($(".qq-file-id-4").length !== 0) {
                  uploader.fineUploader("setName", 4, "4");
                }

                if ($(".qq-file-id-5").length !== 0) {
                  uploader.fineUploader("setName", 5, "5");
                }

                if ($(".qq-file-id-6").length !== 0) {
                  uploader.fineUploader("setName", 6, "6");
                }

                secondUpload = true;

                $("#fine-uploader").fineUploader("uploadStoredFiles");
              } else {
                $(".saveChangesEditProfileBtn").html("Запази");
                location.replace("/my-projects");
              }
            }
          });
          // $.ajax({
          //   url: "/api/editProject",
          //   method: "POST",
          //   contentType: "application/json",
          //   data: JSON.stringify({
          //     id: projectId,
          //     name: name,
          //     description: description,
          //     goal1: goal1,
          //     goal2: goal2,
          //     goal3: goal3,
          //     githubPage: githubPage,
          //     youtubeLink: youtubeLink
          //   }),
          //   success: function (result) {
          //     if (result) {
          //       window.location.href = "/my-projects";
          //     }
          //   }
          // });
        } else {
          $(".saveChangesEditProfileBtn").html("Запази");

          iziToast.error({
            title: "Грешка",
            message:
              "Някои от въведените искания към хора от втора стъпка не са валидни.",
            position: "topRight"
          });
        }
      }
    });
  }

  function createParametersJSONData() {
    var needsProgrammerSkills = retrieveValue("#needsProgrammerSkills");
    var needsProgrammerLanguages = retrieveValue("#needsProgrammerLanguages");
    var needsProgrammerCity = retrieveValue("#needsProgrammerCity");
    var needsProgrammerEducation = retrieveValueFromSelect(
      "#needsProgrammerEducation"
    );

    var needsDesignerSkills = retrieveValue("#needsDesignerSkills");
    var needsDesignerLanguages = retrieveValue("#needsDesignerLanguages");
    var needsDesignerCity = retrieveValue("#needsDesignerCity");
    var needsDesignerEducation = retrieveValueFromSelect(
      "#needsDesignerEducation"
    );

    var needsEngineerSkills = retrieveValue("#needsEngineerSkills");
    var needsEngineerLanguages = retrieveValue("#needsEngineerLanguages");
    var needsEngineerCity = retrieveValue("#needsEngineerCity");
    var needsEngineerEducation = retrieveValueFromSelect(
      "#needsEngineerEducation"
    );

    var needsWriterSkills = retrieveValue("#needsWriterSkills");
    var needsWriterLanguages = retrieveValue("#needsWriterLanguages");
    var needsWriterCity = retrieveValue("#needsWriterCity");
    var needsWriterEducation = retrieveValueFromSelect("#needsWriterEducation");

    var needsScientistSkills = retrieveValue("#needsScientistSkills");
    var needsScientistLanguages = retrieveValue("#needsScientistLanguages");
    var needsScientistCity = retrieveValue("#needsScientistCity");
    var needsScientistEducation = retrieveValueFromSelect(
      "#needsScientistEducation"
    );

    var needsMusicianSkills = retrieveValue("#needsMusicianSkills");
    var needsMusicianLanguages = retrieveValue("#needsMusicianLanguages");
    var needsMusicianCity = retrieveValue("#needsMusicianCity");
    var needsMusicianEducation = retrieveValueFromSelect(
      "#needsMusicianEducation"
    );

    var needsFilmmakerSkills = retrieveValue("#needsFilmmakerSkills");
    var needsFilmmakerLanguages = retrieveValue("#needsFilmmakerLanguages");
    var needsFilmmakerCity = retrieveValue("#needsFilmmakerCity");
    var needsFilmmakerEducation = retrieveValueFromSelect(
      "#needsFilmmakerEducation"
    );

    var needsProductManagerSkills = retrieveValue("#needsProductManagerSkills");
    var needsProductManagerLanguages = retrieveValue(
      "#needsProductManagerLanguages"
    );
    var needsProductManagerCity = retrieveValue("#needsProductManagerCity");
    var needsProductManagerEducation = retrieveValueFromSelect(
      "#needsProductManagerEducation"
    );

    var needsArtistSkills = retrieveValue("#needsArtistSkills");
    var needsArtistLanguages = retrieveValue("#needsArtistLanguages");
    var needsArtistCity = retrieveValue("#needsArtistCity");
    var needsArtistEducation = retrieveValueFromSelect("#needsArtistEducation");

    var dataAjax = [];
    dataAjax.push(makeObject("programmer", needsProgrammerSkills, "skills"));
    dataAjax.push(
      makeObject("programmer", needsProgrammerLanguages, "languages")
    );
    dataAjax.push(makeObject("programmer", needsProgrammerCity, "city"));
    dataAjax.push(
      makeObject("programmer", needsProgrammerEducation, "education")
    );

    dataAjax.push(makeObject("designer", needsDesignerSkills, "skills"));
    dataAjax.push(makeObject("designer", needsDesignerLanguages, "languages"));
    dataAjax.push(makeObject("designer", needsDesignerCity, "city"));
    dataAjax.push(makeObject("designer", needsDesignerEducation, "education"));

    dataAjax.push(makeObject("engineer", needsEngineerSkills, "skills"));
    dataAjax.push(makeObject("engineer", needsEngineerLanguages, "languages"));
    dataAjax.push(makeObject("engineer", needsEngineerCity, "city"));
    dataAjax.push(makeObject("engineer", needsEngineerEducation, "education"));

    dataAjax.push(makeObject("writer", needsWriterSkills, "skills"));
    dataAjax.push(makeObject("writer", needsWriterLanguages, "languages"));
    dataAjax.push(makeObject("writer", needsWriterCity, "city"));
    dataAjax.push(makeObject("writer", needsWriterEducation, "education"));

    dataAjax.push(makeObject("scientist", needsScientistSkills, "skills"));
    dataAjax.push(
      makeObject("scientist", needsScientistLanguages, "languages")
    );
    dataAjax.push(makeObject("scientist", needsScientistCity, "city"));
    dataAjax.push(
      makeObject("scientist", needsScientistEducation, "education")
    );

    dataAjax.push(makeObject("musician", needsMusicianSkills, "skills"));
    dataAjax.push(makeObject("musician", needsMusicianLanguages, "languages"));
    dataAjax.push(makeObject("musician", needsMusicianCity, "city"));
    dataAjax.push(makeObject("musician", needsMusicianEducation, "education"));

    dataAjax.push(makeObject("filmmaker", needsFilmmakerSkills, "skills"));
    dataAjax.push(
      makeObject("filmmaker", needsFilmmakerLanguages, "languages")
    );
    dataAjax.push(makeObject("filmmaker", needsFilmmakerCity, "city"));
    dataAjax.push(
      makeObject("filmmaker", needsFilmmakerEducation, "education")
    );

    dataAjax.push(
      makeObject("productManager", needsProductManagerSkills, "skills")
    );
    dataAjax.push(
      makeObject("productManager", needsProductManagerLanguages, "languages")
    );
    dataAjax.push(
      makeObject("productManager", needsProductManagerCity, "city")
    );
    dataAjax.push(
      makeObject("productManager", needsProductManagerEducation, "education")
    );

    dataAjax.push(makeObject("artist", needsArtistSkills, "skills"));
    dataAjax.push(makeObject("artist", needsArtistLanguages, "languages"));
    dataAjax.push(makeObject("artist", needsArtistCity, "city"));
    dataAjax.push(makeObject("artist", needsArtistEducation, "education"));

    return dataAjax;
  }

  function makeObject(category, value, paramCategory) {
    return {
      category: category,
      paramCategory: paramCategory,
      values: value
    };
  }

  function retrieveValueFromSelect(selector) {
    return makeArray(
      $(selector)
        .children("option:selected")
        .val()
    );
  }

  function retrieveValue(selector) {
    if ($(selector).length !== 0) {
      return makeArray($(selector).val());
    } else {
      return [];
    }
  }

  function makeArray(str) {
    if (typeof str === "undefined") {
      return [];
    }

    if (str === "") {
      return [];
    }

    var result = str.split(", ");
    if (result.length !== 1) {
      result.pop();
    }

    return result;
  }

  setUpWizard();

  function setUpWizard() {
    uploader = $("#fine-uploader").fineUploader({
      debug: true,
      request: {
        endpoint: "/api/upload"
      },
      deleteFile: {
        enabled: true,
        method: "DELETE"
      },
      retry: {
        enableAuto: true
      },
      multiple: true,
      onLeave:
        "Вашата снимка се качва вмомента. Сигурни ли сте че искате да напуснете страницата.",
      autoUpload: false,
      thumbnails: {
        placeholders: {
          waitingPath: "css/waiting-generic.png",
          notAvailablePath: "css/not_available-generic.png"
        }
      },
      validation: {
        sizeLimit: 10048576,
        itemLimit: 7,
        allowedExtensions: ["jpeg", "jpg", "gif", "png"]
      },
      maxConnections: 1,
      callbacks: {
        onAllComplete: function(succeeded, failed) {
          if (secondUpload) {
            $(".saveChangesEditProfileBtn").html("Запази");
            location.replace("/my-projects");
          }
          // if (succeeded) {
          //   window.location.href = "/groups-of-people";
          // }
        },
        onSubmit: function(id, name) {
          hasNewImages = true;
        },
        onUploadChunk: function(id, name, chunkData) {},
        onUploadChunkSuccess: function(id, chunkData, responseJSON, xhr) {}
      }
    });

    $.ajax({
      url: "/api/getProjectImages?id=" + projectId,
      method: "GET",
      success: function(result) {
        // uploader.fineUploader("addInitialFiles", [{
        //   "name": "beeline-moto-1.jpg",
        //   "uuid": "a3ef2360-881d-452c-a5f6-a173d5291066",
        //   "thumbnailUrl": "/static/all-images/beeline-moto-1.jpg"
        // }]);

        uploader.fineUploader("addInitialFiles", result);
      }
    });

    $("#projectName").change(function() {
      changed = true;
    });

    $(document).ready(function() {
      $("#smartwizard").smartWizard({
        selected: 0,
        cycleSteps: false,
        backButtonSupport: true,
        transitionEffect: "slide",
        keyNavigation: false,
        toolbarSettings: {
          toolbarPosition: "bottom", // none, top, bottom, both
          toolbarButtonPosition: "right", // left, right
          showNextButton: true, // show/hide a Next button
          showPreviousButton: true, // show/hide a Previous button
          toolbarExtraButtons: [
            $("<button></button>")
              .text("Запази")
              .addClass("btn btn-info saveChangesEditProfileBtn")
              .on("click", function() {
                if (!hasGoneToSecondPage) {
                  iziToast.error({
                    title: "Грешка",
                    message: "Моля отидете и на втора стъпка за да завършите",
                    position: "topRight"
                  });

                  return false;
                }

                $(".saveChangesEditProfileBtn").html(
                  'Запази <i class="fas fa-circle-notch fa-spin"></i>'
                );
                areMemberDetailsCorrect();
              })
          ]
        }
      });

      $("#smartwizard").on("leaveStep", function(
        e,
        anchorObject,
        stepNumber,
        stepDirection
      ) {
        if (!hasGoneToSecondPage) {
          giveValuesToInputs();
        }

        hasGoneToSecondPage = true;
      });
    });

    $(".preloader").fadeOut(500);
  }
});
// }
