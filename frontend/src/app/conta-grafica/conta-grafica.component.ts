import {DataService} from '../shared/services/data.service';
import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {Router} from '@angular/router';

import {ContaGraficaService} from './service/conta-grafica.service';

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
        const conta = this.contaGrafica.value;

        this.contaService.entrar(conta).subscribe({
            next: () => {
                this.data.setContaGrafica(conta);
                this.router.navigateByUrl("/upload");
            },
            error: (err) => {
                this.errorMessage = err.error.message;
                if (this.errorMessage != 'Conta gráfica inválida!') {
                    this.router.navigateByUrl("/login");
                }
            },
        }).add(() => this.loading = false);
    }
}
