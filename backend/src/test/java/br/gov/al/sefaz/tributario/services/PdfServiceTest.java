package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.pdfhandler.exception.PdfInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

class PdfServiceTest {
    PdfService pdfService;

    @BeforeEach
    void setUp() {
        pdfService = new PdfService();
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
    void deveriaCopiarUmArquivoParaPastaRoot() throws IOException {
        final String filename = "File.txt";
        final String fileContent = "conteúdo do arquivo";

        File savedFile = pdfService.getRootDir().resolve(filename).toFile();

        MockMultipartFile mockFile = new MockMultipartFile("file", fileContent.getBytes());

        assertThat(savedFile).doesNotExist();
        pdfService.saveFile(filename, mockFile);
        assertThat(savedFile).hasContent(fileContent);
    }

    @Test
    void deveriaJogarExceptionAoReceberArquivoNaoPdf() {
        MockMultipartFile mockFile = new MockMultipartFile("file", "pdf Invalido".getBytes());
        assertThatThrownBy(() -> pdfService.saveDiFile(mockFile))
                .isInstanceOf(PdfInvalidoException.class)
                .hasMessageContaining("Arquivo inválido!");
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
                    "numDI", "2216298998",
                    "dataDMI", dataAtual()
            );

            try (FileInputStream fileStream = new FileInputStream(diValido)) {
                MockMultipartFile mockFile = new MockMultipartFile("di", fileStream);

                pdfService.saveDiFile(mockFile);
                assertThat(pdfService.extrairDi()).containsExactlyInAnyOrderEntriesOf(dados);
            }
        }

        private String dataAtual() {
            LocalDate date = LocalDate.now();

            DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("ddMMuuuu");

            return formatterData.format(date);
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
                assertThat(pdfService.extrairDmi()).containsExactlyInAnyOrderEntriesOf(dados);
            }
        }
    }
}