const init = require("../models/init-models");
const sequelize = require("sequelize");
const { Op } = require("sequelize");

const db = init(sequelize);

const controller = {};

controller.getMarcas = async (req, res, next) => {
  try {
    const selectedMarcaId = req.query.selectedMarcaId;
    const selectedVehiculos = req.query.selectedVehiculos;

    console.log(selectedVehiculos);

    const marcas = await db.marca.findAll();

    let vehiculos;
    let servicios;

    if (selectedMarcaId) {
      vehiculos = await db.vehiculo.findAll({
        where: {
          id_marca: selectedMarcaId,
        },
      });
    }

    if (selectedVehiculos) {
      servicios = await db.servicio.findAll({
        include: [
          {
            model: db.vehiculo,
            as: "id_vehiculo_vehiculo",
          },
        ],
        where: {
          id_vehiculo: { [Op.in]: selectedVehiculos },
        },
      });
    }

    res.render("selector_marcas", {
      marcas,
      selectedMarcaId,
      vehiculos,
      servicios,
    });
  } catch (error) {
    res.send("Error al listar el filtro");
  }
};

controller.infoMarca = async (req, res, next) => {
  const selectedMarca = req.params.id;
  const marca = await db.marca.findOne({
    where: {
      id_marca: selectedMarca,
    },
  });
  res.render("info_marca", { marca });
};

controller.editarVehiculo = async (req, res, next) => {
  try {
    const id = req.params.id;
    const vehiculo = await db.vehiculo.findOne({
      where: {
        id_vehiculo: id,
      },
    });
    const propietarios = await db.propietario.findAll();

    const propietariosDelVehiculo = await db.vehiculo_propietario.findAll({
      where: {
        id_vehiculo: vehiculo.id_vehiculo,
      },
    });

    const idPropietariosDelVehiculo = propietariosDelVehiculo.map(
      (vp) => vp.id_propietario,
    );

    res.render("editar_vehiculo", {
      vehiculo,
      propietarios,
      idPropietariosDelVehiculo,
    });
  } catch (error) {
    res.send("Error al encontrar el vehiculo");
  }
};

controller.guardar = async (req, res, next) => {
  try {
    const { id, modelo, year, propietarios } = req.body;
    console.log(id, modelo, year, propietarios);

    await db.vehiculo.update(
      {
        modelo: modelo,
        anio: year,
      },
      {
        where: {
          id_vehiculo: id,
        },
      },
    );

    await db.vehiculo_propietario.destroy({
      where: {
        id_vehiculo: id,
      },
    });

    if (propietarios) {
      const arrayPropietarios = [...propietarios];
      console.log(arrayPropietarios);
      const nuevosPropietarios = arrayPropietarios.map((p) => {
        return {
          id_vehiculo: id,
          id_propietario: p,
        };
      });

      await db.vehiculo_propietario.bulkCreate(nuevosPropietarios);
      console.log(nuevosPropietarios);
    }
    res.redirect("/editar/" + id);
  } catch (error) {
    res.send("Error al guardar");
  }
};

module.exports = controller;

