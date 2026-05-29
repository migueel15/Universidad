const Sequelize = require('sequelize');
const db = require("../config/database");
module.exports = function(sequelize, DataTypes) {
  return db.define('PRODUCT_CODE', {
    PROD_CODE: {
      type: DataTypes.CHAR(2),
      allowNull: false,
      primaryKey: true
    },
    DISCOUNT_CODE: {
      type: DataTypes.CHAR(1),
      allowNull: false
    },
    DESCRIPTION: {
      type: DataTypes.STRING(10),
      allowNull: true
    }
  }, {
    sequelize,
    tableName: 'PRODUCT_CODE',
    timestamps: false,
    indexes: [
      {
        name: "PRIMARY",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "PROD_CODE" },
        ]
      },
    ]
  });
};
