// --- PRODUCT DATA with CORRECT LOCAL IMAGE URLs ---
const products = [
    {id: 1, name: "Waffle-Knit Navy Sweater", price: 79.99, oldPrice: 95.00, category: "Clothing", image: "image/sweater.jpg"},
    {id: 2, name: "Oversized Corduroy Shirt", price: 64.95, oldPrice: 78.50, category: "Clothing", image: "image/shirt.jpg"},
    {id: 3, name: "Classic 550 Sneakers", price: 119.99, oldPrice: 130.00, category: "Footwear", image: "image/air max.jpg"},
    {id: 4, name: "Wave Runner Air Max", price: 160.00, oldPrice: 185.00, category: "Footwear", image: "image/shoes.jpeg"},
    {id: 5, name: "Abstract Graphic Print", price: 89.99, oldPrice: 110.00, category: "Home Decor", image: "image/poster.jpeg"},
    {id: 6, name: "Sculptural Wooden Lamp", price: 249.99, oldPrice: 299.99, category: "Home Decor", image: "image/lamp.jpeg"},
    {id: 7, name: "Premium Leather Wallet", price: 49.99, category: "Accessories", image: "image/wallet.jpg"},
    {id: 8, name: "Designer Sunglasses", price: 134.99, category: "Accessories", image: "image/sunglasses.jpg"},
];

// --- UTILITY & MOCK BACKEND FUNCTIONS ---
function getCart() { return JSON.parse(localStorage.getItem('smartkart_cart')) || []; }
function saveCart(cart) { localStorage.setItem('smartkart_cart', JSON.stringify(cart)); }
function getWallet() {
    const wallet = localStorage.getItem('smartkart_wallet');
    return wallet ? parseFloat(wallet) : 3000;
}
function saveWallet(balance) { localStorage.setItem('smartkart_wallet', balance.toString()); }
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
};

window.buyNow = function(event, productId) {
    event.stopPropagation();
    saveCart([{...products.find(p => p.id === productId), quantity: 1}]);
    window.location.href = 'checkout.html';
};

window.logout = function() {
    sessionStorage.removeItem('loggedInUser');
    alert("You have been logged out.");
    window.location.href = 'index.html';
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
    // Check which page we are on and re-render it
    if (document.getElementById('checkout-page-content')) {
        renderCheckoutPage();
    }
    if (document.getElementById('cart-page-content')) { // Assuming you have a cart page
        renderCartPage();
    }
    updateUICart();
};

window.removeFromCart = function(productId) {
    if (confirm("Are you sure you want to remove this item?")) {
        let cart = getCart().filter(p => p.id !== productId);
        saveCart(cart);
        if (document.getElementById('checkout-page-content')) {
            renderCheckoutPage();
        }
        if (document.getElementById('cart-page-content')) {
            renderCartPage();
        }
        updateUICart();
    }
};

// --- UI UPDATE & RENDER FUNCTIONS ---
function updateUICart() {
    const totalItems = getCart().reduce((sum, item) => sum + item.quantity, 0);
    document.querySelectorAll('#cartCount, #cartCountHero').forEach(el => {
        if (el) { el.textContent = totalItems; }
    });
}

function updateAuthUI() {
    const loggedInUser = getLoggedInUser();
    const authContainers = document.querySelectorAll('#auth-container, #auth-container-hero');
    const loginHTML = `<a href="login.html" class="p-2 hover:text-primary"><i class="fas fa-user-circle text-xl"></i></a>`;
    const logoutHTML = `<button onclick="logout()" class="p-2 hover:text-primary" title="Logout"><i class="fas fa-sign-out-alt text-xl"></i></button>`;

    authContainers.forEach(container => {
        if (container) {
            container.innerHTML = loggedInUser ? logoutHTML : loginHTML;
        }
    });
}

function renderProductCard(product) {
    return `<div class="product-card bg-white rounded-xl overflow-hidden shadow-lg hover:shadow-2xl transition-all duration-300 flex flex-col"><div class="relative"><img src="${product.image}" alt="${product.name}" class="w-full h-80 object-cover"></div><div class="p-4 flex flex-col flex-grow"><span class="text-gray-500 text-sm">${product.category}</span><h3 class="font-bold text-lg my-2 text-dark flex-grow">${product.name}</h3><div class="flex items-center justify-between mt-2"><div><span class="font-bold text-black text-xl">$${product.price.toFixed(2)}</span>${product.oldPrice ? `<span class="text-gray-500 line-through ml-2">$${product.oldPrice.toFixed(2)}</span>` : ''}</div></div><div class="grid grid-cols-2 gap-2 mt-4"><button class="w-full bg-dark/10 text-dark font-bold py-2 rounded-lg hover:bg-dark/20" onclick="addToCart(event, ${product.id})">Add to Cart</button><button class="w-full bg-dark text-white font-bold py-2 rounded-lg hover:bg-black" onclick="buyNow(event, ${product.id})">Buy Now</button></div></div></div>`;
}

function renderHomePage() {
    const grid = document.getElementById('featuredProductsGrid');
    if (grid) { grid.innerHTML = products.map(renderProductCard).join(''); }
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
    let subtotal = 0;

    if (cart.length > 0) {
        container.innerHTML = cart.map(item => {
            const itemTotal = item.price * item.quantity;
            subtotal += itemTotal;
            return `
            <div class="flex items-center justify-between py-4 border-b">
                <div class="flex items-center gap-4">
                    <img src="${item.image}" alt="${item.name}" class="w-20 h-20 object-cover rounded-md">
                    <div>
                        <h4 class="font-bold text-dark">${item.name}</h4>
                        <span class="text-sm text-gray-500">$${item.price.toFixed(2)} each</span>
                    </div>
                </div>
                <div class="flex items-center gap-4">
                    <div class="flex items-center justify-center gap-2">
                        <button class="quantity-btn" onclick="adjustCartQuantity(${item.id}, -1)">-</button>
                        <span class="font-bold text-dark w-6 text-center">${item.quantity}</span>
                        <button class="quantity-btn" onclick="adjustCartQuantity(${item.id}, 1)">+</button>
                    </div>
                    <p class="font-bold text-dark w-24 text-right">$${itemTotal.toFixed(2)}</p>
                    <button onclick="removeFromCart(${item.id})" class="text-red-500 hover:text-red-700 text-lg" title="Remove Item">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                </div>
            </div>`;
        }).join('');
    } else {
        container.innerHTML = `<p class="p-8 text-center text-gray-500">Your cart is empty. <a href="products.html" class="text-dark font-bold hover:underline">Continue Shopping</a></p>`;
    }

    const wallet = getWallet();
    const tax = subtotal * 0.10;
    const total = subtotal + tax;

    document.getElementById('walletBalance').textContent = `$${wallet.toFixed(2)}`;
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
        if (wallet < total) { alert("Insufficient wallet balance to complete this purchase."); return; }

        const remainingBalance = wallet - total;
        saveWallet(remainingBalance);
        saveCart([]);
        alert(`Purchase successful! Your new wallet balance is $${remainingBalance.toFixed(2)}.`);
        window.location.href = 'index.html';
    };
} // <-- THIS IS THE MISSING BRACE THAT WAS ADDED

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
        let users = getRegisteredUsers();
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
    getWallet(); // Initialize wallet on first load if it doesn't exist
    updateUICart();
    updateAuthUI();
    if (document.getElementById('home-page-content')) renderHomePage();
    if (document.getElementById('product-page-content')) renderProductsPage();
    if (document.getElementById('checkout-page-content')) renderCheckoutPage();
    if (document.getElementById('login-page-content')) handleAuthPage();
});