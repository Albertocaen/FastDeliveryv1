$(document).ready(function() {
    $('form').on('submit', function(e) {
        var fileInput = $('#imagen')[0];
        if (fileInput.files.length > 0) {
            var fileName = fileInput.files[0].name.toLowerCase();
            if (!fileName.endsWith('.png') && !fileName.endsWith('.jpg')) {
                e.preventDefault();
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Formato de imagen incorrecto. Solo se permiten archivos PNG y JPG.',
                });
            }
        }
    });

    $('#precio').on('input', function() {
        this.value = this.value.replace(/[^0-9.]/g, '');
    });

    $('#precio').on('keypress', function(e) {
        if (e.which < 48 || e.which > 57) {
            if (e.which !== 46 || $(this).val().indexOf('.') !== -1) {
                e.preventDefault();
            }
        }
    });
});
