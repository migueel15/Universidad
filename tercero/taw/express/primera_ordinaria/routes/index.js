const router = require("express").Router();
const marcasController = require("../controllers/marcas");

router.get("/", marcasController.getMarcas);
router.get("/marca/:id", marcasController.infoMarca);
router.get("/editar/:id", marcasController.editarVehiculo);
router.post("/guardar", marcasController.guardar);

module.exports = router;
