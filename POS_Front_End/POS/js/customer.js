function saveCustomer() {
    let id = $('#customerId').val();
    let name = $('#customerName').val();
    let address = $('#customerAddress').val();

    if (!id || !name || !address) {
        alert('Please fill all fields');
        return;
    }

    $.ajax({
        url: 'http://localhost:8080/api/v1/customers',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            cId: id,
            cName: name,
            cAddress: address
        }),
        success: function(res) {
            alert(res.message || 'Customer saved successfully!');
            clearForm();
            getAllCustomers();
        },
        error: function(err) {
            let errorMsg = err.responseJSON?.message || err.message || 'Error saving customer';
            alert('Error: ' + errorMsg);
        }
    });
}

function updateCustomer() {
    let id = $('#customerId').val();
    let name = $('#customerName').val();
    let address = $('#customerAddress').val();

    if (!id || !name || !address) {
        alert('Please fill all fields');
        return;
    }

    $.ajax({
        url: 'http://localhost:8080/api/v1/customers',
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            cId: id,
            cName: name,
            cAddress: address
        }),
        success: function(res) {
            alert(res.message || 'Customer updated successfully!');
            clearForm();
            getAllCustomers();
        },
        error: function(err) {
            let errorMsg = err.responseJSON?.message || err.message || 'Error updating customer';
            alert('Error: ' + errorMsg);
        }
    });
}

function deleteCustomer(id) {
    if (!confirm('Are you sure you want to delete this customer?')) return;

    $.ajax({
        url: `http://localhost:8080/api/v1/customers/${id}`,
        method: 'DELETE',
        success: function(res) {
            alert(res.message || 'Customer deleted successfully!');
            getAllCustomers();
        },
        error: function(err) {
            let errorMsg = err.responseJSON?.message || err.message || 'Error deleting customer';
            alert('Error: ' + errorMsg);
        }
    });
}

function searchCustomer() {
    let id = $('#customerId').val();
    if (!id) {
        alert('Please enter Customer ID to search');
        return;
    }

    $.ajax({
        url: `http://localhost:8080/api/v1/customers/${id}`,
        method: 'GET',
        success: function(res) {
            if (res.data) {
                $('#customerId').val(res.data.cId);
                $('#customerName').val(res.data.cName);
                $('#customerAddress').val(res.data.cAddress);
            } else {
                alert('Customer not found');
            }
        },
        error: function(err) {
            alert('Customer not found');
        }
    });
}

function getAllCustomers() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/customers',
        method: 'GET',
        success: function(res) {
            let tbody = $('#customerTableBody');
            tbody.empty();

            if (res.data) {
                const customers = Array.isArray(res.data) ? res.data : Object.values(res.data);

                customers.forEach(customer => {
                    tbody.append(`
                        <tr>
                            <td>${customer.cId}</td>
                            <td>${customer.cName}</td>
                            <td>${customer.cAddress}</td>
                            <td>
                                <button class="action-btn edit-btn" onclick="editCustomer('${customer.cId}')">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <button class="action-btn delete-btn" onclick="deleteCustomer('${customer.cId}')">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                    `);
                });
            }
        },
        error: function(err) {
            console.error('Error loading customers:', err);
            alert('Error loading customers');
        }
    });
}

function editCustomer(id) {
    $.ajax({
        url: `http://localhost:8080/api/v1/customers/${id}`,
        method: 'GET',
        success: function(res) {
            if (res.data) {
                $('#customerId').val(res.data.cId);
                $('#customerName').val(res.data.cName);
                $('#customerAddress').val(res.data.cAddress);
            }
        },
        error: function(err) {
            alert('Error loading customer details');
        }
    });
}

function clearForm() {
    $('#customerId').val('');
    $('#customerName').val('');
    $('#customerAddress').val('');
}