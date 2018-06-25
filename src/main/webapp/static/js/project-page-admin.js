var arr = window.location.pathname.split("/");
var projectId = arr[arr.length - 1];

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

initialiseProjectPage();

function initialiseProjectPage() {
  $.ajax({
    url: "/api/project?id=" + projectId,
    method: "GET",
    success: function(result) {
      $(".should-send-profile-id").on("click", function(event) {
        event.preventDefault();
        var indexOfUndescore = $(this)
          .attr("id")
          .indexOf("_");
        var userIdToSend = $(this)
          .attr("id")
          .substring(indexOfUndescore + 1);
        window.location.href = "/user/" + userIdToSend;
      });

      $(".trigger-modal").click(function(e) {
        var recipientId = $(this).attr("id");

        vex.dialog.open({
          message: "Напиши своето съобщение:",
          input: [
            '<input id="new-message-topic" name="topic" type="text" placeholder="Тема" required />',
            '<div class="form-group"><textarea id="new-message-content" name="content" class="form-control" placeholder="Съдържание" rows="10"></textarea></div>'
          ].join(""),
          buttons: [
            $.extend({}, vex.dialog.buttons.YES, { text: "Изпрати" }),
            $.extend({}, vex.dialog.buttons.NO, { text: "Затвори" })
          ],
          callback: function(data) {
            if (!data) {
              console.log("Cancelled");
            } else {
              console.log("Topic:", data.topic, "Content:", data.content);

              var dt = new Date();
              var timestamp =
                "" +
                dt.getDate() +
                "." +
                (dt.getMonth() + 1) +
                "." +
                dt.getFullYear() +
                " " +
                dt.getHours() +
                ":" +
                dt.getMinutes();

              $.ajax({
                url: "/api/sendMessage",
                method: "POST",
                data: JSON.stringify({
                  topic: data.topic,
                  content: data.content,
                  timestamp: timestamp,
                  senderId: id,
                  recipientId: recipientId,
                  kind: "normal"
                }),
                contentType: "application/json",
                success: function(result) {
                  if (result.successfull) {
                    iziToast.success({
                      title: "ОК!",
                      message: "Съобщениета беше изпратено успешно",
                      position: "topRight"
                    });
                  } else {
                    iziToast.warning({
                      title: "Грешка",
                      message: "Съобщениета не беше изпратено успешно",
                      position: "topRight"
                    });
                  }
                }
              });
            }
          }
        });
      });

      tippy("[title]", {
        placement: "top",
        animation: "perspective",
        duration: 700,
        arrow: true,
        arrowType: "round",
        interactive: true,
        size: "large"
      });

      setUpTODOList(result);

      $(".preloader").fadeOut(500);
    }
  });
}

function setUpTODOList(result) {
  $(".lobilist").lobiList({
    sortable: false,
    controls: [],
    enableTodoEdit: false,
    lists: [
      {
        id: "todoList_" + projectId,
        title: "Задачи върху проекта:",
        defaultStyle: "lobilist-info",
        items: []
      }
    ],
    afterItemDelete: function(list, item) {
      $.ajax({
        url: "/api/deleteItem?itemId=" + item.id,
        method: "DELETE",
        success: function(result) {
          if (result) {
          }
        }
      });
    },
    afterMarkAsDone: function(list, checkbox) {
      var itemId = $(checkbox)
        .parent()
        .parent()
        .attr("data-id");
      var itemTitle = $(
        "li[data-id='" + itemId + "'] > .lobilist-item-title"
      ).text();
      $.ajax({
        url: "/api/markListItemAsDone?itemTitle=" + itemTitle,
        method: "GET",
        success: function(result) {
          if (result) {
          }
        }
      });
    },
    afterMarkAsUndone: function(list, checkbox) {
      var itemId = $(checkbox)
        .parent()
        .parent()
        .attr("data-id");
      var itemTitle = $(
        "li[data-id='" + itemId + "'] > .lobilist-item-title"
      ).text();
      $.ajax({
        url: "/api/markListItemAsUndone?itemTitle=" + itemTitle,
        method: "GET",
        success: function(result) {
          if (result) {
          }
        }
      });
    }
  });

  $(".btn-add-todo").click(function() {
    var title = $("input[name='title']");
    var description = $("textarea[name='description']");
    var dueDate = $("input[name='dueDate']");
    if (title.val().length !== 0) {
      $.ajax({
        url: "/api/createItem",
        method: "POST",
        data: JSON.stringify({
          title: title.val(),
          description: description.val(),
          dueDate: dueDate.val(),
          projectId: projectId
        }),
        contentType: "application/json",
        success: function(result) {
          if (result) {
          }
        }
      });
    }
  });

  var $list = $("#todoList_" + projectId).data("lobiList");
  var listEntries = result.listEntries;
  if (listEntries !== null) {
    if (listEntries.length !== 0) {
      for (var i = listEntries.length - 1; i >= 0; i -= 1) {
        $list.addItem({
          id: listEntries[i].id,
          title: listEntries[i].title,
          description: listEntries[i].description,
          dueDate: listEntries[i].dueDate,
          done: listEntries[i].done
        });
      }
    }
  }
}

$("#button-send-all-message").click(function(e) {
  var instance = $(this);
  vex.dialog.open({
    message: "Напиши своето съобщение:",
    input: [
      '<input id="new-message-topic" name="topic" type="text" placeholder="Тема" required />',
      '<div class="form-group"><textarea id="new-message-content" name="content" class="form-control" placeholder="Съдържание" rows="10"></textarea></div>'
    ].join(""),
    buttons: [
      $.extend({}, vex.dialog.buttons.YES, { text: "Изпрати" }),
      $.extend({}, vex.dialog.buttons.NO, { text: "Затвори" })
    ],
    callback: function(data) {
      if (!data) {
        console.log("Cancelled");
      } else {
        instance.html(
          '<i class="fa fa-comments"></i> Изпрати съобщение на всички <i class="fas fa-circle-notch fa-spin"></i>'
        );
        console.log("Topic:", data.topic, "Content:", data.content);

        var dt = new Date();
        var timestamp =
          "" +
          dt.getDate() +
          "." +
          (dt.getMonth() + 1) +
          "." +
          dt.getFullYear() +
          " " +
          dt.getHours() +
          ":" +
          dt.getMinutes();

        $.ajax({
          url: "/api/sendMessageToAllMembers?projectId=" + projectId,
          method: "POST",
          data: JSON.stringify({
            topic: data.topic,
            content: data.content,
            timestamp: timestamp,
            senderId: id,
            recipientId: -1,
            kind: "normal"
          }),
          contentType: "application/json",
          success: function(result) {
            instance.html(
              '<i class="fa fa-comments"></i> Изпрати съобщение на всички'
            );
            if (result.successfull) {
              iziToast.success({
                title: "ОК!",
                message: "Съобщенията бяха изпратени успешно",
                position: "topRight"
              });
            } else {
              iziToast.warning({
                title: "Грешка",
                message: "Съобщенията не бяха изпратени успешно",
                position: "topRight"
              });
            }
          }
        });
      }
    }
  });
});

$("#pills-home-tab").click(function(e) {
  $(this).tab("show");
});

$("#pills-profile-tab").click(function(e) {
  $(this).tab("show");
});

$("#pills-calendar-tab").click(function(e) {
  $(this).tab("show");
});
// }
