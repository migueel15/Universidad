const router = require("express").Router();
const peliculasController = require("../controllers/peliculas");

router.get("/", peliculasController.listarPeliculas);

module.exports = router;
