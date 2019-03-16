var user = null;
var changed = false;

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

function split(val) {
  return val.split(/,\s*/);
}

function extractLast(term) {
  return split(term).pop();
}

var extension = "";

function readURL(input) {
  if (input.files && input.files[0]) {
    extension = input.files[0].name.split('.').pop().toLowerCase();
    var reader = new FileReader();

    reader.onload = function (e) {
      var src = e.target.result;
      $('.img-upload').attr('src', e.target.result);

      $.ajax({
        url: "/api/changeImage?id=" + id,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({
          base64: $(".img-upload").attr("src"),
          extension: extension
        }),
        success: function (result) {
          if (result == true || result == "true") {
            $("#profile-image-navbar").attr("src", src);
            iziToast.success({
              title: "Грешка",
              message: "Вие успешно сменихте профилната си снимка",
              position: "topRight"
            });
          } else {
            iziToast.error({
              title: "Грешка",
              message: "Възникна грешка при сменянето на профилната снимка",
              position: "topRight"
            });
          }

        }
      })
    }

    reader.readAsDataURL(input.files[0]);
  }
}

function setUpAutocomplete() {
  $("#city-profile").autocomplete({
    source: "/api/getCities",
    minLength: 2,
    select: function (event, ui) {
    },
    autoFocus: true
  });

  $("#skills-profile")
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

  $("#interests-profile")
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

  $("#languages-profile")
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

  $("#employer-profile").autocomplete({
    source: "/api/getEmployedBy",
    minLength: 2,
    select: function (event, ui) {
    },
    autoFocus: true
  });

  $("#previous-employer-profile")
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
}

