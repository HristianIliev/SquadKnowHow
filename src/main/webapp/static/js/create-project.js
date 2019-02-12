var map;
var lastMarker = null;
var lat;
var lng;

function initMap() {
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
        icon: "/static/icons/user-map-icon-2.png",
        map: map
      });
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

          lastMarker = new google.maps.Marker({
            position: pos,
            map: map,
            draggable: true
          });

          google.maps.event.addListener(lastMarker, "dragend", function (event) {
            var latitude = event.latLng.lat();
            var longitude = event.latLng.lng();
            lat = latitude;
            lng = longitude;
            // alert(lat + " " + lng);
            geocodeLatLng(geocoder, map, latitude, longitude);
          });
        }
      }
    );

    return false;
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

var verificationCode;
var user = null;
var needsMoney = $(".fund-me").is(":checked");

$(".fund-me").change(function () {
  if ($(".fund-me").is(":checked")) {
    needsMoney = true;
    $(".fund-me-div").after(
      $("<div/>")
      .attr("id", "money-form")
      .attr("style", "margin-top: 20px; margin-bottom: 30px;")
      .addClass("md-form")
      .addClass("form-lg")
      .addClass("form-margins")
      .append(
        $("<i/>")
        .attr("id", "neededMoneyIcon")
        .addClass("far")
        .addClass("fa-money-bill-alt")
        .addClass("prefix")
        .addClass("fa-7x")
      )
      .append(
        $("<input/>")
        .attr("id", "neededMoney")
        .attr("type", "number")
        .attr("min", "0.01")
        .attr("step", "0.01")
        .attr("value", "0.01")
        .addClass("form-control")
      )
      .append(
        $("<label/>")
        .attr("id", "neededMoneyLabel")
        .attr("for", "neededMoney")
        .text("Сума от която се нуждае проекта ти (Euro)")
      )
    );
    $("#neededMoneyLabel").addClass("active");
    $("#neededMoneyIcon").addClass("active");
  } else {
    $("#money-form").remove();
    needsMoney = false;
  }
});

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

var elems = Array.prototype.slice.call(document.querySelectorAll(".js-switch"));

function split(val) {
  return val.split(/,\s*/);
}

function extractLast(term) {
  return split(term).pop();
}

