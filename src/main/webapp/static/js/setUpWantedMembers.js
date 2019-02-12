$(document).ready(function () {
    $(".span-link").click(function () {
        var vexInstance = vex.dialog.alert({
            unsafeMessage: '<i class="fas fa-circle-notch fa-spin" style="font-size: 50px;color: #138fc2;"></i>',
            className: 'vex-theme-flat-attack',
            showCloseButton: true
        });

        var indexOfUndescoreForId = $(this)
            .attr("id")
            .indexOf("_");
        var projectId = $(this)
            .attr("id")
            .substring(indexOfUndescoreForId + 1);
        $.ajax({
            url: "/api/wantedMember?id=" + projectId,
            method: "GET",
            success: function (result) {
                $(".vex-dialog-message").html('<div style="display: flex; flex-direction: column; justify-content: center;">' +
                    '<div class="wmdt">Изисквания към</div><div class="wmdt" style="font-size: 33px;font-weight: 600;">' + result.userCategory.name + '</div>' +
                    '</div>');

                if (result.city !== null) {
                    $(".vex-dialog-message").append('<div class="category-spec"><div class="category-spec-label">Град:</div>' +
                        '<div class="category-spec-value">' + result.city.name + '</div></div>');
                }

                if (result.education !== null) {
                    $(".vex-dialog-message").append('<div class="category-spec"><div class="category-spec-label">Образование:</div>' +
                        '<div class="category-spec-value">' + result.education + '</div></div>');
                }

                if (result.skills.length !== 0) {
                    var skills = "";
                    for (var i = 0; i < result.skills.length; i += 1) {
                        skills += result.skills[i].name;
                        if (i !== result.skills.length - 1) {
                            skills += ", ";
                        }
                    }
                    $(".vex-dialog-message").append('<div class="category-spec"><div class="category-spec-label">Умения:</div>' +
                        '<div class="category-spec-value">' + skills + '</div></div>');
                }

                if (result.languages.length !== 0) {
                    var languages = "";
                    for (var i = 0; i < result.languages.length; i += 1) {
                        languages += result.languages[i].name;
                        if (i !== result.languages.length - 1) {
                            languages += ", ";
                        }
                    }
                    $(".vex-dialog-message").append('<div class="category-spec"><div class="category-spec-label">Езици:</div>' +
                        '<div class="category-spec-value">' + languages + '</div></div>');
                }
            }
        })
    });

})