$(document).ready(function () {
  $(".profile-github-contributions").click(function () {
    $.ajax({
      url: "/api/stopGithubStatistics?id=" + id,
      method: "GET",
      success: function (result) {
        location.replace("/profile");
      }
    });
  });

  $(".profile-github-contributions-show").click(function () {
    $.ajax({
      url: "/api/startGithubStatistics?id=" + id,
      method: "GET",
      success: function (result) {
        location.replace("/profile");
      }
    });
  });

  $(".profile-delete-btn").click(function () {
    $.confirm({
      title: "Потвърдете изтриването",
      content: "Сигурни ли сте, че искате да изтриете профила си?",
      theme: "supervan",
      buttons: {
        Да: {
          btnClass: "btn-blue",
          action: function () {
            $.ajax({
              url: "/api/deleteUser?id=" + id,
              method: "DELETE",
              success: function (result) {
                location.replace("/sign-in?deleted");
              }
            })
          }
        },
        Не: {
          btnClass: "btn-blue",
          action: function () {

          }
        }
      }
    });
  });

  setUpAutocomplete();

  $("#image-file-uploader").change(function () {
    readURL(this);
  });

  $(".img-upload").click(function () {
    $("#image-file-uploader").trigger("click");
  })

  $(".personal-information-change").click(function () {
    var firstName = $("#first-name-profile").val();
    var lastName = $("#last-name-profile").val();
    var description = $("#descriptionOfProfile").val();

    if (firstName.length === 0) {
      iziToast.error({
        title: "Грешка",
        message: "Първото име не може да е празно",
        position: "topRight"
      });
      return false;
    }

    if (firstName.length < 2 || firstName.length > 16) {
      iziToast.error({
        title: "Грешка",
        message: "Първото име трябва да е между 2 и 16 символа",
        position: "topRight"
      });
      return false;
    }

    if (lastName.length === 0) {
      iziToast.error({
        title: "Грешка",
        message: "Фамилията не може да е празно",
        position: "topRight"
      });
      return false;
    }

    if (lastName.length < 2 || lastName.length > 16) {
      iziToast.error({
        title: "Грешка",
        message: "Фамилията трябва да е между 2 и 16 символа",
        position: "topRight"
      });
      return false;
    }

    if (description.length < 25) {
      iziToast.error({
        title: "Грешка",
        message: "Описанието трябва да е минимум 25 символа",
        position: "topRight"
      });
      return false;
    }

    var instance = $(this);

    instance.html('<i class="fa fa-check"></i>Промени данните <i class="fas fa-circle-notch fa-spin"></i>');

    $.ajax({
      url: "/api/changePersonalInfo?id=" + id,
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify({
        firstName: firstName,
        lastName: lastName,
        description: description
      }),
      success: function (result) {
        instance.html('<i class="fa fa-check"></i>Промени данните');
        $(".portlet-body-personal-info").attr("style", "display: flex; justify-content: center; align-items: center;");
        if (result === true || result === 'true') {
          $(".portlet-body-personal-info").html('<div class="alert-success"><span>Вие успешно променихте данните си</span></div>');
        } else {
          $(".portlet-body-personal-info").html('<div class="alert-danger"><span>Възникна проблем при променянето на данните</span></div>');
        }
      }
    })
  });

  $(".other-information-change").click(function () {
    var city = $("#city-profile").val();
    var skills = $("#skills-profile")
      .val()
      .split(", ");
    var interests = $("#interests-profile")
      .val()
      .split(", ");
    var languages = $("#languages-profile")
      .val()
      .split(", ");
    var personalSite = $("#personal-site-profile").val();
    var employedBy = $("#employer-profile").val();
    var previousEmployment = $("#previous-employer-profile")
      .val()
      .split(", ");
    var degree = $("#education-profile").children("option:selected").val();
    var unemployed = false;
    if ($("#previous-employer-profile").val() === '') {
      unemployed = true;
    }
    var github = "";
    if($("#github-profile").length !== 0){
      github = $("#github-profile").val();
    }

    var instance = $(this);

    instance.html('<i class="fa fa-check"></i>Промени данните <i class="fas fa-circle-notch fa-spin"></i>');
    $.ajax({
      url: "/api/changeOtherInfo?id=" + id,
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
        degree: degree,
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
        githubUsername: github
      }),
      contentType: "application/json",
      success: function (result) {
        instance.html('<i class="fa fa-check"></i>Промени данните');
        $(".portlet-body-other-info").attr("style", "display: flex; justify-content: center; align-items: center;");
        if (result === true || result === 'true') {
          $(".portlet-body-other-info").html('<div class="alert-success"><span>Вие успешно променихте данните си</span></div>');
        } else {
          $(".portlet-body-other-info").html('<div class="alert-danger"><span>Възникна проблем при променянето на данните</span></div>');
        }
      }
    });
  });

  $(".password-information-change").click(function () {
    var password = $("#new-pass").val();
    if (password.length === 0) {
      iziToast.error({
        title: "Грешка",
        message: "Паролата не може дае празна",
        position: "topRight"
      });
      return false;
    }

    if (password.length < 8 || password.length > 20) {
      iziToast.error({
        title: "Грешка",
        message: "Паролата трябва да е между 8 и 20 символа",
        position: "topRight"
      });
      return false;
    }

    if (password !== $("#new-pass-re").val()) {
      iziToast.error({
        title: "Грешка",
        message: "Паролите не съвпадат",
        position: "topRight"
      });
      return false;
    }

    var instance = $(this);

    instance.html('<i class="fa fa-check"></i>Промени данните <i class="fas fa-circle-notch fa-spin"></i>');
    $.ajax({
      url: "/api/checkIfPassIsCorrect?id=" + id + "&password=" + $("#old-pass").val(),
      method: "GET",
      success: function (result) {
        if (result === false) {
          instance.html('<i class="fa fa-check"></i>Промени данните');
          iziToast.error({
            title: "Грешка",
            message: "Старата парола е грешна",
            position: "topRight"
          });
        } else {
          $.ajax({
            url: "/api/changePassword?id=" + id + "&newPass=" + password,
            method: "GET",
            success: function (result) {
              instance.html('<i class="fa fa-check"></i>Промени данните');
              $(".portlet-body-password-info").attr("style", "display: flex; justify-content: center; align-items: center;");
              if (result === true || result === 'true') {
                $(".portlet-body-password-info").html('<div class="alert-success"><span>Вие успешно променихте данните си</span></div>');
              } else {
                $(".portlet-body-password-info").html('<div class="alert-danger"><span>Възникна проблем при променянето на данните</span></div>');
              }
            }
          });
        }
      }
    });
  });

  $(".preloader").fadeOut(500);
});

