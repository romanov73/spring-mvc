function edit(id) {
    $.get("ajax/getEmail/"+id, function( data ) {
        $('#editId').text(data.id);
        $('#editTo').val(data.to);
        $('#editMessage').val(data.message);
        $('#editSubject').val(data.subject);
        $('#editModal').modal('show');
    });
}

function save() {
    postData = {
                id:$('#editId').text(),
                to:$('#editTo').val(),
                message:$('#editMessage').val(),
                subject:$('#editSubject').val(),
                version:0
                };

    $.ajax({
        url: 'ajax/saveEmail',
        cache: false,
        dataType: "json",
        data: JSON.stringify(postData),
        contentType: "application/json; charset=utf-8",
        processData: false,
        method: "POST",
        success: function (response) {
            $('#editModal').modal('hide');
            $("#records").html('');
            loadList();
        },
        error: function( error) {
            alert(error.responseText);
        }
    });
}

function loadList() {
        $.get("ajax/list", function( data ) {
            $.each(data, function(key, value) {
                $("#records").append(
                    "<div class='col-md-2'>"+value.to+"</div>"+
                    "<div class='col-md-4'>"+value.subject+"</div>"+
                    "<div class='col-md-4'>"+value.message+"</div>"+
                    "<div class='col-md-2' onclick='edit("+value.id+")'><i class='fa-solid fa-pen-to-square'></i></div>"
                );
            });
        });
}

