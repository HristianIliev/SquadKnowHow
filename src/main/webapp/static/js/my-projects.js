var user = null;

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

getProjectsOfUser();

function getProjectsOfUser() {
  $(".project-edit-button").click(function() {
    var instance = $(this);
    var indexOfUnderscore = instance.attr("id").indexOf("_");
    var projectId = instance.attr("id").substring(indexOfUnderscore + 1);

    window.location.href = "/edit-project/" + projectId;
  });

  $(".project-remove-member-button").click(function() {
    var instance = $(this);
    $.confirm({
      title: "Потвърдете напускането",
      content: "Сигурни ли сте, че искате да напуснете този проект",
      theme: "supervan",
      buttons: {
        Да: {
          btnClass: "btn-blue",
          action: function() {
            instance.html(
              '<i class="fas fa-sign-out-alt"></i> Напусни&nbsp;&nbsp;&nbsp; <i class="fas fa-circle-notch fa-spin"></i>'
            );
            var indexOfUnderscore = instance.attr("id").indexOf("_");
            var projectId = instance
              .attr("id")
              .substring(indexOfUnderscore + 1);

            $.ajax({
              url:
                "/api/removeProjectMember?projectId=" +
                projectId +
                "&memberId=" +
                id,
              method: "PUT",
              success: function(result) {
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
          action: function() {
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

  $(".project-delete-button").click(function() {
    var instance = $(this);
    $.confirm({
      title: "Потвърдете изтриването",
      content: "Сигурни ли сте, че искате да изтриете този проект",
      theme: "supervan",
      buttons: {
        Да: {
          btnClass: "btn-blue",
          action: function() {
            instance.html(
              '<i class="fas fa-minus-circle"></i> Изтрий&nbsp;&nbsp;&nbsp; <i class="fas fa-circle-notch fa-spin"></i>'
            );
            var indexOfUnderscore = instance.attr("id").indexOf("_");
            var projectId = instance
              .attr("id")
              .substring(indexOfUnderscore + 1);

            $.ajax({
              url: "/api/deleteProject?projectId=" + projectId,
              method: "DELETE",
              success: function(result) {
                instance.html(
                  '<i class="fas fa-minus-circle"></i> Изтрий&nbsp;&nbsp;&nbsp; <i class="fas fa-circle-notch fa-spin"></i>'
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
        },
        Не: {
          btnClass: "btn-blue",
          action: function() {
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

  $(".should-send-project-id").on("click", function() {
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var projectIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);
    window.location.href = "/project-admin/" + projectIdToSend;
  });

  $(".should-send-project-id-member").on("click", function() {
    var indexOfUndescore = $(this)
      .attr("id")
      .indexOf("_");
    var projectIdToSend = $(this)
      .attr("id")
      .substring(indexOfUndescore + 1);
    window.location.href = "/project-member/" + projectIdToSend;
  });

  $(".preloader").fadeOut(500);
}
