$(document).ready(function () {
    authentication();
    $('[data-toggle="tooltip"]').tooltip();
    $('#imageUploadedList').onchange = function () {
        console.log(this.val());
    }
});

function autoGetYourLocation() {
    waitingDialog.show('Getting your location...', {dialogSize: '', progressType: 'info'});
    setTimeout(function () {
        waitingDialog.hide();
    }, 2000);
    // waitingDialog.show('Dialog with callback on hidden',{onHide: function () {alert('Callback!');}});
}


function authentication() {


}


$.validator.setDefaults({
    highlight: function (element) {
        $(element).closest('.form-control').addClass('is-invalid');
        $(element).parent().parent('.input-group').closest('.input-group').addClass('was-validated');
    },
    unhighlight: function (element) {
        $(element).closest('.form-control').removeClass('is-invalid').addClass('is-valid');
    },
    errorElement: 'span',
    errorClass: 'invalid-feedback',
    errorPlacement: function (error, element) {
        if (element.parent().parent('.input-group').length) {
            error.insertAfter(element.parent());
        } else {
            error.insertAfter(element);
        }
    }
});

$.validator.addMethod("phoneVN", function (phone_number) {
    return phone_number.match('(09|03|05|07|08)+([0-9]{8})\\b');
}, "Please specify a valid phone number");

