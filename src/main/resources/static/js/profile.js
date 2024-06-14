$(document).ready(function () {
    $('.editarPerfil').on('click', function (event) {
        event.preventDefault();
        const userId = $(this).data('id');

        $.get('/profile/edit', function (data) {
            $('#editProfileContainer').html(data).show(); // Mostrar el contenedor de edición
            $('#editProfileForm').on('submit', function (e) {
                e.preventDefault();
                $.ajax({
                    type: 'POST',
                    url: $('#editProfileForm').attr('action'),
                    data: $('#editProfileForm').serialize(),
                    success: function (response) {
                        Swal.fire({
                            title: 'Éxito',
                            text: 'Perfil actualizado con éxito.',
                            icon: 'success',
                            confirmButtonText: 'OK'
                        }).then((result) => {
                            if (result.isConfirmed) {
                                window.location.href = '/profile'; // Recargar la página para reflejar los cambios
                            }
                        });
                    },
                    error: function (error) {
                        Swal.fire({
                            title: 'Error',
                            text: 'Error al actualizar el perfil: ' + error.responseText,
                            icon: 'error',
                            confirmButtonText: 'OK'
                        });
                    }
                });
            });
        });
    });
});
