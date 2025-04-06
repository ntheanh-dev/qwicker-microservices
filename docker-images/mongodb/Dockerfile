# Sử dụng image MongoDB 4.2.16-bionic
FROM mongo:4.2.16-bionic

# Đặt các biến môi trường để khởi tạo MongoDB
ENV MONGO_INITDB_DATABASE=notification
ENV MONGO_INITDB_ROOT_USERNAME=root
ENV MONGO_INITDB_ROOT_PASSWORD=Admin@123

# Tạo thư mục chứa các script khởi tạo dữ liệu MongoDB
# Bạn sẽ copy các script vào thư mục này
COPY mongodb-init.js /docker-entrypoint-initdb.d/

# Mở cổng 27017 để MongoDB có thể kết nối
EXPOSE 27017

# Khởi động MongoDB với tham số --bind_ip_all để cho phép kết nối từ tất cả các địa chỉ IP
CMD ["mongod", "--bind_ip_all"]
