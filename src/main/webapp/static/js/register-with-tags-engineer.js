var uploader = $("#fine-uploader").fineUploader({
  debug: true,
  request: {
    endpoint: "/api/uploadProfilePicture"
  },
  deleteFile: {
    enabled: true,
    endpoint: "/api/deleteProfilePicture"
  },
  retry: {
    enableAuto: true
  },
  multiple: false,
  onLeave: "Вашата снимка се качва вмомента. Сигурни ли сте че искате да напуснете страницата.",
  autoUpload: false,
  thumbnails: {
    placeholders: {
      waitingPath: "/source/placeholders/waiting-generic.png",
      notAvailablePath: "/source/placeholders/not_available-generic.png"
    }
  },
  validation: {
    sizeLimit: 20485760,
    itemLimit: 1,
    allowedExtensions: ["jpeg", "jpg", "gif", "png"]
  },
  callbacks: {
    onAllComplete: function (succeeded, failed) {
      location.replace("/activation?notactivated");
    }
  }
});

$(function () {
  function split(val) {
    return val.split(/,\s*/);
  }

  function extractLast(term) {
    return split(term).pop();
  }

  $("#location").autocomplete({
    source: "/api/getCities",
    minLength: 2,
    select: function (event, ui) {
    },
    autoFocus: true
  });

  $("#skills")
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

  $("#interests")
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
          "/api/getInterests", {
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

  $("#employed-by").autocomplete({
    source: "/api/getEmployedBy",
    minLength: 2,
    select: function (event, ui) {
    },
    autoFocus: true
  });

  $("#previous-employment")
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
          "/api/getEmployedBy", {
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

  $(".preloader").fadeOut(400);
});

var firstName = sessionStorage.getItem("firstName");
var lastName = sessionStorage.getItem("lastName");
var email = sessionStorage.getItem("email");
var password = sessionStorage.getItem("password");
var description = sessionStorage.getItem("description");
if (firstName === null ||
  lastName === null ||
  email === null ||
  password === null ||
  description === null) {
  location.replace("/home");
}

var dropdownValue = null;

$(".dropdown-item").click(function () {
  dropdownValue = $(this).text();
  $("#educationDropdown").text(dropdownValue);
});

$("#finish-btn").on("click", function () {
  var skillset = $("#var-text2")
    .text()
    .substring(3);
  var city = $("#location").val();
  var skills = $("#skills")
    .val()
    .split(", ");
  var interests = $("#interests")
    .val()
    .split(", ");
  var languages = $("#languages")
    .val()
    .split(", ");
  var workSample = $("#work-sample").val();
  var personalSite = $("#personal-site").val();
  var employedBy = null;
  var unemployed = false;
  if ($("#checkbox1").is(":checked")) {
    unemployed = true;
  } else {
    employedBy = $("#employed-by").val();
  }
  var previousEmployment = $("#previous-employment")
    .val()
    .split(", ");

  var skillsetToSend =
    "" + skillset.charAt(0).toUpperCase() + skillset.slice(1);

  var uploadedFiles = uploader.fineUploader("getUploads");

  $.ajax({
    url: "/api/areTagsCorrect",
    method: "POST",
    data: JSON.stringify({
      city: {
        name: city
      },
      skills: [{
          name: skills[0]
        },
        {
          name: skills[1]
        },
        {
          name: skills[2]
        },
        {
          name: skills[3]
        },
        {
          name: skills[4]
        },
        {
          name: skills[5]
        }
      ],
      interests: [{
          name: interests[0]
        },
        {
          name: interests[1]
        },
        {
          name: interests[2]
        },
        {
          name: interests[3]
        },
        {
          name: interests[4]
        },
        {
          name: interests[5]
        }
      ],
      languages: [{
          name: languages[0]
        },
        {
          name: languages[1]
        },
        {
          name: languages[2]
        },
        {
          name: languages[3]
        },
        {
          name: languages[4]
        },
        {
          name: languages[5]
        }
      ],
      employer: {
        name: employedBy
      },
      previousEmployment: [{
          name: previousEmployment[0]
        },
        {
          name: previousEmployment[1]
        },
        {
          name: previousEmployment[2]
        },
        {
          name: previousEmployment[3]
        },
        {
          name: previousEmployment[4]
        },
        {
          name: previousEmployment[5]
        }
      ]
    }),
    contentType: "application/json",
    success: function (result) {
      if (result === true || result === "true") {
        iziToast.question({
          timeout: 20000,
          close: false,
          overlay: true,
          displayMode: "once",
          id: "question",
          zindex: 999,
          title: "Здравей",
          message: 'С регистрацията се съгласявате с нашите <a href="/termsAndConditions">Условия и правила</a>',
          position: "center",
          buttons: [
            [
              "<button><b>Добре!</b></button>",
              function (instance, toast) {
                instance.hide({
                  transitionOut: "fadeOut"
                }, toast, "button");
                $(".preloader").fadeIn(400);

                $.ajax({
                  url: "/api/register",
                  method: "POST",
                  data: JSON.stringify({
                    firstName: firstName,
                    lastName: lastName,
                    email: email,
                    password: password,
                    description: description,
                    city: {
                      name: city
                    },
                    skills: [{
                        name: skills[0]
                      },
                      {
                        name: skills[1]
                      },
                      {
                        name: skills[2]
                      },
                      {
                        name: skills[3]
                      },
                      {
                        name: skills[4]
                      },
                      {
                        name: skills[5]
                      }
                    ],
                    degree: dropdownValue,
                    interests: [{
                        name: interests[0]
                      },
                      {
                        name: interests[1]
                      },
                      {
                        name: interests[2]
                      },
                      {
                        name: interests[3]
                      },
                      {
                        name: interests[4]
                      },
                      {
                        name: interests[5]
                      }
                    ],
                    languages: [{
                        name: languages[0]
                      },
                      {
                        name: languages[1]
                      },
                      {
                        name: languages[2]
                      },
                      {
                        name: languages[3]
                      },
                      {
                        name: languages[4]
                      },
                      {
                        name: languages[5]
                      }
                    ],
                    githubUsername: null,
                    workSample: workSample,
                    personalSite: personalSite,
                    employer: {
                      name: employedBy
                    },
                    unemployed: unemployed,
                    previousEmployment: [{
                        name: previousEmployment[0]
                      },
                      {
                        name: previousEmployment[1]
                      },
                      {
                        name: previousEmployment[2]
                      },
                      {
                        name: previousEmployment[3]
                      },
                      {
                        name: previousEmployment[4]
                      },
                      {
                        name: previousEmployment[5]
                      }
                    ],
                    skillset: {
                      name: skillsetToSend
                    }
                  }),
                  contentType: "application/json",
                  success: function (result) {
                    if (uploadedFiles.length !== 0) {
                      uploader.fineUploader(
                        "setEndpoint",
                        "/api/uploadProfilePicture?id=" + result.id
                      );
                      $("#fine-uploader").fineUploader("uploadStoredFiles");
                    } else {

                      location.replace("/activation?notactivated");
                    }
                  }
                });
              },
              true
            ],
            [
              "<button>Отказ</button>",
              function (instance, toast) {
                instance.hide({
                  transitionOut: "fadeOut"
                }, toast, "button");
              }
            ]
          ],
          onClosing: function (instance, toast, closedBy) {
            console.info("Closing | closedBy: " + closedBy);
          },
          onClosed: function (instance, toast, closedBy) {
            console.info("Closed | closedBy: " + closedBy);
          }
        });
      } else {
        iziToast.error({
          title: "Грешка",
          message: "Някое от полетата във втора стъпка не е валидно",
          position: "topRight"
        });
      }
    }
  })
});