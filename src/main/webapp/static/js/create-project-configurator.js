$(function () {
    tinymce.init({
        selector: '#textareaBasic',
        height: 500,
        theme: 'modern',
        plugins: ["advlist autolink lists link image charmap print preview hr anchor pagebreak",
            "searchreplace wordcount visualblocks visualchars code fullscreen",
            "insertdatetime media nonbreaking save table contextmenu directionality",
            "emoticons template paste textcolor colorpicker textpattern codesample imagetools "
        ],
        toolbar: 'insertfile undo redo | styleselect | bold italic | fontsizeselect fontfamilyselect alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link | imageupload fileupload print preview media | forecolor backcolor emoticons',
        // plugins: 'image code',
        // without images_upload_url set, Upload tab won't show up
        // images_upload_url: '/api/uploadImage',
        // // automatic_uploads: true,

        // // we override default upload handler to simulate successful upload
        // images_upload_handler: function (blobInfo, success, failure) {
        //     var data = JSON.stringify({
        //         blob: blobInfo.blob(),
        //         filename: blobInfo.filename()
        //     });
        //     console.log(data);
        //     $.ajax({
        //         url: '/api/uploadImage',
        //         method: "POST",
        //         data: data,
        //         contentType: "application/json",
        //         success: function (result) {}
        //     });

        //     // setTimeout(function () {
        //     //     // no matter what you upload, we will turn it into TinyMCE logo :)
        //     //     success('http://moxiecode.cachefly.net/tinymce/v9/images/logo.png');
        //     // }, 2000);
        // }
        setup: function (editor) {

            // create input and insert in the DOM
            var inp = $('<input id="tinymce-uploader" type="file" name="pic" accept="image/*" style="display:none">');
            $(editor.getElement()).parent().append(inp);

            // add the image upload button to the editor toolbar
            editor.addButton('imageupload', {
                text: 'Add image',
                icon: 'image',
                onclick: function (e) { // when toolbar button is clicked, open file select modal
                    inp.trigger('click');
                }
            });

            // when a file is selected, upload it to the server
            inp.on("change", function (e) {
                uploadFile($(this), editor);
            });


            function uploadFile(inp, editor) {
                var input = inp.get(0);
                var data = new FormData();
                data.append('files', input.files[0]);

                $.ajax({
                    url: '/api/uploadImage',
                    type: 'POST',
                    data: data,
                    enctype: 'multipart/form-data',
                    dataType: 'json',
                    processData: false, // Don't process the files
                    contentType: false, // Set content type to false as jQuery will tell the server its a query string request
                    success: function (data, textStatus, jqXHR) {
                        editor.insertContent('<img class="content-img" src="' + data.location + '" data-mce-src="' + data.location + '" />');
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        if (jqXHR.responseText) {
                            errors = JSON.parse(jqXHR.responseText).errors
                            alert('Error uploading image: ' + errors.join(", ") + '. Make sure the file is an image and has extension jpg/jpeg/png.');
                        }
                    }
                });
            }
        }
    });
    //   $("#textareaBasic")
    //     .froalaEditor({
    //       // fileUploadURL: "/api/uploadProjectFile",
    //       // fileUploadMethod: "POST",
    //       fileUpload: false,
    //       // imageUploadURL: '/api/uploadProjectImage',
    //       // imageUploadMethod: 'POST'
    //       imageUpload: false
    //     })
    //     .on("froalaEditor.file.unlink", function (e, editor, file) {
    //       $.ajax({
    //           // Request method.
    //           method: "POST",
    //           // Request URL.
    //           url: "/api/deleteProjectFile",
    //           // Request params.
    //           data: {
    //             src: file.getAttribute("href")
    //           }
    //         })
    //         .done(function (data) {
    //           console.log("File was deleted");
    //         })
    //         .fail(function (err) {
    //           console.log("File delete problem: " + JSON.stringify(err));
    //         });
    //     })
    //     .on("froalaEditor.image.removed", function (e, editor, $img) {
    //       $.ajax({
    //           // Request method.
    //           method: "POST",

    //           // Request URL.
    //           url: "/api/deleteProjectImage",

    //           // Request params.
    //           data: {
    //             src: $img.attr("src")
    //           }
    //         })
    //         .done(function (data) {
    //           console.log("Image was deleted");
    //         })
    //         .fail(function (err) {
    //           console.log("Image delete problem: " + JSON.stringify(err));
    //         });
    //     });
});