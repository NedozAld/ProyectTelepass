function togglePasswordVisibility(inputId, icon) {
    const input = document.getElementById(inputId);
    if (input.type === "password") {
        input.type = "text";
        icon.classList.remove("uil-eye-slash");
        icon.classList.add("uil-eye");
    } else {
        input.type = "password";
        icon.classList.remove("uil-eye");
        icon.classList.add("uil-eye-slash");
    }
}

document.getElementById('changePasswordForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent form submission

    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const urlParams = new URLSearchParams(window.location.search);
    const correo = urlParams.get('email');

    if (newPassword !== confirmPassword) {
        alert('Las contraseñas no coinciden. Por favor, inténtalo de nuevo.');
        return;
    }

    fetch('/api/reset-password/cambiar-contrasena', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            'correo': correo,
            'nuevaContrasena': newPassword
        })
    }).then(response => {
        if (response.ok) {
            alert('Contraseña cambiada exitosamente.');
            window.location.href = "login";
        } else {
            alert('Error al cambiar la contraseña.');
        }
    }).catch(err => console.error('Error:', err));
});
