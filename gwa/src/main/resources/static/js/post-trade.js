$(document).ready(function () {
    authentication();
    $('[data-toggle="tooltip"]').tooltip();
});

function autoGetYourLocation() {
    waitingDialog.show('Getting your location...', {dialogSize: '', progressType: 'info'});
    setTimeout(function () {waitingDialog.hide();}, 2000);
    // waitingDialog.show('Dialog with callback on hidden',{onHide: function () {alert('Callback!');}});
}
function submitAddressSelect() {
    setTimeout(function () {$("#addressModal").modal('hide');}, 300);
    var addressText = document.getElementById("traderAddress"),
        addressFromModelText = document.getElementById("selectAddressFull");
    addressText.value = addressFromModelText.value;

}
function authentication() {


}
$("#tradepostForm").validate({
    rules: {
        tradeTitle: {
            required : true,
            minlength : 3
        },
        tradePrice:{
            required : true,
            number : true
        },
        tradeQuantity:{
            required : true,
            digits: true,
            min : 1,
            max: 1000
        },
        tradeBrand:{
            required : true,
            minlength : 3
        }
    },
    messages: {
        tradeTitle: {
            required : "You missing your title here.",
            minlength : "Your title too short, request at least 3 characters."
        },
        tradePrice:{
            required : "You missing your price here.",
            number : "Your price can't be string, price must be number."
        },
        tradeQuantity:{
            required : "You missing your quantity here.",
            digits: "Quantity must be positive integer.",
            min : "Quantity must be greater than zero.",
            max: "Quantity must be lower or equal 1000."
        },
        tradeBrand:{
            required : "You missing your brand here.",
            minlength : "Your brand too short, request at least 3 characters."
        }
    },
    submitHandler: function(form) {
        var url = $(form).attr("action");
        var data = new FormData($(form));
        console.log("submited: " + url);
        console.log("Form Data: " + data);
        // $.ajax({
        //     url: "processo.php",
        //     type: "POST",
        //     data: new FormData($(form)),
        //     cache: false,
        //     processData: false,
        //     success: function(data) {
        //         $('#loading').hide();
        //         $("#message").html(data);
        //     }
        // });
        // return false;
    }
});
$.validator.addMethod('positiveNumber',
    function (value) {
        return Number(value) > 0;
    }, 'Enter a positive number.');
// $("#tradepostForm").submit( function (e) {
//     var form = $(this);
//     var url = form.attr('action');
//     console.log(url);
//     e.preventDefault();
// });