$(function () {
  var indexOfUndescoreForUserId = $("body")
    .attr("id")
    .indexOf("_");
  var id = $("body")
    .attr("id")
    .substring(indexOfUndescoreForUserId + 1);

  var arr = window.location.pathname.split("/");
  var projectId = arr[arr.length - 1];

  $.ajax({
    url: "/api/getNotes?id=" + id,
    method: "GET",
    success: function (result) {
      $(".note-btn__uLJR").click(function () {
        $(".note-editor-wrapper").attr("status", "entered");
      });

      // TinyMCE editor impl
      var note = "";
      for (var i = 0; i < result.length; i += 1) {
        if (result[i].name === $(".project-name-for-note").text()) {
          note = result[i].description;
        }
      }

      $('#notesEditor').html(note);

      tinymce.init({
        selector: '#notesEditor',
        theme: 'modern',
        init_instance_callback: function (editor) {
          $("#mceu_13-body").append('<div class="close-container"><i class="close-editor fas fa-times"></i></div>');

          $(".close-editor").click(function () {
            $(".note-editor-wrapper").attr("status", "notentered");
            $.ajax({
              url: "/api/createNote?id=" + id + "&projectId=" + projectId,
              method: "POST",
              data: JSON.stringify({
                name: $(".project-name-for-note").text(),
                description: tinyMCE.get("notesEditor").getContent()
              }),
              contentType: "application/json",
              success: function (result) {}
            });
          });
        }
      });
    }
  });
});