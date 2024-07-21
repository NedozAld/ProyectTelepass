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

document.getElementById('login-form').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevenir el comportamiento por defecto del formulario
    authenticateUser();
});

async function authenticateUser() {
    const correo = document.getElementById('login-username').value;
    const contrasena = document.getElementById('login-password').value;

    const response = await fetch('/api/auth/authenticate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ correo, contrasena })
    });

    if (response.ok) {
        const data = await response.json();
        const token = data.token;
        const usuarioId = data.id;
        localStorage.setItem('token', token); // Guardar el token en localStorage
        localStorage.setItem('usuarioId', usuarioId);

        // Obtener el rol del usuario
        const rolResponse = await fetch(`/api/usuario/${usuarioId}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (rolResponse.ok) {
            const rolData = await rolResponse.json();
            const rol = rolData.role; // Suponiendo que el servidor devuelve el rol como 'role'

            // Redirigir según el rol
            if (rol === 'USER') {
                window.location.href = '/usuario';
            } else if (rol === 'ADMIN') {
                window.location.href = '/admin';
            } else {
                alert('Rol desconocido');
            }
        } else {
            alert('No se pudo obtener el rol del usuario');
        }
    } else {
        alert('Fallo al autenticar usuario');
    }
}