elems.forEach(function (html) {
  var switchery = new Switchery(html, {
    color: "#138fc2",
    secondaryColor: "#cccccc",
    jackColor: "#c7e1f1",
    size: "small"
  });

  html.onchange = function () {
    if (html.checked) {
      if (html.id === "needsProgrammer") {
        showInputs(".needsProgrammer", "needsProgrammerSkills", "needsProgrammerLanguages", "needsProgrammerCity", "needsProgrammerEducation");
      } else if (html.id === "needsDesigner") {
        showInputs(".needsDesigner", "needsDesignerSkills", "needsDesignerLanguages", "needsDesignerCity", "needsDesignerEducation");
      } else if (html.id === "needsEngineer") {
        showInputs(".needsEngineer", "needsEngineerSkills", "needsEngineerLanguages", "needsEngineerCity", "needsEngineerEducation");
      } else if (html.id === "needsWriter") {
        showInputs(".needsWriter", "needsWriterSkills", "needsWriterLanguages", "needsWriterCity", "needsWriterEducation");
      } else if (html.id === "needsScientist") {
        showInputs(".needsScientist", "needsScientistSkills", "needsScientistLanguages", "needsScientistCity", "needsScientistEducation");
      } else if (html.id === "needsMusician") {
        showInputs(".needsMusician", "needsMusicianSkills", "needsMusicianLanguages", "needsMusicianCity", "needsMusicianEducation");
      } else if (html.id === "needsFilmmaker") {
        showInputs(".needsFilmmaker", "needsFilmmakerSkills", "needsFilmmakerLanguages", "needsFilmmakerCity", "needsFilmmakerEducation");
      } else if (html.id === "needsProductManager") {
        showInputs(".needsProductManager", "needsProductManagerSkills", "needsProductManagerLanguages", "needsProductManagerCity", "needsProductManagerEducation");
      } else if (html.id === "needsArtist") {
        showInputs(".needsArtist", "needsArtistSkills", "needsArtistLanguages", "needsArtistCity", "needsArtistEducation");
      }
    } else {
      if (html.id === "needsProgrammer") {
        hideInputs("needsProgrammerSkills", "needsProgrammerLanguages", "needsProgrammerCity", "needsProgrammerEducation");
      } else if (html.id === "needsDesigner") {
        hideInputs("needsDesignerSkills", "needsDesignerLanguages", "needsDesignerCity", "needsDesignerEducation");
      } else if (html.id === "needsEngineer") {
        hideInputs("needsEngineerSkills", "needsEngineerLanguages", "needsEngineerCity", "needsEngineerEducation");
      } else if (html.id === "needsWriter") {
        hideInputs("needsWriterSkills", "needsWriterLanguages", "needsWriterCity", "needsWriterEducation");
      } else if (html.id === "needsScientist") {
        hideInputs("needsScientistSkills", "needsScientistLanguages", "needsScientistCity", "needsScientistEducation");
      } else if (html.id === "needsMusician") {
        hideInputs("needsMusicianSkills", "needsMusicianLanguages", "needsMusicianCity", "needsMusicianEducation");
      } else if (html.id === "needsFilmmaker") {
        hideInputs("needsFilmmakerSkills", "needsFilmmakerLanguages", "needsFilmmakerCity", "needsFilmmakerEducation");
      } else if (html.id === "needsProductManager") {
        hideInputs("needsProductManagerSkills", "needsProductManagerLanguages", "needsProductManagerCity", "needsProductManagerEducation");
      } else if (html.id === "needsArtist") {
        hideInputs("needsArtistSkills", "needsArtistLanguages", "needsArtistCity", "needsArtistEducation");
      }
    }
  };
});

function hideInputs(skillsId,
  languagesId,
  cityId,
  educationId) {
  $("#" + skillsId).fadeOut(100, function () {
    $(this).remove();
  });
  $("#" + languagesId).fadeOut(100, function () {
    $(this).remove();
  });
  $("#" + cityId).fadeOut(100, function () {
    $(this).remove();
  });
  $("#" + educationId).niceSelect("destroy");
  $("#" + educationId).remove();
}

function showInputs(majorSelector,
  skillsId,
  languagesId,
  cityId,
  educationid) {
  $('<input type="text" id="' + skillsId + '" class="form-control my-form-control skills-create" placeholder="Умеещ (Опционално)" style="margin-bottom:0px;margin-left: 10px;width: 20%;height: 27px;">' +
    '<input type="text" id="' + languagesId + '" class="form-control my-form-control" placeholder="Говори (Опционално)" style="margin-bottom:0px;margin-left: 10px;width: 20%;height: 27px;">' +
    '<input type="text" id="' + cityId + '" class="form-control my-form-control" placeholder="Живеещ в (Опционално)" style="margin-bottom:0px;margin-left: 10px;width: 20%;height: 27px;">' +
    '<select id="' + educationid + '">' +
    '<option value="0">Неуказано</option>' +
    '<option value="1">В училище</option>' +
    '<option value="2">Професионален бакалавър</option>' +
    '<option value="3">Бакалавър</option>' +
    '<option value="4">Магистър</option>' +
    '<option value="5">Доктор</option>' +
    "</select>").hide().appendTo(majorSelector).fadeIn(100);

  $("#" + educationid).niceSelect();

  setUpAutocomplete("#" + skillsId, "#" + languagesId, "#" + cityId);
}

function setUpAutocomplete(skillsSel, languagesSel, citySel) {
  $(skillsSel)
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
      focus: function (event, ui) {
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
        return false;
      },
      autoFocus: true
    });

  $(languagesSel)
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
        return false;
      },
      autoFocus: true
    });

  $(citySel).autocomplete({
    source: "/api/getCities",
    minLength: 2,
    select: function (event, ui) {
    },
    autoFocus: true
  });
}

