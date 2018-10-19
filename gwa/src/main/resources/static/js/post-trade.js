$(document).ready(function () {
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
