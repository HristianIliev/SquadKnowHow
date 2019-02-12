var amount = 0;
var myActions = null;
var shouldPrompt = true;
var indexOfUndescoreForUserId = $("body")
    .attr("id")
    .indexOf("_");
var id = $("body")
    .attr("id")
    .substring(indexOfUndescoreForUserId + 1);

function isValid() {
    return amount !== 0;
}

function onChangeAmount(handler) {
    handler();
}

function toggleValidationMessage() {
    return myActions.enable();
}

function toggleButton(actions) {
    return actions.disable();
}

paypal.Button.render({
        env: "sandbox", // sandbox | production

        // Show the buyer a 'Pay Now' button in the checkout flow
        commit: true,

        style: {
            label: "paypal",
            size: "medium", // small | medium | large | responsive
            shape: "rect", // pill | rect
            color: "blue", // gold | blue | silver | black
            tagline: false
        },

        validate: function (actions) {
            myActions = actions;
            myActions.disable();
        },

        onClick: function () {
            if (shouldPrompt) {
                vex.dialog.open({
                    message: "Изберете сумата, която искате да вкарате в платформата:",
                    input: [
                        '<div class="input-group"><span class="input-group-addon"><input type="number" min="0.01" step="0.01" value="0.01" name="amount" class="form-control" placeholder="Сумата в USD"></span"</div>'
                    ].join(""),
                    callback: function (data) {
                        if (!data) {
                            return console.log("Cancelled");
                        }

                        amount = data.amount;
                        shouldPrompt = false;
                        myActions.enable();
                        console.log(data.amount);
                    }
                });
            }
        },

        // payment() is called when the button is clicked
        payment: function () {
            // Set up a url on your server to create the payment
            var CREATE_URL = "/api/createPayment";

            var data = {
                amount: amount,
                isToPromote: false
            };

            // Make a call to your server to set up the payment
            return paypal.request.post(CREATE_URL, data).then(function (res) {
                return res.paymentID;
            });
        },

        // onAuthorize() is called when the buyer approves the payment
        onAuthorize: function (data, actions) {
            // Set up a url on your server to execute the payment
            var EXECUTE_URL = "/api/addFunds";

            var data = {
                paymentID: data.paymentID,
                payerID: data.payerID,
                id: id
            };

            console.log(data);

            // Make a call to your server to execute the payment
            return paypal.request.post(EXECUTE_URL, data).then(function (res) {
                console.log("Payment Complete!");
                iziToast.success({
                    title: "ОК!",
                    message: "Вие успешно финансирахте този проект!",
                    position: "topRight"
                });
                location.reload();
            });
        }
    },
    "#fund-account"
);