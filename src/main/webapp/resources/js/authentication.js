let container = document.getElementById('container');

const checkForErrors = () => {
    let errorElements = document.querySelectorAll('.error'); // Tìm tất cả các phần tử lỗi
    return Array.from(errorElements).some(error => error.textContent.trim() !== ''); // Kiểm tra nếu có lỗi nào không
};

const toggle = () => {
    if (checkForErrors()) {
        return; // Nếu có lỗi, không chuyển đổi
    }
    // Nếu không có lỗi, thực hiện chuyển đổi
    container.classList.toggle('sign-in');
    container.classList.toggle('sign-up');
};

// Thực hiện kiểm tra lỗi khi tải trang
setTimeout(() => {
    if (checkForErrors()) {
        container.classList.add('sign-up'); // Chuyển sang giao diện đăng ký nếu có lỗi
    } else {
        container.classList.add('sign-in'); // Giữ giao diện đăng nhập nếu không có lỗi
    }
}, 200);
