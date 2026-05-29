const Sequelize = require('sequelize');
const db = require("../config/database");
module.exports = function(sequelize, DataTypes) {
  return db.define('DISCOUNT_CODE', {
    DISCOUNT_CODE: {
      type: DataTypes.CHAR(1),
      allowNull: false,
      primaryKey: true
    },
    RATE: {
      type: DataTypes.DECIMAL(4,2),
      allowNull: true
    }
  }, {
    sequelize,
    tableName: 'DISCOUNT_CODE',
    timestamps: false,
    indexes: [
      {
        name: "PRIMARY",
        unique: true,
        using: "BTREE",
        fields: [
          { name: "DISCOUNT_CODE" },
        ]
      },
    ]
  });
};
