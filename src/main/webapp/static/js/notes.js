$(document).ready(function () {
  if ($(window).width() < 1440) {
    $("#dropdown-profile-image").attr(
      "style",
      "position: absolute;transform: translate3d(-10px, 93px, 0px);top: 0px;will-change: transform;"
    );
  }

  $(".generatePDF").click(function () {
    var html = "";
    $(".noteContentHtml").each(function (index, element) {
      html += '<div style="text-align:center;">' + $(this).attr("id").substring(0) + "</div>";
      html += $(this).html();
    });

    var opt = {
      margin: 10,
      filename: 'notes.pdf',
    };
    var worker = html2pdf();
    worker.set(opt).from(html).save();

    // var pdf = new jsPDF('l', 'pt', 'a4');
    // var options = {
    //   pagesplit: true
    // };

    // pdf.addHTML(html, 0, 0, options, function () {
    //   pdf.save("test.pdf");
    // });
  });

  $(".the-name").trigger("click");

  $(".panel-default").hover(
    function () {
      var id = $(this).attr("id");
      $("#" + id + " .delete-btn").attr("style", "opacity: 1;");
    },
    function () {
      $(".delete-btn").attr("style", "opacity: 0;");
    }
  );

  $(".delete-btn").click(function () {
    var indexOfUndescoreForNoteId = $(this)
      .attr("id")
      .indexOf("_");
    var noteId = $(this)
      .attr("id")
      .substring(indexOfUndescoreForNoteId + 1);

    var instance = $(this);

    $.ajax({
      url: "/api/deleteNote?noteId=" + noteId,
      method: "DELETE",
      success: function (result) {
        if (result != null) {
          $(instance)
            .parent()
            .parent()
            .parent()
            .parent()
            .fadeOut(500);
        }
      }
    });
  });

  $(".preloader").fadeOut(500);
});