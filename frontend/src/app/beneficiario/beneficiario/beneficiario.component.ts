import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {DataService} from "../../shared/services/data.service";
import {Router} from "@angular/router";
import {BeneficiarioService} from "./service/beneficiario.service";

@Component({
  selector: 'app-beneficiario',
  templateUrl: './beneficiario.component.html',
  styleUrls: ['./beneficiario.component.scss']
})
export class BeneficiarioComponent implements OnInit {

  errorMessage = '';
  loading = false;
  numeroProcesso!: FormControl;

  constructor(
      private data: DataService,
      private beneficiarioService: BeneficiarioService,
      private router: Router
  ) {
    // if (!data.isLogado())
    //     router.navigateByUrl('/login');
    data.setContaGrafica('');
  }

  ngOnInit(): void {
    this.numeroProcesso = new FormControl('', Validators.required);
  }

  submitProcesso(): void {
    this.loading = true;
    const numero = this.numeroProcesso.value;

    this.beneficiarioService.entrar(numero).subscribe({
      next: () => this.salvarNumeroEIrParaUpload(numero),
      error: (erro) => this.salvarErroOuVoltarParaLogin(erro)
    }).add(() => this.loading = false);
  }

  salvarNumeroEIrParaUpload(numero: string): void {
    this.data.setNumeroProcesso(numero);
    this.router.navigateByUrl("/precatorio/beneficiario-upload");
  }

  salvarErroOuVoltarParaLogin(erro: any): void {
    this.errorMessage = erro.error.message;

    if (this.errorMessage != 'Processo inválido!') {
      this.router.navigateByUrl("/login");
    }
  }

}
