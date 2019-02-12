var map;
var lastMarker = null;

function initMap() {
    $.ajax({
        url: "/api/getCoordinates",
        method: "GET",
        success: function (result) {
            var infowindow = new google.maps.InfoWindow();

            var defaultBoundsForAutocomplete = new google.maps.LatLngBounds(
                new google.maps.LatLng(41.10696293, 21.98533944),
                new google.maps.LatLng(44.41803141, 29.29457424)
            );

            var options = {
                bounds: defaultBoundsForAutocomplete
            };

            var input = document.getElementById("find-startup-input");
            var autocomplete = new google.maps.places.Autocomplete(input, options);

            map = new google.maps.Map(document.getElementById("map"), {
                center: {
                    lat: -34.397,
                    lng: 150.644
                },
                zoom: 14
            });
            // map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function (position) {
                    initialLocation = new google.maps.LatLng(
                        position.coords.latitude,
                        position.coords.longitude
                    );
                    map.setCenter(initialLocation);
                    var marker = new google.maps.Marker({
                        position: initialLocation,
                        animation: google.maps.Animation.DROP,
                        icon: "/static/icons/user-map-icon-2.png",
                        map: map
                    });
                });
            }

            for (var i = 0; i < result.length; i += 1) {
                var title = result[i].title;

                position = new google.maps.LatLng(
                    result[i].latitude,
                    result[i].longitude
                );

                var newMarker = new google.maps.Marker({
                    position: position,
                    animation: google.maps.Animation.DROP,
                    icon: "/static/icons/marker3.png",
                    map: map,
                    title: title,
                    info: title
                });

                google.maps.event.addListener(newMarker, 'click', function(){
                    infowindow.close();
                    infowindow.setContent('<div style="text-align: center;">' + this.info + "</div>");
                    infowindow.open(map, this);
                });
            }

            $(".searchProjects").on("submit", function () {
                var geocoder = new google.maps.Geocoder();
                console.log($(".searchedCity").val());
                geocoder.geocode({
                        address: $(".searchedCity").val()
                    },
                    function (results, status) {
                        console.log(status);
                        if (status == google.maps.GeocoderStatus.OK) {
                            var pos = {
                                lat: results[0].geometry.location.lat(),
                                lng: results[0].geometry.location.lng()
                            };
                            map.setCenter(pos);

                            if (lastMarker !== null) {
                                lastMarker.setMap(null);
                            }

                            var newMarker = new google.maps.Marker({
                                position: pos,
                                animation: google.maps.Animation.DROP,
                                map: map,
                            });

                            lastMarker = newMarker
                        }
                    }
                );

                return false;
            });
        }
    });
}