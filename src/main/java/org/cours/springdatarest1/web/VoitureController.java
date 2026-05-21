package org.cours.springdatarest1.web;

import org.cours.springdatarest1.modele.Voiture;
import org.cours.springdatarest1.modele.VoitureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")  // autorise React (port 3000)
public class VoitureController {

    @Autowired
    private VoitureRepo voitureRepo;

    // ── GET ALL ──────────────────────────────────────────────────────
    @GetMapping("/voitures")
    public Iterable<Voiture> getVoitures() {
        return voitureRepo.findAll();
    }

    // ── GET BY ID ────────────────────────────────────────────────────
    @GetMapping("/voitures/{id}")
    public Optional<Voiture> getVoitureById(@PathVariable Long id) {
        return voitureRepo.findById(id);
    }

    // ── POST (Create) ────────────────────────────────────────────────
    @PostMapping("/voitures")
    public Voiture addVoiture(@RequestBody Voiture voiture) {
        return voitureRepo.save(voiture);
    }

    // ── PUT (Update complet) ─────────────────────────────────────────
    @PutMapping("/voitures/{id}")
    public Voiture updateVoiture(@PathVariable Long id, @RequestBody Voiture voiture) {
        voiture.setId(id);
        return voitureRepo.save(voiture);
    }

    // ── DELETE ───────────────────────────────────────────────────────
    @DeleteMapping("/voitures/{id}")
    public Boolean deleteVoiture(@PathVariable Long id) {
        voitureRepo.deleteById(id);
        return true;
    }
}