$("#location").autocomplete({
  source: "/api/getCities",
  minLength: 2,
  select: function (event, ui) {
  },
  autoFocus: true
});

var uploader = $("#fine-uploader").fineUploader({
  debug: true,
  request: {
    endpoint: "/api/upload"
  },
  deleteFile: {
    enabled: true,
    endpoint: "/api/deleteProfilePicture"
  },
  retry: {
    enableAuto: false
  },
  multiple: true,
  onLeave: "Вашата снимка се качва вмомента. Сигурни ли сте че искате да напуснете страницата.",
  autoUpload: false,
  thumbnails: {
    placeholders: {
      waitingPath: "static/css/waiting-generic.png",
      notAvailablePath: "static/css/not_available-generic.png"
    }
  },
  validation: {
    sizeLimit: 10048576,
    itemLimit: 10,
    allowedExtensions: ["jpeg", "jpg", "gif", "png"]
  },
  maxConnections: 1,
  callbacks: {
    onAllComplete: function (succeeded, failed) {
      if (succeeded) {
        window.location.href = "/projects-of-people";
      }
    },
    onUploadChunk: function (id, name, chunkData) {},
    onUploadChunkSuccess: function (id, chunkData, responseJSON, xhr) {}
  }
});

$(document).ready(function () {
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
        .text("Създай")
        .addClass("btn btn-info")
        .on("click", function () {
          var remainingAllowed = uploader.fineUploader(
            "getRemainingAllowedItems"
          );

          var telephone = $("#telephone").val();
          if (remainingAllowed === 10 || remainingAllowed === 9) {
            iziToast.error({
              title: "Грешка",
              message: "Трябва да има минимум две снимки качени",
              position: "topRight"
            });
          } else if (telephone.length !== 13) {
            iziToast.error({
              title: "Грешка",
              message: "Въведеният телефонен номер не е валиден",
              position: "topRight"
            });
          } else {
            areMemberDetailsCorrect(telephone)
          }
        })
      ]
    }
  });

  $("#smartwizard").on("leaveStep", function (
    e,
    anchorObject,
    stepNumber,
    stepDirection
  ) {
    var nameIcon = $("#projectNameIcon");
    var name = $("#projectName");
    var nameLabel = $("#projectNameLabel");

    // var locationIcon = $("#locationIcon");
    var location = $("#arena-address");
    // var locationLabel = $("#locationLabel");

    var showItOn = $("#mceu_22");
    var description = $("#textareaBasic");
    var descriptionLabel = $("#textareaBasicLabel");

    var goalsIcon = $("#projectGoalsIcon");
    var goals = $("#projectGoals");
    var goalsLabel = $("#projectGoalsLabel");

    if (stepNumber === 0) {
      if (name.val().length === 0) {
        // name.webuiPopover("destroy");
        // name.webuiPopover({
        //   placement: "right",
        //   trigger: "manual",
        //   content: "Това поле не може да бъде празно",
        //   style: "popover",
        //   closeable: true,
        //   animation: "pop",
        //   width: "auto", //can be set with  number
        //   height: "auto"
        // });
        // name.webuiPopover("show");
        iziToast.error({
          title: "Грешка",
          message: "Името не може да е празно",
          position: "topRight"
        });

        nameIcon.attr("style", "color: rgb(230, 92, 92);");
        name.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
        nameLabel.attr("style", "color: rgb(230, 92, 92);");

        nameIcon.addClass("validated-icon");
        name.addClass("validated-form");
        nameLabel.addClass("validated-label");

        $(".validated-form").click(function () {
          $(this).attr("style", " ");
          $(".validated-label").attr("style", " ");
          $(".validated-icon").attr("style", " ");
        });

        return false;
      } else if (name.val().length < 5) {
        // name.webuiPopover("destroy");
        // name.webuiPopover({
        //   placement: "right",
        //   trigger: "manual",
        //   content: "Това поле не може да бъде по-малко от 5 символа",
        //   style: "popover",
        //   closeable: true,
        //   animation: "pop",
        //   width: "auto", //can be set with  number
        //   height: "auto"
        // });
        // name.webuiPopover("show");
        iziToast.error({
          title: "Грешка",
          message: "Името не може да е по-малко от 5 символа",
          position: "topRight"
        });

        nameIcon.attr("style", "color: rgb(230, 92, 92);");
        name.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
        nameLabel.attr("style", "color: rgb(230, 92, 92);");

        nameIcon.addClass("validated-icon");
        name.addClass("validated-form");
        nameLabel.addClass("validated-label");

        $(".validated-form").click(function () {
          $(this).attr("style", " ");
          $(".validated-label").attr("style", " ");
          $(".validated-icon").attr("style", " ");
        });

        return false;
      } else if (location.val() === "") {
        // location.webuiPopover("destroy");
        // location.webuiPopover({
        //   placement: "right",
        //   trigger: "manual",
        //   content: "Липсва адрес",
        //   style: "popover",
        //   closeable: true,
        //   animation: "pop",
        //   width: "auto", //can be set with  number
        //   height: "auto"
        // });
        // location.webuiPopover("show");
        iziToast.error({
          title: "Грешка",
          message: "Липсва адрес",
          position: "topRight"
        });

        // locationIcon.attr("style", "color: rgb(230, 92, 92);");
        location.attr(
          "style",
          "border: 1px solid rgb(230, 92, 92);margin-bottom: 10px; margin-top: 10px;"
        );
        // locationLabel.attr("style", "color: rgb(230, 92, 92);");

        // locationIcon.addClass("validated-icon");
        location.addClass("validated-form");
        // locationLabel.addClass("validated-label");

        $(".validated-form").click(function () {
          $(this).attr("style", " ");
          $(".validated-label").attr("style", " ");
          $(".validated-icon").attr("style", " ");
        });

        return false;
      } else {
        var exists = false;

        $.ajax({
          url: "/api/checkProjectName?name=" + name.val(),
          method: "GET",
          async: false,
          success: function (result) {
            if (result.status === 200) {
              // name.webuiPopover("destroy");
              exists = false;
            } else {
              exists = true;
            }
          }
        });

        if (exists === true) {
          // name.webuiPopover("destroy");
          // name.webuiPopover({
          //   placement: "right",
          //   trigger: "manual",
          //   content: "Това име вече съществува",
          //   style: "popover",
          //   closeable: true,
          //   animation: "pop",
          //   width: "auto", //can be set with  number
          //   height: "auto"
          // });
          // name.webuiPopover("show");
          iziToast.error({
            title: "Грешка",
            message: "Това име вече съществува",
            position: "topRight"
          });

          nameIcon.attr("style", "color: rgb(230, 92, 92);");
          name.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
          nameLabel.attr("style", "color: rgb(230, 92, 92);");

          nameIcon.addClass("validated-icon");
          name.addClass("validated-form");
          nameLabel.addClass("validated-label");

          $(".validated-form").click(function () {
            $(this).attr("style", " ");
            $(".validated-label").attr("style", " ");
            $(".validated-icon").attr("style", " ");
          });

          return false;
        }

        // name.webuiPopover("destroy");
        // location.webuiPopover("destroy");
        return true;
      }
    } else if (stepNumber === 1) {
      if (tinyMCE.get("textareaBasic").getContent().length < 150) {
        // showItOn.webuiPopover("destroy");
        // showItOn.webuiPopover({
        //   placement: "right",
        //   trigger: "manual",
        //   content: "Това поле не може да бъде по-мало от 150 символа",
        //   style: "popover",
        //   closeable: true,
        //   animation: "pop",
        //   width: "auto", //can be set with  number
        //   height: "auto"
        // });
        // showItOn.webuiPopover("show");
        iziToast.error({
          title: "Грешка",
          message: "Описанието не може да бъде по-малко от 150 символа",
          position: "topRight"
        });

        showItOn.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
        descriptionLabel.attr("style", "color: rgb(230, 92, 92);");

        showItOn.addClass("validated-form");
        descriptionLabel.addClass("validated-label");

        $(".validated-form").click(function () {
          $(this).attr("style", " ");
          $(".validated-label").attr("style", " ");
        });

        return false;
      } else if (tinyMCE.get("textareaBasic").getContent().length === 0) {
        // showItOn.webuiPopover("destroy");
        // showItOn.webuiPopover({
        //   placement: "right",
        //   trigger: "manual",
        //   content: "Това поле не може да бъде празно",
        //   style: "popover",
        //   closeable: true,
        //   animation: "pop",
        //   width: "auto", //can be set with  number
        //   height: "auto"
        // });
        // showItOn.webuiPopover("show");
        iziToast.error({
          title: "Грешка",
          message: "Описанието не може да бъде празно",
          position: "topRight"
        });

        showItOn.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
        descriptionLabel.attr("style", "color: rgb(230, 92, 92);");

        showItOn.addClass("validated-form");
        descriptionLabel.addClass("validated-label");

        $(".validated-form").click(function () {
          $(this).attr("style", " ");
          $(".validated-label").attr("style", " ");
        });

        return false;
      } else if (goals.val().length === 0) {
        // goals.webuiPopover("destroy");
        // goals.webuiPopover({
        //   placement: "right",
        //   trigger: "manual",
        //   content: "Това поле не може да бъде празно",
        //   style: "popover",
        //   closeable: true,
        //   animation: "pop",
        //   width: "auto", //can be set with  number
        //   height: "auto"
        // });
        // goals.webuiPopover("show");
        iziToast.error({
          title: "Грешка",
          message: "Целите не могат да бъдат празни",
          position: "topRight"
        });

        goalsIcon.attr("style", "color: rgb(230, 92, 92);");
        goals.attr("style", "border-bottom: 1px solid rgb(230, 92, 92)");
        goalsLabel.attr("style", "color: rgb(230, 92, 92);");

        goalsIcon.addClass("validated-icon");
        goals.addClass("validated-form");
        goalsLabel.addClass("validated-label");

        $(".validated-form").click(function () {
          $(this).attr("style", " ");
          $(".validated-label").attr("style", " ");
          $(".validated-icon").attr("style", " ");
        });

        return false;
      } else {
        // description.webuiPopover("destroy");
        return true;
      }
    } else {
      return true;
    }
  });
});

