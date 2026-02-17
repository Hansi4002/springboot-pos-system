let cart = [];

function loadCustomers() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/customers',
        method: 'GET',
        success: function(res) {
            let select = $('#customerSelect');
            select.empty().append('<option value="">Select Customer</option>');

            if (res.data) {
                const customers = Array.isArray(res.data) ? res.data : Object.values(res.data);
                customers.forEach(customer => {
                    select.append(`<option value="${customer.cId}">${customer.cName}</option>`);
                });
            }
        },
        error: function(err) {
            console.error('Error loading customers:', err);
        }
    });
}

function loadItems() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/items',
        method: 'GET',
        success: function(res) {
            let select = $('#itemSelect');
            select.empty().append('<option value="">Select Item</option>');

            if (res.data) {
                const items = Array.isArray(res.data) ? res.data : Object.values(res.data);
                items.forEach(item => {
                    select.append(`<option value="${item.code}" data-price="${item.unitPrice}" data-qty="${item.qtyOnHand}">${item.description} - Rs.${item.unitPrice} (Stock: ${item.qtyOnHand})</option>`);
                });
            }
        },
        error: function(err) {
            console.error('Error loading items:', err);
        }
    });
}

function addToCart() {
    let itemCode = $('#itemSelect').val();
    let qty = parseInt($('#orderQty').val());

    if (!itemCode) {
        alert('Please select an item');
        return;
    }

    if (!qty || qty < 1) {
        alert('Please enter valid quantity');
        return;
    }

    let selected = $('#itemSelect option:selected');
    let price = parseFloat(selected.data('price'));
    let available = parseInt(selected.data('qty'));

    if (qty > available) {
        alert('Insufficient stock! Available: ' + available);
        return;
    }

    cart.push({
        itemCode: itemCode,
        description: selected.text().split(' - ')[0],
        price: price,
        qty: qty,
        total: price * qty
    });

    updateCartDisplay();
    $('#orderQty').val('');
}

function placeOrder() {
    let customerId = $('#customerSelect').val();

    if (!customerId) {
        alert('Please select a customer');
        return;
    }

    if (cart.length === 0) {
        alert('Cart is empty');
        return;
    }

    let total = cart.reduce((sum, item) => sum + (item.price * item.qty), 0);

    let orderData = {
        cId: customerId,
        date: new Date().toISOString(),
        total: total,
        items: cart.map(item => ({
            itemCode: item.itemCode,
            qty: item.qty,
            unitPrice: item.price
        }))
    };

    console.log("Sending data:", JSON.stringify(orderData, null, 2));

    $.ajax({
        url: 'http://localhost:8080/api/v1/orders',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(orderData),
        success: function(res) {
            alert('Order placed successfully! Order ID: ' + res.data.orderId);
            cart = [];
            updateCartDisplay();
            $('#customerSelect').val('');
            loadItems();
            loadOrderHistory();
        },
        error: function(xhr) {
            console.error("Error response:", xhr.responseText);
            alert('Error: ' + (xhr.responseJSON?.message || 'Failed to place order'));
        }
    });
}

function updateCartDisplay() {
    let tbody = $('#cartItems');
    tbody.empty();

    let subtotal = 0;

    cart.forEach((item, index) => {
        subtotal += item.total;
        tbody.append(`
            <tr>
                <td>${item.description}</td>
                <td>Rs.${item.price.toFixed(2)}</td>
                <td>${item.qty}</td>
                <td>Rs.${item.total.toFixed(2)}</td>
                <td>
                    <button class="btn-danger" style="padding:0.2rem 0.5rem;" onclick="removeFromCart(${index})">
                        <i class="fas fa-times"></i>
                    </button>
                </td>
            </tr>
        `);
    });

    $('#subtotal').text(`$${subtotal.toFixed(2)}`);
    $('#total').text(`$${subtotal.toFixed(2)}`);
}

function removeFromCart(index) {
    cart.splice(index, 1);
    updateCartDisplay();
}

function loadOrderHistory() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/orders',
        method: 'GET',
        success: function(res) {
            console.log("Orders from backend:", res.data);

            let tbody = $('#orderHistory');
            tbody.empty();

            if (res.data) {
                const orders = Array.isArray(res.data) ? res.data : Object.values(res.data);
                orders.forEach(order => {
                    console.log("Single order:", order);
                    tbody.append(`
                        <tr>
                            <td>${order.orderId || 'N/A'}</td>
                            <td>${new Date(order.date).toLocaleDateString()}</td>
                            <td>${order.customerName || order.cId}</td>
                            <td>Rs.${order.total.toFixed(2)}</td>
                        </tr>
                    `);
                });
            }
        },
        error: function(err) {
            console.error('Error loading order history:', err);
        }
    });
}

$(document).ready(function() {
    loadCustomers();
    loadItems();
    loadOrderHistory();
});
