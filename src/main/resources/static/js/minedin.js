function changePoolDropdownMenu(text, value, buttonId) {
    $('#' + buttonId).text(text);
    $('#' + buttonId).val(value);
    checkWalletAddressAndPool();
};

function checkWalletAddressAndPool() {
    var isWalletAddressEmpty = $('#walletAddress').val().length === 0;
    var isPoolSelected = $('#poolDropdownMenuButton').val() !== "";
    $('#calculateButton').prop('disabled', isWalletAddressEmpty || !isPoolSelected);
}

function calculate(coinType) {
    window.location.href = '/' + coinType + '/' + poolDropdownMenuButton.value + '/' + walletAddress.value;
}