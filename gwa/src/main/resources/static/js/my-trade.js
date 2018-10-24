$(document).ready(function () {

});

<!-- Tooltip -->
$('[data-title="tooltip"]').tooltip();

<!-- Tab panel  -->
$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
   $(e.target).parent().addClass("active");// newly activated tab
    $(e.relatedTarget).parent().removeClass("active"); // previous active tab
});