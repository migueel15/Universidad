const Sequelize = require("sequelize");
const SQLite = require("sqlite3")

const config = require('./config.json');

const db = new Sequelize('database', 'username', 'password', {
    dialect: config.dialect,
    storage: config.storage, // or ':memory:'
    dialectOptions: {
        // Your sqlite3 options here
        // for instance, this is how you can configure the database opening mode:
        mode: SQLite.OPEN_READWRITE | SQLite.OPEN_CREATE | SQLite.OPEN_FULLMUTEX,
    },
});

module.exports = db;
