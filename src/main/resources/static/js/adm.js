document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('token');
    if (!token) {
        alert('No hay token de autenticación. Por favor, inicia sesión.');
        window.location.href = '/login.html'; // Redirige a la página de inicio de sesión
        return;
    }

    // Manejadores de eventos para los enlaces del menú
    const navLinks = document.querySelectorAll('.nav-bar a');
    navLinks.forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault(); // Evita el comportamiento predeterminado del enlace
            const sectionId = link.getAttribute('data-section');
            const section = document.getElementById(sectionId);

            // Ocultar todas las secciones menos la seleccionada
            const sections = document.querySelectorAll('.content-section');
            sections.forEach(sec => {
                if (sec !== section) {
                    sec.style.display = 'none';
                }
            });

            // Mostrar la sección correspondiente
            section.style.display = 'block';

            // Cargar datos si es necesario
            if (sectionId === 'usuarios') {
                fetchUsuarios();
            } else if (sectionId === 'peajes') {
                fetchPeajes();
            }else if (sectionId === 'vehiculos') {
                fetchVehiculoCategorias();
            }else if (sectionId === 'tarifas') {
                fetchTarifas();
            }else if (sectionId === 'transacciones') {
                fetchTransacciones();
            }else if (sectionId === 'pago-peaje') {
                cargarZonas();
            }else if (sectionId === 'tipoPago') {
                fetchTiposPago();
            }else if (sectionId === 'telepass') {
                fetchTelepasses();
            }

        });
    });



    // Función para cargar las zonas de peaje dinámicamente
    async function cargarZonas() {
        try {
            const response = await fetch('/api/zonas', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error al obtener las zonas de peaje');
            }

            const data = await response.json();
            const zonaPeajeSelect = document.getElementById('zonaPeaje');

            // Limpiar opciones existentes
            zonaPeajeSelect.innerHTML = '<option value="" disabled selected>Seleccionar Zona de Peaje</option>';

            // Agregar opciones de zonas
            data.forEach(zona => {
                const option = document.createElement('option');
                option.value = zona.id;
                option.textContent = zona.nombre;
                zonaPeajeSelect.appendChild(option);
            });

            // Escuchar cambios en la selección de zona de peaje
            zonaPeajeSelect.addEventListener('change', async function() {
                const placa = document.getElementById('placaVehiculo').value;
                const zonaId = zonaPeajeSelect.value;

                // Comprobar que placa y zonaId no sean nulos o vacíos antes de obtener la tarifa
                if (placa && zonaId) {
                    try {
                        const response = await fetch(`/api/pagos/tarifaEfectivo?placa=${placa}&zonaId=${zonaId}`, {
                            headers: {
                                'Authorization': `Bearer ${token}`,
                                'Content-Type': 'application/json'
                            }
                        });

                        if (!response.ok) {
                            throw new Error('Error al obtener la tarifa efectiva');
                        }

                        const tarifa = await response.json();
                        const tarifaEfectivaInput = document.getElementById('tarifaEfectiva');
                        tarifaEfectivaInput.value = `${tarifa} USD`;
                        document.getElementById('resultadoTarifa').style.display = 'block';
                        document.getElementById('btnPagar').style.display = 'block';

                    } catch (error) {
                        console.error('Error al obtener la tarifa efectiva:', error);
                        alert('Hubo un problema al obtener la tarifa efectiva.');
                    }
                } else {
                    // Si no se selecciona alguna de las dos (placa o zona), ocultar el resultado
                    document.getElementById('resultadoTarifa').style.display = 'none';
                    document.getElementById('btnPagar').style.display = 'none';
                }
            });

        } catch (error) {
            console.error('Error al cargar las zonas de peaje:', error);
            alert('Hubo un problema al cargar las zonas de peaje.');
        }
    }

    // Función para realizar el pago efectivo
    async function realizarPagoEfectivo(placa, zonaId) {
        try {
            const response = await fetch(`/api/pagos/realizarPagoEfectivo?placa=${placa}&zonaId=${zonaId}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error al realizar el pago efectivo');
            }

            const pagoDTO = await response.json();
            console.log('Pago realizado con éxito:', pagoDTO);
            // Mostrar mensaje temporal de éxito con Sweet Alert
    Swal.fire({
        icon: 'success',
        title: 'Pago realizado con éxito',
        showConfirmButton: false, // Ocultar el botón de confirmación
        timer: 3000 // Tiempo en milisegundos antes de que se cierre automáticamente
    });



        } catch (error) {
            console.error('Error al realizar el pago efectivo:', error);
            alert('Hubo un problema al realizar el pago efectivo.');
        }
    }

    // Event listener para el botón Pagar
    document.getElementById('btnPagar').addEventListener('click', function() {
        const placa = document.getElementById('placaVehiculo').value;
        const zonaId = document.getElementById('zonaPeaje').value;

        if (placa && zonaId) {
            realizarPagoEfectivo(placa, zonaId);
            // Limpiar campos y ocultar sección de resultado
            document.getElementById('placaVehiculo').value = '';
            document.getElementById('zonaPeaje').value = '';
            document.getElementById('tarifaEfectiva').value = '';
            document.getElementById('resultadoTarifa').style.display = 'none';
            document.getElementById('btnPagar').style.display = 'none';
        } else {
            alert('Por favor seleccione una placa y una zona de peaje válidas.');
        }
    });



    // Mostrar formulario de creación de peaje al hacer clic en "Crear Nuevo Peaje"
    document.getElementById('btnMostrarFormCrearPeaje').addEventListener('click', function(event) {
        event.preventDefault();
        document.getElementById('formCrearPeaje').style.display = 'block';
        document.getElementById('formActualizarPeaje').style.display = 'none';
    });

    // Mostrar formulario de creación de categoría de vehículo al hacer clic en "Crear Nueva Categoría"
    document.getElementById('btnMostrarFormCrearVehiculoCategoria').addEventListener('click', function(event) {
        event.preventDefault();
        document.getElementById('formCrearVehiculoCategoria').style.display = 'block';
        document.getElementById('formActualizarVehiculoCategoria').style.display = 'none';
    });


    // Formulario para crear nueva zona de peaje
    document.getElementById('crearPeajeForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const nombrePeaje = document.getElementById('nombrePeaje').value;
        crearZona({ nombre: nombrePeaje });
        document.getElementById('formCrearPeaje').style.display = 'none';
    });
    // Formulario para crear nueva categoría de vehículo
    document.getElementById('crearVehiculoCategoriaForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const nombreCategoria = document.getElementById('nombreVehiculoCategoria').value;
        crearVehiculoCategoria({ tipo: nombreCategoria });
        document.getElementById('formCrearVehiculoCategoria').style.display = 'none';
         document.getElementById('nombreVehiculoCategoria').value = '';
    });

    // Formulario para actualizar zona de peaje
    document.getElementById('actualizarPeajeForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const peajeId = document.getElementById('peajeIdActualizar').value;
        const nombrePeaje = document.getElementById('nombrePeajeActualizar').value;
        actualizarZona(peajeId, { nombre: nombrePeaje });
        document.getElementById('formActualizarPeaje').style.display = 'none';
    });

    // Formulario para actualizar categoría de vehículo
    document.getElementById('actualizarVehiculoCategoriaForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const categoriaId = document.getElementById('vehiculoCategoriaIdActualizar').value;
        const nombreCategoria = document.getElementById('nombreVehiculoCategoriaActualizar').value;
        actualizarVehiculoCategoria(categoriaId, { tipo: nombreCategoria });
        document.getElementById('formActualizarVehiculoCategoria').style.display = 'none';
    });

    // Agregar manejo de eventos para los botones de acciones en la tabla de peajes
    document.getElementById('peajesTable').addEventListener('click', function(event) {
        const target = event.target;

        // Si se hace clic en el botón "Eliminar" de una fila
        if (target.matches('.btn-eliminar')) {
            const zonaId = target.dataset.peajeId;
            eliminarZona(zonaId);
        }

        // Si se hace clic en el botón "Editar" de una fila
        if (target.matches('.btn-editar')) {
            const zonaId = target.dataset.peajeId;
            const nombreZona = target.closest('tr').querySelector('td:nth-child(2)').textContent;
            document.getElementById('peajeIdActualizar').value = zonaId;
            document.getElementById('nombrePeajeActualizar').value = nombreZona;
            document.getElementById('formCrearPeaje').style.display = 'none';
            document.getElementById('formActualizarPeaje').style.display = 'block';
        }
    });

    // Agregar manejo de eventos para los botones de acciones en la tabla de categorías de vehículos
    document.getElementById('vehiculoCategoriasTable').addEventListener('click', function(event) {
        const target = event.target;

        // Si se hace clic en el botón "Eliminar" de una fila
        if (target.matches('.btn-eliminar')) {
            const categoriaId = target.dataset.vehiculoCategoriaId;
            eliminarVehiculoCategoria(categoriaId);
        }

        // Si se hace clic en el botón "Editar" de una fila
        if (target.matches('.btn-editar')) {
            const categoriaId = target.dataset.vehiculoCategoriaId;
            const nombreCategoria = target.closest('tr').querySelector('td:nth-child(2)').textContent;
            document.getElementById('vehiculoCategoriaIdActualizar').value = categoriaId;
            document.getElementById('nombreVehiculoCategoriaActualizar').value = nombreCategoria;
            document.getElementById('formCrearVehiculoCategoria').style.display = 'none';
            document.getElementById('formActualizarVehiculoCategoria').style.display = 'block';
        }
    });

    async function fetchUsuarios() {
        try {
            const response = await fetch('/api/usuario', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error en la solicitud');
            }

            const data = await response.json();
            console.log('Usuarios recibidos:', data);
            const usuariosTableBody = document.getElementById('usuariosTableBody');
            const emptyMessage = document.getElementById('emptyMessage');

            usuariosTableBody.innerHTML = ''; // Limpiar contenido previo

            if (data.length === 0) {
                emptyMessage.style.display = 'block';
                return;
            }

            data.forEach(usuario => {
                const row = usuariosTableBody.insertRow();
                row.innerHTML = `
                    <td>${usuario.id}</td>
                    <td>${usuario.nombre}</td>
                    <td>${usuario.apellido}</td>
                    <td>${usuario.cedula}</td>
                    <td>${usuario.correo}</td>
                    <td>${usuario.fechaNacimiento}</td>
                    <td>${usuario.genero}</td>
                    <td>${usuario.role}</td>
                    <td>
                        <!-- Botones de acciones (editar, eliminar, etc.) -->
                    </td>
                `;
            });

        } catch (error) {
            console.error('Error al obtener usuarios:', error);
            alert('Hubo un problema al cargar los usuarios.');
        }
    }

    async function fetchPeajes() {
        try {
            const response = await fetch('/api/zonas', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error en la solicitud');
            }

            const data = await response.json();
            console.log('Peajes recibidos:', data);
            const peajesTableBody = document.getElementById('peajesTableBody');
            const emptyMessage = document.getElementById('emptyMessagePeajes');

            peajesTableBody.innerHTML = ''; // Limpiar contenido previo

            if (data.length === 0) {
                emptyMessage.style.display = 'block';
                return;
            }

            data.forEach(peaje => {
                const row = peajesTableBody.insertRow();
                row.innerHTML = `
                    <td>${peaje.id}</td>
                    <td>${peaje.nombre}</td>
                    <td>
                        <button class="btn-editar" data-peaje-id="${peaje.id}">Editar</button>
                        <button class="btn-eliminar" data-peaje-id="${peaje.id}">Eliminar</button>
                    </td>
                `;
            });

        } catch (error) {
            console.error('Error al obtener peajes:', error);
            alert('Hubo un problema al cargar los peajes.');
        }
    }

    async function fetchVehiculoCategorias() {
        try {
            const response = await fetch('/api/vehiculo-categorias', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error en la solicitud');
            }

            const data = await response.json();
            console.log('Categorías de vehículos recibidas:', data);
            const vehiculoCategoriasTableBody = document.getElementById('vehiculoCategoriasTableBody');
            const emptyMessage = document.getElementById('emptyMessageVehiculoCategorias');

            vehiculoCategoriasTableBody.innerHTML = ''; // Limpiar contenido previo

            if (data.length === 0) {
                emptyMessage.style.display = 'block';
                return;
            }

            data.forEach(categoria => {
                const row = vehiculoCategoriasTableBody.insertRow();
                row.innerHTML = `
                    <td>${categoria.id}</td>
                    <td>${categoria.tipo}</td>
                    <td>
                        <button class="btn-editar" data-vehiculo-categoria-id="${categoria.id}">Editar</button>
                        <button class="btn-eliminar" data-vehiculo-categoria-id="${categoria.id}">Eliminar</button>
                    </td>
                `;
            });

        } catch (error) {
            console.error('Error al obtener categorías de vehículos:', error);
            alert('Hubo un problema al cargar las categorías de vehículos.');
        }
    }

    async function crearZona(zonaData) {
        try {
            const response = await fetch('/api/zonas/crear', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(zonaData)
            });

            if (!response.ok) {
                throw new Error('Error al crear la zona');
            }

            const nuevaZona = await response.json();
            console.log('Zona creada:', nuevaZona);
            fetchPeajes(); // Actualizar la tabla de peajes después de crear
        } catch (error) {
            console.error('Error al crear la zona:', error);
            alert('Hubo un problema al crear la zona.');
        }
    }



    async function actualizarZona(zonaId, zonaData) {
        try {
            const response = await fetch(`/api/zonas/actualizar/${zonaId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(zonaData)
            });

            if (!response.ok) {
                throw new Error('Error al actualizar la zona');
            }

            const zonaActualizada = await response.json();
            console.log('Zona actualizada:', zonaActualizada);
            fetchPeajes(); // Actualizar la tabla de peajes después de actualizar
        } catch (error) {
            console.error('Error al actualizar la zona:', error);
            alert('Hubo un problema al actualizar la zona.');
        }
    }

    async function eliminarZona(zonaId) {
        try {
            const response = await fetch(`/api/zonas/eliminar/${zonaId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error al eliminar la zona');
            }

            console.log('Zona eliminada con éxito');
            fetchPeajes(); // Actualizar la tabla de peajes después de eliminar
        } catch (error) {
            console.error('Error al eliminar la zona:', error);
            alert('Hubo un problema al eliminar la zona.');
        }
    }
async function crearVehiculoCategoria(categoriaData) {
        try {
            const response = await fetch('/api/vehiculo-categorias/crear', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(categoriaData)
            });

            if (!response.ok) {
                throw new Error('Error al crear la categoría de vehículo');
            }

            const nuevaCategoria = await response.json();
            console.log('Categoría de vehículo creada:', nuevaCategoria);
            fetchVehiculoCategorias(); // Actualizar la tabla de categorías de vehículos después de crear
        } catch (error) {
            console.error('Error al crear la categoría de vehículo:', error);
            alert('Hubo un problema al crear la categoría de vehículo.');
        }
    }


    async function actualizarVehiculoCategoria(categoriaId, categoriaData) {
        try {
        console.log(categoriaId);
        console.log(categoriaData);
            const response = await fetch(`/api/vehiculo-categorias/actualizar/${categoriaId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(categoriaData)
            });

            if (!response.ok) {
                throw new Error('Error al actualizar la categoría de vehículo');
            }

            const categoriaActualizada = await response.json();
            console.log('Categoría de vehículo actualizada:', categoriaActualizada);
            fetchVehiculoCategorias(); // Actualizar la tabla de categorías de vehículos después de actualizar
        } catch (error) {
            console.error('Error al actualizar la categoría de vehículo:', error);
            alert('Hubo un problema al actualizar la categoría de vehículo.');
        }
    }

    async function eliminarVehiculoCategoria(categoriaId) {
        try {
            const response = await fetch(`/api/vehiculo-categorias/eliminar/${categoriaId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error al eliminar la categoría de vehículo');
            }

            console.log('Categoría de vehículo eliminada con éxito');
            fetchVehiculoCategorias(); // Actualizar la tabla de categorías de vehículos después de eliminar
        } catch (error) {
            console.error('Error al eliminar la categoría de vehículo:', error);
            alert('Hubo un problema al eliminar la categoría de vehículo.');
        }
    }

    // Tarifas
    document.getElementById('btnMostrarFormCrearTarifa').addEventListener('click', function(event) {
event.preventDefault();
document.getElementById('formCrearTarifa').style.display = 'block';
document.getElementById('formActualizarTarifa').style.display = 'none';
fetchVehiculoCategoriasLis('vehiculoTarifa'); // Populate vehicle categories
fetchZonasLis('zonaTarifa'); // Populate zones
});

    // Formulario para crear nueva tarifa
document.getElementById('crearTarifaForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const montoTarifa = document.getElementById('montoTarifa').value;
    const vehiculoTarifa = document.getElementById('vehiculoTarifa').value;
    const zonaTarifa = document.getElementById('zonaTarifa').value;

    const tarifaData = {
        monto: montoTarifa,
        vehiculoId: vehiculoTarifa,
        zonaId: zonaTarifa
    };

    crearTarifa(tarifaData);
    document.getElementById('formCrearTarifa').style.display = 'none';
});

// Formulario para actualizar tarifa
document.getElementById('actualizarTarifaForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const tarifaId = document.getElementById('tarifaIdActualizar').value;
    const montoTarifa = document.getElementById('montoTarifaActualizar').value;
    const vehiculoTarifa = document.getElementById('vehiculoTarifaActualizar').value;
    const zonaTarifa = document.getElementById('zonaTarifaActualizar').value;

    const tarifaData = {
        monto: montoTarifa,
        vehiculoId: vehiculoTarifa,
        zonaId: zonaTarifa
    };

    actualizarTarifa(tarifaId, tarifaData);
    document.getElementById('formActualizarTarifa').style.display = 'none';
});

// Agregar manejo de eventos para los botones de acciones en la tabla de tarifas
document.getElementById('tarifasTable').addEventListener('click', function(event) {
    const target = event.target;

    // Si se hace clic en el botón "Eliminar" de una fila
    if (target.matches('.btn-eliminar')) {
        const tarifaId = target.dataset.tarifaId;
        eliminarTarifa(tarifaId);
    }

    // Si se hace clic en el botón "Editar" de una fila
    if (target.matches('.btn-editar')) {
        const tarifaId = target.dataset.tarifaId;
        const montoTarifa = target.closest('tr').querySelector('td:nth-child(2)').textContent;
        const vehiculoTarifa = target.closest('tr').querySelector('td:nth-child(3)').textContent;
        const zonaTarifa = target.closest('tr').querySelector('td:nth-child(4)').textContent;

        document.getElementById('tarifaIdActualizar').value = tarifaId;
        document.getElementById('montoTarifaActualizar').value = montoTarifa;
        fetchVehiculoCategoriasLis('vehiculoTarifaActualizar', vehiculoTarifa); // Populate vehicle categories with current value
        fetchZonasLis('zonaTarifaActualizar', zonaTarifa); // Populate zones with current value
        document.getElementById('formCrearTarifa').style.display = 'none';
        document.getElementById('formActualizarTarifa').style.display = 'block';
    }
});

async function fetchTarifas() {
    try {
        const response = await fetch('/api/tarifas', {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Error en la solicitud');
        }

        const data = await response.json();
        console.log('Tarifas recibidas:', data);
        const tarifasTableBody = document.getElementById('tarifasTableBody');
        const emptyMessage = document.getElementById('emptyMessageTarifas');

        tarifasTableBody.innerHTML = ''; // Limpiar contenido previo

        if (data.length === 0) {
            emptyMessage.style.display = 'block';
            return;
        }

        data.forEach(tarifa => {
            const row = tarifasTableBody.insertRow();
            row.innerHTML = `
                <td>${tarifa.id}</td>
                <td>${tarifa.monto}</td>
                <td>${tarifa.vehiculo}</td>
                <td>${tarifa.zona}</td>
                <td>
                    <button class="btn-editar" data-tarifa-id="${tarifa.id}">Editar</button>
                    <button class="btn-eliminar" data-tarifa-id="${tarifa.id}">Eliminar</button>
                </td>
            `;
        });

    } catch (error) {
        console.error('Error al obtener tarifas:', error);
        alert('Hubo un problema al cargar las tarifas.');
    }
}

async function crearTarifa(tarifaData) {
    try {
        const response = await fetch('/api/tarifas/crear', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(tarifaData)
        });

        if (!response.ok) {
            throw new Error('Error al crear la tarifa');
        }

        const nuevaTarifa = await response.json();
        console.log('Tarifa creada:', nuevaTarifa);
        fetchTarifas(); // Actualizar la tabla de tarifas después de crear
    } catch (error) {
        console.error('Error al crear la tarifa:', error);
        alert('Hubo un problema al crear la tarifa.');
    }
}

async function actualizarTarifa(tarifaId, tarifaData) {
    try {
        const response = await fetch(`/api/tarifas/actualizar/${tarifaId}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(tarifaData)
        });

        if (!response.ok) {
            throw new Error('Error al actualizar la tarifa');
        }

        const tarifaActualizada = await response.json();
        console.log('Tarifa actualizada:', tarifaActualizada);
        fetchTarifas(); // Actualizar la tabla de tarifas después de actualizar
    } catch (error) {
        console.error('Error al actualizar la tarifa:', error);
        alert('Hubo un problema al actualizar la tarifa.');
    }
}

