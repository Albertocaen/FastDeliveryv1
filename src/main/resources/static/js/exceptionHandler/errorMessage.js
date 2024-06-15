$(document).ready(function() {
    let errorMessage = /*[[${errorMessage}]]*/ '';
    if (errorMessage) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: errorMessage,
        });
    }
});
