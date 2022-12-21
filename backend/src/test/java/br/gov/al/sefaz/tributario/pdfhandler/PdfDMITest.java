package br.gov.al.sefaz.tributario.pdfhandler;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PdfDMITest {
    @Test
    public void lerDMI1() throws IOException {
        final File fileDMI1 = new File("src/test/resources/pdfs/DMI_1.pdf");

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
        final File fileDMI2 = new File("src/test/resources/pdfs/DMI_2.pdf");

        StrPair[] valoresEsperados = {
            new StrPair(  "valMoedaEstrangeira", "5,1411"),
            new StrPair("valTotNotaFiscalSaida", "803286,46"),
            new StrPair( "valImpostoImportacao", "0,00"),
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
        final File fileDMI3 = new File("src/test/resources/pdfs/DMI_3.pdf");

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
    public void lerDMI4() throws IOException {
        final File fileDMI4 = new File("src/test/resources/pdfs/DMI_4.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,3204"),
                new StrPair("valTotNotaFiscalSaida", "269185,58"),
                new StrPair( "valImpostoImportacao", "25005,21"),
                new StrPair(               "valIPI", "19960,88"),
                new StrPair(               "valPIS", "3416,52"),
                new StrPair(            "valCOFINS", "15729,10"),
                new StrPair(       "valOutrasTaxas", "1155,93"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };

        PDF dmi = PDF.dmi(fileDMI4);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDMI5() throws IOException {
        final File fileDMI5 = new File("src/test/resources/pdfs/DMI_5.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,1948"),
                new StrPair("valTotNotaFiscalSaida", "400017,28"),
                new StrPair( "valImpostoImportacao", "26757,91"),
                new StrPair(               "valIPI", "9928,30"),
                new StrPair(               "valPIS", "5853,29"),
                new StrPair(            "valCOFINS", "26897,27"),
                new StrPair(       "valOutrasTaxas", "4913,40"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };

        PDF dmi = PDF.dmi(fileDMI5);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDMI6() throws IOException {
        final File fileDMI6 = new File("src/test/resources/pdfs/DMI_6.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,3204"),
                new StrPair("valTotNotaFiscalSaida", "112664,07"),
                new StrPair( "valImpostoImportacao", "0,00"),
                new StrPair(               "valIPI", "2563,38"),
                new StrPair(               "valPIS", "1656,34"),
                new StrPair(            "valCOFINS", "7611,29"),
                new StrPair(       "valOutrasTaxas", "572,57"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "12"),
        };

        PDF dmi = PDF.dmi(fileDMI6);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDMI7() throws IOException {
        final File fileDMI7 = new File("src/test/resources/pdfs/DMI_7.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,3406"),
                new StrPair("valTotNotaFiscalSaida", "89883,15"),
                new StrPair( "valImpostoImportacao", "9134,73"),
                new StrPair(               "valIPI", "4717,07"),
                new StrPair(               "valPIS", "1332,15"),
                new StrPair(            "valCOFINS", "6121,54"),
                new StrPair(       "valOutrasTaxas", "1546,69"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };

        PDF dmi = PDF.dmi(fileDMI7);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }
@Test @Disabled("Necessario que o tamanho e posição da tabela sejam descobertos dinamicamente")
public void lerDMI8() throws IOException {
    final File fileDMI8 = new File("src/test/resources/pdfs/DMI_8.pdf");

    StrPair[] valoresEsperados = {
            new StrPair(  "valMoedaEstrangeira", "5,2806"),
            new StrPair("valTotNotaFiscalSaida", "112664,07"),
            new StrPair( "valImpostoImportacao", "28665,63"),
            new StrPair(               "valIPI", "0,00"),
            new StrPair(               "valPIS", "3762,37"),
            new StrPair(            "valCOFINS", "17288,96"),
            new StrPair(       "valOutrasTaxas", "9852,44"),
            new StrPair(         "valCapatazia", "0,00"),
            new StrPair(          "valAliquota", "18"),
    };

    PDF dmi = PDF.dmi(fileDMI8);
    Map<String, String> dmiTabela = dmi.getTabela();

    assertThat(dmiTabela).containsOnly(valoresEsperados);
}


    @Test
    public void validarDMIComArquivoValido() throws IOException {
        final File fileDMI1 = new File("src/test/resources/pdfs/DMI_1.pdf");

        PDF dmi = PDF.dmi(fileDMI1);
        assertThat(dmi.isValido()).isTrue();
    }

    @Test
    public void validarDMIComArquivoInvalido() throws IOException {
        final File DMINaoValido = new File("src/test/resources/pdfs/DI_1.pdf");

        PDF dmi = PDF.dmi(DMINaoValido);
        assertThat(dmi.isValido()).isFalse();
    }
}
