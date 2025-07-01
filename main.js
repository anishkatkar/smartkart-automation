// --- PRODUCT DATA with NEW, WORKING IMAGE URLs ---
const products = [
    {id: 1, name: "Waffle-Knit Navy Sweater", price: 79.99, oldPrice: 95.00, category: "Clothing", image: "image 1 (1).jpeg"},
    {id: 2, name: "Oversized Corduroy Shirt", price: 64.95, oldPrice: 78.50, category: "Clothing", image: "image 1 (1).jpg"},
    {id: 3, name: "Classic 550 Sneakers", price: 119.99, oldPrice: 130.00, category: "Footwear", image: "image 1 (2).jpeg"},
    {id: 4, name: "Wave Runner Air Max", price: 160.00, oldPrice: 185.00, category: "Footwear", image: "image 1 (2).jpg"},
    {id: 5, name: "Abstract Graphic Print", price: 89.99, oldPrice: 110.00, category: "Home Decor", image: "image 1 (3).jpeg"},
    {id: 6, name: "Sculptural Wooden Lamp", price: 249.99, oldPrice: 299.99, category: "Home Decor", image: "image 1 (4).jpeg"},
    {id: 7, name: "Premium Leather Wallet", price: 49.99, oldPrice: 59.99, category: "Accessories", image: "https://i.ibb.co/5cWpP9s/waffle-sweater.jpg"}, // Placeholder image
    {id: 8, name: "Designer Sunglasses", price: 134.99, oldPrice: 159.99, category: "Accessories", image: "https://i.ibb.co/3s3v4Yy/corduroy-shirt.jpg"}, // Placeholder image
];

// --- UTILITY FUNCTIONS ---
function getCart() { return JSON.parse(localStorage.getItem('smartkart_cart')) || []; }
function saveCart(cart) { localStorage.setItem('smartkart_cart', JSON.stringify(cart)); }
function getWallet() { return 30000; } // Wallet always resets to $30,000 on page load.
function getRegisteredUsers() { return JSON.parse(localStorage.getItem('smartkart_users')) || []; }
function saveRegisteredUsers(users) { localStorage.setItem('smartkart_users', JSON.stringify(users)); }
function getLoggedInUser() { return sessionStorage.getItem('loggedInUser'); }

// --- ACTION FUNCTIONS ---
window.addToCart = function(event, productId) {
    event.stopPropagation();
    const product = products.find(p => p.id === productId);
    if (!product) return;
    let cart = getCart();
    const existingItem = cart.find(item => item.id === productId);
    if (existingItem) { existingItem.quantity++; } else { cart.push({ ...product, quantity: 1 }); }
    saveCart(cart);
    updateUICart();
    alert(`${product.name} has been added to your cart.`);
}

window.buyNow = function(event, productId) {
    event.stopPropagation();
    const product = products.find(p => p.id === productId);
    if (!product) return;
    saveCart([{ ...product, quantity: 1 }]);
    window.location.href = 'checkout.html';
}

window.logout = function() {
    sessionStorage.removeItem('loggedInUser');
    alert("You have been logged out.");
    window.location.href = 'login.html';
};

window.adjustCartQuantity = function(productId, change) {
    let cart = getCart();
    const item = cart.find(p => p.id === productId);
    if (item) {
        item.quantity += change;
        if (item.quantity <= 0) {
            cart = cart.filter(p => p.id !== productId);
        }
    }
    saveCart(cart);
    renderCheckoutPage();
    updateUICart();
};

// --- UI UPDATE & RENDER FUNCTIONS ---
function updateUICart() {
    const totalItems = getCart().reduce((sum, item) => sum + item.quantity, 0);
    const cartCountElements = document.querySelectorAll('#cartCount, #cartCountHero');

    cartCountElements.forEach(el => {
        if (el) {
            el.textContent = totalItems;
            if (totalItems > 0) {
                el.classList.remove('hidden');
            } else {
                el.classList.add('hidden');
            }
        }
    });
}

