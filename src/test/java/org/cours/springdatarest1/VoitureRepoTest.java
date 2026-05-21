package org.cours.springdatarest1;

import org.cours.springdatarest1.modele.Voiture;
import org.cours.springdatarest1.modele.VoitureRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class VoitureRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VoitureRepo voitureRepo;

    @Test
    public void ajouterVoiture() {
        Voiture v = new Voiture("MiolaCar", "Uber", "Blanche", "M-2020", 2021, 180000);
        entityManager.persistAndFlush(v);
        assertThat(v.getId()).isNotNull();
    }

    @Test
    public void supprimerVoiture() {
        entityManager.persistAndFlush(
                new Voiture("MiolaCar", "Uber", "Blanche", "M-2020", 2021, 180000));
        entityManager.persistAndFlush(
                new Voiture("MiniCooper", "Uber", "Rouge", "C-2020", 2021, 180000));
        voitureRepo.deleteAll();
        assertThat(voitureRepo.findAll()).isEmpty();
    }
}