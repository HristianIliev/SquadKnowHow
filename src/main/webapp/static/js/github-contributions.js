$(document).ready(function () {
    if ($(".github-contribution-calendar").length !== 0) {
        var indexOfUndescoreForUserId = $(".github-username-anchor")
            .attr("id")
            .indexOf("_");
        var username = $(".github-username-anchor")
            .attr("id")
            .substring(indexOfUndescoreForUserId + 1);
        var pos = username.lastIndexOf("/");
        var github = username.substring(pos + 1);
        console.log(github);
        GitHubCalendar(".github-contribution-calendar", github, {
            responsive: true,
            summary_text: "Активност в Github на @" + github
        });
    }
});