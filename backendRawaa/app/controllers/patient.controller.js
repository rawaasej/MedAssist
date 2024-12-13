const Patient = require("../models/patient.model");

// Créer un patient
exports.create = (req, res) => {
  const patient = new Patient(req.body);
  patient.save()
    .then(data => res.status(201).json(data))
    .catch(err => res.status(500).json({ message: err.message }));
};

// Obtenir tous les patients
exports.findAll = (req, res) => {
  Patient.find()
    .then(patients => res.json(patients))
    .catch(err => res.status(500).json({ message: err.message }));
};

// Obtenir un patient par ID
exports.findById = (req, res) => {
  const { patientId } = req.params;
  Patient.findById(patientId)
    .then(patient => {
      if (!patient) {
        return res.status(404).json({ message: "Patient not found" });
      }
      res.json(patient);
    })
    .catch(err => res.status(500).json({ message: err.message }));
};

// Mettre à jour un patient
exports.update = (req, res) => {
  const { patientId } = req.params;
  Patient.findByIdAndUpdate(patientId, req.body, { new: true, runValidators: true })
    .then(patient => {
      if (!patient) {
        return res.status(404).json({ message: "Patient not found" });
      }
      res.json(patient);
    })
    .catch(err => res.status(500).json({ message: err.message }));
};

// Supprimer un patient
exports.delete = (req, res) => {
  const { patientId } = req.params;
  Patient.findByIdAndDelete(patientId)
    .then(patient => {
      if (!patient) {
        return res.status(404).json({ message: "Patient not found" });
      }
      res.json({ message: "Patient deleted successfully" });
    })
    .catch(err => res.status(500).json({ message: err.message }));
};

// Ajouter un médicament à un patient
// Dans votre patientController.js

exports.addMedicament = async (req, res) => {
  const { patientId } = req.params;
  const newMedicament = req.body;

  try {
    // Trouver le patient par son ID
    const patient = await Patient.findById(patientId);
    if (!patient) {
      return res.status(404).json({ message: "Patient not found" });
    }

    // Vérifier si un médicament avec le même nom existe déjà pour ce patient
    const existingMedicament = patient.medicaments.find(med => med.name === newMedicament.name);
    if (existingMedicament) {
      return res.status(400).json({ message: "Le médicament avec ce nom existe déjà pour ce patient" });
    }

    // Si le médicament n'existe pas, l'ajouter
    patient.medicaments.push(newMedicament);

    // Sauvegarder le patient avec le nouveau médicament
    await patient.save();
    res.status(201).json(newMedicament);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
};


// Obtenir les médicaments d'un patient
exports.getPatientMedicaments = async (req, res) => {
  const { patientId } = req.params;

  try {
    const patient = await Patient.findById(patientId);
    if (!patient) {
      return res.status(404).json({ message: "Patient not found" });
    }
    res.json(patient.medicaments);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
};

// Mettre à jour un médicament
exports.updateMedicament = async (req, res) => {
  const { patientId, medicamentName } = req.params;
  const updatedMedicament = req.body;

  try {
    // Rechercher le patient par ID
    const patient = await Patient.findById(patientId);
    if (!patient) {
      return res.status(404).json({ message: "Patient not found" });
    }

    // Rechercher le médicament dans le tableau
    const medicamentIndex = patient.medicaments.findIndex(
      med => med.name === medicamentName
    );

    if (medicamentIndex === -1) {
      return res.status(404).json({ message: "Medicament not found" });
    }

    // Mettre à jour les informations du médicament
    patient.medicaments[medicamentIndex] = {
      ...patient.medicaments[medicamentIndex],
      ...updatedMedicament
    };

    // Sauvegarder les modifications
    await patient.save();
    res.status(200).json(patient.medicaments[medicamentIndex]);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
};


// Supprimer un médicament d'un patient
exports.deleteMedicament = async (req, res) => {
  const { patientId, medicamentName } = req.params;

  try {
    const patient = await Patient.findById(patientId);
    if (!patient) {
      return res.status(404).json({ message: "Patient not found" });
    }

    const initialLength = patient.medicaments.length;
    patient.medicaments = patient.medicaments.filter(
      med => med.name !== medicamentName
    );

    if (patient.medicaments.length === initialLength) {
      return res.status(404).json({ message: "Medicament not found" });
    }

    await patient.save();
    res.status(200).json({ message: "Medicament deleted successfully" });
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
};

// Obtenir tous les médicaments de tous les patients
exports.getAllMedicaments = async (req, res) => {
  try {
    const patients = await Patient.find({}).select('medicaments'); // Sélectionne uniquement les médicaments des patients
    const allMedicaments = patients.flatMap(patient => patient.medicaments);
    res.json(allMedicaments);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
};




exports.authenticate = async (req, res) => {
  const { email, mdp } = req.body;

  try {
    // Vérifier si l'email existe dans la base de données
    const patient = await Patient.findOne({ email });

    if (!patient) {
      return res.status(401).json({ message: "Email ou mot de passe invalide" });
    }

    // Vérifier si le mot de passe correspond
    if (patient.mdp !== mdp) {
      return res.status(401).json({ message: "Email ou mot de passe invalide" });
    }

    // Succès : Retourner les informations du patient
    res.status(200).json({
      message: "Connexion réussie",
      patient: {
        _id: patient._id,
        name: patient.name,
        email: patient.email,
        age: patient.age,
        num: patient.num,
        address: patient.address,
        medicaments: patient.medicaments,
      },
    });
  } catch (error) {
    console.error("Erreur lors de l'authentification :", error);
    res.status(500).json({ message: "Erreur serveur" });
  }
};

// Trouver un patient par email
exports.findByEmail = async (req, res) => {
  const { email } = req.params;

  try {
    const patient = await Patient.findOne({ email });
    if (!patient) {
      return res.status(404).json({ message: "Patient not found" });
    }
    res.json(patient);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }

const mongoose = require('mongoose');}

exports.getMedicamentsByEmail = async (req, res) => {
  const { email } = req.params;

  if (typeof email !== 'string') {
    return res.status(400).json({ message: 'L\'email doit être une chaîne de caractères.' });
  }

  try {
    // Recherche du patient par email (en tant que chaîne de caractères)
    const patient = await Patient.findOne({ email });

    if (!patient) {
      return res.status(404).json({ message: "Patient not found" });
    }

    // Retourner les médicaments du patient
    res.json(patient.medicaments);
  } catch (err) {
    // Gestion des erreurs
    res.status(500).json({ message: err.message });
  }
};
;