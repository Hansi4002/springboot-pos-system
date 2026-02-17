function saveItem() {
    let code = $('#itemCode').val();
    let description = $('#itemDescription').val();
    let price = $('#itemPrice').val();
    let qty = $('#itemQty').val();

    if (!code || !description || !price || !qty) {
        alert('Please fill all fields');
        return;
    }

    $.ajax({
        url: 'http://localhost:8080/api/v1/items',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            code: code,
            description: description,
            unitPrice: parseFloat(price),
            qtyOnHand: parseInt(qty)
        }),
        success: function(res) {
            alert(res.message || 'Item saved successfully!');
            clearForm();
            getAllItems();
        },
        error: function(err) {
            let errorMsg = err.responseJSON?.message || err.message || 'Error saving item';
            alert('Error: ' + errorMsg);
        }
    });
}

function updateItem() {
    let code = $('#itemCode').val();
    let description = $('#itemDescription').val();
    let price = $('#itemPrice').val();
    let qty = $('#itemQty').val();

    if (!code || !description || !price || !qty) {
        alert('Please fill all fields');
        return;
    }

    $.ajax({
        url: 'http://localhost:8080/api/v1/items',
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            code: code,
            description: description,
            unitPrice: parseFloat(price),
            qtyOnHand: parseInt(qty)
        }),
        success: function(res) {
            alert(res.message || 'Item updated successfully!');
            clearForm();
            getAllItems();
        },
        error: function(err) {
            let errorMsg = err.responseJSON?.message || err.message || 'Error updating item';
            alert('Error: ' + errorMsg);
        }
    });
}

function deleteItem(code) {
    if (!confirm('Are you sure you want to delete this item?')) return;

    $.ajax({
        url: `http://localhost:8080/api/v1/items/${code}`,
        method: 'DELETE',
        success: function(res) {
            alert(res.message || 'Item deleted successfully!');
            getAllItems();
        },
        error: function(err) {
            let errorMsg = err.responseJSON?.message || err.message || 'Error deleting item';
            alert('Error: ' + errorMsg);
        }
    });
}

function searchItem() {
    let code = $('#itemCode').val();
    if (!code) {
        alert('Please enter Item Code to search');
        return;
    }

    $.ajax({
        url: `http://localhost:8080/api/v1/items/${code}`,
        method: 'GET',
        success: function(res) {
            if (res.data) {
                $('#itemCode').val(res.data.code);
                $('#itemDescription').val(res.data.description);
                $('#itemPrice').val(res.data.unitPrice);
                $('#itemQty').val(res.data.qtyOnHand);
            } else {
                alert('Item not found');
            }
        },
        error: function(err) {
            alert('Item not found');
        }
    });
}

function getAllItems() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/items',
        method: 'GET',
        success: function(res) {
            let tbody = $('#itemTableBody');
            tbody.empty();

            if (res.data) {
                const items = Array.isArray(res.data) ? res.data : Object.values(res.data);

                items.forEach(item => {
                    tbody.append(`
                        <tr>
                            <td>${item.code}</td>
                            <td>${item.description}</td>
                            <td>Rs.${item.unitPrice.toFixed(2)}</td>
                            <td>${item.qtyOnHand}</td>
                            <td>
                                <button class="action-btn edit-btn" onclick="editItem('${item.code}')">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <button class="action-btn delete-btn" onclick="deleteItem('${item.code}')">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                    `);
                });
            }
        },
        error: function(err) {
            console.error('Error loading items:', err);
            alert('Error loading items');
        }
    });
}

function editItem(code) {
    $.ajax({
        url: `http://localhost:8080/api/v1/items/${code}`,
        method: 'GET',
        success: function(res) {
            if (res.data) {
                $('#itemCode').val(res.data.code);
                $('#itemDescription').val(res.data.description);
                $('#itemPrice').val(res.data.unitPrice);
                $('#itemQty').val(res.data.qtyOnHand);
            }
        },
        error: function(err) {
            alert('Error loading item details');
        }
    });
}

function clearForm() {
    $('#itemCode').val('');
    $('#itemDescription').val('');
    $('#itemPrice').val('');
    $('#itemQty').val('');
}