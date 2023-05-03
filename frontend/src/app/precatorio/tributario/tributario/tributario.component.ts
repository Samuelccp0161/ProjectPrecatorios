import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {DataService} from "../../../shared/services/data.service";
import {Router} from "@angular/router";
import {TributarioService} from "./service/tributario.service";

@Component({
  selector: 'app-tributario',
  templateUrl: './tributario.component.html',
  styleUrls: ['./tributario.component.scss']
})
export class TributarioComponent implements OnInit {
  errorMessage = '';
  loading = false;
  contaGrafica!: FormControl;

  constructor(
      private data: DataService,
      private tributarioService: TributarioService,
      private router: Router
  ) {
    // if (!data.isLogado())
    //     router.navigateByUrl('/login');
    data.setContaGrafica('');
  }

  ngOnInit(): void {
    this.contaGrafica = new FormControl('', Validators.required);
  }

  submitConta(): void {
    this.loading = true;
    const conta = this.contaGrafica.value;

    this.tributarioService.entrar(conta).subscribe({
      next: () => this.salvarContaEIrParaUpload(conta),
      error: (erro) => this.salvarErroOuVoltarParaLogin(erro)
    }).add(() => this.loading = false);
  }

  salvarContaEIrParaUpload(conta: string): void {
    this.data.setContaGrafica(conta);
    this.router.navigateByUrl("/precatorio/tributario-upload");
  }

  salvarErroOuVoltarParaLogin(erro: any): void {
    this.errorMessage = erro.error.message;

    if (this.errorMessage != 'Conta gráfica inválida!') {
      this.router.navigateByUrl("/login");
    }
  }
}
