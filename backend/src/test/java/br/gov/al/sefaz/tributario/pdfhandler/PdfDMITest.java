package br.gov.al.sefaz.tributario.pdfhandler;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PdfDMITest {
    final File fileDMI1 = new File("src/test/resources/pdfs/DMI_1.pdf");
    final File fileDMI2 = new File("src/test/resources/pdfs/DMI_2.pdf");
    final File fileDMI3 = new File("src/test/resources/pdfs/DMI_3.pdf");

    final File fileDI = new File("src/test/resources/pdfs/DI_1.pdf");


    @Test
    public void lerDMI1() throws IOException {
        StrPair[] paresEsperados =  {
                new StrPair(  "valMoedaEstrangeira", "5,1709"),
                new StrPair("valTotNotaFiscalSaida", "433699,77"),
                new StrPair( "valImpostoImportacao", "42296,98"),
                new StrPair(               "valIPI", "32601,56"),
                new StrPair(               "valPIS", "6150,83"),
                new StrPair(            "valCOFINS", "28264,52"),
                new StrPair(       "valOutrasTaxas", "13075,77"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4")
        };

        PDF dmi = PDF.dmi(fileDMI1);
        Map<String, String> dmiTabela = dmi.getTabela();
        assertThat(dmiTabela).containsOnly(paresEsperados);
    }

    @Test
    public void lerDMI2() throws IOException {
        StrPair[] valoresEsperados = {
            new StrPair(  "valMoedaEstrangeira", "5,1411"),
            new StrPair("valTotNotaFiscalSaida", "803286,46"),
            new StrPair( "valImpostoImportacao", ""),
            new StrPair(               "valIPI", "61228,57"),
            new StrPair(               "valPIS", "13187,69"),
            new StrPair(            "valCOFINS", "66880,44"),
            new StrPair(       "valOutrasTaxas", "1872,60"),
            new StrPair(         "valCapatazia", "0,00"),
            new StrPair(          "valAliquota", "4")
        };

        PDF dmi = PDF.dmi(fileDMI2);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }

    @Test
    public void lerDMI3() throws IOException {
        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,3508"),
                new StrPair("valTotNotaFiscalSaida", "539776,76"),
                new StrPair( "valImpostoImportacao", "48267,28"),
                new StrPair(               "valIPI", "30050,51"),
                new StrPair(               "valPIS", "7730,53"),
                new StrPair(            "valCOFINS", "35523,63"),
                new StrPair(       "valOutrasTaxas", "27578,79"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };

        PDF dmi = PDF.dmi(fileDMI3);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }

    @Test
    public void validarDMIComArquivoValido() throws IOException {
        PDF dmi = PDF.dmi(fileDMI1);
        assertThat(dmi.isValido()).isTrue();
    }

    @Test
    public void validarDMIComArquivoInvalido() throws IOException {
        PDF dmi = PDF.dmi(fileDI);
        assertThat(dmi.isValido()).isFalse();
    }
}
