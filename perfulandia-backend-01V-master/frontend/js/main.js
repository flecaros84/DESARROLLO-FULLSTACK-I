// URLs de los microservicios
const API_ENDPOINTS = {
    USERS: 'http://localhost:8081/api/usuarios',
    PRODUCTS: 'http://localhost:8082/api/productos',
    INVENTORY: 'http://localhost:8083/api/inventario',
    SALES: 'http://localhost:8084/api/ventas'
};

// Elementos del DOM
const navLinks = document.querySelectorAll('nav a');
const sections = document.querySelectorAll('section');

// Cargar datos al iniciar
document.addEventListener('DOMContentLoaded', () => {
    loadDashboard();
    setupNavigation();
});

// Configurar navegación
function setupNavigation() {
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            
            // Actualizar enlace activo
            navLinks.forEach(l => l.classList.remove('active'));
            link.classList.add('active');
            
            // Mostrar sección correspondiente
            const targetId = link.getAttribute('href').substring(1);
            sections.forEach(section => {
                if (section.id === targetId) {
                    section.classList.remove('hidden');
                    // Cargar datos específicos de la sección
                    if (targetId === 'usuarios') {
                        loadUsers();
                    } else if (targetId === 'productos') {
                        loadProducts();
                    }
                } else if (section.id) { // Asegurarse de que no es el dashboard
                    section.classList.add('hidden');
                }
            });
            
            // Mostrar dashboard si es el enlace de inicio
            const dashboard = document.getElementById('dashboard');
            if (link.getAttribute('href') === '#') {
                dashboard.classList.remove('hidden');
            } else {
                dashboard.classList.add('hidden');
            }
        });
    });
}

// Cargar datos del dashboard
async function loadDashboard() {
    try {
        // Cargar conteo de usuarios
        const usersResponse = await fetch(API_ENDPOINTS.USERS);
        const users = await usersResponse.json();
        document.getElementById('user-count').textContent = users.length || 0;
        
        // Cargar conteo de productos
        const productsResponse = await fetch(API_ENDPOINTS.PRODUCTS);
        const products = await productsResponse.json();
        document.getElementById('product-count').textContent = products.length || 0;
        
        // Cargar conteo de ventas
        const salesResponse = await fetch(API_ENDPOINTS.SALES);
        const sales = await salesResponse.json();
        document.getElementById('sale-count').textContent = sales.length || 0;
        
    } catch (error) {
        console.error('Error cargando datos del dashboard:', error);
        showError('Error al cargar los datos del dashboard. Asegúrate de que los servicios estén en ejecución.');
    }
}

// Cargar lista de usuarios
async function loadUsers() {
    const usersList = document.getElementById('users-list');
    usersList.innerHTML = '<p>Cargando usuarios...</p>';
    
    try {
        const response = await fetch(API_ENDPOINTS.USERS);
        const users = await response.json();
        
        if (users.length === 0) {
            usersList.innerHTML = '<p>No hay usuarios registrados.</p>';
            return;
        }
        
        const usersHTML = `
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Email</th>
                        <th>Rol</th>
                    </tr>
                </thead>
                <tbody>
                    ${users.map(user => `
                        <tr>
                            <td>${user.id || 'N/A'}</td>
                            <td>${user.nombre || 'N/A'}</td>
                            <td>${user.email || 'N/A'}</td>
                            <td>${user.rol || 'N/A'}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;
        
        usersList.innerHTML = usersHTML;
    } catch (error) {
        console.error('Error cargando usuarios:', error);
        usersList.innerHTML = '<p class="error">Error al cargar los usuarios. Verifica la conexión con el servidor.</p>';
    }
}

// Cargar lista de productos
async function loadProducts() {
    const productsList = document.getElementById('products-list');
    productsList.innerHTML = '<p>Cargando productos...</p>';
    
    try {
        const response = await fetch(API_ENDPOINTS.PRODUCTS);
        const products = await response.json();
        
        if (products.length === 0) {
            productsList.innerHTML = '<p>No hay productos registrados.</p>';
            return;
        }
        
        const productsHTML = `
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Descripción</th>
                        <th>Precio</th>
                    </tr>
                </thead>
                <tbody>
                    ${products.map(product => `
                        <tr>
                            <td>${product.id || 'N/A'}</td>
                            <td>${product.nombre || 'N/A'}</td>
                            <td>${product.descripcion || 'N/A'}</td>
                            <td>$${product.precio ? product.precio.toFixed(2) : '0.00'}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;
        
        productsList.innerHTML = productsHTML;
    } catch (error) {
        console.error('Error cargando productos:', error);
        productsList.innerHTML = '<p class="error">Error al cargar los productos. Verifica la conexión con el servidor.</p>';
    }
}

// Mostrar mensaje de error
function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;
    
    // Insertar al principio del main
    const main = document.querySelector('main');
    main.insertBefore(errorDiv, main.firstChild);
    
    // Eliminar después de 5 segundos
    setTimeout(() => {
        errorDiv.remove();
    }, 5000);
}

// Agregar estilos para el mensaje de error
const errorStyles = document.createElement('style');
errorStyles.textContent = `
    .error-message {
        background-color: #ffebee;
        color: #c62828;
        padding: 1rem;
        margin-bottom: 1rem;
        border-radius: 4px;
        border-left: 4px solid #c62828;
    }
    
    table {
        width: 100%;
        border-collapse: collapse;
        margin: 1rem 0;
    }
    
    th, td {
        padding: 0.75rem;
        text-align: left;
        border-bottom: 1px solid #ddd;
    }
    
    th {
        background-color: #f4f4f4;
        font-weight: 600;
    }
    
    tr:hover {
        background-color: #f9f9f9;
    }
`;
document.head.appendChild(errorStyles);
