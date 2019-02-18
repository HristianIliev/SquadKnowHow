var user = null;

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

$("#start-a-project-btn").click(function () {
  window.location.href = "/create-project";
});

getProjectsOfUser();

function getProjectsOfUser() {
  $(".should-send-project-id").on("click", function () {
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var projectIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);
    window.location.href = "/project-admin/" + projectIdToSend;
  });

  $(".should-send-project-id-member").on("click", function () {
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var projectIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);
    window.location.href = "/project-member/" + projectIdToSend;
  });

  $(".should-send-project-id-not-member").on("click", function () {
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var projectIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);
    window.location.href = "/project/" + projectIdToSend;
  });

  $(".project-edit-button").click(function () {
    var instance = $(this);
    var indexOfUnderscore = instance.attr("id").indexOf("_");
    var projectId = instance.attr("id").substring(indexOfUnderscore + 1);

    window.location.href = "/edit-project/" + projectId;
  });

  $(".project-remove-member-button").click(function () {
    var instance = $(this);
    $.confirm({
      title: "Потвърдете напускането",
      content: "Сигурни ли сте, че искате да напуснете този проект",
      theme: "supervan",
      buttons: {
        Да: {
          btnClass: "btn-blue",
          action: function () {
            instance.html(
              '<i class="fas fa-sign-out-alt"></i> Напусни&nbsp;&nbsp;&nbsp; <i class="fas fa-circle-notch fa-spin"></i>'
            );
            var indexOfUnderscore = instance.attr("id").indexOf("_");
            var projectId = instance
              .attr("id")
              .substring(indexOfUnderscore + 1);

            $.ajax({
              url: "/api/removeProjectMember?projectId=" +
                projectId +
                "&memberId=" +
                id,
              method: "PUT",
              success: function (result) {
                instance.html(
                  '<i class="fas fa-sign-out-alt"></i> Напусни&nbsp;&nbsp;&nbsp;'
                );
                if (result.successfull) {
                  instance
                    .parent()
                    .parent()
                    .parent()
                    .parent()
                    .remove();
                  iziToast.success({
                    title: "ОК!",
                    message: "Напускането беше успешно",
                    position: "topRight"
                  });
                } else {
                  iziToast.error({
                    title: "Грешка",
                    message: "Напускането не беше успешно",
                    position: "topRight"
                  });
                }
              }
            });
          }
        },
        Не: {
          btnClass: "btn-blue",
          action: function () {
            iziToast.warning({
              title: "Отказ",
              message: "Този проект не беше напуснат",
              position: "topRight"
            });
          }
        }
      }
    });
  });

  $(document).ready(function () {
    $(".project-delete-button").click(function () {
      var instance = $(this);
      $.confirm({
        title: "Потвърдете изтриването",
        content: "Сигурни ли сте, че искате да изтриете този проект",
        theme: "supervan",
        buttons: {
          Да: {
            btnClass: "btn-blue",
            action: function () {
              var isCompleted = false;
              instance.html(
                '<i class="fas fa-minus-circle"></i> Изтрий&nbsp;&nbsp;&nbsp; <i class="fas fa-circle-notch fa-spin"></i>'
              );
              var indexOfUnderscore = instance.attr("id").indexOf("_");
              var projectId = instance
                .attr("id")
                .substring(indexOfUnderscore + 1);

              $.ajax({
                url: "/api/getProjectInformation?projectId=" + projectId,
                method: "GET",
                success: function (result) {
                  var currentArray = {
                    memberId: 0,
                    communication: 0,
                    initiative: 0,
                    leadership: 0,
                    innovation: 0,
                    responsibility: 0,
                    review: "",
                    rating: 0,
                    activities: "",
                    recommended: false
                  };
                  var arraysWithInfo = [];
                  var progressSteps = [];
                  progressSteps.push("1");
                  var queueOfModals = [];
                  queueOfModals.push({
                    type: 'question',
                    html: '<b>Приключи ли успешно проекта си</b> <span style="display: block;">Ако да за да завършиш проекта си напълно първо трябва да дадеш оценка и ревю на екипа си</span>'
                  });
                  for (var i = 0, num = 2; i < result.projectMembers.length; i += 1) {
                    if (result.projectMembers[i].id !== result.creator) {
                      progressSteps.push(num.toString());
                      queueOfModals.push({
                        heightAuto: true,
                        input: 'textarea',
                        inputPlaceholder: 'Напиши своето ревю...',
                        confirmButtonText: 'Продължи!',
                        cancelButtonText: 'Отказ!',
                        confirmButtonClass: "not",
                        cancelButtonClass: "not",
                        html: '<span id="' + result.projectMembers[i].id + '" class="member-id"></span> Участник: ' + result.projectMembers[i].firstName + ' ' + result.projectMembers[i].lastName + '<div class="ratings-wrapper"><span id="rating-badge" class="rating-badge"></span><select id="example"><option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option></select></div><div class="soft-skills-wrapper"><div class="soft-skill"><img src="data:image/png;base64,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"alt="" class="soft-skill-image"><div class="soft-skill-information"><span style="font-size: 14px;">Комуникация</span><input type="text" class="example-slider example-slider-communication" name="example_name" value="" /></div></div><div class="soft-skill"><img src="data:image/png;base64,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"alt="" class="soft-skill-image"><div class="soft-skill-information"><span style="font-size: 14px;">Инициативност</span><input type="text" class="example-slider example-slider-initiative" name="example_name" value="" /></div></div><div class="soft-skill"><img src="data:image/png;base64,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"alt="" class="soft-skill-image"><div class="soft-skill-information"><span style="font-size: 14px;">Лидерство</span><input type="text" class="example-slider example-slider-leadership" name="example_name" value="" /></div></div>  <div class="soft-skill"><img src="data:image/png;base64,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"alt="" class="soft-skill-image"><div class="soft-skill-information"><span style="font-size: 14px;">Иновативност</span><input type="text" class="example-slider example-slider-innovation" name="example_name" value="" /></div></div><div class="soft-skill"><img src="data:image/png;base64,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"alt="" class="soft-skill-image"><div class="soft-skill-information"><span style="font-size: 14px;">Отговорност</span><input type="text" class="example-slider example-slider-responsibility" name="example_name" value="" /></div></div></div><div style="margin-top: 10px;display: flex;justify-content: space-between;margin-bottom: 20px;"><span style="font-size: 19px;">Препоръчваш ли ' + result.projectMembers[i].firstName + ' ' + result.projectMembers[i].lastName + ' на други хора?</span><div class="pretty p-switch p-slim"><input id="checkbox1" type="checkbox" /><div class="state p-primary"><label></label></div></div></div><input class="activities form-control form-control-lg" type="text" placeholder="Какви дейности е извършвал този участник..."></input>',
                        onOpen: () => {
                          $('#checkbox1').change(function () {
                            currentArray['recommended'] = this.checked;
                          });

                          currentArray['memberId'] = $(".member-id").attr("id");

                          $("#rating-badge").text("1");

                          $("#example").barrating({
                            theme: "fontawesome-stars-o",
                            readonly: false,
                            hoverState: true,
                            initialRating: 1,
                            onSelect: function (value, text, event) {
                              $("#rating-badge").text(value);
                              currentArray['rating'] = value;
                            }
                          });

                          $(".example-slider-communication").ionRangeSlider({
                            min: 0,
                            max: 100,
                            grid: true,
                            from: 0,
                            postfix: "%",
                            onFinish: function (data) {
                              currentArray['communication'] = data.from;
                            }
                          });

                          $(".example-slider-initiative").ionRangeSlider({
                            min: 0,
                            max: 100,
                            grid: true,
                            from: 0,
                            postfix: "%",
                            onFinish: function (data) {
                              currentArray['initiative'] = data.from;
                            }
                          });

                          $(".example-slider-leadership").ionRangeSlider({
                            min: 0,
                            max: 100,
                            grid: true,
                            from: 0,
                            postfix: "%",
                            onFinish: function (data) {
                              currentArray['leadership'] = data.from;
                            }
                          });

                          $(".example-slider-innovation").ionRangeSlider({
                            min: 0,
                            max: 100,
                            grid: true,
                            from: 0,
                            postfix: "%",
                            onFinish: function (data) {
                              currentArray['innovation'] = data.from;
                            }
                          });

                          $(".example-slider-responsibility").ionRangeSlider({
                            min: 0,
                            max: 100,
                            grid: true,
                            from: 0,
                            postfix: "%",
                            onFinish: function (data) {
                              currentArray['responsibility'] = data.from;
                            }
                          });
                        },
                        onBeforeOpen: () => {
                          $(".swal2-popup").attr("style", "width: 512px;display: flex;height: auto;")
                        },
                        onClose: () => {
                          currentArray['review'] = $(".swal2-textarea").val();
                          currentArray['activities'] = $(".activities").val();
                          arraysWithInfo.push(currentArray);
                          currentArray = {
                            memberId: 0,
                            communication: 0,
                            initiative: 0,
                            leadership: 0,
                            innovation: 0,
                            responsibility: 0,
                            review: "",
                            rating: 0,
                            activities: "",
                            recommended: false
                          };
                        }
                      });

                      num += 1;
                    }

                    Swal.mixin({
                      width: '512px',
                      type: 'question',
                      html: '<b>Приключи ли успешно проекта си</b>',
                      showCloseButton: true,
                      showCancelButton: true,
                      focusConfirm: false,
                      confirmButtonText: '<i class="fa fa-thumbs-up"></i> Да!',
                      cancelButtonText: '<i class="fa fa-thumbs-down"> Не...</i>',
                      confirmButtonClass: "isCompleted",
                      cancelButtonClass: "isNotCompleted",
                      progressSteps: progressSteps,
                      onClose: function () {
                        instance.html(
                          '<i class="fas fa-minus-circle"></i> Изтрий&nbsp;&nbsp;&nbsp; '
                        );
                      }
                    }).queue(queueOfModals).then((result) => {
                      if (result.value) {
                        instance.html(
                          '<i class="fas fa-minus-circle"></i> Изтрий&nbsp;&nbsp;&nbsp; <i class="fas fa-circle-notch fa-spin"></i>'
                        );
                        $.ajax({
                          url: "/api/deleteProject?projectId=" + projectId + "&isCompleted=" + isCompleted,
                          method: "POST",
                          data: JSON.stringify(arraysWithInfo),
                          contentType: "application/json",
                          success: function (result) {
                            instance.html(
                              '<i class="fas fa-minus-circle"></i> Изтрий&nbsp;&nbsp;&nbsp; '
                            );
                            if (result.successfull) {
                              instance
                                .parent()
                                .parent()
                                .parent()
                                .parent()
                                .remove();
                              iziToast.success({
                                title: "ОК!",
                                message: "Проектът беше изтрит успешно",
                                position: "topRight"
                              });
                            } else {
                              iziToast.error({
                                title: "Грешка",
                                message: "Проектът не беше изтрит успешно",
                                position: "topRight"
                              });
                            }
                          }
                        });
                      }
                    })

                    $(".isCompleted").click(function () {
                      isCompleted = true;
                    });

                    $(".isNotCompleted").click(function () {
                      isCompleted = false;
                    });
                  }
                }
              })
            }
          },
          Не: {
            btnClass: "btn-blue",
            action: function () {
              iziToast.warning({
                title: "Отказ",
                message: "Проектът не беше изтрит",
                position: "topRight"
              });
            }
          }
        }
      });
    });
  })


  $(".preloader").fadeOut(500);
}