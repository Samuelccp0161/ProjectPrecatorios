package br.gov.al.sefaz.precatorio.service.impl;

import br.gov.al.sefaz.precatorio.exception.PdfInvalidoException;
import br.gov.al.sefaz.precatorio.service.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PdfServiceImplTest {
    PdfServiceImpl pdfService;

    @BeforeEach
    void setUp() {
        pdfService = new PdfServiceImpl();
        pdfService.deleteRoot();
        pdfService.init();
    }

    @Test
    void deveriaInicializarDiretorioParaUpload() {
        Path rootDir = pdfService.getRootDir();

        pdfService.deleteRoot();
        assertThat(rootDir).doesNotExist();

        pdfService.init();
        assertThat(rootDir).isDirectory();
    }

    @Test
    void deveriaCopiarUmArquivoParaPastaRoot() {
        final String filename = "File.txt";
        final String fileContent = "conteúdo do arquivo";

        File savedFile = pdfService.getRootDir().resolve(filename).toFile();

        MockMultipartFile mockFile = new MockMultipartFile("file", fileContent.getBytes());

        assertThat(savedFile).doesNotExist();
        pdfService.saveFile(filename, mockFile);
        assertThat(savedFile).hasContent(fileContent);
    }

    @Nested
    class AoEnviarUmArquivoDI {

        private final File diValido = Path.of("src/test/resources/pdfs/DI_1.pdf").toFile();
        private final File diInvalido = Path.of("src/test/resources/pdfs/DMI_1.pdf").toFile();

        @Test
        void deveriaReceberESalvarOArquivoDi() throws IOException {
            File savedDi = pdfService.getRootDir().resolve(PdfService.FILENAME_DI).toFile();

            try (FileInputStream fileStream = new FileInputStream(diValido)) {
                MockMultipartFile mockFile = new MockMultipartFile("di", fileStream);

                pdfService.saveDiFile(mockFile);
                assertThat(savedDi).hasSameBinaryContentAs(diValido);
            }
        }

        @Test
        void deveriaJogarExceptionAoReceberArquivoDiInvalido() throws IOException {

            try (FileInputStream fileStream = new FileInputStream(diInvalido)) {
                MockMultipartFile mockFile = new MockMultipartFile("diInvalido", fileStream);

                assertThatThrownBy(() -> pdfService.saveDiFile(mockFile))
                        .isInstanceOf(PdfInvalidoException.class)
                        .hasMessageContaining("O Arquivo enviado não é um 'DI' válido");
            }
        }

        @Test
        void deveriaExtrairOsDadosDoArquivo() throws IOException {
            var dados = Map.of(
                    "valFrete", "7043,00",
                    "valSeguro", "0,00",
                    "valVMLE", "49600,27",
                    "numDI", "2216298998"
            );

            try (FileInputStream fileStream = new FileInputStream(diValido)) {
                MockMultipartFile mockFile = new MockMultipartFile("di", fileStream);

                pdfService.saveDiFile(mockFile);
                assertThat(pdfService.getDadosDI()).containsExactlyInAnyOrderEntriesOf(dados);
            }
        }
    }

    @Nested
    class AoEnviarUmArquivoDMI {

        private final File dmiValido = Path.of("src/test/resources/pdfs/DMI_1.pdf").toFile();
        private final File dmiInvalido = Path.of("src/test/resources/pdfs/DI_1.pdf").toFile();

        @Test
        void deveriaReceberESalvarOArquivoDi() throws IOException {
            File savedDmi = pdfService.getRootDir().resolve(PdfService.FILENAME_DMI).toFile();

            try (FileInputStream fileStream = new FileInputStream(dmiValido)) {
                MockMultipartFile mockFile = new MockMultipartFile("dmi", fileStream);

                pdfService.saveDmiFile(mockFile);
                assertThat(savedDmi).hasSameBinaryContentAs(dmiValido);
            }
        }

        @Test
        void deveriaJogarExceptionAoReceberArquivoDiInvalido() throws IOException {

            try (FileInputStream fileStream = new FileInputStream(dmiInvalido)) {
                MockMultipartFile mockFile = new MockMultipartFile("dmiInvalido", fileStream);

                assertThatThrownBy(() -> pdfService.saveDmiFile(mockFile))
                        .isInstanceOf(PdfInvalidoException.class)
                        .hasMessageContaining("O Arquivo enviado não é um 'DMI' válido");
            }
        }

        @Test
        void deveriaExtrairOsDadosDoArquivo() throws IOException {
            var dados = Map.of(
                    "valMoedaEstrangeira", "5,1709",
                    "valTotNotaFiscalSaida", "433699,77",
                    "valImpostoImportacao", "42296,98",
                    "valIPI", "32601,56",
                    "valPIS", "6150,83",
                    "valCOFINS", "28264,52",
                    "valOutrasTaxas", "13075,77",
                    "valCapatazia", "0,00",
                    "valAliquota", "4"
            );

            try (FileInputStream fileStream = new FileInputStream(dmiValido)) {
                MockMultipartFile mockFile = new MockMultipartFile("dmi", fileStream);

                pdfService.saveDmiFile(mockFile);
                assertThat(pdfService.getDadosDMI()).containsExactlyInAnyOrderEntriesOf(dados);
            }
        }
    }

    @Nested
    class AoEnviarUmArquivoBeneficiario {

        private final File arquivoValido = Path.of("src/test/resources/pdfs/beneficiario/quitacao_01.pdf").toFile();
        private final File arquivoInvalido = Path.of("src/test/resources/pdfs/DI_1.pdf").toFile();

        @Test
        void deveriaReceberESalvarOArquivo() throws IOException {
            File savedfile = pdfService.getRootDir().resolve(PdfService.FILENAME_BENEFICIARIO).toFile();

            try (FileInputStream fileStream = new FileInputStream(arquivoValido)) {
                MockMultipartFile mockFile = new MockMultipartFile("dmi", fileStream);

                pdfService.saveFileBeneficiario(mockFile);
                assertThat(savedfile).hasSameBinaryContentAs(arquivoValido);
            }
        }

        @Test
        void deveriaJogarExceptionAoReceberArquivoInvalido() throws IOException {

            try (FileInputStream fileStream = new FileInputStream(arquivoInvalido)) {
                MockMultipartFile mockFile = new MockMultipartFile("Invalido", fileStream);

                assertThatThrownBy(() -> pdfService.saveFileBeneficiario(mockFile))
                        .isInstanceOf(PdfInvalidoException.class)
                        .hasMessageContaining("O Arquivo enviado não é válido");
            }
        }

        @Test
        void deveriaExtrairOsDadosDoArquivo() throws IOException {
            var dados = getDados();

            try (FileInputStream fileStream = new FileInputStream(arquivoValido)) {
                MockMultipartFile mockFile = new MockMultipartFile("beneficiario", fileStream);

                pdfService.saveFileBeneficiario(mockFile);
                assertThat(pdfService.extrairDadosBeneficiario()).containsExactlyInAnyOrderEntriesOf(dados);
            }
        }

        private Map<String, String> getDados() {
            return new HashMap<>() {{
                    put("matricula", "3255");
                    put("nome", "ITAMAR ALVES LEITE");
                    put("cpf", "26020033449");
                    put("valFaceBruto", "41666,67");
                    put("valFaceHono", "8333,33");
                    put("valAcordoBruto", "12500,00");
                    put("ValAcordoHono", "2500,00");
                    put("ipaseal", "1375,00");
                    put("irpf", "2190,02");
            }};
        }
    }
}
