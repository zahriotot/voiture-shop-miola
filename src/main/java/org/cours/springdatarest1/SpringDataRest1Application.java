package org.cours.springdatarest1;

import org.cours.springdatarest1.modele.Proprietaire;
import org.cours.springdatarest1.modele.ProprietaireRepo;
import org.cours.springdatarest1.modele.Voiture;
import org.cours.springdatarest1.modele.VoitureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDataRest1Application {

    @Autowired private VoitureRepo voitureRepo;
    @Autowired private ProprietaireRepo proprietaireRepo;

    public static void main(String[] args) {
        SpringApplication.run(SpringDataRest1Application.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            Proprietaire p1 = new Proprietaire("Ali", "Hassan");
            Proprietaire p2 = new Proprietaire("Najat", "Bani");
            proprietaireRepo.save(p1);
            proprietaireRepo.save(p2);

            Voiture v1 = new Voiture("Toyota", "Corolla", "Grise",  "A-1-9090", 2018, 95000);
            Voiture v2 = new Voiture("Ford",   "Fiesta",  "Rouge",  "A-2-8090", 2015, 90000);
            Voiture v3 = new Voiture("Honda",  "CRV",     "Bleu",   "A-3-7090", 2016, 140000);
            v1.setProprietaire(p1);
            v2.setProprietaire(p1);
            v3.setProprietaire(p2);
            voitureRepo.save(v1);
            voitureRepo.save(v2);
            voitureRepo.save(v3);
        };
    }
}