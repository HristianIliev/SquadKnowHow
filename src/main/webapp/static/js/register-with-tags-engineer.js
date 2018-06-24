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
  onLeave:
    "Вашата снимка се качва вмомента. Сигурни ли сте че искате да напуснете страницата.",
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
    onAllComplete: function(succeeded, failed) {
      window.location.href = "/sign-in";
    }
  }
});

$(function() {
  function split(val) {
    return val.split(/,\s*/);
  }
  function extractLast(term) {
    return split(term).pop();
  }

  $("#location").autocomplete({
    source: "/api/getCities",
    minLength: 2,
    select: function(event, ui) {
      console.log("Selected: " + ui.item.value + " aka " + ui.item.id);
    },
    autoFocus: true
  });

  $("#skills")
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

  $("#interests")
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
          "/api/getInterests",
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

  $("#employed-by").autocomplete({
    source: "/api/getEmployedBy",
    minLength: 2,
    select: function(event, ui) {
      console.log("Selected: " + ui.item.value + " aka " + ui.item.id);
    },
    autoFocus: true
  });

  $("#previous-employment")
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
          "/api/getEmployedBy",
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
});

var firstName = sessionStorage.getItem("firstName");
var lastName = sessionStorage.getItem("lastName");
var email = sessionStorage.getItem("email");
var password = sessionStorage.getItem("password");
var description = sessionStorage.getItem("description");
var dropdownValue = null;

$(".dropdown-item").click(function() {
  dropdownValue = $(this).text();
  $("#educationDropdown").text(dropdownValue);
});

$("#finish-btn").on("click", function() {
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

  var skillsetToSend = "" +  skillset.charAt(0).toUpperCase() + skillset.slice(1);

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
      skills: [
        {
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
      interests: [
        {
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
      githubUsername: null,
      workSample: workSample,
      personalSite: personalSite,
      employer: {
        name: employedBy
      },
      unemployed: unemployed,
      previousEmployment: [
        {
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
    success: function(result) {
      uploader.fineUploader(
        "setEndpoint",
        "/api/uploadProfilePicture?id=" + result.id
      );
      $("#fine-uploader").fineUploader("uploadStoredFiles");
    }
  });
});
