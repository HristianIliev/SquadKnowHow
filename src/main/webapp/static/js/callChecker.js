var intervalResult = null;
var user = null;
var audio = null;
var localStream = null;
var isMicMuted = false;
var isVideoDisabled = false;

var indexOfUndescoreForUserId = $("body")
  .attr("id")
  .indexOf("_");
var id = $("body")
  .attr("id")
  .substring(indexOfUndescoreForUserId + 1);

$(document).ready(function () {
  intervalResult = setInterval(checkIfThereIsACall, 10000);

  function checkIfThereIsACall() {
    $.ajax({
      url: "/api/checkIfThereIsACall?id=" + id,
      method: "GET",
      success: function (result) {
        if (result.callerId != 0) {
          audio = new Audio("/static/sounds/ringing.mp3");
          audio.play();

          $.ajax({
            url: "/api/thereIsNoCall?id=" + id,
            method: "GET",
            success: function (result) {}
          });

          swal({
            title: result.name,
            imageUrl: result.image,
            text: result.city,
            type: "error",
            showCancelButton: true,
            confirmButtonText: "Cool",
            target: document.getElementById("for-sweet"),
            animation: false,
            customClass: "animated tada",
            reverseButtons: true
          }).then(result2 => {
            audio.pause();

            if (result2.value) {
              if (!result.onlyAudio) {
                $("body").append(
                  '<div id="spinner-video-id"  class="animated zoomIn" style="justify-content: center;display: flex;position: fixed;top: 0;width: 100%; height: 100%;background-color: rgb(57, 190, 185);z-index: 999999;align-items: center;"><div class="sk-wave"><div class="sk-rect sk-rect1"></div><div class="sk-rect sk-rect2"></div><div class="sk-rect sk-rect3"></div><div class="sk-rect sk-rect4"></div><div class="sk-rect sk-rect5"></div></div></div>'
                );
                clearInterval(intervalResult);

                function join() {
                  // document.getElementById("join").disabled = true;
                  // document.getElementById("video").disabled = true;
                  // var channel_key = null;
                  // console.log("Init AgoraRTC client with App ID: " + appId.value);
                  client = AgoraRTC.createClient({
                    mode: "live"
                  });
                  client.init(
                    "f93511279880484bae87889cf18c8946",
                    function () {
                      console.log("AgoraRTC client initialized");
                      client.join(
                        null,
                        "webtest",
                        undefined,
                        function (uid) {
                          console.log(
                            "User " + uid + " join channel successfully"
                          );
                          // if (true) {
                          // camera = videoSource.value;
                          // microphone = audioSource.value;
                          localStream = AgoraRTC.createStream({
                            streamID: uid,
                            audio: true,
                            video: true,
                            screen: false,
                            extensionId: "minllpmhdgpndnkomcoccfekfegnlikg"
                          });
                          //localStream = AgoraRTC.createStream({streamID: uid, audio: false, cameraId: camera, microphoneId: microphone, video: false, screen: true, extensionId: 'minllpmhdgpndnkomcoccfekfegnlikg'});
                          // if (document.getElementById("video").checked) {
                          localStream.setVideoProfile("720p_3");
                          // }
                          // The user has granted access to the camera and mic.
                          localStream.on("accessAllowed", function () {
                            console.log("accessAllowed");
                          });
                          // The user has denied access to the camera and mic.
                          localStream.on("accessDenied", function () {
                            console.log("accessDenied");
                          });
                          localStream.init(
                            function () {
                              console.log("getUserMedia successfully");

                              $(".red-circle").click(function () {
                                localStream.close();
                                client.leave(
                                  function () {
                                    audio.pause();
                                    $("#video").empty();
                                    $("#video").append(
                                      '<img id="receiver" src="/static/images/plaholder_person.png" alt=""><p class="receiver-name">Hristian Iliev</p><p class="call-duration">Вмомента се звъни..</p>'
                                    );
                                    $("#animatedModal").attr(
                                      "style",
                                      "position: fixed; width: 100%; height: 100%; top: 0px; left: 0px; background-color: rgb(57, 190, 185); overflow-y: auto; z-index: 9999; opacity: 1; animation-duration: 0.6s;"
                                    );
                                    $("agora_local").remove();
                                    var close = document.getElementById(
                                      "close-the-modal"
                                    );
                                    audio = new Audio(
                                      "/static/sounds/endCall.mp3"
                                    );
                                    audio.play();
                                    close.click();
                                    console.log("Leavel channel successfully");
                                  },
                                  function (err) {
                                    console.log("Leave channel failed");
                                  }
                                );
                              });

                              $("#mute-mic").click(function () {
                                if (!isMicMuted) {
                                  localStream.disableAudio();
                                  isMicMuted = true;
                                } else {
                                  localStream.enableAudio();
                                  isMicMuted = false;
                                }
                              });

                              $("#stop-video").click(function () {
                                if (!isVideoDisabled) {
                                  localStream.disableVideo();
                                  isVideoDisabled = true;
                                } else {
                                  localStream.enableVideo();
                                  isVideoDisabled = false;
                                }
                              });

                              $("#spinner-video-id").remove();

                              localStream.play("agora_local");

                              client.publish(localStream, function (err) {
                                console.log(
                                  "Publish local stream error: " + err
                                );
                              });
                              client.on("stream-published", function (evt) {
                                console.log(
                                  "Publish local stream successfully"
                                );
                              });
                            },
                            function (err) {
                              console.log("getUserMedia failed", err);
                            }
                          );
                          // }
                        },
                        function (err) {
                          console.log("Join channel failed", err);
                        }
                      );
                    },
                    function (err) {
                      console.log("AgoraRTC client init failed", err);
                    }
                  );
                  // channelKey = "";
                  client.on("error", function (err) {
                    console.log("Got error msg:", err.reason);
                    if (err.reason === "DYNAMIC_KEY_TIMEOUT") {
                      client.renewChannelKey(
                        null,
                        function () {
                          console.log("Renew channel key successfully");
                        },
                        function (err) {
                          console.log("Renew channel key failed: ", err);
                        }
                      );
                    }
                  });
                  client.on("stream-added", function (evt) {
                    var stream = evt.stream;
                    console.log("New stream added: " + stream.getId());
                    console.log("Subscribe ", stream);
                    client.subscribe(stream, function (err) {
                      console.log("Subscribe stream failed", err);
                    });
                  });
                  client.on("stream-subscribed", function (evt) {
                    var stream = evt.stream;
                    console.log(
                      "Subscribe remote stream successfully: " + stream.getId()
                    );
                    if (
                      $("div#video #agora_remote" + stream.getId()).length === 0
                    ) {
                      $("#video").empty();
                      var height = 0.9 * $("#video").height();
                      $("#video").append(
                        '<div id="agora_remote' +
                        stream.getId() +
                        '" style="float:left; width:100%;height:' +
                        height +
                        'px;display:inline-block;"></div>'
                      );
                      $("#animatedModal").attr(
                        "style",
                        "position: fixed; width: 100%; height: 100%; top: 0px; left: 0px; background-color: black; overflow-y: auto; z-index: 9999; opacity: 1; animation-duration: 0.6s;"
                      );
                    }
                    var link = document.getElementById("demo01");
                    link.click();
                    stream.play("agora_remote" + stream.getId());
                  });
                  client.on("stream-removed", function (evt) {
                    var stream = evt.stream;
                    stream.stop();
                    $("#agora_remote" + stream.getId()).remove();
                    console.log("Remote stream is removed " + stream.getId());
                  });
                  client.on("peer-leave", function (evt) {
                    var stream = evt.stream;
                    if (stream) {
                      // stream.stop();
                      stream.close();
                      localStream.close();
                      $("#agora_remote" + stream.getId()).remove();
                      console.log(evt.uid + " leaved from this channel");
                      client.leave(
                        function () {
                          audio.pause();
                          $("#video").empty();
                          $("#video").append(
                            '<img id="receiver" src="/static/images/plaholder_person.png" alt=""><p class="receiver-name">Hristian Iliev</p><p class="call-duration">Вмомента се звъни..</p>'
                          );
                          $("#animatedModal").attr(
                            "style",
                            "position: fixed; width: 100%; height: 100%; top: 0px; left: 0px; background-color: rgb(57, 190, 185); overflow-y: auto; z-index: 9999; opacity: 1; animation-duration: 0.6s;"
                          );
                          $("agora_local").remove();
                          var close = document.getElementById(
                            "close-the-modal"
                          );
                          audio = new Audio("/static/sounds/endCall.mp3");
                          audio.play();
                          close.click();
                          console.log("Leavel channel successfully");
                        },
                        function (err) {
                          console.log("Leave channel failed");
                        }
                      );
                    }
                  });
                }

                join();
              } else {
                $("body").append(
                  '<div id="spinner-video-id"  class="animated zoomIn" style="justify-content: center;display: flex;position: fixed;top: 0;width: 100%; height: 100%;background-color: rgb(57, 190, 185);z-index: 999999;align-items: center;"><div class="sk-wave"><div class="sk-rect sk-rect1"></div><div class="sk-rect sk-rect2"></div><div class="sk-rect sk-rect3"></div><div class="sk-rect sk-rect4"></div><div class="sk-rect sk-rect5"></div></div></div>'
                );
                clearInterval(intervalResult);

                function join() {
                  // document.getElementById("join").disabled = true;
                  // document.getElementById("video").disabled = true;
                  // var channel_key = null;
                  // console.log("Init AgoraRTC client with App ID: " + appId.value);
                  client = AgoraRTC.createClient({
                    mode: "live"
                  });
                  client.init(
                    "f93511279880484bae87889cf18c8946",
                    function () {
                      console.log("AgoraRTC client initialized");
                      client.join(
                        null,
                        "webtest",
                        undefined,
                        function (uid) {
                          console.log(
                            "User " + uid + " join channel successfully"
                          );
                          // if (true) {
                          // camera = videoSource.value;
                          // microphone = audioSource.value;
                          localStream = AgoraRTC.createStream({
                            streamID: uid,
                            audio: true,
                            video: false,
                            screen: false,
                            extensionId: "minllpmhdgpndnkomcoccfekfegnlikg"
                          });
                          //localStream = AgoraRTC.createStream({streamID: uid, audio: false, cameraId: camera, microphoneId: microphone, video: false, screen: true, extensionId: 'minllpmhdgpndnkomcoccfekfegnlikg'});
                          // if (document.getElementById("video").checked) {
                          // localStream.setVideoProfile("720p_3");
                          // }
                          // The user has granted access to the camera and mic.
                          localStream.on("accessAllowed", function () {
                            console.log("accessAllowed");
                          });
                          // The user has denied access to the camera and mic.
                          localStream.on("accessDenied", function () {
                            console.log("accessDenied");
                          });
                          localStream.init(
                            function () {
                              console.log("getUserMedia successfully");

                              $(".red-circle").click(function () {
                                localStream.close();
                                client.leave(
                                  function () {
                                    audio.pause();
                                    $("#video").empty();
                                    $("#video").append(
                                      '<img id="receiver" src="/static/images/plaholder_person.png" alt=""><p class="receiver-name">Hristian Iliev</p><p class="call-duration">Вмомента се звъни..</p>'
                                    );
                                    $("#animatedModal").attr(
                                      "style",
                                      "position: fixed; width: 100%; height: 100%; top: 0px; left: 0px; background-color: rgb(57, 190, 185); overflow-y: auto; z-index: 9999; opacity: 1; animation-duration: 0.6s;"
                                    );
                                    $("agora_local").remove();
                                    var close = document.getElementById(
                                      "close-the-modal"
                                    );
                                    audio = new Audio(
                                      "/static/sounds/endCall.mp3"
                                    );
                                    audio.play();
                                    close.click();
                                    console.log("Leavel channel successfully");
                                  },
                                  function (err) {
                                    console.log("Leave channel failed");
                                  }
                                );
                              });

                              $("#mute-mic").click(function () {
                                if (!isMicMuted) {
                                  localStream.disableAudio();
                                  isMicMuted = true;
                                } else {
                                  localStream.enableAudio();
                                  isMicMuted = false;
                                }
                              });

                              $("#stop-video").click(function () {
                                if (!isVideoDisabled) {
                                  localStream.disableVideo();
                                  isVideoDisabled = true;
                                } else {
                                  localStream.enableVideo();
                                  isVideoDisabled = false;
                                }
                              });

                              $("#spinner-video-id").remove();

                              var timer = new Timer();
                              timer.start();
                              timer.addEventListener("secondsUpdated", function (
                                e
                              ) {
                                $(".call-duration").html(
                                  timer.getTimeValues().toString()
                                );
                              });

                              $("#stop-video").remove();
                              // $("#share-screen").remove();

                              $("#video").html('');

                              for (var i = 0; i < result.images.length; i += 1) {
                                console.log("adding new user");

                                $("#video").append('<div class="call-participators-container"><img src="' + result.images[i] + '" class="receiver-class" alt=""><p class="receiver-name">' + result.names[i] + '</p></div>')
                              }

                              $(".call-participators-container").wrapAll('<div style="display: flex; justify-content: center; flex-direction:row; align-items:center;"></div>')


                              $("#video").append('<p class="call-duration">Вмомента се звъни..</p>');

                              // $("#receiver").attr(
                              //   "src",
                              //   result.image
                              // );
                              // $(".receiver-name").text(result.name);

                              localStream.play("agora_local");

                              client.publish(localStream, function (err) {
                                console.log(
                                  "Publish local stream error: " + err
                                );
                              });
                              client.on("stream-published", function (evt) {
                                console.log(
                                  "Publish local stream successfully"
                                );
                              });
                            },
                            function (err) {
                              console.log("getUserMedia failed", err);
                            }
                          );
                          // }
                        },
                        function (err) {
                          console.log("Join channel failed", err);
                        }
                      );
                    },
                    function (err) {
                      console.log("AgoraRTC client init failed", err);
                    }
                  );
                  // channelKey = "";
                  client.on("error", function (err) {
                    console.log("Got error msg:", err.reason);
                    if (err.reason === "DYNAMIC_KEY_TIMEOUT") {
                      client.renewChannelKey(
                        null,
                        function () {
                          console.log("Renew channel key successfully");
                        },
                        function (err) {
                          console.log("Renew channel key failed: ", err);
                        }
                      );
                    }
                  });
                  client.on("stream-added", function (evt) {
                    var stream = evt.stream;
                    console.log("New stream added: " + stream.getId());
                    console.log("Subscribe ", stream);
                    client.subscribe(stream, function (err) {
                      console.log("Subscribe stream failed", err);
                    });
                  });
                  client.on("stream-subscribed", function (evt) {
                    var stream = evt.stream;
                    console.log(
                      "Subscribe remote stream successfully: " + stream.getId()
                    );
                    if (
                      $("div#video #agora_remote" + stream.getId()).length === 0
                    ) {
                      var height = 0.9 * $("#video").height();
                      $("#video").append(
                        '<div id="agora_remote' +
                        stream.getId() +
                        '" style="display:inline-block;"></div>'
                      );
                    }
                    var link = document.getElementById("demo01");
                    link.click();
                    stream.play("agora_remote" + stream.getId());
                  });
                  client.on("stream-removed", function (evt) {
                    var stream = evt.stream;
                    stream.stop();
                    $("#agora_remote" + stream.getId()).remove();
                    console.log("Remote stream is removed " + stream.getId());
                  });
                  client.on("peer-leave", function (evt) {
                    var stream = evt.stream;
                    if (stream) {
                      // stream.stop();
                      stream.close();
                      localStream.close();
                      $("#agora_remote" + stream.getId()).remove();
                      console.log(evt.uid + " leaved from this channel");
                      client.leave(
                        function () {
                          audio.pause();
                          $("#video").empty();
                          $("#video").append(
                            '<img id="receiver" src="/static/images/plaholder_person.png" alt=""><p class="receiver-name">Hristian Iliev</p><p class="call-duration">Вмомента се звъни..</p>'
                          );
                          $("#animatedModal").attr(
                            "style",
                            "position: fixed; width: 100%; height: 100%; top: 0px; left: 0px; background-color: rgb(57, 190, 185); overflow-y: auto; z-index: 9999; opacity: 1; animation-duration: 0.6s;"
                          );
                          $("agora_local").remove();
                          var close = document.getElementById(
                            "close-the-modal"
                          );
                          audio = new Audio("/static/sounds/endCall.mp3");
                          audio.play();
                          close.click();
                          console.log("Leavel channel successfully");
                        },
                        function (err) {
                          console.log("Leave channel failed");
                        }
                      );
                    }
                  });
                }

                join();
              }
            }
          });

          $(".swal2-error").remove();
          $(".swal2-confirm").attr("id", "call-confirm");
          $("#call-confirm").empty();
          $("#call-confirm").append(
            '<p style="margin-bottom: 0px;font-size: 15px;"><i class="fas fa-video"></i> Говори</p>'
          );
          $(".swal2-cancel").attr("id", "call-cancel");
          $("#call-cancel").empty();
          $("#call-cancel").append(
            '<p style="margin-bottom: 0px;font-size: 15px;"><i class="fas fa-times"></i> Откажи</p>'
          );
        }
      }
    });
  }
});