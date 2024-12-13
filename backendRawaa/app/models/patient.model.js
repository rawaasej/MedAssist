const mongoose = require("mongoose");

const MedicamentsSchema = new mongoose.Schema({
  name: { 
    type: String, 
    required: true 
  },
  dosage: { type: String, required: true },
  frequency: { type: String, required: true },
  startDate: { type: Date, required: true },
  endDate: { type: Date, required: true }
}, { _id: false });

const PatientSchema = new mongoose.Schema({
  name: { type: String, required: true },
  email: { type: String, required: true, unique: true , }, // Email unique pour tous les patients
  mdp: { type: String, required: true },
  age: { type: Number, required: true },
  num: { type: Number, required: true },
  address: { type: String, required: true },
  medicaments: [MedicamentsSchema] 
}, { timestamps: true });

// Index pour garantir l'unicité de l'email dans toute la collection
PatientSchema.index({ email: 1 }, { unique: true });

module.exports = mongoose.model("Patient", PatientSchema);
