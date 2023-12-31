package br.gov.al.sefaz.precatorio.service.impl;

import br.gov.al.sefaz.precatorio.exception.ContaGraficaInvalidaException;
import br.gov.al.sefaz.precatorio.exception.LoginException;
import br.gov.al.sefaz.precatorio.selenium.Navegador;
import br.gov.al.sefaz.precatorio.service.PrecatorioService;
import br.gov.al.sefaz.precatorio.service.TributarioImportacaoService;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class TributarioImportacaoServiceImpl implements TributarioImportacaoService {

    @Override
    public void irParaContaGrafica(String contaGrafica) {
        if (PrecatorioService.naoLogou())
            throw new LoginException("Usuário não está logado!");

        irParaTributarioImportacao();
        preencherContaGrafica(contaGrafica);

        if (naoEntrouNaContaGrafica())
            throw new ContaGraficaInvalidaException("Conta gráfica inválida!");
    }

    private boolean naoEntrouNaContaGrafica() {
        try {
            return Navegador.obterDriver().findElement(By.linkText("Cadastrar")) == null;
        } catch (NoSuchElementException ignore) {
            return true;
        }
    }

    private void preencherContaGrafica(String contaGrafica) {
        Navegador.obterDriver().findElement(By.id("contaGrafica")).sendKeys(contaGrafica, Keys.ENTER);
    }

    private void irParaTributarioImportacao() {
        Navegador.obterDriver().findElement(By.id("mi_0_5")).click();
        Navegador.obterDriver().findElement(By.id("mi_0_6")).click();
    }

    @Override
    public void preencherDados(Map<String, String> dados) {
        if (PrecatorioService.naoLogou())
            throw new LoginException("Usuário não está logado!");
        if (naoEntrouNaContaGrafica())
            throw new ContaGraficaInvalidaException("Conta gráfica não informada!");

        Navegador.obterDriver().findElement(By.id("dataDMI")).sendKeys(dataAtual());

        for (var pair : dados.entrySet()) {
            String id = pair.getKey();
            String valor = pair.getValue();

            Navegador.obterDriver().findElement(By.id(id)).sendKeys(valor);
        }


        zerarIcmsARecolher();
        clicarNoCampoNotaFiscal();
    }

    private String dataAtual() {
        LocalDate date = LocalDate.now();

        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("ddMMuuuu");

        return formatterData.format(date);
    }

    private void clicarNoCampoNotaFiscal() {
        Navegador.obterDriver().findElement(By.id("numNotaFiscal")).click();
    }

    private void zerarIcmsARecolher() {
        Navegador.obterDriver().findElement(By.id("valPorcentagemICMSRecolher")).clear();
    }

    public void close() {
        Navegador.fecharDriver();
    }
}