setUpWizard();



function setUpWizard() {
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
        window.location.href = "/profile";
      }
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
          .text("Запази")
          .addClass("btn btn-info")
          .on("click", function () {
            var firstName = $("#profileFirstName").val();
            var lastName = $("#profileLastName").val();
            var email = $("#profileEmail").val();
            var description = $("#profileDescription").val();
            var password = $("#profilePassword").val();

            if (firstName.length === 0) {
              iziToast.error({
                title: "Грешка",
                message: "Първото име не може да е празно",
                position: "topRight"
              });
              return false;
            }

            if (firstName.length < 2 || firstName.length > 16) {
              iziToast.error({
                title: "Грешка",
                message: "Първото име трябва да е между 2 и 16 символа",
                position: "topRight"
              });
              return false;
            }

            if (lastName.length === 0) {
              iziToast.error({
                title: "Грешка",
                message: "Фамилията не може да е празно",
                position: "topRight"
              });
              return false;
            }

            if (lastName.length < 2 || lastName.length > 16) {
              iziToast.error({
                title: "Грешка",
                message: "Фамилията трябва да е между 2 и 16 символа",
                position: "topRight"
              });
              return false;
            }

            if (email.length === 0) {
              iziToast.error({
                title: "Грешка",
                message: "Емайлът не може да бъде празен",
                position: "topRight"
              });
              return false;
            }

            if (description.length < 25) {
              iziToast.error({
                title: "Грешка",
                message: "Описанието трябва да е минимум 25 символа",
                position: "topRight"
              });
              return false;
            }

            if (password.length === 0) {
              iziToast.error({
                title: "Грешка",
                message: "Паролата не може дае празна",
                position: "topRight"
              });
              return false;
            }

            if (password.length < 8 || password.length > 20) {
              iziToast.error({
                title: "Грешка",
                message: "Паролата трябва да е между 8 и 20 символа",
                position: "topRight"
              });
              return false;
            }

            if (password !== $("#profileConfirmPassword").val()) {
              iziToast.error({
                title: "Грешка",
                message: "Паролите не съвпадат",
                position: "topRight"
              });
              return false;
            }

            // to check if email is free
            if (changed) {
              $.ajax({
                url: "/api/checkIfEmailIsTaken?email=" + email,
                method: "POST",
                contentType: "application/json",
                success: function (result) {
                  if (result === false) {
                    iziToast.error({
                      title: "Грешка",
                      message: "Емайлът вече е зает",
                      position: "topRight"
                    });
                  } else {
                    window.location.href = "/profile";
                  }
                }
              });
            }

            $.ajax({
              url: "/api/editProfile",
              method: "POST",
              contentType: "application/json",
              data: JSON.stringify({
                id: id,
                firstName: firstName,
                lastName: lastName,
                description: description,
                email: email,
                password: password
              }),
              success: function (result) {
                if (result) {
                  uploader.fineUploader(
                    "setEndpoint",
                    "/api/uploadProfilePicture?id=" + id
                  );

                  if ($(".qq-file-id-0").length !== 0) {
                    $("#fine-uploader").fineUploader("uploadStoredFiles");
                  } else {
                    window.location.href = "/profile";
                  }
                }
              }
            });
          }),
          $("<button></button>")
          .text("Изтрий")
          .addClass("btn btn-danger frpe")
          .on("click", function () {
            $.confirm({
              title: "Потвърдете изтриването",
              content: "Сигурни ли сте, че искате да изтриете профила си?",
              theme: "supervan",
              buttons: {
                Да: {
                  btnClass: "btn-blue",
                  action: function () {
                    $.ajax({
                      url: "/api/deleteUser?id=" + id,
                      method: "DELETE",
                      success: function (result) {
                        location.replace("/sign-in?deleted");
                      }
                    })
                  }
                },
                Не: {
                  btnClass: "btn-blue",
                  action: function () {

                  }
                }
              }
            });
          })
        ]
      }
    });

    $("#smartwizard").on("leaveStep", function (
      e,
      anchorObject,
      stepNumber,
      stepDirection
    ) {});

    $("#profileEmail").change(function () {
      changed = true;
    });
  });
}