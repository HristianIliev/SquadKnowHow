$("#showVideoDialog").click(function() {
  vex.dialog.open({
    message: "Избери потребители за видео разговор:",
    input: [
      '<input id="video-call-users" name="emails" type="text" placeholder="Потребители по email" required />'
    ].join(""),
    buttons: [
      $.extend({}, vex.dialog.buttons.YES, { text: "Продължи" }),
      $.extend({}, vex.dialog.buttons.NO, { text: "Затвори" })
    ],
    callback: function(data) {
      if (!data) {
        console.log("Cancelled");
      } else {
        $.ajax({
          url: "/api/checkOnline?emails=" + data.emails,
          method: "GET",
          success: function(result) {
            function join() {
              // document.getElementById("join").disabled = true;
              // document.getElementById("video").disabled = true;
              // var channel_key = null;
              // console.log("Init AgoraRTC client with App ID: " + appId.value);
              client = AgoraRTC.createClient({ mode: "live" });
              client.init(
                "f93511279880484bae87889cf18c8946",
                function() {
                  console.log("AgoraRTC client initialized");
                  client.join(
                    null,
                    "webtest",
                    undefined,
                    function(uid) {
                      console.log("User " + uid + " join channel successfully");
                      if (document.getElementById("video").checked) {
                        camera = videoSource.value;
                        microphone = audioSource.value;
                        localStream = AgoraRTC.createStream({
                          streamID: uid,
                          audio: true,
                          cameraId: camera,
                          microphoneId: microphone,
                          video: true,
                          screen: false
                        });
                        //localStream = AgoraRTC.createStream({streamID: uid, audio: false, cameraId: camera, microphoneId: microphone, video: false, screen: true, extensionId: 'minllpmhdgpndnkomcoccfekfegnlikg'});
                        // if (document.getElementById("video").checked) {
                        localStream.setVideoProfile("720p_3");
                        // }
                        // The user has granted access to the camera and mic.
                        localStream.on("accessAllowed", function() {
                          console.log("accessAllowed");
                        });
                        // The user has denied access to the camera and mic.
                        localStream.on("accessDenied", function() {
                          console.log("accessDenied");
                        });
                        localStream.init(
                          function() {
                            console.log("getUserMedia successfully");
                            localStream.play("agora_local");
                            client.publish(localStream, function(err) {
                              console.log("Publish local stream error: " + err);
                            });
                            client.on("stream-published", function(evt) {
                              console.log("Publish local stream successfully");
                            });
                          },
                          function(err) {
                            console.log("getUserMedia failed", err);
                          }
                        );
                      }
                    },
                    function(err) {
                      console.log("Join channel failed", err);
                    }
                  );
                },
                function(err) {
                  console.log("AgoraRTC client init failed", err);
                }
              );
              // channelKey = "";
              client.on("error", function(err) {
                console.log("Got error msg:", err.reason);
                if (err.reason === "DYNAMIC_KEY_TIMEOUT") {
                  client.renewChannelKey(
                    null,
                    function() {
                      console.log("Renew channel key successfully");
                    },
                    function(err) {
                      console.log("Renew channel key failed: ", err);
                    }
                  );
                }
              });
              client.on("stream-added", function(evt) {
                var stream = evt.stream;
                console.log("New stream added: " + stream.getId());
                console.log("Subscribe ", stream);
                client.subscribe(stream, function(err) {
                  console.log("Subscribe stream failed", err);
                });
              });
              client.on("stream-subscribed", function(evt) {
                var stream = evt.stream;
                console.log(
                  "Subscribe remote stream successfully: " + stream.getId()
                );
                if (
                  $("div#video #agora_remote" + stream.getId()).length === 0
                ) {
                  $("div#video").append(
                    '<div id="agora_remote' +
                      stream.getId() +
                      '" style="float:left; width:810px;height:607px;display:inline-block;"></div>'
                  );
                }
                stream.play("agora_remote" + stream.getId());
              });
              client.on("stream-removed", function(evt) {
                var stream = evt.stream;
                stream.stop();
                $("#agora_remote" + stream.getId()).remove();
                console.log("Remote stream is removed " + stream.getId());
              });
              client.on("peer-leave", function(evt) {
                var stream = evt.stream;
                if (stream) {
                  stream.stop();
                  $("#agora_remote" + stream.getId()).remove();
                  console.log(evt.uid + " leaved from this channel");
                }
              });
            }

            join();
          }
        });
      }
    }
  });

  function split(val) {
    return val.split(/,\s*/);
  }
  function extractLast(term) {
    return split(term).pop();
  }

  $("#video-call-users")
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
          "/api/getEmails",
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

        // var interestToSend = $("#video-call-users")
        //     .val()
        //     .substring(0, $("#interested-in").val().length - 2);
        // sendGetPages(
        //     $("#search-by-city").val(),
        //     $("#skilled-in")
        //         .val()
        //         .substring(0, $("#skilled-in").val().length - 2),
        //     interestToSend
        // );
        return false;
      },
      autoFocus: true
    });
});