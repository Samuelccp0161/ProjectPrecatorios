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

class PdfDITest {
    private final Path caminhoArquivoValido = Path.of("src/test/resources/pdfs/DI_1.pdf");


    @Nested @DisplayName("Ao instanciar")
    class AoInstanciar {

        @Test @DisplayName("deveria extrair o numero da di")
        void deveriaExtrairONumeroDaDi() {
            PdfDI di = new PdfDI(caminhoArquivoValido);
            assertThat(di.getNumeroDi()).isEqualTo("2216298998");
        }

        @Test @DisplayName("deveria extrair a localizacao da tabela")
        void deveriaExtrairLocalizacaoDaTabela() {
            PdfDI di = new PdfDI(caminhoArquivoValido);
            Area area = di.getAreaTabela();

            var offset = Offset.offset(0.5f);

            assertThat(area.getX()).isCloseTo(0f, offset);
            assertThat(area.getY()).isCloseTo(344f, offset);
            assertThat(area.getWidth() ).isCloseTo(595f, offset);
            assertThat(area.getHeight()).isCloseTo(88f, offset);
        }

        @Test @DisplayName("com arquivo inexistente deveria jogar excessao")
        void comArquivoInexistenteDeveriaJogarExcessao() {
            Path caminhoInexistente = Path.of("src/test/resources/pdfs/inexistente.pdf");
            assertThat(caminhoInexistente).doesNotExist();

            assertThatThrownBy(() -> new PdfDI(caminhoInexistente))
                    .isInstanceOf(PdfInvalidoException.class)
                    .hasMessage("Arquivo inexistente!");
        }

        @Test @DisplayName("com arquivo nao DI deveria jogar excessao")
        void comArquivoNaoDiDeveriaJogarExcessao() {
            Path arquivoInvalido = Path.of("src/test/resources/pdfs/DMI_1.pdf");

            assertThatThrownBy(() -> new PdfDI(arquivoInvalido))
                    .isInstanceOf(PdfInvalidoException.class)
                    .hasMessage("O Arquivo enviado não é um 'DI' válido");
        }
    }

    @Nested @DisplayName("Ao extrair")
    class AoExtrair {
        @Test @DisplayName("deveria retornar um map com os dados")
        void deveriaRetornarUmMapComOsDados() {
            PdfDI di = new PdfDI(caminhoArquivoValido);
            var dadosExtraidos = di.extrairDados();

            assertThat(dadosExtraidos).containsOnly(
                    entry("valFrete", "7043,00"),
                    entry("valSeguro", "0,00"),
                    entry("valVMLE", "49600,27"),
                    entry("numDI", "2216298998")
            );
        }
    }

    @DisplayName("Teste exaustivo")
    @ParameterizedTest
    @CsvFileSource(
            files = "src/test/resources/di_dados_extraidos.csv",
            delimiterString = "|",
            numLinesToSkip = 1
    )
    void testeExaustivo(String arquivo, String frete, String seguro, String vmle, String numeroDI) {
        PdfDI di = new PdfDI(Path.of(arquivo));
        var dadosExtraidos = di.extrairDados();

        assertThat(dadosExtraidos).containsOnly(
                entry("valFrete", frete),
                entry("valSeguro", seguro),
                entry("valVMLE", vmle),
                entry("numDI", numeroDI)
        );
    }
}
