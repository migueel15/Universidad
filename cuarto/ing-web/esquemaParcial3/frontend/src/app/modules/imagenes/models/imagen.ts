const mongoose = require('mongoose');

const ImagenSchema = new mongoose.Schema({
  nombre: {
    type: String,
    required: true,
  },
  url: {
    type: String,
    required: true,
  },
});

const Imagen = mongoose.model('Imagen', ImagenSchema);
