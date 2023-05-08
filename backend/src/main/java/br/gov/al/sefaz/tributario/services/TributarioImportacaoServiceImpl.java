package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.exception.ContaGraficaInvalidaException;
import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.selenium.FabricaDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.stereotype.Service;

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
            return FabricaDriver.obterDriver().findElement(By.linkText("Cadastrar")) == null;
        } catch (NoSuchElementException ignore) {
            return true;
        }
    }

    private void preencherContaGrafica(String contaGrafica) {
        FabricaDriver.obterDriver().findElement(By.id("contaGrafica")).sendKeys(contaGrafica, Keys.ENTER);
    }

    private void irParaTributarioImportacao() {
        FabricaDriver.obterDriver().findElement(By.id("mi_0_5")).click();
        FabricaDriver.obterDriver().findElement(By.id("mi_0_6")).click();
    }

    @Override
    public void preencherDados(Map<String, String> dados) {
        if (PrecatorioService.naoLogou())
            throw new LoginException("Usuário não está logado!");
        if (naoEntrouNaContaGrafica())
            throw new ContaGraficaInvalidaException("Conta gráfica não informada!");

        for (var pair : dados.entrySet()) {
            String id = pair.getKey();
            String valor = pair.getValue();

            FabricaDriver.obterDriver().findElement(By.id(id)).sendKeys(valor);
        }

        zerarIcmsARecolher();
        clicarNoCampoNotaFiscal();
    }

    private void clicarNoCampoNotaFiscal() {
        FabricaDriver.obterDriver().findElement(By.id("numNotaFiscal")).click();
    }

    private void zerarIcmsARecolher() {
        FabricaDriver.obterDriver().findElement(By.id("valPorcentagemICMSRecolher")).clear();
    }

    public void close() {
        FabricaDriver.close();
    }
}
