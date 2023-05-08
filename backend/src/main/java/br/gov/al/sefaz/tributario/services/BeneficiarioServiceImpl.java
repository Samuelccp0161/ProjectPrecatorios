package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.exception.LoginException;
import br.gov.al.sefaz.tributario.exception.ProcessoInvalidoException;
import br.gov.al.sefaz.tributario.selenium.FabricaDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BeneficiarioServiceImpl implements BeneficiarioService {
    private static final Logger logger = LoggerFactory.getLogger(BeneficiarioService.class);

    @Override
    public void inserirNumeroProcesso(String numeroProcesso) {
        if (PrecatorioService.naoLogou())
            throw new LoginException("Usuário não está logado!");

        irParaContaBeneficiario();
        inserirNumero(numeroProcesso);

        if (naoEstaEmBeneficiario())
            throw new ProcessoInvalidoException("Processo inválido!");
    }

    private void irParaContaBeneficiario() {
        FabricaDriver.obterDriver().findElement(By.id("mi_0_4")).click();
    }

    private void inserirNumero(String numeroProcesso) {
        WebElement campoProcesso = FabricaDriver.obterDriver().findElement(By.id("numProcesso"));
        campoProcesso.sendKeys(numeroProcesso, Keys.ENTER);
    }

    private boolean naoEstaEmBeneficiario(){
        try {
            return FabricaDriver.obterDriver().findElement(By.id("matricula")) == null;
        } catch (NoSuchElementException ignore) {
            return true;
        }
    }

    @Override
    public void preencherDados(Map<String, String> dados) {
        if (PrecatorioService.naoLogou())
            throw new LoginException("Usuário não está logado!");
        if (naoEstaEmBeneficiario())
            throw new ProcessoInvalidoException("Processo não informado!");

        for (var pair : dados.entrySet()) {
            String id = pair.getKey();
            String valor = pair.getValue();

            logger.info("Tentando inserir no campo com id '" + id + "' o valor '" + valor + "'.");

            FabricaDriver.obterDriver().findElement(By.id(id)).sendKeys(valor);
        }
    }

    public void close() {
        FabricaDriver.close();
    }
}
