module.exports = (app) => {
  const patientController = require("../controllers/patient.controller");

  // Routes pour les patients
  app.post("/api/patients", patientController.create); // Créer un patient
  app.get("/api/patients", patientController.findAll); // Obtenir tous les patients
  app.get("/api/patients/:patientId", patientController.findById); // Obtenir un patient par ID
  app.put("/api/patients/:patientId", patientController.update); // Mettre à jour un patient
  app.delete("/api/patients/:patientId", patientController.delete); // Supprimer un patient

  // Routes pour les médicaments d'un patient
  app.get("/api/patients/:patientId/medicaments", patientController.getPatientMedicaments); // Obtenir les médicaments d'un patient
  app.post("/api/patients/:patientId/medicaments", patientController.addMedicament); // Ajouter un médicament
  app.put("/api/patients/:patientId/medicaments/:medicamentName", patientController.updateMedicament); // Mettre à jour un médicament
  app.delete("/api/patients/:patientId/medicaments/:medicamentName", patientController.deleteMedicament); // Supprimer un médicament

  // Route pour obtenir tous les médicaments de tous les patients
  app.get("/api/medicaments", patientController.getAllMedicaments);
  app.post("/api/patients/auth", patientController.authenticate);
  // Obtenir tous les médicaments
  app.get("/api/patients/email/:email", patientController.findByEmail); // Obtenir un patient par email

  app.get("/api/patients/:email/medicaments", patientController.getMedicamentsByEmail);

};
