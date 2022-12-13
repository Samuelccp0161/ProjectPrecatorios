package br.gov.al.sefaz.tributario.pdfhandler;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PdfDITest {
    final File fileDI1 = new File("src/test/resources/pdfs/DI_1.pdf");
    final File fileDI2 = new File("src/test/resources/pdfs/DI_2.pdf");
    final File fileDI3 = new File("src/test/resources/pdfs/DI_3.pdf");

    final File fileDMI = new File("src/test/resources/pdfs/DMI_1.pdf");


    @Test
    public void lerDI1() throws IOException {
        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "7043,00"),
                new StrPair("valSeguro", "0,00"),
                new StrPair("valVMLE", "49600,27"),
        };

        PDF di = PDF.di(fileDI1);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }

    @Test
    public void lerDI2() throws IOException {
        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "5750,00"),
                new StrPair("valSeguro", "0,00"),
                new StrPair("valVMLE", "116400,00"),
        };
        PDF di = PDF.di(fileDI2);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }

    @Test
    public void lerDI3() throws IOException {
        StrPair[] valoresEsperados = {
                new StrPair("valFrete", "7709,00"),
                new StrPair("valSeguro", "152,34"),
                new StrPair("valVMLE", "60935,93"),
        };
        PDF di = PDF.di(fileDI3);
        Map<String, String> diTabela = di.getTabela();

        assertThat(diTabela).containsOnly(valoresEsperados);
    }

    @Test
    public void validarDIComArquivoValido() throws IOException {
        PDF di = PDF.di(fileDI1);
        assertThat(di.isValido()).isTrue();
    }

    @Test
    public void validarDIComArquivoInvalido() throws IOException {
        PDF di = PDF.di(fileDMI);
        assertThat(di.isValido()).isFalse();
    }
}