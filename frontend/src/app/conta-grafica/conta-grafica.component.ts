import { DataService } from './../shared/services/data.service';
import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { ContaGraficaService } from './service/conta-grafica.service';

@Component({
  selector: 'app-conta-grafica',
  templateUrl: './conta-grafica.component.html',
  styleUrls: ['./conta-grafica.component.scss']
})
export class ContaGraficaComponent implements OnInit {
  errorMessage = '';
  loading = false;
  contaGrafica!: FormControl;

  constructor(
    private data: DataService,
    private contaService: ContaGraficaService,
    private router: Router
  ) { 
    if (!data.isLogado()) 
      router.navigateByUrl('/login');
    data.setContaGrafica('');
  }

  ngOnInit(): void {
    this.contaGrafica = new FormControl('', Validators.required);
  }

  submitConta(): void {
    this.loading = true;
    const contaValue = this.contaGrafica.value;
    let form = new FormData();
    form.append('conta-grafica', contaValue);

    this.contaService.entrar(form).subscribe(
      res => {
        if (res.message == "Entrada da conta gráfica '"+ contaValue +"' bem sucedida!") {
          this.data.setContaGrafica(contaValue);
          this.router.navigateByUrl("/upload");
        }
        else if (res.message == "Conta gráfica inválida!") {
          this.errorMessage = res.message;
        }
        else {
          console.log(res);
          
          this.router.navigateByUrl("/login");
        }

        this.loading = false;
      }
    );
  }
}
