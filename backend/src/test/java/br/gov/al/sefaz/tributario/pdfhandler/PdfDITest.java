package br.gov.al.sefaz.tributario.pdfhandler;

import br.gov.al.sefaz.tributario.exception.PdfInvalidoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PdfDITest {
    static String data;
    @BeforeAll
    static void beforeAll() {

        LocalDate date = LocalDate.now();

        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("ddMMuuuu");

        data = formatterData.format(date);
    }

    @Test
    public void lerDI1() throws IOException {
        final File fileDI1 = new File("src/test/resources/pdfs/DI_1.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "7043,00"),
                new StrPair("valSeguro", "0,00"),
                new StrPair("valVMLE", "49600,27"),
                new StrPair("numDI", "2216298998"),
                new StrPair("dataDMI", data),
        };

        PDF di = PDF.di(fileDI1);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDI2() throws IOException {
        final File fileDI2 = new File("src/test/resources/pdfs/DI_2.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "5750,00"),
                new StrPair("valSeguro", "0,00"),
                new StrPair("valVMLE", "116400,00"),
                new StrPair("numDI", "2219578444"),
                new StrPair("dataDMI", data),
        };
        PDF di = PDF.di(fileDI2);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDI3() throws IOException {
        final File fileDI3 = new File("src/test/resources/pdfs/DI_3.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "7709,00"),
                new StrPair("valSeguro", "152,34"),
                new StrPair("valVMLE", "60935,93"),
                new StrPair("numDI", "2219077631"),
                new StrPair("dataDMI", data),
        };
        PDF di = PDF.di(fileDI3);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDI4() throws IOException {
        final File fileDI4 = new File("src/test/resources/pdfs/DI_4.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "10830,00"),
                new StrPair("valSeguro", "165,23"),
                new StrPair("valVMLE", "42660,00"),
                new StrPair("numDI", "2223920550"),
                new StrPair("dataDMI", data),
        };
        PDF di = PDF.di(fileDI4);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDI5() throws IOException {
        final File fileDI5 = new File("src/test/resources/pdfs/DI_5.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "6756,00"),
                new StrPair("valSeguro", "0,00"),
                new StrPair("valVMLE", "40301,25"),
                new StrPair("numDI", "2224936795"),
                new StrPair("dataDMI", data),
        };
        PDF di = PDF.di(fileDI5);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDI6() throws IOException {
        final File fileDI6 = new File("src/test/resources/pdfs/DI_6.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "4520,00"),
                new StrPair("valSeguro", "0,00"),
                new StrPair("valVMLE", "40883,56"),
                new StrPair("numDI", "2222884836"),
                new StrPair("dataDMI", data),
        };
        PDF di = PDF.di(fileDI6);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDI7() throws IOException {
        final File fileDI7 = new File("src/test/resources/pdfs/DI_7.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "2000,00"),
                new StrPair("valSeguro", "0,00"),
                new StrPair("valVMLE", "37800,00"),
                new StrPair("numDI", "2225373468"),
                new StrPair("dataDMI", data),
        };
        PDF di = PDF.di(fileDI7);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDI8() throws IOException {
        final File fileDI8 = new File("src/test/resources/pdfs/DI_8.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "11200,00"),
                new StrPair("valSeguro", "200,00"),
                new StrPair("valVMLE", "68600,00"),
                new StrPair("numDI", "2224454995"),
                new StrPair("dataDMI", data),
        };
        PDF di = PDF.di(fileDI8);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDI9() throws IOException {
        final File fileDI9 = new File("src/test/resources/pdfs/DI_9.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "491,40"),
                new StrPair("valSeguro", "0,00"),
                new StrPair("valVMLE", "290,09"),
                new StrPair("numDI", "2225206340"),
                new StrPair("dataDMI", data),
        };
        PDF di = PDF.di(fileDI9);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }

    @Test
    public void lerDI11() throws IOException {
        final File fileDI11 = new File("src/test/resources/pdfs/DI_11.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "5500,00"),
                new StrPair("valSeguro", "0,00"),
                new StrPair("valVMLE", "31182,42"),
                new StrPair("numDI", "2222884178"),
                new StrPair("dataDMI", data),
        };
        PDF di = PDF.di(fileDI11);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDI12() throws IOException {
        final File fileDI12 = new File("src/test/resources/pdfs/DI_12.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "789,00"),
                new StrPair("valSeguro", "0,00"),
                new StrPair("valVMLE", "1910,99"),
                new StrPair("numDI", "2224972821"),
                new StrPair("dataDMI", data),
        };
        PDF di = PDF.di(fileDI12);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }

    @Test
    public void lerDI14() throws IOException {
        final File fileDI14 = new File("src/test/resources/pdfs/DI_14.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "4100,00"),
                new StrPair("valSeguro", "0,00"),
                new StrPair("valVMLE", "33183,00"),
                new StrPair("numDI", "2224432185"),
                new StrPair("dataDMI", data),
        };
        PDF di = PDF.di(fileDI14);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDI15() throws IOException {
        final File fileDI15 = new File("src/test/resources/pdfs/DI_15.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "4900,00"),
                new StrPair("valSeguro", "0,00"),
                new StrPair("valVMLE", "24311,29"),
                new StrPair("numDI", "2224121651"),
                new StrPair("dataDMI", data),
        };
        PDF di = PDF.di(fileDI15);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDI16() throws IOException {
        final File fileDI16 = new File("src/test/resources/pdfs/DI_16.pdf");

        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "2000,00"),
                new StrPair("valSeguro", "0,00"),
                new StrPair("valVMLE", "37800,00"),
                new StrPair("numDI", "2225373948"),
                new StrPair("dataDMI", data),
        };
        PDF di = PDF.di(fileDI16);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }

    @Test
    public void validarDIComArquivoInvalido() {
        final File DINaoValido = new File("src/test/resources/pdfs/DMI_1.pdf");

        assertThatThrownBy(() -> PDF.di(DINaoValido))
                .isInstanceOf(PdfInvalidoException.class)
                .hasMessageContaining("O Arquivo enviado não é um 'DI' válido");
    }
}