async function eliminarTarifa(tarifaId) {
    try {
        const response = await fetch(`/api/tarifas/eliminar/${tarifaId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Error al eliminar la tarifa');
        }

        console.log('Tarifa eliminada');
        fetchTarifas(); // Actualizar la tabla de tarifas después de eliminar
    } catch (error) {
        console.error('Error al eliminar la tarifa:', error);
        alert('Hubo un problema al eliminar la tarifa.');
    }
}

async function fetchVehiculoCategoriasLis(selectId, selectedValue) {
    try {
        const response = await fetch('/api/vehiculo-categorias', {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Error en la solicitud');
        }

        const data = await response.json();
        const selectElement = document.getElementById(selectId);

        selectElement.innerHTML = ''; // Limpiar opciones previas
        data.forEach(categoria => {
            const option = document.createElement('option');
            option.value = categoria.id;
            option.text = categoria.tipo;
            selectElement.add(option);
        });

        if (selectedValue) {
            selectElement.value = selectedValue;
        }

    } catch (error) {
        console.error('Error al obtener categorías de vehículos:', error);
        alert('Hubo un problema al cargar las categorías de vehículos.');
    }
}

async function fetchZonasLis(selectId, selectedValue) {
    try {
        const response = await fetch('/api/zonas', {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Error en la solicitud');
        }

        const data = await response.json();
        const selectElement = document.getElementById(selectId);

        selectElement.innerHTML = ''; // Limpiar opciones previas
        data.forEach(zona => {
            const option = document.createElement('option');
            option.value = zona.id;
            option.text = zona.nombre;
            selectElement.add(option);
        });

        if (selectedValue) {
            selectElement.value = selectedValue;
        }

    } catch (error) {
        console.error('Error al obtener zonas:', error);
        alert('Hubo un problema al cargar las zonas.');
    }
}

//Transacciones
async function fetchTransacciones() {
    try {
        const response = await fetch('/api/pagos', {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Error en la solicitud');
        }

        const data = await response.json();
        console.log('Transacciones recibidas:', data);
        const transaccionesTableBody = document.getElementById('transaccionesTableBody');
        const emptyMessage = document.getElementById('emptyMessageTransacciones');

        transaccionesTableBody.innerHTML = '';

        if (data.length === 0) {
            emptyMessage.style.display = 'block';
            return;
        }

        data.forEach(transaccion => {
            const row = transaccionesTableBody.insertRow();
            row.innerHTML = `
                <td>${transaccion.id}</td>
                 <td>${transaccion.usuario}</td>
                <td>${transaccion.vehiculo}</td>
                <td>${transaccion.zona}</td>
                <td>${transaccion.monto}</td>
                <td>${transaccion.fechaPago}</td>
            `;
        });

    } catch (error) {
        console.error('Error al obtener transacciones:', error);
        alert('Hubo un problema al cargar las transacciones de pagos.');
    }
}
// Función para cargar tipos de pago
function fetchTiposPago() {
    fetch('/api/tipopago')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {

                renderTiposPago(data);
            } else {
                showEmptyMessage();
            }
        })
        .catch(error => {
            console.error('Error al obtener tipos de pago:', error);
            alert('Hubo un problema al obtener los tipos de pago.');
        });
}

// Función para renderizar tipos de pago en la tabla
function renderTiposPago(tiposPago) {
    const tipoPagoTableBody = document.getElementById('tipoPagoTableBody');
    tipoPagoTableBody.innerHTML = '';
    tiposPago.forEach(tipoPago => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${tipoPago.id}</td>
            <td>${tipoPago.descripcion}</td>
            <td>
                <button onclick="mostrarFormActualizarTipoPago(${tipoPago.id}, '${tipoPago.descripcion}')">Actualizar</button>
                <button onclick="eliminarTipoPago(${tipoPago.id})">Eliminar</button>
            </td>
        `;
        tipoPagoTableBody.appendChild(tr);
    });
}

