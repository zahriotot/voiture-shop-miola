package org.cours.springdatarest1.web;

import org.cours.springdatarest1.modele.Voiture;
import org.cours.springdatarest1.modele.VoitureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/ia")
public class RecommandationController {

    @Autowired
    private VoitureRepo voitureRepo;

    /**
     * IA basée sur la similarité de prix.
     * Recommande des voitures avec un prix proche (±20%)
     * GET /api/ia/recommander?prixMax=100000&couleur=Rouge
     */
    @GetMapping("/recommander")
    public List<Voiture> recommander(
            @RequestParam(required = false) Integer prixMax,
            @RequestParam(required = false) String couleur) {

        Iterable<Voiture> toutes = voitureRepo.findAll();
        List<Voiture> liste = StreamSupport
                .stream(toutes.spliterator(), false)
                .collect(Collectors.toList());

        // Filtrer par prix si fourni
        if (prixMax != null) {
            liste = liste.stream()
                    .filter(v -> v.getPrix() <= prixMax)
                    .collect(Collectors.toList());
        }

        // Filtrer par couleur si fournie
        if (couleur != null && !couleur.isEmpty()) {
            List<Voiture> parc = liste.stream()
                    .filter(v -> v.getCouleur().equalsIgnoreCase(couleur))
                    .collect(Collectors.toList());
            // Si aucune voiture de cette couleur : retourner les 3 moins chères
            if (!parc.isEmpty()) liste = parc;
        }

        // Trier par prix croissant et retourner les 3 premières
        return liste.stream()
                .sorted(Comparator.comparingInt(Voiture::getPrix))
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * IA de scoring : classe les voitures par rapport qualité/prix
     * GET /api/ia/scoring
     */
    @GetMapping("/scoring")
    public List<Map<String, Object>> scoring() {
        Iterable<Voiture> toutes = voitureRepo.findAll();
        List<Map<String, Object>> resultats = new ArrayList<>();

        for (Voiture v : toutes) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("voiture", v.getMarque() + " " + v.getModele());
            item.put("annee", v.getAnnee());
            item.put("prix", v.getPrix());

            // Score IA : plus la voiture est récente et moins chère, meilleur le score
            int anneeActuelle = 2025;
            double ageScore  = Math.max(0, 10 - (anneeActuelle - v.getAnnee()));
            double prixScore = Math.max(0, 10 - (v.getPrix() / 20000.0));
            double scoreTotal = (ageScore * 0.6 + prixScore * 0.4);

            item.put("score_ia", Math.round(scoreTotal * 10.0) / 10.0);
            item.put("recommandation", scoreTotal >= 7 ? "⭐ Excellent choix"
                    : scoreTotal >= 4 ? "✅ Bon choix"
                    :                   "⚠️ À considérer");
            resultats.add(item);
        }

        // Trier par score décroissant
        resultats.sort((a, b) -> Double.compare(
                (Double) b.get("score_ia"), (Double) a.get("score_ia")));
        return resultats;
    }
}