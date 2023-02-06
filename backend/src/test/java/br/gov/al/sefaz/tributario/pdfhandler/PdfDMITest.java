package br.gov.al.sefaz.tributario.pdfhandler;

import br.gov.al.sefaz.tributario.pdfhandler.exception.PdfInvalidoException;
import br.gov.al.sefaz.tributario.pdfhandler.util.Area;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @Test
    public void lerDMI8() throws IOException {
        final File fileDMI8 = new File("src/test/resources/pdfs/DMI_8.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,2091"),
                new StrPair("valTotNotaFiscalSaida", "608580,00"),
                new StrPair( "valImpostoImportacao", "40005,89"),
                new StrPair(               "valIPI", "0,00"),
                new StrPair(               "valPIS", "8751,29"),
                new StrPair(            "valCOFINS", "40214,25"),
                new StrPair(       "valOutrasTaxas", "5081,58"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };

        PDF dmi = PDF.dmi(fileDMI8);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDMI9() throws IOException {
        final File fileDMI9 = new File("src/test/resources/pdfs/DMI_9.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,2031"),
                new StrPair("valTotNotaFiscalSaida", "255239,93"),
                new StrPair( "valImpostoImportacao", "0,00"),
                new StrPair(               "valIPI", "13460,42"),
                new StrPair(               "valPIS", "4348,75"),
                new StrPair(            "valCOFINS", "19983,55"),
                new StrPair(       "valOutrasTaxas", "154,23"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };
        PDF dmi = PDF.dmi(fileDMI9);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDMI10() throws IOException {
        final File fileDMI10 = new File("src/test/resources/pdfs/DMI_10.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,3160"),
                new StrPair("valTotNotaFiscalSaida", "2942,03"),
                new StrPair( "valImpostoImportacao", "344,96"),
                new StrPair(               "valIPI", "73,50"),
                new StrPair(               "valPIS", "59,79"),
                new StrPair(            "valCOFINS", "275,40"),
                new StrPair(       "valOutrasTaxas", "154,23"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };
        PDF dmi = PDF.dmi(fileDMI10);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }
    @Test @Disabled //FOI TROCADO O "." PELA ",".
    public void lerDMI11() throws IOException {
        final File fileDMI11 = new File("src/test/resources/pdfs/DMI_11.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,3204"),
                new StrPair("valTotNotaFiscalSaida", "325377,60"),
                new StrPair( "valImpostoImportacao", "31226,47"),
                new StrPair(               "valIPI", "0,00"),
                new StrPair(               "valPIS", "4098,47"),
                new StrPair(            "valCOFINS", "18833,47"),
                new StrPair(       "valOutrasTaxas", "2567,53"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };
        PDF dmi = PDF.dmi(fileDMI11);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDMI12() throws IOException {
        final File fileDMI12 = new File("src/test/resources/pdfs/DMI_12.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,3183"),
                new StrPair("valTotNotaFiscalSaida", "23429,13"),
                new StrPair( "valImpostoImportacao", "1608,25"),
                new StrPair(               "valIPI", "0,00"),
                new StrPair(               "valPIS", "301,55"),
                new StrPair(            "valCOFINS", "1385,68"),
                new StrPair(       "valOutrasTaxas", "154,29"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };
        PDF dmi = PDF.dmi(fileDMI12);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDMI13() throws IOException {
        final File fileDMI13 = new File("src/test/resources/pdfs/DMI_13.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,3406"),
                new StrPair("valTotNotaFiscalSaida", "3197141,03"),
                new StrPair( "valImpostoImportacao", "315022,63"),
                new StrPair(               "valIPI", "244011,28"),
                new StrPair(               "valPIS", "45940,80"),
                new StrPair(            "valCOFINS", "211108,92"),
                new StrPair(       "valOutrasTaxas", "3688,25"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };
        PDF dmi = PDF.dmi(fileDMI13);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDMI14() throws IOException {
        final File fileDMI14 = new File("src/test/resources/pdfs/DMI_14.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,3183"),
                new StrPair("valTotNotaFiscalSaida", "216678,12"),
                new StrPair( "valImpostoImportacao", "26688,08"),
                new StrPair(               "valIPI", "10408,35"),
                new StrPair(               "valPIS", "2802,24"),
                new StrPair(            "valCOFINS", "12877,00"),
                new StrPair(       "valOutrasTaxas", "1489,85"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };
        PDF dmi = PDF.dmi(fileDMI14);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDMI15() throws IOException {
        final File fileDMI15 = new File("src/test/resources/pdfs/DMI_15.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,2464"),
                new StrPair("valTotNotaFiscalSaida", "236542,25"),
                new StrPair( "valImpostoImportacao", "25373,61"),
                new StrPair(               "valIPI", "0,00"),
                new StrPair(               "valPIS", "3218,34"),
                new StrPair(            "valCOFINS", "14955,32"),
                new StrPair(       "valOutrasTaxas", "2366,30"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };
        PDF dmi = PDF.dmi(fileDMI15);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }
    @Test @Disabled("NOME DO VALOR DA TAXA ALTERADO")
    public void lerDMI16() throws IOException {
        final File fileDMI16 = new File("src/test/resources/pdfs/DMI_16.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,2091"),
                new StrPair("valTotNotaFiscalSaida", "285624,64"),
                new StrPair( "valImpostoImportacao", "24858,99"),
                new StrPair(               "valIPI", "0,00"),
                new StrPair(               "valPIS", "4078,43"),
                new StrPair(            "valCOFINS", "20683,46"),
                new StrPair(       "valOutrasTaxas", "2259,96"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };
        PDF dmi = PDF.dmi(fileDMI16);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }
    @Test
    public void lerDMI17() throws IOException {
        final File fileDMI17 = new File("src/test/resources/pdfs/DMI_17.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "valMoedaEstrangeira", "5,2031"),
                new StrPair("valTotNotaFiscalSaida", "255239,93"),
                new StrPair( "valImpostoImportacao", "0,00"),
                new StrPair(               "valIPI", "13460,42"),
                new StrPair(               "valPIS", "4348,75"),
                new StrPair(            "valCOFINS", "19983,55"),
                new StrPair(       "valOutrasTaxas", "154,23"),
                new StrPair(         "valCapatazia", "0,00"),
                new StrPair(          "valAliquota", "4"),
        };
        PDF dmi = PDF.dmi(fileDMI17);
        Map<String, String> dmiTabela = dmi.getTabela();

        assertThat(dmiTabela).containsOnly(valoresEsperados);
    }

    @Test
    public void validarDMIComArquivoInvalido() throws IOException {
        final File DMINaoValido = new File("src/test/resources/pdfs/DI_1.pdf");

        assertThatThrownBy(() -> PDF.dmi(DMINaoValido)).isInstanceOf(PdfInvalidoException.class)
                .hasMessageContaining("O Arquivo enviado não é um 'DMI' válido");
    }
}