function areMemberDetailsCorrect(telephone) {
  var data = JSON.stringify(createParametersJSONData());

  $.ajax({
    url: "/api/checkIfNeedsAreCorrect",
    method: "POST",
    data: data,
    contentType: "application/json",
    success: function (result) {

      if (result === true || result === 'true') {
        sendVerificationSMS(telephone);

        showCodeVerificationModal(telephone);
      } else {
        iziToast.error({
          title: "Грешка",
          message: "Някои от въведените искания към хора от втора стъпка не са валидни.",
          position: "topRight"
        });
      }
    }
  })
}

function createParametersJSONData() {
  var needsProgrammerSkills = retrieveValue("#needsProgrammerSkills");
  var needsProgrammerLanguages = retrieveValue("#needsProgrammerLanguages");
  var needsProgrammerCity = retrieveValue("#needsProgrammerCity");
  var needsProgrammerEducation = retrieveValueFromSelect("#needsProgrammerEducation");

  var needsDesignerSkills = retrieveValue("#needsDesignerSkills");
  var needsDesignerLanguages = retrieveValue("#needsDesignerLanguages");
  var needsDesignerCity = retrieveValue("#needsDesignerCity");
  var needsDesignerEducation = retrieveValueFromSelect("#needsDesignerEducation");

  var needsEngineerSkills = retrieveValue("#needsEngineerSkills");
  var needsEngineerLanguages = retrieveValue("#needsEngineerLanguages");
  var needsEngineerCity = retrieveValue("#needsEngineerCity");
  var needsEngineerEducation = retrieveValueFromSelect("#needsEngineerEducation");

  var needsWriterSkills = retrieveValue("#needsWriterSkills");
  var needsWriterLanguages = retrieveValue("#needsWriterLanguages");
  var needsWriterCity = retrieveValue("#needsWriterCity");
  var needsWriterEducation = retrieveValueFromSelect("#needsWriterEducation");

  var needsScientistSkills = retrieveValue("#needsScientistSkills");
  var needsScientistLanguages = retrieveValue("#needsScientistLanguages");
  var needsScientistCity = retrieveValue("#needsScientistCity");
  var needsScientistEducation = retrieveValueFromSelect("#needsScientistEducation");

  var needsMusicianSkills = retrieveValue("#needsMusicianSkills");
  var needsMusicianLanguages = retrieveValue("#needsMusicianLanguages");
  var needsMusicianCity = retrieveValue("#needsMusicianCity");
  var needsMusicianEducation = retrieveValueFromSelect("#needsMusicianEducation");

  var needsFilmmakerSkills = retrieveValue("#needsFilmmakerSkills");
  var needsFilmmakerLanguages = retrieveValue("#needsFilmmakerLanguages");
  var needsFilmmakerCity = retrieveValue("#needsFilmmakerCity");
  var needsFilmmakerEducation = retrieveValueFromSelect("#needsFilmmakerEducation");

  var needsProductManagerSkills = retrieveValue("#needsProductManagerSkills");
  var needsProductManagerLanguages = retrieveValue("#needsProductManagerLanguages");
  var needsProductManagerCity = retrieveValue("#needsProductManagerCity");
  var needsProductManagerEducation = retrieveValueFromSelect("#needsProductManagerEducation");

  var needsArtistSkills = retrieveValue("#needsArtistSkills");
  var needsArtistLanguages = retrieveValue("#needsArtistLanguages");
  var needsArtistCity = retrieveValue("#needsArtistCity");
  var needsArtistEducation = retrieveValueFromSelect("#needsArtistEducation");

  var dataAjax = [];
  dataAjax.push(makeObject("programmer", needsProgrammerSkills, "skills"));
  dataAjax.push(makeObject("programmer", needsProgrammerLanguages, "languages"));
  dataAjax.push(makeObject("programmer", needsProgrammerCity, "city"));
  dataAjax.push(makeObject("programmer", needsProgrammerEducation, "education"));

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
  dataAjax.push(makeObject("scientist", needsScientistLanguages, "languages"));
  dataAjax.push(makeObject("scientist", needsScientistCity, "city"));
  dataAjax.push(makeObject("scientist", needsScientistEducation, "education"));

  dataAjax.push(makeObject("musician", needsMusicianSkills, "skills"));
  dataAjax.push(makeObject("musician", needsMusicianLanguages, "languages"));
  dataAjax.push(makeObject("musician", needsMusicianCity, "city"));
  dataAjax.push(makeObject("musician", needsMusicianEducation, "education"));

  dataAjax.push(makeObject("filmmaker", needsFilmmakerSkills, "skills"));
  dataAjax.push(makeObject("filmmaker", needsFilmmakerLanguages, "languages"));
  dataAjax.push(makeObject("filmmaker", needsFilmmakerCity, "city"));
  dataAjax.push(makeObject("filmmaker", needsFilmmakerEducation, "education"));

  dataAjax.push(makeObject("productManager", needsProductManagerSkills, "skills"));
  dataAjax.push(makeObject("productManager", needsProductManagerLanguages, "languages"));
  dataAjax.push(makeObject("productManager", needsProductManagerCity, "city"));
  dataAjax.push(makeObject("productManager", needsProductManagerEducation, "education"));

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
  return makeArray($(selector).children("option:selected").val());
}

