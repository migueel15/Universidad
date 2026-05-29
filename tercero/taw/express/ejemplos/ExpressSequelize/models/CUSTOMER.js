const Sequelize = require('sequelize');
const db = require("../config/database");
module.exports = function(sequelize, DataTypes) {
  return db.define('CUSTOMER', {
    CUSTOMER_ID: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    DISCOUNT_CODE: {
      type: DataTypes.CHAR(1),
      allowNull: false
    },
    ZIP: {
      type: DataTypes.STRING(10),
      allowNull: false
    },
    NAME: {
      type: DataTypes.STRING(30),
      allowNull: true
    },
    ADDRESSLINE1: {
      type: DataTypes.STRING(30),
      allowNull: true
    },
    ADDRESSLINE2: {
      type: DataTypes.STRING(30),
      allowNull: true
    },
    CITY: {
      type: DataTypes.STRING(25),
      allowNull: true
    },
    STATE: {
      type: DataTypes.CHAR(2),
      allowNull: true
    },
    PHONE: {
      type: DataTypes.CHAR(12),
      allowNull: true
    },
    FAX: {
      type: DataTypes.CHAR(12),
      allowNull: true
    },
    EMAIL: {
      type: DataTypes.STRING(40),
      allowNull: true
    },
    CREDIT_LIMIT: {
      type: DataTypes.INTEGER,
      allowNull: true
    }
  }, {
    sequelize,
    tableName: 'CUSTOMER',
    timestamps: false,
    indexes: [
      {
        name: "PRIMARY",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "CUSTOMER_ID" },
        ]
      },
    ]
  });
};