function updateAuthUI() {
    const loggedInUser = getLoggedInUser();
    const authContainers = document.querySelectorAll('#auth-container, #auth-container-hero');
    const loginHTML = `<a href="login.html" class="p-2 hover:text-primary hidden md:block"><i class="fas fa-user-circle text-xl"></i></a>`;
    const logoutHTML = `<button onclick="logout()" class="p-2 hover:text-primary hidden md:block" title="Logout"><i class="fas fa-sign-out-alt text-xl"></i></button>`;

    authContainers.forEach(container => {
        if (container) {
            container.innerHTML = loggedInUser ? logoutHTML : loginHTML;
        }
    });
}

function renderProductCard(product) {
    return `
    <div class="product-card bg-white rounded-xl overflow-hidden shadow-lg hover:shadow-2xl transition-all duration-300 flex flex-col">
        <div class="relative"><img src="${product.image}" alt="${product.name}" class="w-full h-80 object-cover"><div class="absolute top-4 right-4"><button class="w-10 h-10 bg-white rounded-full flex items-center justify-center text-gray-500 hover:text-red-500"><i class="fas fa-heart"></i></button></div>${product.oldPrice ? `<div class="absolute top-4 left-4 bg-primary text-dark text-sm font-bold py-1 px-3 rounded">SAVE ${Math.round((1 - product.price/product.oldPrice)*100)}%</div>` : ''}</div>
        <div class="p-4 flex flex-col flex-grow">
            <span class="text-gray-500 text-sm">${product.category}</span><h3 class="font-bold text-lg my-2 text-dark flex-grow">${product.name}</h3>
            <div class="flex items-center justify-between mt-2"><div><span class="font-bold text-black text-xl">$${product.price.toFixed(2)}</span>${product.oldPrice ? `<span class="text-gray-500 line-through ml-2">$${product.oldPrice.toFixed(2)}</span>` : ''}</div></div>
            <div class="grid grid-cols-2 gap-2 mt-4">
                <button class="w-full bg-dark/10 text-dark font-bold py-2 rounded-lg hover:bg-dark/20" onclick="addToCart(event, ${product.id})">Add to Cart</button>
                <button class="w-full bg-dark text-white font-bold py-2 rounded-lg hover:bg-black" onclick="buyNow(event, ${product.id})">Buy Now</button>
            </div>
        </div>
    </div>`;
}

function renderHomePage() {
    const featuredGrid = document.getElementById('featuredProductsGrid');
    if (featuredGrid) { featuredGrid.innerHTML = products.slice(0, 4).map(renderProductCard).join(''); }
}

function renderProductsPage() {
    const productGrid = document.getElementById('productGrid');
    if (!productGrid) return;
    const params = new URLSearchParams(window.location.search);
    const category = params.get('category');
    const pageTitle = document.getElementById('page-title');
    let productsToRender = category ? products.filter(p => p.category === category) : products;
    if(pageTitle) pageTitle.textContent = category || "All Products";
    productGrid.innerHTML = productsToRender.length > 0 ? productsToRender.map(renderProductCard).join('') : `<p class="col-span-full text-center">No products found.</p>`;
}