function retrieveValue(selector) {
  if ($(selector).length !== 0) {
    return makeArray($(selector).val());
  } else {
    return [];
  }
}

function makeArray(str) {
  if (typeof str === 'undefined') {
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

function sendVerificationSMS(telephone) {
  $.ajax({
    url: "/api/sendVerificationSMS?telephone=" + telephone + "&userId=" + id,
    method: "GET",
    success: function (result) {
      verificationCode = result;
    }
  });
}

function showCodeVerificationModal(telephone) {
  vex.dialog.open({
    message: "За да завършите успешно създаването на проекта си трябва първо да потвърдите имейла си. Ние ви изпратихме Е-майл с кода за достъп, въведете кода за достъп:",
    input: [
      '<div class="input-group"><span class="input-group-addon"><input type="text" name="code" class="form-control" placeholder="Код"></span"</div>'
    ].join(""),
    callback: function (data) {
      if (!data) {
        return console.log("Cancelled");
      }

      if ((verificationCode = data.code)) {
        var name = $("#projectName").val();
        var description = tinyMCE.get("textareaBasic").getContent();
        var goal1 = $("#projectGoals").val();
        var goal2 = $("#projectGoals2").val();
        var goal3 = $("#projectGoals3").val();
        var githubPage = $("#github-page").val();
        var address = $("#arena-address").val();
        var videoLink = $("#video-of-project").val();
        var neededMoney;
        if (needsMoney) {
          neededMoney = $("#neededMoney").val();
        } else {
          neededMoney = 0.0;
        }

        var needsProgrammer = document.querySelector("#needsProgrammer").checked;
        var needsDesigner = document.querySelector("#needsDesigner").checked;
        var needsEngineer = document.querySelector("#needsEngineer").checked;
        var needsWriter = document.querySelector("#needsWriter").checked;
        var needsScientist = document.querySelector("#needsScientist").checked;
        var needsMusician = document.querySelector("#needsMusician").checked;
        var needsFilmmaker = document.querySelector("#needsFilmmaker").checked;
        var needsProductManager = document.querySelector("#needsProductManager")
          .checked;
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

        if (address.split(",").length - 1 > 1) {
          var index = address.indexOf(", България");
          address = address.substring(0, index);
          var lastIndex = address.lastIndexOf(", ");
          address = address.substring(lastIndex + 2);
        } else {
          var index = address.indexOf(", България");
          address = address.substring(0, index);
        }
        // alert(address.substring(lastIndex + 2) + " " + lat + " " + lng);

        var parametersData = createParametersJSONData();
        var otherData = JSON.stringify({
          name: name,
          description: description,
          goal1: goal1,
          goal2: goal2,
          goal3: goal3,
          githubPage: githubPage,
          city: address,
          youtubeLink: videoLink,
          projectNeeds: [{
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
          telephone: telephone,
          neededMoney: neededMoney,
          receivedMoney: 0,
          needsMoney: needsMoney,
          latitude: lat,
          longitude: lng,
          parameters: parametersData
        })

        $.ajax({
          url: "/api/createProject?creatorId=" + id,
          method: "POST",
          data: otherData,
          contentType: "application/json",
          success: function (result) {
            uploader.fineUploader(
              "setEndpoint",
              "/api/uploadProjectPicture?id=" + result.id
            );
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

            if ($(".qq-file-id-7").length !== 0) {
              uploader.fineUploader("setName", 7, "7");
            }

            if ($(".qq-file-id-8").length !== 0) {
              uploader.fineUploader("setName", 8, "8");
            }

            if ($(".qq-file-id-9").length !== 0) {
              uploader.fineUploader("setName", 9, "9");
            }

            $("#fine-uploader").fineUploader("uploadStoredFiles");
          }
        });
      } else {
        alert("Вашият код беше грешен :(");
      }
      // }
      // });
    }
  });
}

$(".preloader").fadeOut(500);