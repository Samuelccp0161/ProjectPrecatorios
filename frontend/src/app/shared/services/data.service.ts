import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private logado = false
  private contaGrafica = ''
  private numeroProcesso: string = '';
  

  getContaGrafica(): string {
    return this.contaGrafica;
  }
  isLogado(): boolean {
    return this.logado;
  }

  setContaGrafica(conta: string) {
    this.contaGrafica = conta;
  }
  setLogado(value: boolean) {
    this.logado = value;
  }

  setNumeroProcesso(numero: string) {
    this.numeroProcesso = numero;
  }

  getNumeroProcesso() {
    return this.numeroProcesso;
  }
}