function renderCheckoutPage() {
    const container = document.getElementById('checkoutCartItems');
    if (!container) return;
    const cart = getCart();
    const wallet = getWallet();
    const subtotal = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
    const tax = subtotal * 0.10;
    const total = subtotal + tax;

    document.getElementById('walletBalance').textContent = `$${wallet.toFixed(2)}`;

    if (cart.length > 0) {
        container.innerHTML = cart.map(item => `
            <div class="flex items-center justify-between">
                <div class="flex items-center">
                    <img src="${item.image}" alt="${item.name}" class="w-20 h-20 object-cover rounded-md mr-4">
                    <div>
                        <h4 class="font-bold text-dark">${item.name}</h4>
                        <div class="flex items-center gap-2 mt-1">
                            <button class="quantity-btn" onclick="adjustCartQuantity(${item.id}, -1)">-</button>
                            <span class="text-gray-700 font-bold w-6 text-center">${item.quantity}</span>
                            <button class="quantity-btn" onclick="adjustCartQuantity(${item.id}, 1)">+</button>
                        </div>
                    </div>
                </div>
                <p class="font-bold text-dark">$${(item.price * item.quantity).toFixed(2)}</p>
            </div>
        `).join('');
    } else {
        container.innerHTML = `<p class="text-center text-gray-500">Your cart is empty. <a href="products.html" class="text-dark font-bold">Go shopping!</a></p>`;
    }

    document.getElementById('summarySubtotal').textContent = `$${subtotal.toFixed(2)}`;
    document.getElementById('summaryTax').textContent = `$${tax.toFixed(2)}`;
    document.getElementById('summaryTotal').textContent = `$${total.toFixed(2)}`;

    const buyNowBtn = document.getElementById('buyNowBtn');
    buyNowBtn.onclick = () => {
        if (!getLoggedInUser()) {
            alert("Login first before doing payment.");
            window.location.href = 'login.html';
            return;
        }
        if (total === 0) { alert("Your cart is empty!"); return; }
        if (wallet >= total) {
            const remainingBalance = wallet - total;
            saveCart([]);
            document.getElementById('walletBalance').textContent = `$${remainingBalance.toFixed(2)}`;
            container.innerHTML = `<div class="text-center p-4 bg-primary/20 rounded-lg"><h3 class="font-bold text-dark text-lg">Purchase Successful!</h3><p class="text-gray-700">Your items are on the way.</p><a href="products.html" class="inline-block mt-4 bg-dark text-white font-bold py-2 px-4 rounded-lg">Continue Shopping</a></div>`;
            buyNowBtn.disabled = true;
            buyNowBtn.textContent = 'Purchase Complete';
            buyNowBtn.classList.add('opacity-50', 'cursor-not-allowed');
            updateUICart();
        } else {
            alert("Insufficient wallet balance.");
        }
    };
}

function handleAuthPage() {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const loginTab = document.getElementById('loginTab');
    const registerTab = document.getElementById('registerTab');
    if (!loginForm) return;
    loginTab.addEventListener('click', () => {
        loginForm.classList.remove('hidden'); registerForm.classList.add('hidden');
        loginTab.classList.add('border-dark', 'text-dark'); loginTab.classList.remove('text-gray-500');
        registerTab.classList.remove('border-dark', 'text-dark'); registerTab.classList.add('text-gray-500');
    });
    registerTab.addEventListener('click', () => {
        registerForm.classList.remove('hidden'); loginForm.classList.add('hidden');
        registerTab.classList.add('border-dark', 'text-dark'); registerTab.classList.remove('text-gray-500');
        loginTab.classList.remove('border-dark', 'text-dark'); loginTab.classList.add('text-gray-500');
    });
    registerForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const email = document.getElementById('registerEmail').value;
        const password = document.getElementById('registerPassword').value;
        const confirmPassword = document.getElementById('registerConfirmPassword').value;
        if (password.length < 6) { alert('Password must be at least 6 characters long.'); return; }
        if (password !== confirmPassword) { alert('Passwords do not match.'); return; }
        const users = getRegisteredUsers();
        if (users.find(user => user.email === email)) { alert('An account with this email already exists.'); return; }
        users.push({ email, password });
        saveRegisteredUsers(users);
        alert('Registration successful! You can now log in.');
        loginTab.click();
    });
    loginForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;
        const users = getRegisteredUsers();
        const user = users.find(u => u.email === email && u.password === password);
        if (user) {
            sessionStorage.setItem('loggedInUser', email);
            alert(`Welcome back, ${email}!`);
            window.location.href = 'index.html';
        } else {
            alert('Invalid email or password.');
        }
    });
}

// --- INITIALIZATION ---
document.addEventListener('DOMContentLoaded', () => {
    updateUICart();
    updateAuthUI();
    if (document.getElementById('home-page-content')) renderHomePage();
    if (document.getElementById('product-page-content')) renderProductsPage();
    if (document.getElementById('checkout-page-content')) renderCheckoutPage();
    if (document.getElementById('login-page-content')) handleAuthPage();

    const header = document.getElementById('main-header');
    if(header) { window.addEventListener('scroll', () => { window.scrollY > 50 ? header.classList.add('scrolled') : header.classList.remove('scrolled'); }); }
});