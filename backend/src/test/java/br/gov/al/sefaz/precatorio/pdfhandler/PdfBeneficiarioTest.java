package br.gov.al.sefaz.precatorio.pdfhandler;

import br.gov.al.sefaz.precatorio.exception.PdfInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

public class PdfBeneficiarioTest {

    @DisplayName("Teste exaustivo")
    @ParameterizedTest
    @CsvFileSource(
            files = "src/test/resources/quitacao_dados_extraidos.csv",
            delimiterString = "|",
            numLinesToSkip = 1
    )
    void testeExaustivo(String arquivo,
            String matricula,
            String nome,
            String cpf,
            String valFaceBruto,
            String valFaceHono,
            String valAcordoBruto,
            String ValAcordoHono,
            String ipaseal,
            String irpf
    ) {
        PdfBeneficiario beneficiario = new PdfBeneficiario(Path.of(arquivo));
        var dadosExtraidos = beneficiario.extrairDados();

        assertThat(dadosExtraidos).contains(
                entry("matricula", matricula),
                entry("nome", nome),
                entry("cpf", cpf),
                entry("valFaceBruto", valFaceBruto),
                entry("valFaceHono", valFaceHono),
                entry("valAcordoBruto", valAcordoBruto),
                entry("ValAcordoHono", ValAcordoHono),
                entry("ipaseal", ipaseal),
                entry("irpf", irpf)
        );
    }

    @Nested
    @DisplayName("Ao instanciar")
    class AoInstanciar {
        @Test
        @DisplayName("com arquivo inexistente deveria jogar excessao")
        void comArquivoInexistenteDeveriaJogarExcessao() {
            Path caminhoInexistente = Path.of("src/test/resources/pdfs/inexistente.pdf");
            assertThat(caminhoInexistente).doesNotExist();

            assertThatThrownBy(() -> new PdfBeneficiario(caminhoInexistente))
                    .isInstanceOf(PdfInvalidoException.class)
                    .hasMessage("Arquivo inexistente!");
        }

        @Test
        @DisplayName("com arquivo invalido deveria jogar excessao")
        void comArquivoNaoDiDeveriaJogarExcessao() {
            Path arquivoInvalido = Path.of("src/test/resources/pdfs/DMI_1.pdf");

            assertThatThrownBy(() -> new PdfBeneficiario(arquivoInvalido))
                    .isInstanceOf(PdfInvalidoException.class)
                    .hasMessage("O Arquivo enviado não é válido");
        }
    }
}
