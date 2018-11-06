var addressObject = loadLocationData();

function loadLocationData(){
    var obj ={};
    $.ajax({
        url: "/gwa/js/address-data.json",
        async: false,
        success: function (data) {
            obj = data;
        }
    });
    return obj;
}
var provinceSel = document.getElementById("provinceSel"),
    districtSel = document.getElementById("districtSel"),
    wardSel = document.getElementById("wardSel"),
    streetName = document.getElementById("selectAddressStreetName"),
    apartNum = document.getElementById("selectAddressDeptNum"),
    fullAddress = document.getElementById("selectAddressFull");

var wardText, districtText, provinceText, apartNumText, streetNameText;

window.onload = function () {

    function updateAddressText() {
        apartNumText = apartNum.value.trim();
        if (apartNumText.length > 0) {
            apartNumText = apartNumText + " ";
        }
        streetNameText = streetName.value.trim();
        if (streetNameText.length > 0) {
            streetNameText = streetNameText + ", ";
        }
        wardText = wardSel.value + ", ";
        console.log(wardText.trim());
        districtText = districtSel.value + ", ";
        provinceText = provinceSel.value;
        if (wardText.trim() === ",") {
            wardText = "";
        }
        if ((districtText.trim() === ",") || (provinceText === "")) {
            districtText = "";
            wardText = "";
        }
        fullAddress.value = apartNumText + streetNameText + wardText + districtText + provinceText;
    }

    for (var province in addressObject) {
        provinceSel.options[provinceSel.options.length] = new Option(province, province);
    }
    provinceSel.onchange = function () {
        updateAddressText();
        districtSel.length = 1; // remove all options bar first
        wardSel.length = 1; // remove all options bar first
        if (this.selectedIndex < 1) return; // done
        for (var district in addressObject[this.value]) {
            districtSel.options[districtSel.options.length] = new Option(district, district);
        }

    }
    districtSel.onchange = function () {
        updateAddressText();
        wardSel.length = 1; // remove all options bar first
        if (this.selectedIndex < 1) return; // done
        var district = addressObject[provinceSel.value][this.value];
        for (var i = 0; i < district.length; i++) {
            wardSel.options[wardSel.options.length] = new Option(district[i], district[i]);
        }

    }
    wardSel.onchange = function () {
        updateAddressText();
    }
    streetName.onkeyup = function () {
        updateAddressText();
    }
    apartNum.onkeyup = function () {
        updateAddressText();
    }
}