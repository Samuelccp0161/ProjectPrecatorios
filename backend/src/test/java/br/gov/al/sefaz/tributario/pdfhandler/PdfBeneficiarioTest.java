package br.gov.al.sefaz.tributario.pdfhandler;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PdfBeneficiarioTest {
//    @Test
//    void sada() {
//        final File fileQuitacao01 = new File("src/test/resources/pdfs/quitacao_01.pdf");
//        System.out.println(LerPdf.extraiTextoDoPDF(fileQuitacao01));
//        assertTrue(LerPdf.validadorPdf(fileQuitacao01));
//    }
    @Test
    void lerPDF01() {
        final File fileQuitacao01 = new File("src/test/resources/pdfs/beneficiario/quitacao_01.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "MATRICULA", "3255"),
                new StrPair("NOME", "ITAMAR ALVES LEITE"),
                new StrPair( "CPF", "26020033449"),
                new StrPair(               "VALOR DE FACE BRUTO", "41666,67"),
                new StrPair(               "VALOR DE FACE HONORÁRIOS", "8333,33"),
                new StrPair(            "VALOR DE FACE DE CONDENAÇÃO", "50000,00"),
                new StrPair(       "VALOR DE ACORDO BRUTO", "12500,00"),
                new StrPair(         "VALOR DE ACORDO HONORÁRIOS", "2500,00"),
                new StrPair(          "VALOR DO ACORDO TOTAL DA CONDENAÇÃO", "15000,00"),
                new StrPair(          "VALOR DO AL PREVIDÊNCIA", "1375,00"),
                new StrPair(          "VALOR DO IRPF", "2190,02"),
                new StrPair(          "VALOR LÍQUIDO PARA O AUTOR", "8934,99")
        };

//        String dd = LerPdf.ff(fileDM1);
        Map<String, String> quitacaoTabela = PdfBeneficiario.extrairDados(fileQuitacao01);

        assertThat(quitacaoTabela).containsOnly(valoresEsperados);
    }
    @Test
    void lerPDF02() {
        final File fileQuitacao02 = new File("src/test/resources/pdfs/beneficiario/quitacao_02.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "MATRICULA", "1563"),
                new StrPair("NOME", "IRACY LIMA SAMPAIO"),
                new StrPair( "CPF", "02630621472"),
                new StrPair(               "VALOR DE FACE BRUTO", "334782,61"),
                new StrPair(               "VALOR DE FACE HONORÁRIOS", "50217,39"),
                new StrPair(            "VALOR DE FACE DE CONDENAÇÃO", "385000,00"),
                new StrPair(       "VALOR DE ACORDO BRUTO", "100434,78"),
                new StrPair(         "VALOR DE ACORDO HONORÁRIOS", "15065,22"),
                new StrPair(          "VALOR DO ACORDO TOTAL DA CONDENAÇÃO", "115500,00"),
                new StrPair(          "VALOR DO AL PREVIDÊNCIA", "11047,83"),
                new StrPair(          "VALOR DO IRPF", "0,00"),
                new StrPair(          "VALOR LÍQUIDO PARA O AUTOR", "89386,96")

        };
        Map<String, String> quitacaoTabela = PdfBeneficiario.extrairDados(fileQuitacao02);

        assertThat(quitacaoTabela).containsOnly(valoresEsperados);
    }
    @Test
    void lerPDF03() {
        final File fileQuitacao03 = new File("src/test/resources/pdfs/beneficiario/quitacao_03.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "MATRICULA", "55851"),
                new StrPair("NOME", "DELMA MARIA COSTA DE AZEVEDO PANTALEAO"),
                new StrPair( "CPF", "38442418415"),
                new StrPair(               "VALOR DE FACE BRUTO", "22222,22"),
                new StrPair(               "VALOR DE FACE HONORÁRIOS", "4444,44"),
                new StrPair(            "VALOR DE FACE DE CONDENAÇÃO", "26666,66"),
                new StrPair(       "VALOR DE ACORDO BRUTO", "6666,67"),
                new StrPair(         "VALOR DE ACORDO HONORÁRIOS", "1333,33"),
                new StrPair(          "VALOR DO ACORDO TOTAL DA CONDENAÇÃO", "8000,00"),
                new StrPair(          "VALOR DO AL PREVIDÊNCIA", "733,33"),
                new StrPair(          "VALOR DO IRPF", "762,31"),
                new StrPair(          "VALOR LÍQUIDO PARA O AUTOR", "5171,03")

        };
        Map<String, String> quitacaoTabela = PdfBeneficiario.extrairDados(fileQuitacao03);

        assertThat(quitacaoTabela).containsOnly(valoresEsperados);
    }
    @Test
    void lerPDF04() {
        final File fileQuitacao04 = new File("src/test/resources/pdfs/beneficiario/quitacao_04.pdf");

        StrPair[] valoresEsperados = {
                new StrPair(  "MATRICULA", "2576"),
                new StrPair("NOME", "IRACY PEREIRA TAVARES"),
                new StrPair( "CPF", "11140593404"),
                new StrPair(               "VALOR DE FACE BRUTO", "521739,13"),
                new StrPair(               "VALOR DE FACE HONORÁRIOS", "78260,87"),
                new StrPair(            "VALOR DE FACE DE CONDENAÇÃO", "600000,00"),
                new StrPair(       "VALOR DE ACORDO BRUTO", "156521,74"),
                new StrPair(         "VALOR DE ACORDO HONORÁRIOS", "23478,26"),
                new StrPair(          "VALOR DO ACORDO TOTAL DA CONDENAÇÃO", "180000,00"),
                new StrPair(          "VALOR DO AL PREVIDÊNCIA", "17217,39"),
                new StrPair(          "VALOR DO IRPF", "37439,34"),
                new StrPair(          "VALOR LÍQUIDO PARA O AUTOR", "101865,01")

        };
        Map<String, String> quitacaoTabela = PdfBeneficiario.extrairDados(fileQuitacao04);

        assertThat(quitacaoTabela).containsOnly(valoresEsperados);
    }
}