// Función para mostrar mensaje de lista vacía
function showEmptyMessage() {
    const emptyMessageTipoPago = document.getElementById('emptyMessageTipoPago');
    emptyMessageTipoPago.style.display = 'block';
}

// Función para mostrar formulario de actualizar tipo de pago
window.mostrarFormActualizarTipoPago = function(id, descripcion) {
    const formCrearTipoPago = document.getElementById('formCrearTipoPago');
    const formActualizarTipoPago = document.getElementById('formActualizarTipoPago');
    formCrearTipoPago.style.display = 'none';
    formActualizarTipoPago.style.display = 'block';
    document.getElementById('tipoPagoIdActualizar').value = id;
    document.getElementById('nombreTipoPagoActualizar').value = descripcion;
}

// Función para eliminar tipo de pago
window.eliminarTipoPago = function(id) {
    if (confirm('¿Estás seguro de eliminar este tipo de pago?')) {
        fetch(`/api/tipopago/${id}`, {
            method: 'DELETE'
        })
        .then(() => {

            fetchTiposPago(); // Actualizar la lista después de eliminar
        })
        .catch(error => {
            console.error('Error al eliminar tipo de pago:', error);
            alert('Hubo un problema al eliminar el tipo de pago.');
        });
    }
}

// Event listener para mostrar el formulario de crear tipo de pago
document.getElementById('btnMostrarFormCrearTipoPago').addEventListener('click', () => {
    document.getElementById('formCrearTipoPago').style.display = 'block';
    document.getElementById('formActualizarTipoPago').style.display = 'none';
});

