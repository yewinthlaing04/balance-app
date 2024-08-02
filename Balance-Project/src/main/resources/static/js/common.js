document.addEventListener("DOMContentLoaded", () => {
    document.getElementById('signoutLink').addEventListener('click', (e) => {
        document.getElementById("signOutForm").submit()
    })

    Array.from(document.getElementsByClassName('userStatusChangeLink'))
        .forEach( link => link.addEventListener('click', (e) => {

           document.getElementById('changeUserId').setAttribute('value',link.getAttribute('data-id'))
            document.getElementById('changeUserStatus').setAttribute('value',link.getAttribute('data-status'))
             document.getElementById('changeUserName').innerText = link.getAttribute('data-name')
              document.getElementById('changeUserStatusBefore').innerText = link.getAttribute('data-status') == 'true' ? 'Active' : 'InActive'
               document.getElementById('changeUserStatusAfter').innerText = link.getAttribute('data-status') == 'true' ? 'InActive' : 'Active'

            const dialog = new bootstrap.Modal('#userStatusChangeModal')
            dialog.show()
        }))
})

