const Sequelize = require('sequelize');
const db = require("../config/database");
module.exports = function(sequelize, DataTypes) {
  return db.define('PURCHASE_ORDER', {
    ORDER_NUM: {
      autoIncrement: true,
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    CUSTOMER_ID: {
      type: DataTypes.INTEGER,
      allowNull: false
    },
    PRODUCT_ID: {
      type: DataTypes.INTEGER,
      allowNull: false
    },
    QUANTITY: {
      type: DataTypes.SMALLINT,
      allowNull: true
    },
    SHIPPING_COST: {
      type: DataTypes.DECIMAL(12,2),
      allowNull: true
    },
    SALES_DATE: {
      type: DataTypes.DATEONLY,
      allowNull: true
    },
    SHIPPING_DATE: {
      type: DataTypes.DATEONLY,
      allowNull: true
    },
    FREIGHT_COMPANY: {
      type: DataTypes.STRING(30),
      allowNull: true
    }
  }, {
    sequelize,
    tableName: 'PURCHASE_ORDER',
    timestamps: false,
    indexes: [
      {
        name: "PRIMARY",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "ORDER_NUM" },
        ]
      },
    ]
  });
};