// Event listener para manejar el envío del formulario de crear tipo de pago
document.getElementById('crearTipoPagoForm').addEventListener('submit', (event) => {
    event.preventDefault();
    const nombreTipoPago = document.getElementById('nombreTipoPago').value;

    fetch('/api/tipopago', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ descripcion: nombreTipoPago })
    })
    .then(response => response.json())
    .then(data => {

        fetchTiposPago(); // Actualizar la lista después de crear
        document.getElementById('formCrearTipoPago').style.display = 'none'; // Ocultar el formulario
        document.getElementById('crearTipoPagoForm').reset(); // Resetear el formulario
    })
    .catch(error => {
        console.error('Error al crear tipo de pago:', error);
        alert('Hubo un problema al crear el tipo de pago.');
    });
});

// Event listener para manejar el envío del formulario de actualizar tipo de pago
document.getElementById('actualizarTipoPagoForm').addEventListener('submit', (event) => {
    event.preventDefault();
    const id = document.getElementById('tipoPagoIdActualizar').value;
    const nombreTipoPago = document.getElementById('nombreTipoPagoActualizar').value;

    fetch(`/api/tipopago/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ descripcion: nombreTipoPago })
    })
    .then(response => response.json())
    .then(data => {
        fetchTiposPago(); // Actualizar la lista después de actualizar
        document.getElementById('formActualizarTipoPago').style.display = 'none'; // Ocultar el formulario
    })
    .catch(error => {
        console.error('Error al actualizar tipo de pago:', error);
        alert('Hubo un problema al actualizar el tipo de pago.');
    });
});
// Función para cargar todos los Telepasses
function fetchTelepasses() {
    fetch('/api/telepass')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                console.log(data);
                renderTelepasses(data);
            } else {
                showEmptyMessage();
            }
        })
        .catch(error => {
            console.error('Error al obtener Telepasses:', error);
            alert('Hubo un problema al obtener los Telepasses.');
        });
}

// Función para renderizar Telepasses en la tabla
function renderTelepasses(telepasses) {
    const telepassTableBody = document.getElementById('telepassTableBody');
    telepassTableBody.innerHTML = '';
    telepasses.forEach(telepass => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${telepass.id}</td>
            <td>${telepass.usuarioId}</td>
            <td>${telepass.vehiculoId}</td>
            <td>${telepass.activo ? 'Sí' : 'No'}</td>
            <td>
                ${!telepass.activo ? `<button onclick="aprobarTelepass(${telepass.id})">Aprobar</button>` : ''}
            </td>
        `;
        telepassTableBody.appendChild(tr);
    });
}

// Función para mostrar mensaje de lista vacía
function showEmptyMessage() {
    const emptyMessageTelepass = document.getElementById('emptyMessageTelepass');
    emptyMessageTelepass.style.display = 'block';
}

// Función para aprobar Telepass
window.aprobarTelepass = function(id) {
    fetch(`/api/telepass/aprobar/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => response.json())
    .then(data => {
        alert('Telepass aprobado correctamente.');
        // Actualizar la lista después de aprobar
        fetchTelepasses();
    })
    .catch(error => {
        console.error('Error al aprobar Telepass:', error);
        alert('Hubo un problema al aprobar el Telepass.');
    });
}

});

  document.addEventListener('DOMContentLoaded', () => {
// Event listener para el botón "Salir"
document.querySelector('.salirFo').addEventListener('click', () => {
    // Aquí podrías agregar lógica para limpiar datos de sesión si es necesario

    // Redirigir a la página de autenticación
    window.location.href = '/auth'; // Reemplaza '/auth' con la ruta correcta a tu página de autenticación
});
});