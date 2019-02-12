var amount = 0;
var myActions = null;
var planID = 1;
// var shouldPrompt = true;

// function isValid() {
//   return amount !== 0;
// }

// function onChangeAmount(handler) {
//   handler();
// }

// function toggleValidationMessage() {
//   return myActions.enable();
// }

// function toggleButton(actions) {
//   return actions.disable();
// }

paypal.Button.render(
  {
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

    validate: function(actions) {
      myActions = actions;
      //   myActions.disable();
    },

    onClick: function() {
      //   if (shouldPrompt) {
      // vex.dialog.open({
      //   message:
      //     "Изберете сумата, с която искате да финансирате този проект:",
      //   input: [
      //     '<div class="input-group"><span class="input-group-addon"><input type="number" min="0.01" step="0.01" value="0.01" name="amount" class="form-control" placeholder="Сумата в EUR"></span"</div>'
      //   ].join(""),
      //   callback: function(data) {
      // if (!data) {
      //   return console.log("Cancelled");
      // }

      // amount = data.amount;
      // shouldPrompt = false;
      myActions.enable();
      // console.log(data.amount);
      //   }
      // });
      //   }
    },

    // payment() is called when the button is clicked
    payment: function() {
      // Set up a url on your server to create the payment
      amount = $(".priceToPay")
        .text()
        .substring(0, $(".priceToPay").text().length - 3);
      console.log(amount);
      if (amount === 4.8) {
        planID = 0;
      } else if (amount === 9.6) {
        planID = 1;
      } else {
        planID = 2;
      }

      var CREATE_URL = "/api/createPayment?amount=" + amount + "&isToPromote=" + true;

      // Make a call to your server to set up the payment
      return paypal.request.post(CREATE_URL).then(function(res) {
        return res.paymentID;
      });
    },

    // onAuthorize() is called when the buyer approves the payment
    onAuthorize: function(data, actions) {
      // Set up a url on your server to execute the payment
      var EXECUTE_URL = "/api/completePaymentToPromote";

      // Set up the data you need to pass to your server
      var arr = window.location.pathname.split("/");
      var projectId = arr[arr.length - 1];

      var data = {
        paymentID: data.paymentID,
        payerID: data.payerID,
        projectId: projectId,
        planID: planID
      };

      console.log(data);

      // Make a call to your server to execute the payment
      return paypal.request.post(EXECUTE_URL, data).then(function(res) {
        console.log("Payment Complete!");
        iziToast.success({
          title: "ОК!",
          message: "Вие успешно промотирахте този проект!",
          position: "topRight"
        });
        location.reload();
      });
    }
  },
  "#promote-button"
);
