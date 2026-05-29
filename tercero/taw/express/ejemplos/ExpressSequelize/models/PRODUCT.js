const Sequelize = require('sequelize');
const db = require("../config/database");
module.exports = function(sequelize, DataTypes) {
  return db.define('PRODUCT', {
    PRODUCT_ID: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    MANUFACTURER_ID: {
      type: DataTypes.INTEGER,
      allowNull: false
    },
    PRODUCT_CODE: {
      type: DataTypes.CHAR(2),
      allowNull: false
    },
    PURCHASE_COST: {
      type: DataTypes.DECIMAL(12,2),
      allowNull: true
    },
    QUANTITY_ON_HAND: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    MARKUP: {
      type: DataTypes.DECIMAL(4,2),
      allowNull: true
    },
    AVAILABLE: {
      type: DataTypes.STRING(5),
      allowNull: true
    },
    DESCRIPTION: {
      type: DataTypes.STRING(50),
      allowNull: true
    }
  }, {
    sequelize,
    tableName: 'PRODUCT',
    timestamps: false,
    indexes: [
      {
        name: "PRIMARY",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "PRODUCT_ID" },
        ]
      },
    ]
  });
};
