package br.gov.al.sefaz.precatorio.pdfhandler;

import br.gov.al.sefaz.precatorio.exception.PdfInvalidoException;
import br.gov.al.sefaz.precatorio.pdfhandler.util.Area;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

class PdfDMITest {
    private final Path caminhoArquivoValido = Path.of("src/test/resources/pdfs/DMI_1.pdf");


    @Nested @DisplayName("Ao instanciar")
    class AoInstanciar {

        @Test @DisplayName("deveria extrair a localizacao da tabela esquerda")
        void deveriaExtrairLocalizacaoDaTabelaEsquerda() {
            PdfDMI dmi = new PdfDMI(caminhoArquivoValido);
            Area area = dmi.getAreaTabelaEsquerda();

            var offset = Offset.offset(0.5f);

            assertThat(area).isNotNull();
            assertThat(area.getX()).isCloseTo(0f, offset);
            assertThat(area.getY()).isCloseTo(475f, offset);
            assertThat(area.getWidth() ).isCloseTo(284f, offset);
            assertThat(area.getHeight()).isCloseTo(127f, offset);
        }

        @Test @DisplayName("deveria extrair a localizacao da tabela direita")
        void deveriaExtrairLocalizacaoDaTabelaDireita() {
            PdfDMI dmi = new PdfDMI(caminhoArquivoValido);
            Area area = dmi.getAreaTabelaDireita();

            var offset = Offset.offset(0.5f);

            assertThat(area).isNotNull();
            assertThat(area.getX()).isCloseTo(284f, offset);
            assertThat(area.getY()).isCloseTo(475f, offset);
            assertThat(area.getWidth() ).isCloseTo(311f, offset);
            assertThat(area.getHeight()).isCloseTo(194f, offset);
        }

        @Test @DisplayName("com arquivo inexistente deveria jogar excessao")
        void comArquivoInexistenteDeveriaJogarExcessao() {
            Path caminhoInexistente = Path.of("src/test/resources/pdfs/inexistente.pdf");
            assertThat(caminhoInexistente).doesNotExist();

            assertThatThrownBy(() -> new PdfDMI(caminhoInexistente))
                    .isInstanceOf(PdfInvalidoException.class)
                    .hasMessage("Arquivo inexistente!");
        }

        @Test @DisplayName("com arquivo nao DMI deveria jogar excessao")
        void comArquivoNaoDmiDeveriaJogarExcessao() {
            Path arquivoInvalido = Path.of("src/test/resources/pdfs/DI_1.pdf");

            assertThatThrownBy(() -> new PdfDMI(arquivoInvalido))
                    .isInstanceOf(PdfInvalidoException.class)
                    .hasMessage("O Arquivo enviado não é um 'DMI' válido");
        }
    }

    @Nested @DisplayName("Ao extrair")
    class AoExtrair {
        @Test @DisplayName("deveria retornar um map com os dados")
        void deveriaRetornarUmMapComOsDados() {
            PdfDMI dmi = new PdfDMI(caminhoArquivoValido);
            var dadosExtraidos = dmi.extrairDados();

            assertThat(dadosExtraidos).containsOnly(
                    entry("valMoedaEstrangeira", "5,1709"),
                    entry("valTotNotaFiscalSaida", "433699,77"),
                    entry("valImpostoImportacao", "42296,98"),
                    entry("valIPI", "32601,56"),
                    entry("valPIS", "6150,83"),
                    entry("valCOFINS", "28264,52"),
                    entry("valOutrasTaxas", "13075,77"),
                    entry("valCapatazia", "0,00"),
                    entry("valAliquota", "4")
            );
        }
    }

    @DisplayName("Teste exaustivo")
    @ParameterizedTest
    @CsvFileSource(
            files = "src/test/resources/dmi_dados_extraidos.csv",
            delimiterString = "|",
            numLinesToSkip = 1
    )
    void testeExaustivo(
            String arquivo,
            String moedaEstrangeira,
            String totNotaFiscalSaida,
            String impostoImportacao,
            String ipi,
            String pis,
            String cofins,
            String outrasTaxas,
            String capatazia,
            String aliquota
    ) {
        PdfDMI dmi = new PdfDMI(Path.of(arquivo));
        var dadosExtraidos = dmi.extrairDados();

        assertThat(dadosExtraidos).containsOnly(
                entry("valTotNotaFiscalSaida", totNotaFiscalSaida),
                entry("valImpostoImportacao", impostoImportacao),
                entry("valIPI", ipi),
                entry("valPIS", pis),
                entry("valCOFINS", cofins),
                entry("valOutrasTaxas", outrasTaxas),
                entry("valMoedaEstrangeira", moedaEstrangeira),
                entry("valCapatazia", capatazia),
                entry("valAliquota", aliquota)
        );
    }
}
