import { DataService } from '../shared/services/data.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { LoginService } from './service/login.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    errorMessage = '';
    loading = false;

    forms!: FormGroup;

    constructor(
        private data: DataService,
        private loginService: LoginService,
        private formBuilder: FormBuilder,
        private router: Router
    ) {
        data.setLogado(false);
        data.setContaGrafica('');
    }

    ngOnInit(): void {
        this.forms = this.formBuilder.group(
            {
                usuario: ['', Validators.required],
                senha: ['', Validators.required],
            }
        )
    }

    onSubmit(): void {
        this.loading = true;

        let usuario = this.forms.value.usuario;
        let senha = this.forms.value.senha;

        this.loginService.logar(usuario, senha).subscribe({
                next: () => {
                    this.data.setLogado(true);
                    this.router.navigateByUrl('/precatorio');
                },
                error: (err) => this.errorMessage = this.parseError(err)
            }
        ).add(() => this.loading = false);
    }

    parseError(err: any) {
        if (err.status == 404 || err.status == 504)
            return "Não foi possível conectar!";

        return err.error.message;
    }
}
