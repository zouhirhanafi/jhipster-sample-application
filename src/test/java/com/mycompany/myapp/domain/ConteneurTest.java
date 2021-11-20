package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConteneurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conteneur.class);
        Conteneur conteneur1 = new Conteneur();
        conteneur1.setId(1L);
        Conteneur conteneur2 = new Conteneur();
        conteneur2.setId(conteneur1.getId());
        assertThat(conteneur1).isEqualTo(conteneur2);
        conteneur2.setId(2L);
        assertThat(conteneur1).isNotEqualTo(conteneur2);
        conteneur1.setId(null);
        assertThat(conteneur1).isNotEqualTo(conteneur2);
    }
}
