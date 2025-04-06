// Create a new user with admin privileges
db.createUser(
    {
        user: "root",
        pwd: "Admin@123",
        roles: [{role: "readWrite", db: "notification"}]
    }
);

db = new Mongo().getDB("notification");

db.createCollection('notification', {capped: false});