$("#addressSelectForm").validate({
    rules: {
        provinceSel: {
            required: true
        },
        districtSel: {
            required: true
        },
        wardSel: {
            required: true
        },
        selectAddressStreetName: {
            required: true
        }
    },
    messages: {
        provinceSel: {
            required: "Please select your province"
        },
        districtSel: {
            required: "Please select your district"
        },
        wardSel: {
            required: "Please select your ward"
        },
        selectAddressStreetName: {
            required: "Please provide your street name"
        }
    },
    submitHandler: function (form) {
        setTimeout(function () {
            $("#addressModal").modal('hide');
        }, 300);
        var addressText = document.getElementById("traderAddress"),
            addressFromModelText = document.getElementById("selectAddressFull");
        addressText.value = addressFromModelText.value;
    }

});
$("#tradepostForm").validate({
    ignore: [],
    rules: {
        tradeTitle: {
            required: true,
            minlength: 3
        },
        tradePrice: {
            required: true,
            number: true
        },
        tradeQuantity: {
            required: true,
            digits: true,
            min: 1,
            max: 1000
        },
        tradeBrand: {
            required: true,
            minlength: 3
        },
        tradeDesc: {
            required: true,
            minlength: 10
        },
        traderName: {
            required: true,
            minlength: 2
        },
        traderEmail: {
            required: true,
            email: true
        },
        traderPhone: {
            required: true,
            phoneVN: true

        },
        traderAddress: {
            required: true
        },
        tradeType123: {
            required: true

        },
        tradeCondition: {
            required: true
        },
        sendCheck: {
            required: true
        },
        imageUploadedList: {
            required: true
        }
    },
    messages: {
        tradeTitle: {
            required: "You missing your title here.",
            minlength: "Your title too short, request at least 3 characters."
        },
        tradePrice: {
            required: "You missing your price here.",
            number: "Your price can't be string, price must be number."
        },
        tradeQuantity: {
            required: "You missing your quantity here.",
            digits: "Quantity must be positive integer.",
            min: "Quantity must be greater than zero.",
            max: "Quantity must be lower or equal 1000."
        },
        tradeBrand: {
            required: "You missing your brand here.",
            minlength: "Your brand too short, request at least 3 characters."
        },
        tradeDesc: {
            required: "You missing your description here",
            minlength: "Description too short, you need write something to let they know about your product."
        },
        traderName: {
            required: "Let they know your name to make a trade.",
            minlength: "Your name too short. Please use full name."
        },
        traderEmail: {
            required: "You need provide your email.",
            email: "Your email is not correct."
        },
        traderPhone: {
            required: "You need provide your phone.",
            phoneVN: "Your phone is not correct."

        },
        traderAddress: {
            required: "Please select or use auto get your address."
        }
        ,
        tradeType: {
            required: "You must choose type of your trade."
        },
        tradeCondition: {
            required: "You must choose your product condition."
        },
        sendCheck: {
            required: "You must accept our Terms of Use and Privacy Policy to post your trade"
        },
        imageUploadedList: {
            required: "You must upload at least 1 images for your trade."
        }
    },
    submitHandler: function (form) {
        var formObj = {};
        new FormData(form).forEach(function(value, key){
            formObj[key] = value;
            if (key === "imageUploadedList"){
                formObj[key] = JSON.parse(value);
            }
        });
        var url = $(form).attr("action");
        var formDataJson = JSON.stringify(formObj);
        console.log(formDataJson);
        $.ajax({
            url: url,
            type: "POST",
            data: formDataJson,
            contentType : "application/json",
            success: function(result, txtStatus, xhr) {
                console.log(result + " - " + txtStatus);
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
    }
});
var countFileSuccess = 0;
var countFileSelect = 0;
var FiletoSubmit =[];
$.fileup({
    url: "/uploadFile",
    inputID: 'upload-2',
    queueID: 'upload-2-queue',
    dropzoneID: '',
    files: [],
    fieldName: 'file',
    extraFields: {},
    lang: 'en',
    sizeLimit: 2500000,
    filesLimit: 5,
    method: 'post',
    timeout: null,
    autostart: false,
    templateFile: '<div id="fileup-[INPUT_ID]-[FILE_NUM]" class="fileup-file [TYPE]">\n' +
        '    <div class="fileup-preview">\n' +
        '        <img src="[PREVIEW_SRC]" alt="[NAME]"/>\n' +
        '    </div>\n' +
        '    <div class="fileup-container">\n' +
        '        <div class="fileup-description">\n' +
        '            <span class="fileup-name">[NAME]</span> (<span class="fileup-size">[SIZE_HUMAN]</span>)\n' +
        '        </div>\n' +
        '        <div class="fileup-controls">\n' +
        '            <span class="fileup-remove" id="remove-[FILE_NUM]" onclick="$.fileup(\'[INPUT_ID]\', \'remove\', \'[FILE_NUM]\');" title="[REMOVE]"></span>\n' +
        '            <span class="fileup-upload" onclick="$.fileup(\'[INPUT_ID]\', \'upload\', \'[FILE_NUM]\');">[UPLOAD]</span>\n' +
        '            <span class="fileup-abort" onclick="$.fileup(\'[INPUT_ID]\', \'abort, \'[FILE_NUM]\');" style="display:none">[ABORT]</span>\n' +
        '        </div>\n' +
        '        <div class="fileup-result"></div>\n' +
        '        <div class="fileup-progress">\n' +
        '            <div class="fileup-progress-bar"></div>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="fileup-clear"></div>\n' +
        '</div>',
    onSelect: function(file) {
        countFileSelect++;
        $('#multiple .control-button').show();
        console.log(countFileSelect);
        if (countFileSuccess > 0){
            $('#multiple .removeall').hide();
        }
    },
    onRemove: function(file, total) {
        countFileSelect--;
        if (file === '*' || total === 1) {
            $('#multiple .control-button').hide();
        }
        if (countFileSelect === countFileSuccess){
            $('#multiple .control-button').hide();
        }
    },
    onSuccess: function(response, file_number, file) {
        countFileSuccess++;
        $.growl.notice({ title: "Upload success!", message: file.name });
        $("#remove-" +file_number).hide();
        if (countFileSelect === countFileSuccess){
            $('#multiple .control-button').hide();
        }
        if (countFileSuccess > 0){
            $('#multiple .removeall').hide();
        }
        FiletoSubmit.push(JSON.parse(response)['fileDownloadUri']);

        $('#imageUploadedList').val(JSON.stringify(FiletoSubmit));
        console.log($('#imageUploadedList').val);
    },
    onError: function(event, file, file_number) {
        var textErr= "";
        if (event === "files_limit"){
            textErr = "The number of selected file exceeds the limit(5)";
        }
        if(event  ==="file_type"){
            textErr = "File " + file.name +" is not image file types";
        }
        $.growl.error({ title: "Upload error: " , message: textErr});
    }

});

