import { DataService } from './../shared/services/data.service';
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

    let form = new FormData();

    form.append('usuario', this.forms.value.usuario)
    form.append('senha', this.forms.value.senha)

    this.loginService.logar(form).subscribe(
      res => {
        if (res.message == 'Usuário logado com sucesso!') {
          this.data.setLogado(true);
          this.router.navigateByUrl('/grafica');
        } 
        else
          this.errorMessage = res.message;
        
        this.loading = false;
      }
    );
  }
}
