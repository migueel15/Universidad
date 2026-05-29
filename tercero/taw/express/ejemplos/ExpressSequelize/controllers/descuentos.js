const initModels = require("../models/init-models");
const sequelize = require("sequelize");
const models = initModels(sequelize);

const controller = {};


// Listas todos ////////////////////////////////////////////////////////////////////////////////////////////////////////
controller.listarTodos = async function (req, res, next) {
    try {
        await models.DISCOUNT_CODE
            .findAll()
            .then(async (descuentos) => {
            //res.json(supermercados);
            res.render("index", {descuentos: descuentos});
        });
    } catch (error) {
        res.send("Se ha producido un error " + error);
    }
};
// Listas todos ////////////////////////////////////////////////////////////////////////////////////////////////////////
controller.buscarUno = async function (req, res, next) {
    try {
        await models.DISCOUNT_CODE
            .findOne({
                where: {
                    DISCOUNT_CODE: req.params.id
                }
            })
            .then((data) => {
                if (data != null) {
                    //res.json(data);
                    res.render("cliente", {descuento: data});

                } else {
                    res.send("Value not found");
                }
            });
    } catch (error) {
        res.send("Se ha producido un error " + error);
    }
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

module.exports = controller;